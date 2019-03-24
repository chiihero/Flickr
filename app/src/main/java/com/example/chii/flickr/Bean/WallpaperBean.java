package com.example.chii.flickr.Bean;

import java.util.List;

public class WallpaperBean {

    private String msg;
    private List<Wallpaper> wallpaper;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isError() {
        return false;
    }

    public List<Wallpaper> getwallpaper() {
        return wallpaper;
    }

    public class Wallpaper {

        private String author;
        private String link;
        private String tag;
        private String source;
        private String title;
        private String type;
        private int height;
        private int width;

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAuthor() {
            return author;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getLink() {
            return link;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getSource() {
            return source;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }


        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public boolean isNull() {
            return height == 0 || width == 0;
        }

        public void setSize(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}
