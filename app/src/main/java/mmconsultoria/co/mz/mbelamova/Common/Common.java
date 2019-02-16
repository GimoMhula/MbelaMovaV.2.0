package mmconsultoria.co.mz.mbelamova.Common;

import mmconsultoria.co.mz.mbelamova.activity.IGoogleAPI;
import mmconsultoria.co.mz.mbelamova.activity.RetrofitClient;

public class Common {
    public static final String baseURL="https://maps.googleapis.com";

    public static IGoogleAPI geIGoogleAPI(){
        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);
    }
}
