package com.example.messenger.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messenger.R;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private ArrayList<Note> notes = new ArrayList<>();

    public NotesAdapter(ArrayList<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item_rv, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.title_tv.setText(notes.get(position).title);
        holder.text_tv.setText(notes.get(position).text);
        holder.owner_tv.setText(notes.get(position).username);
        if (!notes.get(position).profileImage.isEmpty()){
            Glide.with(holder.itemView.getContext()).load(notes.get(position).profileImage).into(holder.profileImage_iv);
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
