package com.ioninks.challenge;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;

import de.hdodenhof.circleimageview.CircleImageView;
import instamojo.library.InstapayListener;
import instamojo.library.InstamojoPay;
import instamojo.library.Config;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class buyActivity extends AppCompatActivity {

    Button buy_mcoins;
    EditText phone_buy,amount_buy;
    public String phn_num,amount,email,first_name,img_url,mcoins,user_id;
    CircleImageView pro_img;
    public TextView username,my_mcoins;

    FirebaseAuth Auth;
    FirebaseFirestore mfirestore;


    
    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = this;
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
       pay.put("send_sms", true);
      pay.put("send_email", true);
 } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;


    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {

                Intent buyed_coins_success = new Intent(getApplicationContext(),buyed_coins_success.class);
                buyed_coins_success.putExtra("amount",amount);
                buyed_coins_success.putExtra("response",response);
                startActivity(buyed_coins_success);


            }

            @Override
            public void onFailure(int code, String reason) {
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                        .show();
            }
        };
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        phone_buy = findViewById(R.id.phone_buy);
        amount_buy = findViewById(R.id.amount_buy);
        pro_img = findViewById(R.id.sell_pro_img);
        username = findViewById(R.id.sell_username);
        my_mcoins = findViewById(R.id.mcoins);
        Auth = FirebaseAuth.getInstance();
        user_id = Auth.getCurrentUser().getUid();
        mfirestore = FirebaseFirestore.getInstance();

        mfirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if (task.getResult().exists()){
                        email = task.getResult().getString("email");
                        first_name = task.getResult().getString("first_name");
                        img_url = task.getResult().getString("image");
                        mcoins = task.getResult().getString("mCoins");
                        username.setText(first_name);
                        my_mcoins.setText(mcoins);
                        Glide.with(getApplicationContext()).load(img_url).into(pro_img);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(buyActivity.this, "failed:"+e, Toast.LENGTH_SHORT).show();
            }
        });



        buy_mcoins = findViewById(R.id.buy_mcoins);
        // Call the function callInstamojo to start payment here
        buy_mcoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phn_num = phone_buy.getText().toString();
                amount = amount_buy.getText().toString();


                callInstamojoPay(email,phn_num,amount,"buying coins",first_name);
            }
        });

    }
}
