package Fragments;

//import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import androidx.fragment.app.Fragment;
import com.example.campuscravings.Cart;
import com.example.campuscravings.R;
import com.example.campuscravings.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.HomePageAdapter;
import Adapter.campusListAdapter;
import Model.FoodModel;
import Model.collegeModel;


public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    private HomePageAdapter l;
    private DatabaseReference myref;
    private ArrayList<FoodModel> modelList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myref = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater,container,false);


        modelList = new ArrayList<>();
        clearall();
        getdata();
        binding.homeFooditemRV.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.homeCarticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), Cart.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }

    private void getdata() {
        Query query = myref.child("campus").child("c2").child("kodechef").child("food");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearall();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                    Log.d("FOOOOOOOODDD",(String) snapshot1.child("foodname").getValue());

                    FoodModel model = new FoodModel();
                    model.setImage((String) snapshot1.child("foodimg").getValue());
                    model.setName((String) snapshot1.child("foodname").getValue());
                    model.setPrice((Long) snapshot1.child("foodprice").getValue());
//                    model.setQuatntity((String) snapshot1.child("foodquantity").getValue());
//                    if (snapshot1.child("foodname").getValue() != null) {
//                        Log.d("FOOOOODDD",snapshot1.child("foodname").getValue().toString());
//                        model.setName(snapshot1.child("foodname").getValue().toString());
//                    }
//                    if (snapshot1.child("foodprice").getValue() != null) {
//                        model.setPrice((Long) snapshot1.child("foodprice").getValue());
//                    }
//                    model.setQuatntity(snapshot1.child("fooodquantity").getValue().toString());
//                    model.setKey(snapshot1.getKey());
                    modelList.add(model);
                }
                l = new HomePageAdapter(getContext(), modelList);
                binding.homeFooditemRV.setAdapter(l);
                l.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void clearall() {
        if (modelList != null) {
            modelList.clear();
            if (l != null) {
                l.notifyDataSetChanged();
            }
        }
        modelList = new ArrayList<>();
    }

}