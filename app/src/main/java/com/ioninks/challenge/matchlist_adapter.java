package com.ioninks.challenge;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class matchlist_adapter extends RecyclerView.Adapter<matchlist_adapter.ViewHolder> {
    public List<matchlist_model> matchlist;
    public Context context;

    public matchlist_adapter(List<matchlist_model> matchlist){
        this.matchlist = matchlist;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.matchlist_item,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String doc_id = matchlist.get(position).matdocid;

        final String match_stat = matchlist.get(position).getMatch_stat();

            final String team1_name = matchlist.get(position).getTeam1_name();
            holder.setteam1_name(team1_name);

            final String team2_name = matchlist.get(position).getTeam2_name();
            holder.setteam2_name(team2_name);

            final String team1_img_url = matchlist.get(position).getTeam1_img_url();
            holder.setteam1_img_url(team1_img_url);

            final String team2_img_url = matchlist.get(position).getTeam2_img_url();
            holder.setteam2_img_url(team2_img_url);


            final String match_no = matchlist.get(position).getMatch_no();
            holder.setmatch_no(match_no);

            final String match_time = matchlist.get(position).getMatch_time();
            holder.setmatch_time(match_time);

            holder.matchlist_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent challenge_activity = new Intent(context, challengeActivity.class);
                    challenge_activity.putExtra("team1_name", team1_name);
                    challenge_activity.putExtra("team2_name", team2_name);
                    challenge_activity.putExtra("team1_img_url", team1_img_url);
                    challenge_activity.putExtra("team2_img_url", team2_img_url);
                    challenge_activity.putExtra("match_no", match_no);
                    challenge_activity.putExtra("match_time", match_time);
                    challenge_activity.putExtra("doc_id", doc_id);
                    context.startActivity(challenge_activity);
                }
            });

    }

    @Override
    public int getItemCount() {
        return matchlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View view;
        public TextView team1_tv,team2_tv,match_no_tv,match_time_tv;
        public ImageView team1_iv,team2_iv;
        public CardView matchlist_card;
        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            matchlist_card = itemView.findViewById(R.id.matchlist_card);
        }
        public void setteam1_name(String team1_name){
            team1_tv = view.findViewById(R.id.team1_tv);
            team1_tv.setText(team1_name);
        }
        public void setteam2_name(String team2_name){
            team2_tv = view.findViewById(R.id.team2_tv);
            team2_tv.setText(team2_name);
        }
        public void setmatch_no(String match_no){
            match_no_tv = view.findViewById(R.id.match_no);
            match_no_tv.setText(match_no);
        }
        public void setmatch_time(String match_time){
            match_time_tv = view.findViewById(R.id.match_time);
            match_time_tv.setText(match_time);
        }
        public void setteam1_img_url(String team1_img){
            team1_iv = view.findViewById(R.id.team1_img);
            Glide.with(context).load(team1_img).into(team1_iv);
        }
        public void setteam2_img_url(String team2_img){
            team2_iv = view.findViewById(R.id.team2_img);
            Glide.with(context).load(team2_img).into(team2_iv);
        }


    }
}
