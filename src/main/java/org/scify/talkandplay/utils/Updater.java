/**
 * Copyright 2016 SciFY
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scify.talkandplay.utils;

import io.sentry.Sentry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.scify.talkandplay.gui.UpdateErrorMessageFrame;
import org.scify.talkandplay.gui.UpdaterFrame;
import org.scify.talkandplay.gui.WindowsAdminMessageFrame;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * UPDATE STEPS
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Checks if the jar should be updated.
 * Show "updating" frame.
 * Download the zip containing the new jar and the new properties.xml.
 * Unzip it to a tmp folder.
 * Override files to the installation folder.
 * Close the current app.
 *
 * @author christina
 */
public class Updater {

    private Properties properties;
    UpdaterFrame updaterFrame;
    WindowsAdminMessageFrame windowsAdminMessageFrame;
    UpdateErrorMessageFrame updateErrorMessageFrame;
    private String zipFilePath;
    static Logger logger = Logger.getLogger(Updater.class);

    public Updater() {
        properties = Properties.getInstance();
        int index = properties.getZipUrl().lastIndexOf('/');
        this.zipFilePath = properties.getTmpFolder() + File.separator + properties.getZipUrl().substring(index + 1);
    }

    public boolean run() {
        logger.debug("Current user can write to Application directory? " + FileSystemUtils.canWriteToApplicationDir());
        logger.debug("URL: " + properties.getZipUrl());
        logger.debug("Tmp folder: " + properties.getTmpFolder());
        try {
            if (readyForUpdate()) {
                doUpdate();
                return true;
            }
            else {
                showWindowsAdminFrame();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureMessage(e.getMessage());
            // show frame that something went wrong and OK button to continue to the app
            if (updaterFrame != null)
                updaterFrame.dispose();
            showUpdateErrorMessageFrame();
            return false;
        }
    }

    public void doUpdate() {
        showFrame();
        Thread thread = new Thread() {
            public void run  () {
                try {
                    URL url = new URL(properties.getZipUrl());
                    logger.debug("Tmp folder: " + properties.getTmpFolder());
                    File file = new File(zipFilePath);
                    FileUtils.copyURLToFile(url, file);
                    ArrayList<String> tempfilesThatWillReplaceTheExisting = extractZip();
                    overrideFiles(tempfilesThatWillReplaceTheExisting);
                    closeApp();
                } catch (Exception e) {
                    e.printStackTrace();
                    Sentry.captureMessage(e.getMessage());
                    showUpdateErrorMessageFrame();
                }
            }
        };
        thread.start();
    }

    public static boolean readyForUpdate() {
        return FileSystemUtils.canWriteToApplicationDir();
    }

    private void showFrame() {
        updaterFrame = new UpdaterFrame(properties.getVersion());
        updaterFrame.setLocationRelativeTo(null);
        updaterFrame.setVisible(true);
        updaterFrame.revalidate();
        updaterFrame.repaint();
    }

    private void showWindowsAdminFrame() {
        windowsAdminMessageFrame = new WindowsAdminMessageFrame();
        windowsAdminMessageFrame.setLocationRelativeTo(null);
        windowsAdminMessageFrame.setVisible(true);
        windowsAdminMessageFrame.setAlwaysOnTop(true);
    }

    public void showUpdateErrorMessageFrame() {
        updateErrorMessageFrame = new UpdateErrorMessageFrame();
        updateErrorMessageFrame.setLocationRelativeTo(null);
        updateErrorMessageFrame.setVisible(true);
        updateErrorMessageFrame.setAlwaysOnTop(true);
    }

    private ArrayList<String> extractZip() throws IOException {
        ArrayList<String> tempfilesThatWillReplaceTheExisting = new ArrayList<>();

        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = properties.getTmpFolder() + entry.getName();
            logger.debug("Extracting: " + filePath);
            if (!entry.isDirectory()) {
                tempfilesThatWillReplaceTheExisting.add(filePath);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                byte[] bytesIn = new byte[1024];
                int read = 0;
                while ((read = zipIn.read(bytesIn)) != -1) {
                    bos.write(bytesIn, 0, read);
                }
                bos.close();
            } else {
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();

        return tempfilesThatWillReplaceTheExisting;
    }

    private void overrideFiles(ArrayList<String> tempfilesThatWillReplaceTheExisting) throws IOException {
        if (tempfilesThatWillReplaceTheExisting != null) {
            //all the source files are inside the tmp folder defined inside the properties.xml
            //This process runs in the root folder
            //all the destination files are on same folder as well (the root folder).
            logger.debug("Application Folder: " + this.properties.getApplicationFolder());
            for (String source_file : tempfilesThatWillReplaceTheExisting) {
                File source = new File(source_file);
                String targetFileName = this.properties.getApplicationFolder() +
                        File.separator + StringUtils.substringAfter(source_file, properties.getTmpFolder());
                File dest = new File(targetFileName);
                logger.debug("Overriding: " + targetFileName + "\twith:\t" + source_file);

                if (source.isFile()) {
                    FileUtils.deleteQuietly(dest);
                    FileUtils.copyFile(source, dest);
                }
            }
        }

    }

    private void closeApp() {
        System.exit(0);
    }

    public boolean hasUpdate() {
        boolean hasUpdate = false;

        try {
            URL url = new URL(properties.getVersionFileUrl());
            File file = new File(properties.getTmpFolder() + File.separator + properties.getPropertiesFile());

            FileUtils.copyURLToFile(url, file);

            if (file.exists() && !file.isDirectory()) {

                SAXBuilder builder = new SAXBuilder();
                Document configurationFile = builder.build(file);

                String serverVersion = configurationFile.getRootElement().getChildText("version");

                logger.info("Remote version(" + properties.getVersionFileUrl() + "):\t" + serverVersion);
                logger.info("Local version:\t" + properties.getVersion());

                if (Double.parseDouble(properties.getVersion()) < Double.parseDouble(serverVersion)) {
                    hasUpdate = true;
                }
            }
            return hasUpdate;
        } catch (Exception e) {
            logger.info("Could not connect to updater");
            return false;
        }
    }
}
