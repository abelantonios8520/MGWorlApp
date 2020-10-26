package com.abelsalcedo.mgworlapp.activities.colaborador;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.abelsalcedo.mgworlapp.R;
import com.abelsalcedo.mgworlapp.activities.MainActivity;
import com.abelsalcedo.mgworlapp.includes.MyToolbar;
import com.abelsalcedo.mgworlapp.providers.AuthProvider;
import com.abelsalcedo.mgworlapp.providers.GeofireProvider;
import com.abelsalcedo.mgworlapp.providers.TokenProvider;

import java.util.ArrayList;
import java.util.List;

public class MapColaboradorActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private AuthProvider mAuthProvider;
    private GeofireProvider mGeofireProvider;
    private TokenProvider mTokenProvider;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;
    private List<Marker> mClienteMarkers = new ArrayList<>();
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;

    private boolean mIsFirstTime = true;
    private Marker mMarker;

    private Button mButtonConnect;
    private boolean mIsConnect = false;

    private LatLng mCurrentLatLng;

    private ValueEventListener mListener;


    LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {

                    mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                    if (mMarker != null) {
                        mMarker.remove();
                    }

                    mMarker = mMap.addMarker(new MarkerOptions().position(
                            new LatLng(location.getLatitude(), location.getLongitude())
                            )
                                    .title("Tu posicion")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_pin_red))
                    );
                    // OBTENER LA LOCALIZACION DEL USUARIO EN TIEMPO REAL
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(16f)
                                    .build()
                    ));

                    updateLocation();
                    Log.d("ENTRO", "ACTUALIZANDO POSICION");

                    if (mIsFirstTime) {
                        mIsFirstTime = false;
//                        getActiveClientes();
                    }
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_colaborador);
        MyToolbar.show(this, "Bienvenido emprendedor", false);

        mAuthProvider = new AuthProvider();
        mGeofireProvider = new GeofireProvider("active_clientes");
        mTokenProvider = new TokenProvider();
        //mbtnChat = findViewById(R.id.imgBtnChat);
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        mButtonConnect = findViewById(R.id.btnConnect);
        mButtonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsConnect) {
                    disconnect();
                } else {
                    startLocation();
                }
            }
        });


        generateToken();
        isColaboradorWorking();

    }

//    private void chat() {
//        Intent intent = new Intent(MapColaboradorActivity.this, FirebaseChat.class);
//        startActivity(intent);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFusedLocation != null && mLocationCallback != null) {
            mFusedLocation.removeLocationUpdates(mLocationCallback);
        }
        if (mListener != null) {
            if (mAuthProvider.existSession()) {
                mGeofireProvider.isColaboradorWorking(mAuthProvider.getId()).removeEventListener(mListener);
            }
        }
    }

    private void isColaboradorWorking() {
        mListener = mGeofireProvider.isColaboradorWorking(mAuthProvider.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    disconnect();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateLocation() {
        if (mAuthProvider.existSession() && mCurrentLatLng != null) {
            mGeofireProvider.saveLocation(mAuthProvider.getId(), mCurrentLatLng);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(false);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (gpsActived()) {
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    } else {
                        showAlertDialogNOGPS();
                    }
                } else {
                    checkLocationPermissions();
                }
            } else {
                checkLocationPermissions();
            }
        }
    }

//            ============== Inicio ==================
//    private void getActiveClientes() {
//        mGeofireProvider.getActiveClientes(mCurrentLatLng, 10).addGeoQueryEventListener(new GeoQueryEventListener() {
//            @Override
//            public void onKeyEntered(String key, GeoLocation location) {
//                // AÑADIREMOS LOS MARCADORES DE LOS EMPRENDEDORES QUE SE CONECTEN EN LA APLICACION
//
//                for (Marker marker2 : mClienteMarkers) {
//                    if (marker2.getTag() != null) {
//                        if (marker2.getTag().equals(key)) {
//                            return;
//                        }
//                    }
//                }
//
//                LatLng clienteLatLng = new LatLng(location.latitude, location.longitude);
//                Marker marker2 = mMap.addMarker(new MarkerOptions().position(clienteLatLng).title("Tu pedido es: ").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_my_location)));
//                marker2.setTag(key);
//                mClienteMarkers.add(marker2);
//            }
//
//            @Override
//            public void onKeyExited(String key) {
//                for (Marker marker2 : mClienteMarkers) {
//                    if (marker2.getTag() != null) {
//                        if (marker2.getTag().equals(key)) {
//                            marker2.remove();
//                            mClienteMarkers.remove(marker2);
//                            return;
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onKeyMoved(String key, GeoLocation location) {
//                // ACTUALIZAR LA POSICION DE CADA EMPRENDEDOR
//                for (Marker marker2 : mClienteMarkers) {
//                    if (marker2.getTag() != null) {
//                        if (marker2.getTag().equals(key)) {
//                            marker2.setPosition(new LatLng(location.latitude, location.longitude));
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onGeoQueryReady() {
//
//            }
//
//            @Override
//            public void onGeoQueryError(DatabaseError error) {
//
//            }
//        });
//    }
//======    Final de Clientes=========

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && gpsActived()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
        else {
            showAlertDialogNOGPS();
        }
    }

    private void showAlertDialogNOGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor activa tu ubicacion para continuar con MGWorld")
                .setPositiveButton("Configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }

    private boolean gpsActived() {
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isActive = true;
        }
        return isActive;
    }

    private void disconnect() {

        if (mFusedLocation != null) {
            mButtonConnect.setText("Conectarse");
            mIsConnect = false;
            mFusedLocation.removeLocationUpdates(mLocationCallback);
            if (mAuthProvider.existSession()) {
                mGeofireProvider.removeLocation(mAuthProvider.getId());
            }
        }
        else {
            Toast.makeText(this, "No te puedes desconectar con MGWORLD", Toast.LENGTH_SHORT).show();
        }
    }

    private void startLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActived()) {
                    mButtonConnect.setText("Desconectarse");
                    mIsConnect = true;
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                }
                else {
                    showAlertDialogNOGPS();
                }
            }
            else {
                checkLocationPermissions();
            }
        } else {
            if (gpsActived()) {
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }
            else {
                showAlertDialogNOGPS();
            }
        }
    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Proporciona los permisos para continuar con MWorld")
                        .setMessage("Esta aplicacion requiere de los permisos de ubicacion para poder utilizarse")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapColaboradorActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            }
            else {
                ActivityCompat.requestPermissions(MapColaboradorActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.colab_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(MapColaboradorActivity.this, buscarClientesActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.action_logout) {
            logout();
        }
        if (item.getItemId() == R.id.action_update) {
            Intent intent = new Intent(MapColaboradorActivity.this, UpdateProfileColaboradorActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.action_history) {
            Intent intent = new Intent(MapColaboradorActivity.this, HistoryBookingColaboradorActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    void logout() {
        disconnect();
        mAuthProvider.logout();
        Intent intent = new Intent(MapColaboradorActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    void generateToken() {
        mTokenProvider.create(mAuthProvider.getId());
    }
}
//package com.abelsalcedo.mgworlapp.activities.colaborador;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.location.LocationManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Looper;
//import android.provider.Settings;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.abelsalcedo.mgworlapp.activities.colaborador.NotificationBookingActivity;
//import com.abelsalcedo.mgworlapp.providers.ColaboradorProvider;
//import com.abelsalcedo.mgworlapp.providers.GeofireProviderColaborador;
//import com.abelsalcedo.mgworlapp.providers.TokenProvider;
//import com.firebase.geofire.GeoLocation;
//import com.firebase.geofire.GeoQueryEventListener;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.libraries.places.api.Places;
//import com.google.android.libraries.places.api.model.Place;
//import com.google.android.libraries.places.api.model.RectangularBounds;
//import com.google.android.libraries.places.api.net.PlacesClient;
//import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
//import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
//import com.google.firebase.database.DatabaseError;
//import com.abelsalcedo.mgworlapp.R;
//import com.abelsalcedo.mgworlapp.activities.MainActivity;
//import com.abelsalcedo.mgworlapp.includes.MyToolbar;
//import com.abelsalcedo.mgworlapp.providers.AuthProvider;
//import com.abelsalcedo.mgworlapp.providers.GeofireProvider;
//import com.google.maps.android.SphericalUtil;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import com.abelsalcedo.mgworlapp.activities.cliente.PedidoActivity;
//
//public class MapColaboradorActivity extends AppCompatActivity implements OnMapReadyCallback {
//
//    private GoogleMap mMap;
//    private SupportMapFragment mMapFragment;
//    private AuthProvider mAuthProvider;
//    ColaboradorProvider mColaboradorProvider;
//
//    private LocationRequest mLocationRequest;
//    private FusedLocationProviderClient mFusedLocation;
//
//    private GeofireProviderColaborador mGeofireProviderColaborador;
//    private TokenProvider mTokenProvider;
//
//    private final static int LOCATION_REQUEST_CODE = 1;
//    private final static int SETTINGS_REQUEST_CODE = 2;
//
//    private Marker mMarker;
//
//    private LatLng mCurrentLatLng;
//
//    private List<Marker> mClienteMarkers = new ArrayList<>();
//
//    private boolean mIsFirstTime = true;
//
//    private PlacesClient mPlaces;
//    private AutocompleteSupportFragment mAutocomplete;
//    private AutocompleteSupportFragment mAutocompleteDestination;
//
//    private String mOrigin;
//    private LatLng mOriginLatLng;
//
//    private String mDestination;
//    private LatLng mDestinationLatLng;
//
//    private String mExtraColabId;
//
//    private GoogleMap.OnCameraIdleListener mCameraListener;
//
//    private Button mConect;
//    private String mPedir = PedidoActivity.mPedir;
//
//    LocationCallback mLocationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            for (Location location : locationResult.getLocations()) {
//                if (getApplicationContext() != null) {
//
//                    mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//
//                    if (mMarker != null) {
//                        mMarker.remove();
//                    }
//
//                    mMarker = mMap.addMarker(new MarkerOptions().position(
//                            new LatLng(location.getLatitude(), location.getLongitude())
//                            )
//                                    .title("Tu posicion")
//                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_pin_red))
//                    );
//                    // OBTENER LA LOCALIZACION DEL USUARIO EN TIEMPO REAL
//                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
//                            new CameraPosition.Builder()
//                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
//                                    .zoom(16f)
//                                    .build()
//                    ));
//
////                    updateLocation();
//                    Log.d("ENTRO", "ACTUALIZANDO POSICION");
//
//                    if (mIsFirstTime) {
//                        mIsFirstTime = false;
//                        getActiveClientes();
//                    }
//                }
//            }
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map_colaborador);
//        MyToolbar.show(this, "Bienvenido colaborador", false);
//
//        mAuthProvider = new AuthProvider();
//        mGeofireProviderColaborador = new GeofireProviderColaborador("active_clientes");
//        mTokenProvider = new TokenProvider();
//        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
//        mColaboradorProvider = new ColaboradorProvider();
//
//        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mMapFragment.getMapAsync(this);
//        mConect = findViewById(R.id.btnConnect);
//        mExtraColabId = getIntent().getStringExtra("Colaboradores");
//
//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));
//        }
//
//        mPlaces = Places.createClient(this);
////        instanceAutocompleteOrigin();
////        instanceAutocompleteDestination();
//        onCameraMove();
//
//        mConect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                requestColaborador();
//            }
//        });
//
//        generateToken();
//
//
//    }
//
//
//    private void requestColaborador() {
//
//        if (mOriginLatLng != null && mDestinationLatLng != null) {
//            Intent intent = new Intent(MapColaboradorActivity.this, NotificationBookingActivity.class);
//            intent.putExtra("origin_lat", mOriginLatLng.latitude);
//            intent.putExtra("origin_lng", mOriginLatLng.longitude);
//            intent.putExtra("destination_lat", mDestinationLatLng.latitude);
//            intent.putExtra("destination_lng", mDestinationLatLng.longitude);
//            intent.putExtra("origin", mOrigin);
//            intent.putExtra("destination", mDestination);
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "Debe seleccionar el lugar de recogida y el destino", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
////    private void limitSearch() {
////        LatLng northSide = SphericalUtil.computeOffset(mCurrentLatLng, 5000, 0);
////        LatLng southSide = SphericalUtil.computeOffset(mCurrentLatLng, 5000, 180);
////        mAutocomplete.setCountry("PER");
////        mAutocomplete.setLocationBias(RectangularBounds.newInstance(southSide, northSide));
////        mAutocompleteDestination.setCountry("PER");
////        mAutocompleteDestination.setLocationBias(RectangularBounds.newInstance(southSide, northSide));
////    }
//
//    private void onCameraMove() {
//        mCameraListener = new GoogleMap.OnCameraIdleListener() {
//            @Override
//            public void onCameraIdle() {
//                try {
//                    Geocoder geocoder = new Geocoder(MapColaboradorActivity.this);
//                    mOriginLatLng = mMap.getCameraPosition().target;
//                    List<Address> addressList = geocoder.getFromLocation(mOriginLatLng.latitude, mOriginLatLng.longitude, 1);
//                    String city = addressList.get(0).getLocality();
//                    String country = addressList.get(0).getCountryName();
//                    String address = addressList.get(0).getAddressLine(0);
//                    mOrigin = address + " " + city;
//                    mAutocomplete.setText(address + " " + city);
//                } catch (Exception e) {
//                    Log.d("Error: ", "Mensaje error: " + e.getMessage());
//                }
//            }
//        };
//    }
//
////    private void instanceAutocompleteOrigin() {
////        mAutocomplete = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.placeAutocompleteOrigin);
////        mAutocomplete.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
////        mAutocomplete.setHint("Lugar de recogida");
////        mAutocomplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
////            @Override
////            public void onPlaceSelected(@NonNull Place place) {
////                mOrigin = place.getName();
////                mOriginLatLng = place.getLatLng();
////                Log.d("PLACE", "Name: " + mOrigin);
////                Log.d("PLACE", "Lat: " + mOriginLatLng.latitude);
////                Log.d("PLACE", "Lng: " + mOriginLatLng.longitude);
////            }
////
////            @Override
////            public void onError(@NonNull Status status) {
////
////            }
////        });
////    }
//
////    private void instanceAutocompleteDestination() {
////        mAutocompleteDestination = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.placeAutocompleteDestination);
////        mAutocompleteDestination.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
////        mAutocompleteDestination.setHint("Destino");
////        mAutocompleteDestination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
////            @Override
////            public void onPlaceSelected(@NonNull Place place) {
////                mDestination = place.getName();
////                mDestinationLatLng = place.getLatLng();
////                Log.d("PLACE", "Name: " + mDestination);
////                Log.d("PLACE", "Lat: " + mDestinationLatLng.latitude);
////                Log.d("PLACE", "Lng: " + mDestinationLatLng.longitude);
////            }
////
////            @Override
////            public void onError(@NonNull Status status) {
////
////            }
////        });
////    }
//
//    private void getActiveClientes() {
//        mGeofireProviderColaborador.getActiveClientes(mCurrentLatLng, 10).addGeoQueryEventListener(new GeoQueryEventListener() {
//            @Override
//            public void onKeyEntered(String key, GeoLocation location) {
//                // AÑADIREMOS LOS MARCADORES DE LOS EMPRENDEDORES QUE SE CONECTEN EN LA APLICACION
//
//                for (Marker marker : mClienteMarkers) {
//                    if (marker.getTag() != null) {
//                        if (marker.getTag().equals(key)) {
//                            return;
//                        }
//                    }
//                }
//
//                LatLng clienteLatLng = new LatLng(location.latitude, location.longitude);
//                Marker marker = mMap.addMarker(new MarkerOptions().position(clienteLatLng).title(retorno(mPedir)).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_my_location)));
//                marker.setTag(key);
//                mClienteMarkers.add(marker);
//            }
//
//            @Override
//            public void onKeyExited(String key) {
//                for (Marker marker : mClienteMarkers) {
//                    if (marker.getTag() != null) {
//                        if (marker.getTag().equals(key)) {
//                            marker.remove();
//                            mClienteMarkers.remove(marker);
//                            return;
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onKeyMoved(String key, GeoLocation location) {
//                // ACTUALIZAR LA POSICION DE CADA EMPRENDEDOR
//                for (Marker marker : mClienteMarkers) {
//                    if (marker.getTag() != null) {
//                        if (marker.getTag().equals(key)) {
//                            marker.setPosition(new LatLng(location.latitude, location.longitude));
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onGeoQueryReady() {
//
//            }
//
//            @Override
//            public void onGeoQueryError(DatabaseError error) {
//
//            }
//        });
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        //mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.setOnCameraIdleListener(mCameraListener);
//
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(5);
//
//        startLocation();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    if (gpsActived()) {
//                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//                        mMap.setMyLocationEnabled(true);
//                    } else {
//                        showAlertDialogNOGPS();
//                    }
//                } else {
//                    checkLocationPermissions();
//                }
//            } else {
//                checkLocationPermissions();
//            }
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == SETTINGS_REQUEST_CODE && gpsActived()) {
//            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//            mMap.setMyLocationEnabled(true);
//        } else if (requestCode == SETTINGS_REQUEST_CODE && !gpsActived()) {
//            showAlertDialogNOGPS();
//        }
//    }
//
//    private void showAlertDialogNOGPS() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Por favor activa tu ubicacion para continuar")
//                .setPositiveButton("Configuraciones", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
//                    }
//                }).create().show();
//    }
//
//    private boolean gpsActived() {
//        boolean isActive = false;
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            isActive = true;
//        }
//        return isActive;
//    }
//
//    private void startLocation() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                if (gpsActived()) {
//                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//                    mMap.setMyLocationEnabled(true);
//                } else {
//                    showAlertDialogNOGPS();
//                }
//            } else {
//                checkLocationPermissions();
//            }
//        } else {
//            if (gpsActived()) {
//                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//                mMap.setMyLocationEnabled(true);
//            } else {
//                showAlertDialogNOGPS();
//            }
//        }
//    }
//
//    private void checkLocationPermissions() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                new AlertDialog.Builder(this)
//                        .setTitle("Proporciona los permisos para continuar")
//                        .setMessage("Esta aplicacion requiere de los permisos de ubicacion para poder utilizarse")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                ActivityCompat.requestPermissions(MapColaboradorActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
//                            }
//                        })
//                        .create()
//                        .show();
//            } else {
//                ActivityCompat.requestPermissions(MapColaboradorActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
//            }
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.cliente_menu, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.action_logout) {
//            logout();
//        }
//        if (item.getItemId() == R.id.action_update) {
//            Intent intent = new Intent(MapColaboradorActivity.this, UpdateProfileColaboradorActivity.class);
//            startActivity(intent);
//        }
//        if (item.getItemId() == R.id.action_history) {
////            Intent intent = new Intent(MapClienteActivity.this, HistoryBookingClienteActivity.class);
//            Intent intent = new Intent(MapColaboradorActivity.this, MainActivity.class);
//            startActivity(intent);
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    void logout() {
//        mAuthProvider.logout();
//        Intent intent = new Intent(MapColaboradorActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();
//    }
//
//    void generateToken() {
//        mTokenProvider.create(mAuthProvider.getId());
//    }
//
//    protected String retorno(String mPedir){
//        String p;
//        p = mPedir;
//        return p;
//    }
//
////    private void updateLocation() {
////        if (mAuthProvider.existSession() && mCurrentLatLng != null) {
////            mGeofireProviderColaborador.saveLocation(mAuthProvider.getId(), mCurrentLatLng);
////        }
////    }
//}