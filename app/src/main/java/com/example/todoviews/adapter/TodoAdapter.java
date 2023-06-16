package com.example.todoviews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoviews.R;
import com.example.todoviews.models.Todo;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private List<Todo> todoList;
    private Context context;

    public TodoAdapter(@NonNull Context context, List<Todo> todoList) {
        this.context = context;
        this.todoList = todoList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.todoTitleText.setText(todo.getTitle());
        holder.todoIsDoneCeckBox.setChecked(todo.isDone());
        holder.todoIsFavouriteCeckBox.setChecked(todo.isFavourite());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView todoTitleText;
        CheckBox todoIsDoneCeckBox;
        CheckBox todoIsFavouriteCeckBox;

        ViewHolder(View view) {
            super(view);
            todoTitleText = view.findViewById(R.id.todo_title);
            todoIsDoneCeckBox = view.findViewById(R.id.todo_isDone);
            todoIsFavouriteCeckBox = view.findViewById(R.id.todo_isFavourite);
        }

    }
}
