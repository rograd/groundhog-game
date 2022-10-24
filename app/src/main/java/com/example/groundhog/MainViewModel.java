package com.example.groundhog;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.groundhog.model.Player;

public class MainViewModel extends ViewModel {
    private final MainRepository mainRepository;
    private final MutableLiveData<RegistrationState> registrationState = new MutableLiveData<>();
    private final MutableLiveData<Player> cachedPlayer = new MutableLiveData<>();


    public MainViewModel() {
        mainRepository = new MainRepository();
    }

    public void register(String nickname) {
        registrationState.postValue(RegistrationState.IN_PROGRESS);
        Player player = new Player(nickname);
        mainRepository.register(player, new MainRepository.AuthorizerCallback() {
            @Override
            public void onSuccess() {
                registrationState.postValue(RegistrationState.SUCCESS);
            }

            @Override
            public void onError() {
                registrationState.postValue(RegistrationState.ERROR_PLAYER_EXISTS);
            }
        });
    }

    public void checkForExistingPlayer() {
        mainRepository.getCachedPlayer(cachedPlayer::postValue);
    }

    public void login() {
        mainRepository.authorizeCurrent(new MainRepository.AuthorizerCallback() {
            @Override
            public void onSuccess() {
                registrationState.postValue(RegistrationState.SUCCESS);
            }

            @Override
            public void onError() {
                registrationState.postValue(RegistrationState.ERROR_UNAUTHORIZED);
            }
        });
    }

    public MutableLiveData<Player> getCachedPlayer() {
        return cachedPlayer;
    }

    public MutableLiveData<RegistrationState> getRegistrationState() {
        return registrationState;
    }
}
