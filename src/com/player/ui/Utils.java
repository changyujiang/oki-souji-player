package com.player.ui;

import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class Utils {

    private final static int HEIGHT = 288;
    private final static int WIDTH = 352;

    @Nullable
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

    static BufferedImage readImage(String fileName) {
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

}
