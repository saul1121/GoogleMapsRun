package mapsrun.rs.android.com.googlemapsrun;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

public class MapsActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, OnMapReadyCallback, DirectionCallback {

    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;

    private String serverKeys = "AIzaSyAv67xTR71WWtLiLBrJyRBYsyGkG0LtciI";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private CheckBox mMyLocationEnabled;
    private CheckBox mCheckTraffic;
    private List<LatLng> allLats = new ArrayList<LatLng>();

    private Spinner mSpinner;

    private LatLng origin;

    private Marker mMarker2;
    private Marker mMarker3;
    private Marker mMarker4;

    private Location location;

    Address address;


    //Atlanta
    private static final LatLng MARKER2LL = new LatLng(33.716116, -83.857948);

    private static final LatLng MARKER3LL = new LatLng(33.662907, -84.650904);

    private static final LatLng MARKER4LL = new LatLng(33.687337, -84.388437);


    //New York
    private static final LatLng NYMARKER1 = new LatLng(40.767622, -73.965628);

    private static final LatLng NYMARKER2 = new LatLng(40.815968, -73.943312);

    private static final LatLng NYMARKER3 = new LatLng(40.731412, -73.861208);



    //Chicago
    private static final LatLng CHMARKER1 = new LatLng(41.784257, -87.708852);

    private static final LatLng CHMARKER2 = new LatLng(41.909064, -87.717092);

    private static final LatLng CHMARKER3 = new LatLng(41.681775, -87.675893);


    //Loas Angeles
    private static final LatLng LAMARKER1 = new LatLng(34.003862, -118.252867);

    private static final LatLng LAMARKER2 = new LatLng(34.065765, -118.413265);

    private static final LatLng LAMARKER3 = new LatLng(34.014878, -118.308319);


    //Texas
    private static final LatLng TXMARKER1 = new LatLng(30.276084, -97.737774);

    private static final LatLng TXMARKER2 = new LatLng(30.305187, -97.743056);

    private static final LatLng TXMARKER3 = new LatLng(30.271740, -97.754374);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mMyLocationEnabled = (CheckBox) findViewById(R.id.myLocation);
        mCheckTraffic = (CheckBox) findViewById(R.id.myTraffic);

        mSpinner = (Spinner) findViewById(R.id.mySpinner);
        SpinnerAdapter adapter = ArrayAdapter.createFromResource(this, R.array.layers, android.R.layout.simple_spinner_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);

        generateLats();


    }


    private void generateLats(){

        allLats.add(NYMARKER1);
        allLats.add(NYMARKER2);
        allLats.add(NYMARKER3);
        allLats.add(LAMARKER1);
        allLats.add(LAMARKER2);
        allLats.add(LAMARKER3);
        allLats.add(TXMARKER1);
        allLats.add(TXMARKER2);
        allLats.add(TXMARKER3);
        allLats.add(MARKER2LL);
        allLats.add(MARKER3LL);
        allLats.add(MARKER4LL);
        allLats.add(CHMARKER1);
        allLats.add(CHMARKER2);
        allLats.add(CHMARKER3);



    }

    public void requestDirection(View view){
        this.handlerDirection();
    }


    private void handlerDirection(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            mMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
             location= locationManager.getLastKnownLocation(provider);


            origin = new LatLng(location.getLatitude(), location.getLongitude());


        }else{
            mMap.setMyLocationEnabled(false);

            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, false);

        }


        GoogleDirection.withServerKey(serverKeys)
                .from(origin)
                .to(MARKER2LL)
                .transportMode(TransportMode.DRIVING)
                .execute(this);

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

        // Add a marker in Sydney and move the camera  33.905243,-84.4361039
        LatLng anyAddress = new LatLng(33.905243, -84.4361039);
        /*mMap.addMarker(new MarkerOptions()
                .position(anyAddress)
                .title("WELLLL"));
*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(anyAddress, 15));



        //addMarkers();
    }


    public void addMarkers(){

        mMarker2= mMap.addMarker(new MarkerOptions()
                        .position(MARKER2LL)
                        .title("Marti Grass")
                        .snippet("OPEN 24/7")

        );


        mMarker3= mMap.addMarker(new MarkerOptions()
                        .position(MARKER3LL)
                        .title("Marti Grass 3")
                        .snippet("OPEN EVEN CHRISTMAS")

        );


    }


    public void goToMardiGrass2(View view) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MARKER2LL, 14f));
    }


    public void goToMardiGrass3(View view) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MARKER3LL, 10f));
    }


    /*
    *
    * Check for permission in order to get my position
    *
    * */
    public void myPosition(View view) {


        if (!mMyLocationEnabled.isChecked()) {
            mMap.setMyLocationEnabled(false);
            return;
        }


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            mMap.setMyLocationEnabled(true);

        }else{
            mMap.setMyLocationEnabled(false);

            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, false);

        }


    }

    public void checkForTraffic(View view) {

        mMap.setTrafficEnabled(mCheckTraffic.isChecked());

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        updateMapLayer();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void updateMapLayer(){

        String layerName = ((String) mSpinner.getSelectedItem());
        if (layerName.equals("normal")) {
            mMap.setMapType(MAP_TYPE_NORMAL);
        } else if (layerName.equals("hybrid")) {
            mMap.setMapType(MAP_TYPE_HYBRID);


        } else if (layerName.equals("satellite")) {
            mMap.setMapType(MAP_TYPE_SATELLITE);
        } else if (layerName.equals("terrain")) {
            mMap.setMapType(MAP_TYPE_TERRAIN);
        } else if (layerName.equals("none_map")) {
            mMap.setMapType(MAP_TYPE_NONE);
        } else {
            Log.i("LDA", "Error setting layer with name " + layerName);
        }

    }


    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {

        if (direction.isOK()) {
            mMap.addMarker(new MarkerOptions().position(origin));
            mMap.addMarker(new MarkerOptions().position(MARKER2LL));

            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));

            //btnRequestDirection.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDirectionFailure(Throwable t) {


        Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();

    }

    public void searchZip(View view) {

        EditText edit = (EditText) findViewById(R.id.searchZipId);

        String zipCode = edit.getText().toString();

        List<Address> result ;
        Locale current = getResources().getConfiguration().locale;
        Geocoder coder = new Geocoder(this, current);

        try {

          result =  coder.getFromLocationName(zipCode,3);

            if( result!= null && result.size()>0 ){

                Toast.makeText(this, "SIZE : "+result.size(), Toast.LENGTH_SHORT ).show();
                address = result.get(0);

            }
            searchForClosest();

        }catch(IOException ioe){

            Toast.makeText(this, ioe.getMessage(), Toast.LENGTH_LONG ).show();

        }
    }


    public HashMap<LatLng, Float> searchForClosest(){

        double latOrigen = address.getLatitude();
        double lonOrigen = address.getLongitude();
        mMap.clear();
        Hashtable<LatLng, Float> myClosest = new Hashtable<LatLng, Float>();

        for (LatLng cities : allLats){
            myClosest.put(cities, computeDistance(latOrigen, lonOrigen, cities.latitude, cities.longitude));
        }

        //order
        //HashMap orderMap =  sortByValue(myClosest);

        drawMarker((HashMap)sortByValue(myClosest));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), 19));

        return null;
    }


    public void drawMarker(HashMap<LatLng, Float> places){

        int i =0;
        LatLng tmp = new LatLng(1,1);

        if(places!= null){
            Iterator iter = places.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry pair = (Map.Entry)iter.next();
            tmp = (LatLng)pair.getKey();
            mMap.addMarker(new MarkerOptions().position(tmp));
            if(i>=2)break;
        }
        }
        //mMap.addMarker(places)
        drawDirecion(tmp);
    }


    public void drawDirecion(LatLng destini){

        GoogleDirection.withServerKey(serverKeys)
                .from(new LatLng(address.getLatitude(), address.getLongitude()))
                .to(destini)
                .transportMode(TransportMode.DRIVING)
                .execute(this);

    }


    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<>( map.entrySet() );
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    public float computeDistance(double origenLat, double origenLon, double destinoLat, double destinoLon){

        Location loc1 = new Location("");
        loc1.setLatitude(origenLat);
        loc1.setLongitude(origenLon);

        Location loc2 = new Location("");
        loc2.setLatitude(destinoLat);
        loc2.setLongitude(destinoLon);

        return loc1.distanceTo(loc2);
    }

    public void drawMarkers(Marker... markerses){

    }



}
