package com.ioninks.challenge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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

import java.util.List;

public class history_adapter extends RecyclerView.Adapter<history_adapter.ViewHolder>{

    public FirebaseFirestore mFirestore;
    public FirebaseAuth mAuth;
    public List<played_matches_model> played_matches;
    public Context context;


    public history_adapter(List<played_matches_model> played_matches) {
        this.played_matches = played_matches;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        final String challenge_coins = played_matches.get(position).getChallenge_coins();
        final int coins = Integer.parseInt(challenge_coins);

        final  String match_id = played_matches.get(position).getMatch_id();


        final String team = played_matches.get(position).getTeam();
        mFirestore.collection("matches").document(match_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if(task.getResult().exists()){
                        String challenge_team =task.getResult().getString(team+"_name");
                        String challenge_team_url = task.getResult().getString(team+"_img_url");
                        String status = task.getResult().getString("status");
                        String won = task.getResult().getString("won");

                        holder.setteam_name(challenge_team);
                        holder.setteam_img(challenge_team_url);

                        if (status.equals("0")){
                            holder.setplayedstatus("NOT YET STARTED");
                            holder.setHistory_coins(challenge_coins);
                        } else if (status.equals("1")){
                            holder.setplayedstatus("PENDING..");
                            holder.setHistory_coins(challenge_coins);
                        } else if (status.equals("2")){
                            if (team.equals(won)){
                                holder.setplayedstatus("WON");
                                holder.history_constraint.setBackgroundColor(Color.parseColor("#ddffcc"));
                                holder.setHistory_coins(challenge_coins);
                            } else {
                                holder.setplayedstatus("LOST");
                                holder.history_constraint.setBackgroundColor(Color.parseColor("#ffc2b3"));
                                holder.setHistory_coins(challenge_coins);

                            }
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
        public TextView team_name,played_status,history_coins;
        public ImageView team_img;
        ConstraintLayout history_constraint;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            history_constraint = itemView.findViewById(R.id.history_constraint);

        }
        public void setteam_name(String team_name_play){
            team_name = view.findViewById(R.id.team_name_history);
            team_name.setText(team_name_play);
        }
        public void setteam_img(String team_img_play){
            team_img = view.findViewById(R.id.team_img_history);
            Glide.with(context).load(team_img_play).into(team_img);
        }

        public void setplayedstatus(String playedStatus){
            played_status = view.findViewById(R.id.status_history);
            played_status.setText(playedStatus);
        }
        public void setHistory_coins(String historyCoins){
            history_coins = view.findViewById(R.id.history_mcoins);
            history_coins.setText(historyCoins);
        }
    }


}

