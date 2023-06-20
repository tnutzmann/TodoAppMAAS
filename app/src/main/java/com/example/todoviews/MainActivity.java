package com.example.todoviews;

import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.todoviews.accessor.TodoItemCRUDAccessor;
import com.example.todoviews.accessor.UserCRUDAccessor;
import com.example.todoviews.models.User;

public class MainActivity extends AppCompatActivity {
    protected static String logger = TodoItemCRUDAccessor.class.getName();

    private EditText emailEdit;
    private EditText passwordEdit;
    private TextView emailErrorText;
    private TextView passwordErrorText;
    private TextView loginErrorText;
    private Button loginButton;
    private Boolean emailIsValid = false;
    private Boolean passwordIsValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new WebServiceAvailableCheckAsyncTask().execute();

        emailEdit = findViewById(R.id.emailEditText);
        emailErrorText = findViewById(R.id.emailErrorTextView);
        passwordEdit = findViewById(R.id.passwordEditText);
        passwordErrorText = findViewById(R.id.passwordErrorTextView);

        loginButton = findViewById(R.id.loginBtn);
        loginButton.setEnabled(false);
        loginButton.setOnClickListener(this::onLoginButtonClick);
        loginErrorText = findViewById(R.id.loginErrorTextView);

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
        User user = new User(0, emailEdit.getText().toString(), passwordEdit.getText().toString());
        UserCRUDAccessor loginClient = new UserCRUDAccessor(getString(R.string.WebServiceURL));
        new LoginAsyncTask().execute(user);
    }

    private void checkLoginActivation() {
        loginButton.setEnabled(passwordIsValid && emailIsValid);
        loginErrorText.setVisibility(View.INVISIBLE);
    }

    private class LoginAsyncTask extends AsyncTask<User, Void, Boolean> {
        private UserCRUDAccessor loginClient = new UserCRUDAccessor(getString(R.string.WebServiceURL));
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = ProgressDialog.show(MainActivity.this, "Login...", "Please wait.", false, true);
        }

        @Override
        protected Boolean doInBackground(User... users) {
            boolean loginSuccess = false;
            for(User u : users) {
                Log.i(logger, String.format("Password: %s. EMAIL: %s", u.getPassword(), u.getEmail()));
                loginSuccess = loginClient.checkPassword(u);
            }
            return loginSuccess;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            this.progressDialog.cancel();
            if(aBoolean == true) {
                Log.i(logger, "Login Successful");
                startActivity(new Intent(MainActivity.this, TodoListActivity.class));
            } else {
                Log.i(logger, "Login Unsuccessful");
                loginErrorText.setVisibility(View.VISIBLE);
            }
        }
    }

    private class WebServiceAvailableCheckAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private UserCRUDAccessor loginClient = new UserCRUDAccessor(getString(R.string.WebServiceURL));
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                loginClient.checkPassword(new User(0, "Just for Testingreasosn", "Probably this could be made in a better way..."));
            } catch (Exception e) {
                Log.i(logger, "Webservice not available!");
                return false;
            }
            Log.i(logger, "Webservice available!");
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(!aBoolean) startActivity(new Intent(MainActivity.this, TodoListActivity.class));
        }
    }
}
