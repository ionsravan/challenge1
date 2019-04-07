package com.ioninks.challenge;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class sellActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public RadioGroup radioGroup;
    public RadioButton payment_mode_button;
    public Spinner spinner;
    private static final String[] amount_list = {"400","700","1000","1500","2100","2700","3300","4200","5500"};
    String item,user_id,Mcoins;
    public Button sell_btn;
    CircleImageView sell_pro_image;
    TextView sell_username,sell_mcoins,sell_phn_number;
    FirebaseAuth mAuth;
    FirebaseFirestore mfirestore;
    String first_name,img_url,mcoins,payment_method;
    public com.victor.loading.rotate.RotateLoading rotateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        sell_btn = findViewById(R.id.sell_btn);
        sell_pro_image = findViewById(R.id.sell_pro_img);
        sell_mcoins = findViewById(R.id.mcoins);
        sell_username = findViewById(R.id.sell_username);
        sell_phn_number = findViewById(R.id.sell_mobile_number);
        radioGroup = findViewById(R.id.radioGroup);
        rotateLoading = findViewById(R.id.progress_bar);



        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mfirestore = FirebaseFirestore.getInstance();

        spinner = findViewById(R.id.sell_spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(sellActivity.this,
                android.R.layout.simple_spinner_item,amount_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mfirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                Mcoins = task.getResult().getString("mCoins");


            }
        });

        sell_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rotateLoading.start();

                int selectedId = radioGroup.getCheckedRadioButtonId();
                payment_mode_button = findViewById(selectedId);

                payment_method = payment_mode_button.getText().toString();

                String phone_number = sell_phn_number.getText().toString();
                if (!phone_number.isEmpty()) {
                    int ITEM = Integer.parseInt(item);

                    Map<String, String> data = new HashMap<>();
                    data.put("payment_mode", payment_method);
                    data.put("amount", item);
                    data.put("phone_number", phone_number);
                    int MCOINS = Integer.parseInt(Mcoins);
                    int finalCoins = (MCOINS) - (ITEM);
                    final String FINALCOINS = String.valueOf(finalCoins);

                    if (ITEM <= MCOINS) {

                        mfirestore.collection("sold").document(user_id).collection("sold").document().set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {


                                    mfirestore.collection("users").document(user_id).update("mCoins", FINALCOINS).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(sellActivity.this, "updated in data base", Toast.LENGTH_SHORT).show();
                                            Intent success = new Intent(getApplicationContext(), sold_mCoins.class);
                                            success.putExtra("mcoins", item);
                                            rotateLoading.stop();
                                            startActivity(success);
                                        }
                                    });


                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(sellActivity.this, "failed" + e, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(sellActivity.this, "please enter correct portion of mCoins", Toast.LENGTH_LONG).show();
                        rotateLoading.stop();
                    }
                }
            }
        });

        mfirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    first_name = task.getResult().getString("first_name");
                    img_url = task.getResult().getString("image");
                    mcoins = task.getResult().getString("mCoins");

                    sell_username.setText(first_name);
                    sell_mcoins.setText(mcoins);
                    Glide.with(getApplicationContext()).load(img_url).into(sell_pro_image);


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(sellActivity.this, "failed"+e, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
