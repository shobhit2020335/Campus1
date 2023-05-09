package Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import Model.cartModel;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.Viewholder> {
    private ArrayList<cartModel> modelArrayList;


    private Context context;
    private DatabaseReference myref;
    FirebaseAuth auth;

    public cartAdapter(Context context, ArrayList<cartModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        myref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fooditem, parent, false);
        return new Viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        int pos = position;
        holder.foodname.setText(modelArrayList.get(position).getName());
        holder.foodprice.setText(modelArrayList.get(position).getPrice().toString());
        holder.foodquantity.setText(modelArrayList.get(position).getQuantity().toString());
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
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
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
                                    int newPrice = (int) (modelArrayList.get(pos).getPrice()*newQuantity);

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


//        amount.set
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        ImageView foodimage;
        TextView foodprice;
        TextView foodname;
        TextView foodquantity;
        TextView amount;
        Button deleteItem;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            foodimage = itemView.findViewById(R.id.menuImage);
            foodprice = itemView.findViewById(R.id.menuPrice);
            foodname = itemView.findViewById(R.id.menuName);
            foodquantity = itemView.findViewById(R.id.cartQuantity);
            amount = itemView.findViewById(R.id.subtotal_amt);
            deleteItem = itemView.findViewById(R.id.deleteItemCart);


        }
    }

}
