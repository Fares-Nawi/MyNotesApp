package com.example.mynotesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivity";
    private EditText edtTxtName, edtTxtPassword, edtTxtConfirmPassword;
    private TextView txtWarnName,txtWarnPassword,txtWarnConfirmPassword,txtWarnGender;
    private Button btnPickImage,btnTakePhoto,btnRegister,btnLogInRedirect;
    private RadioGroup rgGender;
    private ConstraintLayout parent;
    private static String file = "Users.ser";
    private static ArrayList<User> allUsers = new ArrayList<>();
    String homeDirectory = System.getProperty("user.home");
    String filePath = homeDirectory + File.separator + "users.ser";
    // I'm Make this to band the user to go back to the NotesActivity without authentication
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        allUsers = loadUsersFromFile("Users.ser",RegisterActivity.this);
        initViews();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    initRegister();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnLogInRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LogInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initRegister() throws IOException {
        Log.d(TAG, "initRegister: Started");

        if(validateData()){
            showSnackBar();


            //todo fix the error here

            User user = new User(edtTxtName.getText().toString(),edtTxtPassword.getText().toString());
            allUsers = loadUsersFromFile(file,RegisterActivity.this);
            addUser(allUsers,user);

            Intent intent = new Intent(RegisterActivity.this,NotesActivity.class);
            intent.putExtra("User", edtTxtName.getText().toString());
            startActivity(intent);
        }
    }

    private void showSnackBar(){
        Log.d(TAG,"showSnackBar: Started");
        txtWarnName.setVisibility(View.GONE);
        txtWarnPassword.setVisibility(View.GONE);
        txtWarnConfirmPassword.setVisibility(View.GONE);
        txtWarnGender.setVisibility(View.GONE);

        Snackbar.make(parent,"User Registered",Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }
    private boolean validateData(){
        Log.d(TAG, "validateData: Started");
        if(edtTxtName.getText().toString().equals("")){
            txtWarnName.setVisibility(View.VISIBLE);
            txtWarnName.setText("Enter your Name");
            return false;
        }
        if(edtTxtPassword.getText().toString().equals("")){
            txtWarnPassword.setVisibility(View.VISIBLE);
            txtWarnPassword.setText("Enter your password");
            return false;
        }
        if(edtTxtConfirmPassword.getText().toString().equals("")){
            txtWarnConfirmPassword.setVisibility(View.VISIBLE);
            txtWarnConfirmPassword.setText("Confirm your password");
            return false;
        }

        if(!edtTxtPassword.getText().toString().equals(edtTxtConfirmPassword.getText().toString())){
            txtWarnConfirmPassword.setVisibility(View.VISIBLE);
            txtWarnConfirmPassword.setText("Password doesn't match");
            return false;
        }

        User user = new User(edtTxtName.getText().toString(),edtTxtPassword.getText().toString());
        if(!addUser(allUsers,user)){
            Snackbar.make(parent,"User already exists",Snackbar.LENGTH_INDEFINITE)
                    .setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
            return false;
        }

        return true;

    }
    private void initViews(){
        Log.d(TAG,"initViews: Started");
        edtTxtName = findViewById(R.id.Name);
        edtTxtPassword = findViewById(R.id.Password);
        edtTxtConfirmPassword = findViewById(R.id.confirmPassword);


        btnRegister = findViewById(R.id.register);
        btnLogInRedirect = findViewById(R.id.loginRedirect);



        txtWarnName = findViewById(R.id.txtWarnName);
        txtWarnPassword = findViewById(R.id.txtWarnPassword);
        txtWarnConfirmPassword = findViewById(R.id.txtWarnConfirmPassword);
        txtWarnGender = findViewById(R.id.txtWarnGender);

        parent = findViewById(R.id.parent);
    }

    private static void saveUsersToFileAndUpdate(ArrayList<User> users, String filename, Context context) {
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private boolean addUser(ArrayList<User> allUsers, User user){

        boolean exist =false;
        for(User i : allUsers){
            if(i.Name.equals(user.Name)) {
                System.out.println("User already exist login or try another name!!!!");
                return false;
            }
        }


        allUsers.add(user);
        saveUsersToFileAndUpdate(allUsers, file,RegisterActivity.this);
        System.out.println("user added to the database");
        return true;
    }


}