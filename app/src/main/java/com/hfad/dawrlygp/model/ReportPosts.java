package com.hfad.dawrlygp.model;

public class ReportPosts {

    private String id ;

    private String postId ;

    private int postItemsId ;

    private String userId ;

    private boolean isSeen ;

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getPostItemsId() {
        return postItemsId;
    }

    public void setPostItemsId(int postItemsId) {
        this.postItemsId = postItemsId;
    }
}
