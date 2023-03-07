package Adapter;

import android.content.Context;
import android.content.Intent;
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

import Model.collegeModel;

public class campusListAdapter extends RecyclerView.Adapter<campusListAdapter.Viewholder> {


    private Context context;
    private ArrayList<collegeModel> modelArrayList;

    public campusListAdapter(Context context, ArrayList<collegeModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.campusitems, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.textView.setText(modelArrayList.get(position).getName());
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
        String key = modelArrayList.get(position).getKey();
//        holder.textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, airport.class);
//                intent.putExtra("name", holder.textView.getText().toString());
//                intent.putExtra("i1", holder.imageView.toString());
//                intent.putExtra("key", key);
//                context.startActivity(intent);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//
//            }
//        });


    }
    @Override
    public int getItemCount() {
        return  modelArrayList.size();
    }


    class Viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.campusImage);
            textView = itemView.findViewById(R.id.campusName);


        }
    }
}
