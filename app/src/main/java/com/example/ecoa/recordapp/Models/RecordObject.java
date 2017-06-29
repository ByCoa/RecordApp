package com.example.ecoa.recordapp.Models;

/**
 * Created by ecoa on 6/26/2017.
 */

public class RecordObject {
    private String title;
    private String date;
    private String duration;
    private String path;

    public RecordObject(String title, String date, String duration, String path) {
        this.title = title;
        this.date = date;
        this.duration = duration;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
