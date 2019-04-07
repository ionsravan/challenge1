package com.ioninks.challenge;

public class played_matches_model extends matchplay_id {
    String challenge_coins;
    String match_id;
    String team;
    String user_id;

    public played_matches_model(){

    }

    public played_matches_model(String challenge_coins, String match_id, String team, String user_id) {
        this.challenge_coins = challenge_coins;
        this.match_id = match_id;
        this.team = team;
        this.user_id = user_id;
    }

    public String getChallenge_coins() {
        return challenge_coins;
    }

    public void setChallenge_coins(String challenge_coins) {
        this.challenge_coins = challenge_coins;
    }

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
