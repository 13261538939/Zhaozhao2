package com.szl.zhaozhao2.prefs;

import android.content.Context;

/** 管理全局参数的类 */
public class PrefsUtil extends CachedPrefs {

	public static final String GLOBAL_UTIL = "global_util";

	public PrefsUtil(Context context) {
		super(context.getSharedPreferences(GLOBAL_UTIL, Context.MODE_PRIVATE));
	}

	/** 是否是第一次安装 */
	public BoolVal is_first = new BoolVal("is_first", true);
	/** 本地存储的versioncode */
	public IntVal version_code = new IntVal("version_code", 0);
	/** 下拉刷新 */
	public StringVal update_time = new StringVal("update_time", "");
	/** 用户账号 */
	public StringVal account = new StringVal("account", "");
	/** 密码 */
	public StringVal password = new StringVal("password", "");
	/** 用户名 */
	public StringVal nickname = new StringVal("nickname", "");
	/** 用户头像 */
	public StringVal avatar = new StringVal("avatar", "");
	/** RongCloud token */
	public StringVal token = new StringVal("token", "");
	/** push 开关 */
	public BoolVal push = new BoolVal("push", true);
	/** unlock 开关 */
	public BoolVal unlock = new BoolVal("unlock", false);
	/** 保存更新时间 */
	public StringVal check_version = new StringVal("check_version", "");
	/** 熟人添加引导 */
	public BoolVal show_guide_add = new BoolVal("guideade", true);
	/** 熟人引导 */
	public BoolVal show_ac_guide = new BoolVal("acguide", true);
}
