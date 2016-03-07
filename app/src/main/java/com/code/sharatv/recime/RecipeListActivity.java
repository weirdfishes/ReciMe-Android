package com.code.sharatv.recime;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecipeListActivity extends AppCompatActivity {


    TextView t;
    RelativeLayout relativeLayout;
    String result = "";
    ListView listView;

    public static final String RECIPE_OBJ = "Recime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        String countryname = intent.getStringExtra(MainActivity.COUNTRY_NAME);

        toolbar.setTitle(countryname);
        setSupportActionBar(toolbar);


        relativeLayout = (RelativeLayout) findViewById(R.id.content);
        listView = (ListView) findViewById(R.id.listView);
        new MyTask().execute(countryname);


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

    public void handleUI(String result) {

        try{
            final JSONArray jsonArray = new JSONArray(result);

            String[] recipenames = new String[jsonArray.length()];
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);


                recipenames[i] = obj.getString("name");



                /*
                TextView tv = new TextView(this);
                tv.setTextSize(12);
                tv.setText(obj.getString("name"));

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                params.topMargin = 100;

                relativeLayout.addView(tv, params);
                */

            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, recipenames);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {


                    Intent intent = new Intent(RecipeListActivity.this, RecipeActivity.class);

                    try {

                        intent.putExtra(RECIPE_OBJ, jsonArray.getJSONObject(position).toString());
                        startActivity(intent);

                    }catch(JSONException e) {
                        e.printStackTrace();
                    }

                    /*
                    // ListView Clicked item index
                    int itemPosition = position;

                    // ListView Clicked item value
                    String itemValue = (String) listView.getItemAtPosition(position);

                    // Show Alert
                    Toast.makeText(getApplicationContext(),
                            "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                            .show();
                    */

                }

            });

        }catch(JSONException e) {
            e.printStackTrace();
        }

    }


    private class MyTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            URL url;
            try {
                url = new URL(createUrl(params[0]));
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

            }catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("result", result);
            super.onPostExecute(aVoid);



            handleUI(result);

        }
    }

}



        /*
        Intent intent = getIntent();
        String countryname = intent.getStringExtra(MainActivity.COUNTRY_NAME);


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.content);

        //String result = get(createUrl(countryname));

        String r = get();

        t = new TextView(this);
        new MyTask().execute("");

        relativeLayout.addView(t);



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
