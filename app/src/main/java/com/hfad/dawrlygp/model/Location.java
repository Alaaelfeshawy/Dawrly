package com.hfad.dawrlygp.model;

public class Location {
    private String locationId ;
    private String sendBy;
    private String id ;
    private double lat ;
    private double lon ;
    private int postItemsId;

    public Location() {
    }

    public Location(double lat, double lon) {

        this.lat = lat;

        this.lon = lon;
    }

    public int getPostItemsId() {
        return postItemsId;
    }

    public void setPostItemsId(int postItemsId) {
        this.postItemsId = postItemsId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }
}
