package com.player.entity;

import org.opencv.core.Rect2d;

import java.util.List;

public class ProducerLink {

    public ProducerLink(int id, String name, int startFrame, List<BBox> bBoxes) {
        this.id = id;
        this.name = name;
        this.startFrame = startFrame;
        this.bBoxes = bBoxes;
    }

    private int id;

    private String name;

    private int startFrame;

    private String destPath;

    private int destFrame;

    private List<BBox> bBoxes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartFrame() {
        return startFrame;
    }

    public void setStartFrame(int startFrame) {
        this.startFrame = startFrame;
    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    public int getDestFrame() {
        return destFrame;
    }

    public void setDestFrame(int destFrame) {
        this.destFrame = destFrame;
    }

    public List<BBox> getbBoxes() {
        return bBoxes;
    }

    public void setbBoxes(List<BBox> bBoxes) {
        this.bBoxes = bBoxes;
    }

    public static class BBox {

        int frame;

        int x;

        int y;

        int width;

        int height;

        public BBox(int frame, Rect2d rect2d) {
            this.frame = frame;
            this.x = (int) rect2d.x;
            this.y = (int) rect2d.y;
            this.width = (int) rect2d.width;
            this.height = (int) rect2d.height;
        }

        public int getFrame() {
            return frame;
        }

        public void setFrame(int frame) {
            this.frame = frame;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

    }

    @Override
    public String toString() {
        return name;
    }
}
