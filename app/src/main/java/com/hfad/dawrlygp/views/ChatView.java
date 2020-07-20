package com.hfad.dawrlygp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.databinding.ActivityChatViewBinding;
import com.hfad.dawrlygp.model.MessageInfo;
import com.hfad.dawrlygp.viewModel.ChatViewModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatView extends AppCompatActivity {

   private ActivityChatViewBinding binding ;

   private ChatViewModel model ;

   private String currentUserId;

   private  ArrayList<MessageInfo> list  ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this ,R.layout.activity_chat_view);

        Toolbar toolbar = findViewById(R.id.chatViewToolbar);

        model = ViewModelProviders.of(this).get(ChatViewModel.class);

        binding.scrollView.post(() -> binding.scrollView.fullScroll(View.FOCUS_DOWN));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setContentInsetStartWithNavigation(0);

        currentUserId = getIntent().getStringExtra("currentUserId");

        list = getIntent().getParcelableArrayListExtra("LIST");

        binding.setUser(list.get(0));

        model.addToDB(currentUserId , list.get(0).getReceiverId());

        model.setMessage(new ChatViewModel.Message() {
            @Override
            public void sendMessage(String message, String id , String date) {
                 if(id.equals(currentUserId)){

                   addMessageBox( message, 1 , date);
               }
               else{
                   addMessageBox( message, 2 , date);
               }
            }
        });


    }

    public void SendMessages(View view) {
        String messageText = binding.message.getText().toString();
        DateFormat df = new SimpleDateFormat("h:mm a");
        Date date1 = new Date();
         String currentTime =  df.format(date1);

        if(!messageText.equals("")){

            Map<String, String> map = new HashMap<String, String>();

            map.put("message", messageText);

            map.put("senderId", currentUserId);

            map.put("receiverId",list.get(0).getReceiverId());

            map.put("date",currentTime);

            model.addToTable(map);

            binding.message.setText("");
        }
    }

    public void addMessageBox(String message, int type , String date){

        TextView textView = new TextView(ChatView.this);

        textView.setText(message);

        TextView textView1 = new TextView(ChatView.this);

        textView1.setText(date);

        textView1.setTextSize(10);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);

        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        textView.setLayoutParams(lp2);

        binding.layout1.addView(textView);

        textView1.setLayoutParams(lp2);

        binding.layout1.addView(textView1);


        binding.scrollView.fullScroll(View.FOCUS_DOWN);
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
