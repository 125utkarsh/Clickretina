package com.example.clickretina;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
}
