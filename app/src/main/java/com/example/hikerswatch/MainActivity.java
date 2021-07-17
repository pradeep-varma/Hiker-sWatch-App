package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.location.LocationManager.GPS_PROVIDER;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startListening();

            }
        }
    }
    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(GPS_PROVIDER, 0, 0, locationListener);
        }
    }
    public void usersLocation(Location location){
        Log.i("Location",location.toString());
        TextView lattextView2=(TextView)findViewById(R.id.lattextView2);
        TextView longtextView2=(TextView)findViewById(R.id.longtextView2);
        TextView acctextView2=(TextView)findViewById(R.id.acctextView2);
        TextView alttextView2=(TextView)findViewById(R.id.alttextView2);
        TextView addtextView2=(TextView)findViewById(R.id.addtextView2);
        lattextView2.setText("Latitude:"+location.getLatitude());
        longtextView2.setText("Longitude:"+location.getLongitude());
        acctextView2.setText("Accuracy:"+location.getAccuracy());
        alttextView2.setText("Altitude:"+location.getAltitude());
        Geocoder geocoder=new Geocoder(getApplicationContext(),Locale.getDefault());
        try {
            String address ="Couldn't find address";
            List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addressList!=null && addressList.size()>0){
                Log.i("Placeinfo",addressList.get(0).toString());
                address="Address: \n";
                if(addressList.get(0).getSubThoroughfare()!=null){
                    address+=addressList.get(0).getSubThoroughfare()+" ";
                }
                if(addressList.get(0).getThoroughfare()!=null){
                    address+=addressList.get(0).getThoroughfare()+ "\n";
                }
                if(addressList.get(0).getLocality()!=null){
                    address+=addressList.get(0).getLocality()+ "\n";
                }
                if(addressList.get(0).getPostalCode()!=null){
                    address+=addressList.get(0).getPostalCode()+ "\n";
                }
                if(addressList.get(0).getCountryName()!=null){
                    address+=addressList.get(0).getCountryName()+ "\n";
                }
            }
            addtextView2.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                usersLocation(location);
            }
        };
        if(Build.VERSION.SDK_INT<23){
            startListening();
        }else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }
    }
}