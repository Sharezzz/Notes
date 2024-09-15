package com.example.messenger.notes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    CircleImageView profileImage_iv;

    TextView title_tv, text_tv, owner_tv;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        title_tv = itemView.findViewById(R.id.title_tv);
        text_tv = itemView.findViewById(R.id.text_tv);
        owner_tv = itemView.findViewById(R.id.owner_tv);
        profileImage_iv = itemView.findViewById(R.id.profile_iv);
    }
}
