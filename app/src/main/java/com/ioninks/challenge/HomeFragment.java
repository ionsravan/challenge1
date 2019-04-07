package com.ioninks.challenge;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class HomeFragment extends Fragment {

    public RecyclerView mathlist_recyclerview;
    private List<matchlist_model> matchlist;
    public matchlist_adapter matchlist_adapter;
    public com.victor.loading.rotate.RotateLoading rotateLoading;


    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;

    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        rotateLoading = v.findViewById(R.id.progress_bar);

        rotateLoading.start();

        matchlist = new ArrayList<>();


        mathlist_recyclerview = v.findViewById(R.id.matchlist_recyclerview);
        matchlist_adapter = new matchlist_adapter(matchlist);
        mathlist_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mathlist_recyclerview.setAdapter(matchlist_adapter);
        mathlist_recyclerview.setHasFixedSize(true);



        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("matches").orderBy("time", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String matdoc_id = doc.getDocument().getId();
                        String match_stat = doc.getDocument().getString("match_stat");
                        if(!match_stat.equals("0")) {
                            matchlist_model matchlistModel = doc.getDocument().toObject(matchlist_model.class).withId(matdoc_id);
                            matchlist.add(matchlistModel);
                            matchlist_adapter.notifyDataSetChanged();
                            rotateLoading.stop();
                        }

                    }
                }
            }
        });

        return v;
    }



}
