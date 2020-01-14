package com.example.news.ui.explore;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.api.ApiClient;
import com.example.news.api.ApiInterface;
import com.example.news.models.Source;
import com.example.news.models.SourceWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreFragment extends Fragment {
    public static final String API_KEY = "ac205adfdfed4515b602bf5f610cfed9";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Source> sources = new ArrayList<>();
    private ExploreAdapter adapter;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_explore, container, false);

        recyclerView = view.findViewById(R.id.source_recyclerView);
        layoutManager = new LinearLayoutManager(getActivity().getApplication());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        LoadJson();

        return view;

    }

    public void LoadJson(){

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);


        Call<SourceWrapper> call;

        call = apiInterface.getSources("FR", "fr",API_KEY);


        call.enqueue(new Callback<SourceWrapper>() {


            @Override
            public void onResponse(Call<SourceWrapper> call, Response<SourceWrapper> response) {
                if (response.isSuccessful() && response.body().getSources() != null){

                    if (!sources.isEmpty()){
                        sources.clear();
                    }

                    sources = response.body().getSources();
                    adapter = new ExploreAdapter(sources,getContext());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    initListener();


                } else {
                    Toast.makeText(getActivity().getApplication(),"Noresult",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<SourceWrapper> call, Throwable t) {

            }
        });

    }

    private void initListener(){

        adapter.setOnItemClickListener(new ExploreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sources.get(position).getUrl()));
                startActivity(intent);
            }
        });

    }


}
