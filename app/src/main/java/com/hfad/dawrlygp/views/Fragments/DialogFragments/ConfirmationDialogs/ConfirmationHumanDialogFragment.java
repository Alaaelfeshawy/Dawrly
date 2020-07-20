package com.hfad.dawrlygp.views.Fragments.DialogFragments.ConfirmationDialogs;

import android.app.Dialog;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.hfad.dawrlygp.databinding.DialogConfirmationBinding;
import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.ItemPosts;
import com.hfad.dawrlygp.viewModel.ConfirmationHumanDialogViewModel;
import com.hfad.dawrlygp.views.Fragments.DialogFragments.Dialogs.HumanDialogFragment;
import com.hfad.dawrlygp.views.Fragments.DialogFragments.Dialogs.ItemDialogFragment;

import java.io.FileNotFoundException;
import java.util.List;

public class ConfirmationHumanDialogFragment extends DialogFragment {

    public static String TITLE = "Confirmation";

    private double lat , lon ;

    private Bundle mArgs ;

    private DialogConfirmationBinding binding ;

    private ConfirmationHumanDialogViewModel model ;

    private List<HumanPosts> humanPosts ;

    private String fileEx ;


    public ConfirmationHumanDialogFragment() {
    }

    @NonNull
    @Override

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder build = new AlertDialog.Builder(getActivity() ,
                android.R.style.Theme_Material_NoActionBar_Fullscreen);

             binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.dialog_confirmation,null,false);
             binding.setLifecycleOwner(this);
             model = ViewModelProviders.of(this).get(ConfirmationHumanDialogViewModel.class);
             mArgs = getArguments();
             build.setTitle(TITLE);
             build.setView(binding.getRoot());

             binding.imageBackgroundConfirm.setVisibility(View.GONE);

            humanPosts = mArgs.getParcelableArrayList("LIST");

            lat = mArgs.getDouble(ItemDialogFragment.LAT);

            lon = mArgs.getDouble(ItemDialogFragment.LON);

            binding.setHuman(humanPosts.get(0));

             setData();

             binding.linearLayout.setVisibility(View.VISIBLE);

             binding.proBar.setVisibility(View.GONE);

             post();

             savePost();

             return build.create();


    }


    private   void  savePost(){
        binding.save.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                binding.linearLayout.setVisibility(View.GONE);
                                                binding.proBar.setVisibility(View.VISIBLE);
                                                binding.imageBackgroundConfirm.setVisibility(View.VISIBLE);
                                                boolean success = model.save(humanPosts.get(0),lat , lon , fileEx);
                                                if (success){
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
                                        }
        );

    }

    private static final String TAG = "ConfirmationHumanDialog";

    private void post(){
        Log.d(TAG, "post: "+fileEx + humanPosts.get(0).getImageUrl());
        binding.post.setOnClickListener(view -> {
            binding.linearLayout.setVisibility(View.GONE);
            binding.proBar.setVisibility(View.VISIBLE);
            binding.imageBackgroundConfirm.setVisibility(View.VISIBLE);
            boolean success = model.post(humanPosts.get(0),lat , lon , fileEx);
            if (success){
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    dismiss();
                    Toast.makeText(getContext(),"Request sent to admin successfully",
                            Toast.LENGTH_LONG).show();

                }, 3000);

            }
            else {
                dismiss();
                Toast.makeText(getContext(),"Something wrong happen try again",
                        Toast.LENGTH_LONG).show();
            }

        });
    }

    private void setData(){


        if (humanPosts.get(0).getImageUrl() == null){
            binding.imageLinear.setVisibility(View.GONE);
            binding.divider8.setVisibility(View.GONE);

        }

        else {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(
                        getContext().getContentResolver()
                                .openInputStream(Uri.parse(humanPosts.get(0).getImageUrl())));

                fileEx = getFileExtension(Uri.parse(humanPosts.get(0).getImageUrl()));

                binding.imageConfirmation.setImageBitmap(bitmap);

                binding.divider8.setVisibility(View.GONE);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR =getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


}
