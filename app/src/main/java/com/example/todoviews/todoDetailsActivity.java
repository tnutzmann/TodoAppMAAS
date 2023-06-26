package com.example.todoviews;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;

public class todoDetailsActivity extends AppCompatActivity {
    private Intent caller;
    private EditText todoTitleEdit;
    private EditText todoDescriptionEdit;
    private DatePicker todoDatePicker;
    private TimePicker todoTimePicker;
    private CheckBox todoIsDoneCheckBox;
    private CheckBox todoIsFavouriteCheckBox;
    private Button saveButton;
    private Button backButton;
    private Button deleteButton;

    // todo-data
    private long todoId;
    private long todoDueDateTimestamp;
    private Date todoDueDate;
    private String todoTitle;
    private String todoDescription;
    private boolean todoIsDone;
    private boolean todoIsFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_details);

        // instantiate UI elements
        todoTitleEdit = findViewById(R.id.detailsTodoTitle);
        todoDescriptionEdit = findViewById(R.id.detailsTodoDescription);
        todoDatePicker = findViewById(R.id.detailsTodoDate);
        todoTimePicker = findViewById(R.id.detailsTodoTime);
        todoIsDoneCheckBox = findViewById(R.id.detailsTodoIsDoneCheckBox);
        todoIsFavouriteCheckBox = findViewById(R.id.detailsTodoIsFavouriteCheckBox);

        saveButton = findViewById(R.id.todoDetailsSaveButton);
        backButton = findViewById(R.id.todoDetailsBackButton);
        deleteButton = findViewById(R.id.todoDetailsDeleteButton);

        // get todo data
        caller = getIntent();
        todoId = caller.getLongExtra("ID", -1);
        todoDueDateTimestamp = caller.getLongExtra("DUE_DATE", System.currentTimeMillis());
        todoDueDate = new Date(todoDueDateTimestamp);
        todoTitle = caller.getStringExtra("TITLE");
        if(todoTitle == null) todoTitle = "";
        todoDescription = caller.getStringExtra("DESCRIPTION");
        if(todoDescription == null) todoDescription = "";
        todoIsDone = caller.getBooleanExtra("IS_DONE", false);
        todoIsFavourite = caller.getBooleanExtra("IS_FAV", false);


        // write data to UI elements
        todoTitleEdit.setText(todoTitle);
        todoDescriptionEdit.setText(todoDescription);

        todoDatePicker.updateDate(todoDueDate.getYear() + 1900, todoDueDate.getMonth(), todoDueDate.getDate());

        todoTimePicker.setIs24HourView(true);
        todoTimePicker.setHour(todoDueDate.getHours());
        todoTimePicker.setMinute(todoDueDate.getMinutes());

        todoIsDoneCheckBox.setChecked(todoIsDone);
        todoIsFavouriteCheckBox.setChecked(todoIsFavourite);

        // register button events
        saveButton.setOnClickListener(this::saveTodo);
        backButton.setOnClickListener(this::abort);
        deleteButton.setOnClickListener(this::deleteTodo);

        if(todoId == -1) {
            // -1 means new todoItem will be created so no Deletebutton is needed
            deleteButton.setVisibility(View.GONE);
        }
    }

    private void updateTodoValues() {
        todoTitle = todoTitleEdit.getText().toString();
        todoDescription = todoDescriptionEdit.getText().toString();
        // date and Time handling
        todoDueDate.setTime(0);
        todoDueDate.setYear(todoDatePicker.getYear() - 1900);
        todoDueDate.setMonth(todoDatePicker.getMonth());
        todoDueDate.setDate(todoDatePicker.getDayOfMonth());
        todoDueDate.setSeconds(0);
        todoDueDate.setMinutes(todoTimePicker.getMinute());
        todoDueDate.setHours(todoTimePicker.getHour());
        todoDueDateTimestamp = todoDueDate.getTime();

        todoIsDone = todoIsDoneCheckBox.isChecked();
        todoIsFavourite = todoIsFavouriteCheckBox.isChecked();
    }

    private void saveTodo(View v) {
        updateTodoValues();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("ID", todoId);
        resultIntent.putExtra("TITLE", todoTitle);
        resultIntent.putExtra("DESCRIPTION", todoDescription);
        resultIntent.putExtra("DUE_DATE", todoDueDateTimestamp);
        resultIntent.putExtra("IS_DONE", todoIsDone);
        resultIntent.putExtra("IS_FAV", todoIsFavourite);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void deleteTodo(View v) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("DELETE TDOD");
        alertBuilder.setMessage("Are you sure to delete this TODO");
        alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ID", todoId);
                resultIntent.putExtra("DELETED", true);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        alertBuilder.setNegativeButton("Cancel", null);
        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }

    private void abort(View v) {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }
}