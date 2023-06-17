package com.example.todoviews.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Todo {
    private static long ID = 0;
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String description;
    private boolean isDone = false;
    private boolean isFavourite = false;
    private long dueDate;

    public Todo(String title, String description, long dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public Todo(long id,String title, String description, boolean isDone, boolean isFavorite, long dueDate) {
        this.setId(id);
        this.title = title;
        this.description = description;
        this.isDone = isDone;
        this.isFavourite = isFavorite;
        this.dueDate = dueDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        if(id <= 0) {
            this.id = ID++;
        } else {
            this.id = id;
        }
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
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public void update(Todo todo) {
        this.setTitle(todo.getTitle());
        this.setDescription(todo.getDescription());
        this.setDone(todo.isDone());
        this.setFavourite(todo.isFavourite());
        this.setDueDate(todo.getDueDate());
    }

    public int compareTo(Todo t) {
        if(this.isDone == t.isDone) return (int)(t.getDueDate() - this.dueDate);
        if(this.isDone) return 1;
        return -1;
    }

}
