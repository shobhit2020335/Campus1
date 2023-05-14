package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.campuscravings.R;
import com.example.campuscravings.databinding.FragmentCampusBinding;
import com.example.campuscravings.databinding.FragmentNotificationBinding;

import java.util.ArrayList;

import Adapter.campusListAdapter;
import Adapter.notificationAdapter;


public class NotificationFragment extends Fragment {


    FragmentNotificationBinding binding;
    notificationAdapter l;
    public static ArrayList<String> nList;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNotificationBinding.inflate(inflater, container, false);

        nList = new ArrayList<>();
        nList.add("Welcome to campus cravings.");
        nList.add("Your order for 2 burgers is placed successfully");
        nList.add("Yay! Your order for 2 burgers is ready.");
        nList.add("Hungry? Fill your cravings on campus with Campus Cravings");
        nList.add("Your order for 1 aloo parantha is placed successfully. .");
        nList.add("Your order for 1 aloo parantha is Ready. .");
        nList.add("Your order for 3 omlette is placed successfully. .");
        nList.add("Your order for 1 aloo parantha is placed successfully. .");
        nList.add("Your order for 3 omlette is ready. .");
        nList.add("Your order for 1 aloo parantha is ready. .");
        binding.recycler1.setLayoutManager(new LinearLayoutManager(getContext()));
        l = new notificationAdapter(getContext(), nList);
        binding.recycler1.setAdapter(l);
        l.notifyDataSetChanged();

        binding.textView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nList.clear();
            }
        });
        return binding.getRoot();
    }
}