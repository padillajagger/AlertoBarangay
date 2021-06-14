package com.jagger.alertobarangay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

public class SignUp extends AppCompatActivity {

    FirebaseDatabase firebase;
    DatabaseReference reference;
    TextInputLayout regName, regEmail, regPassword, regAddress, mobile_no, regAge, regGender, regDate_of_birth, regVoters;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        register = findViewById(R.id.registeruser);
        regName = findViewById(R.id.signup_fullname);
        regEmail = findViewById(R.id.signup_email);
        regAge = findViewById(R.id.signup_age);
        regGender = findViewById(R.id.signup_gender);
        regDate_of_birth = findViewById(R.id.signup_date_of_birth);
        regPassword = findViewById(R.id.signup_password);
        regAddress = findViewById(R.id.signup_address);
        mobile_no = findViewById(R.id.mobile_no);
        regVoters = findViewById(R.id.signup_voters_id);

    }

    private boolean validateFullname() {
        String val = regName.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            regName.setError("Field can not be empty");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateMobileNo() {
        String val = mobile_no.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            mobile_no.setError("Mobile number is required.");
            return false;
        } else {
            mobile_no.setError(null);
            mobile_no.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("Field can not be empty");
            return false;
        }else if (!val.matches(checkEmail)){
            regEmail.setError("Invalid Email");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateVoters(){
        String val = regVoters.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            regVoters.setError("Field can not be empty");
            return false;
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("VotersID");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(val)){
                    Toast.makeText(SignUp.this, "Voters ID did not match", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(SignUp.this, "Welcome to Alerto Barangay", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return true;
    }

    private boolean validateAge() {
        String val = regAge.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            regAge.setError("Field can not be empty");
            return false;
        } else {
            regAge.setError(null);
            regAge.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateGender() {
        String val = regGender.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            regGender.setError("Field can not be empty");
            return false;
        } else {
            regGender.setError(null);
            regGender.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateDateofbirth() {
        String val = regDate_of_birth.getEditText().getText().toString().trim();


        if (val.isEmpty()) {
            regDate_of_birth.setError("Field can not be empty");
            return false;
        } else {
            regDate_of_birth.setError(null);
            regDate_of_birth.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateAddress() {
        String val = regAddress.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            regAddress.setError("Field can not be empty");
            return false;
        } else {
            regAddress.setError(null);
            regAddress.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            regPassword.setError("Field can not be empty");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }
    public void callRegisterUser(View view) {
        if (!validateFullname() | !validateEmail() | !validateAddress() | !validatePassword() | !validateMobileNo() | !validateVoters() | !validateAge() | !validateGender() | !validateDateofbirth())  {
            return;
        }

        firebase = FirebaseDatabase.getInstance();
        reference = firebase.getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });

        Intent intent = new Intent(getApplicationContext(),Dashboard.class);
        String name = regName.getEditText().getText().toString().trim();
        String email = regEmail.getEditText().getText().toString().trim();
        String address = regAddress.getEditText().getText().toString().trim();
        String phone = mobile_no.getEditText().getText().toString().trim();
        String age = regAge.getEditText().getText().toString().trim();
        String gender = regGender.getEditText().getText().toString().trim();
        String dateofbirth = regDate_of_birth.getEditText().getText().toString().trim();
        String password = regPassword.getEditText().getText().toString().trim();
        String voters = regVoters.getEditText().getText().toString().trim();
        startActivity(intent);

        UserClass user = new UserClass(name, email, address, phone, age, gender, dateofbirth, password, voters);
        reference.child(voters).setValue(user);


    }
}
