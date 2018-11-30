package com.player.entity;

import com.google.gson.annotations.SerializedName;
import org.opencv.core.Rect2d;

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


    public static class Link {

        public Link() {

        }

        public Link(int curFrameNum, Rect2d rect2d) {
            this.curFrameNum = curFrameNum;
            this.x = (int) rect2d.x;
            this.y = (int) rect2d.y;
            this.width = (int) rect2d.width;
            this.height = (int) rect2d.height;
        }

        @SerializedName("x")
        private int x;

        @SerializedName("y")
        private int y;

        @SerializedName("w")
        private int width;

        @SerializedName("h")
        private int height;

        @SerializedName("frame")
        private int frameNum;

        @SerializedName("path")
        private String path;

        private int curFrameNum;

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

        public int getFrameNum() {
            return frameNum;
        }

        public void setFrameNum(int frameNum) {
            this.frameNum = frameNum;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getCurFrameNum() {
            return curFrameNum;
        }

        public void setCurFrameNum(int curFrameNum) {
            this.curFrameNum = curFrameNum;
        }
    }

}
