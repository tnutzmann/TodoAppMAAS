package com.example.todoviews.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoviews.adapter.TodoAdapter;
import org.jetbrains.annotations.NotNull;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private TodoAdapter adapter;
    public RecyclerItemTouchHelper(TodoAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(adapter.getContext());
            alertBuilder.setTitle("Delete TODO");
            alertBuilder.setMessage("Are you sure to delete this TODO?");
            alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int position) {
                    adapter.deleteTodo(position);
                }
            });
        }
    }
}
