package binance;

/**
 * Created by hwj on 17-10-20.
 */
public class BinanceApi {
    static String APIKEY;
    static String APISECRET;
    static Long RECVWINDOW = 6500L;
    private ApiMethods apiMethods = ApiMethods.getInstance();

    public static void init(String apiKey, String apiSecret) {
        APIKEY = apiKey;
        APISECRET = apiSecret;
    }

    public void setRecvWindow(Long recvWindow) {
        RECVWINDOW = recvWindow;
    }
}
