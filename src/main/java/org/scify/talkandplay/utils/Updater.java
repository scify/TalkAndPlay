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

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import io.sentry.Sentry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.scify.talkandplay.gui.UpdateErrorMessageFrame;
import org.scify.talkandplay.gui.UpdaterFrame;
import org.scify.talkandplay.gui.WindowsAdminMessageFrame;

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

    public Updater() {
        properties = Properties.getInstance();
        int index = properties.getZipUrl().lastIndexOf('/');
        this.zipFilePath = properties.getTmpFolder() + File.separator + properties.getZipUrl().substring(index + 1);
    }

    public void run() {
        System.out.println("Current user can write to Application directory? " + FileSystemUtils.canWriteToApplicationDir());
        System.out.println("URL: " + properties.getZipUrl());
        System.out.println("Tmp folder: " + properties.getTmpFolder());
        try {
            if (hasUpdate()) {
                if (readyForUpdate())
                    doUpdate();
                else
                    showWindowsAdminFrame();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.capture(e);
            // show frame that something went wrong and OK button to continue to the app
            if (updaterFrame != null)
                updaterFrame.dispose();
            showUpdateErrorMessageFrame();
        }
    }

    public void doUpdate() throws IOException {
        showFrame();
        downloadZip();
        ArrayList<String> tempfilesThatWillReplaceTheExisting = extractZip();
        overrideFiles(tempfilesThatWillReplaceTheExisting);
        closeApp();
    }

    public static boolean readyForUpdate() {
        return FileSystemUtils.canWriteToApplicationDir();
    }

    private void showFrame() {
        updaterFrame = new UpdaterFrame(properties.getVersion());
        updaterFrame.setLocationRelativeTo(null);
        updaterFrame.setVisible(true);
    }

    private void showWindowsAdminFrame() {
        windowsAdminMessageFrame = new WindowsAdminMessageFrame();
        windowsAdminMessageFrame.setLocationRelativeTo(null);
        windowsAdminMessageFrame.setVisible(true);
        windowsAdminMessageFrame.setAlwaysOnTop(true);
    }

    private void showUpdateErrorMessageFrame() {
        updateErrorMessageFrame = new UpdateErrorMessageFrame();
        updateErrorMessageFrame.setLocationRelativeTo(null);
        updateErrorMessageFrame.setVisible(true);
        updateErrorMessageFrame.setAlwaysOnTop(true);
    }

    private void downloadZip() throws IOException {
        URL url = new URL(properties.getZipUrl());
        System.out.println("Tmp folder: " + properties.getTmpFolder());
        File file = new File(zipFilePath);
        FileUtils.copyURLToFile(url, file);
    }

    private ArrayList<String> extractZip() throws IOException {
        ArrayList<String> tempfilesThatWillReplaceTheExisting = new ArrayList<>();

        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = properties.getTmpFolder() + entry.getName();
            System.out.println("Extracting: " + filePath);
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
            System.out.println("Application Folder: " + this.properties.getApplicationFolder());
            for (String source_file : tempfilesThatWillReplaceTheExisting) {
                File source = new File(source_file);
                String targetFileName = this.properties.getApplicationFolder() +
                        File.separator + StringUtils.substringAfter(source_file, properties.getTmpFolder());
                File dest = new File(targetFileName);
                System.out.println("Overriding: " + targetFileName + "\twith:\t" + source_file);

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

    private boolean hasUpdate() throws IOException, JDOMException {
        boolean hasUpdate = false;

        URL url = new URL(properties.getVersionFileUrl());
        File file = new File(properties.getTmpFolder() + File.separator + properties.getPropertiesFile());

        FileUtils.copyURLToFile(url, file);

        if (file.exists() && !file.isDirectory()) {

            SAXBuilder builder = new SAXBuilder();
            Document configurationFile = builder.build(file);

            String version = configurationFile.getRootElement().getChildText("version");

            System.out.println("Remote version:\t" + version);
            System.out.println("Local version:\t" + properties.getVersion());

            if (!properties.getVersion().equals(version)) {
                hasUpdate = true;
            }
        }

        return hasUpdate;
    }
}
