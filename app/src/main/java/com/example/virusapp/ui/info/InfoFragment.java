package com.example.virusapp.ui.info;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.virusapp.MainActivity;
import com.example.virusapp.R;
import com.example.virusapp.data.DataManager;
import com.example.virusapp.ui.news.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends Fragment {

    private InfoViewModel infoViewModel;
    private RecyclerView recyclerView;
    private DataManager service;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //infoViewModel =
          //      ViewModelProviders.of(this).get(InfoViewModel.class);
        final ConstraintLayout domain_layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_info, container, false);
        View root = inflater.inflate(R.layout.fragment_info, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(domain_layout.getContext());

        recyclerView=root.findViewById(R.id.info_recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        final InfoAdapter adapter = new InfoAdapter(this.getContext(),null);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.INVISIBLE);

        SearchView searchView=root.findViewById(R.id.info_searchView);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getContext(), "your key is:" + query,
                        Toast.LENGTH_SHORT).show();
                //adapter.setEntities();
                service= ((MainActivity)getActivity()).getmService();
                if(service==null) return false;
                adapter.setEntities(service.getEntities(query));
                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);

                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return root;
    }
}