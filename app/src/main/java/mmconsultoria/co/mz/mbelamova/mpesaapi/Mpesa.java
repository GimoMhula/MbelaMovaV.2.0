package mmconsultoria.co.mz.mbelamova.mpesaapi;

import java.util.Random;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import mz.co.moovi.mpesalib.api.MpesaRepository;
import mz.co.moovi.mpesalib.api.MpesaRepositoryImpl;
import mz.co.moovi.mpesalib.api.MpesaService;
import mz.co.moovi.mpesalib.api.PaymentRequest;
import mz.co.moovi.mpesalib.api.PaymentResponse;
import mz.co.moovi.mpesalib.config.MpesaConfig;
import mz.co.moovi.mpesalibui.MpesaSdk;

import static java.lang.String.valueOf;

public class Mpesa {

    private String baseUrl=MpesaSdk.SANDBOX_BASE_URL;
    private String publicKey="MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAmptSWqV7cGUUJJhUBxsMLonux24u+FoTlrb+4Kgc6092JIszmI1QUoMohaDDXSVueXx6IXwYGsjjWY32HGXj1iQhkALXfObJ4DqXn5h6E8y5/xQYNAyd5bpN5Z8r892B6toGzZQVB7qtebH4apDjmvTi5FGZVjVYxalyyQkj4uQbbRQjgCkubSi45Xl4CGtLqZztsKssWz3mcKncgTnq3DHGYYEYiKq0xIj100LGbnvNz20Sgqmw/cH+Bua4GJsWYLEqf/h/yiMgiBbxFxsnwZl0im5vXDlwKPw+QnO2fscDhxZFAwV06bgG0oEoWm9FnjMsfvwm0rUNYFlZ+TOtCEhmhtFp+Tsx9jPCuOd5h2emGdSKD8A6jtwhNa7oQ8RtLEEqwAn44orENa1ibOkxMiiiFpmmJkwgZPOG/zMCjXIrrhDWTDUOZaPx/lEQoInJoE2i43VN/HTGCCw8dKQAwg0jsEXau5ixD0GUothqvuX3B9taoeoFAIvUPEq35YulprMM7ThdKodSHvhnwKG82dCsodRwY428kg2xM/UjiTENog4B6zzZfPhMxFlOSFX4MnrqkAS+8Jamhy1GgoHkEMrsT5+/ofjCx0HjKbT5NuA2V/lmzgJLl3jIERadLzuTYnKGWxVJcGLkWXlEPYLbiaKzbJb2sYxt+Kt5OxQqC1MCAwEAAQ==";
    private String apiKey="qsmkq098a9bm4v63dhbrga4w7slx3z1y";

    private String ThirdPartyReference = valueOf(new Random(99999).nextInt());
    public static final String  TransactionReference="T12344C";
    public  final String ServiceProviderCode ="171717";


    private MpesaService service;

    public Mpesa() {
       service = paymentRequest -> {
            MpesaConfig config = new MpesaConfig(baseUrl , apiKey, publicKey);
            MpesaRepository repository = new MpesaRepositoryImpl(config);
           Random random = new Random(99999);
            return repository.pay(paymentRequest);
        };






    }


    public Single<PaymentResponse> pay(String  amount, String phoneNumber){
       PaymentRequest request = new PaymentRequest(ThirdPartyReference,amount,phoneNumber,ServiceProviderCode,TransactionReference);

       return service.pay(request)
                .subscribeOn(Schedulers.io());

    }


}
