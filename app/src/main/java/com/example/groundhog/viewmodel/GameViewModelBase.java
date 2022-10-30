package com.example.groundhog.viewmodel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.example.groundhog.BR;
import com.example.groundhog.controller.GameActivityController;
import com.example.groundhog.repository.GameRepository;

public abstract class GameViewModelBase extends BaseObservable {
    protected final GameRepository repository = new GameRepository();
    protected String nickname;
    protected int score;
    protected boolean loading;
    protected boolean inputLocked;
    protected GameActivityController activityController;

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

    public void setActivityController(GameActivityController activityController) {
        this.activityController = activityController;
    }
}
