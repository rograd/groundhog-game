package com.example.groundhog;

import com.example.groundhog.Player;
import com.example.groundhog.response.RegistrationCallback;
import com.example.groundhog.response.PlayerResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import java.util.Objects;

public class PlayerRepository {
    private final CollectionReference playerCollection;
    private final FirebaseAuth auth;

    public PlayerRepository() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        playerCollection = database.collection("players");
        auth = FirebaseAuth.getInstance();
    }

    public void register(Player player, RegistrationCallback registrationCallback) {
        getCachedPlayer(cachedPlayer -> {
            if (cachedPlayer.exists()) {
                registrationCallback.onSuccess(cachedPlayer);
                return;
            }

            auth.signInAnonymously()
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = authResult.getUser();
                        String id = Objects.requireNonNull(user).getUid();
                        registerNewPlayer(id, player, registrationCallback);
                    });
        });
    }

    private void checkPlayerExists() {

    }

    private void registerNewPlayer(String id, Player player, RegistrationCallback registrationCallback) {
            playerCollection
                .document(id)
                .set(player)
                .addOnSuccessListener(unused -> registrationCallback.onSuccess(player));
    }

    public void getCachedPlayer(PlayerResponse playerResponse) {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            playerResponse.onSuccess(Player.nonExistent);
            return;
        }

        playerCollection
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Player player = documentSnapshot.toObject(Player.class);
                    playerResponse.onSuccess(player);
                });
    }
}