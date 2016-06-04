package com.szl.zhaozhao2.manager.imageloader;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.szl.zhaozhao2.R;

public class DisplayImageOptionsManager {

	public static DisplayImageOptions getCommenOptions() {

		return getOptions(R.drawable.zanwu, R.drawable.wutu, R.drawable.tanhao,
				true, true, true);
	}

	public static DisplayImageOptions getNoCacheOptions() {

		return getOptions(R.drawable.zanwu, R.drawable.wutu, R.drawable.tanhao,
				false, false, true);
	}

	public static DisplayImageOptions getNoDefaultOptions() {

		return getOptions(0, R.drawable.wutu, R.drawable.tanhao, false, true,
				true);
	}

	public static DisplayImageOptions getSectedImageOptions() {

		return getSectedImageOptions(R.drawable.zanwu, R.drawable.wutu,
				R.drawable.tanhao, true, false, true);
	}

	public static DisplayImageOptions getUserBackgroundOptions() {

		return getOptions(0, R.drawable.bg_personage_header,
				R.drawable.bg_personage_header, true, true, true);
	}

	private static DisplayImageOptions getOptions(int imageOnLoading,
			int imageEmptyUri, int imageOnFail, boolean cacheInMemory,
			boolean cacheOnDisk, boolean considerExifParams) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(imageOnLoading)
				.showImageForEmptyUri(imageEmptyUri)
				.showImageOnFail(imageOnFail).cacheInMemory(cacheInMemory)
				.cacheOnDisk(cacheOnDisk)
				.considerExifParams(considerExifParams)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}

	private static DisplayImageOptions getSectedImageOptions(
			int imageOnLoading, int imageEmptyUri, int imageOnFail,
			boolean cacheInMemory, boolean cacheOnDisk,
			boolean considerExifParams) {

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(imageOnLoading)
				.showImageForEmptyUri(imageEmptyUri)
				.showImageOnFail(imageOnFail).cacheInMemory(cacheInMemory)
				.cacheOnDisk(cacheOnDisk)
				.considerExifParams(considerExifParams)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}
	
	
	public  static DisplayImageOptions getRoundImageOptions(){
		return getRoundOptions(R.drawable.zanwu, R.drawable.wutu, R.drawable.tanhao,
				true, true, true,8);
	}
	public  static DisplayImageOptions getRoundImageOptions(int radius){
		return getRoundOptions(R.drawable.zanwu, R.drawable.wutu, R.drawable.tanhao,
				true, true, true,radius);
	}
	
	private static DisplayImageOptions getRoundOptions(int imageOnLoading,
			int imageEmptyUri, int imageOnFail, boolean cacheInMemory,
			boolean cacheOnDisk, boolean considerExifParams,int radius){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(imageOnLoading)
		.showImageForEmptyUri(imageEmptyUri)
		.showImageOnFail(imageOnFail).cacheInMemory(cacheInMemory)
		.cacheOnDisk(cacheOnDisk)
		.considerExifParams(considerExifParams)
		.bitmapConfig(Bitmap.Config.ARGB_8888)
		.displayer(new RoundedBitmapDisplayer(radius))
		.build();
        return options;
	}
}
