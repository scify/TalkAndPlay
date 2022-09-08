package org.scify.talkandplay.utils;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import io.sentry.Sentry;
import org.apache.log4j.Logger;

public class FirebaseRestAPI {

    protected Gson g;
    protected final ResourceManager rm;
    protected String firebaseUrl;
    protected String shapesToken;

    protected String version;
    protected static FirebaseRestAPI instance;
    protected Logger logger = Logger.getLogger(FirebaseRestAPI.class);
    protected static String SHAPESLOGINEVENT = "\"eventType\": \"SHAPES_LOGIN\", ";
    protected static String SHAPESLOGOUTEVENT = "\"eventType\": \"SHAPES_LOGOUT\", ";
    protected static String COMMUNICATIONCATEGORYEVENT = "\"eventType\": \"COMMUNICATION_CATEGORY_EVENT\", ";
    protected static String GAMESELECTIONEVENT = "\"eventType\": \"GAME_SELECTION_EVENT\", ";

    protected static String restAPIStorageUrl;


    public FirebaseRestAPI(String firebaseUrl, String restAPIStorageUrl, String version) {
        this.restAPIStorageUrl = restAPIStorageUrl;
        this.version = version;
        g = new Gson();
        rm = ResourceManager.getInstance();
        this.firebaseUrl = firebaseUrl;
        logger.info("Firebase was initiated successfully!");
        instance = this;
    }

    public void updateShapesToken() {
        LoginManager loginManager = TalkAndPlayProfileConfiguration.getInstance().getLoginManager();
        if (loginManager instanceof ShapesLoginManager) {
            shapesToken = ((ShapesLoginManager) loginManager).getToken();
        }
    }

    public static FirebaseRestAPI getInstance() {
        return instance;
    }

    public void postShapesLogin() {
        if (firebaseUrl.length() > 0) {
            String jsonBody = "{";
            jsonBody += "\"language\": \"" + rm.getSelectedLanguage() + "\", ";
            if (shapesToken != null && shapesToken.length() > 0)
                jsonBody += "\"authToken\": \"" + shapesToken + "\", ";
            jsonBody += SHAPESLOGINEVENT;
            jsonBody += "\"timestamp\":{\".sv\": \"timestamp\"}}";
            sendToFireBase(jsonBody);
        }
        //restAPIStorageUrl POST
        String jsonBody = "{";
        jsonBody += "\"lang\": \"" + rm.getSelectedLanguage() + "\", ";
        if (shapesToken != null && shapesToken.length() > 0)
            jsonBody += "\"token\": \"" + shapesToken + "\", ";
        jsonBody += "\"action\": \"SHAPES_LOGIN\", ";
        jsonBody += "\"version\": \"" + version + "\"}";
        sendToRestAPIStorage(jsonBody);
    }

    public void postShapesLogout() {
        if (firebaseUrl.length() > 0) {
            String jsonBody = "{";
            jsonBody += "\"language\": \"" + rm.getSelectedLanguage() + "\", ";
            if (shapesToken != null && shapesToken.length() > 0)
                jsonBody += "\"authToken\": \"" + shapesToken + "\", ";
            jsonBody += SHAPESLOGOUTEVENT;
            jsonBody += "\"timestamp\":{\".sv\": \"timestamp\"}}";
            sendToFireBase(jsonBody);
        }
        //restAPIStorageUrl POST
        String jsonBody = "{";
        jsonBody += "\"lang\": \"" + rm.getSelectedLanguage() + "\", ";
        if (shapesToken != null && shapesToken.length() > 0)
            jsonBody += "\"token\": \"" + shapesToken + "\", ";
        jsonBody += "\"action\": \"SHAPES_LOGOUT\", ";
        jsonBody += "\"version\": \"" + version + "\"}";
        sendToRestAPIStorage(jsonBody);
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
        sendToFireBase(jsonBody);
        //restAPIStorageUrl POST
        jsonBody = "{";
        jsonBody += "\"lang\": \"" + rm.getSelectedLanguage() + "\", ";
        if (shapesToken != null && shapesToken.length() > 0)
            jsonBody += "\"token\": \"" + shapesToken + "\", ";
        jsonBody += "\"action\": \"COMMUNICATION_CATEGORY_EVENT\", ";
        jsonBody += "\"category_name\": \"" + categoryName + "\", ";
        jsonBody += "\"parent_category_name\": \"" + parentCategoryName + "\", ";
        jsonBody += "\"version\": \"" + version + "\"}";
        sendToRestAPIStorage(jsonBody);
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
        sendToFireBase(jsonBody);
        //restAPIStorageUrl POST
        jsonBody = "{";
        jsonBody += "\"lang\": \"" + rm.getSelectedLanguage() + "\", ";
        if (shapesToken != null && shapesToken.length() > 0)
            jsonBody += "\"token\": \"" + shapesToken + "\", ";
        jsonBody += "\"action\": \"GAME_SELECTION_EVENT\", ";
        jsonBody += "\"gameName\": \"" + gameName + "\", ";
        jsonBody += "\"gameType\": \"" + gameType + "\", ";
        jsonBody += "\"duration\": \"" + duration + "\", ";
        jsonBody += "\"mistakes\": \"" + mistakes + "\", ";
        jsonBody += "\"version\": \"" + version + "\"}";
        sendToRestAPIStorage(jsonBody);
    }

    protected void sendToFireBase(final String jsonString) {
        Thread firebaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Unirest.setTimeouts(100000, 100000);
                    HttpResponse<String> response = Unirest.post(firebaseUrl)
                            .header("Content-Type", "application/json")
                            .body(jsonString).asString();
                    String responseMessage = response.getBody();
                    logger.debug(responseMessage);
                } catch (Exception e) {
                    logger.error(e);
                    Sentry.capture(e.getMessage());
                }
            }
        });
        firebaseThread.start();
    }

    protected void sendToRestAPIStorage(final String jsonString) {
        Thread restAPIStorageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Unirest.setTimeouts(100000, 100000);
                    HttpResponse<String> response = Unirest.post(restAPIStorageUrl)
                            .header("Content-Type", "application/json")
                            .body(jsonString).asString();
                    String responseMessage = response.getBody();
                    logger.debug(responseMessage);
                } catch (Exception e) {
                    logger.error(e);
                    Sentry.capture(e.getMessage());
                }
            }
        });
        restAPIStorageThread.start();
    }
}
