package com.example.harrypotterapptest.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class NetworkUtils {

    public static URL buildUrl(){
        String urlString = "http://hp-api.herokuapp.com/api/characters";
        URL url = null;
        try{
            url = new URL(urlString);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); // getting the connection open
        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A"); // delimiter for end of message
            boolean hasInput = scanner.hasNext();
            if(hasInput) return scanner.next(); // success
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
        return null;
    } // end of get Resp

    public static ArrayList<String> getCharacterNames(String responseString){ //, String attribute){
        ArrayList<String> characterList = new ArrayList<String>();
        try{
            JSONArray allCharactersArray = new JSONArray(responseString);
            for(int i = 0; i < allCharactersArray.length(); i++){
                JSONObject childJson = allCharactersArray.getJSONObject(i);
                if(childJson.has("name")){ //attribute)){
                    String name = childJson.getString("name"); //attribute);
                    if(name != null) characterList.add(name);
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return characterList;
    } // end of parse

    public static ArrayList<String> getCharacterNamesHouse(String responseString, String searchHouse){
        ArrayList<String> characterList = new ArrayList<String>();
        try{
            JSONArray allCharactersArray = new JSONArray(responseString);
            for(int i = 0; i < allCharactersArray.length(); i++){
                JSONObject childJson = allCharactersArray.getJSONObject(i);
                if(childJson.has("house") && childJson.has("name")){
                    String house = childJson.getString("house");
                    String name = childJson.getString("name");
                    if(house != null && name != null){
                        if(house.equals(searchHouse)){
                            characterList.add(name);
                        }
                    }
                    else
                    {
                        System.out.println("Wrong Source: does not have house or name attribute");
                        break;
                    }
                }
                else
                {
                    System.out.println("Wrong Source: does not have house or name attribute");
                    break;
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return characterList;
    } // end of parse
}

