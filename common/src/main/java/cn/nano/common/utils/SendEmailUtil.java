package cn.nano.common.utils;

public class SendEmailUtil {

    /*	*//**
     * /** 发送email 不带附件
     *
     * @param context
     * @param emailSubject
     *            邮件主题
     * @param emailBody
     *            邮件内容
     * @param emailAddress
     *            接受人的邮件地址
     *//*
	public static void sendTxtEmail(Context context, String emailSubject,
			String emailBody, String[] emailAddress) {
		if (context == null) {
			return;
		}
		Intent intent = null;
		try {
			intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:" + ""));
			intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject); // 邮件主题名
			intent.putExtra(Intent.EXTRA_TEXT, emailBody); // 邮件主题名
			if (!TextUtils.isEmpty(emailAddress.toString())) {
				intent.putExtra(Intent.EXTRA_EMAIL, emailAddress);// 发送对象
			}
			intent.setType("text/plain");
			intent.setType("message/rfc822");
			context.startActivity(intent);
		} catch (Exception e) {
			if (DeviceUtil.isCoolPad()) {//
				intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"
						+ emailAddress));
				context.startActivity(intent);
			} else {// 默认情况下，直接调用email的欢迎界面
				Intent mIntent = new Intent();
				ComponentName comp = null;
				if (DeviceUtil.isHTC_X515()) {
					comp = new ComponentName("com.htc.android.mail",
							"com.htc.android.mail.activity.Welcome");
				} else {
					comp = new ComponentName("com.android.email",
							"com.android.email.activity.Welcome");
				}
				mIntent.setComponent(comp);
				mIntent.setAction("android.intent.action.MAIN");
				try {
					context.startActivity(mIntent);
				} catch (Exception e2) {
					e2.printStackTrace();
//					Toast.makeText(context,
//							context.getString(R.string.no_email_application),
//							Toast.LENGTH_SHORT).show();
				}

			}

		}

	}

	*//**
     * /** 发送email 不带附件
     *
     * @param context
     * @param emailSubject
     *            邮件主题
     * @param emailBody
     *            邮件内容
     * @param emailAddress
     *            接受人的邮件地址
     *//*
	public static void sendTxtEmail(Context context, String emailSubject,
			String emailBody, String emailAddress) {
		if (context == null) {
			return;
		}
		Intent intent = null;
		try {
			intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"
					+ emailAddress));
			intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject); // 邮件主题名
			intent.putExtra(Intent.EXTRA_TEXT, emailBody); // 邮件主题名
			if (!TextUtils.isEmpty(emailAddress)) {
				String[] mailto = new String[] { emailAddress };
				intent.putExtra(Intent.EXTRA_EMAIL, mailto);// 发送对象
			}
			intent.setType("text/plain");
			intent.setType("message/rfc822");
			context.startActivity(intent);
		} catch (Exception e) {
			if (DeviceUtil.isCoolPad()) {//
				intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"
						+ emailAddress));
				context.startActivity(intent);
			} else {// 默认情况下，直接调用email的欢迎界面
				Intent mIntent = new Intent();
				ComponentName comp = null;
				if (DeviceUtil.isHTC_X515()) {
					comp = new ComponentName("com.htc.android.mail",
							"com.htc.android.mail.activity.Welcome");
				} else {
					comp = new ComponentName("com.android.email",
							"com.android.email.activity.Welcome");
				}
				mIntent.setComponent(comp);
				mIntent.setAction("android.intent.action.MAIN");
				try {
					context.startActivity(mIntent);
				} catch (Exception e2) {
					e2.printStackTrace();
//					Toast.makeText(context,
//							context.getString(R.string.no_email_application),
//							Toast.LENGTH_SHORT).show();
				}

			}

		}

	}

	*//**
     * /** 发送email 不带附件
     *
     * @param context
     * @param emailSubject
     *            邮件主题
     * @param builder
     * @param emailAddress
     *            接受人的邮件地址
     *//*
	public static void sendTxtUrlEmail(Context context, String emailSubject,
			SpannableStringBuilder builder, String emailAddress) {
		if (context == null) {
			return;
		}
		Intent intent = null;
		try {
			intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"
					+ emailAddress));
			intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject); // 邮件主题名
			intent.putExtra(Intent.EXTRA_TEXT, builder); // 邮件主题名
			if (!TextUtils.isEmpty(emailAddress)) {
				String[] mailto = new String[] { emailAddress };
				intent.putExtra(Intent.EXTRA_EMAIL, mailto);// 发送对象
			}
			intent.setType("text/plain");
			intent.setType("message/rfc822");
			context.startActivity(intent);
		} catch (Exception e) {
			if (DeviceUtil.isCoolPad()) {//
				intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"
						+ emailAddress));
				context.startActivity(intent);
			} else {// 默认情况下，直接调用email的欢迎界面
				Intent mIntent = new Intent();
				ComponentName comp = null;
				if (DeviceUtil.isHTC_X515()) {
					comp = new ComponentName("com.htc.android.mail",
							"com.htc.android.mail.activity.Welcome");
				} else {
					comp = new ComponentName("com.android.email",
							"com.android.email.activity.Welcome");
				}
				mIntent.setComponent(comp);
				mIntent.setAction("android.intent.action.MAIN");
				try {
					context.startActivity(mIntent);
				} catch (Exception e2) {
					e2.printStackTrace();
//					Toast.makeText(context,
//							context.getString(R.string.no_email_application),
//							Toast.LENGTH_SHORT).show();
				}

			}

		}

	}

	*//**
     * 发送email 带附件
     *
     * @param activity
     * @param fileName
     *            文件全路径
     * @param emailSubject
     *            邮件主题
     * @param emailBody
     *            邮件内容
     * @param emailAddress
     *            接受的邮件地址
     * @param requestCode
     *            调用第三方客户端的CODE
     *//*
	public static void sendEmailImageForResult(Activity activity,
			String fileName, String emailSubject, String emailBody,
			String emailAddress, int requestCode) {
		if (activity == null) {
			return;
		}
		Intent intent = null;
		try {
			intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"
					+ emailAddress));
			String[] mailto = new String[] { emailAddress };
			intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject); // 邮件主题名
			intent.putExtra(Intent.EXTRA_EMAIL, mailto);// 发送对象
			intent.putExtra(Intent.EXTRA_TEXT, emailBody); // 邮件正文
			if (fileName != null) {
				Uri uri = Uri.fromFile(new File(fileName));
				intent.putExtra(Intent.EXTRA_STREAM, uri);//
			}

			intent.setType("image/*"); // 其他的均使用流当做二进制数据来发送
			intent.setType("message/rfc822");
			activity.startActivityForResult(intent, requestCode);
		} catch (Exception e) {
			if (DeviceUtil.isCoolPad()) {//
				intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"
						+ emailAddress));
				activity.startActivityForResult(intent, requestCode);
			} else {// 默认情况下，直接调用email的欢迎界面
				Intent mIntent = new Intent();
				ComponentName comp = null;

				comp = new ComponentName("com.android.email",
						"com.android.email.activity.Welcome");

				mIntent.setComponent(comp);
				mIntent.setAction("android.intent.action.MAIN");
				try {
					activity.startActivityForResult(mIntent, requestCode);
				} catch (Exception e2) {
//					Toast.makeText(activity,
//							activity.getString(R.string.no_email_application),
//							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	*//**
     * 发送email 带图片附件
     *
     * @param context
     *            上下文对象
     * @param emailSubject
     *            邮件主题
     * @param emailBody
     *            邮件内容
     * @param uri
     * @return
     *//*
	public static boolean sendEmailImage(Context context, String emailSubject,
			String emailBody, Uri uri) {
		Intent intent = null;
		try {

			// uri = Uri.parse(MediaStore.Images.Media.insertImage(
			// context.getContentResolver(), sharebitmap, null, null));
			intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
			intent.putExtra(Intent.EXTRA_TEXT, emailBody);
			intent.putExtra(Intent.EXTRA_STREAM, uri);
			intent.setType("image/jpeg"); // 其他的均使用流当做二进制数据来发送
			intent.setType("message/rfc822");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			context.startActivity(intent);
		} catch (Exception e) {
			if (uri == null) {
				return false;
			}
			if (DeviceUtil.isCoolPad()) {//
				intent = new Intent(Intent.ACTION_SENDTO);
				context.startActivity(intent);
			} else {// 默认情况下，直接调用email的欢迎界面
				Intent mIntent = new Intent();
				ComponentName comp = null;

				comp = new ComponentName("com.android.email",
						"com.android.email.activity.Welcome");

				mIntent.setComponent(comp);
				mIntent.setAction("android.intent.action.MAIN");
				try {
					context.startActivity(mIntent);
				} catch (Exception e2) {
					return false;
				}

			}
		}
		return true;

	}

	*//**
     * 发送email 带图片附件
     *
     * @param context
     *            上下文对象
     * @param emailSubject
     *            邮件主题
     * @param emailBody
     *            邮件内容
     * @param fileNameType
     *            附件附件名称
     * @param sharedBitmap
     *            邮件附件内容
     * @return
     *//*
	public static boolean sendEmailImage(Context context, String emailSubject, String emailBody,
									String fileNameType, Bitmap	sharedBitmap, String strCacheFileDir)
	{
		Uri uri = null;
		Intent intent = null;
		try {
			uri = UriUtil.getUri(context, sharedBitmap, fileNameType, strCacheFileDir);
			// uri = Uri.parse(MediaStore.Images.Media.insertImage(
			// context.getContentResolver(), sharebitmap, null, null));
			intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
			intent.putExtra(Intent.EXTRA_TEXT, emailBody);
			intent.putExtra(Intent.EXTRA_STREAM, uri);
			intent.setType("image/jpeg"); // 其他的均使用流当做二进制数据来发送
			intent.setType("message/rfc822");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			context.startActivity(intent);
		} catch (Exception e) {
			if (uri == null) {
				return false;
			}
			if (DeviceUtil.isCoolPad()) {//
				intent = new Intent(Intent.ACTION_SENDTO);
				context.startActivity(intent);
			} else {// 默认情况下，直接调用email的欢迎界面
				Intent mIntent = new Intent();
				ComponentName comp = null;

				comp = new ComponentName("com.android.email",
						"com.android.email.activity.Welcome");

				mIntent.setComponent(comp);
				mIntent.setAction("android.intent.action.MAIN");
				try {
					context.startActivity(mIntent);
				} catch (Exception e2) {
					return false;
				}

			}
		}
		return true;

	}

	*//**
     * 发送email 带图片附件
     *
     * @param context
     *            上下文对象
     * @param emailSubject
     *            邮件主题
     * @param emailBody
     *            邮件内容
     * @param imgFilePath
     *            邮件附件图片路径
     * @return
     *//*
	public static boolean sendEmailImage(Context context, String emailSubject,
			String emailBody, String imgFilePath) {
		Uri fileUri = null;
		Intent intent = null;
		File srcFile = null;
		try {
			srcFile = new File(imgFilePath);
			fileUri = UriUtil.getUri(srcFile);
			intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
			intent.putExtra(Intent.EXTRA_TEXT, emailBody);
			intent.putExtra(Intent.EXTRA_STREAM, fileUri);
			intent.setType("image/jpeg"); // 其他的均使用流当做二进制数据来发送
			intent.setType("message/rfc822");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			context.startActivity(intent);
		} catch (Exception e) {
			if (fileUri == null) {
				return false;
			}
			if (DeviceUtil.isCoolPad()) {//
				intent = new Intent(Intent.ACTION_SENDTO);
				context.startActivity(intent);
			} else {// 默认情况下，直接调用email的欢迎界面
				Intent mIntent = new Intent();
				ComponentName comp = null;

				comp = new ComponentName("com.android.email",
						"com.android.email.activity.Welcome");

				mIntent.setComponent(comp);
				mIntent.setAction("android.intent.action.MAIN");
				try {
					context.startActivity(mIntent);
				} catch (Exception e2) {
					return false;
				}

			}
		}
		return true;

	}

	*//**
     * 发送彩信，附件为视频格式
     *
     * @param activity
     * @param emailSubject
     * @param emailBody
     * @param videoFilePath
     *            彩信附件中的视频绝对路径
     * @return true or false
     *//*
	public static boolean sendEmailVideo(Activity activity,
			String emailSubject, String emailBody, String videoFilePath) {
		Uri fileUri = null;
		Intent intent = null;

		try {
			File file = new File(videoFilePath);
			fileUri = UriUtil.getUri(file);
			// fileUri = UriUtil.queryUriForVideo(activity, file);
			intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
			intent.putExtra(Intent.EXTRA_TEXT, emailBody);
			intent.putExtra(Intent.EXTRA_STREAM, fileUri);
			intent.setType("video/*"); // 其他的均使用流当做二进制数据来发送
			intent.setType("message/rfc822");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			activity.startActivity(intent);
		} catch (Exception e) {
			if (fileUri == null) {
				return false;
			}
			if (DeviceUtil.isCoolPad()) {//
				intent = new Intent(Intent.ACTION_SENDTO);
				activity.startActivity(intent);
			} else {// 默认情况下，直接调用email的欢迎界面
				Intent mIntent = new Intent();
				ComponentName comp = null;
				comp = new ComponentName("com.android.email",
						"com.android.email.activity.Welcome");
				mIntent.setComponent(comp);
				mIntent.setAction("android.intent.action.MAIN");
				try {
					activity.startActivity(mIntent);
				} catch (Exception e2) {
					return false;
				}

			}
		}
		return true;

	}

	*//**
     * 发送彩信，附件为视频格式
     *
     * @param activity
     * @param emailSubject
     * @param emailBody
     * @param fileUri
     *            彩信附件中的视频uri
     * @return true or false
     *//*
	public static boolean sendEmailVideo(Activity activity,
			String emailSubject, String emailBody, Uri fileUri) {
		Intent intent = null;

		try {
			// fileUri = UriUtil.queryUriForVideo(activity, file);
			intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
			intent.putExtra(Intent.EXTRA_TEXT, emailBody);
			intent.putExtra(Intent.EXTRA_STREAM, fileUri);
			intent.setType("video/*"); // 其他的均使用流当做二进制数据来发送
			intent.setType("message/rfc822");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			activity.startActivity(intent);
		} catch (Exception e) {
			if (fileUri == null) {
				return false;
			}
			if (DeviceUtil.isCoolPad()) {//
				intent = new Intent(Intent.ACTION_SENDTO);
				activity.startActivity(intent);
			} else {// 默认情况下，直接调用email的欢迎界面
				Intent mIntent = new Intent();
				ComponentName comp = null;
				comp = new ComponentName("com.android.email",
						"com.android.email.activity.Welcome");
				mIntent.setComponent(comp);
				mIntent.setAction("android.intent.action.MAIN");
				try {
					activity.startActivity(mIntent);
				} catch (Exception e2) {
					return false;
				}

			}
		}
		return true;
	}*/
}
