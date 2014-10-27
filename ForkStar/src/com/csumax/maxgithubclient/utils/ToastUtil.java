package com.csumax.maxgithubclient.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastUtil {

	private static Toast mToast = null;

	private static final int LENGTH_LONG = Toast.LENGTH_LONG;
	private static final int LENGTH_SHORT = Toast.LENGTH_SHORT;

	public static void showShort(Context context, String message) {
		mToast = Toast.makeText(context, message, LENGTH_SHORT);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
	}

	public static void showShort(Context context, int resId) {
		mToast = Toast.makeText(context, resId, LENGTH_SHORT);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
	}

	public static void showLong(Context context, String message) {
		mToast = Toast.makeText(context, message, LENGTH_LONG);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
	}

	public static void showLong(Context context, int resId) {
		mToast = Toast.makeText(context, resId, LENGTH_LONG);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
	}

	public static void showIconToast(Context context, String message, int iconId) {
		mToast = Toast.makeText(context, message, LENGTH_SHORT);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		View toastView = mToast.getView();
		ImageView imageView = new ImageView(context);
		imageView.setImageResource(iconId);
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.addView(imageView);
		linearLayout.addView(toastView);
		mToast.setView(linearLayout);
		mToast.show();
	}

}
