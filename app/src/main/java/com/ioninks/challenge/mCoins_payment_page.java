package com.ioninks.challenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cdflynn.android.library.checkview.CheckView;
import de.hdodenhof.circleimageview.CircleImageView;

public class mCoins_payment_page extends AppCompatActivity {

    CheckView success_check;
    CircleImageView team_img_mcoins;
    TextView team_name_mcoins,mcoins_coins_page,payment_text;
    Button mcoins_buy,mcoins_sell;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_coins_payment_page);

        payment_text = findViewById(R.id.payment_text);
        team_img_mcoins = findViewById(R.id.team_img_mcoins);
        team_name_mcoins = findViewById(R.id.team_name_mcoins);
        mcoins_buy = findViewById(R.id.mCoins_buy);
        mcoins_sell = findViewById(R.id.mCoins_sell);
        mcoins_coins_page = findViewById(R.id.mcoins_coins_page);
        success_check = findViewById(R.id.check);
        success_check.check();

        Intent intent = getIntent();
        String team_img = intent.getStringExtra("team_image");
        String team_name =intent.getStringExtra("team_name");
        String mcoins = intent.getStringExtra("mcoins");




        team_name_mcoins.setText(team_name);
        mcoins_coins_page.setText(mcoins);

        Glide.with(getApplicationContext()).load(team_img).into(team_img_mcoins);

        mcoins_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sellActivity = new Intent(getApplicationContext(),sellActivity.class);
                startActivity(sellActivity);
            }
        });




    }
}
