package com.ioninks.challenge;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class matchdoc_id {

    @Exclude
    public String matdocid;

    public <T extends matchdoc_id> T withId(@NonNull final String id) {
        this.matdocid = id;
        return (T) this;
    }
}
