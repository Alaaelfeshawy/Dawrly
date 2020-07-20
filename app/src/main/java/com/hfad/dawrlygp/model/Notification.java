package com.hfad.dawrlygp.model;

public class Notification {

   private String notificationPostId ;
   private String postId;
   private String postType ;

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getNotificationPostId() {
        return notificationPostId;
    }

    public void setNotificationPostId(String notificationPostId) {
        this.notificationPostId = notificationPostId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
