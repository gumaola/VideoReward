package cn.nano.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;

import java.io.File;

@SuppressLint("NewApi")
public class MessageUtil {

    /**
     * 发送短息
     *
     * @param context
     * @param text
     */
    public static void sendSMS(Context context, String text) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", text);
        context.startActivity(intent);
    }

    /**
     * 发送彩信，附件为图片格式
     *
     * @param context
     * @param subject      彩信主题
     * @param text         彩信内容
     * @param fileNameType 输出文件名的部分字符串
     * @param srcBitmap    bitmap 对象
     * @return true or false
     */
    public static boolean sendMMSImgObject(Context context, String subject, String text, String fileNameType,
                                           Bitmap srcBitmap, String strCacheFileDir) {
        Intent intent = null;
        Uri uri = null;
        try {
            uri = UriUtil.getUriForShare(context, srcBitmap, fileNameType, strCacheFileDir);

            if (Build.VERSION.SDK_INT >= 19) {
                String defaultSmsPackageName = Telephony.Sms
                        .getDefaultSmsPackage(context);
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                // Can be null in case that there is no default, then the user
                // would be able to choose any app that support this intent.
                if (defaultSmsPackageName != null) {
                    intent.setPackage(defaultSmsPackageName);
                }
            } else {
                intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                intent.putExtra("subject", subject); // 彩信的主题
                // intent.putExtra("address", "10086"); //彩信发送目的号码
                intent.putExtra("sms_body", text); // 彩信中文字内容
                // intent.putExtra(Intent.EXTRA_TEXT, text);

                intent.setType("image/*");// 彩信附件图片类型
                intent.setPackage("com.android.mms");

                // intent.setClassName("com.android.mms",
                // "com.android.mms.ui.ComposeMessageActivity");
            }
            context.startActivity(intent);
        } catch (Exception e) {
            // for sony
            try {
                intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                intent.putExtra(Intent.EXTRA_SUBJECT, subject); // 彩信的主题
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.setType("image/jpeg");// 彩信附件类型
                intent.setPackage("com.sonyericsson.conversations");
                context.startActivity(intent);
            } catch (Exception e1) {
                // for thc one
                try {
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                    intent.putExtra("subject", subject); // 彩信的主题
                    intent.putExtra("sms_body", text); // 彩信中文字内容
                    intent.setType("image/jpeg");// 彩信附件图片类型
                    intent.setClassName("com.android.mms",
                            "com.android.mms.ui.ComposeMessageActivity");
                    context.startActivity(intent);
                } catch (Exception e2) {
                    // for htc sense
                    try {
                        intent = new Intent("android.intent.action.SEND_MSG");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                        intent.putExtra("subject", subject); // 彩信的主题
                        // intent.putExtra("address", "10086"); //彩信发送目的号码
                        intent.putExtra("sms_body", text); // 彩信中文字内容
                        // intent.putExtra(Intent.EXTRA_TEXT, text);
                        intent.setType("image/jpeg");// 彩信附件类型
                        intent.setPackage("com.android.mms");
                        context.startActivity(intent);
                    } catch (Exception e3) {
                        // for huawei
                        try {
                            intent = new Intent(Intent.ACTION_SEND);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                            intent.putExtra(Intent.EXTRA_SUBJECT, subject); // 彩信的主题
                            intent.putExtra(Intent.EXTRA_TEXT, text);

                            // intent.putExtra("subject", subject); // 彩信的主题
                            // // intent.putExtra("address", "10086");
                            // //彩信发送目的号码
                            // intent.putExtra("sms_body", text); // 彩信中文字内容
                            // intent.putExtra(Intent.EXTRA_TEXT, text);
                            intent.setType("image/*");// 彩信附件类型
                            intent.setPackage("com.huawei.message");
                            context.startActivity(intent);
                        } catch (Exception e4) {

                            try {// for lenovo
                                intent = new Intent(Intent.ACTION_SEND);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                                // intent.putExtra(Intent.EXTRA_SUBJECT,
                                // subject); // 彩信的主题
                                // intent.putExtra(Intent.EXTRA_TEXT, text);

                                intent.putExtra("subject", subject); // 彩信的主题
                                // // intent.putExtra("address", "10086");
                                // //彩信发送目的号码
                                intent.putExtra("sms_body", text); // 彩信中文字内容
                                // intent.putExtra(Intent.EXTRA_TEXT, text);
                                intent.setType("image/*");// 彩信附件类型
                                intent.setPackage("com.lenovo.ideafriend");
                                context.startActivity(intent);
                            } catch (Exception e5) {

                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 发送彩信，附件为图片格式
     *
     * @param context
     * @param subject 彩信主题
     * @param text    彩信内容
     * @param uri
     * @return true or false
     */
    public static boolean sendMMSImgObject(Context context, String subject,
                                           String text, Uri uri) {
        Intent intent = null;
        try {

            if (Build.VERSION.SDK_INT >= 19) {
                String defaultSmsPackageName = Telephony.Sms
                        .getDefaultSmsPackage(context);
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                // Can be null in case that there is no default, then the user
                // would be able to choose any app that support this intent.
                if (defaultSmsPackageName != null) {
                    intent.setPackage(defaultSmsPackageName);
                }
            } else {
                intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                intent.putExtra("subject", subject); // 彩信的主题
                // intent.putExtra("address", "10086"); //彩信发送目的号码
                intent.putExtra("sms_body", text); // 彩信中文字内容
                // intent.putExtra(Intent.EXTRA_TEXT, text);

                intent.setType("image/*");// 彩信附件图片类型
                intent.setPackage("com.android.mms");

                // intent.setClassName("com.android.mms",
                // "com.android.mms.ui.ComposeMessageActivity");
            }
            context.startActivity(intent);
        } catch (Exception e) {
            // for sony
            try {
                intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                intent.putExtra(Intent.EXTRA_SUBJECT, subject); // 彩信的主题
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.setType("image/jpeg");// 彩信附件类型
                intent.setPackage("com.sonyericsson.conversations");
                context.startActivity(intent);
            } catch (Exception e1) {
                // for thc one
                try {
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                    intent.putExtra("subject", subject); // 彩信的主题
                    intent.putExtra("sms_body", text); // 彩信中文字内容
                    intent.setType("image/jpeg");// 彩信附件图片类型
                    intent.setClassName("com.android.mms",
                            "com.android.mms.ui.ComposeMessageActivity");
                    context.startActivity(intent);
                } catch (Exception e2) {
                    // for htc sense
                    try {
                        intent = new Intent("android.intent.action.SEND_MSG");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                        intent.putExtra("subject", subject); // 彩信的主题
                        // intent.putExtra("address", "10086"); //彩信发送目的号码
                        intent.putExtra("sms_body", text); // 彩信中文字内容
                        // intent.putExtra(Intent.EXTRA_TEXT, text);
                        intent.setType("image/jpeg");// 彩信附件类型
                        intent.setPackage("com.android.mms");
                        context.startActivity(intent);
                    } catch (Exception e3) {
                        // for huawei
                        try {
                            intent = new Intent(Intent.ACTION_SEND);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                            intent.putExtra(Intent.EXTRA_SUBJECT, subject); // 彩信的主题
                            intent.putExtra(Intent.EXTRA_TEXT, text);

                            // intent.putExtra("subject", subject); // 彩信的主题
                            // // intent.putExtra("address", "10086");
                            // //彩信发送目的号码
                            // intent.putExtra("sms_body", text); // 彩信中文字内容
                            // intent.putExtra(Intent.EXTRA_TEXT, text);
                            intent.setType("image/*");// 彩信附件类型
                            intent.setPackage("com.huawei.message");
                            context.startActivity(intent);
                        } catch (Exception e4) {

                            try {// for lenovo
                                intent = new Intent(Intent.ACTION_SEND);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                                // intent.putExtra(Intent.EXTRA_SUBJECT,
                                // subject); // 彩信的主题
                                // intent.putExtra(Intent.EXTRA_TEXT, text);

                                intent.putExtra("subject", subject); // 彩信的主题
                                // // intent.putExtra("address", "10086");
                                // //彩信发送目的号码
                                intent.putExtra("sms_body", text); // 彩信中文字内容
                                // intent.putExtra(Intent.EXTRA_TEXT, text);
                                intent.setType("image/*");// 彩信附件类型
                                intent.setPackage("com.lenovo.ideafriend");
                                context.startActivity(intent);
                            } catch (Exception e5) {

                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 发送彩信，附件为图片格式
     *
     * @param context
     * @param subject     彩信主题
     * @param text        彩信内容
     * @param imgFilePath 分享的图片绝对路径
     * @return true or false
     */
    public static boolean sendMMSImgPath(Context context, String subject,
                                         String text, String imgFilePath) {
        Intent intent = null;
        Uri uri = null;
        File srcFile = new File(imgFilePath);
        uri = UriUtil.getUriForShare(context, srcFile);
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                String defaultSmsPackageName = Telephony.Sms
                        .getDefaultSmsPackage(context);
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                // Can be null in case that there is no default, then the user
                // would be able to choose any app that support this intent.
                if (defaultSmsPackageName != null) {
                    intent.setPackage(defaultSmsPackageName);
                }
            } else {
                intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                intent.putExtra("subject", subject); // 彩信的主题
                // intent.putExtra("address", "10086"); //彩信发送目的号码
                intent.putExtra("sms_body", text); // 彩信中文字内容
                // intent.putExtra(Intent.EXTRA_TEXT, text);

                intent.setType("image/*");// 彩信附件图片类型
                intent.setPackage("com.android.mms");

                // intent.setClassName("com.android.mms",
                // "com.android.mms.ui.ComposeMessageActivity");
            }
            context.startActivity(intent);
        } catch (Exception e) {
            // for sony
            try {
                intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                intent.putExtra(Intent.EXTRA_SUBJECT, subject); // 彩信的主题
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.setType("image/jpeg");// 彩信附件类型
                intent.setPackage("com.sonyericsson.conversations");
                context.startActivity(intent);
            } catch (Exception e1) {
                // for thc one
                try {
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                    intent.putExtra("subject", subject); // 彩信的主题
                    intent.putExtra("sms_body", text); // 彩信中文字内容
                    intent.setType("image/jpeg");// 彩信附件图片类型
                    intent.setClassName("com.android.mms",
                            "com.android.mms.ui.ComposeMessageActivity");
                    context.startActivity(intent);
                } catch (Exception e2) {
                    // for htc sense
                    try {
                        intent = new Intent(Intent.ACTION_SEND);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                        intent.putExtra("subject", subject); // 彩信的主题
                        // intent.putExtra("address", "10086"); //彩信发送目的号码
                        intent.putExtra("sms_body", text); // 彩信中文字内容
                        // intent.putExtra(Intent.EXTRA_TEXT, text);
                        intent.setType("image/jpeg");// 彩信附件类型
                        intent.setPackage("com.android.mms");
                        context.startActivity(intent);
                    } catch (Exception e3) {
                        try {
                            intent = new Intent(Intent.ACTION_SEND);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                            intent.putExtra("subject", subject); // 彩信的主题
                            // intent.putExtra("address", "10086"); //彩信发送目的号码
                            intent.putExtra("sms_body", text); // 彩信中文字内容
                            // intent.putExtra(Intent.EXTRA_TEXT, text);
                            intent.setType("image/*");// 彩信附件类型
                            intent.setPackage("com.huawei.message");
                            context.startActivity(intent);
                        } catch (Exception e4) {
                            try {// for lenovo
                                intent = new Intent(Intent.ACTION_SEND);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Intent.EXTRA_STREAM, uri);// uri为你的附件的uri
                                intent.putExtra("subject", subject); // 彩信的主题
                                // //彩信发送目的号码
                                intent.putExtra("sms_body", text); // 彩信中文字内容
                                intent.setType("image/*");// 彩信附件类型
                                intent.setPackage("com.lenovo.ideafriend");
                                context.startActivity(intent);
                            } catch (Exception e5) {

                                return false;
                            }
                        }
                    }
                }

            }
        }
        return true;
    }

    /**
     * 发送彩信，附件为视频格式
     *
     * @param activity
     * @param subject       彩信主题
     * @param text          彩信内容
     * @param videoFilePath 彩信附件中的视频绝对路径
     * @return true or false
     */
    public static boolean sendMMSVideo(Activity activity, String subject,
                                       String text, String videoFilePath) {
        Intent intent = null;
        Uri fileUri = null;
        try {
            File file = new File(videoFilePath);
            fileUri = UriUtil.getUriForShare(activity, file);

            if (Build.VERSION.SDK_INT >= 19) {
                String defaultSmsPackageName = Telephony.Sms
                        .getDefaultSmsPackage(activity);
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("video/*");
                intent.putExtra(Intent.EXTRA_STREAM, fileUri);// uri为你的附件的uri
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                // Can be null in case that there is no default, then the user
                // would be able to choose any app that support this intent.
                if (defaultSmsPackageName != null) {
                    intent.setPackage(defaultSmsPackageName);
                }
            } else {
                intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, fileUri);// uri为你的附件的uri
                intent.putExtra("subject", subject); // 彩信的主题
                // intent.putExtra("address", "10086"); //彩信发送目的号码
                intent.putExtra("sms_body", text); // 彩信中文字内容
                // intent.putExtra(Intent.EXTRA_TEXT, text);

                intent.setType("video/*");// 彩信附件视频类型
                intent.setPackage("com.android.mms");

                // intent.setClassName("com.android.mms",
                // "com.android.mms.ui.ComposeMessageActivity");
            }
            activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            // for htc
            try {
                intent = new Intent("android.intent.action.SEND_MSG");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, fileUri);// uri为你的附件的uri
                intent.putExtra("subject", subject); // 彩信的主题
                // intent.putExtra("address", "10086"); //彩信发送目的号码
                intent.putExtra("sms_body", text); // 彩信中文字内容
                // intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.setType("video/*");// 彩信附件视频类型
                intent.setPackage("com.android.mms");
                activity.startActivity(intent);
            } catch (Exception e2) {
                // for sony
                try {
                    intent = new Intent("android.intent.action.SEND");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_STREAM, fileUri);// uri为你的附件的uri
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject); // 彩信的主题
                    intent.putExtra(Intent.EXTRA_TEXT, text);
                    intent.setType("video/*");// 彩信附件视频类型
                    intent.setPackage("com.sonyericsson.conversations");
                    activity.startActivity(intent);
                } catch (Exception e3) {
                    try {
                        intent = new Intent(Intent.ACTION_SEND);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Intent.EXTRA_STREAM, fileUri);// uri为你的附件的uri
                        intent.putExtra("subject", subject); // 彩信的主题
                        // intent.putExtra("address", "10086"); //彩信发送目的号码
                        intent.putExtra("sms_body", text); // 彩信中文字内容
                        // intent.putExtra(Intent.EXTRA_TEXT, text);
                        intent.setType("video/*");// 彩信附件类型
                        intent.setPackage("com.huawei.message");
                        activity.startActivity(intent);
                    } catch (Exception e4) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 发送彩信，附件为视频格式
     *
     * @param activity
     * @param subject  彩信主题
     * @param text     彩信内容
     * @param fileUri
     * @return true or false
     */
    public static boolean sendMMSVideo(Activity activity, String subject,
                                       String text, Uri fileUri) {
        Intent intent = null;
        try {

            if (Build.VERSION.SDK_INT >= 19) {
                String defaultSmsPackageName = Telephony.Sms
                        .getDefaultSmsPackage(activity);
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("video/*");
                intent.putExtra(Intent.EXTRA_STREAM, fileUri);// uri为你的附件的uri
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                // Can be null in case that there is no default, then the user
                // would be able to choose any app that support this intent.
                if (defaultSmsPackageName != null) {
                    intent.setPackage(defaultSmsPackageName);
                }
            } else {
                intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, fileUri);// uri为你的附件的uri
                intent.putExtra("subject", subject); // 彩信的主题
                // intent.putExtra("address", "10086"); //彩信发送目的号码
                intent.putExtra("sms_body", text); // 彩信中文字内容
                // intent.putExtra(Intent.EXTRA_TEXT, text);

                intent.setType("video/*");// 彩信附件视频类型
                intent.setPackage("com.android.mms");

                // intent.setClassName("com.android.mms",
                // "com.android.mms.ui.ComposeMessageActivity");
            }
            activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            // for htc
            try {
                intent = new Intent("android.intent.action.SEND_MSG");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, fileUri);// uri为你的附件的uri
                intent.putExtra("subject", subject); // 彩信的主题
                // intent.putExtra("address", "10086"); //彩信发送目的号码
                intent.putExtra("sms_body", text); // 彩信中文字内容
                // intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.setType("video/*");// 彩信附件视频类型
                intent.setPackage("com.android.mms");
                activity.startActivity(intent);
            } catch (Exception e2) {
                // for sony
                try {
                    intent = new Intent("android.intent.action.SEND");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_STREAM, fileUri);// uri为你的附件的uri
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject); // 彩信的主题
                    intent.putExtra(Intent.EXTRA_TEXT, text);
                    intent.setType("video/*");// 彩信附件视频类型
                    intent.setPackage("com.sonyericsson.conversations");
                    activity.startActivity(intent);
                } catch (Exception e3) {
                    try {
                        intent = new Intent(Intent.ACTION_SEND);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Intent.EXTRA_STREAM, fileUri);// uri为你的附件的uri
                        intent.putExtra("subject", subject); // 彩信的主题
                        // intent.putExtra("address", "10086"); //彩信发送目的号码
                        intent.putExtra("sms_body", text); // 彩信中文字内容
                        // intent.putExtra(Intent.EXTRA_TEXT, text);
                        intent.setType("video/*");// 彩信附件类型
                        intent.setPackage("com.huawei.message");
                        activity.startActivity(intent);
                    } catch (Exception e4) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
