package com.ioninks.challenge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.victor.loading.newton.CradleBall;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class team2_bottomsheet extends BottomSheetDialogFragment {

    public int mCoins;
    public String MCOINS;
    private data mdata;
    EditText challenge_amount;
    FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;
    TextView Mcoins;
    Button buy_mcoins;
    ProgressBar progressBar;

    @ServerTimestamp
    Date createdDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.challenge_bottom_sheet,container,false);

        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        Mcoins = v.findViewById(R.id.mcoins_bottomsheet);
        buy_mcoins = v.findViewById(R.id.buy_btm);

        mAuth = FirebaseAuth.getInstance();
        mfirestore = FirebaseFirestore.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();

        Intent intent = getActivity().getIntent();
        final String team2_name_st = intent.getStringExtra("team2_name");
        final String team2_img_chg_st = intent.getStringExtra("team2_img_url");
        final String doc_id = intent.getStringExtra("doc_id");
        TextView bottom_team_name = v.findViewById(R.id.bottom_team_name);
        CircleImageView bottom_team_img = v.findViewById(R.id.bottom_team_img);
       challenge_amount = v.findViewById(R.id.challenge_amount);
        bottom_team_name.setText(team2_name_st);
        Glide.with(getActivity()).load(team2_img_chg_st).into(bottom_team_img);

        Button challenge_button = v.findViewById(R.id.bottom_challenge);


        //onload of the activity
        mfirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        MCOINS = task.getResult().getString("mCoins");
                        mCoins = Integer.parseInt(MCOINS);
                        Mcoins.setText(MCOINS);
                    }
                }
            }
        });

        //buy coins page

        buy_mcoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Buy = new Intent(getActivity(),buyActivity.class);
                startActivity(Buy);
            }
        });

        //on challenge clicked
        challenge_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String chng_amnt = challenge_amount.getText().toString();
                int challenge_amount = Integer.parseInt(chng_amnt);
                mdata.team2_challege_amount(chng_amnt);

                progressBar.setVisibility(View.VISIBLE);



                if(mCoins>=challenge_amount && challenge_amount >50) {

                        int remaining_amount = mCoins - challenge_amount;
                        String remaining_amount_string = String.valueOf(remaining_amount);
                        Map<String, Object> data_doc = new HashMap<>();
                        data_doc.put("challenge_coins", chng_amnt);
                        data_doc.put("user_id", user_id);
                        data_doc.put("match_id", doc_id);
                        data_doc.put("team", "team2");
                        data_doc.put("time", createdDate);
                        data_doc.put("final_amount", "0");

                        mfirestore.collection("matches").document(doc_id).collection("team2").document().set(data_doc).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "Challenge successfull", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "failed" + e, Toast.LENGTH_SHORT).show();
                            }
                        });
                        mfirestore.collection("users").document(user_id).collection("played_matches").document().set(data_doc).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "failed" + e, Toast.LENGTH_SHORT).show();
                            }
                        });
                        mfirestore.collection("users").document(user_id).update("mCoins", remaining_amount_string).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Intent mcoins_payed = new Intent(getActivity(), mCoins_payment_page.class);
                                mcoins_payed.putExtra("mcoins", chng_amnt);
                                mcoins_payed.putExtra("team_name", team2_name_st);
                                mcoins_payed.putExtra("team_image", team2_img_chg_st);
                                mcoins_payed.putExtra("doc_id", doc_id);
                                progressBar.setVisibility(View.GONE);
                                startActivity(mcoins_payed);

                            }
                        });


                } else {
                    Toast.makeText(getActivity(), "enter correct portion of coins", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "check minimum mCoins should be 50", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }


            }
        });
        return v;


    }
    public  interface data{
        void team2_challege_amount(String amount);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

            mdata = (team2_bottomsheet.data)context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement bottom sheet data");
        }

    }
}
