package org.scify.talkandplay.utils;

public class AnnouncementTranslation {
    public String title;
    public String message;
    public String link;

    public String language;

    public AnnouncementTranslation(String title, String message, String link, String language) {
        this.title = title;
        this.message = message;
        this.link = link;
        this.language = language;
    }
}
