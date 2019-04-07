package com.ioninks.challenge;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class matchplay_id {

    @Exclude
    public String playid;

    public <T extends matchplay_id> T withId(@NonNull final String id) {
        this.playid = id;
        return (T) this;
    }
}
