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

import java.io.File;
import java.net.URLDecoder;

import io.sentry.Sentry;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

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
    private String propertiesFile;
    private String applicationFolder;

    private Document configurationFile;

    public Properties() {
        try {
            String encodedApplicationFolder = (new File(Properties.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())).getParentFile().getAbsolutePath();
            applicationFolder = URLDecoder.decode(encodedApplicationFolder, "UTF-8");

            File file = new File(applicationFolder, "properties.xml");
            if (!file.exists()) {
                file = new File("properties.xml");
            }
            SAXBuilder builder = new SAXBuilder();
            configurationFile = builder.build(file);
            parseXML();
            //PropertiesConfigurator is used to configure logger from properties file
            PropertyConfigurator.configure("log4j.properties");

            //Log in console in and log file
            logger.debug("Log4j appender configuration is successful !!");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            Sentry.capture(e);
        }
    }

    private void parseXML() throws Exception {
        Element properties = configurationFile.getRootElement();

        setVersion(properties.getChildText("version"));
        setVersionFileUrl(properties.getChildText("versionFileUrl"));
        setPropertiesFile(properties.getChildText("propertiesFile"));
        setZipUrl(properties.getChildText("zipUrl"));
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

    static Logger logger = Logger.getLogger(Properties.class);

    public void setZipUrl(String zipUrl) {
        this.zipUrl = zipUrl;
    }

    public String getPropertiesFile() {
        return propertiesFile;
    }

    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    public String getTmpFolder() {
        return System.getProperty("java.io.tmpdir") + File.separator + "TalkAndPlay" + File.separator;
    }

    public String getApplicationFolder() {
        return applicationFolder;
    }

}
