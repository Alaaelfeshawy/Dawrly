package com.hfad.dawrlygp.views.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.databinding.RequestListBinding;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.views.PostDetails;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<Posts> requests ;

    private Context context ;

    private ArrayList<Posts> requestPosts = new ArrayList<>();

    private int id ;



    public RequestAdapter(Context context ,List<Posts> requests , int id) {
        this.requests = requests;
        this.context = context;
        this.id = id ;
    }

    @NonNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RequestListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.request_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(binding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final Posts posts = requests.get(position);

        holder.binding.setPosts(posts);


        if (id == 1){
            holder.binding.textViewList.setText("'s post requested to report");

        }
        else {
            holder.binding.textViewList.setText("'s post requested to post");
        }
        if (posts.isStatus()){
            holder.binding.notificationIcon.setVisibility(View.GONE);
        }

        holder.binding.cardViewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String PageId = "Request" ;

                Intent intent = new Intent(context, PostDetails.class);

                requestPosts.add(posts);

                intent.putParcelableArrayListExtra("LIST", requestPosts);

                intent.putExtra("PageId",PageId);

                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RequestListBinding binding;

        public ViewHolder(@NonNull RequestListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
