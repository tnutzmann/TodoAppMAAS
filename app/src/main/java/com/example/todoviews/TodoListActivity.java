package com.example.todoviews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoviews.accessor.ITodoListAccessor;
import com.example.todoviews.accessor.TodoListFromRoomAccessor;
import com.example.todoviews.adapter.TodoAdapter;
import com.example.todoviews.models.Todo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TodoListActivity extends AppCompatActivity {
    private RecyclerView todoRecyclerView;
    private ITodoListAccessor accessor;
    private FloatingActionButton addTodoBtn;
    private ActivityResultLauncher<Intent> activityResultLauncher;

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

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Log.i(this.getClass().getName(), String.format("got something back: %d", result.getResultCode()));
                if(result.getResultCode() == Activity.RESULT_OK) {
                    Bundle resultData = result.getData().getExtras();
                    if(resultData.getLong("ID") == -1) {
                        // new todoitem
                        accessor.addTodo(new Todo(resultData.getString("TITLE"), resultData.getString("DESCRIPTION"), resultData.getLong("DUE_DATE")));
                    } else {

                    }
                }
            }
        });

        addTodoBtn = findViewById(R.id.openAddTodoDialogButton);
        addTodoBtn.setOnClickListener(this::onAddTodoBtnClick);
    }

    private void onAddTodoBtnClick(View v) {
        Log.i(this.getClass().getName(), "Open Add Todo Dialog.");

        Intent addTodoIntent = new Intent(TodoListActivity.this, todoDetailsActivity.class);
        /*
        addTodo.putExtra("ID", -1);
        addTodo.putExtra("TITLE", "");
        addTodo.putExtra("DESCRIPTION", "");
        addTodo.putExtra("DUE_DATE", System.currentTimeMillis());
         */
        //startActivityForResult(addTodo, Activity.RESULT_OK); // the requestcode will represent the id of a todoitem if provided
        activityResultLauncher.launch(addTodoIntent);
        //accessor.addTodo(new Todo("RoomTest_","Simply added by AddBtn."));
    }
}