package com.jagger.alertobarangay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    TextInputLayout textInputLayoutUsername;
    TextInputLayout textInputLayoutPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        textInputLayoutUsername = findViewById(R.id.voters);
        textInputLayoutPassword = findViewById(R.id.password);
    }

    public void btnBacK(View view) {
    }

    public void callForgotPassword(View view) {
    }

    public void Login(View view) {
        String username = textInputLayoutUsername.getPrefixText().toString() +  textInputLayoutUsername.getEditText().getText().toString();
        String password = textInputLayoutPassword.getEditText().getText().toString();

        Query ref = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("votersid").equalTo(username);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    String spassword="",key;
                    for (DataSnapshot child: snapshot.getChildren()) {
                        key =child.getKey();
                        spassword= child.child("password").getValue().toString();
                    }


                    if(spassword.equals(password))
                    {

                        Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                        intent.putExtra("votersid",username);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(Login.this, "Voter's ID is not registered.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
