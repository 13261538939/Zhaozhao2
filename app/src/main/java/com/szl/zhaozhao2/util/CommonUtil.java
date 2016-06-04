package com.szl.zhaozhao2.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.format.Time;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.szl.zhaozhao2.R;
import com.szl.zhaozhao2.application.DApplication;
import com.szl.zhaozhao2.log.LogPrinter;
import com.szl.zhaozhao2.ui.activity.MainActivity;

/**
 * 常用工具类
 * 
 * @author HH
 */
public class CommonUtil {
	private static final String TAG = "CommonUtil";

	public static final SimpleDateFormat COMMENT_DATE_FORMAT = new SimpleDateFormat(
			"MM-dd", Locale.CHINA);
	public static final SimpleDateFormat LOCATION_DATE_FORMAT = new SimpleDateFormat(
			"MM-dd HH:mm", Locale.CHINA);
	public static final SimpleDateFormat HM_DATE_FORMAT = new SimpleDateFormat(
			"HH:mm", Locale.CHINA);
	private static final String JUST_NOW = "刚刚";
	private static final String INNER_ONE_HOUR = "分钟前";
	private static final String INNER_HOUR = "小时前";
	private static final String YESTERDAY = "昨天";

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public static boolean isAppOnForeground(Context ctx) {
		ActivityManager activityManager = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = ctx.getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 根据URL获取图片在文件系统中的文件名
	 * 
	 * @param url
	 * @return
	 */
	public static String getTempFileName(String url) {

		String tempFileName = "";
		try {
			if (null != url && !"".equals(url.trim())) {
				url = url.replace("_2.jpg", "_1.jpg");
				// http://r1.sinaimg.cn/10260/2011/0329/4f/0/5037778/60x60x100x0x0x1.jpg
				// change to
				// http___r1.sinaimg.cn_10260_2011_0329_4f_0_5037778_60x60x100x0x0x1.jpg
				tempFileName = url.replace('/', '_').replace(':', '_')
						.replace(',', '_').replace('?', '_');
				if (!tempFileName.contains(".jpg")
						&& !tempFileName.contains(".png")
						&& !tempFileName.contains(".gif")) {
					tempFileName += ".jpg";
				}
			}
			if (tempFileName.length() > 130) {
				int index = tempFileName.lastIndexOf(".");
				String suffix = tempFileName.substring(index,
						tempFileName.length());
				tempFileName = MD5.hexdigest(tempFileName);
				tempFileName += suffix;
			}
		} catch (Exception e) {
			LogPrinter.e(TAG, e.getMessage(), e.getCause());
		}
		return tempFileName;
	}

	/**
	 * 获取手机的Ip地址
	 */
	public static String getLocalIpAddress() {
		try {
			// 检查Wifi状态
			if (isWifi(DApplication.getApplication())) {
				WifiManager wm = (WifiManager) DApplication.getApplication()
						.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wi = wm.getConnectionInfo();
				// 获取32位整型IP地址
				int ipAdd = wi.getIpAddress();
				// 把整型地址转换成“*.*.*.*”地址
				String ip = intToIp(ipAdd);
				LogPrinter.d("ipwifi", ip);
				return ip;

			} else {
				for (Enumeration<NetworkInterface> en = NetworkInterface
						.getNetworkInterfaces(); en.hasMoreElements();) {
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf
							.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							LogPrinter.d("ip3g", inetAddress.getHostAddress()
									.toString());
							return inetAddress.getHostAddress().toString();
						}
					}
				}
			}
		} catch (SocketException ex) {
			LogPrinter.e("WifiPreference IpAddress", ex.toString());
		}
		return null;

	}

	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(float dpValue) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpValue, DApplication.getApplication().getResources()
						.getDisplayMetrics());
		// final float scale =
		// context.getResources().getDisplayMetrics().density;
		// return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object object) {
		if (object == null)
			return true;
		if (object instanceof Uri)
			return (((Uri) object).toString()).length() == 0;
		if (object instanceof CharSequence)
			return ((CharSequence) object).length() == 0;
		if (object instanceof Collection)
			return ((Collection) object).isEmpty();
		if (object instanceof Map)
			return ((Map) object).isEmpty();
		if (object.getClass().isArray())
			return java.lang.reflect.Array.getLength(object) == 0;
		return false;
	}

	public static boolean isNotEmpty(Object object) {
		return !isEmpty(object);
	}

	public static String generateMachineId() {
		Context context = DApplication.getApplication()
				.getApplicationContext();
		TelephonyManager TelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
		// #debug
		LogPrinter.d(TAG, "szImei: " + szImei);

		String szDevIDShort = "35" // we make this look like a valid IMEI
				+ Build.BOARD.length() % 10 + Build.BRAND.length()
				% 10
				+ Build.CPU_ABI.length() % 10 + Build.DEVICE.length()
				% 10
				+ Build.DISPLAY.length() % 10 + Build.HOST.length()
				% 10
				+ Build.ID.length() % 10 + Build.MANUFACTURER.length()
				% 10
				+ Build.MODEL.length() % 10 + Build.PRODUCT.length()
				% 10
				+ Build.TAGS.length() % 10 + Build.TYPE.length()
				% 10
				+ Build.USER.length() % 10; // 13 digits
		// #debug
		LogPrinter.d(TAG, "szDevIDShort: " + szDevIDShort);

		String szAndroidID = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		// #debug
		LogPrinter.d(TAG, "szAndroidID: " + szAndroidID);

		String szLongID = szImei + szDevIDShort + szAndroidID; // compute md5
		// #debug
		LogPrinter.d(TAG, "szLongID: " + szLongID);

		if (isEmpty(szAndroidID)) {
			szAndroidID = "unknown";
		}

		return szAndroidID;
	}

	private static String clientVersion = null;

	public static String getVersion() {
		if (clientVersion == null) {
			Context context = DApplication.getApplication()
					.getApplicationContext();
			String version = "";
			try {
				version = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				// #debug
				LogPrinter.e(TAG, e.getMessage(), e.getCause());
			}
			clientVersion = version;
		}
		return clientVersion;
	}

	private static int clientVersionCode = 0;

	public static int getVersionCode() {
		if (clientVersionCode == 0) {
			Context context = DApplication.getApplication()
					.getApplicationContext();
			int version = 0;
			try {
				version = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0).versionCode;
			} catch (NameNotFoundException e) {
				// #debug
				LogPrinter.e(TAG, e.getMessage(), e.getCause());
			}
			clientVersionCode = version;
		}
		return clientVersionCode;
	}

	public static String getAppDataFromManifest(Context context, String key) {
		String ret = null;
		try {
			ApplicationInfo appinfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			ret = appinfo.metaData.getString(key);
		} catch (NameNotFoundException e) {
			// #debug
			LogPrinter.e(TAG, e.getMessage(), e.getCause());
		}
		return ret;
	}

	private static String appWM = null;

	public static String getAppWM() {
		try {
			if (appWM == null) {
				appWM = getAppDataFromManifest(DApplication.getApplication()
						.getApplicationContext(), "UMENG_CHANNEL");
				appWM = appWM.substring(1);
				return appWM;
			} else {
				return appWM;
			}
		} catch (NullPointerException e) {
			return appWM;
		}
	}

	// private static String appFrom = null;
	//
	// public static String getAppFrom() {
	// try {
	// if (appFrom == null) {
	// appFrom = getAppDataFromManifest(AHApplication.getApplication()
	// .getApplicationContext(), "UMENG_CHANNEL");
	// appFrom = appFrom.substring(1);
	// return appFrom;
	// } else {
	// return appFrom;
	// }
	// } catch (NullPointerException e) {
	// return appFrom;
	// }
	// }

	private static Toast mToast = null;

	public static void showToast(Context context, String text) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(R.layout.my_toast, null);
		TextView my_toast_message = (TextView) layout
				.findViewById(R.id.my_toast_message);
		my_toast_message.setBackgroundResource(R.drawable.corners_toast);
		my_toast_message.getBackground().setAlpha(130);
		if (null == mToast) {
			mToast = new Toast(context);
			my_toast_message.setText("");
			mToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0,
					200);
			mToast.setDuration(Toast.LENGTH_LONG);
		}
		if (null != text) {
			my_toast_message.setText(text);
			mToast.setView(layout);
			mToast.show();
		}
	}

	public final static boolean isScreenLocked() {
		Context ctx = DApplication.getApplication().getApplicationContext();
		android.app.KeyguardManager mKeyguardManager = (KeyguardManager) ctx
				.getSystemService(Context.KEYGUARD_SERVICE);
		return mKeyguardManager.inKeyguardRestrictedInputMode();
	}

	public static String[] toPrimitive(ArrayList<String> array) {
		if (array == null) {
			return null;
		} else if (array.size() == 0) {
			return new String[0];
		}
		final String[] result = new String[array.size()];
		for (int i = 0; i < array.size(); i++) {
			result[i] = array.get(i);
		}
		return result;
	}

	/**
	 * 获取圆角bitmap
	 * 
	 * @param bitmap
	 * @param roundPX
	 * @return
	 */
	public static Bitmap getRCB(Bitmap bitmap, float roundPX)
	// RCB means Rounded Corner Bitmap
	{
		Bitmap dstbmp = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(dstbmp);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return dstbmp;
	}

	/**
	 * 把inputStream转换为String
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static String stream2String(InputStream inputStream)
			throws IOException {
		String str = "";
		// ByteArrayOutputStream相当于内存输出流
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		// 将输入流转移到内存输出流中
		while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
			out.write(buffer, 0, len);
		}
		// 将内存流转换为字符串
		str = new String(out.toByteArray());
		return str;
	}

	/**
	 * 判断是否移动网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean is2gOr3g(Context context) {
		try {
			ConnectivityManager conMan = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			// mobile 3G Data Network
			State mobile = State.UNKNOWN;
			NetworkInfo networkInfo = conMan
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (networkInfo != null) {
				mobile = networkInfo.getState();
			}
			if (mobile == State.CONNECTED || mobile == State.CONNECTING)
				return true;
			return false;
		} catch (Exception e) {
			LogPrinter.e(TAG, e.getMessage(), e.getCause()); // To
																// change
																// body of
																// catch
																// statement
																// use
																// File |
			// Settings | File Templates.
		}
		return false;
	}

	/**
	 * 判断是否3G网络
	 * 
	 * @return
	 */
	public static boolean is3gNet(Context context) {
		try {
			ConnectivityManager conMan = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			// mobile 3G Data Network
			NetworkInfo networkInfo = conMan
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (networkInfo != null) {
				int subType = networkInfo.getSubtype();

				switch (subType) {
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					return false; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_CDMA:
					return false; // ~ 14-64 kbps
				case TelephonyManager.NETWORK_TYPE_EDGE:
					return false; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					return true; // ~ 400-1000 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					return true; // ~ 600-1400 kbps
				case TelephonyManager.NETWORK_TYPE_GPRS:
					return false; // ~ 100 kbps
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					return true; // ~ 2-14 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPA:
					return true; // ~ 700-1700 kbps
				case TelephonyManager.NETWORK_TYPE_HSUPA:
					return true; // ~ 1-23 Mbps
				case TelephonyManager.NETWORK_TYPE_UMTS:
					return true; // ~ 400-7000 kbps
					// Unknown
				case TelephonyManager.NETWORK_TYPE_UNKNOWN:
					return false;
				default:
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			LogPrinter.e(TAG, e.getMessage(), e.getCause()); // To
																// change
																// body of
																// catch
																// statement
																// use
																// File |
			// Settings | File Templates.
		}
		return false;
	}

	/**
	 * 判断是否wifi连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifi(Context context) {
		try {
			ConnectivityManager conMan = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			State wifi = State.UNKNOWN;
			NetworkInfo networkInfo = conMan
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (networkInfo != null) {
				wifi = networkInfo.getState();
			}
			if (wifi == State.CONNECTED || wifi == State.CONNECTING)
				return true;
			return false;
		} catch (Exception e) {
			LogPrinter.e(TAG, e.getMessage(), e.getCause());
		}
		return false;
	}

	/**
	 * 判断网络连接状况
	 * 
	 * @param aContext
	 * @return
	 */
	public static boolean isNetworkConnected(Context aContext) {
		boolean flag = false;
		try {
			ConnectivityManager conMan = (ConnectivityManager) aContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			// mobile 3G Data Network
			State mobile = State.UNKNOWN;
			State wifi = State.UNKNOWN;
			NetworkInfo networkInfo = conMan
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (networkInfo != null) {
				mobile = networkInfo.getState();
			}
			networkInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (networkInfo != null) {
				wifi = networkInfo.getState();
			}
			if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
				flag = true;

			}
			if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
				flag = true;

			}
		} catch (Exception e) {
			LogPrinter.e(TAG, e.getMessage(), e.getCause()); // To
																// change
																// body of
																// catch
																// statement
																// use
																// File |
			// Settings | File Templates.
		}
		return flag;
	}

	/**
	 * 判断是否安装某应用程序
	 */

	public static boolean appIsInstalled(String packageName) {
		try {
			PackageManager pm = DApplication.getApplication()
					.getPackageManager();
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			return false;
		}
		return true;
	}

	/**
	 * 获取string 所占字符的个数，汉字为1个，英文字母，符号为0.5个,用于统计转发微博字数不能超过140
	 * 
	 * @param str
	 * @return
	 */
	public static float getExactTextLength(String str) {
		float count = 0;
		String tempStr;

		for (int i = 0; i < str.length(); i++) {
			tempStr = String.valueOf(str.charAt(i));
			if (tempStr.getBytes().length == 3) {
				count++;
			} else {
				count = count + 0.5f;
			}
		}
		return count;
	}

	/**
	 * 获取SD卡
	 * 
	 * @return
	 */
	public static File getExternalStorage() {
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory();
		} else {
			return null;
		}
	}

	/**
	 * 把图片存储到本地
	 * 
	 * @param bitmap
	 * @return
	 */
	public static boolean save2Local(Bitmap bitmap, String path) {
		if (getExternalStorage() != null) {
			File file = new File(getExternalStorage() + path);
			if (file.exists()) {
				file.delete();
			} else {
				file.mkdirs();
			}
			try {
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
				return true;
			} catch (FileNotFoundException e) {
				LogPrinter.e(TAG, e.getMessage(), e.getCause());
				return false;
			} catch (IOException e) {
				LogPrinter.e(TAG, e.getMessage(), e.getCause());
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 从本地读取头像
	 * 
	 * @param pathString
	 * @return
	 */
	public static Bitmap getBitmapFromLocal(String path) {
		Bitmap bitmap = null;
		try {
			if (getExternalStorage() != null) {
				File file = new File(getExternalStorage() + path);
				if (file.exists()) {
					bitmap = BitmapFactory.decodeFile(path);
				}
			}
		} catch (Exception e) {
			LogPrinter.e(TAG, "getBitmapFromLocal error");
		}
		return bitmap;
	}

	/**
	 * 向本地写数据，以覆盖原有形式
	 */
	public static void saveString2LocalAsOver(String content, String fileName) {
		try {
			FileOutputStream fos = DApplication.getApplication()
					.openFileOutput(fileName, Context.MODE_PRIVATE);
			PrintStream ps = new PrintStream(fos);
			ps.print(content);
			ps.close();
		} catch (FileNotFoundException e) {
			LogPrinter.e(TAG, "saveString2Local FileNotFoundException");
		}
	}

	/**
	 * 向本地写数据,以追加形式
	 */
	public static void saveString2LocalAsAdd(String content, String fileName) {
		try {
			FileOutputStream fos = DApplication.getApplication()
					.openFileOutput(fileName, Context.MODE_APPEND);
			PrintStream ps = new PrintStream(fos);
			ps.print(content);
			ps.close();
		} catch (FileNotFoundException e) {
			LogPrinter.e(TAG, "saveString2Local FileNotFoundException");
		}
	}

	/**
	 * 获取SD卡剩余空间
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static long getRemainSize() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 日期转换为固定格式的时间戳
	 * 
	 * @param longtime
	 * @return
	 */
	// public static String getNowTime(long longtime) {
	// String temp_str = "";
	// // HH表示24小时制 如果换成hh表示12小时制
	// DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// try {
	// temp_str = sdf.format(longtime);
	// } catch (Exception e) {
	// temp_str = longtime + "";
	// }
	// return temp_str;
	// }

	/**
	 * 获取全部图片地址
	 * 
	 * @return
	 */
	public static List<String> listAlldir(Context context) {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		Uri uri = intent.getData();
		List<String> list = new ArrayList<String>();
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, proj, null,
				null, null);// managedQuery(uri, proj, null, null, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				String path = cursor.getString(0);
				list.add(new File(path).getAbsolutePath());
			}
		}
		Collections.reverse(list);// 倒序
		return list;
	}

	public static boolean isUnlockShow() {
		List<RunningTaskInfo> taskInfo = getRunningTaskInfos(1);
		if (taskInfo.size() > 0) {
			if ("com.iadiae.acquaintancehelp.pattern.UnlockGesturePasswordActivity"
					.equals(taskInfo.get(0).baseActivity.getClassName())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static void turnToMain() {
		List<RunningTaskInfo> taskInfo = getRunningTaskInfos(1);
		if (taskInfo.size() > 0) {
			LogPrinter.d("taskInfo", "baseActivity: "
					+ taskInfo.get(0).baseActivity.getClassName());
			LogPrinter.d("taskInfo", "num: " + taskInfo.get(0).numActivities);
			if (!"com.iadiae.acquaintancehelp.ui.activity.MainActivity"
					.equals(taskInfo.get(0).baseActivity.getClassName())) {
				Intent intent = new Intent();
				if (DApplication.isLogined) {
					intent.setClass(DApplication.getApplication(),
							MainActivity.class);
				} else {
//					intent.setClass(DApplication.getApplication(),
//							LoginActivity.class);
				}
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				DApplication.getApplication().startActivity(intent);
			}
		}
	}

	public static void turnToSplash() {
		List<RunningTaskInfo> taskInfo = getRunningTaskInfos(1);
		if (taskInfo.size() > 0) {
			LogPrinter.d("taskInfo", "baseActivity: "
					+ taskInfo.get(0).baseActivity.getClassName());
			LogPrinter.d("taskInfo", "num: " + taskInfo.get(0).numActivities);
			if (!"com.iadiae.acquaintancehelp.ui.activity.MainActivity"
					.equals(taskInfo.get(0).baseActivity.getClassName())) {
				Intent intent = new Intent();
				if (DApplication.isLogined) {
					intent.setClass(DApplication.getApplication(),
							MainActivity.class);
				} else {
//					intent.setClass(DApplication.getApplication(),
//							LoginActivity.class);
				}
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				DApplication.getApplication().startActivity(intent);
			}
		}
	}

	public static List<RunningTaskInfo> getRunningTaskInfos(int maxNum) {
		ActivityManager manager = (ActivityManager) DApplication
				.getApplication().getSystemService(Context.ACTIVITY_SERVICE);
		// crash id 11691，ActivityManager.getRunningTasks内部NPE，机型是Nexus 4，系统4.4
		try {
			return manager.getRunningTasks(maxNum);
		} catch (Exception e) {
			return new ArrayList<ActivityManager.RunningTaskInfo>(0);
		}
	}

	/**
	 * 判断字符串是否以".gif"结尾
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEndWithGif(String str) {
		if (str != null) {
			return str.toLowerCase(Locale.getDefault()).endsWith(".gif");
		}
		return false;
	}

	/**
	 * 位置时间转换
	 * 
	 * @param curTime
	 * @param originTime
	 * @return
	 */
	public static String getTimeLocationLength(long originTime) {
		originTime /= 1000;// 转换为以秒秒
		String timeLength = "";
		long curTime = getCurrentTime();
		long timeOffset = curTime - originTime;
		if (timeOffset < 60) {
			timeLength = JUST_NOW;

		} else if (timeOffset > 60 && timeOffset < 60 * 60) {
			long time = timeOffset / 60;
			String prefx = INNER_ONE_HOUR;
			timeLength = time + prefx;

		} else if (timeOffset > 60 * 60 && timeOffset < 60 * 60 * 24) {
			long time = timeOffset / 3600;
			String prefx = INNER_HOUR;
			timeLength = time + prefx;

		} else if (timeOffset > 60 * 60 * 24
				&& timeOffset < (curTime - getZeroTime())) {
			timeLength = YESTERDAY
					+ " "
					+ parserDataFromSecondInDisplayTypeHm(String
							.valueOf(originTime));

		} else {
			timeLength = parserDataFromSecondInDisplayTypeLocation(String
					.valueOf(originTime));

		}
		return timeLength;
	}

	/**
	 * 时间转换
	 * 
	 * @param curTime
	 * @param originTime
	 * @return
	 */
	public static String getTimeLength(long originTime) {
		originTime /= 1000;// 转换为以秒秒
		String timeLength = "";
		long curTime = getCurrentTime();
		long timeOffset = curTime - originTime;
		if (timeOffset < 60) {
			timeLength = JUST_NOW;

		} else if (timeOffset > 60 && timeOffset < 60 * 60) {
			long time = timeOffset / 60;
			String prefx = INNER_ONE_HOUR;
			timeLength = time + prefx;

		} else if (timeOffset > 60 * 60 && timeOffset < 60 * 60 * 24) {
			long time = timeOffset / 3600;
			String prefx = INNER_HOUR;
			timeLength = time + prefx;

		} else if (timeOffset > 60 * 60 * 24
				&& timeOffset < (curTime - getZeroTime())) {
			timeLength = YESTERDAY;

		} else {
			timeLength = parserDataFromSecondInDisplayType(String
					.valueOf(originTime));

		}
		return timeLength;
	}

	public static String parserDataFromSecondInDisplayTypeHm(String str) {
		SimpleDateFormat sdf = HM_DATE_FORMAT;
		StringBuffer sb = new StringBuffer();
		if (str != null && !str.equals("")) {
			long millsecond = Long.valueOf(str) * 1000;
			Date date = new Date(millsecond);
			sdf.format(date, sb, new FieldPosition(1));
		}
		return sb.toString();
	}

	public static String parserDataFromSecondInDisplayTypeLocation(String str) {
		SimpleDateFormat sdf = LOCATION_DATE_FORMAT;
		StringBuffer sb = new StringBuffer();
		if (str != null && !str.equals("")) {
			long millsecond = Long.valueOf(str) * 1000;
			Date date = new Date(millsecond);
			sdf.format(date, sb, new FieldPosition(1));
		}
		return sb.toString();
	}

	public static String parserDataFromSecondInDisplayType(String str) {
		SimpleDateFormat sdf = COMMENT_DATE_FORMAT;
		StringBuffer sb = new StringBuffer();
		if (str != null && !str.equals("")) {
			long millsecond = Long.valueOf(str) * 1000;
			Date date = new Date(millsecond);
			sdf.format(date, sb, new FieldPosition(1));
		}
		return sb.toString();
	}

	/**
	 * 获取某天的零时时刻
	 * 
	 * @return
	 */
	public static long getZeroTime() {
		Calendar ca = Calendar.getInstance(Locale.CHINA);
		int hour = ca.get(Calendar.HOUR_OF_DAY);
		int minute = ca.get(Calendar.MINUTE);
		int second = ca.get(Calendar.SECOND);
		long current = getCurrentTime();
		long zeroTime = current - (hour * 3600 + minute * 60 + second) - 60
				* 60 * 24;
		return zeroTime;
	}

	/**
	 * 获取当前的时间，以秒为单位
	 * 
	 * @return
	 */
	public static long getCurrentTime() {

		Time t = new Time();
		t.setToNow();
		long time = t.toMillis(false) / 1000;
		return time;
	}

	/**
	 * 获得一个UUID
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18)
				+ s.substring(19, 23) + s.substring(24);
	}

	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public static boolean isAppOnForeground() {
		ActivityManager activityManager = (ActivityManager) DApplication
				.getApplication().getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = DApplication.getApplication().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	// 删除Editable中指定位置的元素，可能为字符，也可能为ImageSpan
	public static Editable deleteElement(Editable editText, int position) {
		ImageSpan[] imageSpans = editText
				.getSpans(0, position, ImageSpan.class);
		if (imageSpans.length > 0) {
			ImageSpan lastImageSpan = null;
			int end = 0;
			for (int i = imageSpans.length - 1; i >= 0; i--) {
				lastImageSpan = imageSpans[i];
				end = editText.getSpanEnd(lastImageSpan);
				if (end == position) {
					break;
				}
			}
			if (end == position) {
				int start = editText.getSpanStart(lastImageSpan);
				editText.delete(start, end);
			} else {
				editText.delete(position - 1, position);
			}
		} else {
			editText.delete(position - 1, position);
		}
		return editText;
	}

	/**
	 * 
	 * @param activity
	 * @return > 0 success; <= 0 fail
	 */
	public static int getStatusHeight(Activity activity) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = activity.getResources()
						.getDimensionPixelSize(i5);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}
}
