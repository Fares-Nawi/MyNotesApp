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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class edtDeleteNoteActivity extends AppCompatActivity {

    private static final String TAG = "edtDeleteNoteActivity";
    private static String file = "Users.ser";
    private static ArrayList<User> allUsers = new ArrayList<>();


    private EditText edtTxtNote;
    private Button btnEdt, btndelete;


    private ConstraintLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edt_delete_note);
        allUsers = loadUsersFromFile(file, edtDeleteNoteActivity.this);
        initViews();

        Intent intent = getIntent();
        String user = intent.getStringExtra("User");
        int index = intent.getIntExtra("Index",0);
        String text = intent.getStringExtra("Text");

        edtTxtNote.setText(text);

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                deleteNoteFromaUser(allUsers, user, index);
                Intent intent = new Intent(edtDeleteNoteActivity.this, NotesActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);

            }


        });

        btnEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newNote = edtTxtNote.getText().toString();

                for (User i : allUsers) {
                    System.out.println(i.toString());
                }
                deleteNoteFromaUser(allUsers, user, index);
                addNoteToaUser(allUsers, user, newNote);
                Intent intent = new Intent(edtDeleteNoteActivity.this, NotesActivity.class);

                intent.putExtra("User", user);
                startActivity(intent);

            }


        });
    }

    private void initViews() {
        Log.d(TAG, "initViews: Started");

        edtTxtNote = findViewById(R.id.txtNote);

        btnEdt = findViewById(R.id.edit);
        btndelete = findViewById(R.id.delete);
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

    private void addNoteToaUser(ArrayList<User> allUsers, String Name, String Note) {
        for (User i : allUsers) {
            System.out.println(i.Name + "  " + Name);
            if (i.Name.equals(Name)) {
                i.Notes.add(Note);
                saveUsersToFileAndUpdate(allUsers, file, edtDeleteNoteActivity.this);
                System.out.println("new Note added");
                break;
            }
        }
    }

    private void deleteNoteFromaUser(ArrayList<User> allUsers, String Name, int index) {
        for (User i : allUsers) {
            if (i.Name.equals(Name)) {
                i.Notes.remove(index);
                saveUsersToFileAndUpdate(allUsers, file, edtDeleteNoteActivity.this);
                System.out.println("Note deleted from a User");
                break;
            }
        }
    }
}