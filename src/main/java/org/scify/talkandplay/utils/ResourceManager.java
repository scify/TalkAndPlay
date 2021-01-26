package org.scify.talkandplay.utils;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.scify.talkandplay.gui.ApplicationLauncher;

import javax.swing.*;
import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ResourceManager {

    static Logger logger = Logger.getLogger(ApplicationLauncher.class);

    private final URL JarResources = getClass().getResource("/org/scify/talkandplay/resources");
    private final File dataResources = new File("resources");
    private List<String> languages;
    private int selectedLanguageIndex;
    private List<File> languageDirs;
    private File defaultDir;

    private List<HashMap<String, String>> languageTexts;

    protected static ResourceManager instance;

    public static ResourceManager getInstance() {
        if (instance == null)
            instance = new ResourceManager();
        return instance;
    }

    public ResourceManager() {
        File[] filesInDir = dataResources.listFiles();
        languages = new ArrayList<>();
        languageDirs = new ArrayList<>();
        languageTexts = new ArrayList<>();
        for (int i = 0; i < filesInDir.length; i++) {
            File file = filesInDir[i];
            if (file.isDirectory()) {
                String dirName = file.getName();
                if (dirName.equals("default")) {
                    defaultDir = file;
                } else {
                    languages.add(dirName);
                    languageDirs.add(file);
                    HashMap<String, String> texts = new HashMap<String, String>();
                    languageTexts.add(texts);
                    File languageXMLFile = new File(file,"language.xml");
                    SAXBuilder builder = new SAXBuilder();
                    try {
                        Document languageDoc = builder.build(languageXMLFile);
                        List languageTexts = languageDoc.getRootElement().getChildren();
                        Iterator<Element> iter = languageTexts.iterator();
                        while (iter.hasNext()) {
                            Element e = iter.next();
                            texts.put(e.getName(), e.getValue());
                        }
                    } catch (Exception e) {
                        logger.error("language.xml file for language " + dirName + " not found");
                    }

                }
            }
        }
        selectedLanguageIndex = 0;
    }

    public void setLanguage(int languageIndex) {
        selectedLanguageIndex = languageIndex;
    }

    public int getSelectedLanguageIndex() {
        return selectedLanguageIndex;
    }

    public String getSelectedLanguage() {
        return languages.get(selectedLanguageIndex);
    }

    public int getNumberOfAvailableLanguages() {
        return languages.size();
    }

    protected Image getFullPathImage(String resource) {
        File resourceFile = new File(resource);
        return this.getImage(resourceFile);
    }

    protected ImageIcon getFullPathImageIcon(String resource) {
        File resourceFile = new File(resource);
        return this.getImageIcon(resourceFile);
    }

    protected Image getJarImage(String resource) {
        File resourceFile = new File(JarResources.getFile(), resource);
        return this.getImage(resourceFile);
    }

    protected ImageIcon getJarImageIcon(String resource) {
        File resourceFile = new File(JarResources.getFile(), resource);
        return this.getImageIcon(resourceFile);
    }

    protected Image getImageOfLanguage(String resource) {
        return this.getImageOfLanguage(resource, this.selectedLanguageIndex);
    }

    public Image getImageOfLanguage(String resource, int languageIndex) {
        File resourceFile = new File(languageDirs.get(languageIndex), resource);
        if (!resourceFile.exists())
            resourceFile = new File(defaultDir, resource);
        return this.getImage(resourceFile);
    }

    protected ImageIcon getImageIconOfLanguage(String resource) {
        return this.getImageIconOfLanguage(resource, this.selectedLanguageIndex);
    }

    public ImageIcon getImageIconOfLanguage(String resource, int languageIndex) {
        File resourceFile = new File(languageDirs.get(languageIndex), resource);
        if (!resourceFile.exists())
            resourceFile = new File(defaultDir, resource);
        return this.getImageIcon(resourceFile);
    }

    protected Image getImage(File resourceFile) {
        ImageIcon imageIcon = this.getImageIcon(resourceFile);
        if (imageIcon == null)
            return null;
        else
            return imageIcon.getImage();
    }

    protected ImageIcon getImageIcon(File resourceFile) {
        try {
            URL url = resourceFile.toURI().toURL();
            return (new ImageIcon(url));
        } catch (MalformedURLException e) {
            logger.error("UI resource " + resourceFile + " not found");
            return null;
        }
    }

    public Image getImage(ImageResource imageResource) {
        if (imageResource == null)
            return null;
        else
            return getImage(imageResource.path, imageResource.getResourceType());
    }

    public Image getImage(String path, ResourceType resourceType) {
        Image ret = null;
        switch (resourceType) {
            case FROM_JAR:
                ret = getJarImage(path);
                break;
            case FROM_RESOURCES:
                ret =  getImageOfLanguage(path);
                break;
            case FULL_PATH:
                ret = getFullPathImage(path);
                break;
            default:
                break;
        }
        return ret;
    }

    public ImageIcon getImageIcon(ImageResource imageResource) {
        return getImageIcon(imageResource.path, imageResource.getResourceType());
    }

    public ImageIcon getImageIcon(String path, ResourceType resourceType) {
        ImageIcon ret = null;
        switch (resourceType) {
            case FROM_JAR:
                ret = getJarImageIcon(path);
                break;
            case FROM_RESOURCES:
                ret =  getImageIconOfLanguage(path);
                break;
            case FULL_PATH:
                ret = getFullPathImageIcon(path);
                break;
            default:
                break;
        }
        return ret;
    }

    public File getSound(String path, int languageIndex) {
        return getFile(path, languageIndex);
    }

    public File getSound(SoundResource soundResource) {
        if (soundResource == null)
            return null;
        else
            return getSound(soundResource.getPath(), soundResource.getResourceType());
    }

    public File getSound(String path, ResourceType resourceType) {
        return getFileOfResource(path, resourceType);
    }

    public String getTextOfXMLTag(String tag) {
        return languageTexts.get(selectedLanguageIndex).get(tag);
    }

    public String decodeTextIfRequired(String string) {
        String s = string.trim();
        if (s.startsWith("{{") && s.endsWith("}}")) {
            s = s.substring(2);
            s = s.substring(0, s.length()-2);
            s = s.trim();
            s = getTextOfXMLTag(s);
        }
        return s;
    }

    public File getFileOfResource (String path, ResourceType resourceType) {
        File ret = null;
        switch (resourceType) {
            case FROM_JAR:
                ret = new File(JarResources.getFile(), path);
                break;
            case FROM_RESOURCES:
                ret =  getFile(path, selectedLanguageIndex);
                break;
            case FULL_PATH:
                ret = new File(path);
                break;
            default:
                break;
        }
        return ret;
    }

    public File getFile(String path, int languageIndex) {
        File ret = new File(languageDirs.get(languageIndex), path);
        if (!ret.exists()) {
            ret = new File(defaultDir, path);
            if (!ret.exists())
                ret = null;
        }
        return ret;
    }
}
