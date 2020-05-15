package cn.nano.getui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

public enum NotificationProvider {

    INSTANCE;

    /**
     * {
     * "title":"消息title"，
     * "message":"消息内容",
     * //以上两个字段必须要有
     * <p>
     * <p>
     * "page":"meetingDetail",//需要跳转的页面
     * <p>
     * "extra":{//额外信息
     * }
     * <p>
     * }
     */
    public static final String PUSH_AREA_TITLE = "title";
    public static final String PUSH_AREA_MESSAGE = "message";
    public static final String PUSH_AREA_EATRA = "extra";
    public static final String PUSH_AREA_PAGE = "page";

    public static final String MEETING_CHANNEL_ID = "meeting_channel";
    public static final String CASSETTE_CHANNEL_ID = "cassette_channel";
    public static final String SOCIAL_CHANNEL_ID = "social_channel";
    public static final String CAMPAIGN_CHANNEL_ID = "campaign_channel";
    public static final String BENEFIT_CHANNEL_ID = "benefit_channel";
    public static final String OTHER_CHANNEL_ID = "other_channel";

    public void initChannels(Context context) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//            return;
//        }
//        List<String> CHANNEL_IDS = new ArrayList<>();
//        CHANNEL_IDS.add(MEETING_CHANNEL_ID);
//        CHANNEL_IDS.add(CASSETTE_CHANNEL_ID);
//        CHANNEL_IDS.add(SOCIAL_CHANNEL_ID);
//        CHANNEL_IDS.add(CAMPAIGN_CHANNEL_ID);
//        CHANNEL_IDS.add(BENEFIT_CHANNEL_ID);
//        CHANNEL_IDS.add(OTHER_CHANNEL_ID);
//
//        List<NotificationChannel> channels = new ArrayList<>();
//        for (String id : CHANNEL_IDS) {
//            NotificationChannel channel;
//
//            switch (id) {
//                case MEETING_CHANNEL_ID:
//                    channel = new NotificationChannel(id, context.getString(R.string.meeting_channel),
//                            NotificationManager.IMPORTANCE_HIGH);
//                    channel.setDescription(context.getString(R.string.meeting_channel_desc));
//                    channel.enableLights(true);
//                    channel.setLightColor(Color.BLUE);
//                    channel.enableVibration(true);
//                    channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//                    break;
//                case CASSETTE_CHANNEL_ID:
//                    channel = new NotificationChannel(id, context.getString(R.string.cassette_channel),
//                            NotificationManager.IMPORTANCE_HIGH);
//                    channel.setDescription(context.getString(R.string.cassette_channel_desc));
//                    channel.enableLights(true);
//                    channel.setLightColor(Color.BLUE);
//                    channel.enableVibration(true);
//                    channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//                    break;
//                case SOCIAL_CHANNEL_ID:
//                    channel = new NotificationChannel(id, context.getString(R.string.social_channel),
//                            NotificationManager.IMPORTANCE_DEFAULT);
//                    channel.setDescription(context.getString(R.string.social_channel_desc));
//                    channel.enableLights(true);
//                    channel.setLightColor(Color.BLUE);
//                    break;
//                case CAMPAIGN_CHANNEL_ID:
//                    channel = new NotificationChannel(id, context.getString(R.string.campaign_channel),
//                            NotificationManager.IMPORTANCE_DEFAULT);
//                    channel.setDescription(context.getString(R.string.campaign_channel_desc));
//                    channel.enableLights(false);
//                    channel.setSound(null, null);
//                    break;
//                case BENEFIT_CHANNEL_ID:
//                    channel = new NotificationChannel(id, context.getString(R.string.benefit_channel),
//                            NotificationManager.IMPORTANCE_DEFAULT);
//                    channel.setDescription(context.getString(R.string.benefit_channel_desc));
//                    channel.enableLights(false);
//                    channel.setSound(null, null);
//                    break;
//                case OTHER_CHANNEL_ID:
//                    channel = new NotificationChannel(id, context.getString(R.string.other_channel),
//                            NotificationManager.IMPORTANCE_DEFAULT);
//                    channel.setDescription(context.getString(R.string.other_channel_desc));
//                    channel.enableLights(false);
//                    channel.setSound(null, null);
//                    break;
//                default:
//                    continue;
//            }
//
//            channels.add(channel);
//        }
//
//        //最后在 notificationManager 中创建该通知渠道
//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        if (notificationManager != null) {
//            notificationManager.createNotificationChannels(channels);
//        }
    }

    //生成notification id
    private static final AtomicInteger atomic = new AtomicInteger(0);

    public static int getID() {
        return atomic.incrementAndGet();
    }

    //发送通知
    public void postNotification(Context context, String channelId, String title, String content,
                                 PendingIntent pendingIntent, int notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(title)
                .bigText(content);

        builder.setContentTitle(title).setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setStyle(bigTextStyle)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder.setSmallIcon(R.drawable.push_small)
//                    .setColor(0xff333333);
        } else {
//            builder.setSmallIcon(R.drawable.push_small);
        }

        boolean isShowSound = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 20
                && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 8;

        if (isShowSound) {
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(notificationId, builder.build());
        }
    }
}
