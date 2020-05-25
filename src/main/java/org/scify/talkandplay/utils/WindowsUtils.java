package org.scify.talkandplay.utils;

import java.io.File;
import java.io.IOException;

public class WindowsUtils {

    private WindowsUtils() {  }

    public static boolean isAdmin() {
        return canWriteToApplicationDir();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Current user is admin ? " + WindowsUtils.isAdmin());
    }

    public static boolean canWriteToApplicationDir() {
        Properties properties = new Properties();
        File installDir = new File(properties.getApplicationFolder());
        System.out.println("Trying to write a temp file to " + properties.getApplicationFolder());
        if (!installDir.canWrite())
            return false;

        File fileTest = null;
        try {
            fileTest = File.createTempFile("test_write", ".txt", installDir);
        } catch (IOException e) {
            //If an exception occured while trying to create the file, it means that it is not writable
            return false;
        } finally {
            if (fileTest != null)
                fileTest.delete();
        }
        return true;
    }
}
