package org.scify.talkandplay.utils;

public abstract class LoginManager {
    public abstract OperationMessage signIn(String email, String password);
    public abstract OperationMessage signUp(String email, String password, String confirmPassword);
}
