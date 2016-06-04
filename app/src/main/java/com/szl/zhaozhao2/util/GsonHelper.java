package com.szl.zhaozhao2.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.szl.zhaozhao2.log.LogPrinter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class GsonHelper {

	private static final String TAG = "GsonHelper";

	private static Gson INSTANCE;

	static {
		INSTANCE = new Gson();
	}

	public static <T> T parse(String strDataJson, Class<T> classOfT) {
		T data = null;
		if (null != strDataJson) {
			try {
				data = INSTANCE.fromJson(strDataJson, classOfT);
			} catch (JsonSyntaxException e) {
				LogPrinter.e(TAG, e.getMessage());
			} catch (Exception e) {
				LogPrinter.e(TAG, e.getMessage());
			}
		}
		return data;
	}

	// public static <T> List<T> parseList(String strDataJson, Class<T>
	// classOfT) {
	// List<T> list = new ArrayList<T>();
	// JsonParser parser = new JsonParser();
	// JsonArray Jarray = parser.parse(strDataJson.toString())
	// .getAsJsonArray();
	// try {
	// list = INSTANCE.fromJson(Jarray.toString(),
	// new TypeToken<List<T>>() {
	// }.getType());
	//
	// } catch (Exception e) {
	// // do nothing
	// }
	// return list;
	// }

	public static <T> List<T> parseList(String jsonArray, Class<T> classOfT) {
		JsonParser parser = new JsonParser();
		JsonArray Jarray = parser.parse(jsonArray).getAsJsonArray();
		ArrayList<T> data = new ArrayList<T>();
		for (JsonElement obj : Jarray) {
			T cse = INSTANCE.fromJson(obj, classOfT);
			data.add(cse);
		}
		return data;
	}

	public static <T> T parse(byte[] dataJson, Class<T> classOfT) {
		T data = null;
		String strDataJson = null;
		try {
			strDataJson = new String(dataJson, "utf-8");
			data = parse(strDataJson, classOfT);
		} catch (UnsupportedEncodingException e) {
			LogPrinter.e(TAG, e.getMessage());
		}
		return data;
	}
}
