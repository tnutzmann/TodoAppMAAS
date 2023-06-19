package de.thb.fbi.msr.maus.einkaufsliste.model;

import java.io.Serializable;

public class TodoItem implements Serializable {
    private long id;
    private String title;
    private String description;
    private boolean isDone = false;
    private boolean isFavourite = false;
    private long dueDate;

    public TodoItem(long id,String title, String description, boolean isDone, boolean isFavorite, long dueDate) {
        this.setId(id);
        this.title = title;
        this.description = description;
        this.isDone = isDone;
        this.isFavourite = isFavorite;
        this.dueDate = dueDate;
    }

    public TodoItem() {
        // TODO Auto-generated constructor stub
    }

    public TodoItem update(TodoItem todo) {
        this.setTitle(todo.getTitle());
        this.setDescription(todo.getDescription());
        this.setDone(todo.isDone());
        this.setFavourite(todo.isFavourite());
        this.setDueDate(todo.getDueDate());

        return this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "TodoItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", isDone=" + isDone +
                ", isFavourite=" + isFavourite +
                ", dueDate=" + dueDate +
                '}';
    }

    public boolean equals(Object other) {
        if(!(other instanceof TodoItem)) return false;
        return ((TodoItem) other).getId() == this.getId();
    }
}
