package com.csumax.maxgithubclient.view;

import com.csumax.maxgithubclient.ui.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomProgressDialog extends Dialog {

	private Context context = null;
	private static CustomProgressDialog customProgressDialog = null;

	private CustomProgressDialog(Context context) {
		super(context);
		this.context = context;
	}

	private CustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static CustomProgressDialog createDialog(Context context) {
		customProgressDialog = new CustomProgressDialog(context,
				R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.customprogressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return customProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		if (customProgressDialog == null) {
			return;
		}

		ImageView imageView_loading = (ImageView) customProgressDialog
				.findViewById(R.id.iv_customprogressdialog_loading);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView_loading
				.getBackground();
		animationDrawable.start();
	}

	public CustomProgressDialog setMessage(String message) {
		TextView textView_message = null;
		if (message != null) {
			textView_message = (TextView) customProgressDialog
					.findViewById(R.id.tv_customprogressdialog_message);
			textView_message.setText(message);
		}
		return customProgressDialog;
	}

}
