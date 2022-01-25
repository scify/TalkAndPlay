package org.scify.talkandplay.utils;

public class DummyLoginManager extends LoginManager {

    public DummyLoginManager() {

    }

    @Override
    public OperationMessage signIn(String email, String password) {
        if (email.equals("test") && password.equals("123"))
            return new OperationMessage(true);
        else
            return new OperationMessage(false, "wrong credentials");
    }

    @Override
    public OperationMessage signUp(String email, String password, String confirmPassword) {
        if (email.equals("test") && password.equals("123") && password.equals(confirmPassword))
            return new OperationMessage(true);
        else
            return new OperationMessage(false, "wrong credentials");
    }
}
