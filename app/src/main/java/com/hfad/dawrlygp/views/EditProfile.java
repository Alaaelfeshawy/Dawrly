package com.hfad.dawrlygp.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.databinding.ActivityEditProfileBinding;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.viewModel.EditProfileViewModel;
import com.hfad.dawrlygp.views.Helpers.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditProfile extends AppCompatActivity {

    private Calendar myCalendar;

    private Toolbar toolbar ;

    private static final String TAG = "EditProfileUser";

    private EditProfileViewModel model ;

    private ActivityEditProfileBinding binding ;

    public static String address ;

    private Uri targetUri ;

    public static  double lat , lon ;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);

        toolbar = findViewById(R.id.edit_profile_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Edit Profile");

        model = ViewModelProviders.of(this).get(EditProfileViewModel.class);

        binding.setEditProfileViewModel(model);

        binding.setLifecycleOwner(this);

        binding.scrollT.setVisibility(View.GONE);

        binding.progressbarEdit.setVisibility(View.VISIBLE);

        model.showData();

        binding.savebtn.setEnabled(false);

        model.setDataSuccess(isSuccess -> {
            if (isSuccess){
                binding.scrollT.setVisibility(View.VISIBLE);
                binding.progressbarEdit.setVisibility(View.GONE);
            }
        });

        binding.swipeEdit.setOnRefreshListener(() -> {
            model.showData();
            setData();
            binding.swipeEdit.postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.swipeEdit.setRefreshing(false);
                }
            },3000);
        });

        setData();

        model.getData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(final User user) {
                binding.progressbarEdit2.setVisibility(View.VISIBLE);
                model.updateData(targetUri , getFileExtension(targetUri));
                model.setCall(new EditProfileViewModel.SuccessCall() {
                    @Override
                    public void onSucessCall(boolean isSuccess) {
                        if (isSuccess){
                            Toast.makeText(EditProfile.this, "Updated Successfully",
                                    Toast.LENGTH_SHORT).show();
                            binding.progressbarEdit2.setVisibility(View.GONE);

                        }else {
                            Toast.makeText(EditProfile.this, "Something wrong happen try again!", Toast.LENGTH_SHORT).show();
                            binding.progressbarEdit2.setVisibility(View.GONE);
                        }
                    }


                });
            }
        });

        binding.firstNameProfile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.savebtn.setEnabled(true);
                binding.savebtn.setBackgroundResource(R.drawable.roundedbutton);

                return false;
            }
        });

        binding.birthDateProfile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.savebtn.setEnabled(true);
                binding.savebtn.setBackgroundResource(R.drawable.roundedbutton);

                return false;
            }
        });

        binding.radioFemaleProfile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.savebtn.setEnabled(true);
                binding.savebtn.setBackgroundResource(R.drawable.roundedbutton);

                return false;
            }
        });

        binding.radioMaleProfile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.savebtn.setEnabled(true);
                binding.savebtn.setBackgroundResource(R.drawable.roundedbutton);

                return false;
            }
        });


        binding.locationProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , Maps.class);
                startActivityForResult(intent,1);
                binding.savebtn.setEnabled(true);
                binding.savebtn.setBackgroundResource(R.drawable.roundedbutton);


            }
        });


        myCalendar = Calendar.getInstance();
        binding.birthDateProfile.setFocusable(false);
        binding.birthDateProfile.setClickable(true);
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

        binding.birthDateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditProfile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                binding.savebtn.setEnabled(true);
                binding.savebtn.setBackgroundResource(R.drawable.roundedbutton);


            }
        });

        model.setImage(new EditProfileViewModel.Image() {
            @Override
            public void onSetImage(final boolean isImageExist) {
                binding.changePp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        binding.savebtn.setEnabled(true);

                        binding.savebtn.setBackgroundResource(R.drawable.roundedbutton);

                        selectImage(v.getContext(),isImageExist);


                    }
                });

            }
        });



    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.birthDateProfile.setText(sdf.format(myCalendar.getTime()));
    }

    private void selectImage(Context context , boolean isImageExist) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");
        if (isImageExist){
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
                        if (ContextCompat.checkSelfPermission(EditProfile.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(EditProfile.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    0);
                        } else {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);
                        }
                    }
                   else if (options[item].equals("Choose from Gallery")) {
                            if (ContextCompat.checkSelfPermission(EditProfile.this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(EditProfile.this,
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
                        final ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
                        progressDialog.setMessage("Deleting..");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        if (model.removeImage()){
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    progressDialog.dismiss();
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
                        if (ContextCompat.checkSelfPermission(EditProfile.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(EditProfile.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    0);
                        } else {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);                        }

                    }

                    else if (options[item].equals("Choose from Gallery")) {
                        if (ContextCompat.checkSelfPermission(EditProfile.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(EditProfile.this,
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
           Log.v("Target",""+targetUri);
       }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        binding.ProfilePic.setImageBitmap(selectedImage);
                        Glide.with(this)
                                .load(selectedImage)
                                .apply(RequestOptions.circleCropTransform())
                                .into( binding.ProfilePic);
                        ConvertFromBitmapToURI(selectedImage);

                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK) {
                        Bundle bundle = data.getExtras();
                        address = bundle.getString(Maps.ADDRESS);
                        binding.locationDetails.setText(address);
                        lat = bundle.getDouble(Maps.LAT);
                        lon = bundle.getDouble(Maps.LON);
                        Log.v("Location","Fragment 1 Lat : "+lat +"\n"+"Long : "+lon);
                    }
                    break;
                case 2:
                    if (resultCode == RESULT_OK && data != null) {
                        targetUri = data.getData();
                        showImage(targetUri);

                    }
                    break;
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

    public void showImage(Uri targetUri){
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(
                    getContentResolver()
                            .openInputStream(targetUri));
            binding.ProfilePic.setImageBitmap(bitmap);
            Glide.with(this)
                    .load(bitmap)
                    .apply(RequestOptions.circleCropTransform())
                    .into( binding.ProfilePic);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setData(){
        model.setData((fname, lname, birthofdate, location, URL, gender) -> {
            binding.firstNameProfile.setText(fname);
            binding.lastNameProfile.setText(lname);
            binding.birthDateProfile.setText(birthofdate);
            binding.locationDetails.setText(location);
            if (gender.equals("Female")){
                binding.radioFemaleProfile.setChecked(true);
            }
            else {
                binding.radioMaleProfile.setChecked(true);
            }
            Log.d(TAG, "getData: "+URL);

                Picasso.get()
                        .load(URL)
                        .placeholder(R.drawable.pp)
                        .transform(new CircleTransformation())
                        .into(binding.ProfilePic);



        });
    }


}
