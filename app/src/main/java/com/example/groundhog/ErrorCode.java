package com.example.groundhog;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

public enum ErrorCode {
    COULD_NOT_AUTHORIZE("Couldn't authorize, please sign up."),
    NICKNAME_IN_USE("Nickname's already in use."),
    DATABASE_ERROR("Database error.");

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
