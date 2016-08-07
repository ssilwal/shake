package com.tanishqaggarwal.handshake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);
    }

    public void onSignClick(View v) {
        String usernameText = username.getText().toString();
        if (!usernameText.equals("")) {
            Intent i = new Intent(this, SignActivity.class);
            i.putExtra("username", usernameText);
            startActivity(i);
            finish();
        }
    }

    public void onAuthenticateClick(View v) {
        String usernameText = username.getText().toString();
        if (!usernameText.equals("")) {
            Intent i = new Intent(this, AuthenticateActivity.class);
            i.putExtra("username", usernameText);
            startActivity(i);
            finish();
        }
    }
}
