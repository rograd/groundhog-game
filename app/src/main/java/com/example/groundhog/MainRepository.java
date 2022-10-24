package com.example.groundhog;

import com.example.groundhog.model.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import java.util.Objects;

public class MainRepository {
    private final CollectionReference playerCollection;
    private final FirebaseAuth auth;

    public MainRepository() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        playerCollection = database.collection("players");
        auth = FirebaseAuth.getInstance();
    }

    interface AuthorizerCallback {
        void onSuccess();
        void onError();
    }

    interface PlayerResponse {
        void onSuccess(Player player);
    }

    public void register(Player player, AuthorizerCallback authorizerCallback) {
        // User is signed in.
        if (auth.getCurrentUser() != null)
            return;

        auth.signInAnonymously()
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    String id = Objects.requireNonNull(user).getUid();
                    register(id, player, authorizerCallback);
                });
    }

    private void register(String id, Player player, AuthorizerCallback authorizerCallback) {
        playerCollection
                .document(id)
                .set(player)
                .addOnSuccessListener(unused -> authorizerCallback.onSuccess());
    }

    public void authorizeCurrent(AuthorizerCallback authorizerCallback) {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            authorizerCallback.onSuccess();
            // check in database whether user exists
            return;
        }

        authorizerCallback.onError();
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