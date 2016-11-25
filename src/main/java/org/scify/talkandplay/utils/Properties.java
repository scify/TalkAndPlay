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

import java.io.File;
import java.net.URLDecoder;
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

    public String getTmpFolder() {
        return System.getProperty("java.io.tmpdir");
    }

    public void setTmpFolder(String tmpFolder) {
        this.tmpFolder = tmpFolder;
    }

    public String getApplicationFolder() {
        return applicationFolder;
    }


}
