package com.szl.zhaozhao2.prefs;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/** 给SharedPrefernce添加缓存 */
public class CachedPrefs {

	private SharedPreferences mPref;
	private Editor mEditor = null;
	private boolean needCommit = true;// 如果不需要commit,则不调用commit操作

	@SuppressLint("CommitPrefEdits")
	public CachedPrefs(SharedPreferences pref) {
		mPref = pref;
		mEditor = mPref.edit();
	}

	public void commit() {
		if (needCommit) {
			mEditor.commit();
		}
	}

	public abstract class PrefVal<DatType> {
		private DatType mDat;// Value值
		protected String mKey;

		protected abstract DatType getValue(DatType defVal);

		protected abstract void setValue(DatType val);

		PrefVal(String key, DatType defVal) {
			mKey = key;
			mDat = getValue(defVal);
		}

		public DatType getVal() {
			return mDat;
		}

		public CachedPrefs setVal(DatType val) {
			if (!mDat.equals(val)) {
				mDat = val;
				setValue(val);
			}
			return CachedPrefs.this;
		}
	}

	public class BoolVal extends PrefVal<Boolean> {
		public BoolVal(String key, boolean defVal) {
			super(key, defVal);
		}

		protected Boolean getValue(Boolean defVal) {
			return mPref.getBoolean(mKey, defVal);
		}

		protected void setValue(Boolean val) {
			mEditor.putBoolean(mKey, val);
			commit();
		}
	}

	public class IntVal extends PrefVal<Integer> {
		public IntVal(String key, int defVal) {
			super(key, defVal);
		}

		protected Integer getValue(Integer defVal) {
			return mPref.getInt(mKey, defVal);
		}

		protected void setValue(Integer val) {
			mEditor.putInt(mKey, val);
			commit();
		}
	}

	public class StringVal extends PrefVal<String> {
		public StringVal(String key, String defVal) {
			super(key, defVal);
		}

		protected String getValue(String defVal) {
			return mPref.getString(mKey, defVal);
		}

		protected void setValue(String val) {
			mEditor.putString(mKey, val);
			commit();
		}
	}

	public class LongVal extends PrefVal<Long> {
		public LongVal(String key, Long defVal) {
			super(key, defVal);
		}

		protected Long getValue(Long defVal) {
			return mPref.getLong(mKey, defVal);
		}

		protected void setValue(Long val) {
			mEditor.putLong(mKey, val);
			commit();
		}
	}

}
