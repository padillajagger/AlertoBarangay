package com.jagger.alertobarangay;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class PhoneNumberWebAppConnection extends AppCompatActivity {
    private WebView webView;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_web_app_connection);

        userid = getIntent().getStringExtra("uid");

        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://register-alerto-barangay.netlify.app/?UID="+userid);
    }
}