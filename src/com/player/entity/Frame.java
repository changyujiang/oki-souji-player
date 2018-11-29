package com.player.entity;

import java.util.List;

public class Frame {

    private int frameNum;

    private List<Link> links;

    public int getFrameNum() {
        return frameNum;
    }

    public void setFrameNum(int frameNum) {
        this.frameNum = frameNum;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    class Link {

        private int x;

        private int y;

        private int width;

        private int height;

        private String path;

        private int frameNum;

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

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getFrameNum() {
            return frameNum;
        }

        public void setFrameNum(int frameNum) {
            this.frameNum = frameNum;
        }
    }

}
