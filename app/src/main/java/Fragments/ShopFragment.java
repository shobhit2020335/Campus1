package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.campuscravings.R;
import com.example.campuscravings.databinding.FragmentCampusBinding;
import com.example.campuscravings.databinding.FragmentShopBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.ShopListAdapter;
import Adapter.campusListAdapter;
import Model.ShopModel;
import Model.collegeModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment {

    private String campusKey; // used for fetching shops data of given campus
    FragmentShopBinding binding;
    private ShopListAdapter shopListAdapter;

    private ArrayList<ShopModel> modelList;
    private FragmentManager fragmentManager;
    private DataSnapshot shopsSnapshot;


    public ShopFragment(String campusKey, DataSnapshot shopsSnapshot) {
        // saving key of campus this fragment is related to
        this.campusKey = campusKey;
        this.shopsSnapshot = shopsSnapshot;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fragmentManager= getActivity().getSupportFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentShopBinding.inflate(inflater,container,false);
        binding.shopRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        modelList=new ArrayList<>();
        clearall();
        getdata();
        return binding.getRoot();
    }

    private void getdata(){
        clearall();
        if(this.shopsSnapshot != null){
            for(DataSnapshot snapshot:this.shopsSnapshot.getChildren()){
                ShopModel model=new ShopModel();
                if(snapshot.hasChild("img"))
                    model.setImg((String) snapshot.child("img").getValue());
                if(snapshot.hasChild("name"))
                    model.setName((String )snapshot.child("name").getValue());
                model.setKey(snapshot.getKey());
                model.setCampusKey(this.campusKey);
                // if menu are present in database then add those in model
                if(snapshot.hasChild("menu"))
                    model.setMenuSnapshot(snapshot.child("menu"));
                modelList.add(model);
            }

            shopListAdapter=new ShopListAdapter(getContext(), modelList, fragmentManager);
            binding.shopRecyclerView.setAdapter(shopListAdapter);
            shopListAdapter.notifyDataSetChanged();
        }
    }

    private void clearall(){
        if(modelList!=null){
            modelList.clear();
            if(shopListAdapter!=null){
                shopListAdapter.notifyDataSetChanged();
            }
        }
        modelList=new ArrayList<>();
    }


}