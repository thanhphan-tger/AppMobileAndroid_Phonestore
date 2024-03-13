package com.example.mobilecbr.utils;

public class API {
    public static String localhost = "172.17.15.217";
    public static String urlCategory = "http://" + localhost +"/android/getCategory.php";
    public static String urlAllProduct = "http://" + localhost +"/android/getAllProduct.php?catID=";
    public static String urlCheckUser = "http://" + localhost +"/android/checkUserLogin.php";
    public static String urlCheckEmail = "http://" + localhost +"/android/checkEmailExists.php?email=";
    public static String urlInsertUser = "http://" + localhost +"/android/insertUser.php";
    public static String urlInsertCart = "http://" + localhost +"/android/addCart.php";
    public static String urlAllProCart = "http://" + localhost +"/android/getAllProOfUser.php?userid=";
    public static String urlUpdateCart = "http://" + localhost +"/android/updateQuantity.php";
    public static String urlDeleteProCart = "http://" + localhost +"/android/removeProInCart.php";
    public static String urlUpdateUser = "http://" + localhost +"/android/updateUser.php";
    public static String urlInsertBill = "http://" + localhost +"/android/insertBill.php";
    public static String urlInsertBillDetail = "http://" + localhost +"/android/insertBillDetail.php";
}

