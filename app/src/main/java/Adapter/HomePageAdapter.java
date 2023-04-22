package Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.campuscravings.R;

import java.util.ArrayList;

import Model.FoodModel;
import Model.collegeModel;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.Viewholder> {

    private Context context;

    public HomePageAdapter(Context context, ArrayList<FoodModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    private ArrayList<FoodModel> modelArrayList;

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homeitems, parent, false);
        return new HomePageAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.foodname.setText(modelArrayList.get(position).getName());
        holder.foodprice.setText(modelArrayList.get(position).getPrice().toString());
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
        if(Integer.parseInt(holder.foodquantity.getText().toString())>0){
            int qty=Integer.parseInt(holder.foodquantity.getText().toString());

        }
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


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            foodimage = itemView.findViewById(R.id.cart_item_img);
            foodprice = itemView.findViewById(R.id.item_price);
            foodname = itemView.findViewById(R.id.item_name);
            foodquantity = itemView.findViewById(R.id.qty);


        }
    }
}
