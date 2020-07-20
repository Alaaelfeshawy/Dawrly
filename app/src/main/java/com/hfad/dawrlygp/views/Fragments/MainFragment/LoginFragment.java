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
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.databinding.FragmentLoginBinding;

import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.viewModel.LoginViewModel;

import java.util.Objects;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private LoginViewModel loginViewModel;

    private FragmentLoginBinding binding;

    private NavController navController;




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(binding.getRoot());

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        binding = DataBindingUtil.inflate(LayoutInflater.
                from(getContext()),R.layout.fragment_login,null,false);

        binding.setLoginViewModel(loginViewModel);

        binding.setLifecycleOwner(this);

        if (loginViewModel.userExist()){
           NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host);
            NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_main);
            navGraph.setStartDestination(R.id.chooserFragment2);
            navController.setGraph(navGraph);
        }

        if (loginViewModel.adminExist()){
           NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host);
            NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_main);
            navGraph.setStartDestination(R.id.adminSplashFragment);
            navController.setGraph(navGraph);

        }

        loginViewModel.getUser().observe(this, new Observer<User>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChanged(final User user) {
                if (TextUtils.isEmpty(Objects.requireNonNull(user).getEmail())) {
                    binding.emailLogin.setError("Enter an E-Mail Address");
                    binding.emailLogin.requestFocus();
                }
                else if (!user.isEmailValid(user.getEmail())) {
                    binding.emailLogin.setError("Enter a Valid E-mail Address");
                    binding.emailLogin.requestFocus();
                }
                else if (TextUtils.isEmpty(Objects.requireNonNull(user).getPassword())) {
                    binding.passwordLogin.setError("Enter a Password");
                    binding.passwordLogin.requestFocus();
                }
                else {
                    binding.loginError.setText("");
                    binding.progressBar.setVisibility(View.VISIBLE);
                    loginViewModel.login(user.getEmail(),user.getPassword());
                    loginViewModel.setAdminCallback(new LoginViewModel.AdminCallback() {
                        @Override
                        public void AdminCallBack(boolean isSuccess) {
                            if (isSuccess){
                                binding.progressBar.setVisibility(View.GONE);
                                navController.navigate(R.id.action_loginfragment_to_adminSplashFragment);
                               // getActivity().getFragmentManager().popBackStack();

                            }

                        }
                    });
                    loginViewModel.setBack(new LoginViewModel.myCallBack() {
                        @Override
                        public void onCallback(final Boolean success) {
                            if (success){
                                binding.progressBar.setVisibility(View.GONE);
                                navController.navigate(R.id.action_loginfragment_to_chooserFragment2);

                            }
                            else{

                                binding.loginError.setText("E-mail or password invalid");
                                binding.progressBar.setVisibility(View.GONE);

                            }
                        }
                    });




                }
            }
        }

        );
       binding.forgetPassword.setOnClickListener(this);
       binding.btCreateAccount.setOnClickListener(this);


        return binding.getRoot() ;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btCreateAccount:
                navController.navigate(R.id.action_loginfragment_to_registrationFragment);

                break;
            case R.id.forgetPassword:
                navController.navigate(R.id.action_loginfragment_to_forgetPasswordFragment);
                 break;
        }

    }

}
