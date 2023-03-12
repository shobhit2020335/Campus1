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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.campuscravings.R;

import java.util.ArrayList;

import Model.MenuModel;
public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.Viewholder>{

    private Context context;
    private ArrayList<MenuModel> modelArrayList;
    private FragmentManager fragmentManager;

    public MenuListAdapter(Context context, ArrayList<MenuModel> modelArrayList, FragmentManager fragmentManager) {
        this.context = context;
        this.modelArrayList = modelArrayList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_items, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.textView.setText(modelArrayList.get(position).getName());
//        holder.campusCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ShopFragment shopFragment = new ShopFragment(modelArrayList.get(position).getKey(), modelArrayList.get(position).getShopsSnapshot()); // position might cause problems
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.parentActivity, shopFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//
//            }
//        });
        Glide.with(context)
                .load(modelArrayList.get(position).getImg()).placeholder(R.drawable.placeholder).addListener(new RequestListener<Drawable>() {
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
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        CardView menuCard;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.menuImage);
            textView = itemView.findViewById(R.id.menuName);
            menuCard = itemView.findViewById(R.id.menuCard);

        }
    }
}
