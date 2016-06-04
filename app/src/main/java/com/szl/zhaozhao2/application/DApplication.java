package com.szl.zhaozhao2.application;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.szl.zhaozhao2.model.User;
import com.szl.zhaozhao2.prefs.PrefsManager;

public class DApplication extends Application{
	
	private static final String TAG = "DApplication";
	private static DApplication application;
	// 用户信息
	public static String account;
	public static String password;
	public static boolean isLogined;
	public static User user;
	public static String smId;
	// 手机唯一标识
	public static  String imei;
	// 屏幕宽高
	public static int screenWidth;
	public static int screenHeight;
	
	
   //private DApplication(){};
	
	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	
	/**
	 *  做一些必要的初始化
	 */
	public void init(){
		
		application = this;
		account = PrefsManager.getPrefsUtil(this).account.getVal();
		password = PrefsManager.getPrefsUtil(this).password.getVal();
		isLogined = !account.equals("") && !password.equals("") ? true : false;
		imei = ((TelephonyManager)getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		user = new User();
		smId = "";
		
		initScreenWidthAndHeight();
		
		// ToDo 第三方的一些初始化工作
		initImageLoader(getApplicationContext());
		
	}
	
	 public void initScreenWidthAndHeight(){
		 screenWidth = this.getResources().getDisplayMetrics().widthPixels;
		 screenHeight = this.getResources().getDisplayMetrics().heightPixels;
	 }
	 
	 public void initImageLoader(Context context){
		 ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					context).threadPriority(Thread.NORM_PRIORITY - 2)
					.denyCacheImageMultipleSizesInMemory()
					.diskCacheFileNameGenerator(new Md5FileNameGenerator())
					// 自定义磁盘缓存路径
					.diskCacheSize(50 * 1024 * 1024)
					// 50 Mb
					.tasksProcessingOrder(QueueProcessingType.LIFO)
					// .writeDebugLogs() // Remove for release app
					.build();
			// Initialize ImageLoader with configuration.
			ImageLoader.getInstance().init(config);
	 }
	
	 public static DApplication getApplication(){
		 if(application == null){
			 synchronized (DApplication.class) {
			  if(application == null){
				  application = new DApplication();
			  }
			}
		 }
		 return application;
	 }
	
}
