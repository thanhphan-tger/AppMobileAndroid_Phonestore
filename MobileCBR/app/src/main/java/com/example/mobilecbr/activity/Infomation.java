package com.example.mobilecbr.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.mobilecbr.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mobilecbr.databinding.ActivityInfomationBinding;

public class Infomation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityInfomationBinding binding;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityInfomationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = (Toolbar) findViewById(R.id.toolbarInfo);
        actionBar();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.baseline_keyboard_backspace_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng MobileStoreCBR = new LatLng(10.807264933059122, 106.6240542288045);
        mMap.addMarker(new MarkerOptions().position(MobileStoreCBR).title("Mobile Store CBR").snippet("240, Lê Trọng Tấn, Tây Thạnh, Tân Phú, Thành phố Hồ Chí Minh"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MobileStoreCBR));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(MobileStoreCBR).zoom(200).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}