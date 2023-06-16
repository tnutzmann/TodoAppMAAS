package com.example.todoviews;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    EditText emailEdit;
    EditText passwordEdit;
    TextView emailErrorText;
    TextView passwordErrorText;
    TextView loginErrorText;
    Button loginButton;
    ProgressBar progressBar;
    Boolean emailIsValid = false;
    Boolean passwordIsValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEdit = findViewById(R.id.emailEditText);
        emailErrorText = findViewById(R.id.emailErrorTextView);
        passwordEdit = findViewById(R.id.passwordEditText);
        passwordErrorText = findViewById(R.id.passwordErrorTextView);

        loginButton = findViewById(R.id.loginBtn);
        loginButton.setEnabled(false);
        loginButton.setOnClickListener(this::onLoginButtonClick);
        loginErrorText = findViewById(R.id.loginErrorTextView);
        progressBar = findViewById(R.id.loginProgressBar);


        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {   }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {   }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!Patterns.EMAIL_ADDRESS.matcher(emailEdit.getText().toString()).matches()) {
                    emailErrorText.setVisibility(View.VISIBLE);
                    emailIsValid = false;
                } else {
                    emailErrorText.setVisibility(View.INVISIBLE);
                    emailIsValid = true;
                }
                checkLoginActivation();
            }
        });

        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {   }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {   }

            @Override
            public void afterTextChanged(Editable editable) {
                String passwd = passwordEdit.getText().toString();
                if(passwd.length() == 6 && isInteger(passwd)) {
                    passwordErrorText.setVisibility(View.INVISIBLE);
                    passwordIsValid = true;
                } else {
                    passwordErrorText.setVisibility(View.VISIBLE);
                    passwordIsValid = false;
                }
                checkLoginActivation();
            }

            public boolean isInteger(String s) {
                try {
                    Integer.parseInt(s);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    private void onLoginButtonClick(View v) {
        Log.i(MainActivity.class.getName(), String.format("Password: %d. EMAIL: %d", Integer.parseInt(passwordEdit.getText().toString()), emailEdit.getText().toString().length() ));
        progressBar.setVisibility(View.VISIBLE);
        new CountDownTimer(3000,100){
            int progress = 0;
            @Override
            public void onTick(long l) {
                progress = 100 - (int) (l/100);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                if(Integer.parseInt(passwordEdit.getText().toString()) == emailEdit.getText().toString().length()) {
                    loginErrorText.setVisibility(View.VISIBLE);
                } else {
                    Log.i(MainActivity.class.getName(), "onLoginButtonClick() " + v);
                    startActivity(new Intent(MainActivity.this, TodoListActivity.class));
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    private void checkLoginActivation() {
        loginButton.setEnabled(passwordIsValid && emailIsValid);
        loginErrorText.setVisibility(View.INVISIBLE);
    }
}