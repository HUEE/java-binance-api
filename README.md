# Java Binance API
This project is designed to help you make your own projects that interact with the [Binance API](https://www.binance.com/restapipub.html). You can stream candlestick chart data, market depth, or use other advanced features such as setting stop losses and iceberg orders. This project seeks to have complete API coverage including WebSockets.

#### Installation 
  The Java and Android project can also use the library.<br/>
  <br/>1.Download the Jar [here](https://github.com/HUEE/java-binance-api/blob/master/binanceApi.jar),and dependence the Jar in your Java or Android project<br/>
  2.git clone the java source ,and add the source into your project .
  <br/><br/>So,You can use him as simple as the following:<br/>
### Init
```java
   BinanceApi.init("APIKEY","APISECRET");
   ApiMethods apiMethods = ApiMethods.getInstance()
``` 
   Now , you can get all method through "apiMethods" ,and fetch data what you want .<br/>
   All normal request result in ApiCallback,you can get data String in "onSuccess",and also can deal with err in "onFailure".
   <br/>But the websocket callback through "WebSocketListener",you can get data from "onMessage".
   
#### Example

##### Get all account orders; active, canceled, or filled
```java
    //some multi-parameter request we use Map Upload,
    //but you must upload what server want
    Map map = new HashMap();
    map.put("symbol", "BNBETH");
    map.put("timestamp", System.currentTimeMillis());
    apiMethods.getAllOrders(map, new ApiCallback() {
           public void onSuccess(String result) {
               System.out.print(result);
           }
           public void onFailure(String msg) {
               System.out.print(msg);
           }
        });
```    
                                                                      
##### 24 hour price change statistics. 
 ```java
     apiMethods.get24hr("BNBETH", new ApiCallback() {
         public void onSuccess(String result) {
             System.out.print(result);
         }
         public void onFailure(String msg) {
             System.out.print(msg);
         }
     });
 ```
 ####  Websocket url for depth endpoint
 ```java
 // 1m,3m,5m,15m,30m,1h,2h,4h,6h,8h,12h,1d,3d,1w,1M
 apiMethods.wsKline("BNBETH", "1m",new WebSocketListener() {
             @Override
             public void onMessage(WebSocket webSocket, String text) {
                 super.onMessage(webSocket, text);
                 System.out.println(text);
             }
 
             @Override
             public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                 super.onFailure(webSocket, t, response);
             }
 
             @Override
             public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                 super.onOpen(webSocket, response);
             }
         });
  ```
  ##### when you want to close the websocket 
 ```java
 apiMethods.closeWs();
 ```
 
 ### Other
All requests are similar to the above, so I just list the Apilist below.<br/>
if you want  test or know more about the API ,you can clone the demo Source code.<br/>
Also ,if there are any problems in use, please let me know.I'll fix it as soon as possible

 ## ApiList
  ```java
    //Test connectivity to the Rest API.
    ping(ApiCallback callback)
  ```
  ```java
    //Test connectivity to the Rest API and get the current server time.
    getTime(ApiCallback callback)
  ```
  ```java
    //Get compressed, aggregate trades.
    getAggTrades(Map map, ApiCallback callback)
  ```
  ```java
    //Kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.
    getKlines(Map map, ApiCallback callback)
  ```
  ```java
    //24 hour price change statistics.
    get24hr(String symbol, ApiCallback callback)
  ```
  ```java
      //Latest price for all symbols.
     getAllPrices(ApiCallback callback)
  ```
  ```java
     //Best price/qty on the order book for all symbols.
     getAllBookTickers(ApiCallback callback)
  ```
  ```java
     //Check an order's status
     getOrder(Map map, ApiCallback callback)
  ```
  ```java
     //Send in a new order
     sendOrder(Map map, ApiCallback callback)
  ```
  ```java
    //Cancel an active order
    cancelOrder(Map map, Callback<String> callback)
  ```
  ```java
    // Get all open orders on a symbo 
    getOpenOrders(String symbol, ApiCallback callback)
  ```
  ```java
    //get symbol depth 
    getDepth(String symbol, ApiCallback callback)
  ```
  ```java
    //Get current account information
    getCount(ApiCallback callback)
  ```
  ```java
    //Get trades for a specific account and symbol
    getMyTrades(Map map, ApiCallback callback)
  ```
  ```java
    //Get all account orders; active, canceled, or filled
    getAllOrders(Map map, ApiCallback callback)
  ```
  ```java
    //Start a new user data stream
    getUserDataStream(ApiCallback callback)
 ```
 ```java
    //Close out a user data stream
    closeUserDataStream(String listenKey, ApiCallback callback)
 ```
 ```java
    //Fetch deposit history.
    getDepositHistory(Map map, ApiCallback callback)
 ```
 ```java
    //Fetch withdraw history
    getWithdrawHistory(Map map, ApiCallback callback)
 ```
 ```java
    //Submit a withdraw request.
    submitWithdraw(Map map, ApiCallback callback)
 ```
 ```java
    //Depth Websocket Endpoint
    wsDepth(String symbol, WebSocketListener socketListener)
 ```
 ```java
    //Kline Websocket Endpoint
    wsKline(String symbol, String interval, WebSocketListener socketListener) 
 ```
 ```java
    //Trades Websocket Endpoint
    wsAggTrade(String symbol, WebSocketListener socketListener)
 ```
 ```java
    //User Data Websocket Endpoint
    wsUserData(String key, WebSocketListener socketListener)
 ```
    

  
  
 
