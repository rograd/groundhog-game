package com.example.groundhog.viewmodel;

import com.example.groundhog.repository.GameRepository;
import com.example.groundhog.utils.ErrorCode;
import com.example.groundhog.model.Player;

public class GameViewModel extends GameViewModelBase {
    public void beginGame() {
        setLoading(true);
        Player player = new Player(getNickname());
        repository.registerPlayer(player, new GameRepository.CompletionListener() {
            @Override
            public void onSuccess(Player player) {
                setLoading(false);
                activityController.showToast(player.getNickname());
            }

            @Override
            public void onError(ErrorCode errorCode) {
                setLoading(false);
                activityController.showToast(errorCode.toString());
                if (errorCode == ErrorCode.COULD_NOT_AUTHORIZE) {
                    setInputLocked(false);
                    setNickname("");
                    setScore(0);
                }
            }
        });
    }

    public void tryLoadExistingPlayer() {
        setLoading(true);
        repository.getCurrentPlayer(new GameRepository.CompletionListener() {
            @Override
            public void onSuccess(Player player) {
                setLoading(false);
                setInputLocked(true);
                setNickname(player.getNickname());
                setScore(player.getScore());
            }

            @Override
            public void onError(ErrorCode errorCode) {
                setLoading(false);
            }
        });
    }

    public void showLeaderboard() {
        activityController.showLeaderboard();
    }
}
