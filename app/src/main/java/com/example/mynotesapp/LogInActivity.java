package com.example.mynotesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "LogInActivity";
    private EditText edtTxtName, edtTxtPassword;


    private Button btnLogIn,btnRegisterRedirect;

    private static String file = "Users.ser";

    private static ArrayList<User> allUsers = new ArrayList<>();
    private ConstraintLayout parent;

    // I'm Make this to band the user to go back to the NotesActivity without authentication
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LogInActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        initViews();


        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                initLogin();
            }
        });

        btnRegisterRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initLogin(){
        Log.d(TAG, "initLogin: Started");

        if(validateData()){
            showSnackBar();
            Intent intent = new Intent(LogInActivity.this,NotesActivity.class);
            intent.putExtra("User", edtTxtName.getText().toString());
            startActivity(intent);
        }
    }

    private void showSnackBar(){
        Log.d(TAG,"showSnackBar: Started");



        Snackbar.make(parent,"User LogedIn",Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }
    private boolean validateData(){
        Log.d(TAG, "validateData: Started");

        // Get the Vibrator service
        Vibrator vibrator = getSystemService(Vibrator.class);


        String Name = edtTxtName.getText().toString();
        String Password = edtTxtPassword.getText().toString();
        if(Name.equals("")){
            Snackbar.make(parent,"Enter your Username",Snackbar.LENGTH_INDEFINITE)
                    .setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
            // Check if the device supports vibration
            if (vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Create a vibration effect for the specified duration
                    VibrationEffect effect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE);

                    // Vibrate the device
                    vibrator.vibrate(effect);
                } else {
                    // For older versions, vibrate without specifying a pattern
                    vibrator.vibrate(500);
                }
            }
            return false;
        }
        if(Password.equals("")){
            Snackbar.make(parent,"Enter your password",Snackbar.LENGTH_INDEFINITE)
                    .setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
            // Check if the device supports vibration
            if (vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Create a vibration effect for the specified duration
                    VibrationEffect effect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE);

                    // Vibrate the device
                    vibrator.vibrate(effect);
                } else {
                    // For older versions, vibrate without specifying a pattern
                    vibrator.vibrate(500);
                }
            }
            return false;
        }
        allUsers = loadUsersFromFile(file,LogInActivity.this);
        /*for (User i : allUsers){
            System.out.println(i.toString());
        }*/
        boolean exists = false;
        for(User user : allUsers){
            if(user.Name.equals(Name)){
                exists = true;
                if(user.Password.equals(Password)){
                    return true;
                }else{
                    Snackbar.make(parent,"Wrong Password",Snackbar.LENGTH_INDEFINITE)
                            .setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                    return false;
                }
            }
        }
        if(!exists){
            Snackbar.make(parent,"user does not exist",Snackbar.LENGTH_INDEFINITE)
                    .setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
            return  false;
        }

        return true;

    }
    private void initViews(){
        Log.d(TAG,"initViews: Started");
        edtTxtName = findViewById(R.id.Name);
        edtTxtPassword = findViewById(R.id.Password);



        btnLogIn = findViewById(R.id.login);
        btnRegisterRedirect = findViewById(R.id.registerRedirect);

        parent = findViewById(R.id.parent);
    }
    private static ArrayList<User> loadUsersFromFile(String filename, Context context) {
        try (FileInputStream fis = context.openFileInput(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            return (ArrayList<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}