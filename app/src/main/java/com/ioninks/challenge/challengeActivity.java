package com.ioninks.challenge;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class challengeActivity extends AppCompatActivity implements team1_bottomsheet.data,team2_bottomsheet.data{

    TextView team1_name,team2_name,match_no,match_time,team1_name_squad,team1_squad,team2_name_squad,team2_squad,team1_perc,team2_perc;
    CircleImageView team1_img,team2_img;
    Button bt_team1,bt_team2;
    String team1_amount="",team2_amount="";
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);


        team1_name = findViewById(R.id.team1_name_chg);
        team2_name = findViewById(R.id.team2_name_chg);
        team1_img = findViewById(R.id.team1_img_chg);
        team2_img = findViewById(R.id.team2_img_chg);
        match_time = findViewById(R.id.match_time_chg);
        match_no = findViewById(R.id.match_no_chg);
        bt_team1 = findViewById(R.id.team1_btn);
        bt_team2 = findViewById(R.id.team2_btn);
        team1_name_squad = findViewById(R.id.team1_name_squad);
        team1_squad = findViewById(R.id.team1_squad);
        team2_name_squad = findViewById(R.id.team2_name_squad);
        team2_squad = findViewById(R.id.team2_squad);
        team1_perc = findViewById(R.id.team1_percent);
        team2_perc = findViewById(R.id.team2_percent);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        String user_id = mAuth.getCurrentUser().getUid();



        Intent intent = getIntent();
        final String team1_name_st = intent.getStringExtra("team1_name");
        final String team2_name_st = intent.getStringExtra("team2_name");
        String team1_img_chg_st = intent.getStringExtra("team1_img_url");
        String team2_img_chg_st = intent.getStringExtra("team2_img_url");
        String match_no_st = intent.getStringExtra("match_no");
        String match_time_st= intent.getStringExtra("match_time");
        final String doc_id = intent.getStringExtra("doc_id");


        team1_name.setText(team1_name_st);
        team2_name.setText(team2_name_st);
        match_no.setText(match_no_st);
        match_time.setText(match_time_st);
        team1_name_squad.setText(team1_name_st);
        team2_name_squad.setText(team2_name_st);

        Glide.with(getApplicationContext()).load(team1_img_chg_st).into(team1_img);
        Glide.with(getApplicationContext()).load(team2_img_chg_st).into(team2_img);


        mFirestore.collection("users").document("teams").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        String team1_players = task.getResult().getString(team1_name_st);
                        String team2_players = task.getResult().getString(team2_name_st);

                        team1_squad.setText(team1_players);
                        team2_squad.setText(team2_players);

                    }

                }


            }
        });

        mFirestore.collection("matches").document(doc_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if (task.getResult().exists()) {
                        String team1_percent = task.getResult().getString("team1_percent");
                        String team2_percent = task.getResult().getString("team2_percent");

                        team1_perc.setText(team1_percent);
                        team2_perc.setText(team2_percent);


                    }
                }
            }
        });

        bt_team1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team1_bottomsheet team1_bottomsheet = new team1_bottomsheet();
                team1_bottomsheet.show(getSupportFragmentManager(),"team1_bottomsheet");
            }
        });

        bt_team2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team2_bottomsheet team2_bottomsheet = new team2_bottomsheet();
                team2_bottomsheet.show(getSupportFragmentManager(),"team2_bottomsheet");
            }
        });



    }

    @Override
    public void team1_challege_amount(String amount) {
        this.team1_amount = amount;

    }

    @Override
    public void team2_challege_amount(String amount) {
        this.team2_amount = amount;
    }
}
