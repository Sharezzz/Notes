package com.example.messenger.bottomnav.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.messenger.databinding.FragmentNotesBinding;
import com.example.messenger.notes.Note;
import com.example.messenger.notes.NotesAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NotesFragment extends Fragment {

    private FragmentNotesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotesBinding.inflate(inflater, container, false);
        loadNotes();
        return binding.getRoot();
    }
    private void loadNotes(){
        ArrayList<Note> notes = new ArrayList<Note>();
        FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot noteSnap : snapshot.getChildren()){
                    if(!noteSnap.child("title").getValue().toString().isEmpty() || !noteSnap.child("text").getValue().toString().isEmpty()){
                        String username = noteSnap.child("username").getValue().toString();
                        String title = noteSnap.child("title").getValue().toString();
                        String text = noteSnap.child("text").getValue().toString();
                        String profileImage = noteSnap.child("profileImage").getValue().toString();

                        notes.add(new Note(title,text,username,profileImage));
                    }
                }
                binding.notesRv.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.notesRv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                binding.notesRv.setAdapter(new NotesAdapter(notes));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
