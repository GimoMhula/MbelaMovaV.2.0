package mmconsultoria.co.mz.mbelamova.googleMaps;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DataParser {
    public static final String TAG="DataParser";

    private HashMap<String,String> getDuration (JSONArray googleDirectionJson){

        HashMap<String,String> googleDirectionMap=new HashMap<>();
        String duration="";
        String distance ="";

        Log.d(TAG, "getDuration: "+ googleDirectionJson.toString());


        try {
            duration=googleDirectionJson.getJSONObject(0).getJSONObject("duration").getString("text");
            distance=googleDirectionJson.getJSONObject(0).getJSONObject("distance").getString("text");

               googleDirectionMap.put("duration",duration);
               googleDirectionMap.put("distance",distance);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googleDirectionMap;
    }

    public HashMap<String,String> parseDirections(String jsonData){
        JSONArray jsonArray=null;
        JSONObject jsonObject=null;


        try {
            jsonObject=new JSONObject(jsonData);
            jsonArray=jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getDuration(jsonArray);
    }

}
