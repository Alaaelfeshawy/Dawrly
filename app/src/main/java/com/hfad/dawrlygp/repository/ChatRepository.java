package com.hfad.dawrlygp.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.dawrlygp.model.Message;
import com.hfad.dawrlygp.model.MessageInfo;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.views.Fragments.UserFragment.ChatFragment;
import com.hfad.dawrlygp.views.Helpers.DataCalled;

import java.util.ArrayList;
import java.util.List;

public class ChatRepository {

    public static ChatRepository instance ;

    private FirebaseAuth auth ;

    private static ChatFragment mContext ;

    private static DataCalled dataCalled ;

    private List<MessageInfo> names = new ArrayList<>();

    private Data data ;

    private MessageInfo messageInfo;

    private Success success ;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static ChatRepository getInstance(ChatFragment context){
        mContext = context ;
        if (instance == null){
            instance = new ChatRepository();
        }
        dataCalled = (DataCalled) mContext;

        return instance ;
    }

    public MutableLiveData getData(){
        setNames();
        MutableLiveData<List<MessageInfo>> liveData = new MediatorLiveData<>();
        liveData.setValue(names);
        return liveData ;
    }

    private void setNames() {

        names.clear();

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){

            final String currentUserId = auth.getCurrentUser().getUid();

            Query query = FirebaseDatabase.getInstance()
                    .getReference("User");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){

                            final User user = snapshot1.getValue(User.class);

                            final String fname = user.getFname();

                            final String lname = user.getLname();

                            final String image = user.getImageId();

                            final String userId = user.getUser_id();

                            Log.v("ChatRepo","1");
                            Query query1 = FirebaseDatabase.getInstance()
                                    .getReference("Message")
                                    .child(currentUserId +" To " + userId);

                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                            Message message = snapshot.getValue(Message.class);

                                            final String senderId = message.getSenderId();

                                            if (senderId.equals(currentUserId)) {

                                                snapshot.getRef().child("seen").setValue(true);
                                            }


                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            Query query = FirebaseDatabase.getInstance()
                                            .getReference("Message")
                                            .child(currentUserId +" To " + userId)
                                            .orderByKey()
                                            .limitToLast(1);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                            Message message = snapshot.getValue(Message.class);

                                            final String lastMessage = message.getMessage();

                                            Log.v("ChatRepo","1");

                                            messageInfo = new MessageInfo();

                                            messageInfo.setFname(fname);

                                            messageInfo.setLname(lname);

                                            messageInfo.setImage(image);

                                            messageInfo.setLastMessage(lastMessage);

                                            messageInfo.setReceiverId(userId);

                                            messageInfo.setSenderId(message.getSenderId());

                                            messageInfo.setSeen(message.isSeen());

                                            messageInfo.setDate(message.getDate());

                                            names.add(messageInfo);

                                            success.setSuccess(true);

                                       }
                                        dataCalled.onDataCalled();

                                    }

                                    else {
                                        success.setSuccess(false);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        }



    }
    public interface Data {
        public void getData(String fname , String lname , String image);
    }

    public interface Success{
        public void setSuccess(boolean isSUccess);
    }
}
