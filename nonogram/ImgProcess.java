package com.nonogram;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

import static java.awt.color.ColorSpace.CS_GRAY;

public class ImgProcess {
    private BufferedImage img;
    private static int IMG_SIZE;
    public int rec[][];
    public void ProcessImage(String str, int _gameSize) throws IOException {
        BufferedImage srcImg = null;
        IMG_SIZE = _gameSize;
        try {
            srcImg = ImageIO.read(new File(str));
        } catch (IOException e) {
            System.out.println("Error read: "+e);
        }
        img = ConvertToBinary(ResizeImage(srcImg, IMG_SIZE, IMG_SIZE));
    }
    private BufferedImage ConvertToBinary(BufferedImage srcImg) throws IOException {
        rec = new int[IMG_SIZE][IMG_SIZE];
        BufferedImage binaryImg = new BufferedImage(IMG_SIZE, IMG_SIZE, BufferedImage.TYPE_INT_RGB);
        if (srcImg == null) {
            System.out.println("No image loaded");
        }
        else {
            for(int i=0;i<IMG_SIZE;i++)
            {
                for(int j=0;j<IMG_SIZE;j++)
                {
                    // Get RGB Value
                    int val = srcImg.getRGB(i, j);
                    // Convert to three separate channels
                    int r = (0x00ff0000 & val) >> 16;
                    int g = (0x0000ff00 & val) >> 8;
                    int b = (0x000000ff & val);
                    int m=(r+g+b);
                    // (255+255+255)/2 =283 middle of intensity
                    if(m>=283)
                    {
                        // for light color, set white
                        binaryImg.setRGB(i, j, Color.WHITE.getRGB());
                    }
                    else{
                        // for dark color, set black
                        binaryImg.setRGB(i, j, 0);
                        rec[j][i] = 1;
                    }
                }
            }
        }
        File file = new File("MyImage.jpg");
        ImageIO.write(binaryImg, "jpg", file);
        return binaryImg;
    }

    private BufferedImage ResizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    public void DisplayImage(BufferedImage img) throws IOException {
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(200,300);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}
