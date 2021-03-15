package com.example.hpapp;

import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.os.Bundle;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.Reader;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.util.*;
public class MainActivity extends AppCompatActivity {

    private TextView mSearchResultsDisplay;
    private EditText mSearchTermEditText;
    private Button mSearchButton;
    private Button mResetButton;
    private Button mMoreButton;
    public static String defaultString = "";
    final static String urlString = "http://hp-api.herokuapp.com/api/characters/";
    final static ArrayList<String> houses = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchResultsDisplay   = (TextView) findViewById(R.id.display_text);
        mSearchTermEditText     = (EditText) findViewById(R.id.et_search_box);
        mSearchButton           = (Button) findViewById(R.id.search_button);
        mResetButton            = (Button) findViewById(R.id.reset_button);
        mMoreButton  = (Button) findViewById(R.id.more_button);
        mSearchResultsDisplay.setMovementMethod(new ScrollingMovementMethod());
        houses.add("Gryffindor");
        houses.add("Hufflepuff");
        houses.add("Ravenclaw");
        houses.add("Slytherin");
        mSearchButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        //get search string from user
                        makeNetworkSearchQuery();


                    }

                }
        );
        new setDefault().execute();
        mResetButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        //display default strings
                        mSearchResultsDisplay.setText(defaultString);
                    }

                }
        );
        //This button will direct the user to the next page for more information about harry potter
        mMoreButton.setOnClickListener(
                new View.OnClickListener(){

                    public void onClick(View v){

                        Class destinationActivity = MoreActivity.class;

                        Intent startMoreActivityIntent = new Intent(MainActivity.this, destinationActivity);
                        startActivity(startMoreActivityIntent);
                        Log.d("info", "More Activity launched");
                    }

                }
        );
    }


//Set the default information to be the info of all characters
    public class setDefault extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            JSONArray jsa = null;
            try {
                jsa = readJsonFromURL(urlString);
                String info = "";

                for (int i = 0; i < jsa.size(); i++) {
                    JSONObject json = (JSONObject) jsa.get(i);
                    info += "name: " + json.get("name") + "\nhouse: " +
                            json.get("house") + "\nhairColour: " + json.get("hairColour") + "\ndate of birth: " + json.get("dateOfBirth") + "\n\n";
                }
                result = info;

            } catch (Exception e) {
                e.printStackTrace();
                result = "Error loading the default message: please check your internet setting!";
            }finally{
                return result;
            }
        }

        @Override
        protected void onPostExecute(String responseData){
           defaultString=responseData;
           mSearchResultsDisplay.setText(defaultString);
        }
    }

    public void makeNetworkSearchQuery(){
        String searchTerm = mSearchTermEditText.getText().toString();
        //If the user is searching for a character name, do the following:
        new FetchNetworkData().execute(searchTerm);


    }

    public class FetchNetworkData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params){
            //get the search term
            if(params.length == 0) return null;
            String searchTerm = params[0];

            String result= null;
            try{
                //Get the information string from search
                if(!houses.contains(searchTerm)){
                    result = researchFromJson(searchTerm);
                }else{
                    result = getCharacterNamesHouse(searchTerm);
                }

            }catch(Exception e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String responseData){
            //If the result string is not empty, display the string
            if(responseData!=null){
                mSearchResultsDisplay.setText(responseData);
            }else{
                //If it is empty, return an error message
                mSearchResultsDisplay.setText("Info Not Found!");
            }

        }
    }

    public String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }

        return sb.toString();
    }

    public JSONArray readJsonFromURL(String urlString) throws IOException, Exception {
        JSONObject json = null;
        URL url = new URL(urlString);
        InputStream is = url.openStream();
        JSONArray jsonArr = null;
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader rd = new BufferedReader(isr);
            String jsonText = readAll(rd);

            Object obj = new JSONParser().parse(jsonText);
            jsonArr = (JSONArray) obj;


        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            is.close();
        }
        return jsonArr;
    }	// end of method

    public String researchFromJson(String searchTerm){
        JSONArray jsa = null;
        try{
            //Get the json from the url
            jsa=readJsonFromURL(urlString);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        String info = null;
        //traverse through the json
        for(int i = 0;i<jsa.size();i++){
            JSONObject json = (JSONObject)jsa.get(i);
            if(json.get("name").toString().contains(searchTerm)){
                if(info==null) info = "name: ";
                else info+="name: ";
                info+=json.get("name")+"\nhouse: " +
                        json.get("house") + "\nhairColour: " + json.get("hairColour") + "\ndate of birth: " +  json.get("dateOfBirth") + "\n\n";
            }
        }
        return info;
    }
    public String getCharacterNamesHouse (String searchHouse){
        JSONArray jsa = null;
        try{
            //Get the json from the url
            jsa=readJsonFromURL(urlString);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        String info = null;
        //traverse through the json
        for(int i = 0;i<jsa.size();i++){
            JSONObject json = (JSONObject)jsa.get(i);
            if(json.get("name")==null || json.get("house")==null){
                info = "Wrong Source: does not have house or name attribute";
                break;
            }
            else{
                if(json.get("house").toString().contains(searchHouse)) {
                    if (info == null) info = "name: ";
                    else info += "name: ";
                    info+= json.get("name") + "\n";
                }
            }

        }
        return info;
    }
}
