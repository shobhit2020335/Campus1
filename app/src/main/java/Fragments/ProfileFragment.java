package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.campuscravings.R;
import com.example.campuscravings.User;
import com.example.campuscravings.databinding.FragmentCampusBinding;
import com.example.campuscravings.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentProfileBinding.inflate(inflater,container,false);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();


        database.getReference().child("users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    User user = snapshot.getValue(User.class);
                    if (snapshot.hasChild("profilephoto")) {


                        Picasso.get()
                                .load(user.getProfilephoto())
                                .placeholder(R.drawable.placeholder)
                                .into(binding.profilePic);
                    } else {
                        binding.profilePic.setImageResource(R.drawable.profile1);
                    }
                    binding.profileName.setText(user.getName());
                    binding.profileMail.setText(user.getEmail());
                    binding.phoneProfile.setText(user.getPhno().toString());
                    binding.addressProfile.setText(user.getAddress());
                    binding.passwordProfile.setText(user.getPassword());
                    binding.dobProfile.setText(user.getDob());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.profileLayout.setVisibility(View.INVISIBLE);
            }
        });
        return binding.getRoot();
    }
}