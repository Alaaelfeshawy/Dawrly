package com.hfad.dawrlygp.model;

public class Options {

   private String name ;

   private int image ;

    public Options( int image) {
        this.image = image;
    }

    public Options(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public int getImage() {

        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public Options() {
    }

    public Options(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
