package com.hfad.dawrlygp.views.Fragments.DialogFragments;



import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.dawrlygp.viewModel.MainDialogViewModel;
import com.hfad.dawrlygp.views.Adapters.PostAdapter;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.views.Fragments.DialogFragments.Dialogs.HumanDialogFragment;
import com.hfad.dawrlygp.views.Fragments.DialogFragments.Dialogs.ItemDialogFragment;
import com.hfad.dawrlygp.views.Helpers.DataCalled;


public class MainDialogFragment extends DialogFragment implements DataCalled {

    public static String Title = "Select item which you Lost or Found";

    public static String Position="position";

    private PostAdapter postAdapter;

    private int pos = -1;

    private MainDialogViewModel model;

    private Button next;

    private RecyclerView postView;

    private View convertView;

    private LayoutInflater inflater;

    private DialogFragment newFragment;

    private FragmentManager fragmentManager;

    private ProgressBar progressBar ;

    private Bundle b ;

    private AlertDialog.Builder dialog ;


    public MainDialogFragment() {
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialog = new AlertDialog.Builder(getActivity(),
                android.R.style.Theme_Material_NoActionBar_Fullscreen);

        model = ViewModelProviders.of(this).get(MainDialogViewModel.class);

        inflater = getActivity().getLayoutInflater();

        convertView = inflater.inflate(R.layout.post_dialog, null);

        postView = convertView.findViewById(R.id.post_list);

        next = convertView.findViewById(R.id.next);

        final ImageView imageView = convertView.findViewById(R.id.imageBackground);

        progressBar = convertView.findViewById(R.id.progress_bar);
        dialog.setTitle(Title);
        dialog.setView(convertView);
        imageView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        postView.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        model.init(this);
        model.setSuccess(new MainDialogViewModel.Success() {
            @Override
            public void onSuccess(boolean isSuccess) {
                if (isSuccess){
                    progressBar.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    postView.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(convertView.getContext(),"Something wrong happen try again",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        postAdapter = new PostAdapter(model.getData().getValue());
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        postView.setLayoutManager(gridLayoutManager);
        postView.setItemAnimator(new DefaultItemAnimator());
        postView.setAdapter(postAdapter);

        postAdapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                pos = position ;
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager = getFragmentManager();
                b = new Bundle();
                int position = pos+1;
                b.putInt(Position,position);

                if (pos == 8)
                {
                    newFragment = new HumanDialogFragment();
                    newFragment.setArguments(b);
                    newFragment.show(fragmentManager, HumanDialogFragment.TITLE);
                    dismiss();
                }

                else if (pos >=0 && pos<=7){
                    newFragment = new ItemDialogFragment();
                    newFragment.setArguments(b);
                    newFragment.show(fragmentManager, ItemDialogFragment.TITLE);
                    dismiss();
                }
                else {
                    Toast.makeText(getContext(), "Choose item which you Lost or Found",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


        return dialog.create();
    }


    @Override
    public void onDataCalled() {
       /* model.getData().observe(this, new Observer<List<PostItems>>() {
            @Override
            public void onChanged(List<PostItems> postItems) {
            }
        });*/
        postAdapter.notifyDataSetChanged();

    }
}