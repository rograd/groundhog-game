package com.example.groundhog;

import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.databinding.DataBindingUtil;
import com.example.groundhog.databinding.ActivityGameBinding;




public class MainActivity extends AppCompatActivity implements ActivityController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        hideSystemBars();

        ActivityGameBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_game);

        binding.setViewModel(new GameViewModel(this, () -> {
            setContentView(new GameView(this, new RenderThread.GameCallback() {
                @Override
                public void onScore(int score) {
                    showToast(score + "/20");
                }

                @Override
                public void onOver() {

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