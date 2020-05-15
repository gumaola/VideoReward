package cn.nano.app.push;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import cn.nano.app.activity.LaunchActivity;
import cn.nano.common.app.LifeCyclerObserver;
import cn.nano.main.account.AccountManager;
import cn.nano.main.message.MessageActivity;
import cn.nano.main.server.AutoLoginOutCallback;
import cn.nano.main.server.ServerApi;
import cn.nano.main.server.result.BaseResult;
import okhttp3.Call;

/**
 * 个推接收消息service
 */
public class GetuiPushReceiver extends GTIntentService {

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        //获取到push token
        Log.e("GetuiSDK", "onReceiveClientId -> " + "clientid = " + clientid);
        String token = AccountManager.INSTANCE.getUserToken();
        if (TextUtils.isEmpty(token)) {
            return;
        }
        ServerApi.pushCid(token, clientid, new AutoLoginOutCallback<BaseResult>() {
            @Override
            public void onResponse(BaseResult response, int id) {
                super.onResponse(response, id);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
            }
        });
    }


    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {
        Log.e("GetuiSDK", "onNotificationMessageClicked -> " + "gtNotificationMessage = " + gtNotificationMessage.getContent());
        Activity activity = LifeCyclerObserver.getObserver().getCurrentActivity();
        if (activity == null) {
            LaunchActivity.forward();
            return;
        }
        MessageActivity.forward(activity);
    }


    ////******* 以下方法暂未使用 ************//////
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        //获取到透传信息，主动发送消息
        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();
        String messageData = new String(payload);

        Log.d("GetuiSDK", "onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid +
                "\nmessageid = " + messageid + "\npkg = " + pkg
                + "\ncid = " + cid
                + "\nmessage = " + messageData);
    }

    @Override
    public void onReceiveServicePid(Context context, int serviceId) {
        Log.e("GetuiSDK", "onReceiveServicePid -> " + "serviceId = " + serviceId);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        Log.e("GetuiSDK", "onReceiveOnlineState -> " + "state = " + b);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        Log.e("GetuiSDK", "onReceiveCommandResult -> " + "gtCmdMessage = " + gtCmdMessage);
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {
        Log.e("GetuiSDK", "onNotificationMessageArrived -> " + "gtNotificationMessage = " + gtNotificationMessage.getContent());
    }
}
