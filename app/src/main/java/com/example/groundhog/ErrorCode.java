package com.example.groundhog;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

public enum ErrorCode {
    UNAUTHORIZED_PLAYER("Unauthorized player."),
    NICKNAME_IN_USE("Nickname already in use.");

    private final String description;

    ErrorCode(String description) {
        this.description = description;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return this.description;
    }
}
