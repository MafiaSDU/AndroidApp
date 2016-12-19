package kz.edu.sdu.galix.mafia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button loginButton;
    EditText usernameEdit;
    Intent intent;
    ConnectionToServer connection;
    SharedPreferences spf;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEdit = (EditText) findViewById(R.id.username);
        loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(this);

        spf = getSharedPreferences("data",MODE_PRIVATE);
        connection = new ConnectionToServer(this, spf);
        setTitle("Login");

    }

    @Override
    public void onClick(View view) {
        String username = usernameEdit.getText().toString();
        if (loginButton.getId() == view.getId()) {
            if (username.length() != 0) {
                intent = new Intent();
                intent.putExtra("user_name",username );
                setResult(RESULT_OK, intent);
                finish();
                HashMap<String,String> user = new HashMap<>();
                user.put("name", usernameEdit.getText().toString());
                connection.Connect("api/user/add",user, Request.Method.POST);
            } else {
                Toast.makeText(LoginActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
