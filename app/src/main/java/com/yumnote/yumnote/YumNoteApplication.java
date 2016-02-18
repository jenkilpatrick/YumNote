package com.yumnote.yumnote;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by jen on 2/17/16.
 */
public class YumNoteApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}
