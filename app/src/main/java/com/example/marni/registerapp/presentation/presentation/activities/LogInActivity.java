package com.example.marni.registerapp.presentation.presentation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.marni.registerapp.presentation.asyncklassen.LoginTask;
import static com.example.marni.registerapp.presentation.asyncklassen.LoginTask.UNAUTHORIZED;
import com.example.marni.registerapp.R;

public class LogInActivity extends AppCompatActivity implements LoginTask.SuccessListener {
    private EditText editTextEmail;
    private EditText editTextPassword;
    public static final String JWT_STR = "jwt_str";
    public static final String USER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Button button = (Button) findViewById(R.id.buttonSignIn);
        editTextEmail = (EditText) findViewById(R.id.editTextEmailaddress);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            public void onClick(View v) {
                login("https://mysql-test-p4.herokuapp.com/loginRegister");
            }
        });
    }

    public void successful(String response) {

        if (response.equals(UNAUTHORIZED)) {
            Toast.makeText(this, "Login failed, please try again.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Logged in", Toast.LENGTH_LONG).show();

            JWT jwt = new JWT(response);
            Claim user = jwt.getClaim("user");
            Intent intent = new Intent(getApplicationContext(), RegisterHistoryActivity.class);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putString(JWT_STR, jwt.toString()).apply();
            prefs.edit().putString(USER, String.valueOf(user.asInt())).apply();

            startActivity(intent);
        }
    }


    void login(String apiUrl) {

        LoginTask task = new LoginTask(this);
        String[] urls = new String[]{
                apiUrl, editTextEmail.getText().toString(), editTextPassword.getText().toString(),
        };
        task.execute(urls);
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
