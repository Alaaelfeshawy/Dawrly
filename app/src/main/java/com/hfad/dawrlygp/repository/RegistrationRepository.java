package com.hfad.dawrlygp.repository;

public class RegistrationRepository {

    public static RegistrationRepository instance ;

    public static RegistrationRepository getInstance(){
        if (instance == null){
            instance = new RegistrationRepository();
        }
        return instance;
    }


    }



