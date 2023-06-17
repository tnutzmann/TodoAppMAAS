package com.example.todoviews;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;

public class todoDetailsActivity extends AppCompatActivity {
    private Intent caller;
    private EditText todoTitleEdit;
    private EditText todoDescriptionEdit;
    private DatePicker todoDatePicker;
    private TimePicker todoTimePicker;
    private Button saveButton;
    private Button backButton;

    // todo-data
    private long todoId;
    private long todoDueDateTimestamp;
    private Date todoDueDate;
    private String todoTitle;
    private String todoDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_details);

        // instantiate UI elements
        todoTitleEdit = findViewById(R.id.detailsTodoTitle);
        todoDescriptionEdit = findViewById(R.id.detailsTodoDescription);
        todoDatePicker = findViewById(R.id.detailsTodoDate);
        todoTimePicker = findViewById(R.id.detailsTodoTime);
        saveButton = findViewById(R.id.todoDetailsSaveButton);
        backButton = findViewById(R.id.todoDetailsBackButton);

        // get todo data
        caller = getIntent();
        todoId = caller.getLongExtra("ID", -1);
        todoDueDateTimestamp = caller.getLongExtra("DUE_DATE", System.currentTimeMillis());
        todoDueDate = new Date(todoDueDateTimestamp);
        todoTitle = caller.getStringExtra("TITLE");
        if(todoTitle == null) todoTitle = "";
        todoDescription = caller.getStringExtra("DESCRIPTION");
        if(todoDescription == null) todoDescription = "";

        // write data to UI elements
        todoTitleEdit.setText(todoTitle);
        todoDescriptionEdit.setText(todoDescription);

        todoDatePicker.updateDate(todoDueDate.getYear() + 1900, todoDueDate.getMonth(), todoDueDate.getDate());

        todoTimePicker.setIs24HourView(true);
        todoTimePicker.setHour(todoDueDate.getHours());
        todoTimePicker.setMinute(todoDueDate.getMinutes());

        // register button events
        saveButton.setOnClickListener(this::saveTodo);
        backButton.setOnClickListener(this::abort);
    }

    void updateTodoValues() {
        todoTitle = todoTitleEdit.getText().toString();
        todoDescription = todoDescriptionEdit.getText().toString();

        todoDueDate.setTime(0);

        todoDueDate.setYear(todoDatePicker.getYear() - 1900);
        todoDueDate.setMonth(todoDatePicker.getMonth());
        todoDueDate.setDate(todoDatePicker.getDayOfMonth());

        todoDueDate.setSeconds(0);
        todoDueDate.setMinutes(todoTimePicker.getMinute());
        todoDueDate.setHours(todoTimePicker.getHour());

        todoDueDateTimestamp = todoDueDate.getTime();
    }

    void saveTodo(View v) {
        updateTodoValues();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("ID", todoId);
        resultIntent.putExtra("TITLE", todoTitle);
        resultIntent.putExtra("DESCRIPTION", todoDescription);
        resultIntent.putExtra("DUE_DATE", todoDueDateTimestamp);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    void abort(View v) {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }
}