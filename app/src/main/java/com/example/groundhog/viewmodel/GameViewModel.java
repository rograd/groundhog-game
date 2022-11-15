package com.example.groundhog.viewmodel;

import com.example.groundhog.controller.GameActivityController;
import com.example.groundhog.repository.GameRepository;
import com.example.groundhog.utils.ErrorCode;
import com.example.groundhog.model.Player;
import com.example.groundhog.controller.GameController;

public class GameViewModel extends GameViewModelBase {
    public void startGame() {
        setGameStarted(true);
        gameController.onStart();
//        setLoading(true);
//        Player player = new Player(getNickname());
//        repository.registerPlayer(player, new GameRepository.CompletionListener() {
//            @Override
//            public void onSuccess(Player player) {
//                setLoading(false);
//                // activityController.showToast(player.getNickname());
//                // gameController.startGame();
//            }
//
//            @Override
//            public void onError(ErrorCode errorCode) {
//                setLoading(false);
//                activityController.showToast(errorCode.toString());
//                if (errorCode == ErrorCode.COULD_NOT_AUTHORIZE) {
//                    setInputLocked(false);
//                    setNickname("");
//                    setScore(0);
//                }
//            }
//        });
    }

    public GameViewModel(GameActivityController activityController, GameController gameController) {
        this.activityController = activityController;
        this.gameController = gameController;
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
