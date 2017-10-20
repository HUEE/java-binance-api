
import binance.ApiCallback;
import binance.ApiMethods;
import binance.BinanceApi;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hwj on 17-10-20.
 */
public class main {
    static ApiMethods apiMethods;

    public static void main(String args[]) {
        BinanceApi.init("", "");
        apiMethods = ApiMethods.getInstance();

        test(new ApiCallback() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
            }

            @Override
            public void onFailure(String msg) {
                System.out.println(msg);
            }
        });


        testWs(new WebSocketListener() {
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                System.out.println(text);
            }

            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                System.out.println("WebSocket Open , data is coming");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                System.out.println(t.getMessage());
            }
        });
    }

    public static void test(ApiCallback callback) {
        Map map = new HashMap();
        map.put("symbol", "BNBETH");
//        map.put("interval","1m");
//        map.put("type","LIMIT");
//        map.put("timeInForce","GTC");
//        map.put("quantity",0.1);
//        map.put("price",0.000001);
//        map.put("payload", "ETH");
//        map.put("recvWindow", BinanceApi.RECVWINDOW);
        map.put("timestamp", System.currentTimeMillis());


        apiMethods.ping(callback);
        apiMethods.getTime(callback);
        apiMethods.getDepth("BNBETH", callback);
//        apiMethods.getAggTrades(map, callback);
//        apiMethods.getKlines(map, callback);
//        apiMethods.getAllPrices(callback);
//        apiMethods.get24hr("BNBETH", callback);
//        apiMethods.getAllBookTickers(callback);
//        apiMethods.getOpenOrders("BNBETH", callback);
//        apiMethods.getOrder(map, callback);
//        apiMethods.sendOrder(map, callback);
//        apiMethods.cancelOrder(map, callback);
//        apiMethods.getCount(callback);
//        apiMethods.getMyTrades(map, callback);
//        apiMethods.getAllOrders(map, callback);
//        apiMethods.getUserDataStream(callback);
//        apiMethods.closeUserDataStream("", callback);
//        apiMethods.getDepositHistory(map, callback);
//        apiMethods.getWithdrawHistory(map, callback);
//        apiMethods.submitWithdraw(map, callback);

    }

    public static void testWs(WebSocketListener listener) {
        apiMethods.wsDepth("BNBETH", listener);
//        apiMethods.wsKline("BNBETH", "1m", listener);
//        apiMethods.wsAggTrade("BNBETH", listener);

//        apiMethods.wsUserData("", listener);
    }


}
