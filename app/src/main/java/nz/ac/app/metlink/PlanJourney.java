/*
package nz.ac.app.metlink;
import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class PlanJourney extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap  gMap;
    private GoogleApiClient client;
    Marker marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (googleServicesAvaiabale())
        {
            Toast.makeText(this,"All good to go...", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_main);
            iniMap();
        }
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void iniMap()
    {
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public  boolean googleServicesAvaiabale()
    {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvaiable = api.isGooglePlayServicesAvailable(this);
        if  ( isAvaiable == ConnectionResult.SUCCESS)
        {
            return true;
        }
        else if (api.isUserResolvableError(isAvaiable))
        {
            Dialog dialog =api.getErrorDialog(this,isAvaiable,0);
            dialog.show();
        }
        else
        {
            Toast.makeText(this,"Cant connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        if (gMap != null) {
            gMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    View v = getLayoutInflater().inflate(R.layout.info_window,null);

                    TextView tvLocality = (TextView) v.findViewById(R.id.tv_location);
                    TextView tvLat = (TextView)v.findViewById(R.id.tv_lat);
                    TextView tvLng = (TextView)v.findViewById(R.id.tv_lng);

                    LatLng ll = marker.getPosition();
                    tvLocality.setText("You looking @ : "+ marker.getTitle());

                    tvLat.setText("Latitude : " + ll.latitude);
                    tvLng.setText("Longitude : " + ll.longitude);

                    return v;
                }
            })  ;
        }
        goToLocationZoom(-33.824209, 151.233951,10);
    }
    private void goToLocation (double lat, double lng)
    {
        LatLng ll = new LatLng(lat,lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        gMap.moveCamera(update);


    }
    private void goToLocationZoom (double lat, double lng, int zoom)
    {
        LatLng ll = new LatLng(lat,lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,zoom);
        gMap.moveCamera(update);

    }

    public void goToLocation(View view) {
        EditText location = (EditText) findViewById(R.id.eTGoLocation);
        try {
            String Location = location.getText().toString();

            Geocoder gc = new Geocoder(this);
            List<Address> list = gc.getFromLocationName(Location, 1);

            android.location.Address address = list.get(0);

            String locality = address.getLocality();

            Toast.makeText(this, "Changing location to : " + locality, Toast.LENGTH_LONG).show();
            double lat = address.getLatitude();
            double lng = address.getLongitude();

            goToLocationZoom(lat, lng, 15);

            */
/*
            if (marker !=null) {
                marker.remove();
            }
            return new MarkerOptions()
                    .title(locality)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                    .position(new LatLng(lat,lng))
                    .snippet("I am here ");
            *//*

            //MarkerOptions option = seMarkerOptions(locality, lat, lng);

            //gMap.addMarker(option);
            // marker = gMap.addMarker(option);

            MarkerOptions option = seMarkerOptions(locality, lat, lng);

            gMap.addMarker(option);
            marker = gMap.addMarker(option);

        } catch (Exception e) {
            Toast.makeText(this, "Error! Need location details", Toast.LENGTH_LONG).show();
        }
    }

    private MarkerOptions seMarkerOptions(String locality, double lat, double lng) {
        if (marker != null) {
            marker.remove();
        }
        return new MarkerOptions()
                .title(locality)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                .position(new LatLng(lat, lng))
                .snippet("I am here ");
    }


}
*/
