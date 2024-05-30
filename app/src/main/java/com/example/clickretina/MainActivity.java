package com.example.clickretina;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private InfographicCircleView infographicCircleView;
    private TextView descriptionTextView;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infographicCircleView = findViewById(R.id.infographic_circle);
        descriptionTextView = findViewById(R.id.description_text_view);
        titleTextView = findViewById(R.id.title_text_view);


        // Set progress for InfographicCircleView
        int progress = 53; // Replace with dynamic progress value if needed
        infographicCircleView.setProgress(progress);
        infographicCircleView.setMax(100);

        // Fetch data on background thread
        new FetchDataTask().execute();

        Button copyTitleButton = findViewById(R.id.copy_button);
        Button shareTitleButton = findViewById(R.id.share_button);
        Button copyDescriptionButton = findViewById(R.id.copy);
        Button shareDescriptionButton = findViewById(R.id.share);

        copyDescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(descriptionTextView.getText().toString());
            }
        });

        shareDescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareText(descriptionTextView.getText().toString());
            }
        });

        copyTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(titleTextView.getText().toString());
            }
        });

        shareTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareText(titleTextView.getText().toString());
            }
        });

    }

    private class FetchDataTask extends AsyncTask<Void, Void, ApiResponse> {

        @Override
        protected ApiResponse doInBackground(Void... voids) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://run.mocky.io")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);

            try {
                Response<ApiResponse> response = apiService.getData().execute();
                if (response.isSuccessful() && response.body() != null) {
                    return response.body();
                } else {
                    return null;
                }
            } catch (Exception e) {
                Log.e("MainActivity", "API call failed", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ApiResponse result) {
            if (result != null && result.getChoices().length > 0) {
                ApiResponse.Choice choice = result.getChoices()[0];
                if (choice != null && choice.getMessage() != null) {
                    JsonObject contentJson = new Gson().fromJson(choice.getMessage().getContent(), JsonObject.class);
                    String title = contentJson.has("title") ? contentJson.get("title").getAsString() : "No title available";
                    String description = contentJson.has("description") ? contentJson.get("description").getAsString() : "No description available";
                    titleTextView.setText(title);
                    descriptionTextView.setText(description);
                } else {
                    titleTextView.setText("No data available");
                    descriptionTextView.setText("No data available");
                }
            } else {
                titleTextView.setText("No data available");
                descriptionTextView.setText("No data available");
            }
        }


    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Text", text);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(MainActivity.this, "Text copied", Toast.LENGTH_SHORT).show();
    }

    private void shareText(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(shareIntent, "Share Text"));
    }


}
