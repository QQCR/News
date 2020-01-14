package com.example.news.ui.collection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.AppContext;
import com.example.news.R;
import com.example.news.database.SaveDao;
import com.example.news.database.UserDao;
import com.example.news.models.Save;

import java.util.ArrayList;
import java.util.List;

public class CollectionFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Save> saves = new ArrayList<>();
    private CollectionAdapter adapter;
    private SaveDao saveDao;
    private UserDao userDao;
    private AppContext appContext;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);

        recyclerView = view.findViewById(R.id.saved_recyclerView);
        layoutManager = new LinearLayoutManager(getActivity().getApplication());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        appContext = (AppContext) getActivity().getApplication();
        saveDao = new SaveDao(getContext());
        userDao = new UserDao(getContext());

        loadSaved();

        return view;
    }

    private void loadSaved(){

        saves.clear();
        int id = userDao.getIdbyName(appContext.getUser().getUsername());
        if(saveDao.getUserSave(id)!=null){
            saves.addAll(saveDao.getUserSave(id));
        }
        adapter = new CollectionAdapter(saves,getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        initListener();

    }

    private void initListener(){

        adapter.setOnItemClickListener(new CollectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(saves.get(position).getUrl()));
                startActivity(intent);
            }
        });

        adapter.setOnButtonClickListener(new CollectionAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClicked(View view, int position) {
                if(saveDao.deleteSave(saves.get(position).getId())){
                    Toast.makeText(getContext(), "delete successful!", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                loadSaved();
            }
        });
    }
}
