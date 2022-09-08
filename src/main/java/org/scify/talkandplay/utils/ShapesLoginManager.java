package org.scify.talkandplay.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import io.sentry.Sentry;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ShapesLoginManager extends LoginManager {

    protected Gson g;
    protected final ResourceManager rm;
    protected String token;



    protected String signInUrl;
    protected String signUpUrl;

    protected String XShapesKey;
    protected boolean inShapesMode;
    protected Logger logger;

    public ShapesLoginManager() {
        g = new Gson();
        rm = ResourceManager.getInstance();
        logger = org.apache.log4j.Logger.getLogger(ShapesLoginManager.class);
        loadPropertiesFile();
    }

    protected void loadPropertiesFile() {
        File shapesPropertiesFile = new File("shapes.mode");
        if (shapesPropertiesFile.exists()) {
            inShapesMode = true;
            logger.info("Starting in SHAPES mode");
            /*try {
                BufferedReader reader = new BufferedReader(new FileReader(shapesPropertiesFile));
                String line = reader.readLine().trim();
                String[] s = line.split("signIn=");
                signInUrl = s[1].trim();

                line = reader.readLine().trim();
                s = line.split("signUp=");
                signUpUrl = s[1].trim();

                line = reader.readLine().trim();
                s = line.split("X-Shapes-Key=");
                XShapesKey = s[1].trim();

            } catch (Exception e) {
                String msg = "Error in shapes.properties file: (" + e.getMessage() + ")";
                logger.error(msg);
                Sentry.capture(msg);
            }*/
        } else {
            logger.info("Starting in NORMAL mode");
            inShapesMode = false;
        }
    }

    public boolean isInShapesMode() {
        return inShapesMode;
    }

    public String getToken() {
        return token;
    }

    @Override
    public OperationMessage signIn(String email, String password) {
        String emailCleaned = email.trim();
        String passwordCleaned = password.trim();
        if (emailCleaned.length() <= 3 || !emailCleaned.contains("@") || passwordCleaned.length() < 4) {
            return new OperationMessage(false, rm.getTextOfXMLTag("wrongCredentialsMsg"));
        } else {
            try {
                Unirest.setTimeouts(100000, 100000);
                HttpResponse<String> response = Unirest.post(signInUrl)
                        .header("Accept", "application/json")
                        .header("X-Shapes-Key", XShapesKey)
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
                logger.error(e);
                return new OperationMessage(false, rm.getTextOfXMLTag("cannotAccessServerMessage"));
            }
        }
    }

    @Override
    public OperationMessage signUp(String email, String password, String confirmPassword) {
        String emailCleaned = email.trim();
        String passwordCleaned = password.trim();
        String confirmPasswordCleaned = confirmPassword.trim();
        if (emailCleaned.length() <= 3 || !emailCleaned.contains("@") || !passwordCleaned.equals(confirmPasswordCleaned) || passwordCleaned.length() < 4) {
            return new OperationMessage(false, rm.getTextOfXMLTag("wrongCredentialsMsg"));
        } else {
            try {
                Unirest.setTimeouts(0, 0);
                HttpResponse<String> response = Unirest.post(signUpUrl)
                        .header("Accept", "application/json")
                        .header("X-Shapes-Key", XShapesKey)
                        .header("Content-Type", "application/json")
                        .body("{ \"email\" : \"" + emailCleaned + "\", \"password\": \"" + passwordCleaned + "\" }")
                        .asString();
                JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();
                int count = jsonObject.get("count").getAsInt();
                Unirest.shutdown();
                if (count == 1)
                    return new OperationMessage(true);
                else {
                    String error = jsonObject.get("error").getAsString().trim();
                    if (error.equals("User email already exists!")) {
                        return new OperationMessage(false, rm.getTextOfXMLTag("accountAlreadyExists"));
                    } else {
                        return new OperationMessage(false, rm.getTextOfXMLTag("wrongCredentialsMsg"));
                    }
                }
            } catch (Exception e) {
                logger.error(e);
                return new OperationMessage(false, rm.getTextOfXMLTag("cannotAccessServerMessage"));
            }
        }

    }

    public void setXShapesKey(String XShapesKey) {
        this.XShapesKey = XShapesKey;
    }
    public void setSignInUrl(String signInUrl) {
        this.signInUrl = signInUrl;
    }

    public void setSignUpUrl(String signUpUrl) {
        this.signUpUrl = signUpUrl;
    }
}
