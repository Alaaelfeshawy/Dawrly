package com.hfad.dawrlygp.views.Fragments.MainFragment;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.databinding.FragmentForgetPasswordBinding;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.viewModel.ForgetPasswordViewModel;

import java.util.Objects;

public class ForgetPasswordFragment extends Fragment {

   private FragmentForgetPasswordBinding binding ;
   private ForgetPasswordViewModel model ;
   private NavController navController;
   private FirebaseAuth auth ;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(binding.getRoot());

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        model = ViewModelProviders.of(this).get(ForgetPasswordViewModel.class);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.fragment_forget_password,null,false);
        binding.setUser(model);
        binding.setLifecycleOwner(this);
        binding.forgetPasswordToolbar.setTitle("Activity");
        binding.forgetPasswordToolbar.setNavigationIcon(R.drawable.backarrow);
        binding.forgetPasswordToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_forgetPasswordFragment_to_loginfragment);

            }
        });
        auth = FirebaseAuth.getInstance();

        model.getData().observe(this, new Observer<User>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChanged(final User user) {
                if (TextUtils.isEmpty(Objects.requireNonNull(user).getEmail())) {
                    binding.forgetEmail.setError("Enter an E-mail");
                    binding.forgetEmail.requestFocus();

                }
                else if (!user.isEmailValid(user.getEmail())){
                    binding.forgetEmail.setError("Enter an e-mail invalid");
                }

                else {

                    binding.progress.setVisibility(View.VISIBLE);
                    model.forgetPassword(user.getEmail());
                    model.setCallBack(new ForgetPasswordViewModel.CallBack() {
                       @Override
                       public void onCallBack(boolean isSuccess) {
                           if (isSuccess){
                               binding.forgetEmail.setText("");
                               Toast.makeText(getContext(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();

                           }else {
                               binding.forgetEmail.setError("Invalid Email");
                               binding.forgetEmail.requestFocus();
                               Toast.makeText(getContext() , "Failed to send reset email!", Toast.LENGTH_SHORT).show();

                           }
                           binding.progress.setVisibility(View.GONE);

                       }
                   });

                }


            }
        });
        return binding.getRoot();
    }



}
