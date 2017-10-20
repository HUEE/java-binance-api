package binance;

import okhttp3.*;

import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by hwj on 17-10-20.
 */
public class ParamsInterceptor implements Interceptor {
    String signatureData;

    public okhttp3.Response intercept(Chain chain)
            throws IOException {
        Request original = chain.request();
        Request.Builder request = original.newBuilder();
        if ((original.headers().get("type") != null) && (original.headers().get("type").equals("public"))) {
            return chain.proceed(request.build());
        }
        request.header("X-MBX-APIKEY", BinanceApi.APIKEY);

        RequestBody requestBody = chain.request().body();
        FormBody.Builder formBody = null;
        String method = chain.request().method();
        if (method.equals("POST")) {
            if ((null != requestBody) && ((requestBody instanceof FormBody))) {
                formBody = new FormBody.Builder();
                for (int i = 0; i < ((FormBody) requestBody).size(); i++) {
                    formBody.add(((FormBody) requestBody).encodedName(i),
                            URLDecoder.decode(((FormBody) requestBody)
                                            .encodedValue(i),
                                    "UTF-8"));
                    this.signatureData = (this.signatureData + ((FormBody) requestBody).encodedName(i) + "=" + URLDecoder.decode(((FormBody) requestBody).encodedValue(i), "UTF-8"));
                    if (i != ((FormBody) requestBody).size() - 1) {
                        this.signatureData += "&";
                    }
                }
                formBody.add("signature", Crypto.HMACSHA256(this.signatureData.getBytes(), BinanceApi.APISECRET.getBytes()));
            }

            if (null != formBody) {
                request.post(formBody.build());
            }
        } else if (method.equals("GET")) {
            String url = chain.request().url().toString();
            String path = url.substring(url.indexOf("?") + 1, url.length());

            HttpUrl httpUrl = chain.request().url().newBuilder()
                    .addQueryParameter("signature",
                            Crypto.HMACSHA256(path
                                    .getBytes(), BinanceApi.APISECRET.getBytes()))
                    .build();
            request = request.url(httpUrl);

        }

        return chain.proceed(request.build());
    }
}
