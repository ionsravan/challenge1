package com.ioninks.challenge;

public class matchlist_model  extends matchdoc_id{

    String team1_img_url;
    String team2_img_url;
    String team1_name;
    String team2_name;
    String match_no;
    String match_time;
    String match_stat;

    public matchlist_model(String team1_img_url, String team2_img_url, String team1_name, String team2_name, String match_no, String match_time, String match_stat) {
        this.team1_img_url = team1_img_url;
        this.team2_img_url = team2_img_url;
        this.team1_name = team1_name;
        this.team2_name = team2_name;
        this.match_no = match_no;
        this.match_time = match_time;
        this.match_stat = match_stat;
    }

    public matchlist_model(){}



    public String getTeam1_img_url() {
        return team1_img_url;
    }

    public void setTeam1_img_url(String team1_img_url) {
        this.team1_img_url = team1_img_url;
    }

    public String getTeam2_img_url() {
        return team2_img_url;
    }

    public void setTeam2_img_url(String team2_img_url) {
        this.team2_img_url = team2_img_url;
    }

    public String getTeam1_name() {
        return team1_name;
    }

    public void setTeam1_name(String team1_name) {
        this.team1_name = team1_name;
    }

    public String getTeam2_name() {
        return team2_name;
    }

    public void setTeam2_name(String team2_name) {
        this.team2_name = team2_name;
    }

    public String getMatch_no() {
        return match_no;
    }

    public void setMatch_no(String match_no) {
        this.match_no = match_no;
    }

    public String getMatch_time() {
        return match_time;
    }

    public void setMatch_time(String match_time) {
        this.match_time = match_time;
    }

    public String getMatch_stat() {
        return match_stat;
    }

    public void setMatch_stat(String match_stat) {
        this.match_stat = match_stat;
    }



}
