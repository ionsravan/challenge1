package com.ioninks.challenge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ioninks.challenge.R.color.app_green;

public class played_matches_adapter extends RecyclerView.Adapter<played_matches_adapter.ViewHolder>{

    public FirebaseFirestore mFirestore;
    public FirebaseAuth mAuth;
    public List<played_matches_model> played_matches;
    public Context context;


    public played_matches_adapter(List<played_matches_model> played_matches) {
        this.played_matches = played_matches;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.played_match_item,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();

        final  String play_id = played_matches.get(position).playid;

        final String challenge_coins = played_matches.get(position).getChallenge_coins();
        final int coins = Integer.parseInt(challenge_coins);

        final  String match_id = played_matches.get(position).getMatch_id();


        final String team = played_matches.get(position).getTeam();
        mFirestore.collection("matches").document(match_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if(task.getResult().exists()){
                        String challenge_team =task.getResult().getString(team+"_name");
                        String challenge_team_url = task.getResult().getString(team+"_img_url");
                        String status = task.getResult().getString("status");
                        final String won = task.getResult().getString("won");

                        holder.setteam_name(challenge_team);
                        holder.setteam_img(challenge_team_url);

                        if (status.equals("0")){
                            holder.setplayedstatus("NOT YET STARTED");
                            holder.setchallengeCoins("no update");

                        } else if (status.equals("1")){
                            mFirestore.collection("matches").document(match_id).collection("Ratio").document("ratio").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        String team1_ratio = task.getResult().getString("team1");
                                        int team1 = Integer.parseInt(team1_ratio);
                                        String team2_ratio = task.getResult().getString("team2");
                                        int team2 = Integer.parseInt(team2_ratio);
                                        holder.setplayedstatus("PENDING..");

                                        if (team.equals("team1")){
                                            int final_amount;
                                            final_amount = (coins * team1)/team2;
                                            String famount  = String.valueOf(final_amount);
                                           holder.setchallengeCoins(famount);



                                        }else if(team.equals("team2")){
                                            int final_amount;
                                            final_amount = (coins * team2)/team1;
                                           String famount = String.valueOf(final_amount);
                                            holder.setchallengeCoins(famount);
                                        }
                                    }
                                }
                            });
                        } else if (status.equals("2")){
                            mFirestore.collection("matches").document(match_id).collection("Ratio").document("ratio").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        String team1_ratio = task.getResult().getString("team1");
                                        int team1 = Integer.parseInt(team1_ratio);
                                        String team2_ratio = task.getResult().getString("team2");
                                        int team2 = Integer.parseInt(team2_ratio);

                                        if (team.equals("team1")){
                                            int final_amount;
                                            final_amount = (coins * team1)/team2;
                                            String famount  = String.valueOf(final_amount);
                                            holder.setchallengeCoins(famount);


                                        }else if(team.equals("team2")){
                                            int final_amount;
                                            final_amount = (coins * team2)/team1;
                                            String famount = String.valueOf(final_amount);
                                            holder.setchallengeCoins(famount);
                                        }
                                        if (team.equals(won)){
                                            holder.setplayedstatus("WON");
                                            if (team.equals("team1")){
                                                int final_amount;
                                                final_amount = (coins * team1)/team2;
                                                String famount  = String.valueOf(final_amount);
                                                mFirestore.collection("users").document(user_id).collection("played_matches").document(play_id).update("final_amount",famount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {


                                                    }
                                                });
                                            }else if(team.equals("team2")){
                                                int final_amount;
                                                final_amount = (coins * team2)/team1;
                                                String famount = String.valueOf(final_amount);

                                                mFirestore.collection("users").document(user_id).collection("played_matches").document(play_id).update("final_amount",famount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {


                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context, "failed to upload the final amount", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        } else {
                                            holder.setplayedstatus("LOST");

                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "failed:"+e, Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return played_matches.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View view;
        public TextView team_name,challenge_coins,played_status;
        public ImageView team_img;
        public CardView card_played;


        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            card_played = itemView.findViewById(R.id.card_played);


        }
        public void setteam_name(String team_name_play){
            team_name = view.findViewById(R.id.team_name_played);
            team_name.setText(team_name_play);
        }
        public void setteam_img(String team_img_play){
            team_img = view.findViewById(R.id.team_img_played);
            Glide.with(context).load(team_img_play).into(team_img);
        }
        public void setchallengeCoins(String challengeCoins){
            challenge_coins = view.findViewById(R.id.mcoins_played);
            challenge_coins.setText(challengeCoins);
        }

        public void setplayedstatus(String playedStatus){
            played_status = view.findViewById(R.id.status_played);
            played_status.setText(playedStatus);
        }
    }


}
