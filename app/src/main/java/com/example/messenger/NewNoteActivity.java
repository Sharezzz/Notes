package com.example.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.messenger.databinding.ActivityNewNoteBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;

public class NewNoteActivity extends AppCompatActivity {

    private ActivityNewNoteBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.textEt.getText().toString().isEmpty() || binding.titleEt.getText().toString().isEmpty()){
                    Toast.makeText(NewNoteActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                }else{
                    HashMap<String, String> noteInfo = new HashMap<>();
                    noteInfo.put("title", binding.titleEt.getText().toString());
                    noteInfo.put("text", binding.textEt.getText().toString());
                    startActivity(new Intent(NewNoteActivity.this, MainActivity.class));
                }
            }
        });
    }
}