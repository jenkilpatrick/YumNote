package com.yumnote.yumnote.model;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by jen on 2/19/16.
 */
public abstract class DefaultValueEventListener implements ValueEventListener {
    private String TAG = "DefaultValueEventListener";

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        Log.e(TAG, "The read failed: " + firebaseError.getMessage());
    }
}
