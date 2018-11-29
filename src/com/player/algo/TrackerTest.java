package com.player.algo;

import javafx.util.Pair;
import org.opencv.core.Mat;
import org.opencv.core.Rect2d;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class TrackerTest {
    static public BufferedImage getImg(Mat imgMat, Rect2d bbox){
        BufferedImage img = new BufferedImage(352, 288, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < 288; y++) {
            for (int x = 0; x < 352; x++) {
                byte[] bgr = new byte[3];
                imgMat.get(y, x, bgr);
                int pix = 0xff000000 | ((bgr[2] & 0xff) << 16) | ((bgr[1] & 0xff) << 8) | (bgr[0] & 0xff);
                img.setRGB(x, y, pix);
            }
        }
        Rect2d imgBbox = bbox.clone();
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.GREEN);
        g2d.drawRect((int)imgBbox.x, (int)imgBbox.y, (int)imgBbox.width, (int)imgBbox.height);
        g2d.dispose();
        return img;
    }

    static public void showImg(BufferedImage img){
        JFrame frame = new JFrame();
        GridBagLayout gLayout = new GridBagLayout();
        frame.getContentPane().setLayout(gLayout);
        JLabel lbIm = new JLabel(new ImageIcon(img));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        frame.getContentPane().add(lbIm, c);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        CVObjTracker ot = new CVObjTracker();
        Rect2d bbox = new Rect2d(220, 60, 60, 135);
        int trackDis = 150;
//        List<Pair<Integer, Rect2d>> bboxs = ot.trackObj("data/AIFilmOne", 130, bbox, trackDis);
//        for (Pair<Integer, Rect2d>pair : bboxs){
//            System.out.println(pair);
//        }
//        System.out.println(bboxs.size());

//        VideoCapture vc = new VideoCapture();
//        vc.open("data/AIFilmOne.avi");
//        int video_length = (int) vc.get(Videoio.CAP_PROP_FRAME_COUNT);
//        int frames_per_second = (int) vc.get(Videoio.CAP_PROP_FPS);
//        int frame_number = (int) vc.get(Videoio.CAP_PROP_POS_FRAMES);
//        System.out.println(video_length);
//        System.out.println(frames_per_second);
//        int frameIndex = 1;
//        for(; frameIndex <= 100; frameIndex++) {
//            vc.read(imgMat);
//        }
//        Rect2d box = bboxs.get(0).getValue();
//        BufferedImage img1 = getImg(imgMat, box);
//
//        for(; frameIndex <= 250 + trackDis; frameIndex++)
//            vc.read(imgMat);
//
//
//        Rect2d box2 = bboxs.get(bboxs.size()-1).getValue();
//        BufferedImage img2 = getImg(imgMat, box2);
//
//        JFrame frame = new JFrame();
//        GridBagLayout gLayout = new GridBagLayout();
//        frame.getContentPane().setLayout(gLayout);
//
//        JLabel lbText1 = new JLabel("Original image (Left)");
//        lbText1.setHorizontalAlignment(SwingConstants.CENTER);
//        JLabel lbText2 = new JLabel("Image after modification (Right)");
//        lbText2.setHorizontalAlignment(SwingConstants.CENTER);
//        JLabel lbIm1 = new JLabel(new ImageIcon(img1));
//        JLabel lbIm2 = new JLabel(new ImageIcon(img2));
//
//        GridBagConstraints c = new GridBagConstraints();
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.anchor = GridBagConstraints.CENTER;
//        c.weightx = 0.5;
//        c.gridx = 0;
//        c.gridy = 0;
//        frame.getContentPane().add(lbText1, c);
//
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.anchor = GridBagConstraints.CENTER;
//        c.weightx = 0.5;
//        c.gridx = 1;
//        c.gridy = 0;
//        frame.getContentPane().add(lbText2, c);
//
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.gridx = 0;
//        c.gridy = 1;
//        frame.getContentPane().add(lbIm1, c);
//
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.gridx = 1;
//        c.gridy = 1;
//        frame.getContentPane().add(lbIm2, c);
//
//        frame.pack();
//        frame.setVisible(true);

    }
}