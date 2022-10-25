package com.example.groundhog;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import com.example.groundhog.databinding.ActivityPlayerBinding;


public class PlayerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPlayerBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        binding.setViewModel(new PlayerViewModel());
        binding.executePendingBindings();

        binding.getViewModel().checkForExistingPlayer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.leaderboard_menu) {
            Intent intent = new Intent(PlayerActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}