package com.hfad.dawrlygp.views.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.databinding.NotificationListBinding;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.views.Helpers.CircleTransformation;
import com.hfad.dawrlygp.views.PostDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Posts> names ;

    private Context context;

    private ArrayList<Posts> notificationPosts = new ArrayList<>() ;


    public NotificationAdapter(List<Posts> names, Context context) {
        this.names = names;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {

        NotificationListBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.notification_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(binding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final Posts posts = names.get(position);

        if (posts.getPostTypeId().equals("confirm")){

         if (posts.isStatus()){

             holder.binding.notificationIconInNotify.setVisibility(View.GONE);
         }
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.adminicon);

            Glide.with(context)
                    .load(drawable)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.binding.imageNotificationList);

            holder.binding.firstNameNotificationList.setText("Admin has approved your request to add this post");

            holder.binding.cardViewNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String PageId = "Home" ;

                    Intent intent = new Intent(context, PostDetails.class);

                    Bundle bundle = new Bundle();

                    bundle.putString("PageId",PageId);

                    notificationPosts.add(posts);

                    intent.putParcelableArrayListExtra("LIST", notificationPosts);

                    intent.putExtra("PageId",PageId);

                    context.startActivity(intent);
                   }
            });
        }

        if (posts.getPostTypeId().equals("similarity")){

            holder.binding.notificationIconInNotify.setVisibility(View.GONE);

            Picasso.get()
                    .load(posts.getUserImageUrl())
                    .placeholder(R.drawable.pp)
                    .transform(new CircleTransformation())
                    .into(holder.binding.imageNotificationList);

            holder.binding.firstNameNotificationList.setText(posts.getFname()+" "+ posts.getLname());
            final int similarity = (int) (posts.getSimilarity()*100);
            holder.binding.lastNameNotificationList.setText("has similarity with your post "+similarity+"%");

            holder.binding.cardViewNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String PageId = "SimilarityActivity" ;

                    Intent intent = new Intent(context, PostDetails.class);

                    Bundle bundle = new Bundle();

                    bundle.putString("PageId",PageId);

                    notificationPosts.add(posts);

                    intent.putParcelableArrayListExtra("LIST", notificationPosts);

                    intent.putExtra("PageId",PageId);

                    context.startActivity(intent);
                }
            });
        }




    }



    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

         private NotificationListBinding binding ;

        public ViewHolder(@NonNull NotificationListBinding binding ) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}