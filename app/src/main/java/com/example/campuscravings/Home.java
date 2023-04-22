package com.example.campuscravings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.campuscravings.databinding.ActivityHomeBinding;
import com.iammert.library.readablebottombar.ReadableBottomBar;

import androidx.fragment.app.Fragment;
//import com.example.campuscravings.Fragments.HomeFragment;
import Fragments.CampusFragment;
import Fragments.HomeFragment;
import Fragments.NotificationFragment;
import Fragments.ProfileFragment;

public class Home extends AppCompatActivity {
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//        Fragment fragment= new Fragment(/*/);
        transaction.replace(R.id.parent, new HomeFragment());
        transaction.commit();
        binding.BottomNavigation.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {
                FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
                switch (i){
                    case 0:
                        transaction.replace(R.id.parent, new HomeFragment());
                        Toast.makeText(Home.this, "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        transaction.replace(R.id.parent,new CampusFragment());
                        Toast.makeText(Home.this, "Campus", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        transaction.replace(R.id.parent,new NotificationFragment());
                        Toast.makeText(Home.this, "Notification", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        transaction.replace(R.id.parent,new ProfileFragment());
                        Toast.makeText(Home.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                }
                transaction.commit();
            }
        });
    }
}
//FF3F5E18