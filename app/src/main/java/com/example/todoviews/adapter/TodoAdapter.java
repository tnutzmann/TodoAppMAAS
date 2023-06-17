package com.example.todoviews.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoviews.R;
import com.example.todoviews.accessor.ITodoListAccessor;
import com.example.todoviews.models.Todo;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;


public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private List<Todo> todoList;
    private Context context;

    private ITodoListAccessor accessor;

    public TodoAdapter(@NonNull Context context, List<Todo> todoList, ITodoListAccessor accessor) {
        this.context = context;
        this.todoList = todoList;
        this.accessor = accessor;
    }

    public Context getContext(){
        return this.context;
    }

    public List<Todo> getTodoList() {
        return this.todoList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Todo todo = todoList.get(position);

        holder.todoTitleText.setText(todo.getTitle());
        holder.todoDueDate.setText(String.format("Due to: %s", new Date(todo.getDueDate()).toLocaleString()));
        holder.todoIsDoneCeckBox.setChecked(todo.isDone());
        holder.todoIsFavouriteCeckBox.setChecked(todo.isFavourite());

        if(todo.getDueDate() < System.currentTimeMillis()) {
            holder.todoTitleText.setTextColor(context.getColor(R.color.red));
        }

        holder.todoIsDoneCeckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.i(this.getClass().getName(), "DoneCheckBox was clicked");
                todo.setDone(isChecked);
                accessor.updateItem(todo);
            }
        });

        holder.todoIsFavouriteCeckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.i(this.getClass().getName(), "FavouritCheckBox was clicked");
                todo.setFavourite(isChecked);
                accessor.updateItem(todo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView todoTitleText;
        TextView todoDueDate;
        CheckBox todoIsDoneCeckBox;
        CheckBox todoIsFavouriteCeckBox;

        ViewHolder(View view) {
            super(view);
            this.setIsRecyclable(false);
            todoTitleText = view.findViewById(R.id.todo_title);
            todoDueDate = view.findViewById(R.id.todo_dueDate);
            todoIsDoneCeckBox = view.findViewById(R.id.todo_isDone);
            todoIsFavouriteCeckBox = view.findViewById(R.id.todo_isFavourite);
        }

    }

    public void deleteTodo(int position) {
        accessor.deleteItem(todoList.get(position));
    }

    public void editTodo(int position) {
        accessor.updateItem(todoList.get(position));
    }

    public Todo lookupItem(Todo item) {
        for (Todo current : this.todoList) {
            if (current.getId() == item.getId()) {
                return current;
            }
        }
        return null;
    }

    public Todo getTodoAtPosition(int pos) {
        return todoList.get(pos);
    }
}
