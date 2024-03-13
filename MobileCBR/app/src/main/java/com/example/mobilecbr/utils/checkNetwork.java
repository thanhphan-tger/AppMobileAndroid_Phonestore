package com.example.mobilecbr.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AlertDialog;

import com.example.mobilecbr.R;

public class checkNetwork {
    public static boolean haveNetwork(Context context)
    {
        boolean cnWifi = false;
        boolean cnMobile = false;

        ConnectivityManager cn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cn.getAllNetworkInfo();
        for(NetworkInfo net : netInfo)
        {
            if(net.getTypeName().equalsIgnoreCase("WIFI"))
            {
                if(net.isConnected())
                    cnWifi = true;
            }
            if(net.getTypeName().equalsIgnoreCase("MOBILE"))
            {
                if(net.isConnected())
                    cnMobile = true;
            }
        }
        return cnWifi || cnMobile;
    }

    public static void showReport(Context context, String mes)
    {
        new AlertDialog.Builder(context)
                .setMessage(mes)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }
}
