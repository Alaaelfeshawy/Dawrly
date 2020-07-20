package com.hfad.dawrlygp.views.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import com.hfad.dawrlygp.databinding.ChatListBinding;
import com.hfad.dawrlygp.model.MessageInfo;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.viewModel.ChatViewModel;
import com.hfad.dawrlygp.views.ChatView;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.views.PostDetails;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends  RecyclerView.Adapter<ChatAdapter.ViewHolder>
{
    private List<MessageInfo> names , filter;

    private Context context;

    private ChatViewModel model ;

    private ArrayList<MessageInfo> messageInfos = new ArrayList<>() ;

    public ChatAdapter(List<MessageInfo> names, Context context , ChatViewModel model) {
        this.names = names;
        this.filter = names;
        this.context = context;
        this.model = model ;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        ChatListBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.chat_list, parent, false);

        return new ViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {
        final String currentUserId = model.userId();

        MessageInfo info = names.get(position);

        holder.binding.setChat(info);

        if (names.get(position).isSeen()){

            holder.binding.notificationIconInNotifyChat.setVisibility(View.GONE);
        }

        if (!names.get(position).getSenderId().equals(currentUserId)){

            holder.binding.titleChatList.setText(names.get(position).getLastMessage());

        }
        else {
            holder.binding.titleChatList.setText("you: "+names.get(position).getLastMessage());

        }

        holder.binding.cardViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                model.seen(names.get(position).getReceiverId(),currentUserId);

                if (!currentUserId.equals(names.get(position).getReceiverId())) {

                    final Intent intent = new Intent(view.getContext() , ChatView.class);

                    messageInfos.add(names.get(position));

                    intent.putParcelableArrayListExtra("LIST", messageInfos);

                    intent.putExtra("currentUserId",currentUserId);

                    context.startActivity(intent);
                }


            }
        });



    }


    @Override
    public int getItemCount() {
        return names.size();
    }


    public Context getContext(){
        return context;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ChatListBinding binding ;

        public ViewHolder( ChatListBinding binding) {

            super(binding.getRoot());

            this.binding = binding;



        }
    }

    public void filter(String text) {
        if(text.isEmpty()){
            names = filter;
        } else{
            List<MessageInfo> filteredList = new ArrayList<>();
            text = text.toLowerCase().trim();
            for(MessageInfo names: filter){
                if(names.getFname().toLowerCase().contains(text)
                        || names.getLname().toLowerCase().contains(text)){
                    // items.clear();
                    filteredList.add(names);
                }
            }
            names = filteredList ;
        }
        notifyDataSetChanged();
    }




}
