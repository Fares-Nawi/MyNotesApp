package com.example.mynotesapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesRecViewAdapter extends RecyclerView.Adapter<NotesRecViewAdapter.ViewHolder>{

    private ArrayList<String> notes = new ArrayList<>();
    private Context context;
    public NotesRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtNote.setText(notes.get(position));
        int p = position;
        String text = notes.get(p);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the context from the view
                Context context = v.getContext();

                // Get the user data from the intent
                Intent intent = ((Activity) context).getIntent();
                String user = intent.getStringExtra("User");

                // Create a new intent to start the writeNoteActivity
                Intent writeNoteIntent = new Intent(context, edtDeleteNoteActivity.class);
                writeNoteIntent.putExtra("User", user);
                writeNoteIntent.putExtra("Index", p);
                writeNoteIntent.putExtra("Text", text);
                context.startActivity(writeNoteIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
        // update the data in the Recycle View
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout parent;
        private TextView txtNote;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNote = itemView.findViewById(R.id.txtNote);
            parent = itemView.findViewById(R.id.parent);
        }
    }



}
