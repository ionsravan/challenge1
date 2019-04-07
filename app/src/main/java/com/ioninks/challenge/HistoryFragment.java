package com.ioninks.challenge;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    RecyclerView history_rv;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    public List<played_matches_model> played_matches;
    public history_adapter history_adapter;
    TextView no_matches;
    public com.victor.loading.rotate.RotateLoading rotateLoading;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        history_rv = v.findViewById(R.id.history_rv);
        no_matches = v.findViewById(R.id.no_matches);
        no_matches.setVisibility(View.INVISIBLE);

        rotateLoading = v.findViewById(R.id.progress_bar);
        rotateLoading.start();

        played_matches = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();
        mFirestore = FirebaseFirestore.getInstance();
        // Inflate the layout for this fragment
        history_adapter = new history_adapter(played_matches);
        history_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        history_rv.setAdapter(history_adapter);

        mFirestore.collection("users").document(user_id).collection("played_matches").document().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {

                    } else{
                        rotateLoading.stop();
                        no_matches.setVisibility(View.VISIBLE);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "No matches played yet", Toast.LENGTH_SHORT).show();
            }
        });

        mFirestore.collection("users").document(user_id).collection("played_matches").orderBy("time", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        played_matches_model played_matches_model = doc.getDocument().toObject(played_matches_model.class);
                        played_matches.add(played_matches_model);
                        history_adapter.notifyDataSetChanged();
                        rotateLoading.stop();

                    } else {
                        no_matches.setVisibility(View.VISIBLE);
                    }


                }
            }
        });




        return v;
    }




}
