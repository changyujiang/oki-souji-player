package com.player.ui;

import java.util.ArrayList;
import java.util.Map;

class Links {
    private Map<String, ArrayList<LinkedObj>> links;

    public Map<String, ArrayList<LinkedObj>> getLinks() {
        return links;
    }

    public void setLinks(Map<String, ArrayList<LinkedObj>> links) {
        this.links = links;
    }
}

class LinkedObj{
    private int x;
    private int y;
    private int width;
    private int height;
    private String filePath;
    private int frameIndex;

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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }
}

