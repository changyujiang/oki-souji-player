package com.player.entity;

import com.google.gson.annotations.SerializedName;
import com.player.ui.Producer;

import java.util.ArrayList;
import java.util.List;

public class Frame {

    private int frameNum;

    private List<Link> links;

    public Frame() {

    }

    public Frame(int frameNum, ProducerLink.BBox bBox) {
        this.frameNum = frameNum;
        links = new ArrayList<>();
        links.add(new Link(bBox));
    }

    public Frame(int frameNum, ProducerLink.BBox bBox, int destFrameNum, String path) {
        this.frameNum = frameNum;
        links = new ArrayList<>();
        links.add(new Link(bBox, destFrameNum, path));
    }

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

    public void addBbox(ProducerLink.BBox bBox) {
        if (links == null) {
            links = new ArrayList<>();
        }
        links.add(new Link(bBox));
    }

    public void addLink(ProducerLink.BBox bBox, int frameNum, String path) {
        if (links == null) {
            links = new ArrayList<>();
        }
        links.add(new Link(bBox, frameNum, path));
    }

    public static class Link {

        public Link() {

        }

        public Link(ProducerLink.BBox bBox) {
            this.x = bBox.x;
            this.y = bBox.y;
            this.width = bBox.width;
            this.height = bBox.height;
        }

        public Link(ProducerLink.BBox bBox, int frameNum, String path) {
            this.x = bBox.x;
            this.y = bBox.y;
            this.width = bBox.width;
            this.height = bBox.height;
            this.frameNum = frameNum;
            this.path = path;
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

    }

}
