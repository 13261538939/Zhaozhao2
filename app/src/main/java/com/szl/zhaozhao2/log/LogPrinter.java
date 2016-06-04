package com.szl.zhaozhao2.log;

import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.channels.WritableByteChannel;

import android.content.Context;
import android.database.Cursor;

public class LogPrinter implements UncaughtExceptionHandler {
	public static boolean DEBUG = true;
	public static boolean DEBUG_LOGFILE = DEBUG && true;
	public static boolean DEBUG_MODULE_DIAL = DEBUG && true;
	public static boolean DEBUG_MODULE_CONTACT = DEBUG && true;
	public static boolean DEBUG_MODULE_SMS = DEBUG && true;
	public static boolean DEBUG_MODULE_SETTING = DEBUG && true;
	public static boolean DEBUG_MODULE_NET = DEBUG && true;
	private static boolean LOG = DEBUG || DEBUG_MODULE_DIAL
			|| DEBUG_MODULE_CONTACT || DEBUG_MODULE_SMS || DEBUG_MODULE_SETTING
			|| DEBUG_MODULE_NET;
	private static String TAG = "LogPrinter";

	// #expand final static String LOG_DIR = "%LOG_DIR%" + LOG_TAG;
	// private static String LOG_DIR = "/logs/";
	// #expand final static String LOG_NAME = "%LOG_NAME%";
	// private static String LOG_NAME = "FATAL";

	// public final static String getLogPath() {
	// return FileUtil.getAppFilePath() + LOG_DIR;
	// }

	public final static void v(final String tag, final String msg) {
		if (LOG)
			android.util.Log.v(tag, msg);
	}

	public final static void v(final String tag, final String msg,
			Throwable throwable) {
		if (LOG)
			android.util.Log.v(tag, msg, throwable);
	}

	public final static void d(final String tag, final String msg) {
		if (LOG)
			android.util.Log.d(tag, msg);
	}

	public final static void d(final String tag, final String msg,
			Throwable throwable) {
		if (LOG)
			android.util.Log.d(tag, msg, throwable);
	}

	public final static void i(final String tag, final String msg) {
		if (LOG)
			android.util.Log.i(tag, msg);
	}

	public final static void i(final String tag, final String msg,
			Throwable throwable) {
		if (LOG)
			android.util.Log.i(tag, msg, throwable);
	}

	public final static void w(final String tag, final String msg) {
		if (LOG)
			android.util.Log.w(tag, msg);
	}

	public final static void w(final String tag, Throwable throwable) {
		if (LOG)
			android.util.Log.w(tag, throwable);
	}

	public final static void w(final String tag, final String msg,
			Throwable throwable) {
		if (LOG)
			android.util.Log.w(tag, msg, throwable);
	}

	public final static void e(final String tag, final String msg) {
		if (LOG)
			android.util.Log.e(tag, msg);
	}

	public final static void e(final String tag, final String msg,
			Throwable throwable) {
		if (LOG)
			android.util.Log.e(tag, msg, throwable);
	}

	/**
	 * write the log to file
	 */
	// public synchronized static void log(final String pkgName, final String
	// text) {
	// if (!DEBUG_LOGFILE)
	// return;
	//
	// String name = pkgName;
	// if (name == null)
	// name = "unknowPacageName";
	// String sdStatus = android.os.Environment.getExternalStorageState();
	// if (sdStatus.equals(android.os.Environment.MEDIA_MOUNTED)) {
	// String dir = getLogPath();
	// File f = new File(dir);
	// if (!f.exists())
	// f.mkdirs();
	// try {
	// File logFile = new File(dir + name + ".log");
	// if (!logFile.exists())
	// logFile.createNewFile();
	//
	// FileOutputStream outStream = new FileOutputStream(logFile, true);
	//
	// SimpleDateFormat format = new SimpleDateFormat(
	// "yyyy-MM-dd HH:mm:ss");
	// String content = format.format(new Date());
	// content += "|" + text;
	// outStream.write((content.toString() + "\r\n").getBytes());
	// outStream.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// }

	/**
	 * @param tag
	 * @param priority
	 *            debug level
	 * @return byte to write
	 */
	public final static int println(final String tag, final int priority,
			final String msg, Throwable throwable) {
		StackTraceElement[] elements = throwable.getStackTrace();
		elements[0].getLineNumber();
		return 0;
	}

	public final static int println(final String tag, final int priority,
			final String msg, Throwable throwable, int rawLineNum) {

		return 0;
	}

	public final static void setWriter(WritableByteChannel writableByteChannel) {

	}

	// private static LogPrinter _instance;
	// private Context context;

	// public final static void getLog() {
	// java.util.Calendar calendar = java.util.Calendar.getInstance();
	// String fileName = LOG_NAME
	// + calendar.getTime().toLocaleString().replace(' ', '_')
	// .replace('-', '_').replace(':', '_') + ".log";
	// String sdStatus = android.os.Environment.getExternalStorageState();
	// if (sdStatus.equals(android.os.Environment.MEDIA_MOUNTED)) {
	// try {
	// String dir = getLogPath();
	// java.io.File logDir = new File(dir);
	// if (!logDir.exists())
	// logDir.mkdirs();
	// java.io.File logFile = new File(dir + fileName);
	// if (!logFile.exists())
	// logFile.createNewFile();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// } else {
	// try {
	// java.io.File logFile = _instance.context
	// .getFileStreamPath(fileName);
	// if (!logFile.exists())
	// logFile.createNewFile();
	// } catch (IOException e) {
	// e.printStackTrace();
	// return;
	// }
	// }
	// ArrayList<String> commandLine = new ArrayList<String>();
	// commandLine.add("logcat");
	// commandLine.add("-d");
	// commandLine.add("-f");
	// commandLine.add(fileName);
	// commandLine.add("-v");
	// commandLine.add("time");
	// commandLine.add("-s");
	// commandLine.add("*:*");
	// try {
	// Runtime.getRuntime().exec(
	// commandLine.toArray(new String[commandLine.size()]));
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// android.os.Process.killProcess(android.os.Process.myPid());
	// }

	public static void logMEM() {
		// #mdebug
		LogPrinter
				.v(TAG,
						("********************************************" + "\n"
								+ "nativeHeap = "
								+ android.os.Debug.getNativeHeapAllocatedSize()
								+ " bytes\n" + "totalMemory = "
								+ Runtime.getRuntime().totalMemory()
								+ " bytes\n" + "freeMemory = "
								+ Runtime.getRuntime().freeMemory()
								+ " bytes\n" + "maxMemory = "
								+ Runtime.getRuntime().maxMemory() + " bytes\n" + "********************************************"));
		// #enddebug
	}

	public static void logCursor(final Cursor c) {
		// #mdebug
		for (int j = 0; j < c.getColumnCount(); j++) {
			LogPrinter.i(TAG,
					"name:" + c.getColumnName(j) + "=" + c.getString(j));
		}
		// #enddebug
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		LogPrinter.e(TAG, "FATAL/Uncaught", ex);
		// if (DEBUG_LOGFILE)
		// getLog();
	}

	public LogPrinter(Context context) {
		Thread.setDefaultUncaughtExceptionHandler(this);
		// this.context = context;
		// _instance = this;
	}

}
