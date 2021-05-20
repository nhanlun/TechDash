package com.example.techdash.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techdash.models.User;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private static final String TAG = UserRepository.class.getSimpleName();
    private static UserRepository INSTANCE = null;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private MutableLiveData<User> user = new MutableLiveData<>(null);

    private MutableLiveData<ArrayList<User>> friends = new MutableLiveData<>();

    private UserRepository() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Log.d(TAG, "Logged in");
            fetchUser(currentUser);
        }
    }

    public static UserRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserRepository();
        }
        return INSTANCE;
    }

    public LiveData<User> getUser() {
        return user;
    }

    void fetchUser(FirebaseUser firebaseUser) {
        String uid = firebaseUser.getUid();
        db.collection("users").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> map = task.getResult().getData();
                            if (map != null) {
                                Log.d(TAG, "Fetched user");
                                user.postValue(new User(map));
                            } else {
                                Log.d(TAG, "User not found, creating new user");
                                createUser(firebaseUser);
                            }
                        } else {
                        }
                    }
                });
    }

    private void updateName(FirebaseUser firebaseUser, String name) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User name updated.");
                            fetchUser(firebaseUser);
                        }
                    }
                });
    }

    void createUser(FirebaseUser firebaseUser) {
        String uid = firebaseUser.getUid();
        String name = firebaseUser.getDisplayName();
        User newUser = new User(uid, name, 0);
        HashMap<String, Object> map = newUser.toHashMap();
        db.collection("users").document(uid)
                .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Created a new user");
                    user.postValue(newUser);
                }
            }
        });
    }

    public void loginWithFacebook(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Login successfully");
                    FirebaseUser currentUser = auth.getCurrentUser();
                    fetchUser(currentUser);
                } else {
                    Log.d(TAG, "Login failed");
                }
            }
        });
    }

    public void loginWithAccount(String name) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (name != null) updateName(currentUser, name);
        else fetchUser(currentUser);

    }

    public void logout() {
        auth.signOut();
        LoginManager.getInstance().logOut();
        user.setValue(null);
    }

    public LiveData<ArrayList<User>> searchUserToAddFriend(String key) {
        MutableLiveData<ArrayList<User>> friend = new MutableLiveData<>();
        ArrayList<User> friendArrayList = new ArrayList<>();

        DocumentReference docRef = db.collection("users").document(key);//find by uid
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "document snapshot" + document.getData());
                        friendArrayList.add(new User(document.getData()));
                        friend.postValue(friendArrayList);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "Task failed with " + task.getException());
                }
                if (friendArrayList.size() == 0) {// find by name
                    db.collection("users").whereEqualTo("name", key).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    //Map<String, Object> map = documentSnapshot.getData();
                                    User tmp = new User(documentSnapshot.getData());
                                    friendArrayList.add(tmp);
                                    Log.d("Hihi", documentSnapshot.getData().toString());
                                }
                                Log.d("UserRepo 152", friendArrayList.toString() + task.getResult().size());
                                friend.postValue(friendArrayList);
                            } else {
                                Log.d(TAG, "Error getting list of document", task.getException());
                            }
                        }
                    });
                }
            }
        });
        Log.d("Friend lits", friendArrayList.toString());
        return friend;
    }


    public void addFriend(User friend) {
        db.collection("users").document(user.getValue().getUid()).collection("friends")
                .document(friend.getUid()).set(friend.toHashMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Add friend successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Fail to add friend");
            }
        });
    }

    public LiveData<ArrayList<User>> getFriendList() {
        return friends;
    }

    public void fetchFriendList() {
        ArrayList<User> friendList = new ArrayList<>();
        db.collection("users").document(user.getValue().getUid()).collection("friends").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                friendList.add(new User(document.getData()));
                            }
                            friends.postValue(friendList);
                        } else {
                            Log.d(TAG, "U have no friend");
                        }
                    }
                });
    }
}
