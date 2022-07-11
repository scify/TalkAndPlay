package org.scify.talkandplay.utils;

import java.util.HashMap;

public class Announcement {
    protected int severity;
    protected String type;
    protected String updated_at;
    protected HashMap<String, AnnouncementTranslation> announcementTranslations;

    public Announcement(int severity, String type, String updated_at) {
        announcementTranslations = new HashMap<>();
        this.severity = severity;
        this.type = type;
        this.updated_at = updated_at;
    }

    public void addAnnouncementTranslation(String language, AnnouncementTranslation announcementTranslation) {
        announcementTranslations.put(language, announcementTranslation);
    }

    public AnnouncementTranslation getAnnouncementTranslation(String lang) {
        return announcementTranslations.get(lang);
    }


    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
