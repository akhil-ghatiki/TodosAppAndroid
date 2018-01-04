package com.example.todosapp.datamodel;

/**
 * Created by aghatiki on 12/26/2017.
 */

public class Item {

    private String title;
    private String description;
    private boolean status;
    private String attachment;

    public Item(String title, String description, boolean status, String attachment) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.attachment = attachment;
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

    public boolean isDone() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
