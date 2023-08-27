package com.example.mynotesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class NotesActivity extends AppCompatActivity {
    private static final String TAG = "NotesActivity";

    private RecyclerView notesRecView;
    private Button btnAddNote,btnLogout;

    private ArrayList<User> allUsers = new ArrayList<>();

    private static String file = "Users.ser";
    private ConstraintLayout parent;

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        notesRecView = findViewById(R.id.notesRecView);
        ArrayList<String> Notes = new ArrayList<>();



        allUsers = loadUsersFromFile(file,NotesActivity.this);
        Intent intent = getIntent();
        String user = intent.getStringExtra("User");

        for (User i : allUsers){
            if(i.Name.equals(user)){
                Notes = i.Notes;
            }
        }


        NotesRecViewAdapter adapter = new NotesRecViewAdapter(this);
        adapter.setNotes(Notes);

        notesRecView.setAdapter(adapter);
        notesRecView.setLayoutManager(new LinearLayoutManager(this));
        initViews();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NotesActivity.this, "User Logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NotesActivity.this,LogInActivity.class);
                startActivity(intent);
            }
        });


        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                String user = intent.getStringExtra("User");
                intent = new Intent(NotesActivity.this,writeNoteActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });


    }

    private void initViews(){
        Log.d(TAG,"initViews: Started");

        btnAddNote  = findViewById(R.id.addNote);
        btnLogout = findViewById(R.id.logout);


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