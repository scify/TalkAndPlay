/**
* Copyright 2016 SciFY
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.scify.talkandplay.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private String zipFilePath;

    public Updater() {
        properties = new Properties();
        int index = properties.getZipUrl().lastIndexOf('/');
        this.zipFilePath = properties.getTmpFolder() + File.separator + properties.getZipUrl().substring(index + 1);
    }

    public void run() {
        System.out.println("Current user can write to Application directory? " + FileSystemUtils.canWriteToApplicationDir());
        System.out.println("URL: " + properties.getZipUrl());
        System.out.println("Tmp folder: " + properties.getTmpFolder());
        if (hasUpdate()) {
            if(readyForUpdate())
                doUpdate();
            else
                showWindowsAdminFrame();
        }
    }

    public void doUpdate() {
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

/*    private void deleteTmpFolder() {
        try {
            File dir = new File(this.properties.getApplicationFolder() + File.separator + properties.getTmpFolder());
            System.out.println("Deleting tmp folder, exists " + dir.exists());
            if (dir.exists() && dir.isDirectory()) {
                FileUtils.cleanDirectory(dir);
                FileUtils.deleteDirectory(dir);
            }
        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

    private void downloadZip() {
        try {
            URL url = new URL(properties.getZipUrl());
            System.out.println("Tmp folder: " + properties.getTmpFolder());
            File file = new File(zipFilePath);
            FileUtils.copyURLToFile(url, file);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
            Sentry.capture(ex);
        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
            Sentry.capture(ex);
        }
    }

    private ArrayList<String> extractZip() {
        ArrayList<String> tempfilesThatWillReplaceTheExisting= new ArrayList<>();
        try {
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
        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
            Sentry.capture(ex);
            return null;
        }
        return tempfilesThatWillReplaceTheExisting;
    }

    private void overrideFiles(ArrayList<String> tempfilesThatWillReplaceTheExisting) {
        if (tempfilesThatWillReplaceTheExisting!=null)
        {
            //all the source files are inside the tmp folder defined inside the properties.xml
            //This process runs in the root folder
            //all the destination files are on same folder as well (the root folder).
            System.out.println("Application Folder: " + this.properties.getApplicationFolder());
            for (String source_file: tempfilesThatWillReplaceTheExisting) {
                File source = new File(source_file);
                String targetFileName = this.properties.getApplicationFolder() +
                        File.separator + StringUtils.substringAfter(source_file, properties.getTmpFolder());
                File dest = new File(targetFileName);
                System.out.println("Overriding: " + targetFileName + "\twith:\t" + source_file);

                if (source.isFile()) {
                    try {
                        FileUtils.deleteQuietly(dest);
                        FileUtils.copyFile(source, dest);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Sentry.capture(e);
                    }
                }
            }
        }

    }

 /*   private void startUpdater() {
        try {
            System.out.println("java -jar " + properties.getJarPath() + "/" + properties.getTmpFolder() + "/" + properties.getUpdater());
            Process proc = Runtime.getRuntime().exec("java -jar " + properties.getJarPath() + "/" + properties.getTmpFolder() + "/" + properties.getUpdater());
            InputStream in = proc.getInputStream();
            InputStream err = proc.getErrorStream();
        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

    private void closeApp() {
        System.exit(0);
    }

    private boolean hasUpdate() {
        boolean hasUpdate = false;
        try {
            URL url = new URL(properties.getVersionFileUrl());
            File file = new File(properties.getTmpFolder() + File.separator + properties.getPropertiesFile());

            FileUtils.copyURLToFile(url, file);

            if (file.exists() && !file.isDirectory()) {

                SAXBuilder builder = new SAXBuilder();
                Document configurationFile =  builder.build(file);

                String version = configurationFile.getRootElement().getChildText("version");

                System.out.println("Remote version:\t" + version);
                System.out.println("Local version:\t"+ properties.getVersion());

                if (!properties.getVersion().equals(version)) {
                    hasUpdate = true;
                }
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
            Sentry.capture(ex);
        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
            Sentry.capture(ex);
        } catch (JDOMException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
            Sentry.capture(ex);
        } finally {
            System.out.println("Has update? " + hasUpdate);
            return hasUpdate;
        }
    }
}
