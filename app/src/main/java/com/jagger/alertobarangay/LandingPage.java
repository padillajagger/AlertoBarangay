package com.jagger.alertobarangay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_landing_page);

    }

    public void callLogin(View view) {
        Intent intent = new Intent(LandingPage.this,Login.class);
        startActivity(intent);
    }

    public void callSignUp(View view) {
        Intent intent = new Intent(getApplicationContext(),Register.class);
        startActivity(intent);
    }

    public void callNextSignup(View view) {
    }
}