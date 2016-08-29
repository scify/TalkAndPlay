/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.talkandplay.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.scify.talkandplay.gui.MainFrame;
import org.scify.talkandplay.gui.UpdaterFrame;

/**
 * Checks if the jar should be updated. Download the zip containing the new jar
 * and the updater. Unzip it to a tmp folder Start the updater jar. Close the
 * current app. Delete the tmp folder
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
            extractZip();
            startUpdater();
            closeApp();
        }
        deleteTmpFolder();
    }

    private void showFrame() {
        UpdaterFrame updaterFrame = new UpdaterFrame();
        updaterFrame.setLocationRelativeTo(null);
        updaterFrame.setVisible(true);
    }

    private void deleteTmpFolder() {
        try {
            File dir = new File(properties.getJarPath() + File.separator + properties.getTmpFolder());
            System.out.println("Deleting tmp folder, exists " + dir.exists());
            if (dir.exists() && dir.isDirectory()) {
                FileUtils.cleanDirectory(dir);
                FileUtils.deleteDirectory(dir);
            }
        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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

    private void extractZip() {
        try {
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(properties.getTmpFolder() + File.separator + properties.getZipFile()));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            while (entry != null) {
                String filePath = properties.getTmpFolder() + File.separator + entry.getName();
                if (!entry.isDirectory()) {
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
        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startUpdater() {
        try {
            System.out.println("java -jar " + properties.getJarPath() + "/" + properties.getTmpFolder() + "/" + properties.getUpdater());
            Process proc = Runtime.getRuntime().exec("java -jar " + properties.getJarPath() + "/" + properties.getTmpFolder() + "/" + properties.getUpdater());
            InputStream in = proc.getInputStream();
            InputStream err = proc.getErrorStream();
        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
                Document configurationFile = (Document) builder.build(file);

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
