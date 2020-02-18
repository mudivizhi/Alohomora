package com.mudivizhi.alohomora;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class AllOffers extends Fragment {

    private RecyclerView rv;
    private List<AllOffersModel> modelList;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_all_offers,container,false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rv = getActivity().findViewById(R.id.rv_all_offers);



        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(layoutManager);

        modelList = new ArrayList<>();


        String temp = "https://yt3.ggpht.com/a/AGF-l7-BBIcC888A2qYc3rB44rST01IEYDG3uzbU_A=s900-c-k-c0xffffffff-no-rj-mo";

        modelList.add(new AllOffersModel(temp,"70% Offer","Upto 70% Off on Electronics + 10% Discount on Axis Bank Cards"));
        modelList.add(new AllOffersModel(temp,"70% Offer","Upto 70% Off on Electronics + 10% Discount on Axis Bank Cards"));
        modelList.add(new AllOffersModel(temp,"70% Offer","Upto 70% Off on Electronics + 10% Discount on Axis Bank Cards"));
        modelList.add(new AllOffersModel(temp,"70% Offer","Upto 70% Off on Electronics + 10% Discount on Axis Bank Cards"));
        modelList.add(new AllOffersModel(temp,"70% Offer","Upto 70% Off on Electronics + 10% Discount on Axis Bank Cards"));
        modelList.add(new AllOffersModel(temp,"70% Offer","Upto 70% Off on Electronics + 10% Discount on Axis Bank Cards"));


        final AllOffersAdapter adapter = new AllOffersAdapter(modelList);
        rv.setAdapter(adapter);

    }


}
