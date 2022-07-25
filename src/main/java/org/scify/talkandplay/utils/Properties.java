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
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import io.sentry.Sentry;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * import org.jdom.input.SAXBuilder;
 * <p>
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

    private String environment;

    private String restAPIParametersUrl;

    private String restAPIStorageUrl;

    private Document configurationFile;

    protected static Properties instance;

    protected static ParametersFromRestAPI parametersFromRestAPI;
    static Logger logger = Logger.getLogger(Properties.class);

    public static Properties getInstance() {
        if (instance == null)
            instance = new Properties();
        return instance;
    }

    private Properties() {
        try {
            applicationFolder = URLDecoder.decode(System.getProperty("user.dir"), "UTF-8");
            logger.debug("Application folder: " + applicationFolder);
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

    private void parseXML() {
        Element properties = configurationFile.getRootElement();

        setVersion(properties.getChildText("version"));
        setVersionFileUrl(properties.getChildText("versionFileUrl"));
        setPropertiesFile(properties.getChildText("propertiesFile"));
        setZipUrl(properties.getChildText("zipUrl"));
        environment = properties.getChildText("environment");
        restAPIParametersUrl = properties.getChildText("restAPIParametersUrl");
        restAPIStorageUrl = properties.getChildText("restAPIStorageUrl");
        parametersFromRestAPI = null;
    }

    public ParametersFromRestAPI getParametersFromRestAPI() {
        if (parametersFromRestAPI != null)
            return parametersFromRestAPI;

        try {
            Unirest.setTimeouts(100000, 100000);
            HttpResponse<String> response = Unirest.get(restAPIParametersUrl)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .queryString("version", version)
                    .asString();
            JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();
            String shapesSignInUrl = jsonObject.get("shapes_auth_url_login").getAsString();
            String shapesSignUpUrl = jsonObject.get("shapes_auth_url_register").getAsString();
            String shapesKey = jsonObject.get("shapes_x_key").getAsString();
            String sentryDSN = jsonObject.get("sentry_dsn").getAsString();
            String firebaseUrl = jsonObject.get("firebase_url").getAsString();

            List<Announcement> announcements = new ArrayList<>();
            if (jsonObject.has("announcements")) {
                JsonArray announcementsJson = jsonObject.get("announcements").getAsJsonArray();
                for (JsonElement announcementElement: announcementsJson) {
                    JsonObject announcementJson = announcementElement.getAsJsonObject();
                    int severity = announcementJson.get("severity").getAsInt();
                    String type = announcementJson.get("type").getAsString();
                    String updated_at = announcementJson.get("updated_at").getAsString();
                    Announcement announcement = new Announcement(severity, type, updated_at);
                    JsonArray translationsJson = announcementJson.get("translations").getAsJsonArray();
                    for (JsonElement translation : translationsJson) {
                        JsonObject translationJson = translation.getAsJsonObject();
                        String title = "";
                        JsonElement value = translationJson.get("title");
                        if (!value.isJsonNull())
                            title = value.getAsString();

                        String message = "";
                        value = translationJson.get("message");
                        if (!value.isJsonNull())
                            message = value.getAsString();

                        String link = "";
                        value = translationJson.get("link");
                        if (!value.isJsonNull())
                            link = value.getAsString();

                        String language =  "";
                        value = translationJson.get("language");
                        if (!value.isJsonNull())
                            language = value.getAsString();

                        AnnouncementTranslation announcementTranslation = new AnnouncementTranslation(title, message, link, language);
                        announcement.addAnnouncementTranslation(language, announcementTranslation);
                    }
                    announcements.add(announcement);
                }
            }

            ParametersFromRestAPI ret = new ParametersFromRestAPI(shapesSignInUrl, shapesSignUpUrl, shapesKey, sentryDSN, firebaseUrl, announcements);
            parametersFromRestAPI = ret;
            return ret;
        } catch (Exception e) {
            logger.error("Retrieving parameters from rest API failed with error: (" + e.getMessage() + ")");
            return null;
        }
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

    public String getEnvironment() {
        return environment;
    }

    public String getRestAPIStorageUrl() {
        return restAPIStorageUrl;
    }
}
