package com.example.hpapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MoreActivity extends AppCompatActivity {
    private Button mWebsiteButton;
    private Button mBackButton;
    final static String websiteUrl = "https://www.wizardingworld.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        mWebsiteButton            = (Button) findViewById(R.id.website_button);
        mBackButton = (Button) findViewById(R.id.back_button);
        //This button will direct the user to pottermore website: a harry potter fans website
        mWebsiteButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        openWebPage(websiteUrl);
                    }
                }
        );
        //This button will direct the user back to search page
        mBackButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Class destinationActivity = MainActivity.class;

                        Intent startMoreActivityIntent = new Intent(MoreActivity.this, destinationActivity);
                        startActivity(startMoreActivityIntent);
                        Log.d("info", "More Activity launched");


                    }

                }
        );
    }
    public void openWebPage(String urlString){
        Uri webpage = Uri.parse(urlString);

        Intent openWebPageIntent = new Intent(Intent.ACTION_VIEW, webpage);
        // check if that intent can be launched, and then launch it
        if(openWebPageIntent.resolveActivity(getPackageManager()) != null){
            startActivity(openWebPageIntent);
        }
    }
}
