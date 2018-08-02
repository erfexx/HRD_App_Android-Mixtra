package com.example.kayangan.absencehrd.Model;

public class Comment {
    private int id, task_id;
    private String title, comment, commenter, created_at, updated_at;

    public Comment() {
    }

    public Comment(int task_id, String title, String comment, String commenter, String created_at, String updated_at) {
        this.task_id = task_id;
        this.title = title;
        this.comment = comment;
        this.commenter = commenter;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
