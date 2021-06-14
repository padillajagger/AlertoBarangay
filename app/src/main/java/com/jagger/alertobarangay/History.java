package com.jagger.alertobarangay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class History extends AppCompatActivity{


    private RecyclerView recyclerView;
    ReportAdapter adapter;
    DatabaseReference mbase;
    String mobile_no;
    ImageView back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        back_button = findViewById(R.id.back_button);
        mobile_no=getIntent().getStringExtra("mobile_no");
        mbase = FirebaseDatabase.getInstance().getReference("Report");


        mbase.orderByChild("mobile_no").equalTo(mobile_no);

        Query ref = FirebaseDatabase.getInstance().getReference().child("Report").orderByChild("mobile_no").equalTo(mobile_no);
        recyclerView = findViewById(R.id.rvHistory);
        // To display the Recycler view linearly
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Report> options = new FirebaseRecyclerOptions.Builder<Report>()
                .setQuery(ref,new SnapshotParser<Report>(){
                    @NonNull
                    @Override
                    public Report parseSnapshot(@NonNull DataSnapshot snapshot) {
                       Report report= snapshot.getValue(Report.class);
                       report.id=snapshot.getKey();
                        return report;
                    }
                }
            )
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new ReportAdapter(options,this);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);


    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
    public void callDashboard(View view){
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        //Add Transition
        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View, String>(back_button, "transition_back_button");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(History.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }
}