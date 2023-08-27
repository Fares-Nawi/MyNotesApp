package com.example.mynotesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
//TODO when i add a note the notes in NotesActivity gone
public class writeNoteActivity extends AppCompatActivity {
    private static final String TAG = "writeNoteActivity";
    private static String file = "Users.ser";
    private static ArrayList<User> allUsers = new ArrayList<>();


    private EditText edtTxtNote;
    private Button btnAdd;


    private ConstraintLayout parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);
        initViews();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String user = intent.getStringExtra("User");
                String newNote =  edtTxtNote.getText().toString();
                allUsers = loadUsersFromFile(file,writeNoteActivity.this);
                for (User i : allUsers) {
                    System.out.println(i.toString());
                }
                addNoteToaUser(allUsers,user,newNote);
                intent = new Intent(writeNoteActivity.this,NotesActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);



            }


        });
    }

    private void initViews(){
        Log.d(TAG,"initViews: Started");

        edtTxtNote = findViewById(R.id.txtNote);
        edtTxtNote.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        edtTxtNote.setGravity(Gravity.TOP);
        btnAdd = findViewById(R.id.add);
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
    private void saveUsersToFileAndUpdate(ArrayList<User> users, String filename, Context context) {
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addNoteToaUser(ArrayList<User> allUsers, String Name, String Note){
        for (User i :allUsers){
            System.out.println(i.Name+"  "+Name);
            if(i.Name.equals(Name)){
                i.Notes.add(Note);
                saveUsersToFileAndUpdate(allUsers, file,writeNoteActivity.this);
                System.out.println("new Note added");
                break;
            }
        }
    }
}