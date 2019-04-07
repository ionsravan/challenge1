package com.ioninks.challenge;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

import cdflynn.android.library.checkview.CheckView;

public class buyed_coins_success extends AppCompatActivity {

    CheckView success_check;
    Button mcoins_buy,mcoins_sell;
    FirebaseFirestore mfirestore;
    TextView mcoins_coinspage;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyed_coins_success);

        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();


        mcoins_buy = findViewById(R.id.mCoins_buy);
        mcoins_coinspage = findViewById(R.id.mcoins_coins_page);
        mcoins_sell = findViewById(R.id.mCoins_sell);
        success_check =findViewById(R.id.check);
        success_check.check();



        mfirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                  String available_coins = task.getResult().getString("mCoins");
                    Intent intent = getIntent();
                    String mcoins = intent.getStringExtra("amount");
                    String response = intent.getStringExtra("response");

                    final Map<String,String> data = new HashMap<>();
                    data.put("orderId",response);
                    data.put("amount",mcoins);
                    int MCOINS = Integer.parseInt(mcoins);
                    int AVAILABLE_COINS = Integer.parseInt(available_coins);

                    int final_coins =(AVAILABLE_COINS)+(MCOINS);

                    String FINAL_COINS = String.valueOf(final_coins);
                    mfirestore.collection("users").document(user_id).update("mCoins",FINAL_COINS).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mfirestore.collection("users").document(user_id).collection("buyed_coins").document().set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(buyed_coins_success.this, "buyed_coins updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                    mcoins_coinspage.setText(FINAL_COINS);

                }
            }
        });

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
