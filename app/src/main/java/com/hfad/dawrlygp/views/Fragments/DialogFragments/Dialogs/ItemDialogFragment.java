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

import com.hfad.dawrlygp.databinding.DialogMobileBinding;
import com.hfad.dawrlygp.model.ItemPosts;
import com.hfad.dawrlygp.model.PostItems;
import com.hfad.dawrlygp.viewModel.ItemDialogViewModel;
import com.hfad.dawrlygp.views.Fragments.DialogFragments.ConfirmationDialogs.ConfirmationItemDialogFragment;
import com.hfad.dawrlygp.views.Fragments.DialogFragments.MainDialogFragment;
import com.hfad.dawrlygp.views.Maps;
import com.hfad.dawrlygp.R;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ItemDialogFragment extends DialogFragment implements  View.OnClickListener{

    public static String TITLE = "Mobile" , address , LAT="lat",LON = "lon" , city , CITY="city" , TITLE_3 = "sunglasses";

    private static double lat , lon ;

    private Bitmap selectedImage ;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private Calendar myCalendar;

   private DialogFragment newFragment ;

   private FragmentManager fragmentManager;

   private ItemDialogViewModel model ;

   private DialogMobileBinding binding ;

   private int position ;

    public ItemDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        super.onCreateDialog(savedInstanceState);

        model = ViewModelProviders.of(this).get(ItemDialogViewModel.class);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_mobile,null,false);

        Bundle mArgs = getArguments();

        position = mArgs.getInt(MainDialogFragment.Position);

        binding.setItem(model);

        binding.setLifecycleOwner(this);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),android.R.style.Theme_Material_NoActionBar_Fullscreen);

        if (position == 1){

            builder.setTitle(TITLE);

            binding.modelNameMobile.setVisibility(View.VISIBLE);

            binding.modelNameTitle.setVisibility(View.VISIBLE);

            builder.setView(binding.getRoot());

            fragmentManager = getFragmentManager();

            model.getData().observe(this, new Observer<ItemPosts>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onChanged(ItemPosts itemPosts) {
                    itemPosts.setPostItemsId(position);
                    itemPosts.setCity(city);
                    if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getTitle())) {
                        binding.brandNameMobile.setError("Enter brand name");
                        binding.brandNameMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getModelName())){
                        binding.modelNameMobile.setError("Enter model name");
                        binding.modelNameMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getColor())){
                        binding.colorMobile.setError("Enter Color");
                        binding.colorMobile.requestFocus();

                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getDescription())){
                        binding.descMobile.setError("Enter Description");
                        binding.descMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getLocation())){
                        binding.locationMobile.setError("Enter Location");
                        binding.locationMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getTime())){
                        binding.dateMobile.setError("Enter Date");
                        binding.dateMobile.requestFocus();
                    }
                    else {
                        newFragment = new ConfirmationItemDialogFragment();
                        Bundle bundle = new Bundle();
                        ArrayList<ItemPosts> itemPosts1 = new ArrayList<>();
                        itemPosts1.addAll(Collections.singleton(itemPosts));
                        bundle.putParcelableArrayList("LIST", itemPosts1);
                        bundle.putDouble(LAT,lat);
                        bundle.putDouble(LON,lon);
                        newFragment.setArguments(bundle);
                        newFragment.show(fragmentManager, ConfirmationItemDialogFragment.TITLE);
                        dismiss();
                    }
                }
            });
        }

        if (position == 2){

            builder.setTitle("Keys");

            binding.brandNameMobile.setVisibility(View.GONE);

            binding.brandNameTitle.setVisibility(View.GONE);

            builder.setView(binding.getRoot());

            fragmentManager = getFragmentManager();

            model.getData().observe(this, new Observer<ItemPosts>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onChanged(ItemPosts itemPosts) {
                    itemPosts.setPostItemsId(position);
                    itemPosts.setCity(city);

                    if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getColor())){

                        binding.colorMobile.setError("Enter Color");
                        binding.colorMobile.requestFocus();

                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getDescription())){
                        binding.descMobile.setError("Enter Description");
                        binding.descMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getLocation())){
                        binding.locationMobile.setError("Enter Location");
                        binding.locationMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getTime())){
                        binding.dateMobile.setError("Enter Date");
                        binding.dateMobile.requestFocus();
                    }
                    else {
                        newFragment = new ConfirmationItemDialogFragment();
                        Bundle bundle = new Bundle();
                        ArrayList<ItemPosts> itemPosts1 = new ArrayList<>();
                        itemPosts1.addAll(Collections.singleton(itemPosts));
                        bundle.putParcelableArrayList("LIST", itemPosts1);
                        bundle.putDouble(LAT,lat);
                        bundle.putDouble(LON,lon);
                        newFragment.setArguments(bundle);
                        newFragment.show(fragmentManager, ConfirmationItemDialogFragment.TITLE);
                        dismiss();
                    }
                }
            });
        }

        if (position == 3 ){

            builder.setTitle(TITLE_3);

            builder.setView(binding.getRoot());

            fragmentManager = getFragmentManager();

            model.getData().observe(this, new Observer<ItemPosts>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onChanged(ItemPosts itemPosts) {

                    itemPosts.setPostItemsId(position);
                    itemPosts.setCity(city);

                    if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getTitle())) {
                        binding.brandNameMobile.setError("Enter brand name");
                        binding.brandNameMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getColor())){
                        binding.colorMobile.setError("Enter Color");
                        binding.colorMobile.requestFocus();

                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getDescription())){
                        binding.descMobile.setError("Enter Description");
                        binding.descMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getLocation())){
                        binding.locationMobile.setError("Enter Location");
                        binding.locationMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getTime())){
                        binding.dateMobile.setError("Enter Date");
                        binding.dateMobile.requestFocus();
                    }
                    else {
                        newFragment = new ConfirmationItemDialogFragment();
                        Bundle bundle = new Bundle();
                        ArrayList<ItemPosts> itemPosts1 = new ArrayList<>();
                        itemPosts1.addAll(Collections.singleton(itemPosts));
                        bundle.putParcelableArrayList("LIST", itemPosts1);
                        bundle.putDouble(LAT,lat);
                        bundle.putDouble(LON,lon);
                        newFragment.setArguments(bundle);
                        newFragment.show(fragmentManager, ConfirmationItemDialogFragment.TITLE);
                        dismiss();
                    }
                }
            });
        }

        if (position == 4 ){

            builder.setTitle("Wallets");

            builder.setView(binding.getRoot());

            fragmentManager = getFragmentManager();

            model.getData().observe(this, new Observer<ItemPosts>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onChanged(ItemPosts itemPosts) {
                    itemPosts.setPostItemsId(position);
                    itemPosts.setCity(city);

                    if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getTitle())) {
                        binding.brandNameMobile.setError("Enter brand name");
                        binding.brandNameMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getColor())){
                        binding.colorMobile.setError("Enter Color");
                        binding.colorMobile.requestFocus();

                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getDescription())){
                        binding.descMobile.setError("Enter Description");
                        binding.descMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getLocation())){
                        binding.locationMobile.setError("Enter Location");
                        binding.locationMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getTime())){
                        binding.dateMobile.setError("Enter Date");
                        binding.dateMobile.requestFocus();
                    }
                    else {

                        newFragment = new ConfirmationItemDialogFragment();
                        Bundle bundle = new Bundle();
                        ArrayList<ItemPosts> itemPosts1 = new ArrayList<>();
                        itemPosts1.addAll(Collections.singleton(itemPosts));
                        bundle.putParcelableArrayList("LIST", itemPosts1);
                        bundle.putDouble(LAT,lat);
                        bundle.putDouble(LON,lon);
                        newFragment.setArguments(bundle);
                        newFragment.show(fragmentManager, ConfirmationItemDialogFragment.TITLE);
                        dismiss();
                    }
                }
            });
        }

        if (position == 5 ){

            builder.setTitle("Wallets");

            builder.setView(binding.getRoot());

            fragmentManager = getFragmentManager();

            model.getData().observe(this, new Observer<ItemPosts>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onChanged(ItemPosts itemPosts) {
                    itemPosts.setPostItemsId(position);
                    itemPosts.setCity(city);

                    if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getTitle())) {
                        binding.brandNameMobile.setError("Enter brand name");
                        binding.brandNameMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getColor())){
                        binding.colorMobile.setError("Enter Color");
                        binding.colorMobile.requestFocus();

                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getDescription())){
                        binding.descMobile.setError("Enter Description");
                        binding.descMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getLocation())){
                        binding.locationMobile.setError("Enter Location");
                        binding.locationMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getTime())){
                        binding.dateMobile.setError("Enter Date");
                        binding.dateMobile.requestFocus();
                    }
                    else {
                        newFragment = new ConfirmationItemDialogFragment();
                        Bundle bundle = new Bundle();
                        ArrayList<ItemPosts> itemPosts1 = new ArrayList<>();
                        itemPosts1.addAll(Collections.singleton(itemPosts));
                        bundle.putParcelableArrayList("LIST", itemPosts1);
                        bundle.putDouble(LAT,lat);
                        bundle.putDouble(LON,lon);
                        newFragment.setArguments(bundle);
                        newFragment.show(fragmentManager, ConfirmationItemDialogFragment.TITLE);
                        dismiss();
                    }
                }
            });
        }

        if (position == 6 ){

            builder.setTitle("Wallets");

            builder.setView(binding.getRoot());

            fragmentManager = getFragmentManager();

            model.getData().observe(this, new Observer<ItemPosts>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onChanged(ItemPosts itemPosts) {
                    itemPosts.setPostItemsId(position);
                    itemPosts.setCity(city);

                    if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getTitle())) {
                        binding.brandNameMobile.setError("Enter brand name");
                        binding.brandNameMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getColor())){
                        binding.colorMobile.setError("Enter Color");
                        binding.colorMobile.requestFocus();

                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getDescription())){
                        binding.descMobile.setError("Enter Description");
                        binding.descMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getLocation())){
                        binding.locationMobile.setError("Enter Location");
                        binding.locationMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getTime())){
                        binding.dateMobile.setError("Enter Date");
                        binding.dateMobile.requestFocus();
                    }
                    else {

                        newFragment = new ConfirmationItemDialogFragment();
                        Bundle bundle = new Bundle();
                        ArrayList<ItemPosts> itemPosts1 = new ArrayList<>();
                        itemPosts1.addAll(Collections.singleton(itemPosts));
                        bundle.putParcelableArrayList("LIST", itemPosts1);
                        bundle.putDouble(LAT,lat);
                        bundle.putDouble(LON,lon);
                        newFragment.setArguments(bundle);
                        newFragment.show(fragmentManager, ConfirmationItemDialogFragment.TITLE);
                        dismiss();
                    }
                }
            });
        }

        if (position == 7){

            builder.setTitle("Books");

            binding.brandNameTitle.setText("Book Name");

            binding.colorMobile.setVisibility(View.GONE);

            binding.colorTitle.setVisibility(View.GONE);

            builder.setView(binding.getRoot());

            fragmentManager = getFragmentManager();

            model.getData().observe(this, new Observer<ItemPosts>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onChanged(ItemPosts itemPosts) {
                    itemPosts.setPostItemsId(position);
                    itemPosts.setCity(city);

                    if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getTitle())){
                        binding.colorMobile.setError("Enter Title");
                        binding.colorMobile.requestFocus();

                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getDescription())){
                        binding.descMobile.setError("Enter Description");
                        binding.descMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getLocation())){
                        binding.locationMobile.setError("Enter Location");
                        binding.locationMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getTime())){
                        binding.dateMobile.setError("Enter Date");
                        binding.dateMobile.requestFocus();
                    }
                    else {
                        newFragment = new ConfirmationItemDialogFragment();
                        Bundle bundle = new Bundle();
                        ArrayList<ItemPosts> itemPosts1 = new ArrayList<>();
                        itemPosts1.addAll(Collections.singleton(itemPosts));
                        bundle.putParcelableArrayList("LIST", itemPosts1);
                        bundle.putDouble(LAT,lat);
                        bundle.putDouble(LON,lon);
                        newFragment.setArguments(bundle);
                        newFragment.show(fragmentManager, ConfirmationItemDialogFragment.TITLE);
                        dismiss();
                    }
                }
            });
        }

        if (position == 8){

            builder.setTitle("Papers ");

            binding.brandNameTitle.setText("Title");

            binding.colorMobile.setVisibility(View.GONE);

            binding.colorTitle.setVisibility(View.GONE);

            builder.setView(binding.getRoot());

            fragmentManager = getFragmentManager();

            model.getData().observe(this, new Observer<ItemPosts>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onChanged(ItemPosts itemPosts) {
                    itemPosts.setPostItemsId(position);
                    itemPosts.setCity(city);

                 if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getTitle())){
                        binding.colorMobile.setError("Enter Title");
                        binding.colorMobile.requestFocus();

                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getDescription())){
                        binding.descMobile.setError("Enter Description");
                        binding.descMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getLocation())){
                        binding.locationMobile.setError("Enter Location");
                        binding.locationMobile.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Objects.requireNonNull(itemPosts).getTime())){
                        binding.dateMobile.setError("Enter Date");
                        binding.dateMobile.requestFocus();
                    }
                    else {
                        newFragment = new ConfirmationItemDialogFragment();
                        Bundle bundle = new Bundle();
                        ArrayList<ItemPosts> itemPosts1 = new ArrayList<>();
                        itemPosts1.addAll(Collections.singleton(itemPosts));
                        bundle.putParcelableArrayList("LIST", itemPosts1);
                        bundle.putDouble(LAT,lat);
                        bundle.putDouble(LON,lon);
                        newFragment.setArguments(bundle);
                        newFragment.show(fragmentManager, ConfirmationItemDialogFragment.TITLE);
                        dismiss();
                    }
                }
            });
        }


        binding.locationMobile.setFocusable(false);

        binding.locationMobile.setClickable(true);

        binding.locationMobile.setOnClickListener(this);

        myCalendar = Calendar.getInstance();

        binding.dateMobile.setFocusable(false);

        binding.dateMobile.setClickable(true);

        binding.mobileImage.setOnClickListener(this);

        mobileDate();

        return builder.create();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK && data != null) {
                    selectedImage = (Bitmap) data.getExtras().get("data");
                    Uri uri = ConvertFromBitmapToURI(selectedImage);
                    binding.imageUrl.setText(String.valueOf(uri));


                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    address = bundle.getString(Maps.ADDRESS);
                    lat = bundle.getDouble(Maps.LAT);
                    lon = bundle.getDouble(Maps.LON);
                    city = bundle.getString(Maps.CITY);
                    binding.locationMobile.setText(address);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK && data != null) {
                    Uri targetUri = data.getData();
                    binding.imageUrl.setText(String.valueOf(targetUri));
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



                }

                else if (options[item].equals("Choose from Gallery")) {
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

                }

                else  {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.dateMobile.setText(sdf.format(myCalendar.getTime()));
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

    private void mobileDate(){
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

        binding.dateMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.mobile_image:
                selectImage(v.getContext());
                break;
            case R.id.locationMobile:
                Intent intent = new Intent(v.getContext() , Maps.class);
                startActivityForResult(intent,1);
                break;

        }
    }
}
