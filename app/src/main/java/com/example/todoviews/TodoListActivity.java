package com.example.todoviews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoviews.accessor.ITodoListAccessor;
import com.example.todoviews.accessor.TodoListFromRoomAccessor;
import com.example.todoviews.adapter.TodoAdapter;
import com.example.todoviews.models.Todo;
import com.example.todoviews.utils.RecyclerItemTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TodoListActivity extends AppCompatActivity {
    private RecyclerView todoRecyclerView;
    private ITodoListAccessor accessor;
    private FloatingActionButton addTodoBtn;
    private ToggleButton sortingSwitch;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        todoRecyclerView = findViewById(R.id.todoRecyclerView);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        accessor = new TodoListFromRoomAccessor(this);

        adapter = accessor.getAdapter();
        todoRecyclerView.setAdapter(adapter);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Log.i(this.getClass().getName(), String.format("got something back: %d", result.getResultCode()));
                if(result.getResultCode() == Activity.RESULT_OK) {
                    Bundle resultData = result.getData().getExtras();
                    if(resultData.getLong("ID") == -1) {
                        // new todoitem
                        Todo newTodo = new Todo(resultData.getString("TITLE"), resultData.getString("DESCRIPTION"),resultData.getLong("DUE_DATE"));
                        newTodo.setDone(resultData.getBoolean("IS_DONE"));
                        accessor.addTodo(newTodo);
                    } else {
                        if(resultData.getBoolean("DELETED", false)) {
                            // delete todoItem
                            Todo todo = adapter.lookupItem(resultData.getLong("ID"));
                            accessor.deleteItem(todo);
                        } else {
                            // update todoItem
                            Todo todo = adapter.lookupItem(resultData.getLong("ID"));
                            todo.setTitle(resultData.getString("TITLE", todo.getTitle()));
                            todo.setDescription(resultData.getString("DESCRIPTION", todo.getDescription()));
                            todo.setDueDate(resultData.getLong("DUE_DATE", todo.getDueDate()));
                            todo.setDone(resultData.getBoolean("IS_DONE", todo.isDone()));

                            accessor.updateItem(todo);
                        }
                    }

                }
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(todoRecyclerView);

        addTodoBtn = findViewById(R.id.openAddTodoDialogButton);
        addTodoBtn.setOnClickListener(this::onAddTodoBtnClick);

        sortingSwitch = findViewById(R.id.sortingToggleButton);
        sortingSwitch.setOnCheckedChangeListener(this::onSortingSwitchChanged);
    }

    private void onAddTodoBtnClick(View v) {
        Log.i(this.getClass().getName(), "Open Add Todo Dialog.");

        Intent addTodoIntent = new Intent(TodoListActivity.this, todoDetailsActivity.class);
        activityResultLauncher.launch(addTodoIntent);
    }

    public void updateTodo(int pos) {
        Log.i(this.getClass().getName(), "Open Add Todo Dialog.");
        Todo todo = adapter.getTodoAtPosition(pos);

        Intent updateTodoIntent = new Intent(TodoListActivity.this, todoDetailsActivity.class);
        updateTodoIntent.putExtra("ID", todo.getId());
        updateTodoIntent.putExtra("TITLE", todo.getTitle());
        updateTodoIntent.putExtra("DESCRIPTION", todo.getDescription());
        updateTodoIntent.putExtra("DUE_DATE", todo.getDueDate());
        updateTodoIntent.putExtra("IS_DONE", todo.isDone());

        activityResultLauncher.launch(updateTodoIntent);
    }

    public void onSortingSwitchChanged(CompoundButton compoundButton, boolean isChecked) {
        if(isChecked) {
            ((TodoListFromRoomAccessor)this.accessor).setDateFirstSorting();
        } else {
            ((TodoListFromRoomAccessor)this.accessor).setFavouriteFirstSorting();
        }
    }
}