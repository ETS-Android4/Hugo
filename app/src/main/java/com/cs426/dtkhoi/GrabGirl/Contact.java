package com.cs426.dtkhoi.GrabGirl;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Contact implements Serializable{

    private int _avatar_id;
    private String _name;
    private int _age;
    private String _phoneNumber;
    private double _distance;
    private String _product;
    private String _location;

    // Constructor
    Contact(int avatar_id, String name, int age, String phone, double distance, String product, String location)
    {
        this._avatar_id = avatar_id;
        this._name = name;
        this._age = age;
        this._phoneNumber = phone;
        this._distance = distance;
        this._product = product;
        this._location = location;
    }

    // Getter
    public int get_avatar_id() {
        return _avatar_id;
    }

    public String get_name() {
        return _name;
    }

    public int get_age() {
        return _age;
    }

    public String get_phoneNumber() {
        return _phoneNumber;
    }

    public double get_distance() {
        return _distance;
    }

    public String get_product() {
        return _product;
    }

    public LatLng get_LatLng()
    {
        switch(_location)
        {
            case "Quận 12":
                return new LatLng(10.863070, 106.653982);
            case "Quận 7":
                return new LatLng(10.734034, 106.721579);
            case "Quận 2":
                return new LatLng(10.787273, 106.749810);
            case "Quận 8":
                return new LatLng(10.745731, 106.687956);

//            case "CGV Su Van Hanh":
//                return new LatLng(10.770513, 106.669826);
//            case "Toong Co-Working Space":
//                return new LatLng(10.777904, 106.692987);
            default:
                return null;
        }
    }
}