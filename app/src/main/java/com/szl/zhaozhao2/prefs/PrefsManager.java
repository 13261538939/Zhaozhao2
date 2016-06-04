package com.szl.zhaozhao2.prefs;

import android.content.Context;

public class PrefsManager {

	private static PrefsUtil prefsUtil;

	public static PrefsUtil getPrefsUtil(Context context) {
		if (prefsUtil == null) {
			synchronized (PrefsManager.class) {
				if (prefsUtil == null) {
					prefsUtil = new PrefsUtil(context);
				}
			}
		}
		return prefsUtil;
	}
}
