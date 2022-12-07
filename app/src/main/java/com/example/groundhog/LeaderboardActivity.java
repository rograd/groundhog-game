package com.example.groundhog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        TextView leaderboard = findViewById(R.id.leaderboard);

        SharedPreferences preferences = getSharedPreferences("", Context.MODE_PRIVATE);
        Map<String, ?> scores = preferences.getAll();
        String text = "";

        for (Map.Entry<String, ?> entry : scores.entrySet()) {
            text += entry.getKey() + "; " + entry.getValue() + "\n";
        }

        leaderboard.setText(text);

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
