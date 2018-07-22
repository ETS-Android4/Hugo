package com.cs426.dtkhoi.GrabGirl;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs426.dtkhoi.GrabGirl.Modules.DirectionFinder;
import com.cs426.dtkhoi.GrabGirl.Modules.DirectionFinderListener;
import com.cs426.dtkhoi.GrabGirl.Modules.Route;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;

    MapWrapperLayout mapWrapperLayout;
    View customInfoWindow;

    Button btnSetlocation;
    Button btnSetDestination;

    InteractiveInfoWindowListener setAsLocationClick;
    InteractiveInfoWindowListener setAsDestinationClick;

    private Button btnFindPath;
    private EditText editTextOrigin;
    private EditText editTextDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    private ImageView myLocationBtn;

    private int PERMISSION_REQUEST_CODE = 604;

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LocationHelper.setRealLocation(new LatLng(location.getLatitude(), location.getLongitude()));
            Log.d("Location change", "Location Changed");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            Log.d("Location change", "Got result of permission");
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Objects.requireNonNull(locationManager).requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0,
                        locationListener
                );
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapWrapperLayout = findViewById(R.id.map_wrapper);

        initComponents();
    }

    private void initComponents() {
        btnFindPath = findViewById(R.id.button_find_path);
        editTextOrigin = findViewById(R.id.edit_text_origin);
        editTextDestination = findViewById(R.id.edit_text_destination);
        myLocationBtn = findViewById(R.id.locationBtn);

        LocationHelper.context = this;
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
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapWrapperLayout.init(mMap, this);
        RequestLocationPermission();

        final Contact contact = (Contact) getIntent().getSerializableExtra("ContactInfo");

        animToHerLocation();

        // FindPath onClick event
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        myLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAndDisplayLocation();
            }
        });

        customInfoWindow = LayoutInflater.from(this).inflate(R.layout.custom_info_window, null);
        btnSetlocation = customInfoWindow.findViewById(R.id.set_location_btn);
        btnSetDestination = customInfoWindow.findViewById(R.id.set_destination_btn);

        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        // Handle setAsLocation Button onClick
        setAsLocationClick = new InteractiveInfoWindowListener(btnSetlocation) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                Log.d("Set as Location", "Clicked");

                LatLng position = marker.getPosition();
                double latitude = position.latitude;
                double longitude = position.longitude;

                // Method 1: Use a string with format "Lat, Lng" as address, luckily it still works, but not recommended !
//                editTextOrigin.setText(latitude + ", " + longitude);

                // Method 2: Use Reverse Geocoder to convert Lat and Lng into real Address, highly recommended !
                String fullAdd = getCompleteAddress(latitude, longitude);
                editTextOrigin.setText(fullAdd);
            }
        };
        btnSetlocation.setOnTouchListener(setAsLocationClick);

        // Handle setAsDestination Button onClick
        setAsDestinationClick = new InteractiveInfoWindowListener(btnSetDestination) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                Log.d("Set as Destination", "Clicked");

                LatLng position = marker.getPosition();
                double latitude = position.latitude;
                double longitude = position.longitude;

                // Method 1: Use a string with format "Lat, Lng" as address, luckily it still works, but not recommended
//                editTextDestination.setText(latitude + ", " + longitude);

                // Method 2: Use Reverse Geocoder to convert Lat and Lng into Real Address string, highly recommended !
                String fullAdd = getCompleteAddress(latitude, longitude);
                editTextDestination.setText(fullAdd);
            }
        };
        btnSetDestination.setOnTouchListener(setAsDestinationClick);

        // Set InfoWindowAdapter
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                setAsLocationClick.setMarker(marker);
                setAsDestinationClick.setMarker(marker);

                TextView title = customInfoWindow.findViewById(R.id.title_text);
                TextView snippet = customInfoWindow.findViewById(R.id.snippet_text);

                title.setText(marker.getTitle());
                snippet.setText(marker.getSnippet());

                mapWrapperLayout.setMarkerWithInfoWindow(marker, customInfoWindow);
                return customInfoWindow;
            }
        });
    }

    private String getCompleteAddress(double latitude, double longitude) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
//                Log.w("Current address", strReturnedAddress.toString());
            } else {
//                Log.w("Current address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Current address", "Cannot get Address!");
        }
        return strAdd;
    }

    private void sendRequest() {
        String origin = editTextOrigin.getText().toString();
        String destination = editTextDestination.getText().toString();

        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter your location !", Toast.LENGTH_SHORT).show();
            return;
        }

        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter your destination !", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateAndDisplayLocation() {
        progressDialogUpdateLocation();

        LatLng curLocation = LocationHelper.getCurrentLocation();

        // Add a marker at my location
        mMap.addMarker(new MarkerOptions().position(curLocation).title("You are here !")).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(curLocation));
    }

    private void progressDialogUpdateLocation() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("God's Eye activated !");
        progress.setMessage("Searching for you ...");
        progress.show();

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progress.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 3000);
    }

    private void RequestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE
            );
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Log.d("Location change", "Else case");
            if (locationManager != null) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0,
                        locationListener
                );
            }
        }
    }

    private void animToHerLocation() {
        // Parse contact info from ContactAdapter
        Contact contact = (Contact) getIntent().getSerializableExtra("ContactInfo");

        // Retrieve LatLng object point to her location
        LatLng herLatLng = contact.get_LatLng();
        String herName = "Customer: " + contact.get_name();
        String herProduct = "Product: " + contact.get_product();

        // Add a marker at her location
        Marker herLocation = mMap.addMarker(new MarkerOptions()
                .position(herLatLng)
                .title(herName)
                .snippet(herProduct));

        herLocation.showInfoWindow();

        // Create an animation for map while moving to this location
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);

//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(herLatLng, 10);
//        mMap.animateCamera(cameraUpdate);

        // Set some features of map
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(herLatLng)      // Set the center of the map to HCMUS
                .zoom(18)                   // Set the zoom (1<= zoom <= 20)
                .bearing(90)                // Set the orientation of the camera to east
                .tilt(30)                   // Set the tilt of the camera to 30 degrees
                .build();

        // Do animation to move to this location
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Just a second", "Analyzing shortest path using Dijkstra ...", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.text_view_distance)).setText(route.distance.text);
            ((TextView) findViewById(R.id.text_view_time)).setText(route.duration.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));

            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions()
                    .geodesic(true)
                    .color(Color.BLUE)
                    .width(10);

            for (int i = 0; i < route.points.size(); i++) {
                polylineOptions.add(route.points.get(i));

            }

            polylinePaths.add(mMap.addPolyline(polylineOptions));

            getDistrict(route);
        }
    }

    private void getDistrict(Route route)
    {
        int a = route.points.size() / 50;
        ArrayList<String> setOfDistrict = new ArrayList<>();

        LatLng curLocation = LocationHelper.getCurrentLocation();

        Log.w("First point", getCompleteAddress(curLocation.latitude, curLocation.longitude));

        // Add district of first point and last point on the Polyline
        setOfDistrict.add(getDistrictFromlatLng(curLocation.latitude, curLocation.longitude));
        setOfDistrict.add(getDistrictOfPoint(route, route.points.size() - 1));

        for (int i = 0; i < route.points.size(); i++)
        {
            if (i % a ==  0)
            {
                String district = getDistrictOfPoint(route, i);

                if (!setOfDistrict.contains(district)) {
                    setOfDistrict.add(district);
                }
            }
        }

        Log.w("Set of District", Arrays.toString(setOfDistrict.toArray()));

        showSetOfDistrictDialog(setOfDistrict);
    }

    private void showSetOfDistrictDialog(ArrayList<String> setOfDistrict) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("You will cross these districts. Grab packages belong to them");
        progress.setMessage(Arrays.toString(setOfDistrict.toArray()));
        progress.show();

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progress.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 6000);
    }

    @Nullable
    private String getDistrictOfPoint(Route route, int pointIndex) {
        double latitude = route.points.get(pointIndex).latitude;
        double longitude = route.points.get(pointIndex).longitude;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String district = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0)
            {
                Address address = addressList.get(0);
                district = address.getSubAdminArea();//district
            }
        } catch (IOException e) {
            // Log.e(TAG, "Unable to connect to Geocoder", e);
        }
        return district;
    }

    @Nullable
    private String getDistrictFromlatLng(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String district = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                district = address.getSubAdminArea();//district
            }
        } catch (IOException e) {
            // Log.e(TAG, "Unable to connect to Geocoder", e);
        }
        return district;
    }
}
