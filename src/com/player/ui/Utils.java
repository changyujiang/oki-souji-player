package com.player.ui;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

class Utils {
    private final static int HEIGHT = 288;
    private final static int WIDTH = 352;

    static File selectFile(Component component) {
        final JFileChooser fc = new JFileChooser();

        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showDialog(component, null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            log("Selected path is: " + file);
            return file;
        } else {
            log("Open command canceled by user.");
            return null;
        }
    }

    static void log(String message) {
        System.out.println(message);
    }

    private static BufferedImage readImage(String fileName) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        try {
            File file = new File(fileName);
            byte[] imageContent = Files.readAllBytes(file.toPath());

            int[] reds = new int[WIDTH * HEIGHT];
            int[] greens = new int[WIDTH * HEIGHT];
            int[] blues = new int[WIDTH * HEIGHT];
            int[] pixels = new int[WIDTH * HEIGHT];

            int index = 0;
            int offset = WIDTH * HEIGHT;

            for (int i = 0; i < HEIGHT; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    reds[index] = imageContent[index];
                    greens[index] = imageContent[index + offset];
                    blues[index] = imageContent[index + offset*2];
                    pixels[index] = 0xff000000 | ((reds[index] & 0xff) << 16) | ((greens[index] & 0xff) << 8) | (blues[index] & 0xff);
                    index++;
                }
            }
            image.setRGB(0,0, WIDTH, HEIGHT, pixels, 0, WIDTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * @param frameNumber from 1 - 9000
     * @return bufferedImage
     */
    static BufferedImage loadFrame(File dir, int frameNumber) {
        if (dir == null) {
            log("Error: dir is null");
            return null;
        }
        String fileName = dir.getName() + String.format("%04d", frameNumber) + ".rgb";
        String filePath = dir.getAbsolutePath() + File.separator + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            log("Error: file: " + filePath + "is not exists.");
            return null;
        }
        return readImage(filePath);
    }

    static Links loadLinks(File dir){
        Links links = null;
        if (dir == null) {
            log("Error: dir is null");
            return null;
        }
        String fileName = dir.getName() + ".json";
        String filePath = dir.getAbsolutePath() + File.separator + fileName;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            Gson gson = new Gson();
            links = gson.fromJson(bufferedReader, Links.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return links;
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

}
