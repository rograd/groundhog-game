package com.example.groundhog;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

public enum RegistrationState {
    SUCCESS,
    IN_PROGRESS,
    ERROR_PLAYER_EXISTS("Player already exists."),
    ERROR_UNAUTHORIZED("Unauthorized access.");

    private String description;

    RegistrationState() {}

    RegistrationState(String description) {
        this.description = description;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return description;
    }
}
