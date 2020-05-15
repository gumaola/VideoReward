package cn.nano.common.okhttp.callback;

import androidx.annotation.CallSuper;

import com.google.gson.JsonSyntaxException;

import java.lang.reflect.ParameterizedType;

import cn.nano.common.utils.GsonUtil;
import cn.nano.common.utils.LogUtil;
import okhttp3.Call;
import okhttp3.Response;

public abstract class GsonCallback<T> extends Callback<T> {
    //用于返回字符串，可能需要保存处理，有一些接口则不需要这个string对象
    private String mResponseStr;

    public final String getResponseStr() {
        return mResponseStr;
    }

    @Override
    @CallSuper
    public T parseNetworkResponse(Response response, int id) throws Exception {
        mResponseStr = response.body().string();
        try {
            Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];
            if (entityClass == String.class) {
                return (T) mResponseStr;
            }

            if (mResponseStr.contains("[]")) {
                mResponseStr = mResponseStr.replace("[]", "null");
            }

            return GsonUtil.createGson().fromJson(mResponseStr, entityClass);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        e.printStackTrace();
    }

    /**
     * 返回json对象，不需要保存文件
     *
     * @param response
     */
    @Override
    public void onResponse(T response, int id) {
        LogUtil.log("resonse");
    }
}

