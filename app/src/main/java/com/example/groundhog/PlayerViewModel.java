package com.example.groundhog;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.example.groundhog.response.RegistrationCallback;

public class PlayerViewModel extends BaseObservable {

    private final PlayerRepository repository;
    private String nickname;
    private int score;
    private boolean loading;

    public PlayerViewModel() {
        this.repository = new PlayerRepository();
    }

    public void register() {
        Player player = new Player(getNickname());
        repository.register(player, new RegistrationCallback() {
            @Override
            public void onSuccess(Player player) {
                // start new game
            }

            @Override
            public void onError(ErrorCode errorCode) {
                // show snackbar with an error
                // user already exists
                // couldn't authorize
            }
        });
    }

    public void checkForExistingPlayer() {
        setLoading(true);
        // TODO: no error handler
        repository.getCachedPlayer(player -> {
            setLoading(false);
            setNickname(player.getNickname());
            setScore(player.getScore());
        });
    }

    @Bindable
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        notifyPropertyChanged(BR.nickname);
    }

    @Bindable
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        notifyPropertyChanged(BR.score);
    }

    @Bindable
    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyPropertyChanged(BR.loading);
    }
}
