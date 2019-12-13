package com.example.project_news;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.project_news.model.Articles;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHoler> {


      private List<Articles> articles;
      private Context context;
      private OnItemClickListener onItemClickListener;


    public Adapter(List<Articles> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public  MyViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHoler(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHoler holders, int position) {
       final  MyViewHoler holder= holders;
       Articles model = articles.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());///
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(model.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.processBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.processBar.setVisibility(View.GONE);
                return false;
            }
        })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);
        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDescription());
        holder.source.setText(model.getSource().getName());
        holder.time.setText(" \u2022 "+Utils.DateToTimeFormat(model.getPublishedAt()));///
        holder.publishe_AT.setText(Utils.DateFormat(model.getPublishedAt()));
        holder.author.setText( model.getAuthor());

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public  void setOnItemClickListener(OnItemClickListener onItemClickListener){
          this.onItemClickListener=onItemClickListener;
    }


    public  interface OnItemClickListener{
        void onItemClick(View view,int p);
    }


    public class  MyViewHoler extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title,desc,author,publishe_AT,source,time;
        ImageView imageView;
        ProgressBar processBar;
        OnItemClickListener onItemClickListener;

        public MyViewHoler(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(this);
            title=itemView.findViewById(R.id.txt_title);
            desc = itemView.findViewById(R.id.txt_desc);
            author = itemView.findViewById(R.id.txt_author);
            publishe_AT=itemView.findViewById(R.id.txt_publishedAT);
            source = itemView.findViewById(R.id.txt_source);
            time = itemView.findViewById(R.id.txt_time);
            imageView = itemView.findViewById(R.id.t_img);
            processBar = itemView.findViewById(R.id.p_load_photo);

            onItemClickListener=listener;

        }
        @Override
        public void onClick(View v) {
           onItemClickListener.onItemClick(v,getAdapterPosition());
        }
    }

}
