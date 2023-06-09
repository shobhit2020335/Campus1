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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.HomePageAdapter;
import Adapter.campusListAdapter;
import Adapter.cartAdapter;
import Model.FoodModel;
import Model.cartModel;
import Model.collegeModel;


public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    private HomePageAdapter l;
    private DatabaseReference myref;
    private ArrayList<FoodModel> modelList;
    public  static String CAMPUSNAME="IIIT";
    FirebaseAuth auth;

    DatabaseReference cartItemsRef;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
//        cartItemsRef =  FirebaseDatabase.getInstance().getReference("users/"+auth.getUid()+"/cart/item");


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

        binding.homeCollegename.setText(CAMPUSNAME);
//        cartItemsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    if(snapshot.hasChildren()){
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        for (int i=0;i<modelList.size();i++){
            if (modelList.get(i).getQuatntity()>0){
                binding.homeCarticon.setBackgroundResource(R.drawable.ic_filled_shopping_cart_foreground);
            }
        }
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
                    model.setQuatntity(0L);
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query query1 = myref.child("users").child(auth.getUid()).child("cart").child("item");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                clearall();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                    Log.d("FOOOOOOOODDD",(String) snapshot1.child("foodname").getValue());

//                    cartModel model = new cartModel();
//                    model.setImage((String) snapshot1.child("itemname").getValue());
//                    model.setImage((String) snapshot1.child("itemimage").getValue());
                    if (snapshot1.exists()) {
                        String name = ((String) snapshot1.child("itemname").getValue());
//                    model.setPrice((Long) snapshot1.child("itemprice").getValue());
                        Long quantity = ((Long) snapshot1.child("itemquantity").getValue());
                        for (int i = 0; i < modelList.size(); i++) {
                            if (modelList.get(i).getName().equals(name)) {
                                modelList.get(i).setQuatntity(quantity);
                            }
                        }
                    }

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