package com.ioninks.challenge;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import java.util.StringJoiner;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    // Required empty public constructor

    CircleImageView user_pro_pic;
    TextView first_name_tv,last_name_tv,user_mcoins;
    Button user_sell,user_buy;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    public List<played_matches_model> played_matches_models;
    public played_matches_adapter played_matches_adapter;
    RecyclerView user_challenges_rv;
    public com.victor.loading.rotate.RotateLoading rotateLoading;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        played_matches_models = new ArrayList<>();

         user_pro_pic = v.findViewById(R.id.user_pro_pic);
         last_name_tv = v.findViewById(R.id.last_name);
         first_name_tv = v.findViewById(R.id.first_name);
         user_sell = v.findViewById(R.id.user_sell);
         user_buy = v.findViewById(R.id.user_buy);
         user_mcoins = v.findViewById(R.id.user_mcoins);


        rotateLoading = v.findViewById(R.id.progress_bar);
        rotateLoading.start();

         user_buy.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent buyActivity = new Intent(getActivity(),buyActivity.class);
                 startActivity(buyActivity);
             }
         });


         mAuth = FirebaseAuth.getInstance();
         final String user_id = mAuth.getCurrentUser().getUid();
         mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if (task.getResult().exists()){
                        String first_name = task.getResult().getString("first_name");
                        String last_name = task.getResult().getString("last_name");
                        String image_url = task.getResult().getString("image");
                        String mcoins = task.getResult().getString("mCoins");
                        first_name_tv.setText(first_name);
                        last_name_tv.setText(last_name);
                        user_mcoins.setText(mcoins);
                        Glide.with(getActivity()).load(image_url).into(user_pro_pic);

                    }else {
                        Intent setup = new Intent(getActivity(),SetupActivity.class);
                        startActivity(setup);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "failed error:"+e, Toast.LENGTH_SHORT).show();

            }
        });
        user_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sellActivity = new Intent(getActivity(), com.ioninks.challenge.sellActivity.class);
                startActivity(sellActivity);

            }
        });

        user_challenges_rv = v.findViewById(R.id.user_challenges_rv);
        played_matches_adapter = new played_matches_adapter(played_matches_models);
        user_challenges_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        user_challenges_rv.setAdapter(played_matches_adapter);



        mFirestore.collection("users").document(user_id).collection("played_matches").document().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {

                    } else{
                        rotateLoading.stop();
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

                        String play_id = doc.getDocument().getId();
                        played_matches_model played_matches_model = doc.getDocument().toObject(played_matches_model.class).withId(play_id);

                        played_matches_models.add(played_matches_model);
                        played_matches_adapter.notifyDataSetChanged();
                        rotateLoading.stop();
                    }

                }
            }
        });





        return v;

    }
}
