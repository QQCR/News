package com.example.news.ui.headlines;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.MainActivity;
import com.example.news.NewsDetailActivity;
import com.example.news.R;
import com.example.news.api.ApiClient;
import com.example.news.api.ApiInterface;
import com.example.news.models.Article;
import com.example.news.models.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HeadlinesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String API_KEY = "ac205adfdfed4515b602bf5f610cfed9";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private TextView topHeadline;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button btnRetry;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_headlines, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        topHeadline = view.findViewById(R.id.topheadelines);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity().getApplication());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        onLoadingSwipeRefresh("");

        errorLayout = view.findViewById(R.id.errorLayout);
        errorImage = view.findViewById(R.id.errorImage);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        btnRetry = view.findViewById(R.id.btnRetry);

        setHasOptionsMenu(true);

        return view;

    }


        public void LoadJson(final String keyword){
            errorLayout.setVisibility(View.GONE);

            swipeRefreshLayout.setRefreshing(true);

            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);


            Call<News> call;
            if (keyword.length() > 0 ){
                call = apiInterface.getNewsSearch(keyword, "fr", "publishedAt", API_KEY);
            } else {
                call = apiInterface.getNews("FR", API_KEY);
            }


            call.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    if (response.isSuccessful() && response.body().getArticle() != null){

                        if (!articles.isEmpty()){
                            articles.clear();
                        }

                        articles = response.body().getArticle();
                        adapter = new Adapter(articles,getContext());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        initListener();

                        topHeadline.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);


                    } else {

                        topHeadline.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                        String errorCode;
                        switch (response.code()) {
                            case 404:
                                errorCode = "404 not found";
                                break;
                            case 500:
                                errorCode = "500 server broken";
                                break;
                            default:
                                errorCode = "unknown error";
                                break;
                        }

                        showErrorMessage(
                                R.drawable.no_result,
                                "No Result",
                                "Please Try Again!\n"+
                                        errorCode);
                    }
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    topHeadline.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);

                    showErrorMessage(
                            R.drawable.oops,
                            "Oops..",
                            "Network failure, Please Try Again\n"+
                                    t.toString());
                }
            });

    }

    private void initListener(){

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                ImageView imageView = view.findViewById(R.id.img);
                Intent intent = new Intent(getContext(), NewsDetailActivity.class);

                Article article = articles.get(position);
                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("img",  article.getUrlToImage());
                intent.putExtra("date",  article.getPublishedAt());
                intent.putExtra("source",  article.getSource().getName());
                intent.putExtra("author",  article.getAuthor());

                Pair<View, String> pair = Pair.create((View)imageView, ViewCompat.getTransitionName(imageView));
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        pair
                );

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, optionsCompat.toBundle());
                }else {
                    startActivity(intent);
                }

            }
        });

    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);

        SearchManager searchManager = (SearchManager) (getActivity().getSystemService(Context.SEARCH_SERVICE));
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo((getActivity().getComponentName())));
        searchView.setQueryHint("Search Latest News...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    onLoadingSwipeRefresh(query);
                } else {
                    //Toast.makeText(MainActivity.this, "Type more than two letters!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);


    }


    @Override
    public void onRefresh() {
        LoadJson("");
    }

    private void onLoadingSwipeRefresh(final String keyword){

        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadJson(keyword);
                    }
                }
        );

    }

    private void showErrorMessage(int imageView, String title, String message){

        Log.d("error", "showErrorMessage: ");

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadingSwipeRefresh("");
            }
        });

    }
}
