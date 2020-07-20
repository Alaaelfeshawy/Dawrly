package com.hfad.dawrlygp.views.Fragments.DialogFragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.hfad.dawrlygp.views.Maps;
import com.hfad.dawrlygp.R;

public class MapDialogFragment extends DialogFragment {

    public  static String TITLE = "Use This Location ?";

    private View convertView ;

    private LayoutInflater inflater;

    private TextView name ;

    public MapDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         super.onCreateDialog(savedInstanceState);
        inflater = LayoutInflater.from(getActivity());
        convertView = inflater.inflate(R.layout.dialog_map, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(TITLE);
        builder.setView(convertView);
        Bundle bundle = getArguments();
        final String address = bundle.getString(Maps.ADDRESS);
        final double lat = bundle.getDouble(Maps.LAT);
        final double lon = bundle.getDouble(Maps.LON);
        final String city = bundle.getString(Maps.CITY);


        Log.v("Location","Dialog Lat : "+lat +"\n"+"Long : "+lon);


        name = convertView.findViewById(R.id.map_confirmation);
        name.setText(address);
        builder.setNegativeButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Bundle bundle1 = new Bundle();
                bundle1.putString(Maps.ADDRESS,address);
                bundle1.putDouble(Maps.LAT,lat);
                bundle1.putDouble(Maps.LON,lon);
                bundle1.putString(Maps.CITY,city);
                Log.v("LocationMaps","MapsFragment City : "+ city);


                Intent returnIntent = new Intent();
                returnIntent.putExtras(bundle1);
                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();


            }
        });
        builder.setNeutralButton("Change Location", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });
        return builder.create();

    }
}
