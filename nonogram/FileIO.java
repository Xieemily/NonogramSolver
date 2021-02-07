package com.nonogram;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * File creation and write to file.
 */
class FileIO {
    private static String file;

    /**
     * Create output file.
     * @param path output file path
     */
    public static void CreateFile(String path) {
        try {
            File myObj = new File(path);
            if (myObj.createNewFile()) {
                file = path;
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            // e.printStackTrace();
        }
    }

    /**
     * Write result to previous created output file.
     * @param content content to be appended to file
     */
    public static void WriteToFile(String content) {
        try {
            FileWriter myWriter = new FileWriter(file, true);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            // e.printStackTrace();
        }
    }
}
