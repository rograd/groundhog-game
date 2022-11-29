package com.example.groundhog;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public abstract class GameViewModelBase extends BaseObservable {
    protected final GameRepository repository = new GameRepository();
    protected String nickname;
    protected int score;
    protected boolean loading;
    protected boolean inputLocked;
    protected boolean gameStarted;

    @Bindable
    public String getNickname() {
        return nickname;
    }

    @Bindable
    public int getScore() {
        return score;
    }

    @Bindable
    public boolean isInputLocked() {
        return inputLocked || loading;
    }

    @Bindable
    public boolean isLoading() {
        return loading;
    }

    @Bindable
    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        notifyPropertyChanged(BR.nickname);
    }

    public void setScore(int score) {
        this.score = score;
        notifyPropertyChanged(BR.score);
    }

    public void setInputLocked(boolean inputLocked) {
        this.inputLocked = inputLocked;
        notifyPropertyChanged(BR.inputLocked);
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyPropertyChanged(BR.loading);
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
        notifyPropertyChanged(BR.gameStarted);
    }
}
