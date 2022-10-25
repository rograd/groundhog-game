package com.example.groundhog.response;

import com.example.groundhog.ErrorCode;
import com.example.groundhog.Player;

public interface RegistrationCallback {
    void onSuccess(Player player);
    void onError(ErrorCode errorCode);
}