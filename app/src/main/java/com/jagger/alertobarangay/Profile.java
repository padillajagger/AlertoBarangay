package com.jagger.alertobarangay;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView emailTextView = findViewById(R.id.email);
        final TextView addressTextView = findViewById(R.id.address);
        final TextView nameTextView = findViewById(R.id.name);
        final TextView emailaddressTextView = findViewById(R.id.emailaddress);
        final TextView phonnumberTextView = findViewById(R.id.phonenumber);
        final TextView votersTextView = findViewById(R.id.voters);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserClass userClass = snapshot.getValue(UserClass.class);

                if (userClass !=null){
                    String fullname = userClass.name;
                    String email = userClass.email;
                    String address = userClass.address;
                    String phone = userClass.phone;
                    String voters = userClass.voters;

                    votersTextView.setText(voters);
                    phonnumberTextView.setText(phone);
                    emailTextView.setText(email);
                    addressTextView.setText(address);
                    nameTextView.setText(""+ fullname + "");
                    emailaddressTextView.setText(""+ email + "");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }
}