package com.hfad.dawrlygp.views.Fragments.DialogFragments.ConfirmationDialogs;

import android.app.Dialog;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.databinding.ConfirmationMobileDialogBinding;
import com.hfad.dawrlygp.model.ItemPosts;
import com.hfad.dawrlygp.viewModel.ConfirmationItemDialogViewModel;
import com.hfad.dawrlygp.views.Fragments.DialogFragments.Dialogs.ItemDialogFragment;
import java.io.FileNotFoundException;
import java.util.List;

public class ConfirmationItemDialogFragment extends DialogFragment {

    public static String TITLE = "Confirmation";

    private Bundle mArgs ;

    private ConfirmationMobileDialogBinding binding ;

    private ConfirmationItemDialogViewModel model ;

    private List<ItemPosts> itemPosts ;

    private double lat , lon ;

    private String fileEx ;


    public ConfirmationItemDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder build = new AlertDialog.Builder(getActivity() ,
                android.R.style.Theme_Material_NoActionBar_Fullscreen);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.confirmation_mobile_dialog,null,false);

        model = ViewModelProviders.of(this).get(ConfirmationItemDialogViewModel.class);

        mArgs = getArguments();

        build.setTitle(TITLE);

        build.setView(binding.getRoot());

        binding.imageBackgroundConfirm.setVisibility(View.GONE);

        itemPosts = mArgs.getParcelableArrayList("LIST");

        lat = mArgs.getDouble(ItemDialogFragment.LAT);

        lon = mArgs.getDouble(ItemDialogFragment.LON);

        binding.setConfirmation(itemPosts.get(0));

        setData();

       binding.linearLayoutConfirmation.setVisibility(View.VISIBLE);

       binding.proBar.setVisibility(View.GONE);

        post();

        save();


        return build.create();


    }

    private void setData(){

        if (itemPosts.get(0).getPostItemsId() == 1)
        {
            binding.modelName.setVisibility(View.VISIBLE);
            binding.modelNameTitle.setVisibility(View.VISIBLE);
            binding.divider3.setVisibility(View.VISIBLE);
        }


         if (itemPosts.get(0).getPostItemsId() == 7
                 || itemPosts.get(0).getPostItemsId() ==8
                 || itemPosts.get(0).getPostItemsId() ==2 )
         {
             if (itemPosts.get(0).getTitle() == null){
                 binding.brandNameTitle.setVisibility(View.GONE);

             }
            binding.mobileColor.setVisibility(View.GONE);
            binding.colorTitle.setVisibility(View.GONE);
            binding.divider4.setVisibility(View.GONE);
         }

        if (itemPosts.get(0).getImageUrl() == null){
            binding.imageLinear.setVisibility(View.GONE);
            binding.divider8.setVisibility(View.GONE);

        }
        else {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(
                        getContext().getContentResolver().openInputStream(Uri.parse(itemPosts.get(0).getImageUrl())));

                fileEx = getFileExtension(Uri.parse(itemPosts.get(0).getImageUrl()));

                binding.imageConfirmationMobile.setImageBitmap(bitmap);

                binding.divider8.setVisibility(View.GONE);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void  save (){

        binding.saveMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success = model.save(itemPosts.get(0) , lat , lon , fileEx);
                if (success){
                    binding.linearLayoutConfirmation.setVisibility(View.GONE);
                    binding.proBar.setVisibility(View.VISIBLE);
                    binding.imageBackgroundConfirm.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            dismiss();
                            Toast.makeText(getContext(),"Post Saved Successfully",
                                    Toast.LENGTH_LONG).show();


                        }
                    }, 3000);

                }
                else {
                    dismiss();
                    Toast.makeText(getContext(),"Something wrong happen try again",
                            Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void post() {
        binding.postMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.linearLayoutConfirmation.setVisibility(View.GONE);
                binding.proBar.setVisibility(View.VISIBLE);
                binding.imageBackgroundConfirm.setVisibility(View.VISIBLE);
                boolean success = model.post(itemPosts.get(0) , lat , lon , fileEx);
                if (success){
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            dismiss();
                            Toast.makeText(getContext(),"Request sent to admin successfully",
                                    Toast.LENGTH_LONG).show();

                        }
                    }, 3000);

                }
                else {
                    dismiss();
                    Toast.makeText(getContext(),"Something wrong happen try again",
                            Toast.LENGTH_LONG).show();
                }


            }
        });
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR =getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }



}
