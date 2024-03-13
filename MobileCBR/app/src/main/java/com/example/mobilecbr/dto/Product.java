package com.example.mobilecbr.dto;

import java.io.Serializable;

public class Product implements Serializable {
    private int _id;
    private int _catid;

    private int _price;

    private String _name, _image, _desc;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_catid() {
        return _catid;
    }

    public void set_catid(int _catid) {
        this._catid = _catid;
    }

    public int get_price() {
        return _price;
    }

    public void set_price(int _price) {
        this._price = _price;
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

    public String get_desc() {
        return _desc;
    }

    public void set_desc(String _desc) {
        this._desc = _desc;
    }

    public Product() {
    }

    public Product(String _name, int _price, String _image, String _desc, int _catid) {
        this._name = _name;
        this._price = _price;
        this._image = _image;
        this._desc = _desc;
        this._catid = _catid;
    }
}
