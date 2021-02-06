package com.nonogram;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class FileIO {
    private static String file;
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
