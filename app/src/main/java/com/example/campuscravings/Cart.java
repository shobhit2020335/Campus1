package com.example.campuscravings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.campuscravings.databinding.ActivityCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.HomePageAdapter;
import Adapter.cartAdapter;
import Model.FoodModel;
import Model.cartModel;

public class Cart extends AppCompatActivity {

    ActivityCartBinding binding;
    private cartAdapter l;
    private DatabaseReference myref;
    FirebaseAuth auth;
    public static ArrayList<cartModel> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        myref = FirebaseDatabase.getInstance().getReference();
        modelList = new ArrayList<>();
        clearall();
        getdata();
        binding.cartitems.setLayoutManager(new LinearLayoutManager(Cart.this));


        binding.iconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getdata() {
        Query query = myref.child("users").child(auth.getUid()).child("cart").child("item");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearall();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                    Log.d("FOOOOOOOODDD",(String) snapshot1.child("foodname").getValue());

                    cartModel model = new cartModel();
//                    model.setImage((String) snapshot1.child("itemname").getValue());
                    model.setImage((String) snapshot1.child("itemimage").getValue());
                    model.setName((String) snapshot1.child("itemname").getValue());
                    Object priceObj = snapshot1.child("itemprice").getValue();
                    if (priceObj != null) {
                        model.setPrice((Long) priceObj);
                    }
//                    model.setPrice((Long) snapshot1.child("itemprice").getValue());
                    model.setQuantity((Long) snapshot1.child("itemquantity").getValue());
                    modelList.add(model);
                }
                Long total = 0L;
                for (int i = 0; i < modelList.size(); i++) {

                    Long price = modelList.get(i).getPrice();
                    if (price != null) {
                        total += price;
                    }
                }
                binding.subtotalAmt.setText(String.valueOf(total));
                binding.totalAmt.setText(String.valueOf(total - 0));
                Long finalTotal = total;
                binding.checkoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Cart.this, payments.class);
                        intent.putExtra("subtotal", String.valueOf(finalTotal));
                        intent.putExtra("total", String.valueOf(finalTotal));
                        intent.putExtra("discount", String.valueOf(0));
                        startActivity(intent);

                    }
                });

                l = new cartAdapter(Cart.this, modelList);

                binding.cartitems.setAdapter(l);
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