package com.jagger.alertobarangay;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class OtpVerification extends AppCompatActivity {
    String mVerificationId;
    PhoneAuthCredential credential;
    FirebaseAuth auth;
    Activity me;
    String TAG="OTP";
    String name,address,email,password,mobile_no,age,gender,date_of_birth;
    EditText otp;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        requestReadPermission();


        alertDialog = new AlertDialog.Builder(OtpVerification.this).create();
        alertDialog.setTitle("OTP");
        alertDialog.setMessage("Invalid Code.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        auth = FirebaseAuth.getInstance();
        me=this;
        name=getIntent().getStringExtra("name");
        address=getIntent().getStringExtra("address");
        email=getIntent().getStringExtra("email");
        age=getIntent().getStringExtra("age");
        gender=getIntent().getStringExtra("gender");
        date_of_birth=getIntent().getStringExtra("date_of_birth");
        password=getIntent().getStringExtra("password");
        mobile_no=getIntent().getStringExtra("mobile_no");

        otp = findViewById(R.id.otp);

        new Receive_SMS().setEditText(otp);
        request_Otp();



        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otp.getText().toString();
                credential = PhoneAuthProvider.getCredential(mVerificationId, code);

                signInWithPhoneAuthCredential(credential);
            }
        });
    }

    private void requestReadPermission() {
        if(ContextCompat.checkSelfPermission(OtpVerification.this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(OtpVerification.this,new String[]{Manifest.permission.RECEIVE_SMS},200);
        }
    }

    private void request_Otp()
    {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(mobile_no)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(me)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Log.d(TAG, "onVerificationCompleted:");
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Log.d("OTP",e.getMessage() );
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                // The SMS verification code has been sent to the provided phone number, we
                                // now need to ask the user to enter the code and then construct a credential
                                // by combining the code with a verification ID.
                                //Toast.makeText(me, verificationId, Toast.LENGTH_SHORT).show();

                                // Save verification ID and resending token so we can use them later
                                mVerificationId = verificationId;
                                PhoneAuthProvider.ForceResendingToken mResendToken = token;


                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Toast.makeText(OtpVerification.this, "success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),Login.class);
                            intent.putExtra("mobile_no",mobile_no);
                            startActivity(intent);
                            //FirebaseUser user = task.getResult().getUser();
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                            User user = new User();
                            user.name=name;
                            user.address=address;
                            user.email=email;
                            user.age=age;
                            user.gender=gender;
                            user.date_of_birth=date_of_birth;
                            user.password=password;
                            user.mobile_no=mobile_no;

                            database.child("Users").child(mobile_no).setValue(user);
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                               // Toast.makeText(OtpVerification.this, "Invalid Code.", Toast.LENGTH_SHORT).show();
                               alertDialog.show();
                            }
                        }
                    }
                });
    }



    public static class User {

        public String name;
        public String email;
        public String mobile_no;
        public String address;
        public String password;
        public String gender;
        public String date_of_birth;
        public String age;

        public User(String name, String email, String mobile_no, String address, String password, String age, String gender, String date_of_birth) {
            this.name = name;
            this.email = email;
            this.mobile_no = mobile_no;
            this.address = address;
            this.password = password;
            this.age = age;
            this.gender = gender;
            this.date_of_birth = date_of_birth;

        }

        public User() {}
    }
}