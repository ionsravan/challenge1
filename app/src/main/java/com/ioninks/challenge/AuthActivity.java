package com.ioninks.challenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity {

    EditText phone_number;
    Button send_otp;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mAuth = FirebaseAuth.getInstance();

        phone_number = findViewById(R.id.phone_number);
        send_otp = findViewById(R.id.send_otp_btn);


        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = phone_number.getText().toString().trim();
                if(mobile.isEmpty() || mobile.length() < 10){
                    phone_number.setError("Enter a valid mobile");
                    phone_number.requestFocus();
                    return;
                }

                Intent otp_intent = new Intent(AuthActivity.this,OtpActivity.class);
                otp_intent.putExtra("mobile",mobile);
                startActivity(otp_intent);
            }
        });

    }

    @Override
    protected void onStart() {
        if (mAuth.getCurrentUser() != null){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
        super.onStart();
    }
}
