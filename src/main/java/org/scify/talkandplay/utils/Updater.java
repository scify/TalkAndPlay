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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.scify.talkandplay.gui.UpdaterFrame;

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

    public Updater() {
        properties = new Properties();

    }

    public void run() {
        System.out.println("URL: " + properties.getZipUrl());
        System.out.println("Zip file: " + properties.getZipFile());
        if (hasUpdate()) {
            showFrame();
            downloadZip();
            ArrayList<String> tempfilesThatWillReplaceTheExisting = extractZip();
            overrideFiles(tempfilesThatWillReplaceTheExisting);
            closeApp();
        }
    }

    private void showFrame() {
        UpdaterFrame updaterFrame = new UpdaterFrame(properties.getVersion());
        updaterFrame.setLocationRelativeTo(null);
        updaterFrame.setVisible(true);
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
            File file = new File(properties.getTmpFolder() + File.separator + properties.getZipFile());
            FileUtils.copyURLToFile(url, file);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ArrayList<String> extractZip() {
        ArrayList<String> tempfilesThatWillReplaceTheExisting= new ArrayList<>();
        try {
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(properties.getTmpFolder() + File.separator + properties.getZipFile()));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            while (entry != null) {
                String filePath = properties.getTmpFolder() + File.separator + entry.getName();
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
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
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

            for (String source_file: tempfilesThatWillReplaceTheExisting) {
                System.out.println("Overriding: " + source_file);
                File source = new File(source_file);
                String sourceFileName =source.getName();
                //one folder above is the destination file...
                File dest = new File(this.properties.getApplicationFolder()+ File.separator+sourceFileName);
                if (source.isFile()) {
                    try {
                        FileUtils.deleteQuietly(dest);
                        FileUtils.copyFile(source, dest);
                    } catch (IOException e) {
                        e.printStackTrace();
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

                System.out.println(version + "," + properties.getVersion());

                if (!properties.getVersion().equals(version)) {
                    hasUpdate = true;
                }
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.out.println("Has update? " + hasUpdate);
            return hasUpdate;
        }
    }
}
