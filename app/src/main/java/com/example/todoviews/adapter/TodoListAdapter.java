package com.example.todoviews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.todoviews.R;
import com.example.todoviews.models.Todo;

import java.util.ArrayList;

public class TodoListAdapter extends ArrayAdapter<Todo> {
    private ArrayList<Todo> todoList = new ArrayList<>();
    private Context context;

    public TodoListAdapter(@NonNull Context context, ArrayList<Todo> todoList) {
        super(context, R.layout.todo_item , todoList);
        this.todoList = todoList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View todoItem = convertView;
        if(todoItem == null) {
            todoItem = LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false);
        }
        Todo currentTodo = todoList.get(position);

        TextView todo_title_text = (TextView) todoItem.findViewById(R.id.todo_title);
        todo_title_text.setText(currentTodo.getTitle());

        CheckBox todoIsDoneCeckBox = (CheckBox) todoItem.findViewById(R.id.todo_isDone);
        todoIsDoneCeckBox.setChecked(currentTodo.isDone());

        CheckBox todoIsFavouriteCeckBox = (CheckBox) todoItem.findViewById(R.id.todo_isFavourite);
        todoIsFavouriteCeckBox.setChecked(currentTodo.isFavourite());

        /*
        TextView todo_description_text = (TextView) todoItem.findViewById(R.id.todo_description);
        todo_description_text.setText(currentTodo.getDescription());
        */

        return todoItem;
    }
}
