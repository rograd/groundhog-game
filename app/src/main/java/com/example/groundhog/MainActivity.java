package com.example.groundhog;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import com.example.groundhog.model.Player;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private EditText edtNickname;
    private Button btnBegin;
    private ProgressBar pgbLoading;
    private MainViewModel viewModel;
    private TextView txvScore;
    private LinearLayout llScore;
    private View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.checkForExistingPlayer();

        initObservers();
    }

    private void findViews() {
        edtNickname = findViewById(R.id.edtNickname);
        txvScore = findViewById(R.id.txvScore);
        btnBegin = findViewById(R.id.btnBegin);
        pgbLoading = findViewById(R.id.pgbLoading);
        llScore = findViewById(R.id.llScore);
        root = findViewById(R.id.root);
    }

    private final TextWatcher inputValidator = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // no spaces regex
            btnBegin.setEnabled(charSequence.length() > 3);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void initObservers() {
        viewModel.getRegistrationState().observe(this, state -> {
            switch (state) {
                case IN_PROGRESS:
                    pgbLoading.setVisibility(View.VISIBLE);
                    break;

                    // TODO: change error codes into one error
                case ERROR_UNAUTHORIZED:
                case ERROR_PLAYER_EXISTS:
                    Snackbar.make(root, state.toString(), Snackbar.LENGTH_LONG).show();
                    break;

                case SUCCESS:
                    // start game
                    break;

                default:
                    break;
            }
        });

        viewModel.getCachedPlayer().observe(this, player -> {
            if (!player.equals(Player.nonExistent))
                updateViewForKnownPlayer(player);
            else
                updateViewForUnknownPlayer();
        });
    }

    private void updateViewForKnownPlayer(Player player) {
        edtNickname.setText(player.getNickname());
        int score = player.getScore();
        txvScore.setText(String.valueOf(score));
        llScore.setVisibility(View.VISIBLE);
        btnBegin.setEnabled(true);
        // jeden listener nickname=null
        btnBegin.setOnClickListener(view -> {
            viewModel.login();
        });
    }

    private void updateViewForUnknownPlayer() {
        edtNickname.setEnabled(true);
        edtNickname.addTextChangedListener(inputValidator);
        btnBegin.setOnClickListener(view -> {
            String nickname = edtNickname.getText().toString();
            viewModel.register(nickname);
        });
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
            // show leaderboard
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}