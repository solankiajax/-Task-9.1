package com.example.lostandfound;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.lostandfound.data.DatabaseHelper;
import com.example.lostandfound.model.Item;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DisplayMap extends FragmentActivity implements OnMapReadyCallback {
    List<Item> itemList;
    List<String> addresses;
    DatabaseHelper db;
    GoogleMap map;
    List<LatLng> latLngList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map);
        try{
            itemList = new ArrayList<Item>();
            addresses = new ArrayList<String>();
           latLngList = new ArrayList<LatLng>();
            db = new DatabaseHelper(DisplayMap.this);

            MapFragment mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
            mapFragment.getMapAsync(this);


            itemList = db.fetchAllItems();

            if(itemList != null)
            {
                for(int i =0;i<itemList.size();i++){
                    addresses.add(itemList.get(i).getLocation());
                }
                for(int i =0;i<itemList.size();i++){
                    latLngList.add(getLocationFromAddress(addresses.get(i)));
                }
            }
            else
            {
                addresses = null;
            }
        }catch (Exception e){
            Log.d("reached oc",e.getMessage());
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try{
            map = googleMap;

            for (int i = 0; i < latLngList.size(); i++) {

                // below line is use to add marker to each location of our array list.
                map.addMarker(new MarkerOptions().position(latLngList.get(i)).title("Marker"));

                // below lin is use to zoom our camera on map.
                map.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

                // below line is use to move our camera to the specific location.
                map.moveCamera(CameraUpdateFactory.newLatLng(latLngList.get(i)));
            }
        }catch (Exception e){
            Log.d("reached omr",e.getMessage());
        }


    }

    public LatLng getLocationFromAddress(String strAddress) {
        try {
            Geocoder coder = new Geocoder(this);
            List<Address> address;
            GeoPoint p1 = null;

            try {
                address = coder.getFromLocationName(strAddress, 5);
                if (address == null) {
                    return null;
                }
                Address location = address.get(0);

                return new LatLng(location.getLatitude(), location.getLongitude());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }catch (Exception e){
            Log.d("reached glfa",e.getMessage());
            return null;
        }

    }


}