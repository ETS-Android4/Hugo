package com.cs426.dtkhoi.GrabGirl;

import java.io.Serializable;

public class Keeper implements Serializable
{
    private int _keeper_id;
    private String _keeperName;
    private double  _keeperDistance;

    // Constructor
    Keeper(int avatar_id, String name,  double distance)
    {
        this._keeper_id = avatar_id;
        this._keeperName = name;
        this._keeperDistance = distance;
    }

    public int get_avatar_id() {
        return _keeper_id;
    }

    public String get_name() {
        return _keeperName;
    }

    public double get_distance() {
        return _keeperDistance;
    }
}
