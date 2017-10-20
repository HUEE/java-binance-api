package binance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

/**
 * Created by hwj on 17-10-20.
 */
public abstract class ApiCallback implements Callback {
    public void onResponse(Call call, Response response) {
        if (response.isSuccessful() && null != response.body()) {
            onSuccess(response.body().toString());
        } else {
            try {
                onFailure(response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onFailure(Call call, Throwable throwable) {
        onFailure(throwable.getMessage());
    }

    /**
     * 请求对应回调接口
     */
    public abstract void onSuccess(String result);

    public abstract void onFailure(String msg);

}
