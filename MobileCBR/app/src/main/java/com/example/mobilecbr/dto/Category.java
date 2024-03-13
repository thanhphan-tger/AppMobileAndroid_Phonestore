package com.example.mobilecbr.dto;

import java.io.Serializable;

public class Category {
    private int _id;
    private String _name;

    private String _image;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }

    public Category(int _id, String _name, String _image) {
        this._id = _id;
        this._name = _name;
        this._image = _image;
    }
}
