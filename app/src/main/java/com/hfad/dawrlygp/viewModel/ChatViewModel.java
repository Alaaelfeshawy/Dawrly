package com.hfad.dawrlygp.viewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.dawrlygp.model.MessageInfo;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.repository.ChatRepository;
import com.hfad.dawrlygp.repository.SearchRepository;
import com.hfad.dawrlygp.views.Fragments.UserFragment.ChatFragment;

import java.util.List;
import java.util.Map;

public class ChatViewModel extends ViewModel {

   private MutableLiveData<List<MessageInfo>> data = new MediatorLiveData<>();

   private ChatRepository repository ;

   private DatabaseReference reference1  , reference2;

   private FirebaseAuth auth ;

   private Message message ;

   private Data data1 ;

   private Success success ;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void addToDB(String currentUserId , String userId){

        reference1 = FirebaseDatabase.getInstance().getReference("Message").child(currentUserId
        +" To " + userId);

        reference2 = FirebaseDatabase.getInstance().getReference("Message").child(userId+" To "+
                currentUserId);

        reference1.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

               Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

               String messageEt = map.get("message").toString();

               String id = map.get("senderId").toString();

               String date = map.get("date").toString();


               message.sendMessage(messageEt,id , date);

           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
   }

   public void addToTable(Map map){

       String messageId = reference1.push().getKey();

       map.put("messageId",messageId);

       map.put("seen",false);

       reference1.push().setValue(map);

       reference2.push().setValue(map);
   }

   public void seen(String user_Id , String currentUserId){

       Query query = FirebaseDatabase.getInstance()
               .getReference("Message")
               .child(currentUserId +" To " + user_Id);
       query.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists()){
                   for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                       snapshot.getRef().child("seen").setValue(true);

                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });


   }

    public void init(ChatFragment chatFragment){

        repository = ChatRepository.getInstance(chatFragment);

        data = repository.getData();


        repository.setSuccess(new ChatRepository.Success() {
            @Override
            public void setSuccess(boolean isSUccess) {

                success.setSuccess(isSUccess);
            }
        });

   }

    public String userId(){
        auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();
        Log.v("USER_ID",""+currentUserId);
        return currentUserId ;

    }


    public LiveData<List<MessageInfo>> getData() {
        return data;
    }

    public interface Message {
       public  void sendMessage(String message , String id , String date);
    }

    public interface Data {
        public void getData(String fname , String lname , String image );
    }
    public interface Success{
        public void setSuccess(boolean isSuccess);
    }

}
