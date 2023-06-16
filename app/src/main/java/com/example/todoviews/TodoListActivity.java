package com.example.todoviews;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoviews.accessor.ITodoListAccessor;
import com.example.todoviews.accessor.TodoListFromRoomAccessor;
import com.example.todoviews.accessor.TodoListFromStaticAccessor;
import com.example.todoviews.adapter.TodoAdapter;
import com.example.todoviews.models.Todo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TodoListActivity extends AppCompatActivity {
    private RecyclerView todoRecyclerView;
    private ITodoListAccessor accessor;
    private FloatingActionButton addTodoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        todoRecyclerView = findViewById(R.id.todoRecyclerView);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //accessor = new TodoListFromStaticAccessor(this);
        accessor = new TodoListFromRoomAccessor(this);

        final TodoAdapter adapter = accessor.getAdapter();
        todoRecyclerView.setAdapter(adapter);

        addTodoBtn = findViewById(R.id.addTodoButton);
        addTodoBtn.setOnClickListener(this::onAddTodoBtnClick);
    }

    private void onAddTodoBtnClick(View v) {
        Log.i(this.getClass().getName(), "Add Todo.");
        accessor.addTodo(new Todo("RoomTest_","Simply added by AddBtn."));
    }
}