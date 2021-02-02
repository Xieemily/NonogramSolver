package com.nonogram;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class ImgProcess {
    private BufferedImage img;
    private static int IMG_SIZE;
    public int[][] rec;

    /**
     * Read image from file and convert it to binary image
     *
     * @param path
     *          path of source image
     * @param _gameSize
     *          specify board length
     *
     */
    public void ProcessImage(String path, int _gameSize) throws IOException {
        BufferedImage srcImg = null;
        IMG_SIZE = _gameSize;
        try {
            srcImg = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Error read: "+e);
        }
        img = ConvertToBinary(ResizeImage(srcImg, IMG_SIZE, IMG_SIZE));
    }

    /**
     * Convert input image to binary image by comparing pixel intensity
     *
     * @param srcImg
     *          path of source image
     * @return binaryImage
     *
     */
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

    private BufferedImage ResizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    public void DisplayImage(BufferedImage img) {
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
