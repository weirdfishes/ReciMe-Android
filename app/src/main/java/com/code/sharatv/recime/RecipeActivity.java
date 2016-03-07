package com.code.sharatv.recime;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

public class RecipeActivity extends AppCompatActivity {


    ImageView imageView;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    ListView ingredientsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        String json_string = intent.getStringExtra(RecipeListActivity.RECIPE_OBJ);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ingredientsList = (ListView) findViewById(R.id.ingredients);

        JSONObject recipe = null;

        imageView = (ImageView) findViewById(R.id.image);

        int[] timers = null;
        String[] steps = null;
        String[] ingredients = null;

        try {
            recipe = new JSONObject(json_string);
            toolbar.setTitle(recipe.getString("name"));

            TextView recipeName = (TextView) findViewById(R.id.recipeName);
            recipeName.setText(recipe.getString("name"));

            String url = recipe.getString("imageURL");

            JSONArray j =  recipe.getJSONArray("steps");
            String str = getSteps(j);

            TextView stepsView = (TextView) findViewById(R.id.steps);
            stepsView.setText(str);

            ingredients = getIngredients(recipe.getJSONArray("ingredients"));

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, ingredients);
            ingredientsList.setAdapter(adapter);

            new LoadImage().execute(url);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public String getSteps(JSONArray arr) {
        String str = "";

        for(int i = 0; i < arr.length(); i++) {
            try {
                str = str + arr.getString(i) + "\n\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    public String[] getIngredients(JSONArray arr) {
        String[] str = new String[arr.length()];

        for(int i = 0; i < arr.length(); i++) {
            try {
                JSONObject obj = arr.getJSONObject(i);
                str[i] = obj.getString("quantity") + " \t " + obj.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return str;
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RecipeActivity.this);
            progressDialog.setMessage("Loading Image...");
            progressDialog.show();
        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            if(image != null) {
                imageView.setImageBitmap(image);
                progressDialog.dismiss();
            } else {
                progressDialog.dismiss();
                Toast.makeText(RecipeActivity.this, "Image not found...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
