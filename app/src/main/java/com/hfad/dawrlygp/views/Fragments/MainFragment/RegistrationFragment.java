package com.hfad.dawrlygp.views.Fragments.MainFragment;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.firebase.database.annotations.Nullable;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.databinding.FragmentRegistrationBinding;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.viewModel.RegistrationViewModel;
import com.hfad.dawrlygp.views.Maps;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class RegistrationFragment extends Fragment implements View.OnClickListener{

    private Calendar myCalendar;
    private NavController navController ;
    public static String address  ;
    public static double  lat , lon ;
    private RegistrationViewModel model ;
    private FragmentRegistrationBinding binding ;



    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(binding.getRoot());

    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        model = ViewModelProviders.of(this).get(RegistrationViewModel.class);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.fragment_registration,null,false);
        binding.setUser(model);
        binding.setLifecycleOwner(this);
        model.getData().observe(this, new Observer<User>() {
           @TargetApi(Build.VERSION_CODES.KITKAT)
           @Override
           public void onChanged(final User user) {
               if (TextUtils.isEmpty(Objects.requireNonNull(user).getEmail())){
                   binding.registerMail.setError("Enter an E-mail address");
                   binding.registerMail.requestFocus();
               }

               else if (!user.isEmailValid(user.getEmail())) {
                   binding.registerMail.setError("Enter a valid e-mail address");
                   binding.registerMail.requestFocus();
               }

               else if (TextUtils.isEmpty(Objects.requireNonNull(user).getFname())){
                   binding.fNameRegister.setError("Enter first name");
                   binding.fNameRegister.requestFocus();
               }

               else if (TextUtils.isEmpty(Objects.requireNonNull(user).getPhoneNumber())){
                   binding.phoneNumber.setError("Enter valid phone number");
                   binding.phoneNumber.requestFocus();
               }

               else if (TextUtils.isEmpty(Objects.requireNonNull(user).getLname())){
                   binding.lNameRegister.setError("Enter last name");
                   binding.lNameRegister.requestFocus();
               }

               else if (TextUtils.isEmpty(Objects.requireNonNull(user).getPassword())){
                  binding.passwordRegister.setError("Enter an password");
                  binding.passwordRegister.requestFocus();
              }

               else if (!user.isPasswordValid(user.getPassword())) {
                   binding.registerError.setText("Enter a valid password ");
                   binding.registerError.requestFocus();
               }

               else if (TextUtils.isEmpty(Objects.requireNonNull(user).getConfirmPassword())){
                   binding.confirmPasswordRegister.setError("");
                   binding.confirmPasswordRegister.requestFocus();
               }

               else if (TextUtils.isEmpty(Objects.requireNonNull(user).getDateOfBirth())){
                   binding.dateBirthRegister.setError("Enter birth date");
                   binding.dateBirthRegister.requestFocus();
               }

               else if (TextUtils.isEmpty(Objects.requireNonNull(user).getGender())){
                   binding.registerError.setText("Enter gender");
               }

               else if (!user.getPassword().equals(user.getConfirmPassword())) {
                   binding.registerError.setText("Password doesn't matches");
                   binding.registerError.requestFocus();
               }

               else if (TextUtils.isEmpty(Objects.requireNonNull(user).getLocation())) {
                   binding.registerError.setText("Set your location");
                   binding.registerError.requestFocus();
               }

               else {
                   binding.registerProgress.setVisibility(View.VISIBLE);
                   model.signIn(user.getEmail(),user.getPassword());
                   model.setBack(new RegistrationViewModel.myCallBack() {
                       @Override
                       public void onCallback(Boolean success) {
                           if (success){
                               binding.registerProgress.setVisibility(View.GONE);
                               navController.navigate(R.id.action_registrationFragment_to_loginfragment);
                               Log.v("registration", String.valueOf(success));
                           } if (!success){

                               binding.registerProgress.setVisibility(View.GONE);
                               binding.registerError.setText("The email address is already in use by another account");
                               Log.v("registration", String.valueOf(success));



                           }
                       }
                   });



               }


           }
       });
        myCalendar = Calendar.getInstance();
        binding.dateBirthRegister.setFocusable(false);
        binding.dateBirthRegister.setClickable(true);
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

        binding.dateBirthRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        binding.btBackToLogin.setOnClickListener(this);
        binding.locationDet.setFocusable(false);
        binding.locationDet.setClickable(true);
        binding.locationDet.setOnClickListener(this);

        return binding.getRoot() ;

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        binding.dateBirthRegister.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.location_det:
                Intent intent = new Intent(view.getContext() , Maps.class);
                startActivityForResult(intent,1);
                break;
            case R.id.btBackToLogin:
                navController.navigate(R.id.action_registrationFragment_to_loginfragment);
                break;

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    address = bundle.getString(Maps.ADDRESS);
                    lat = bundle.getDouble(Maps.LAT);
                    lon = bundle.getDouble(Maps.LON);
                    Log.v("Location","Fragment 1 Lat : "+lat +"\n"+"Long : "+lon);

                    binding.locationDet.setText(address);
                }
                break;
        }
    }



}
