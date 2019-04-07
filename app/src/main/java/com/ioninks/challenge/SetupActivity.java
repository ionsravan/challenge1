package com.ioninks.challenge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {


    private EditText firstname,lastname,email;
    private Button save_details;
    private CircleImageView pro_pic;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mfirestore;
    FirebaseStorage mstorage;
    StorageReference storageReference;
    private String user_id;
    String coins,PRO_PIC;
    public com.victor.loading.rotate.RotateLoading rotateLoading;

    @ServerTimestamp
    Date createdDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        firstname = findViewById(R.id.first_name_setup);
        lastname = findViewById(R.id.last_name_setup);
        email = findViewById(R.id.email_setup);
        save_details = findViewById(R.id.save_details);
        pro_pic = findViewById(R.id.propic_setup);
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        rotateLoading = findViewById(R.id.progress_bar);
        rotateLoading.start();


        mstorage = FirebaseStorage.getInstance();
        storageReference = mstorage.getReference();

        mfirestore.collection("users").document("image").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    PRO_PIC = task.getResult().getString("image");

                    Glide.with(getApplicationContext()).load(PRO_PIC).into(pro_pic);

                }
            }
        });


        mfirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if (task.getResult().exists()){
                        String first_name_rt = task.getResult().getString("first_name");
                        String last_name_rt = task.getResult().getString("last_name");
                        String email_rt = task.getResult().getString("email");
                        String image_url_rt = task.getResult().getString("image");
                        coins = task.getResult().getString("mCoins");
                        firstname.setText(first_name_rt);
                        lastname.setText(last_name_rt);
                        email.setText(email_rt);
                        Glide.with(getApplicationContext()).load(image_url_rt).into(pro_pic);
                        rotateLoading.stop();
                    }else {
                        coins = "100";
                        rotateLoading.stop();
                    }

                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "firebase error "+ error, Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SetupActivity.this, "error:"+e, Toast.LENGTH_SHORT).show();

            }
        });




        save_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rotateLoading.start();
                final String first_name = firstname.getText().toString();
                final String last_name = lastname.getText().toString();
                final String email_st = email.getText().toString();
                Map<String,Object> user_map = new HashMap<>();
                user_map.put("first_name",first_name);
                user_map.put("last_name",last_name);
                user_map.put("email",email_st);
                user_map.put("image",PRO_PIC);
                user_map.put("mCoins",coins);
                user_map.put("time",createdDate);
                mfirestore.collection("users").document(user_id).set(user_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        rotateLoading.stop();
                                        Toast.makeText(SetupActivity.this, "successfully updated Details", Toast.LENGTH_SHORT).show();
                                        sendto_mainactivity();

                                    }else{
                                        Toast.makeText(SetupActivity.this, "data not updated", Toast.LENGTH_SHORT).show();
                                        rotateLoading.stop();

                                    }
                                }
                            });

            }
        });



    }

    private void sendto_mainactivity() {

        Intent mainactivity = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(mainactivity);
    }
}
