package com.nonogram;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class FileIO {
    private String file;
    public void CreateFile(String path) {
        try {
            File myObj = new File(path);
            if (myObj.createNewFile()) {
                this.file = path;
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void WriteToFile(String content) {
        try {
            FileWriter myWriter = new FileWriter(this.file, true);
            myWriter.write(content);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
