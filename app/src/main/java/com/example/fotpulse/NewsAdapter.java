package com.example.fotpulse;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsList;
    private Context context;

    public NewsAdapter(List<NewsItem> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsItem newsItem = newsList.get(position);

        holder.title.setText(newsItem.getTitle());
        holder.description.setText(newsItem.getDescription());
        Glide.with(context).load(newsItem.getImageUrl()).into(holder.image);
        holder.newsType.setText(newsItem.getType());

        String formattedTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(newsItem.getTimestamp()));
        holder.timestamp.setText(formattedTimestamp);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FullNewsActivity.class);
            intent.putExtra("newsTitle", newsItem.getTitle());
            intent.putExtra("newsDescription", newsItem.getDescription());
            intent.putExtra("newsImage", newsItem.getImageUrl());
            intent.putExtra("newsType", newsItem.getType());
            intent.putExtra("newsTimestamp", formattedTimestamp);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, newsType, timestamp;
        ImageView image;

        public NewsViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.newsTitle);
            description = itemView.findViewById(R.id.newsDescription);
            newsType = itemView.findViewById(R.id.newsType);
            timestamp = itemView.findViewById(R.id.newsTimestamp);
            image = itemView.findViewById(R.id.newsImage);
        }
    }
}
