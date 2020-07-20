package com.hfad.dawrlygp.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.databinding.ActivityChangePasswordBinding;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.viewModel.ChangePasswordViewModel;

import java.util.Objects;

public class ChangePassword extends AppCompatActivity {

    private Toolbar toolbar ;

    private ChangePasswordViewModel model ;

    private ActivityChangePasswordBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this ,
                R.layout.activity_change_password , null );
        model = ViewModelProviders.of(this).get(ChangePasswordViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setChangePassword(model);
        toolbar = findViewById(R.id.change_password_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Change Password");



        model.getData().observe(this, new Observer<User>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChanged(final User user) {
                if (TextUtils.isEmpty(Objects.requireNonNull(user).getPassword())) {
                    binding.currentPassword.setError("Enter current password");
                    binding.currentPassword.requestFocus();
                    binding.errorPassword.setText("");


                }
                else if (!user.isPasswordValid(user.getPassword())) {
                    binding.currentPassword.setError("Enter a Valid password");
                    binding.currentPassword.requestFocus();
                    binding.errorPassword.setText("");
                    binding.progressChange.setVisibility(View.GONE);


                }
                else if (TextUtils.isEmpty(Objects.requireNonNull(user).getNewPassword())) {
                    binding.newPassword.setError("Enter new password");
                    binding.newPassword.requestFocus();
                    binding.errorPassword.setText("");

                }
                else if (!user.isPasswordValid(user.getNewPassword())) {
                    binding.newPassword.setError("Enter a Valid password");
                    binding.newPassword.requestFocus();
                    binding.errorPassword.setText("");

                }
                else if (!user.getNewPassword().equals(user.getConfirmPassword())) {
                    binding.errorPassword.setText("Password doesn't match");

                }
                else {
                    binding.progressChange.setVisibility(View.VISIBLE);
                   model.updatePassword();
                   model.setCallBack(new ChangePasswordViewModel.CallBack() {
                       @Override
                       public void onCallBackPassword(boolean isSuccess) {
                           if (!isSuccess){
                               binding.errorPassword.setText("Invalid password");
                               binding.progressChange.setVisibility(View.GONE);

                           }
                       }

                       @Override
                       public void onCallBack(boolean isSuccess) {

                           if (isSuccess){
                               binding.progressChange.setVisibility(View.GONE);
                               Toast.makeText(ChangePassword.this, "Password is updated!",
                                       Toast.LENGTH_SHORT).show();
                               binding.currentPassword.setText("");
                               binding.newPassword.setText("");
                               binding.confirmNewPassword.setText("");
                               binding.errorPassword.setText("");

                           }else {
                               binding.progressChange.setVisibility(View.GONE);
                               binding.errorPassword.setText("Password should be at least 6 characters , 1 digit and 1 Uppercase");

                           }

                       }
                   });

                }
            }
        });
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
}
