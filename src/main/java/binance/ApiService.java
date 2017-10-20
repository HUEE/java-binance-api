package binance;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * Created by hwj on 17-10-20.
 */
public interface ApiService {
    @Headers({"type:public"})
    @GET("api/v1/ping")
    Call<Object> ping();

    @Headers({"type:public"})
    @GET("api/v1/time")
    Call<Object> time();

    @Headers({"type:public"})
    @GET("api/v1/depth")
    Call<Object> depth(@Query("symbol") String paramString,@Query("limit") int limit);

    @Headers({"type:public"})
    @GET("api/v1/aggTrades")
    Call<Object> aggTrades(@QueryMap Map<String, String> paramMap);

    @Headers({"type:public"})
    @GET("api/v1/klines")
    Call<Object> klines(@QueryMap Map<String, String> paramMap);

    @Headers({"type:public"})
    @GET("api/v1/ticker/allPrices")
    Call<Object> allPrices();

    @Headers({"type:public"})
    @GET("api/v1/ticker/24hr")
    Call<Object> get24hr(@Query("symbol") String paramString);

    @Headers({"type:public"})
    @GET("api/v1/ticker/allBookTickers")
    Call<Object> allBookTickers();

    @GET("api/v3/order")
    Call<Object> order(@QueryMap Map<String, String> paramMap);

    @FormUrlEncoded
    @POST("api/v3/order")
    Call<Object> sendOrder(@FieldMap Map<String, String> paramMap);

    @DELETE("api/v3/order")
    Call<Object> cancelOrder(@QueryMap Map<String, String> paramMap);

    @GET("api/v3/openOrders")
    Call<Object> openOrders(@Query("symbol") String paramString,@Query("recvWindow") Long recvWindow, @Query("timestamp") Long paramLong);

    @GET("api/v3/account")
    Call<Object> account(@Query("recvWindow") Long recvWindow,@Query("timestamp") Long paramLong);

    @GET("api/v3/myTrades")
    Call<Object> myTrades(@QueryMap Map<String, String> paramMap);

    @GET("api/v3/allOrders")
    Call<Object> allOrders(@QueryMap Map<String, String> paramMap);

    @Headers({"type:api"})
    @POST("api/v1/userDataStream")
    Call<Object> getUserDataStream();

    @Headers({"type:api"})
    @DELETE("api/v1/userDataStream")
    Call<Object> closeUserDataStream(@Query("listenKey") String paramString);

    @FormUrlEncoded
    @POST("wapi/v1/getDepositHistory.html")
    Call<Object> depositHistory(@FieldMap Map<String, String> paramMap);

    @FormUrlEncoded
    @POST("wapi/v1/getWithdrawHistory.html")
    Call<Object> withdrawHistory(@FieldMap Map<String, String> paramMap);

    @FormUrlEncoded
    @POST("wapi/v1/withdraw.html")
    Call<Object> withdraw(@FieldMap Map<String, String> paramMap);

}
