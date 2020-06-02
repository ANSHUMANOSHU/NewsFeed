package com.example.nwesfeed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecViewAdapter extends RecyclerView.Adapter<RecViewAdapter.ViewHolder>{
    Context context;
    ArrayList<NewsItem> news =new ArrayList<>();

    public RecViewAdapter() {
    }

    public RecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newsitem,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.webView.setWebViewClient(new WebViewClient());
        holder.webView.getSettings().setJavaScriptEnabled(true);
        holder.webView.getSettings().setAppCacheEnabled(true);
        holder.webView.loadData(news.get(position).getDesc(),"text/html","UTF-8");
        holder.date.setText(news.get(position).getDate());
        Toast.makeText(context, ""+news.get(position).getDate(), Toast.LENGTH_SHORT).show();
        holder.title.setText(news.get(position).getTitle());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO----------------------
                Intent intent =new Intent(context,WebActivity.class);
                intent.putExtra("url",news.get(position).getLink());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,date;
        WebView webView;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.news_linear_view);
            title=itemView.findViewById(R.id.title);
            date=itemView.findViewById(R.id.date);
            webView=itemView.findViewById(R.id.description);
        }
    }

    public void setNews(ArrayList<NewsItem> news) {
        this.news = news;
        notifyDataSetChanged();
        Toast.makeText(context, ""+news.size(), Toast.LENGTH_SHORT).show();
    }
}
