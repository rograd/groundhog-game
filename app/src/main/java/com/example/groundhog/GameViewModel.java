package com.example.groundhog;

@FunctionalInterface
interface GameCallback {
    void onStart();
}

public class GameViewModel extends GameViewModelBase {
    private final ActivityController activityController;
    private final GameCallback callback;

    public GameViewModel(ActivityController activityController, GameCallback callback) {
        this.activityController = activityController;
        this.callback = callback;
    }

    public void startGame() {
        setGameStarted(true);
        callback.onStart();
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

    public void tryLoadExistingPlayer() {
        setLoading(true);
 /*       repository.getCurrentPlayer(new GameRepository.CompletionListener() {
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
        });*/
    }

    public void showLeaderboard() {
        activityController.showLeaderboard();
    }
}
