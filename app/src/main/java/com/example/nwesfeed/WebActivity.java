package com.example.nwesfeed;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {
    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView=findViewById(R.id.web_page);

        try {
            Intent intent=getIntent();
            String url=intent.getStringExtra("url");
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else{
        super.onBackPressed();
        }
    }
}
