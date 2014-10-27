package com.csumax.maxgithubclient.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class BitmapUtil {

	/**
	 * 获取缓存
	 * 
	 * @param url
	 * @return
	 */
	public static String getBitmapCache(String url) {

		File file = new File(new File(
				Environment.getExternalStorageDirectory(), "xxxx"),
				"cachebitmap");

		if (file.exists()) {
			return file.getAbsolutePath();
		} else {
			return null;
		}
	}

	/**
	 * 根据网络路径获取Bitmap
	 * 
	 * @param path
	 * @param file
	 * @return
	 */
	public static Bitmap getBitmapFromUrl(String urlStr, File file) {

		Bitmap bitmap = null;

		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream in = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(in);
			FileOutputStream out = new FileOutputStream(file.getPath());
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			in.close();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bitmap;
	}

	public static Bitmap getBitmap(String urlStr, Context context) {

		String imageName = urlStr.substring(urlStr.lastIndexOf("/") + 1,
				urlStr.length());

		Log.i("DEMO", "imageName :" + imageName);

		File file = new File(getPath(context), imageName);

		if (file.exists()) {
			Log.i("DEMO", "getBitmap from Local");
			return BitmapFactory.decodeFile(file.getPath());
		} else {
			Log.i("DEMO", "getBitmap from Net");
			return getBitmapFromUrl(urlStr, file);
		}

	}

	private static String getPath(Context context) {
		String path = null;
		boolean hasSDCard = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		String packageName = context.getPackageName() + "/cach/images/";
		if (hasSDCard) {
			path = "/sdcard/apps_images/" + packageName;
		} else {
			path = "/data/data/" + packageName;
		}
		File file = new File(path);
		boolean isExist = file.exists();
		if (!isExist) {

			file.mkdirs();

		}
		return file.getPath();
	}
}
