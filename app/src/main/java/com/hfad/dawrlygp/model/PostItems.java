package com.hfad.dawrlygp.model;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hfad.dawrlygp.R;
import com.squareup.picasso.Picasso;

public class PostItems {

   private String imageUrl ;
   private String title ;
   private int postsitemsId ;

    public int getPostsitemsId() {
        return postsitemsId;
    }

    public void setPostsitemsId(int postsitemsId) {
        this.postsitemsId = postsitemsId;
    }

    public PostItems()
    {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @BindingAdapter({"android:image"})
    public static void loadImage(final ImageView view, String image) {

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl("gs://dawrly-12bd7.appspot.com/");
        StorageReference reference = ref.child("PostItems/"+image);
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri uri) {

                Log.v("URL","Uri "+uri.toString());

                Picasso.get()
                        .load(uri)
                        .into(view);

            }
        });


    }
}
