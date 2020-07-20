package com.hfad.dawrlygp.model;

public class BendingPosts {

    private String id ;

    private String postId ;

    private int postItemsId;

    private boolean seen ;

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public int getPostItemsId() {
        return postItemsId;
    }

    public void setPostItemsId(int postItemsId) {
        this.postItemsId = postItemsId;
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
}
