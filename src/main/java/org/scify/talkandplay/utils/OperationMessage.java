package org.scify.talkandplay.utils;

public class OperationMessage {

    protected boolean success;
    protected String error;

    public OperationMessage(boolean success) {
        this.success = success;
        error = "";
    }

    public OperationMessage(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}
