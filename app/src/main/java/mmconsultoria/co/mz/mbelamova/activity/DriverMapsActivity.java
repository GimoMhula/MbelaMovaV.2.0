package mmconsultoria.co.mz.mbelamova.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import io.reactivex.android.schedulers.AndroidSchedulers;
import mmconsultoria.co.mz.mbelamova.Common.Common;
import mmconsultoria.co.mz.mbelamova.Common.Util;
import mmconsultoria.co.mz.mbelamova.R;
import mmconsultoria.co.mz.mbelamova.model.BaseActivity;
import mmconsultoria.co.mz.mbelamova.mpesaapi.Mpesa;
import mmconsultoria.co.mz.mbelamova.util.AppUtils;
import mz.co.moovi.mpesalib.api.PaymentResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static java.lang.Double.parseDouble;
import static java.lang.Double.valueOf;


@RequiresApi(api = Build.VERSION_CODES.P)
public class DriverMapsActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,com.google.android.gms.location.LocationListener ,RoutingListener {

    private static final String TAG = "DriverMapsActivity";
    //Permissoes
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //Constantes
    private static final float DEFAULT_ZOOM = 15f;
    private static final int RC_SIGN_IN = 9001;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    //Widget Vars
    private ImageView mGps;
    private DrawerLayout mDrawerLayout;
    private de.hdodenhof.circleimageview.CircleImageView mPerfilFoto;
    private ImageView navigation_menu;
    private CardView mSettings;
    private TextView nav_profile_name;
    private EditText valorRegarga;
    private TextView txtSaldo;
    private Button btnRecarregar;

    //Map Vars
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient; // Voce me deu Problemas
    private Location mLastLocation;
    private LocationRequest locationRequest;
    private GoogleApiClient mGoogleApiClient;
    DatabaseReference driver;
    GeoFire geoFire;
    private LocationRequest mLocationRequest;
    Marker currentMarker;
    private LatLng start;
    protected LatLng end;


    //Dados google

    //private final String TAG =this.getClass().getSimpleName() ;
    GoogleSignInAccount acct;
    GoogleSignInClient mGoogleSignInClient;

    private Polyline currentPolyline;

    // Animacao do carro
    private List<LatLng> polyLineList;
    private Marker carMarker;
    private float v;
    private double lat, lng;
    private Handler handler;
    private LatLng startPosition, endPosition, currentPosition;
    private int index, next;
    private Button tracarRota;
    private Button partida;
    private Button destino;
    private String destination;
    private PolylineOptions polylineOptions, blackPolylineOptions;
    private Polyline blackPolyline, greyPolyline;
    private IGoogleAPI mService;
    private ProgressDialog progressDialog;
    private List<Polyline> polylines;
    private NavigationView navigationView;
    private Location currentLocation;
    private Mpesa mpesa;
    private Object dataTranfer;

    Double tripDistance;
    private double value;
    private double volume=0.16;
    private double fuelPrice=69;

    //private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};
//Inicializacao de variaves


    @Override
    protected void onStart() {
        super.onStart();
        if (acct != null) {

            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();

            nav_profile_name.setText(personName);
            Picasso.with(DriverMapsActivity.this)
                    .load(personPhoto)
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .fit()
                    .into(mPerfilFoto);
            Toast.makeText(this, personName + "Email:" + personEmail, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


//        currentPosition=new LatLng(mLastLocation.getAltitude(),mLastLocation.getLongitude());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mpesa = new Mpesa();
        Toast.makeText(this, "Driver Mode", Toast.LENGTH_SHORT).show();

        //Elton Info
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //  signIn();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        nav_profile_name = headerView.findViewById(R.id.nome_textView);
        mPerfilFoto = headerView.findViewById(R.id.perfil_foto);


        navigation_menu = (ImageView) findViewById(R.id.navigation_menu);

        mGps = (ImageView) findViewById(R.id.ic_gps);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        acceptLocationPermission();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


        // Google AutoCompliteFragment
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                destination = place.getName().toString();
                end = place.getLatLng();
                geoLocate();

                Log.i(TAG, "Place_Searched: " + place.getName().toString());

            }


            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        // Filtro para Mocambique
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("MZ")
                .build();

        autocompleteFragment.setFilter(typeFilter);


        // Nevegation Drawer
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(false);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        int id = menuItem.getItemId();

                        if (id == R.id.nav_pedir_boleia) {
                            Intent homeIntent = new Intent(DriverMapsActivity.this, ClientMapActivity.class);
                            startActivity(homeIntent);
                            finish();


                        } else if (id == R.id.nav_termos_de_uso) {
                            startMyActivity(TermsActivity.class);
                        } else if (id == R.id.nav_definicoes) {
                            startMyActivity(SettingsActivity.class);
                        } else if (id == R.id.nav_promocao) {
                            startMyActivity(PromoActivity.class);
                        } else if (id == R.id.nav_pagamento) {
                            Toast.makeText(DriverMapsActivity.this, "Pagamento", Toast.LENGTH_SHORT).show();
                            View customView = getLayoutInflater().inflate(R.layout.recharge_dialog, null, false);
                            valorRegarga = customView.findViewById(R.id.valor_a_recarregar);
                            btnRecarregar = customView.findViewById(R.id.recarregar_button);

                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DriverMapsActivity.this);
                            //alertBuilder.setTitle("Recarga");
                            alertBuilder.setView(customView);
                            AlertDialog alertDialog = alertBuilder.create();
                            valorRegarga.setText(String.format("%.3f",
                                    valueToPay(volume,fuelPrice)
                                    )
                            );
                            alertDialog.show();

                            btnRecarregar.setOnClickListener(v -> {
                                alertDialog.hide();

                                payMpesa();
                                alertDialog.dismiss();
                                alertDialog.cancel();
                            });
                            /*alertDialog.hide();*/
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


                        } else if (id == R.id.nav_feedback) {
                            Toast.makeText(DriverMapsActivity.this, "Feedback", Toast.LENGTH_SHORT).show();
                        } else if (id == R.id.nav_historico) {

                            Toast.makeText(DriverMapsActivity.this, "Historico", Toast.LENGTH_SHORT).show();
                        }

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });


        // Botao Tracar Rota ...Funcionalidade
        tracarRota = findViewById(R.id.btnGetDirection);
        partida = findViewById(R.id.btnPartida);
        destino = findViewById(R.id.btnDestino);
        destino.setAlpha(0);

        tracarRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetVerification();

                if (destination == null) {
                    autocompleteFragment.setHint(getString(R.string.Destiny));
                } else {
                    mMap.clear();
                    dataTranfer = new Object[3];
                    //   dataTranfer[0]=mMap;
//                    GetDirectionsData getDirectionsData=new GetDirectionsData();
//                    getDirectionsData.execute(dataTranfer);
                    getDirection();
                }


                // myHome();

//                route();


            }


            private void internetVerification() {
                if (Util.Operations.isOnline(DriverMapsActivity.this)) {

                } else {
                    snackBar(findViewById(R.id.drawer_layout), "Sem Conexão a internet");
                }
            }
        });

        partida.setOnClickListener(v -> {
            //Place Picker
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            try {
                startActivityForResult(builder.build(DriverMapsActivity.this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
            // Start marker
            MarkerOptions options = new MarkerOptions();
            options.position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
            options.title("Local de Partida");
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue));
            mMap.addMarker(options);


            // Marker pickUpMarker= mMap.addMarker(new MarkerOptions().position(currentPosition).title("PickUpLocation"));


        });

        destino.setOnClickListener(v -> {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            try {
                startActivityForResult(builder.build(DriverMapsActivity.this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }


            //  Marker pickUpMarker= mMap.addMarker(new MarkerOptions().position(currentPosition).title("PickUpLocation"));

            // End marker
            MarkerOptions options = new MarkerOptions();
            options = new MarkerOptions();
            options.position(end);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green));
            mMap.addMarker(options);

        });


        polyLineList = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getLocationPermission();
        mService = Common.geIGoogleAPI();

    }

    private void payMpesa() {

        //final String phoneNumber = mpesaPhoneNr.getText().toString().trim();
        // final String amount = mpesaAmount.getText().toString().trim();

        Timber.d("O valor da transacao eh: " + value);




        mpesa.pay(String.format("%.3f",
                valueToPay(volume,fuelPrice)
        ), "258845204801")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
    }

    private void onError(Throwable throwable) {
        Timber.d(throwable);
    }

    private void onSuccess(PaymentResponse paymentResponse) {
        Timber.d("Successo" +"code: "+paymentResponse.getOutput_ResponseCode()+" TransactionID: "+paymentResponse.getOutput_TransactionID());
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        // The Routing request failed
        progressDialog.dismiss();
        if (e != null) {
            snackBar(findViewById(R.id.drawer_layout), "Erro: " + e.getMessage());
            Log.d(TAG, "onRoutingFailure: " + e.getMessage());


        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void snackBar(View viewById, String s) {
        Snackbar.make(viewById, s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onRoutingStart() {

    }


    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        progressDialog.dismiss();
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        mMap.moveCamera(center);

        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            // int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            // polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

        }

        // Start marker
        MarkerOptions options = new MarkerOptions();
        options.position(start);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue));
        mMap.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(end);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green));
        mMap.addMarker(options);

    }

    @Override
    public void onRoutingCancelled() {

    }

    public void route() {
        start = currentPosition;
        Log.d(TAG, "current position: " + currentPosition);
//
//        Log.d(TAG, "start: "+start);
//        Log.d(TAG, "end: "+end);

        if (start == null || end == null) {
            if (start == null) {
//                if(starting.getText().length()>0)
//                {
//                    starting.setError("Choose location from dropdown.");
//                }
//                else
//                {
//                    Toast.makeText(this,"Please choose a starting point.",Toast.LENGTH_SHORT).show();
//                }
            }
            if (end == null) {
                Toast.makeText(this, "End is empty", Toast.LENGTH_SHORT).show();
//                if(destination.getText().length()>0)
//                {
//                    destination.setError("Choose location from dropdown.");
//                }
//                else
//                {
//                    Toast.makeText(this,"Please choose a destination.",Toast.LENGTH_SHORT).show();
//                }
            }
        } else {
            progressDialog = ProgressDialog.show(DriverMapsActivity.this, "Por favor Aguarde.",
                    "Processando a Rota.", true);
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(false)// depois mudar
                    .waypoints(start, end)
                    .key(getResources().getString(R.string.google_maps_key))
                    .build();
            routing.execute();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation =location;
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11f));
        String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();
        DatabaseReference dbref=FirebaseDatabase.getInstance().getReference("driversAvailable");
        GeoFire geoFire=new GeoFire(dbref);
        geoFire.setLocation(userId,new GeoLocation(location.getLatitude(),location.getLongitude()));
    }

    public interface OnArticleSelectedListener {
        public void onArticleSelected(Uri articleUri);
    }

    Runnable drawPathRunnable = new Runnable() {
        @Override
        public void run() {
            if (index < polyLineList.size() - 1) {
                index++;
                next = index + 1;
            }
            if (index < polyLineList.size() - 1) {
                startPosition = polyLineList.get(index);
                endPosition = polyLineList.get(next);

            }

            final ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 1);
            valueAnimator.setDuration(3000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    v = valueAnimator.getAnimatedFraction();
                    lng = v * endPosition.longitude + (1 - v) * startPosition.longitude;
                    lat = v * endPosition.latitude + (1 - v) * startPosition.latitude;
                    LatLng newPos = new LatLng(lat, lng);
                    carMarker.setPosition(newPos);
                    carMarker.setAnchor(0.5f, 5f);
                    carMarker.setRotation(getBearing(startPosition, newPos));
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(newPos)
                                    .zoom(15.5f)
                                    .build()
                    ));
                    valueAnimator.start();
                    handler.postDelayed(drawPathRunnable, 3000); // Problema, tinha que ser "this"
                }
            });
        }
    };

    private float getBearing(LatLng startPosition, LatLng endPosition) {
        double lat = Math.abs(startPosition.latitude - endPosition.latitude);
        double lng = Math.abs(startPosition.longitude - endPosition.longitude);

        if (startPosition.latitude < endPosition.latitude && startPosition.longitude < endPosition.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (startPosition.latitude >= endPosition.latitude && startPosition.longitude < endPosition.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (startPosition.latitude >= endPosition.latitude && startPosition.longitude >= endPosition.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (startPosition.latitude < endPosition.latitude && startPosition.longitude >= endPosition.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void getDirection() {
        currentPosition = new LatLng(mLastLocation.getAltitude(), mLastLocation.getLongitude());
        String requestApi = null;
        try {
            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=driving&" +
                    "transit_routing_preference=less_driving&" +
                    "origin=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() + "&" +
                    "destination=" + destination + "&" +
                    "key=" + getResources().getString(R.string.google_direction_api);
            Log.d(TAG, "getDirection: " + requestApi);// Print Url for Debug
            mService.getPath(requestApi).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("routes");
                        //
                        //JSONObject rout = jsonArray.getJSONObject(0);
                        JSONArray legs = new JSONArray(jsonArray.getJSONObject(0).getString("legs"));
                        JSONObject legs_info = legs.getJSONObject(0);
                        JSONArray steps = legs_info.getJSONArray("steps");
                        String duracao = legs_info.getJSONObject("duration").getString("text");
                        String distancia = legs_info.getJSONObject("distance").getString("text");
                        tripDistance = parseDouble(distancia.replace("km", "").replaceAll(" ", ""));

                        Log.d(TAG, "duracao: " + duracao);
                        Log.d(TAG, "distancia: " + distancia);

//                        tripDistance = Double.parseDouble(legs_info.getJSONObject("distance").getString("text"));
                        Toast.makeText(DriverMapsActivity.this, "deslocamento Antes : " + (tripDistance), Toast.LENGTH_LONG).show();
                        Toast.makeText(DriverMapsActivity.this, "deslocamento Depois: " + (tripDistance + 2), Toast.LENGTH_LONG).show();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject route = jsonArray.getJSONObject(i);
                            JSONObject poly = route.getJSONObject("overview_polyline");
                            String polyline = poly.getString("points");
                            polyLineList = decodePoly(polyline);

                            //Adjusting Bounds

                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (LatLng latLng : polyLineList)
                                builder.include(latLng);
                            LatLngBounds bounds = builder.build();
                            CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
                            mMap.animateCamera(mCameraUpdate);

                            polylineOptions = new PolylineOptions();
                            polylineOptions.color(R.color.rotaTracandoFundo);
                            polylineOptions.width(8);
                            polylineOptions.startCap(new SquareCap());
                            polylineOptions.endCap(new SquareCap());
                            polylineOptions.jointType(JointType.ROUND);
                            polylineOptions.addAll(polyLineList);
                            greyPolyline = mMap.addPolyline(polylineOptions);

                            blackPolylineOptions = new PolylineOptions();
                            blackPolylineOptions.color(R.color.rotaTracandoSuperficie);
                            blackPolylineOptions.width(8);
                            blackPolylineOptions.startCap(new SquareCap());
                            blackPolylineOptions.endCap(new SquareCap());
                            blackPolylineOptions.jointType(JointType.ROUND);
                            blackPolyline = mMap.addPolyline(blackPolylineOptions);

                            mMap.addMarker(new MarkerOptions()
                                    .position(polyLineList.get(polyLineList.size() - 1))
                                    .title("Local de encontro"));
                            //pickup Location

                            //Animacao
                            ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
                            polylineAnimator.setDuration(2000);
                            polylineAnimator.setInterpolator(new LinearInterpolator());
                            polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    List<LatLng> points = greyPolyline.getPoints();
                                    int percentValue = (int) valueAnimator.getAnimatedValue();
                                    int size = points.size();
                                    int newPoits = (int) (size * (percentValue / 100.0f));
                                    List<LatLng> p = points.subList(0, newPoits);
                                    blackPolyline.setPoints(p);


                                }
                            });
                            polylineAnimator.start();
                            carMarker = mMap.addMarker(new MarkerOptions().position(currentPosition)
                                    .flat(true)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.carro)));

                            handler = new Handler();
                            index = -1;
                            next = 1;

                            handler.postDelayed(drawPathRunnable, 300);
                        }
                    } catch (Exception e) {

                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(DriverMapsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {

        }


    }


    private List decodePoly(String encoded) {

        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

// Metodos para Rota
    //TODO: Rotas

    // Fim metodos para rotas

    private void init() {

        mGps.setOnClickListener(v -> {
            Log.d(TAG, "onClick,init: localizacaoActual");
            getDeviceLocation();
//
        });

    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: Localizacao Geografica");


        Geocoder geocoder = new Geocoder(DriverMapsActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(destination, 1);

        } catch (IOException e) {
            Log.d(TAG, "geoLocate: IOException" + e.getMessage());

        }

        if (list.size() > 0) {
            Address address = list.get(0); // Localizacao pesquisada
            Log.d(TAG, "geoLocate: Localizacao Localizada " + address.toString());
            //  Toast.makeText(this, "Localizacao Localizada "+address.toString(), Toast.LENGTH_SHORT).show();
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, "Dentro", Toast.LENGTH_LONG).show();
                // Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: Buscando a localizacao Actual");
        // Toast.makeText(this, "Buscando a localizacao Actual", Toast.LENGTH_SHORT).show();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();


                ((Task) location).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Localizacao encontrada");
                            currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                mLastLocation = currentLocation;
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My location");
                            }

                        } else {
                            Log.d(TAG, "onComplete: Local actual nulo");
                            Toast.makeText(DriverMapsActivity.this, "Não foi possível carregar a localização actual", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: Excepção de Segurançaa" + e.getMessage());
        }
    }


    private void moveCamera(LatLng latLng, float zoom, String title) {// mover a camera
        Log.d(TAG, "moveCamera: movendo a camera para latitude:" + latLng.latitude + "longitude:" + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        //Opcoes de marcador
        if (!title.equals("My location")) {

            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options); // Adicionando o marcador ao mapa

        }
        // hideSoftKeyboard();
    }

    private void initMap() {// inicializar o mapa
        Log.d(TAG, "initMap: inicializando o mapa");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(DriverMapsActivity.this);

    }


    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: buscando permissao de localizacao");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permissao falhou");
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    Log.d(TAG, "onRequestPermissionsResult: permissao consedida");
                    // Inicializa o mapa pois tudo esta bem
                    initMap();
                }
            }
        }
    }


    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
//medi bang
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d(TAG, "mLastLocation " + mLastLocation.toString());
        if (mLastLocation != null) {
//            if (location_swhitch.isChecked()) {
            final double latitude = mLastLocation.getLatitude();
            final double longitude = mLastLocation.getLongitude();

            geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    //Add Marker
                    if (currentMarker != null) {
                        currentMarker.remove();
                        currentMarker = mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.carro))
                                .position(new LatLng(latitude, longitude))
                                .title("Voce"));

                        //Move Camera to this position
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));

//                            // Animacao do carro
//                            rotateMarker(currentMarker, -360, mMap);

                    }
                }
            });
//            } else {
//                Log.d(TAG, "displayLocation: " + "cannot get the location");
//                Toast.makeText(this, "cannot get the location", Toast.LENGTH_SHORT).show();
//
//            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Toast.makeText(this, "Mapa inicializado com Sucesso!", Toast.LENGTH_LONG).show();

        mMap = googleMap;
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            //    displayLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

            mMap.getUiSettings().setMyLocationButtonEnabled(false);// desabilitar localizacao My location

            // Tutorial Retrofit
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setTrafficEnabled(false);
            mMap.setIndoorEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(true); // Habilitar Zoom

            init();
            // displayLocation();
            // startLocationUpdates();

        }


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        return true;
    }

    public void openDrawer(View view) {
        mDrawerLayout.openDrawer(GravityCompat.START, true);
    }


    private void stopLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        currentMarker.remove();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, (com.google.android.gms.location.LocationListener) this);
        // LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest,new LocationCallback(),Looper.getMainLooper());


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest=new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);

        displayLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void acceptLocationPermission() {
        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            if (!AppUtils.isLocationEnabled(this)) {
                // notify user
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage(getString(R.string.location_dialog_msg));
                dialog.setPositiveButton(getString(R.string.location_settings), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                });
                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub

                    }
                });
                dialog.show();
            }
            buildGoogleApiClient();
        } else {
            Toast.makeText(this, "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStop() {
        super.onStop();
        String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbref=FirebaseDatabase.getInstance().getReference("driversAvailable");
        GeoFire geoFire=new GeoFire(dbref);
        geoFire.removeLocation(userId);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }
    public double valueToPay(double volume , double fuelPrice){
        return volume*fuelPrice*tripDistance+(volume*fuelPrice*tripDistance*0.15);

    }
}
