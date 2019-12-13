package com.example.project_news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class News_Detail extends AppCompatActivity  implements  AppBarLayout.OnOffsetChangedListener{

    private ImageView imageView;
    private TextView app_title,app_subtitle,date,time,tiltle;
    private boolean hideTobarview;
    private FrameLayout frameLayout;
    private LinearLayout linearLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private  String mURL,mImg,mTitle,mDate,mSource,mAuthor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news__detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout= findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        appBarLayout=findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);
        frameLayout.findViewById(R.id.date_behavior);
        linearLayout=findViewById(R.id.title_appbar);
        imageView = findViewById(R.id.backdrop);
        app_title = findViewById(R.id.title_on_appbar);
        app_subtitle = findViewById(R.id.subtitle_on_appbar);
        date = findViewById(R.id.n_date);
        time = findViewById(R.id.n_time);
        tiltle = findViewById(R.id.title);


        Intent intent  = getIntent();

        mURL = intent.getStringExtra("url");
        mImg = intent.getStringExtra("img");
        mTitle= intent.getStringExtra("title");
        mDate = intent.getStringExtra("date");
        mSource = intent.getStringExtra("source");
        mAuthor = intent.getStringExtra("author");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());

        Glide.with(this)
                .load(mImg)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
        app_title.setText(mSource);
        app_subtitle.setText(mURL);
        date.setText(Utils.DateFormat(mDate));
        tiltle.setText(mTitle);

        String author =null;
        if (mAuthor != null || mAuthor != "") {
            mAuthor = " \u2022 " + mAuthor;
        }
        else{
            author ="";

        }
        time.setText(mSource + author +" \u2022 "+Utils.DateToTimeFormat(mDate));
        initview(mURL);


    }
    private  void  initview(String url){
        WebView webView = findViewById(R.id.webView);
        webView .getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);;
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

        int maxscroll = appBarLayout.getTotalScrollRange();
        float percentage=(float) Math.abs(i)/(float) maxscroll;

        if (percentage == 1f && hideTobarview){
            frameLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            hideTobarview = !hideTobarview;
        }
        else if (percentage < 1f && !hideTobarview){
            frameLayout.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            hideTobarview = !hideTobarview;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int  id = item.getItemId();

        if(id == R.id.view_web){
            Intent intent= new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mURL));
            startActivity(intent);
            return true;

        }
        else if(id == R.id.share){
            try {
                Intent intent= new Intent(Intent.ACTION_SEND);
                intent.setType("text/plan");
                intent.putExtra(Intent.EXTRA_SUBJECT,mSource);
                String str= mTitle + "\n" +mURL +"\n"+ "share from the News"+"\n";
                intent.putExtra(Intent.EXTRA_TEXT,str);
                startActivity(intent.createChooser(intent,"share of :"));

            }catch (Exception e){
                Toast.makeText(this,"Sorry ",Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
