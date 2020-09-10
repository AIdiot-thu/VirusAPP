package com.example.virusapp.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.virusapp.R;
import com.example.virusapp.tableview.OnTableClick;
import com.example.virusapp.tableview.TableView;

public class MapFragment extends Fragment {

    private MapViewModel mapViewModel;
    private TableView tableView;
    private String[] mlistHead={"省份","累计确诊","疑似病例","治愈病例","死亡人数"};//声明表格表头
    private String[][] mlistContent={{"湖南","pcy","2014211617","男","god"}, {"2","pcy","2014211617","男","god"}};//对应内容

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel =
                ViewModelProviders.of(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
//        final TextView textView = root.findViewById(R.id.text_map);
//        mapViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        tableView = root.findViewById(R.id.tabview);
        tableView.setTable(new OnTableClick() {
            @Override
            public void onTableClickListener(int row, int col) {

            }
        });
        tableView.setTableHead(mlistHead);
        tableView.setTableContent(mlistContent);
        return root;
    }
}