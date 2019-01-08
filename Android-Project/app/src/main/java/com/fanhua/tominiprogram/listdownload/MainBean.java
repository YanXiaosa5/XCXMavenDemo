package com.fanhua.tominiprogram.listdownload;

import java.io.Serializable;

public class MainBean implements Serializable{
    private String title;
    private String url;
    private boolean isDownload;

    public MainBean(String title, String url, boolean isDownload) {
        this.title = title;
        this.url = url;
        this.isDownload = isDownload;
    }

    @Override
    public String toString() {
        return "MainBean{" +
                "title='" + title + '\'' +
                ", isDownload=" + isDownload +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public MainBean(String title, boolean isDownload) {
        this.title = title;
        this.isDownload = isDownload;
    }
}