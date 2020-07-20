package com.hfad.dawrlygp.views.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.hfad.dawrlygp.databinding.HomeListBinding;
import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.MessageInfo;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.views.PostDetails;
import com.hfad.dawrlygp.R;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends   RecyclerView.Adapter<HomeAdapter.ViewHolderHome>
{
    private Context context;

    private List<Posts> items;

    private ArrayList<Posts> postsInfo = new ArrayList<>() ;


    public HomeAdapter(List<Posts> items, Context context) {
        this.items = items;
        this.context = context;
    }



    @NonNull
    @Override
    public HomeAdapter.ViewHolderHome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                HomeListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.home_list, parent, false);
                return new ViewHolderHome (binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderHome holder, final int position) {

        final Posts posts = items.get(position);


        if (posts.getPostItemsId() == 1){

            holder.binding.nameEdit.setText(posts.getTextBrandNameName() +" " +posts.getTextAgeModelName());

        }
        else if (items.get(position).getPostItemsId() == 2){

            holder.binding.nameEdit.setText("Keys");

        }
        else if (items.get(position).getPostItemsId() == 9){

            holder.binding.nameEdit.setText(posts.getTextBrandNameName() +" " + posts.getTextAgeModelName() +" years old");
        }
        else {
            holder.binding.nameEdit.setText(posts.getTextBrandNameName() );

        }

        if (items.get(position).getTypeId() == 1){
            holder.binding.timeHomeListEdit.setText("Found at "+items.get(position).getTime());
        }

        else {
            holder.binding.timeHomeListEdit.setText("Lost at "+items.get(position).getTime());
        }



        holder.binding.cardViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String PageId = "HomeActivity" ;

                Intent intent = new Intent(context, PostDetails.class);

                postsInfo.add(posts);

                intent.putParcelableArrayListExtra("LIST", postsInfo);

                intent.putExtra("PageId",PageId);

                context.startActivity(intent);

            }
        });

        holder.binding.setPosts(posts);


    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolderHome extends RecyclerView.ViewHolder{


     private HomeListBinding  binding ;

      public ViewHolderHome(@NonNull HomeListBinding  binding) {

          super(binding.getRoot());

          this.binding = binding;

      }
  }



}
