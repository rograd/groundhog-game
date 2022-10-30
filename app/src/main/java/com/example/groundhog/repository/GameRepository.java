package com.example.groundhog.repository;

import com.example.groundhog.model.Player;
import com.example.groundhog.utils.ErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import java.util.Objects;

public class GameRepository {
    private final CollectionReference playerCollection;
    private final FirebaseAuth auth;

    public GameRepository() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        playerCollection = database.collection("players");
        auth = FirebaseAuth.getInstance();
    }

    public interface CompletionListener {
        void onSuccess(Player player);
        void onError(ErrorCode errorCode);
    }

    public void registerPlayer(Player player, CompletionListener callback) {
        getCurrentPlayer(new CompletionListener() {
            @Override
            public void onSuccess(Player cachedPlayer) {
                callback.onSuccess(cachedPlayer);
            }

            @Override
            public void onError(ErrorCode errorCode) {
                createPlayer(player, callback);
            }
        });
    }

    private void createPlayer(Player player, CompletionListener callback) {
        auth.signInAnonymously()
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    String id = Objects.requireNonNull(user).getUid();
                    checkUniqueNickname(id, player, callback);
                })
                .addOnFailureListener(exception -> callback.onError(ErrorCode.DATABASE_ERROR));
    }

    private void checkUniqueNickname(String id, Player player, CompletionListener callback) {
        playerCollection
                .whereEqualTo("nickname", player.getNickname())
                .count()
                .get(AggregateSource.SERVER)
                .addOnSuccessListener(query -> {
                    if (query.getCount() > 0) {
                        callback.onError(ErrorCode.NICKNAME_IN_USE);
                    } else {
                        insertPlayer(id, player, callback);
                    }
                });
    }

    private void insertPlayer(String id, Player player, CompletionListener callback) {
        playerCollection
                .document(id)
                .set(player)
                .addOnSuccessListener(unused -> callback.onSuccess(player))
                .addOnFailureListener(exception -> callback.onError(ErrorCode.DATABASE_ERROR));
    }

    private interface AuthorizationListener {
        void onSuccess(FirebaseUser user);
        void onError();
    }

    private void getCurrentUser(AuthorizationListener listener) {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            listener.onError();
            return;
        }

        currentUser
                .reload()
                .addOnSuccessListener(unused -> listener.onSuccess(auth.getCurrentUser()))
                .addOnFailureListener(e -> listener.onError());
    }

    private void getPlayer(FirebaseUser currentUser, CompletionListener listener) {
        playerCollection
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(document -> {
                    Player player;
                    if (document.exists() && (player = document.toObject(Player.class)) != null) {
                        listener.onSuccess(player);
                    } else {
                        currentUser.delete();
                        listener.onError(ErrorCode.COULD_NOT_AUTHORIZE);
                    }
                })
                .addOnFailureListener(e -> listener.onError(ErrorCode.DATABASE_ERROR));
    }

    public void getCurrentPlayer(CompletionListener listener) {
        getCurrentUser(new AuthorizationListener() {
            @Override
            public void onSuccess(FirebaseUser currentUser) {
                getPlayer(currentUser, listener);
            }

            @Override
            public void onError() {
                listener.onError(ErrorCode.COULD_NOT_AUTHORIZE);
            }
        });
    }
}