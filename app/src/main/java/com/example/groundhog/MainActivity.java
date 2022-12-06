package com.example.groundhog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.SharedPreferencesKt;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.databinding.DataBindingUtil;
import com.example.groundhog.databinding.ActivityGameBinding;




public class MainActivity extends AppCompatActivity implements ActivityController {
    private int score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        hideSystemBars();

        ActivityGameBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_game);

        binding.setViewModel(new GameViewModel(this, () -> {
            setContentView(new GameView(this, new RenderThread.GameCallback() {
                @Override
                public void onScore(int s) {
                    showToast(s + "/20");
                    score = s;
                }

                @Override
                public void onOver() {
                    SharedPreferences preferences = getSharedPreferences("", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("nickname", String.valueOf(score));
                    editor.apply();

                    showLeaderboard();
                }
            }));
        }));
        binding.executePendingBindings();

        //binding.getViewModel().tryLoadExistingPlayer();
    }

    private void hideSystemBars() {
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null)
            return;

        // Configure the behavior of the hidden system bars
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
    }

    @Override
    public void showLeaderboard() {
        Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
        startActivity(intent);
    }

    @Override
    public void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}