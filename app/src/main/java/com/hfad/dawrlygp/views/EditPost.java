package com.hfad.dawrlygp.views;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.databinding.ActivityEditPostBinding;
import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.ItemPosts;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.viewModel.EditPostViewModel;
import com.hfad.dawrlygp.viewModel.EditProfileViewModel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditPost extends AppCompatActivity {

    private Toolbar toolbar ;

    private Calendar myCalendar;

    public static String address ;

    private Uri targetUri ;

    private String fileEx ;

    public static  double lat , lon ;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private int postItemsId ;

    private EditPostViewModel model ;

    private ActivityEditPostBinding binding ;

    private String id, postId ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_post);

        toolbar = findViewById(R.id.editPostToolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Edit Post");

        model = ViewModelProviders.of(EditPost.this).get(EditPostViewModel.class);

        binding.setLifecycleOwner(this);

        binding.setEdit(model);

        myCalendar = Calendar.getInstance();

        setData();

        model.getData().observe(this, new Observer<Posts>() {
            @Override
            public void onChanged(Posts posts) {

                model.updateData(targetUri,postItemsId,postId ,fileEx);
                model.setCall(new EditPostViewModel.SuccessCall() {
                    @Override
                    public void onSuccessCall(boolean isSuccess) {
                        if (isSuccess){
                            Toast.makeText(EditPost.this,"Records was edited successfully",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });
            }
        });

        binding.dateEditPost.setFocusable(false);

        binding.dateEditPost.setClickable(true);

        binding.loctationEdit.setFocusable(false);

        binding.loctationEdit.setClickable(true);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        binding.dateEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditPost.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                binding.save.setEnabled(true);
                binding.save.setBackgroundResource(R.drawable.roundedbutton);
            }
        });

        binding.loctationEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , Maps.class);
                startActivityForResult(intent,1);
                binding.save.setEnabled(true);
                binding.save.setBackgroundResource(R.drawable.roundedbutton);

            }
        });

        model.setImage(new EditPostViewModel.Image() {
            @Override
            public void onSetImage(final boolean isImageExist) {
               binding.uploadImage.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       binding.save.setEnabled(true);

                       binding.save.setBackgroundResource(R.drawable.roundedbutton);

                       Log.v("EditPostActivity",""+isImageExist);

                       selectImage(v.getContext(),isImageExist);
                   }
               });

            }
        });

        binding.radioLostCheck.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.save.setEnabled(true);
                binding.save.setBackgroundResource(R.drawable.roundedbutton);

                return false;
            }
        });

        binding.radioFoundCheck.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.save.setEnabled(true);
                binding.save.setBackgroundResource(R.drawable.roundedbutton);

                return false;
            }
        });
        binding.radioFemaleProfile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.save.setEnabled(true);
                binding.save.setBackgroundResource(R.drawable.roundedbutton);

                return false;
            }
        });

        binding.radioMaleProfile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.save.setEnabled(true);
                binding.save.setBackgroundResource(R.drawable.roundedbutton);

                return false;
            }
        });
        binding.nameBrandNameEditPost.setOnTouchListener((v, event) -> {
            binding.save.setEnabled(true);
            binding.save.setBackgroundResource(R.drawable.roundedbutton);
            return false;
        });

        binding.ageModelNameEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.save.setEnabled(true);
                binding.save.setBackgroundResource(R.drawable.roundedbutton);
                return false;
            }
        });

        binding.genderColorEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.save.setEnabled(true);
                binding.save.setBackgroundResource(R.drawable.roundedbutton);
                return false;
            }
        });

        binding.descEditPost.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.save.setEnabled(true);
                binding.save.setBackgroundResource(R.drawable.roundedbutton);
                return false;
            }
        });



    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.dateEditPost.setText(sdf.format(myCalendar.getTime()));
    }

    private void selectImage(Context context , boolean isImageExist) {

        binding.save.setEnabled(true);

        binding.save.setBackgroundResource(R.drawable.roundedbutton);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");
        if (!isImageExist){
            final CharSequence[] options =
                    {
                            "Take Photo",
                            "Choose from Gallery",
                            "Remove Photo",
                            "Cancel",

                    };
            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(final DialogInterface dialog, int item) {

                    if (options[item].equals("Take Photo")) {
                        if (ContextCompat.checkSelfPermission(EditPost.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(EditPost.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    0);
                        } else {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);
                        }
                    }
                    else if (options[item].equals("Choose from Gallery")) {
                        if (ContextCompat.checkSelfPermission(EditPost.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(EditPost.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    2);
                        } else {
                            Intent pickPhoto = new Intent();
                            pickPhoto.setType("image/*");
                            pickPhoto.setAction(Intent.ACTION_PICK);
                            startActivityForResult(pickPhoto, 2);
                        }

                    }
                    else if (options[item].equals("Remove Photo")) {
                        Log.v("Click","Click");
                        final ProgressDialog progressDialog = new ProgressDialog(EditPost.this);
                        progressDialog.setMessage("Deleting..");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        if (model.removeImage(postItemsId,postId)){
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {

                                    progressDialog.dismiss();
                                    finish();
                                }
                            }, 3000);
                        }

                    }else {
                        dialog.dismiss();
                    }
                }
            });
        }
        else {
            final CharSequence[] options =
                    {
                            "Take Photo",
                            "Choose from Gallery",
                            "Cancel",

                    };
            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(final DialogInterface dialog, int item) {

                    if (options[item].equals("Take Photo")) {
                        if (ContextCompat.checkSelfPermission(EditPost.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(EditPost.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    0);
                        } else {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);                        }

                    }

                    else if (options[item].equals("Choose from Gallery")) {
                        if (ContextCompat.checkSelfPermission(EditPost.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(EditPost.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    2);
                        } else {
                            Intent pickPhoto = new Intent();
                            pickPhoto.setType("image/*");
                            pickPhoto.setAction(Intent.ACTION_PICK);
                            startActivityForResult(pickPhoto, 2);
                        }

                    }

                    else {
                        dialog.dismiss();
                    }
                }
            });

        }
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        binding.imageEditPost.setImageBitmap(selectedImage);
                        ConvertFromBitmapToURI(selectedImage);
                        binding.imageEditPost.setVisibility(View.VISIBLE);
                        Glide.with(this)
                                .load(selectedImage)
                                .into(binding.imageEditPost);

                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK) {
                        Bundle bundle = data.getExtras();
                        address = bundle.getString(Maps.ADDRESS);
                        binding.loctationEdit.setText(address);
                        lat = bundle.getDouble(Maps.LAT);
                        lon = bundle.getDouble(Maps.LON);
                        Log.v("Location","Fragment 1 Lat : "+lat +"\n"+"Long : "+lon);
                    }
                    break;
                case 2:
                    if (resultCode == RESULT_OK && data != null) {
                        targetUri = data.getData();
                        fileEx = getFileExtension(targetUri);
                        Log.v("EditPostActivity", "url is " + targetUri);
                        showImage(targetUri);

                    }
                    break;
            }


        }

    }

    public void ConvertFromBitmapToURI(Bitmap selectedImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            String path = MediaStore.Images.Media.insertImage(getBaseContext().getContentResolver(),
                    selectedImage, "Title", null);
            targetUri = Uri.parse(path);
            fileEx = getFileExtension(targetUri);
            Log.v("Target",""+targetUri);
        }

    }

    public void showImage(Uri targetUri){
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(
                    getContentResolver()
                            .openInputStream(targetUri));
            binding.imageEditPost.setImageBitmap(bitmap);
            binding.imageEditPost.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(bitmap)
                    .into(binding.imageEditPost);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setData() {

        final Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            id = bundle.getString("PageId");
            postId = bundle.getString("postId");
            postItemsId = bundle.getInt("postItemsId");
            if (id.equals("Save")) {
                if (postItemsId >=1 && postItemsId <=8) {
                    model.showData(postItemsId, postId);
                    model.setDataItems(new EditPostViewModel.DataItems() {
                        @Override
                        public void getData(ItemPosts itemPosts) {
                            if (itemPosts.getTitle() == null){
                                binding.nameBrandNameTitleEdit.setVisibility(View.GONE);
                                binding.nameBrandNameEditPost.setVisibility(View.GONE);
                            }
                            if (itemPosts.getColor() !=null){
                                binding.genderColorTitle.setVisibility(View.VISIBLE);
                                binding.genderColorEdit.setVisibility(View.VISIBLE);
                                binding.genderColorTitle.setText("Color");
                                binding.genderColorEdit.setText(itemPosts.getColor());


                            }
                            if (itemPosts.getModelName() !=null){
                                binding.ageModelNameTitle.setVisibility(View.VISIBLE);
                                binding.ageModelNameEdit.setVisibility(View.VISIBLE);
                                binding.ageModelNameTitle.setText("Model name");
                                binding.ageModelNameEdit.setText(itemPosts.getModelName());


                            }
                            binding.nameBrandNameEditPost.setText(itemPosts.getTitle());
                            binding.dateEditPost.setText(itemPosts.getTime());
                            binding.descEditPost.setText(itemPosts.getDescription());
                            binding.loctationEdit.setText(itemPosts.getLocation());
                            if (itemPosts.getTypeId() == 1) {
                                binding.radioFoundCheck.setChecked(true);

                            } else {
                                binding.radioLostCheck.setChecked(true);
                            }
                            if (itemPosts.getImageUrl() == null || itemPosts.getImageUrl().equals("null") ) {
                                binding.imageEditPost.setVisibility(View.GONE);
                            }
                            else {
                                Picasso.get()
                                        .load(itemPosts.getImageUrl())
                                        .placeholder(R.drawable.noimage)
                                        .into(binding.imageEditPost);                            }

                        }
                    });

                }

                else if (postItemsId == 9) {
                    model.showData(postItemsId, postId);
                    model.setDataHuman(new EditPostViewModel.DataHuman() {
                        @Override
                        public void getData(HumanPosts humanPosts) {
                            binding.genderColorTitle.setVisibility(View.VISIBLE);
                            binding.radioSexProfile.setVisibility(View.VISIBLE);
                            binding.ageModelNameTitle.setVisibility(View.VISIBLE);
                            binding.ageModelNameEdit.setVisibility(View.VISIBLE);
                            binding.nameBrandNameEditPost.setText(humanPosts.getName());
                            binding.ageModelNameEdit.setText(humanPosts.getAge()+"");
                            if (humanPosts.getGender().equals("Female")){
                                binding.radioFemaleProfile.setChecked(true);
                            }
                            else {
                                binding.radioMaleProfile.setChecked(true);
                            }
                            binding.dateEditPost.setText(humanPosts.getTime());
                            binding.descEditPost.setText(humanPosts.getDescription());
                            binding.loctationEdit.setText(humanPosts.getLocation());
                            if (humanPosts.getTypeId() == 1) {
                                binding.radioFoundCheck.setChecked(true);

                            } else {
                                binding.radioLostCheck.setChecked(true);
                            }
                            if (humanPosts.getImageUrl() == null || humanPosts.getImageUrl().equals("null") ) {
                                binding.imageEditPost.setVisibility(View.GONE);
                            } else {
                                Picasso.get()
                                        .load(humanPosts.getImageUrl())
                                        .placeholder(R.drawable.noimage)
                                        .into(binding.imageEditPost);
                            }

                        }
                    });


                }
            }


        }

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

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}
