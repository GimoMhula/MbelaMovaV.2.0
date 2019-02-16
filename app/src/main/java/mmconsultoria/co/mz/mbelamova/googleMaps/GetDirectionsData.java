package mmconsultoria.co.mz.mbelamova.googleMaps;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.util.HashMap;

public class GetDirectionsData extends AsyncTask<Object,String,String> {
    GoogleMap mMap;
    String url;
    String googleDirectionsData;
    String duration,distance;


    @Override
    protected String doInBackground(Object... objects) {
//        mMap=(GoogleMap)objects[0];
        url=(String)objects[1];
      // DownloadUrl downloadUrl=new DownloadUrl();
      //  try {
        //    googleDirectionsData=downloadUrl.readUrl(url);
      //  }catch (IOException e){
       //     e.printStackTrace();
       // }

        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {

        HashMap<String,String> directionsList=null;
        DataParser parser =new DataParser();
        directionsList=parser.parseDirections(s);
        duration=directionsList.get("duration");
        distance=directionsList.get("distance");
        Log.d("DriverMapsActivity", "duracao: "+duration);
    }
}
