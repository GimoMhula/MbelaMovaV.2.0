package mmconsultoria.co.mz.mbelamova.mpesaapi;

import android.app.Application;

import mz.co.moovi.mpesalib.api.MpesaRepository;
import mz.co.moovi.mpesalib.api.MpesaRepositoryImpl;
import mz.co.moovi.mpesalib.api.MpesaService;
import mz.co.moovi.mpesalib.config.MpesaConfig;

public class MpesaApplication extends Application {
    private String baseUrl;
    private String publicKey;
    private String apiKey;

    @Override
    public void onCreate() {
        super.onCreate();
        MpesaService service = paymentRequest -> {
            MpesaConfig config = new MpesaConfig(baseUrl , apiKey, publicKey);
            MpesaRepository repository = new MpesaRepositoryImpl(config);

            return repository.pay(paymentRequest);
        };
    }
}
