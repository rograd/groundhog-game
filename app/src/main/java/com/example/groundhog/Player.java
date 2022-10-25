package com.example.groundhog;


public class Player {
    private String nickname;
    private int score;
    public static final Player nonExistent = new Player();

    public Player() {
    }

    public Player(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public int getScore() {
        return score;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean exists() {
        return this.equals(nonExistent);
    }
}
