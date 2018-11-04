package com.a.addy.todo;

public class Task implements java.io.Serializable{

    public int importance;
    public String title,description,date;

    public Task(int importance, String title, String description, String date) {
        this.importance = importance;
        this.title = title;
        this.description = description;
        this.date = date;
    }


    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
