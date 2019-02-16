package mmconsultoria.co.mz.mbelamova.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;

import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mmconsultoria.co.mz.mbelamova.R;
import mmconsultoria.co.mz.mbelamova.adapter.PlacesAutoCompleteAdapter;
import mmconsultoria.co.mz.mbelamova.adapter.RecyclerItemClickListener;
import timber.log.Timber;


public class MyFragment extends BottomSheetDialogFragment implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, View.OnClickListener,View.OnFocusChangeListener{


    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;

    EditText start_EDT;
    EditText end_EDT;
    Button tracarRota_BT;
    TextView home;
    TextView work;
    private View view;
    private final String  TAG="MyFragment";
    private GoogleApiClient mGoogleApiClient;
    private EditText mAutocompleteView_Start;
    private ListView mRecyclerViewHome;
    private String currentFoncus ;
    private boolean endFocu=false;
    private EditText mAutocompleteView_End;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        buildGoogleApiClient();
        view=inflater.inflate(R.layout.fragment_my, container, false);

        mAutocompleteView_Start = (EditText) view.findViewById(R.id.startEDT);
        mAutocompleteView_End = (EditText) view.findViewById(R.id.endEDT);
        ListView listView=(ListView) view.findViewById(R.id.recyclerView);

       // listnear();

        start_EDT=(EditText) view.findViewById(R.id.startEDT);
        end_EDT=(EditText) view.findViewById(R.id.endEDT);
        tracarRota_BT=(Button) view.findViewById(R.id.tracar_rota_BT);
        start_EDT.setOnFocusChangeListener(this::onFocusChange);
        end_EDT.setOnFocusChangeListener(this::onFocusChange);



        prencherLista();

        return view;


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }
    private void prencherLista() {
// ListView HOme,Work
        mRecyclerViewHome=(ListView) view.findViewById(R.id.listViewHome);
        String opcoes[]={"* Casa","* Trabalho","* Local actual","* Selecionar no Mapa"};

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                getActivity(),
                R.layout.lista_view,
                opcoes);
        mRecyclerViewHome.setAdapter(adapter);
        mRecyclerViewHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewCliked, int position, long id) {


                TextView textView = (TextView) viewCliked;
                if(currentFoncus.equals("edt1")){
                    endFocu=false;
                    if(((TextView) viewCliked).getText().toString().equals("Casa")){
                        start_EDT.setText("casa");
                        Toast.makeText(getActivity(),  ((TextView) viewCliked).getText().toString(), Toast.LENGTH_SHORT).show();

                    }
                    if(((TextView) viewCliked).getText().toString().equals("Trabalho")){
                        start_EDT.setText("Trabalho");
                        Toast.makeText(getActivity(),  ((TextView) viewCliked).getText().toString(), Toast.LENGTH_SHORT).show();

                    }
                    if(((TextView) viewCliked).getText().toString().equals("Local actual")){
                        Toast.makeText(getActivity(),  ((TextView) viewCliked).getText().toString(), Toast.LENGTH_SHORT).show();
                        start_EDT.setText("Local actual");
                    }
                    if(((TextView) viewCliked).getText().toString().equals("Selecionar no Mapa")){
                        Toast.makeText(getActivity(),  ((TextView) viewCliked).getText().toString(), Toast.LENGTH_SHORT).show();

                    }
                }else if (currentFoncus.equals("edt2")){

                    if(((TextView) viewCliked).getText().toString().equals("Casa")){
                        end_EDT.setText("casa");
                        Toast.makeText(getActivity(),  ((TextView) viewCliked).getText().toString(), Toast.LENGTH_SHORT).show();

                    }
                    if(((TextView) viewCliked).getText().toString().equals("Trabalho")){
                        end_EDT.setText("Trabalho");
                        Toast.makeText(getActivity(),  ((TextView) viewCliked).getText().toString(), Toast.LENGTH_SHORT).show();

                    }
                    if(((TextView) viewCliked).getText().toString().equals("Local actual")){
                        Toast.makeText(getActivity(),  ((TextView) viewCliked).getText().toString(), Toast.LENGTH_SHORT).show();
                        end_EDT.setText("Local actual");
                    }
                    if(((TextView) viewCliked).getText().toString().equals("Selecionar no Mapa")){
                        Toast.makeText(getActivity(),  ((TextView) viewCliked).getText().toString(), Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });
       // listnear();


        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                        .setCountry("MZ")
                        .build();
        mAutoCompleteAdapter =  new PlacesAutoCompleteAdapter(getActivity(), R.layout.searchview_adapter,
                mGoogleApiClient, BOUNDS_INDIA, typeFilter);

        mRecyclerView=(RecyclerView) view.findViewById(R.id.recyclerView);
        mLinearLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAutoCompleteAdapter);

        mAutocompleteView_Start.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
//                if(s.toString().equals("")){
//                    start_EDT.setText("Local actual");
//                    start_EDT.setBackgroundColor(Color.rgb(4,5,1));
//                }else
                Log.d(TAG, "isConnected: "+ mGoogleApiClient.isConnected());
                    if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {

                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                }else if(!mGoogleApiClient.isConnected()){
                    Toast.makeText(getActivity(), "Google API not connected",Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"Google API not connected");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }


        });

        mAutocompleteView_End.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
//                if(s.toString().equals("")){
//                    start_EDT.setText("Local actual");
//                    start_EDT.setBackgroundColor(Color.rgb(4,5,1));
//                }else
                Log.d(TAG, "isConnected: "+ mGoogleApiClient.isConnected());
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {

                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                }else if(!mGoogleApiClient.isConnected()){
                    Toast.makeText(getActivity(), "Google API not connected",Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"Google API not connected");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }


        });


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                        final String placeId = String.valueOf(item.placeId);
                        Log.i("TAG", "Autocomplete item selected: " + item.description);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(mGoogleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if(places.getCount()==1){
                                    //Do the things here on Click.....
                                    if(currentFoncus.equals("edt1")){
                                        start_EDT.setText(places.get(0).getAddress());

                                        Toast.makeText(getActivity(),String.valueOf(places.get(0).getLatLng()) +"|"+places.get(0).getAddress(),Toast.LENGTH_SHORT).show();
                                    }
                                    if(currentFoncus.equals("edt2")){
                                        end_EDT.setText(places.get(0).getAddress());
                                        Toast.makeText(getActivity(),String.valueOf(places.get(0).getLatLng()) +"|"+places.get(0).getAddress(),Toast.LENGTH_SHORT).show();
                                    }

                                }else {
                                    Toast.makeText(getActivity(),"OOPs!!! Something went wrong...",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Log.i("TAG", "Clicked: " + item.description);
                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);
                    }
                })
        );
        //Antigo
//        ListView listView=(ListView) view.findViewById(R.id.list_view);
//        String opcoes[]={"-> Casa","-> Trabalho","-> Selecionar no Mapa"};
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(
//                getActivity(),
//                R.layout.searchview_adapter,
//                opcoes);
//        listView.setAdapter(adapter);

    }

    private void listnear() {
        ListView listView=(ListView) view.findViewById(R.id.listViewHome);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewCliked, int position, long id) {
                TextView textView = (TextView) viewCliked;
                if(((TextView) viewCliked).getText().toString().equals("Casa")){
                    Toast.makeText(getActivity(),  ((TextView) viewCliked).getText().toString(), Toast.LENGTH_SHORT).show();

                }
                if(((TextView) viewCliked).getText().toString().equals("Trabalho")){
                    Toast.makeText(getActivity(),  ((TextView) viewCliked).getText().toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });

//        start_EDT=(EditText) view.findViewById(R.id.startEDT);
//        start_EDT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v("Google API Callback", "Connection Done");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Google API Callback", "Connection Suspended");
        Log.v("Code", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v("Google API Callback","Connection Failed");
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        Toast.makeText(getActivity(),"Connection Failed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v==start_EDT){
            Toast.makeText(getActivity(), "Dentro Start", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
           if(v == start_EDT && hasFocus){
               currentFoncus = "edt1";
               Timber.d(" view %s, focus state: %s",((EditText)v).getText().toString(), currentFoncus);
           }

           else if(v == end_EDT && hasFocus){
               currentFoncus = "edt2";
               Timber.d(" view %s, focus state: %s",((EditText)v).getText().toString(), currentFoncus);
           }

    }
}
