package binance;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by hwj on 17-10-20.
 */
public class ApiMethods {
    public static final String BASE_URL = "https://www.binance.com/";
    private static final String BASE_WS = "wss://stream.binance.com:9443/ws/";
    private Retrofit retrofit;
    private ApiService httpService;
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static ApiMethods INSTANCE;
    WebSocket webSocket;

    private static class SingletonHolder {
        public SingletonHolder() {
            INSTANCE = new ApiMethods();
        }
    }

    public static ApiMethods getInstance() {
        if (null == INSTANCE) {
            new SingletonHolder();
        }
        return INSTANCE;
    }

    private ApiMethods() {
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new ParamsInterceptor())
                .sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts())
                .hostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();
        this.retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        this.httpService = this.retrofit.create(ApiService.class);
    }

    /**
     * Test connectivity to the Rest API.
     *
     * @param callback
     */
    public void ping(ApiCallback callback) {
        this.httpService.ping().enqueue(callback);
    }

    /**
     * Test connectivity to the Rest API and get the current server time.
     *
     * @param callback
     */
    public void getTime(ApiCallback callback) {
        this.httpService.time().enqueue(callback);
    }

    /**
     * Get compressed, aggregate trades.
     * Trades that fill at the time, from the same order,
     * with the same price will have the quantity aggregated.
     *
     * @param map
     * @param callback
     */
    public void getAggTrades(Map map, ApiCallback callback) {
        this.httpService.aggTrades(map).enqueue(callback);
    }

    /**
     * Kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.
     *
     * @param map
     * @param callback
     */
    public void getKlines(Map map, ApiCallback callback) {
        this.httpService.klines(map).enqueue(callback);
    }

    /**
     * 24 hour price change statistics.
     *
     * @param symbol
     * @param callback
     */
    public void get24hr(String symbol, ApiCallback callback) {
        this.httpService.get24hr(symbol).enqueue(callback);
    }

    /**
     * Latest price for all symbols.
     *
     * @param callback
     */
    public void getAllPrices(ApiCallback callback) {
        this.httpService.allPrices().enqueue(callback);
    }

    /**
     * Best price/qty on the order book for all symbols.
     *
     * @param callback
     */
    public void getAllBookTickers(ApiCallback callback) {
        this.httpService.allBookTickers().enqueue(callback);
    }

    /**
     * Check an order's status
     *
     * @param map      Name     	        Type	Mandatory	Description
     *                 symbol	            STRING	YES
     *                 orderId	            LONG	NO
     *                 origClientOrderId	STRING	NO
     *                 recvWindow	        LONG	NO
     *                 timestamp	        LONG	YES
     * @param callback
     */
    public void getOrder(Map map, ApiCallback callback) {
        this.httpService.order(map).enqueue(callback);
    }

    /**
     * Send in a new order
     *
     * @param map      Name	            Type	Mandatory	Description
     *                 symbol	        STRING	YES
     *                 side	            ENUM	YES
     *                 type         	ENUM	YES
     *                 timeInForce	    ENUM	YES
     *                 quantity	        DECIMAL	YES
     *                 price	        DECIMAL	YES
     *                 newClientOrderId	STRING	NO	    A unique id for the order. Automatically generated if not sent.
     *                 stopPrice	    DECIMAL	NO	    Used with stop orders
     *                 icebergQty	    DECIMAL	NO	    Used with iceberg orders
     *                 timestamp	    LONG	YES
     * @param callback
     */
    public void sendOrder(Map map, ApiCallback callback) {
        this.httpService.sendOrder(map).enqueue(callback);
    }

    /**
     * Cancel an active order
     *
     * @param map      Name	                Type	Mandatory	Description
     *                 symbol	            STRING	YES
     *                 orderId	            LONG	NO
     *                 origClientOrderId	STRING	NO
     *                 newClientOrderId	    STRING	NO	        Used to uniquely identify this cancel. Automatically generated by default.
     *                 recvWindow	        LONG	NO
     *                 timestamp	        LONG	YES
     * @param callback
     */
    public void cancelOrder(Map map, Callback<String> callback) {
        this.httpService.cancelOrder(map).enqueue(callback);
    }

    /**
     * Get all open orders on a symbo
     *
     * @param symbol
     * @param callback
     */
    public void getOpenOrders(String symbol, ApiCallback callback) {
        this.httpService.openOrders(symbol, BinanceApi.RECVWINDOW, Long.valueOf(System.currentTimeMillis())).enqueue(callback);
    }

    /**
     * get symbol depth ,default limit 100
     *
     * @param symbol
     * @param callback
     */
    public void getDepth(String symbol, ApiCallback callback) {
        this.httpService.depth(symbol, 100).enqueue(callback);
    }

    /**
     * get symbol depth
     *
     * @param symbol
     * @param limit    Default 100; max 100.
     * @param callback
     */
    public void getDepth(String symbol, int limit, ApiCallback callback) {
        this.httpService.depth(symbol, limit).enqueue(callback);
    }

    /**
     * Get current account information
     *
     * @param callback
     */
    public void getCount(ApiCallback callback) {
        this.httpService.account(BinanceApi.RECVWINDOW, Long.valueOf(System.currentTimeMillis())).enqueue(callback);
    }

    /**
     * Get trades for a specific account and symbol
     *
     * @param map      Name	        Type	Mandatory	Description
     *                 symbol	    STRING	YES
     *                 limit	    INT	    NO	    Default 500; max 500.
     *                 fromId	    LONG	NO	    TradeId to fetch from. Default gets most recent trades.
     *                 recvWindow	LONG	NO
     *                 timestamp	LONG	YES
     * @param callback
     */
    public void getMyTrades(Map map, ApiCallback callback) {
        this.httpService.myTrades(map).enqueue(callback);
    }

    /**
     * Get all account orders; active, canceled, or filled
     *
     * @param map      Name	        Type	Mandatory	Description
     *                 symbol	    STRING	YES
     *                 orderId	    LONG	NO
     *                 limit	    INT	    NO	    Default 500; max 500.
     *                 recvWindow	LONG	NO
     *                 timestamp	LONG	YES
     * @param callback
     */
    public void getAllOrders(Map map, ApiCallback callback) {
        this.httpService.allOrders(map).enqueue(callback);
    }

    /**
     * Start a new user data stream
     *
     * @param callback
     */
    public void getUserDataStream(ApiCallback callback) {
        this.httpService.getUserDataStream().enqueue(callback);
    }

    /**
     * Close out a user data stream
     *
     * @param listenKey
     * @param callback
     */
    public void closeUserDataStream(String listenKey, ApiCallback callback) {
        this.httpService.closeUserDataStream(listenKey).enqueue(callback);
    }

    /**
     * Fetch deposit history.
     *
     * @param map      Name     	Type	Mandatory	Description
     *                 asset	    STRING	NO
     *                 startTime	LONG	NO
     *                 endTime	    LONG	NO
     *                 recvWindow	LONG	NO
     *                 timestamp	LONG	YES
     * @param callback
     */
    public void getDepositHistory(Map map, ApiCallback callback) {
        this.httpService.depositHistory(map).enqueue(callback);
    }

    /**
     * Fetch withdraw history
     *
     * @param map      Name	        Type	Mandatory	Description
     *                 asset	    STRING	NO
     *                 startTime	LONG	NO
     *                 endTime	    LONG	NO
     *                 recvWindow	LONG	NO
     *                 timestamp	LONG	YES
     * @param callback
     */
    public void getWithdrawHistory(Map map, ApiCallback callback) {
        this.httpService.withdrawHistory(map).enqueue(callback);
    }

    /**
     * Submit a withdraw request.
     *
     * @param map      Name	        Type	Mandatory	Description
     *                 asset	    STRING	YES
     *                 address	    STRING	YES
     *                 amount	    LONG	YES
     *                 name     	STRING	NO	Description of the address
     *                 recvWindow	LONG	NO
     *                 timestamp	LONG	YES
     * @param callback
     */
    public void submitWithdraw(Map map, ApiCallback callback) {
        this.httpService.withdraw(map).enqueue(callback);
    }


    /**
     * creat request
     *
     * @param symbol
     * @param key
     * @return
     */
    private Request creatRequest(String symbol, String key) {
        String url = key.equals("") ? BASE_WS + symbol : BASE_WS + symbol + "@" + key;
        return new Request.Builder()
                .url(url)
                .build();
    }

    /**
     * Depth Websocket Endpoint
     *
     * @param symbol
     * @param socketListener
     */
    public void wsDepth(String symbol, WebSocketListener socketListener) {
        ws(symbol, "depth", socketListener);
    }

    /**
     * Kline Websocket Endpoint
     *
     * @param symbol
     * @param socketListener
     */
    public void wsKline(String symbol, String interval, WebSocketListener socketListener) {
        ws(symbol, "kline_" + interval, socketListener);
    }

    /**
     * Trades Websocket Endpoint
     *
     * @param symbol
     * @param socketListener
     */
    public void wsAggTrade(String symbol, WebSocketListener socketListener) {
        ws(symbol, "aggTrade", socketListener);
    }

    /**
     * User Data Websocket Endpoint
     *
     * @param key
     * @param socketListener
     */
    public void wsUserData(String key, WebSocketListener socketListener) {
        ws(key, "", socketListener);
    }

    /**
     * creat websocket
     *
     * @param symbol
     * @param key
     * @param socketListener
     */
    private void ws(String symbol, String key, WebSocketListener socketListener) {
        this.webSocket = new OkHttpClient.Builder()
                .readTimeout(5L, TimeUnit.SECONDS)
                .sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts())
                .hostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build()
                .newWebSocket(creatRequest(symbol.toLowerCase(), key), socketListener);
    }

    /**
     * close websocket
     */
    public void closeWs() {
        if (null != webSocket) {
            webSocket.cancel();
        }
    }

    /**
     * enable for https
     *
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

}
