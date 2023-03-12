package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.campuscravings.R;
import com.example.campuscravings.databinding.FragmentMenuBinding;
import com.example.campuscravings.databinding.FragmentShopBinding;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import Adapter.MenuListAdapter;
import Adapter.ShopListAdapter;
import Model.MenuModel;
import Model.ShopModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    private String campusKey; // used for fetching shops data of given campus
    private String shopKey;
    FragmentMenuBinding binding;
    private MenuListAdapter menuListAdapter;

    private ArrayList<MenuModel> modelList;
    private FragmentManager fragmentManager;
    private DataSnapshot menuSnapshot;


    public MenuFragment(String campusKey, String shopKey, DataSnapshot menuSnapshot) {
        // saving key of campus this fragment is related to
        this.campusKey = campusKey;
        this.menuSnapshot = menuSnapshot;
        this.shopKey = shopKey;
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
        binding=FragmentMenuBinding.inflate(inflater,container,false);
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        modelList=new ArrayList<>();
        clearall();
        getdata();
        return binding.getRoot();
    }

    private void getdata(){
        clearall();
        if(this.menuSnapshot != null){
            for(DataSnapshot snapshot:this.menuSnapshot.getChildren()){
                MenuModel model=new MenuModel();
                if(snapshot.hasChild("img"))
                    model.setImg((String) snapshot.child("img").getValue());
                if(snapshot.hasChild("name"))
                    model.setName((String )snapshot.child("name").getValue());
                model.setKey(snapshot.getKey());
                model.setCampusKey(this.campusKey);
                model.setShopKey(this.shopKey);
                modelList.add(model);
            }

            menuListAdapter=new MenuListAdapter(getContext(), modelList, fragmentManager);
            binding.menuRecyclerView.setAdapter(menuListAdapter);
            menuListAdapter.notifyDataSetChanged();
        }
    }

    private void clearall(){
        if(modelList!=null){
            modelList.clear();
            if(menuListAdapter!=null){
                menuListAdapter.notifyDataSetChanged();
            }
        }
        modelList=new ArrayList<>();
    }
}