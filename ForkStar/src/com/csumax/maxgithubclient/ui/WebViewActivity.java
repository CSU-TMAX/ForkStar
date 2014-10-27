package com.csumax.maxgithubclient.ui;

import com.csumax.maxgithubclient.fragment.WebViewFragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager.LayoutParams;

public class WebViewActivity extends FragmentActivity {

	private SharedPreferences mSharedPreferences;
	private Editor editor;

	private WebViewFragment fragment;
	private LayoutParams layoutParams;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		Intent intent = getIntent();
		String title = intent
				.getStringExtra(getString(R.string.webview_intent_title));
		String subTitle = intent
				.getStringExtra(getString(R.string.webview_intent_subtitle));

		ActionBar actionBar = getActionBar();
		actionBar.setTitle(title);
		actionBar.setSubtitle(subTitle);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);

		fragment = (WebViewFragment) getSupportFragmentManager()
				.findFragmentById(R.id.webview_fragment);

		mSharedPreferences = getSharedPreferences(getString(R.string.sp_login),
				Context.MODE_PRIVATE);
		editor = mSharedPreferences.edit();

		layoutParams = getWindow().getAttributes();
	}

}
