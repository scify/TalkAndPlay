package org.scify.talkandplay.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.apache.log4j.Logger;

public class ShapesLoginManager extends LoginManager {

    protected Gson g;
    protected final ResourceManager rm;
    protected String token;
    protected Logger logger;

    public ShapesLoginManager() {
        g = new Gson();
        rm = ResourceManager.getInstance();
        logger = org.apache.log4j.Logger.getLogger(ShapesLoginManager.class);
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
                HttpResponse<String> response = Unirest.post("https://kubernetes.pasiphae.eu/shapes/asapa/auth/register")
                        .header("Accept", "application/json")
                        .header("X-Shapes-Key", "7Msbb3w^SjVG%j")
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
}
