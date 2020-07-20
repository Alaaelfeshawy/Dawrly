package com.hfad.dawrlygp.views.Fragments.DialogFragments.Dialogs;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hfad.dawrlygp.databinding.DialogHumanBinding;
import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.ItemPosts;
import com.hfad.dawrlygp.viewModel.HumanDialogViewModel;
import com.hfad.dawrlygp.views.Fragments.DialogFragments.ConfirmationDialogs.ConfirmationHumanDialogFragment;
import com.hfad.dawrlygp.views.Fragments.DialogFragments.ConfirmationDialogs.ConfirmationItemDialogFragment;
import com.hfad.dawrlygp.views.Fragments.DialogFragments.MainDialogFragment;
import com.hfad.dawrlygp.views.Maps;
import com.hfad.dawrlygp.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class HumanDialogFragment extends DialogFragment {

    public static String TITLE = "Human", LAT ="lat",LON="lon";

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private Calendar myCalendar;

    private  FragmentManager fragmentManager ;

    private DialogFragment newFragment ;

    public static String address , city ;

    private static double lat , lon ;

    private HumanDialogViewModel model ;

    private DialogHumanBinding binding ;

    private Bitmap selectedImage ;

    public  HumanDialogFragment (){}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        model = ViewModelProviders.of(this).get(HumanDialogViewModel.class);
        binding = DataBindingUtil.inflate(LayoutInflater.
                from(getContext()),R.layout.dialog_human,null,false);
        binding.setHuman(model);
        binding.setLifecycleOwner(this);
        fragmentManager = getFragmentManager();
        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newFragment = new MainDialogFragment();
                newFragment.show(fragmentManager,MainDialogFragment.Title);
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity() ,
                android.R.style.Theme_Material_NoActionBar_Fullscreen);
        builder.setTitle(TITLE);
        builder.setView(binding.getRoot());
        model.getData().observe(this, new Observer<HumanPosts>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChanged(HumanPosts posts) {
                posts.setCity(city);
                posts.setPostItemsId(9);
                if (TextUtils.isEmpty(Objects.requireNonNull(posts).getName())) {
                    binding.namePostHuman.setError("Enter the name");
                    binding.namePostHuman.requestFocus();
                }
                else if (binding.agePostHuman.getText().toString().isEmpty()){
                    binding.agePostHuman.setError("Enter the age");
                    binding.agePostHuman.requestFocus();
                }
                else if (TextUtils.isEmpty(Objects.requireNonNull(posts).getGender())){
                    binding.errorGender.setVisibility(View.VISIBLE);
                    binding.errorGender.setText("Enter Gender");
                }
                else if (TextUtils.isEmpty(Objects.requireNonNull(posts).getDescription())){
                    binding.errorGender.setText(null);
                    binding.descPostHuman.setError("Enter Description");
                    binding.descPostHuman.requestFocus();
                }
                else if (TextUtils.isEmpty(Objects.requireNonNull(posts).getLocation())){
                    binding.postLocationHuman.setError("Enter Location");
                    binding.postLocationHuman.requestFocus();
                }
                else if (TextUtils.isEmpty(Objects.requireNonNull(posts).getTime())){
                    binding.postLocationHuman.setError(null);
                    binding.date.setError("Enter Date");
                    binding.date.requestFocus();
                }
                else {
                    newFragment = new ConfirmationHumanDialogFragment();
                    Bundle bundle = new Bundle();
                    ArrayList<HumanPosts> humanPosts = new ArrayList<>();
                    humanPosts.addAll(Collections.singleton(posts));
                    bundle.putParcelableArrayList("LIST", humanPosts);
                    bundle.putDouble(LAT,lat);
                    bundle.putDouble(LON,lon);
                    newFragment.setArguments(bundle);
                    newFragment.show(fragmentManager, ConfirmationHumanDialogFragment.TITLE);
                    dismiss();

                }



            }
        });

        binding.humanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(view.getContext());
            }});
        binding.postLocationHuman.setFocusable(false);
        binding.postLocationHuman.setClickable(true);
        binding.postLocationHuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , Maps.class);
                startActivityForResult(intent,1);
            }
        });
        myCalendar = Calendar.getInstance();
        binding.date.setFocusable(false);
        binding.date.setClickable(true);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
           AlertDialog alertDialog = builder.create();

           alertDialog.show();

        return alertDialog ;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK && data != null) {
                    selectedImage = (Bitmap) data.getExtras().get("data");
                    Uri uri = ConvertFromBitmapToURI(selectedImage);
                    binding.humanPp.setText(String.valueOf(uri));

                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    address = bundle.getString(Maps.ADDRESS);
                    lat = bundle.getDouble(Maps.LAT);
                    lon = bundle.getDouble(Maps.LON);
                    city = bundle.getString(Maps.CITY);
                    binding.postLocationHuman.setText(address);
                    Log.v("LocationMaps","Dialog City : "+ city);

                }
                break;
            case 2:
                if (resultCode == RESULT_OK && data != null) {
                    Uri targetUri = data.getData();
                    binding.humanPp.setText(String.valueOf(targetUri));
                    break;
                }
        }
    }

    private void selectImage(Context context) {

        final CharSequence[] options =
                {
                        "Take Photo",
                        "Choose from Gallery",
                        "Cancel",

                };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CAMERA},
                                0);
                    } else {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 0);
                    }



                } else if (options[item].equals("Choose from Gallery")) {
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                2);
                    } else {
                        Intent pickPhoto = new Intent();
                        pickPhoto.setType("image/*");
                        pickPhoto.setAction(Intent.ACTION_PICK);
                        startActivityForResult(pickPhoto, 2);
                    }

                } else  {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private Uri ConvertFromBitmapToURI(Bitmap selectedImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);}
        else {
            String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                    selectedImage, "Title", null);
            Uri parse = Uri.parse(path);
            return parse ;

        }
        return  null ;

    }


    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.date.setText(sdf.format(myCalendar.getTime()));
    }


}
