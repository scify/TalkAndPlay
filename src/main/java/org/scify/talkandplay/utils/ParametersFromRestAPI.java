package org.scify.talkandplay.utils;

import java.util.List;

public class ParametersFromRestAPI {

    public String shapesSignInUrl;

    public String shapesSignUpUrl;

    public String shapesKey;

    public String sentryDSN;

    public String firebaseUrl;

    public List<Announcement> announcements;

    public ParametersFromRestAPI(String shapesSignInUrl, String shapesSignUpUrl, String shapesKey, String sentryDSN, String firebaseUrl, List<Announcement> announcements) {
        this.shapesSignInUrl = shapesSignInUrl;
        this.shapesSignUpUrl = shapesSignUpUrl;
        this.shapesKey = shapesKey;
        this.sentryDSN = sentryDSN;
        this.firebaseUrl = firebaseUrl;
        this.announcements = announcements;
    }
}
