package com.example.todoviews.utils;

import android.content.Context;
import androidx.room.*;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.todoviews.models.Todo;
import com.example.todoviews.models.TodoDao;
import org.jetbrains.annotations.NotNull;

@Database(entities = {Todo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TodoDao todoDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "TODO_DB").build();
                }
            }
        }
        return INSTANCE;
    }
}
