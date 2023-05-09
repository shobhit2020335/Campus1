package Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.campuscravings.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Model.FoodModel;
import Model.collegeModel;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.Viewholder> {

    private Context context;
    private DatabaseReference myref;
    FirebaseAuth auth;

    public HomePageAdapter(Context context, ArrayList<FoodModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    private ArrayList<FoodModel> modelArrayList;

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        myref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homeitems, parent, false);
        return new HomePageAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        int pos = position;
        holder.foodname.setText(modelArrayList.get(position).getName());
        holder.foodprice.setText(modelArrayList.get(position).getPrice().toString());
        Long quantity = modelArrayList.get(position).getQuatntity();
        if (quantity != null) {
            holder.foodquantity.setText(modelArrayList.get(position).getQuatntity().toString());
        } else {
            holder.foodquantity.setText("0");
        }

//        holder.foodquantity.setText(modelArrayList.get(position).getQuatntity());
        Glide.with(context)
                .load(modelArrayList.get(position).getImage()).placeholder(R.drawable.placeholder).addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.d("IMAGETAG", e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("IMAGETAG", "IMAGE SET");
                        return false;
                    }
                })
                .into(holder.foodimage);
        if (Integer.parseInt(holder.foodquantity.getText().toString()) > 0) {
            int qty = Integer.parseInt(holder.foodquantity.getText().toString());

        }
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference cartRef = myref.child("users").child(auth.getUid()).child("cart").child("item");
                Query query = cartRef.orderByChild("itemname").equalTo(modelArrayList.get(pos).getName());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // If a match is found, update the quantity and price
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                int currentQuantity = snapshot.child("itemquantity").getValue(Integer.class);
                                if (currentQuantity > 1) {
                                    int newQuantity = currentQuantity - 1;
                                    int newPrice = (int) (modelArrayList.get(pos).getPrice() * newQuantity);

                                    snapshot.getRef().child("itemquantity").setValue(newQuantity);
                                    snapshot.getRef().child("itemprice").setValue(newPrice);
                                }
                                if (currentQuantity <= 1) {
                                    holder.foodquantity.setText("0");
                                    snapshot.getRef().removeValue();
                                }
                            }
                        } else {
                            // If no match is found, create a new child with the item details
                            Toast.makeText(context, "Item is not added to cart", Toast.LENGTH_SHORT).show();
                            holder.foodquantity.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error
                    }
                });

            }
        });
        holder.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "One " + modelArrayList.get(pos).getName() + " added to cart successfully.", Toast.LENGTH_SHORT).show();
                String q = holder.foodquantity.getText().toString();
                int qu = Integer.parseInt(q);
                qu += 1;
                int totalprice = (int) ((modelArrayList.get(pos).getPrice()) * qu);
                holder.foodquantity.setText(String.valueOf(qu));
//                myref.child("users").child(auth.getUid()).child("cart").child("item").child("itemname").setValue(modelArrayList.get(pos).getName());
//                myref.child("users").child(auth.getUid()).child("cart").child("item").child("itemquantity").setValue(qu);
//                myref.child("users").child(auth.getUid()).child("cart").child("item").child("itemprice").setValue(totalprice);
//                String itemId = myref.child("users").child(auth.getUid()).child("cart").child("item").push().getKey();
//                myref.child("users").child(auth.getUid()).child("cart").child("item").child(itemId).child("itemname").setValue(modelArrayList.get(pos).getName());
//                myref.child("users").child(auth.getUid()).child("cart").child("item").child(itemId).child("itemquantity").setValue(qu);
//                myref.child("users").child(auth.getUid()).child("cart").child("item").child(itemId).child("itemprice").setValue(totalprice);

                DatabaseReference cartRef = myref.child("users").child(auth.getUid()).child("cart").child("item");
                Query query = cartRef.orderByChild("itemname").equalTo(modelArrayList.get(pos).getName());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // If a match is found, update the quantity and price
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                int currentQuantity = snapshot.child("itemquantity").getValue(Integer.class);
                                int newQuantity = currentQuantity + 1;
                                int newPrice = (int) (modelArrayList.get(pos).getPrice() * newQuantity);

                                snapshot.getRef().child("itemquantity").setValue(newQuantity);
                                snapshot.getRef().child("itemprice").setValue(newPrice);
                            }
                        } else {
                            // If no match is found, create a new child with the item details
                            int quantity = 1;
                            int price = Math.toIntExact(modelArrayList.get(pos).getPrice());
                            String name = modelArrayList.get(pos).getName();
                            String img = modelArrayList.get(pos).getImage();

                            DatabaseReference newItemRef = cartRef.push();
                            newItemRef.child("itemname").setValue(name);
                            newItemRef.child("itemimage").setValue(img);
                            newItemRef.child("itemquantity").setValue(quantity);
                            newItemRef.child("itemprice").setValue(price);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        ImageView foodimage;
        ImageButton removeItem;
        ImageButton addItem;
        TextView foodprice;
        TextView foodname;
        TextView foodquantity;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            foodimage = itemView.findViewById(R.id.cart_item_img);
            foodprice = itemView.findViewById(R.id.item_price);
            foodname = itemView.findViewById(R.id.item_name);
            foodquantity = itemView.findViewById(R.id.qty);
            removeItem = itemView.findViewById(R.id.removeItem);
            addItem = itemView.findViewById(R.id.addItem);


        }
    }
}
