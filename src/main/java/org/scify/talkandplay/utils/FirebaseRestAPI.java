package org.scify.talkandplay.utils;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import io.sentry.Sentry;
import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FirebaseRestAPI {

    protected Gson g;
    protected final ResourceManager rm;
    protected String firebaseUrl;
    protected String shapesToken;
    protected static FirebaseRestAPI instance;
    protected Logger logger = Logger.getLogger(FirebaseRestAPI.class);
    protected static String SHAPESLOGINEVENT = "\"eventType\": \"SHAPES_LOGIN\", ";
    protected static String SHAPESLOGOUTEVENT = "\"eventType\": \"SHAPES_LOGOUT\", ";
    protected static String COMMUNICATIONCATEGORYEVENT = "\"eventType\": \"COMMUNICATION_CATEGORY_EVENT\", ";
    protected static String GAMESELECTIONEVENT = "\"eventType\": \"GAME_SELECTION_EVENT\", ";


    public FirebaseRestAPI() {
        g = new Gson();
        rm = ResourceManager.getInstance();
        loadPropertiesFile();
    }

    public static FirebaseRestAPI getInstance() {
        if (instance == null) {
            instance = new FirebaseRestAPI();
        }
        return instance;
    }

    protected void loadPropertiesFile() {
        File fireBaseProperties = new File("firebase.properties");
        if (fireBaseProperties.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fireBaseProperties));
                String line = reader.readLine().trim();
                String[] s = line.split("firebaseUrl=");
                firebaseUrl = s[1].trim();
            } catch (Exception e) {
                String msg = "Error in firebase.properties file: (" + e.getMessage() + ")";
                logger.error(msg);
                Sentry.capture(msg);
            }
        } else {
            String message = "firebase.properties file missing!";
            logger.error(message);
            Sentry.capture(message);
        }

        LoginManager loginManager = TalkAndPlayProfileConfiguration.getInstance().getLoginManager();
        if (loginManager instanceof ShapesLoginManager) {
            shapesToken = ((ShapesLoginManager) loginManager).getToken();
        }
    }

    public void postShapesLogin() {
        if (firebaseUrl.length() > 0) {
            String jsonBody = "{";
            jsonBody += "\"language\": \"" + rm.getSelectedLanguage() + "\", ";
            if (shapesToken != null && shapesToken.length() > 0)
                jsonBody += "\"authToken\": \"" + shapesToken + "\", ";
            jsonBody += SHAPESLOGINEVENT;
            jsonBody += "\"timestamp\":{\".sv\": \"timestamp\"}}";
            try {
                Unirest.setTimeouts(100000, 100000);
                HttpResponse<String> response = Unirest.post(firebaseUrl)
                        .header("Content-Type", "application/json")
                        .body(jsonBody).asString();
            } catch (Exception e) {
                logger.error(e);
                Sentry.capture(e);
            }
        }
    }

    public void postShapesLogout() {
        if (firebaseUrl.length() > 0) {
            String jsonBody = "{";
            jsonBody += "\"language\": \"" + rm.getSelectedLanguage() + "\", ";
            if (shapesToken != null && shapesToken.length() > 0)
                jsonBody += "\"authToken\": \"" + shapesToken + "\", ";
            jsonBody += SHAPESLOGOUTEVENT;
            jsonBody += "\"timestamp\":{\".sv\": \"timestamp\"}}";
            try {
                Unirest.setTimeouts(100000, 100000);
                HttpResponse<String> response = Unirest.post(firebaseUrl)
                        .header("Content-Type", "application/json")
                        .body(jsonBody).asString();
            } catch (Exception e) {
                logger.error(e);
                Sentry.capture(e);
            }
        }
    }

    public void postCommunicationCategorySelection(String categoryName, String parentCategoryName) {
        String jsonBody = "{";
        jsonBody += "\"language\": \"" + rm.getSelectedLanguage() + "\", ";
        if (shapesToken != null && shapesToken.length() > 0)
            jsonBody += "\"authToken\": \"" + shapesToken + "\", ";
        jsonBody += COMMUNICATIONCATEGORYEVENT;
        jsonBody += "\"categoryName\": \"" + categoryName + "\", ";
        jsonBody += "\"parentCategoryName\": \"" + parentCategoryName + "\", ";
        jsonBody += "\"timestamp\":{\".sv\": \"timestamp\"}}";

        try {
            Unirest.setTimeouts(100000, 100000);
            HttpResponse<String> response = Unirest.post(firebaseUrl)
                    .header("Content-Type", "application/json")
                    .body(jsonBody).asString();
        } catch (Exception e) {
            logger.error(e);
            Sentry.capture(e);
        }
    }

    public void postGameSelection(String gameName, String gameType, long duration, int mistakes) {
        String jsonBody = "{";
        jsonBody += "\"language\": \"" + rm.getSelectedLanguage() + "\", ";
        if (shapesToken != null && shapesToken.length() > 0)
            jsonBody += "\"authToken\": \"" + shapesToken + "\", ";
        jsonBody += GAMESELECTIONEVENT;
        jsonBody += "\"gameName\": \"" + gameName + "\", ";
        jsonBody += "\"gameType\": \"" + gameType + "\", ";
        jsonBody += "\"duration\": " + duration + ", ";
        jsonBody += "\"mistakes\": " + mistakes + ", ";
        jsonBody += "\"timestamp\":{\".sv\": \"timestamp\"}}";

        try {
            Unirest.setTimeouts(100000, 100000);
            HttpResponse<String> response = Unirest.post(firebaseUrl)
                    .header("Content-Type", "application/json")
                    .body(jsonBody).asString();
        } catch (Exception e) {
            logger.error(e);
            Sentry.capture(e);
        }
    }

}
