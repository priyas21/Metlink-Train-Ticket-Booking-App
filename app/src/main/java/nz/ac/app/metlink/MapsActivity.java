package nz.ac.app.metlink;

import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    //26.	https://www.youtube.com/watch?v=dr0zEmuDuIk (Maps current location and address)
   // 27.	https://www.youtube.com/watch?v=J3R4b-KauuI (Maps zoom)

    public void onSearch(View view)
{
    EditText location_txt =(EditText)findViewById(R.id.TFaddress);
    String location=location_txt.getText().toString();
    List<android.location.Address> addressList=null;
    if(location!=null || !location.equals(""))
    {
        Geocoder geocoder=new Geocoder(this);
        try {
            addressList= geocoder.getFromLocationName(location,1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        android.location.Address address=addressList.get(0);
        LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title(location));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng)) ;
    }
}
    public void changeType(View view)
    {
        if(mMap.getMapType()==GoogleMap.MAP_TYPE_NORMAL)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }
    public void onZoom(View view)
    {
        if(view.getId()==R.id.Bzoomin)
        {
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if(view.getId()==R.id.Bzoomout)
        {
        mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng porirua = new LatLng(-41.137462, 174.843421);
        mMap.addMarker(new MarkerOptions().position(porirua).title("Porirua Railway Station"));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(porirua));
    }
}
