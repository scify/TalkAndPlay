package org.scify.talkandplay.utils;

import java.io.File;
import java.io.IOException;

public class FileSystemUtils {

    private FileSystemUtils() {  }

    public static void main(String[] args) throws IOException {
        System.out.println("Current user can write to Application directory ? " + FileSystemUtils.canWriteToApplicationDir());
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

    public static boolean platformIsWindows() {
        return System.getProperty("os.name").toUpperCase().contains("WINDOWS");
    }
}
