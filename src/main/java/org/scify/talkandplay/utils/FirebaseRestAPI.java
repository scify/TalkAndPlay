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
                logger.info(firebaseUrl);
            } catch (Exception e) {
                logger.error("Error in firebase.properties file");
                Sentry.capture("Error in firebase.properties file: (" + e.getMessage() + ")");
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
            if (shapesToken.length() > 0)
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
            if (shapesToken.length() > 0)
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

    /*@Override
    public OperationMessage signIn(String email, String password) {
        String emailCleaned = email.trim();
        String passwordCleaned = password.trim();
        if (emailCleaned.length() <= 3 || !emailCleaned.contains("@") || passwordCleaned.length() < 4) {
            return new OperationMessage(false, rm.getTextOfXMLTag("wrongCredentialsMsg"));
        } else {
            try {
                Unirest.setTimeouts(100000, 100000);
                HttpResponse<String> response = Unirest.post("https://kubernetes.pasiphae.eu/shapes/asapa/auth/login")
                        .header("Accept", "application/json")
                        .header("X-Shapes-Key", "7Msbb3w^SjVG%j")
                        .header("Content-Type", "application/json")
                        .body("{ \"email\" : \"" + emailCleaned + "\", \"password\": \"" + passwordCleaned + "\" }")
                        .asString();
                JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();
                int count = jsonObject.get("count").getAsInt();
                Unirest.shutdown();
                if (count == 1) {
                    token = jsonObject.get("items").getAsJsonArray().get(0).getAsJsonObject().get("token").getAsString();
                    return new OperationMessage(true);
                } else
                    return new OperationMessage(false, rm.getTextOfXMLTag("wrongCredentialsMsg"));

            } catch (Exception e) {
                Logger.getLogger(FirebaseRestAPI.class.getName()).log(Level.SEVERE, null, e);
                return new OperationMessage(false, rm.getTextOfXMLTag("cannotAccessServerMessage"));
            }
        }
    }*/
}
