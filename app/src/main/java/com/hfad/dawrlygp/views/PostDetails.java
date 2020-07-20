package com.hfad.dawrlygp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.databinding.ActivityPostDetailsBinding;
import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.Message;
import com.hfad.dawrlygp.model.MessageInfo;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.viewModel.AccountViewModel;
import com.hfad.dawrlygp.viewModel.PostsDetailsViewModel;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PostDetails extends AppCompatActivity implements View.OnClickListener {

    private ActivityPostDetailsBinding binding;

    private PostsDetailsViewModel model;

    private Posts posts ;

    private String currentUserId ;

    private ArrayList<MessageInfo> messageInfos = new ArrayList<>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_details);

        model = ViewModelProviders.of(this).get(PostsDetailsViewModel.class);

        Toolbar toolbar = findViewById(R.id.posts_details_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Activity");

        ArrayList<Posts> list = getIntent().getParcelableArrayListExtra("LIST");

        String pageId = getIntent().getStringExtra("PageId");

        currentUserId = model.userId();

        posts = list.get(0);

        binding.setPosts(posts);

        if (pageId != null) {

            if (posts.getTypeId() == 1) {

                binding.myPostType.setText("Found");

            } else {

                binding.myPostType.setText("Lost");
            }

            if (posts.getPostItemsId() >= 1 && posts.getPostItemsId() <= 8) {

                if (posts.getTextAgeModelName() != null) {
                    binding.agetitle.setVisibility(View.VISIBLE);
                    binding.agetitle.setText("Model name");
                    binding.myPostAge.setVisibility(View.VISIBLE);
                    binding.view1.setVisibility(View.VISIBLE);
                }
                if (posts.getTextBrandNameName() != null) {
                    binding.nameTitle.setVisibility(View.VISIBLE);
                    binding.nameTitle.setText("Title");
                    binding.myPostName.setVisibility(View.VISIBLE);
                    binding.view2.setVisibility(View.VISIBLE);

                }
                if (posts.getTextColorGender() != null) {
                    binding.genderTitle.setVisibility(View.VISIBLE);
                    binding.genderTitle.setText("Color");
                    binding.myPostGender.setVisibility(View.VISIBLE);
                    binding.view3.setVisibility(View.VISIBLE);

                }

            } else {
                binding.agetitle.setVisibility(View.VISIBLE);
                binding.myPostAge.setVisibility(View.VISIBLE);
                binding.view1.setVisibility(View.VISIBLE);
                binding.nameTitle.setVisibility(View.VISIBLE);
                binding.myPostName.setVisibility(View.VISIBLE);
                binding.view2.setVisibility(View.VISIBLE);
                binding.genderTitle.setVisibility(View.VISIBLE);
                binding.myPostGender.setVisibility(View.VISIBLE);
                binding.view3.setVisibility(View.VISIBLE);
            }

            if (pageId.equals("Request")) {

                binding.deletePost.setOnClickListener(this);

                binding.confirmPost.setOnClickListener(this);

                model.seen(posts.getPostId());

            }

            if (pageId.equals("Home")) {

                binding.deletePost.setVisibility(View.GONE);

                binding.confirmPost.setVisibility(View.GONE);

                model.seenHome(posts.getPostId());


            }

            if (pageId.equals("HomeActivity")) {

                binding.deletePost.setVisibility(View.GONE);

                binding.confirmPost.setVisibility(View.GONE);

                binding.messageDetails.setVisibility(View.VISIBLE);

                binding.contactWithMobile.setVisibility(View.VISIBLE);

                binding.messageDetails.setOnClickListener(this);

                binding.contactWithMobile.setOnClickListener(this);

                binding.menuIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        PopupMenu popup = new PopupMenu(PostDetails.this,view);
                        popup.getMenuInflater().inflate(R.menu.report, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                             @Override
                                                             public boolean onMenuItemClick(MenuItem item) {
                                                                 switch (item.getItemId()) {
                                                                     case R.id.report:
                                                                         reportPost(posts.getPostItemsId(),
                                                                                 posts.getPostId());
                                                                         break;


                                                                 }
                                                                 return false;
                                                             }
                                                         }
                        );
                        popup.show();

                    }

                });
            }

            if (pageId.equals("SimilarityActivity")) {

                binding.deletePost.setVisibility(View.GONE);

                binding.confirmPost.setVisibility(View.GONE);

                binding.messageDetails.setVisibility(View.VISIBLE);

                binding.contactWithMobile.setVisibility(View.VISIBLE);

                binding.messageDetails.setOnClickListener(this);

                binding.contactWithMobile.setOnClickListener(this);

                binding.menuIcon.setOnClickListener(view -> {
                    PopupMenu popup = new PopupMenu(PostDetails.this,view);
                    popup.getMenuInflater().inflate(R.menu.report, popup.getMenu());
                    popup.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.report:
                                reportPost(posts.getPostItemsId(),
                                        posts.getPostId());
                                break;


                        }
                        return false;
                    }
                    );
                    popup.show();

                });

               model.seenSimilarity(posts.getPostId());
            }
        }
    }

    private void reportPost(int postItemsId , String postId){
        model.reportPost(postItemsId,postId);
        model.setSuccessReport(new PostsDetailsViewModel.SuccessReport() {
            @Override
            public void setSuccess(boolean isSuccess) {
                if (isSuccess){
                    Toast.makeText(PostDetails.this,"Post is reported successfully",
                            Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(PostDetails.this,"You already report this post before",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        model.setReportUser(new PostsDetailsViewModel.ReportUser() {
            @Override
            public void setUserReport(boolean isSuccess) {
                if (! isSuccess){
                    Toast.makeText(PostDetails.this,"You can't report yourself",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.deletePost:
               deletePost();
                break;
            case R.id.confirmPost:
                confirmPost();
                break;
            case R.id.messageDetails:
                messageDetails();
                break;
            case R.id.contactWithMobile:
                connectWithMobile();
                break;


        }
    }

    private void deletePost(){
        binding.postsProgress.setVisibility(View.VISIBLE);
        model.delete(posts.getPostId(),posts.getPostItemsId());
        model.setSuccess(new PostsDetailsViewModel.Success() {
            @Override
            public void onSuccess(boolean isSuccess) {
                if (isSuccess){
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.postsProgress.setVisibility(View.GONE);
                            Toast.makeText(PostDetails.this,"Request is deleted " +
                                    "successfully",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    },3000);
                }
            }
        });
    }

    private void confirmPost(){
        binding.postsProgress.setVisibility(View.VISIBLE);
        model.confirm(posts.getPostId(),posts.getPostItemsId());
        model.setConfirmSuccess(new PostsDetailsViewModel.ConfirmSuccess() {
            @Override
            public void confirmSuccess(boolean isSuccess) {
                if (isSuccess){
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.postsProgress.setVisibility(View.GONE);
                            Toast.makeText(PostDetails.this,"Request is Approved successfully"
                                    ,Toast.LENGTH_LONG).show();
                            finish();
                        }
                    },3000);


                }
            }
        });
    }

    private void messageDetails(){

        if (!currentUserId.equals(posts.getUserId())) {

            final Intent intent = new Intent(PostDetails.this, ChatView.class);

            MessageInfo message = new MessageInfo();

            message.setFname(posts.getFname());

            message.setLname(posts.getLname());

            message.setImage(posts.getUserImageUrl());

            message.setReceiverId(posts.getUserId());

            messageInfos.add(message);

            intent.putParcelableArrayListExtra("LIST", messageInfos);

            intent.putExtra("currentUserId",currentUserId);

            startActivity(intent);
        }

    }

    private void connectWithMobile() {

        if (!currentUserId.equals(posts.getUserId())) {

            if (posts.getCommunicationByNumber().equals("true")) {

                Intent i = new Intent(Intent.ACTION_DIAL);

                i.setData(Uri.parse("tel:" + posts.getUserNumber()));

                startActivity(i);
            } else {
                Toast.makeText(PostDetails.this, "Sorry number isn't available", Toast.LENGTH_LONG).show();

            }


        }
    }
}
