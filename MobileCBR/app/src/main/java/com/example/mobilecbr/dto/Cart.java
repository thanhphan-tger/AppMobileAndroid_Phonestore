package com.example.mobilecbr.dto;

public class Cart {
    private int _proid, _price, _quantity;

    private String _name, _image;

    public int get_proid() {
        return _proid;
    }

    public void set_proid(int _proid) {
        this._proid = _proid;
    }

    public int get_price() {
        return _price;
    }

    public void set_price(int _price) {
        this._price = _price;
    }

    public int get_quantity() {
        return _quantity;
    }

    public void set_quantity(int _quantity) {
        this._quantity = _quantity;
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

    public Cart() {
    }

    public Cart(int _proid, String _name, int _price, String _image, int _quantity) {
        this._proid = _proid;
        this._price = _price;
        this._quantity = _quantity;
        this._name = _name;
        this._image = _image;
    }
}
