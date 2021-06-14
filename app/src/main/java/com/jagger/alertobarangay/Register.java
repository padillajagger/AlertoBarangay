package com.jagger.alertobarangay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private Button next;
    private TextInputLayout name, votersid, phone, email, password, confirmPassword;
    private FirebaseDatabase firebase;
    private FirebaseAuth fAuth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        firebase = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.fullName);
        votersid = findViewById(R.id.votersID);
        phone = findViewById(R.id.PhoneNumber);
        next = findViewById(R.id.nextBtn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = name.getEditText().getText().toString().trim();
                String vid = votersid.getEditText().getText().toString().trim();
                String email1 = email.getEditText().getText().toString().trim();
                String phoneNumber = phone.getEditText().getText().toString().trim();
                String password1 = password.getEditText().getText().toString().trim();
                String password2 = confirmPassword.getEditText().getText().toString().trim();

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Residents");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        if (!snapshot.hasChild(vid) && !snapshot.hasChild(fname)) {
                            Toast.makeText(Register.this, "Invalid name and voters id", Toast.LENGTH_LONG).show();
                        } else {
                            if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
                                email.setError("Use another email");
                                email.setFocusable(true);
                            }
                            else if (password1.length() <8){
                                password.setError("Must be 8 characters and above");
                            }
                            else if (!password2.equals(password1)){
                                confirmPassword.setError("Passwords do not match.");
                            }
                            else {
                                fAuth.createUserWithEmailAndPassword(email1, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            FirebaseUser firebaseUser = fAuth.getCurrentUser();
                                            String email = firebaseUser.getEmail();
                                            String uid = firebaseUser.getUid();

                                            HashMap<Object, String> hashMap = new HashMap<>();
                                            hashMap.put("email", email1);
                                            hashMap.put("name", fname);
                                            hashMap.put("usersid", uid);
                                            hashMap.put("votersid", vid);
                                            hashMap.put("phone", phoneNumber);
                                            hashMap.put("password", password1);

                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference ref = database.getReference("Users");
                                            ref.child(uid).setValue(hashMap);

                                            Toast.makeText(Register.this, "Registered successfully.", Toast.LENGTH_LONG).show();

                                            Intent intent =  new Intent(getApplicationContext(), PhoneNumberWebAppConnection.class);
                                            intent.putExtra("uid", uid);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });


    }

}