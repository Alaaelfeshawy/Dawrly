package com.hfad.dawrlygp.views.Adapters;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hfad.dawrlygp.databinding.MyPostsListBinding;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.viewModel.AccountViewModel;
import com.hfad.dawrlygp.viewModel.SavedViewModel;
import com.hfad.dawrlygp.views.EditPost;
import com.hfad.dawrlygp.views.EditProfile;
import com.hfad.dawrlygp.views.Fragments.UserFragment.AccountFragment;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.views.Helpers.ReadMoreOption;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyPostsAdapter  extends RecyclerView.Adapter<MyPostsAdapter.ViewHolder>
{

    private List<Posts> postsList ;

    private Context context;

    private ReadMoreOption readMoreOption;

    private  String id  ;

    private SavedViewModel savedViewModel;

    private AccountViewModel accountViewModel ;

    public MyPostsAdapter(List<Posts> postsList, Context context , String id , AccountViewModel model) {
        this.postsList = postsList;
        this.context = context;
        readMoreOption = new ReadMoreOption.Builder(context).build();
        this.id = id;
        accountViewModel = model;

    }

    public MyPostsAdapter(List<Posts> postsList, Context context , String id , SavedViewModel model) {
        this.postsList = postsList;
        this.context = context;
        readMoreOption = new ReadMoreOption.Builder(context).build();
        this.id = id;
        this.savedViewModel = model;

    }



    @NonNull
    @Override
    public MyPostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        MyPostsListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.my_posts_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {

        if (id.equals(AccountFragment.ID)) {
            getData(position , holder);
            holder.binding.menuIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    accountViewModel.checkFound(postsList.get(position).getPostId());
                    accountViewModel.setSuccessCall(new AccountViewModel.Success() {
                        @Override
                        public void setSuccess(boolean isSuccess) {
                            if (isSuccess){
                                PopupMenu popup = new PopupMenu(view.getContext(),view);
                                popup.getMenuInflater().inflate(R.menu.my_posts_menu2, popup.getMenu());
                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                                     @Override
                                                                     public boolean onMenuItemClick(MenuItem item) {
                                                                         switch (item.getItemId()) {
                                                                             case R.id.delete_my_posts:
                                                                                 delete(position,id);
                                                                                 break;
                                                                             case R.id.edit:
                                                                                 editPost(position);
                                                                                 break;


                                                                         }
                                                                         return false;
                                                                     }
                                                                 }
                                );
                                popup.show();

                            }
                            else {
                                PopupMenu popup = new PopupMenu(view.getContext(),view);
                                popup.getMenuInflater().inflate(R.menu.my_posts_menu, popup.getMenu());
                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                                     @Override
                                                                     public boolean onMenuItemClick(MenuItem item) {
                                                                         switch (item.getItemId())
                                                                         {
                                                                             case R.id.delete_my_posts:
                                                                                 delete(position,id);
                                                                                 break;
                                                                             case R.id.found:
                                                                                 found(position,id);
                                                                                 break;
                                                                             case R.id.edit:
                                                                                 editPost(position);
                                                                                 break;


                                                                         }
                                                                         return false;
                                                                     }
                                                                 }
                                );
                                popup.show();

                            }
                        }
                    });
                }
            });
        }

        else {
            getData(position,holder);
            holder.binding.postNow.setVisibility(View.VISIBLE);
            holder.binding.postNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    savedViewModel.postNow(postsList.get(position).getPostId(),postsList.get(position).getPostItemsId());
                    savedViewModel.setSuccess(new SavedViewModel.Success() {
                        @Override
                        public void onSuccess(boolean isSuccess) {
                            if (isSuccess){
                                Toast.makeText(v.getContext(),"Request sent to admin successfully",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
            holder.binding.menuIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    PopupMenu  popup = new PopupMenu(view.getContext(),view);
                    popup.inflate(R.menu.saved_menu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.delete_save:
                                    delete(position,id);
                                    break;
                                case R.id.edit_save:
                                     editPost(position);
                                case R.id.edit:
                                    break;

                            }
                            return false;
                        }
                    });
                    popup.show();

                }
            });

        }



        }

      private AlertDialog delete (final int position , final String id){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle("");
        builder.setMessage("Are you sure for deleting this post ?");
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                removeItem(position , id);

            }
        });
        builder.setNeutralButton("Undo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
       return dialog ;
    }

      private AlertDialog found (final int position , final String id){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("We are glad to find your losts ");

        builder.setMessage("Delete this post from your account ?");

        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeItem(position , id);

            }
        });
        builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                accountViewModel.found(postsList.get(position).getPostId());

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog ;
    }

    private void editPost(int i){
        Intent intent = new Intent(context,EditPost.class);
        String PageId = "Save" ;
        Bundle bundle = new Bundle();
        bundle.putString("PageId",PageId);
        bundle.putString("postId",postsList.get(i).getPostId());
        bundle.putInt("postItemsId",postsList.get(i).getPostItemsId());
        intent.putExtras(bundle);
        context.startActivity(intent);;

    }

    @Override

    public int getItemCount() {
        return  postsList.size();

    }

    private void removeItem(int position , String id) {
        if (id.equals(AccountFragment.ID)) {
            Log.v("MyPostsAdapter",""+postsList.get(position).getPostItemsId()+" "
                    + postsList.get(position).getPostId());
            String postId = postsList.get(position).getPostId();
            int postItemsId = postsList.get(position).getPostItemsId();
            accountViewModel.delete(postId , postItemsId);
            Toast.makeText(context,"Post is deleted successfully", Toast.LENGTH_LONG).show();
        }
        else
        {
            Log.v("MyPostsAdapter",""+postsList.get(position).getPostItemsId()+" "
                    + postsList.get(position).getPostId());
            String postId = postsList.get(position).getPostId();
            int postItemsId = postsList.get(position).getPostItemsId();
            savedViewModel.delete(postId , postItemsId);
            Toast.makeText(context,"Post is deleted successfully",
                    Toast.LENGTH_LONG).show();

        }

        notifyItemRemoved(position);
    }

    private void getData(int position , ViewHolder holder){

        Posts posts = postsList.get(position);

        if (position % 2 == 0) {
            readMoreOption.addReadMoreTo(holder.binding.myPostDesc,
                    Html.fromHtml(posts.getDecription()));
        }
        else {
            readMoreOption.addReadMoreTo(holder.binding.myPostDesc,
                    Html.fromHtml(posts.getDecription()));
        }
        if (posts.getTypeId()==1){
            holder.binding.myPostType.setText("Found");

        }
        else {
            holder.binding.myPostType.setText("Lost");

        }


        if (posts.getPostItemsId()>=1 && posts.getPostItemsId() <=8) {

            if (posts.getPostItemsId() ==1){

                if (posts.getTextAgeModelName() != null ){
                    holder.binding.ageTitle.setVisibility(View.VISIBLE);
                    holder. binding.ageTitle.setText("Model name : ");
                    holder.binding.myPostAge.setVisibility(View.VISIBLE);
                }
            }

            if (posts.getTextBrandNameName() != null){
                holder. binding.nameTitle.setVisibility(View.VISIBLE);
                holder. binding.nameTitle.setText("Title : ");
                holder.binding.myPostName.setVisibility(View.VISIBLE);

            }

            if (posts.getTextColorGender() != null){
                holder. binding.genderTitle.setVisibility(View.VISIBLE);
                holder. binding.genderTitle.setText("Color : ");
                holder. binding.myPostGender.setVisibility(View.VISIBLE);

            }
        }

        else {
            holder.binding.genderTitle.setText("Gender : ");
            holder.binding.ageTitle.setVisibility(View.VISIBLE);
            holder.binding.myPostAge.setVisibility(View.VISIBLE);
            holder.binding.nameTitle.setVisibility(View.VISIBLE);
            holder.binding.myPostName.setVisibility(View.VISIBLE);
            holder.binding.genderTitle.setVisibility(View.VISIBLE);
            holder.binding.myPostGender.setVisibility(View.VISIBLE);
        }

        holder.binding.setPosts(posts);




    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private MyPostsListBinding binding;

        public ViewHolder(@NonNull MyPostsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}