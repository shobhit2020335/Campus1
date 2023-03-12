package Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.campuscravings.R;
import com.example.campuscravings.databinding.FragmentCampusBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.campusListAdapter;
import Model.collegeModel;


public class CampusFragment extends Fragment {
    FragmentCampusBinding binding;
    private campusListAdapter l;
    private DatabaseReference myref;
    private ArrayList<collegeModel> modelList;
    private FragmentManager fragmentManager;

    public CampusFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myref= FirebaseDatabase.getInstance().getReference();
        this.fragmentManager= getActivity().getSupportFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentCampusBinding.inflate(inflater,container,false);
        binding.campusRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        modelList=new ArrayList<>();
        clearall();
        getdata();
        return binding.getRoot();
    }

    private void getdata(){
        Query query=myref.child("campus");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearall();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    collegeModel model=new collegeModel();
                    model.setImg((String) snapshot1.child("img").getValue());
                    model.setName(snapshot1.child("name").getValue().toString());
                    model.setKey(snapshot1.getKey());
                    // if shops are present in database then add those in model
                    if(snapshot1.hasChild("shops"))
                        model.setShopsSnapshot(snapshot1.child("shops"));
                    modelList.add(model);
                }

                l=new campusListAdapter(getContext(), modelList, fragmentManager);
                binding.campusRecyclerView.setAdapter(l);
                l.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void clearall(){
        if(modelList!=null){
            modelList.clear();
            if(l!=null){
                l.notifyDataSetChanged();
            }
        }
        modelList=new ArrayList<>();
    }

}