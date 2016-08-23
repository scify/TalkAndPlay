/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.talkandplay.utils;

import static com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler.BUFFER_SIZE;
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

/**
 * Checks if the jar should be updated
 *
 * @author christina
 */
public class Updater {

    private Properties properties;

    private static final String TMP_DIRECTORY = System.getProperty("user.dir") + File.separator + "tmp/";
    private static final String ZIP_FILE = "talkandplay.zip";
    private static final String PROPERTIES_FILE = "properties.xml";
    private static final String UPDATER_FILE = "updater.jar";
    private static final String JAR_FILE = "talkandplay.jar";

    public Updater() {
        properties = new Properties();
    }

    public void run() {
        if (hasUpdate()) {
            downloadZip();
            extractZip();
            startUpdater();
            closeApp();
        }
    }

    private void downloadZip() {
        try {
            URL url = new URL(properties.getZipUrl());
            System.out.println(properties.getZipUrl());
            File file = new File(TMP_DIRECTORY + ZIP_FILE);
            FileUtils.copyURLToFile(url, file);

        } catch (MalformedURLException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void extractZip() {
        try {
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(TMP_DIRECTORY + ZIP_FILE));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            while (entry != null) {
                String filePath = TMP_DIRECTORY + entry.getName();
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

    private void closeApp() {
        System.exit(0);
    }

    private void startUpdater() {
        try {
            Process proc = Runtime.getRuntime().exec("java -jar " + TMP_DIRECTORY + UPDATER_FILE);
            System.out.println("java -jar " + TMP_DIRECTORY + UPDATER_FILE);
            InputStream in = proc.getInputStream();
            InputStream err = proc.getErrorStream();
        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean hasUpdate() {
        try {
            URL url = new URL(properties.getVersionFileUrl());
            File file = new File(TMP_DIRECTORY + PROPERTIES_FILE);

            FileUtils.copyURLToFile(url, file);

            if (!file.exists() || file.isDirectory()) {
                return false;
            }

            SAXBuilder builder = new SAXBuilder();
            Document configurationFile = (Document) builder.build(file);

            String version = configurationFile.getRootElement().getChildText("version");

            if (!properties.getVersion().equals(version)) {
                return true;
            }

            return false;

        } catch (MalformedURLException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (JDOMException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
