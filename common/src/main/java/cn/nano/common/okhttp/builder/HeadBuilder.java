package cn.nano.common.okhttp.builder;

import cn.nano.common.okhttp.OkHttpUtils;
import cn.nano.common.okhttp.request.OtherRequest;
import cn.nano.common.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
