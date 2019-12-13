package com.example.project_news;

import  com.example.project_news.model.news;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_news.api.Apiclient;
import com.example.project_news.api.Apiinterface;
import com.example.project_news.model.Articles;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public  static final String API_key="2ce10eb30f0e4be494723ef0131e7b9e";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Articles> articles=new ArrayList<>();///
    private  Adapter adapter;
    private String TAG = MainActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout errorelativeLayout;
    private  ImageView errorImageView;
    private Button btnButton;
    private TextView errortiltle,errormessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         swipeRefreshLayout= findViewById(R.id.SW_refesh_layput);
         swipeRefreshLayout.setOnRefreshListener(this);
         swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        recyclerView = findViewById(R.id.R_clearview);
        layoutManager= new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator( new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
//        loadjson("");
        onloading("");

        errorelativeLayout = findViewById(R.id.Error);
        errorImageView = findViewById(R.id.errorimg);
        errortiltle = findViewById(R.id.errotiltle);
        errormessage = findViewById(R.id.errormessage);
        btnButton = findViewById(R.id.btn_retry);


    }
    public  void  loadjson(final String Keyword){


      errorelativeLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
        Apiinterface apiinterface= Apiclient.getApiClient().create(Apiinterface.class);
        String country=Utils.getCountry();//++
        String category = Utils.getcategory();


        Call<news> call;
        if(Keyword.length() > 0){
             call = apiinterface.getnewsSearch(Keyword,"publishedAt",API_key);
        }
        else{
            call = apiinterface.getNews(country,category,API_key);
        }
        call.enqueue(new Callback<news>() {
            @Override
            public void onResponse(Call<news> call, Response<news> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {

                    if (!articles.isEmpty()) {
                        articles.clear();
                    }

                    articles = response.body().getArticles();
                    adapter = new Adapter(articles, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                     ClickPush();
                    swipeRefreshLayout.setRefreshing(false);
                }
                else{
                    swipeRefreshLayout.setRefreshing(false);
                    String errors;
                    switch (response.code()){
                        case 404:
                            errors="404 not found";
                            break;
                        case 500:
                            errors ="500 server broket";
                            break;default:
                                errors="Unknow Error";
                                break;

                    }
                    ShoeError(R.drawable.no_result,"No Result","please Try Agian \n "+ errors);
                }
            }

            @Override
            public void onFailure(Call<news> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                ShoeError(R.drawable.no_result,"OOPS....","NETWORK FAILURE ,\n"+ t.toString());
            }
        });
    }

    @Override
    public void onRefresh() {
        loadjson("");
    }
    private  void onloading(final  String keyword){
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        loadjson(keyword);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus,menu);
        SearchManager searchManager =(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView= (SearchView)menu.findItem(R.id.action_search).getActionView();
        MenuItem menuItem= menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search Title...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() >2){
//                    loadjson(query);
                    loadjson(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                loadjson(newText);
                loadjson(newText);
                return false;
            }
        });
        menuItem.getIcon().setVisible(false,false);
        return true;
    }

    private void  ClickPush(){
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int p) {

                ImageView imageView = view.findViewById(R.id.t_img);
                Intent  intent = new Intent(MainActivity.this,News_Detail.class);

                Articles article = articles.get(p);
                intent.putExtra("url",article.getUrl());
                intent.putExtra("title",article.getTitle());
                intent.putExtra("img",article.getUrlToImage());
                intent.putExtra("date",article.getPublishedAt());
                intent.putExtra("source",article.getSource().getName());
                intent.putExtra("author",article.getContent());

                Pair<View,String> pair = Pair.create((View)imageView, ViewCompat.getTransitionName(imageView));
                ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this,
                        pair
                );

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    startActivity(intent,optionsCompat.toBundle());
                }
                else{
                    startActivity(intent);
                }


            }
        });
    }
    private  void ShoeError(int imageView,String title,String messagge){

        if(errorelativeLayout.getVisibility()== View.GONE){
            errorelativeLayout.setVisibility(View.VISIBLE);
        }
        errorImageView.setImageResource(imageView);
        errortiltle.setText(title);
        errormessage.setText(messagge);
        btnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onloading("");
            }
        });
    }
}
