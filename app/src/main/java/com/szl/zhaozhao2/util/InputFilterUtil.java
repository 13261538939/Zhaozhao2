package com.szl.zhaozhao2.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

public class InputFilterUtil {

	public static InputFilter passWordFilter = new InputFilter() {

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			return deal(source, dstart);
		}
	};

	private static CharSequence deal(CharSequence source, int editStart) {
		String input = source.toString();
		CharSequence newSource = "";
		Pattern chinese = Pattern.compile("[\u0391-\uFFE5]+");
		if (!TextUtils.isEmpty(input)) {
			for (int i = 0; i < input.length(); i++) {
				CharSequence target = input.subSequence(i, i + 1);
				Matcher matcher = chinese.matcher(target);
				if (" ".equals(target) || "\n".equals(target)
						|| matcher.matches()) {
				} else {
					newSource = newSource.toString() + target;
				}
			}
		}
		return newSource;
	}

	public static InputFilter signatureFilter = new InputFilter() {

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			return filterSigna(source, start, end, dest, dstart, dend);
		}
	};

	private static CharSequence filterSigna(CharSequence src, int start,
			int end, Spanned dest, int dstart, int dend) {
		boolean bool = "\n".equals(src);
		if (!bool) {
			return dest.subSequence(dstart, dstart) + src.toString();
		}
		return dest.subSequence(dstart, dend);
	}

	public static InputFilter tagFilter = new InputFilter() {

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			String input = source.toString();
			CharSequence newSource = "";
			if (!TextUtils.isEmpty(input)) {
				for (int i = 0; i < input.length(); i++) {
					CharSequence target = input.subSequence(i, i + 1);
					if (",".equals(target) || "，".equals(target)
							|| "#".equals(target) || " ".equals(target)) {
					} else {
						newSource = newSource.toString() + target;
					}
				}
			}

			return newSource;
		}
	};
}
