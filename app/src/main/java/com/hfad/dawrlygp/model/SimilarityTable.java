package com.hfad.dawrlygp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SimilarityTable  implements Parcelable {

    public SimilarityTable() {
    }

    private String postId1;
    private String postId2;
   // private String userId1;
   // private String userId2;
    private String desc1;
    private String desc2;
    private String date1 ;
    private String date2 ;
    private String color1;
    private String color2;
    private String modelName1 ;
    private String modelName2 ;
    private String titleName1;
    private String titleName2;
    private String imageURL1;
    private String imageURL2;
    private double similarity;
    private int postItemsId;
    private String simId;
    private double imageSimilarity;
    private boolean isSeen;

    protected SimilarityTable(Parcel in) {
        postId1 = in.readString();
        postId2 = in.readString();
        desc1 = in.readString();
        desc2 = in.readString();
        date1 = in.readString();
        date2 = in.readString();
        color1 = in.readString();
        color2 = in.readString();
        modelName1 = in.readString();
        modelName2 = in.readString();
        titleName1 = in.readString();
        titleName2 = in.readString();
        imageURL1 = in.readString();
        imageURL2 = in.readString();
        similarity = in.readDouble();
        postItemsId = in.readInt();
        simId = in.readString();
        imageSimilarity = in.readDouble();
        isSeen = in.readByte() != 0;
    }

    public static final Creator<SimilarityTable> CREATOR = new Creator<SimilarityTable>() {
        @Override
        public SimilarityTable createFromParcel(Parcel in) {
            return new SimilarityTable(in);
        }

        @Override
        public SimilarityTable[] newArray(int size) {
            return new SimilarityTable[size];
        }
    };

    public double getImageSimilarity() {
        return imageSimilarity;
    }

    public void setImageSimilarity(double imageSimilarity) {
        this.imageSimilarity = imageSimilarity;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getColor1() {
        return color1;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public String getColor2() {
        return color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public String getModelName1() {
        return modelName1;
    }

    public void setModelName1(String modelName1) {
        this.modelName1 = modelName1;
    }

    public String getModelName2() {
        return modelName2;
    }

    public void setModelName2(String modelName2) {
        this.modelName2 = modelName2;
    }

    public String getTitleName1() {
        return titleName1;
    }

    public void setTitleName1(String titleName1) {
        this.titleName1 = titleName1;
    }

    public String getTitleName2() {
        return titleName2;
    }

    public void setTitleName2(String titleName2) {
        this.titleName2 = titleName2;
    }

    public String getSimId() {
        return simId;
    }

    public void setSimId(String simId) {
        this.simId = simId;
    }


    public int getPostItemsId() {
        return postItemsId;
    }

    public void setPostItemsId(int postItemsId) {
        this.postItemsId = postItemsId;
    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String dec1) {
        this.desc1 = dec1;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public String getPostId1() {
        return postId1;
    }

    public void setPostId1(String postId1) {
        this.postId1 = postId1;
    }

    public String getPostId2() {
        return postId2;
    }

    public void setPostId2(String postId2) {
        this.postId2 = postId2;
    }

    public String getImageURL1() {
        return imageURL1;
    }

    public void setImageURL1(String imageURL1) {
        this.imageURL1 = imageURL1;
    }

    public String getImageURL2() {
        return imageURL2;
    }

    public void setImageURL2(String imageURL2) {
        this.imageURL2 = imageURL2;
    }

    /*  public String getUserId1() {
        return userId1;
    }

    public void setUserId1(String userId1) {
        this.userId1 = userId1;
    }

    public String getUserId2() {
        return userId2;
    }

    public void setUserId2(String userId2) {
        this.userId2 = userId2;
    }*/

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    @NonNull
    @Override
    public String toString() {
        return  /*userId1 +"\n UserId2 : "+userId2+*/"\n Desc1 : "
                +desc1+"\n Desc2 : "+desc2+"\n PostId1 : "+postId1+"\n PostId2 : "+postId2
                +"\n Post Items Id "+postItemsId   +"\n Similarity "+similarity+
                "\n Color1 : "+color1+"\n Color2 : "+color2+"\n title : "+titleName1+"\n title2 : "+titleName2
                +"\n modelName1 "+modelName1   +"\n ModelNam2 "+modelName2;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SimilarityTable similarityTable = (SimilarityTable) obj;

        return /*userId1.equals(similarityTable.userId1) &&
                userId2.equals(similarityTable.userId2) &&*/
                postId1.equals(similarityTable.postId1) &&
                postId2.equals(similarityTable.postId2) &&
                desc1 .equals(similarityTable.desc1)&&
                desc2.equals(similarityTable.desc2) &&
                postItemsId == similarityTable.postItemsId;

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(postId1);
        dest.writeString(postId2);
        dest.writeString(desc1);
        dest.writeString(desc2);
        dest.writeString(date1);
        dest.writeString(date2);
        dest.writeString(color1);
        dest.writeString(color2);
        dest.writeString(modelName1);
        dest.writeString(modelName2);
        dest.writeString(titleName1);
        dest.writeString(titleName2);
        dest.writeString(imageURL1);
        dest.writeString(imageURL2);
        dest.writeDouble(similarity);
        dest.writeInt(postItemsId);
        dest.writeString(simId);
        dest.writeDouble(imageSimilarity);
        dest.writeByte((byte) (isSeen ? 1 : 0));
    }
}
