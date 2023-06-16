package com.example.todoviews.models;

import androidx.room.*;

import java.util.List;

@Dao
public interface TodoDao {
    @Insert
    void insert(Todo todo);

    @Insert
    void inserAll(Todo ... todos);

    @Update
    void updateTodo(Todo todo);

    @Delete
    void deleteTodo(Todo todo);

    @Query("SELECT * FROM todo")
    List<Todo> getAll();

    @Query("SELECT * FROM todo WHERE id LIKE :id LIMIT 1")
    Todo findById(long id);
}
