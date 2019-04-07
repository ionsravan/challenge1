package com.ioninks.challenge;



import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private HistoryFragment historyFragment;
    private ProfileFragment profileFragment;

    int versionCode = BuildConfig.VERSION_CODE;

    FirebaseAuth mAuth;
    FirebaseFirestore mfirestore;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String VERSION = String.valueOf(versionCode);

        // Call the function callInstamojo to start payment here
        mAuth = FirebaseAuth.getInstance();
        mfirestore = FirebaseFirestore.getInstance();
        BottomNavigationView navigation = findViewById(R.id.navigation_bar);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        //fragments
        homeFragment = new HomeFragment();
        historyFragment = new HistoryFragment();
        profileFragment = new ProfileFragment();

        replacefragment(homeFragment);



        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()){

                    case R.id.navigation_home:
                        replacefragment(homeFragment);
                        return true;
                    case R.id.navigation_history:
                       replacefragment(historyFragment);
                        return true;
                    case R.id.navigation_profile:
                        replacefragment(profileFragment);
                        return true;
                }
                return false;
            }
        });

        mfirestore.collection("update").document("new_update").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String status = task.getResult().getString("status");
                    final String url = task.getResult().getString("url");
                    final String message = task.getResult().getString("message");
                    final String version = task.getResult().getString("version");


                    if (!VERSION.equals(version)){

                        if (status.equals("1")) {


                            alertDialogBuilder.setMessage(message);
                            alertDialogBuilder.setPositiveButton("yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {

                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(url));
                                            startActivity(i);

                                        }
                                    });

                            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MainActivity.this, "Update as soon as possible to avail free coins", Toast.LENGTH_LONG).show();
                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }

                }
                }
            }
        });


    }

    public void replacefragment(android.support.v4.app.Fragment fragmet){
        if (fragmet!= null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container,fragmet)
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.settings:

                Intent contact = new Intent(getApplicationContext(),Contact.class);
                startActivity(contact);

                return true;
            case R.id.logout:
                mAuth.signOut();
                Intent i = new Intent(getApplicationContext(),AuthActivity.class);
                startActivity(i);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        if (mAuth.getCurrentUser() == null){
            Intent i = new Intent(getApplicationContext(),AuthActivity.class);
            startActivity(i);
        }
        super.onStart();
    }
}
