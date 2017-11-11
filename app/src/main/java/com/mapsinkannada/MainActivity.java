package com.mapsinkannada;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

//testing
public class MainActivity extends AppCompatActivity {

    Button toMapBtn;
    LatLng source, destination;
    PlaceAutocompleteFragment autocompleteFragment;
    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toMapBtn = (Button) findViewById(R.id.toMapBtn);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        toMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("source", source);
                intent.putExtra("destination", destination);
                startActivity(intent);
            }
        });

        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Toast.makeText(MainActivity.this, "Current locaiton: " + location.toString(), Toast.LENGTH_SHORT).show();
                                source = new LatLng(location.getLatitude(), location.getLongitude());
                            }
                        }
                    });
        }
        catch (SecurityException e){
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                destination = place.getLatLng();
                Toast.makeText(MainActivity.this, "Selected: " + place.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Toast.makeText(MainActivity.this, "Error: " + status.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
