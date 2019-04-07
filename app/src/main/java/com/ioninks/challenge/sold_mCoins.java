package com.ioninks.challenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import cdflynn.android.library.checkview.CheckView;

public class sold_mCoins extends AppCompatActivity {

    CheckView success_check;
    Button mcoins_buy,mcoins_sell;
    FirebaseFirestore mfirestore;
    TextView mcoins_coinspage;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sold_m_coins);

        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();



        mcoins_buy = findViewById(R.id.mCoins_buy);
        mcoins_coinspage = findViewById(R.id.mcoins_coins_page);
        mcoins_sell = findViewById(R.id.mCoins_sell);
        success_check =findViewById(R.id.check);
        success_check.check();

        Intent intent = getIntent();

        String Mcoins = intent.getStringExtra("mcoins");
        mcoins_coinspage.setText(Mcoins);


        mcoins_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sellActivty = new Intent(getApplicationContext(),sellActivity.class);
                startActivity(sellActivty);
            }
        });
        mcoins_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent buyActivity = new Intent(getApplicationContext(), com.ioninks.challenge.buyActivity.class);
                startActivity(buyActivity);
            }
        });

    }
}
