package com.player.ui;

import com.player.entity.Frame;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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

    static BufferedImage getPlaceHolder() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        return image;
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

    static Clip loadAudio(File dir){
        Clip clip = null;
        if (dir == null) {
            log("Error: dir is null");
            return clip;
        }
        String fileName = dir.getName() + ".wav";
        String filePath = dir.getAbsolutePath() + File.separator + fileName;
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(stream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return clip;
    }

    static Map<Integer, Frame> loadFrameMeta(File dir) {
        Map<Integer, Frame> frameMap = new HashMap<>();
        if (dir == null) {
            log("Error: dir is null");
            return frameMap;
        }
        String fileName = dir.getName() + ".json";
        String filePath = dir.getAbsolutePath() + File.separator + fileName;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            Type listType = new TypeToken<ArrayList<Frame>>(){}.getType();
            List<Frame> frames = new Gson().fromJson(bufferedReader, listType);
            for (Frame frame: frames) {
                frameMap.put(frame.getFrameNum(), frame);
            }
        } catch (Exception e) {
            log("Error: no valid json file");
            e.printStackTrace();
        }
        return frameMap;
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    private static final String ERROR_PRODUCER = "Producer Error";

    static void showErrorMessage(JFrame jFrame, String em) {
        JOptionPane.showMessageDialog(
                jFrame,
                em,
                ERROR_PRODUCER,
                JOptionPane.ERROR_MESSAGE);
    }

    static void showMessage(JFrame jFrame, String message) {
        JOptionPane.showMessageDialog(
                jFrame,
                message
        );
    }

    static JDialog showLoadingDialog(JFrame jFrame) {
        final JDialog jDialog = new JDialog(jFrame, "Producer", true);
        jDialog.add(BorderLayout.NORTH, new JLabel("Processing..."));
        jDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        jDialog.setSize(300, 75);
        jDialog.setLocationRelativeTo(jFrame);

        jDialog.setVisible(true);

        return jDialog;
    }

    static void hideLoadingDialog(JFrame jFrame, JDialog dialog) {
        dialog.setVisible(false);
        System.exit(0);
    }

}
