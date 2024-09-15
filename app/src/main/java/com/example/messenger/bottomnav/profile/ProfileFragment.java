package com.example.messenger.bottomnav.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.messenger.LoginActivity;
import com.example.messenger.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private Uri file;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        loadInfo();
        binding.profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newNote();
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });

        return binding.getRoot();
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().getData() != null){
                file = result.getData().getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), file);
                    binding.profileImg.setImageBitmap(bitmap);
                }catch (IOException e){
                    e.printStackTrace();
                }
                uploadImage();
            }
        }
    });


    private void loadInfo(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String username = snapshot.child("username").getValue().toString();
                        String image = snapshot.child("profileImage").getValue().toString();
                        binding.usernameTv.setText(username);
                        if (!image.isEmpty()){
                            Glide.with(getContext()).load(image).into(binding.profileImg);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(intent);
    }

    private void uploadImage(){
        if (file != null){
            String name = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseStorage.getInstance().getReference().child("images/"+name).putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "Фото загружено успешно", Toast.LENGTH_SHORT).show();

                            FirebaseStorage.getInstance().getReference().child("images/"+name).getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .child("profileImage").setValue(uri.toString());
                                        }
                                    });
                        }
                    });

        }
    }

    private void newNote(){
        if(binding.textEt.getText().toString().isEmpty() || binding.titleEt.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
        }else {
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("title").setValue(binding.titleEt.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("text").setValue(binding.textEt.getText().toString());
            binding.textEt.setText("");
            binding.titleEt.setText("");
        }
    }

    private void deleteNote(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("title").setValue("");
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("text").setValue("");
        binding.textEt.setText("");
        binding.titleEt.setText("");
    }
}
