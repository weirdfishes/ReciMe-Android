package com.code.sharatv.recime;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecipeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String countryname = intent.getStringExtra(MainActivity.COUNTRY_NAME);


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.content);

        //String result = get(createUrl(countryname));

        String r = get();

        TextView t = new TextView(this);
        t.setText(r);

        relativeLayout.addView(t);

        /*

        try {
            JSONArray jsonArray = new JSONArray(r);

            for(int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);

                TextView tv = new TextView(this);
                tv.setTextSize(15);
                tv.setText(obj.getString("name"));

                relativeLayout.addView(tv);

            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
        */
    }

    private String createUrl(String s) {

        String url = "https://lets-grub.herokuapp.com/recipes?country=";

        if (!s.contains(" "))
            url += s;

        else {
            String[] split = s.split(" ");

            for(int i = 0; i < split.length; i++) {
                url += split[i];
                if(i != split.length-1)
                    url += "%20";
            }
        }

        return url;
    }

    public String get() {

        URL url;
        String result = "";

        try {
            url = new URL("https://lets-grub.herokuapp.com/recipes?country=United%20States%20of%20America");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String output;
            while((output = br.readLine()) != null) {
                result = result.concat(output);
            }
            urlConnection.disconnect();

            return result;

        }catch(Exception e) {
            e.printStackTrace();
        }

        return result;

    }
}