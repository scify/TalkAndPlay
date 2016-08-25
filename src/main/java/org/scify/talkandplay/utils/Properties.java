/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.talkandplay.utils;

import java.io.File;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * import org.jdom.input.SAXBuilder;
 *
 * /**
 * Holds the properties of the application
 *
 * @author christina
 */
public class Properties {

    private String version;
    private String versionFileUrl;
    private String zipUrl;
    private String tmpFolder;
    private String zipFile;
    private String propertiesFile;
    private String updater;
    private String jar;
    private String jarPath;

    private Document configurationFile;

    public Properties() {
        try {
            /*
              If you run java -jar when not in the same dir with the jar,
            the jar won't read any external files. So the path should be the absolute one.
            However, when developing (i.e using Netbeans) the absolute path is not the correct one, so the default one is used
             */
            jarPath = (new File(Properties.class.getProtectionDomain().getCodeSource().getLocation().getPath())).getParentFile().getAbsolutePath();
            jarPath = jarPath.replace("\\", "/");
            System.out.println(jarPath + ", file separator " + File.separator);
            String absolutePath = jarPath + File.separator + "properties.xml";
            File file = new File(absolutePath);
            if (!file.exists()) {
                file = new File("properties.xml");
                jarPath = "properties.xml";
            }

            SAXBuilder builder = new SAXBuilder();
            configurationFile = (Document) builder.build(file);

            parseXML();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private void parseXML() throws Exception {
        Element properties = configurationFile.getRootElement();

        System.out.println("versionFileUrl " + properties.getChildText("versionFileUrl"));

        setVersion(properties.getChildText("version"));
        setVersionFileUrl(properties.getChildText("versionFileUrl"));
        setZipUrl(properties.getChildText("zipUrl"));
        setTmpFolder(properties.getChildText("tmpFolder"));
        setZipFile(properties.getChildText("zipFile"));
        setPropertiesFile(properties.getChildText("propertiesFile"));
        setUpdater(properties.getChildText("updater"));
        setJar(properties.getChildText("jar"));
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionFileUrl() {
        return versionFileUrl;
    }

    public void setVersionFileUrl(String versionFileUrl) {
        this.versionFileUrl = versionFileUrl;
    }

    public String getZipUrl() {
        return zipUrl;
    }

    public void setZipUrl(String zipUrl) {
        this.zipUrl = zipUrl;
    }

    public String getZipFile() {
        return zipFile;
    }

    public void setZipFile(String zipFile) {
        this.zipFile = zipFile;
    }

    public String getPropertiesFile() {
        return propertiesFile;
    }

    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getJar() {
        return jar;
    }

    public void setJar(String jar) {
        this.jar = jar;
    }

    public String getTmpFolder() {
        return tmpFolder;
    }

    public void setTmpFolder(String tmpFolder) {
        this.tmpFolder = tmpFolder;
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

}
