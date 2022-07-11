package org.scify.talkandplay.utils;

public class ParametersFromRestAPI {

    public String shapesSignInUrl;

    public String shapesSignUpUrl;

    public String shapesKey;

    public String sentryDSN;

    public String firebaseUrl;

    public Announcement announcement;

    public ParametersFromRestAPI(String shapesSignInUrl, String shapesSignUpUrl, String shapesKey, String sentryDSN, String firebaseUrl, Announcement announcement) {
        this.shapesSignInUrl = shapesSignInUrl;
        this.shapesSignUpUrl = shapesSignUpUrl;
        this.shapesKey = shapesKey;
        this.sentryDSN = sentryDSN;
        this.firebaseUrl = firebaseUrl;
        this.announcement = announcement;
    }
}
