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
import com.hfad.dawrlygp.databinding.ActivityChangMailBinding;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.viewModel.ChangeEmailViewModel;

import java.util.Objects;

public class ChangeMail extends AppCompatActivity {

   private Toolbar toolbar ;

   private ActivityChangMailBinding binding ;

   private ChangeEmailViewModel model ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chang_mail);
        model = ViewModelProviders.of(this).get(ChangeEmailViewModel.class);
       // binding.setLifecycleOwner(this);
        binding.setChange(model);

        toolbar = findViewById(R.id.change_mail_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Change Email");

        model.getData().observe(this, new Observer<User>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChanged(final User user) {

                if (TextUtils.isEmpty(Objects.requireNonNull(user).getEmail())) {
                    binding.currentMail.setError("Enter an E-Mail Address");
                    binding.currentMail.requestFocus();
                    binding.error.setText("");

                }

                else if (!user.isEmailValid(user.getEmail())) {
                    binding.currentMail.setError("Enter a Valid E-mail Address");
                    binding.currentMail.requestFocus();
                    binding.error.setText("");

                }

                else if (TextUtils.isEmpty(Objects.requireNonNull(user).getNewEmail())) {
                    binding.newMail.setError("Enter an E-Mail Address");
                    binding.newMail.requestFocus();
                    binding.error.setText("");

                }

                else if (!user.isEmailValid(user.getNewEmail())) {
                    binding.newMail.setError("Enter a Valid E-mail Address");
                    binding.newMail.requestFocus();
                    binding.error.setText("");

                }

                else {
                       binding.progressMail.setVisibility(View.VISIBLE);
                       model.updateMail();
                       model.setBack(new ChangeEmailViewModel.CallBack() {
                           @Override
                           public void onCallBackMail(Boolean isSuccessMail) {
                               if (!isSuccessMail){
                                    binding.progressMail.setVisibility(View.GONE);
                                    binding.error.setText("Invalid E-mail");

                               }
                           }

                           @Override
                           public void onCallBack(Boolean isSuccess) {
                               if (isSuccess){
                                     binding.progressMail.setVisibility(View.GONE);
                                     Toast.makeText(ChangeMail.this, "E-mail is updated!",
                                            Toast.LENGTH_SHORT).show();
                                     binding.currentMail.setText("");
                                     binding.newMail.setText("");
                                      binding.error.setText("");
                               }else {
                                   binding.progressMail.setVisibility(View.GONE);
                                   binding.error.setText("The email address is already in use by another account");
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
