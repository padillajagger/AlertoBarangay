
package com.jagger.alertobarangay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EmergencyReport extends AppCompatActivity {


    FusedLocationProviderClient fusedLocationProviderClient;
    EditText editTextComments;
    String mobile_no;
    double lat;
    double lon;
    AlertDialog alertDialog;
    RadioGroup radioGroup;
    Button btnSelectedRadio;
    String incidentType;
    Button btnReport;
    String date;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_report);


        back_button = findViewById(R.id.back_button);
        radioGroup = findViewById(R.id.radio_group);


        incidentType = "Others";
        date = Calendar.getInstance().getTime().toString();


        alertDialog = new AlertDialog.Builder(EmergencyReport.this).create();
        alertDialog.setTitle("Report");
        alertDialog.setMessage("Report sent!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), History.class);
                        intent.putExtra("mobile_no", mobile_no);
                        startActivity(intent);

                        dialog.dismiss();
                    }
                });

        mobile_no = getIntent().getStringExtra("mobile_no");

        btnReport = findViewById(R.id.btnReport);
        editTextComments = findViewById(R.id.comment);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnReport.setEnabled(false);


                if (ActivityCompat.checkSelfPermission(EmergencyReport.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(EmergencyReport.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    ActivityCompat.requestPermissions(EmergencyReport.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] = grantResults[1]) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {

                        lat = location.getLatitude();
                        lon = location.getLongitude();
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        Report report = new Report(date, lon, lat, editTextComments.getText().toString(), incidentType, mobile_no, "Waiting for Action");
                        database.child("Report").child(date).setValue(report);



                        database.push().setValue(report, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error == null) {
                                    alertDialog.setMessage("Report Sent!");
                                    alertDialog.show();
                                    btnReport.setEnabled(true);
                                } else {
                                    alertDialog.setMessage("Report Sending Failed");
                                    alertDialog.show();
                                    btnReport.setEnabled(true);
                                }
                            }
                        });

                    } else {
                        LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                //super.onLocationResult(locationResult);
                                Location location1 = locationResult.getLastLocation();
                                if (location != null) {
                                    lat = location.getLatitude();
                                    lon = location.getLongitude();
                                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                                    Report report = new Report(date, lon, lat, editTextComments.getText().toString(), incidentType, mobile_no, "Waiting for Action");



                                    database.push().setValue(report, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            if (error == null) {
                                                alertDialog.setMessage("Report Sent!");
                                                alertDialog.show();
                                                btnReport.setEnabled(true);
                                            } else {
                                                alertDialog.setMessage("Report Sending Failed");
                                                alertDialog.show();
                                                btnReport.setEnabled(true);
                                            }
                                        }
                                    });

                                } else {
                                    btnReport.setEnabled(true);
                                    Toast.makeText(EmergencyReport.this, "Location is not ready.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };

                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                    }

                }
            });
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    public void setType(View view) {
        radioGroup.getCheckedRadioButtonId();
        Button radio = findViewById(radioGroup.getCheckedRadioButtonId());
        incidentType = radio.getText().toString();
    }
}

