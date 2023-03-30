package Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.campuscravings.LoadingDialog;
import com.example.campuscravings.R;
import com.example.campuscravings.databinding.FragmentEditProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditProfile extends Fragment {

    FragmentEditProfileBinding binding;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    LoadingDialog loadingDialog = new LoadingDialog((Activity) getContext());

    public EditProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        binding.uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                loadingDialog.startLoading();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 11);

            }
        });

        binding.SaveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                        Log.d("1234567",binding.EditName.getText().toString());
                if (!binding.EditName.getText().toString().isEmpty()) {
                    String updatedname = binding.EditName.getText().toString();
                    database.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("name").setValue(updatedname);
                    binding.EditName.setText(updatedname);
                    Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                }
                if (!binding.Editaddress.getText().toString().isEmpty()) {
                    String updatedaddress = binding.Editaddress.getText().toString();
                    database.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("address").setValue(updatedaddress);
                    binding.Editaddress.setText(updatedaddress);
                    Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

                }

                if (!binding.Editdob.getText().toString().isEmpty()) {
                    String updateddob = binding.Editdob.getText().toString();
                    database.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("dob").setValue(updateddob);
                    binding.Editdob.setText(updateddob);
                    Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

                }
            }
        });
//
        return binding.getRoot();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            if (data.getData() != null) {
                Uri uri = data.getData();
                binding.uploadimage.setImageURI(uri);
//                loadingDialog.dismissdialog();
                binding.SaveB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Log.d("1234567",binding.EditName.getText().toString());
                        if (!binding.EditName.getText().toString().isEmpty()) {
                            String updatedname = binding.EditName.getText().toString();
                            database.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("name").setValue(updatedname);
                            binding.EditName.setText(updatedname);

                        }
                        if (!binding.Editaddress.getText().toString().isEmpty()) {
                            String updatedaddress = binding.Editaddress.getText().toString();
                            database.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("address").setValue(updatedaddress);
                            binding.Editaddress.setText(updatedaddress);
                        }

                        if (!binding.Editdob.getText().toString().isEmpty()) {
                            String updateddob = binding.Editdob.getText().toString();
                            database.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("dob").setValue(updateddob);
                            binding.Editdob.setText(updateddob);
                        }
//                        loadingDialog.startLoading();
//

                        final StorageReference reference = storage.getReference().child("profile_photo").child(FirebaseAuth.getInstance().getUid());
                        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getContext(), "profile image updated successfully", Toast.LENGTH_SHORT).show();
//                                loadingDialog.dismissdialog();
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        database.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("profilephoto").setValue(uri.toString());
//                                        loadingDialog.dismissdialog();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });
                    }
                });
            }


        }
    }
}