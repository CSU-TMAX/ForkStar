package com.csumax.maxgithubclient.fragment;

import org.eclipse.egit.github.core.client.GitHubClient;

import com.csumax.maxgithubclient.ui.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewFragment extends ProgressFragment {

	private String owner;
	private String name;
	private String filename;
	private String sha;

	private SharedPreferences mSharedPreferences;
	private String mOAuth;
	private GitHubClient gitHubClient;

	private WebView mWebView;

	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		setContentView(R.layout.webview_fragment);
		setContentEmpty(false);
		setContentShown(true);

		View view = getContentView();
		mWebView = (WebView) view.findViewById(R.id.webview);

		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setLoadsImagesAutomatically(true);
		webSettings
				.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS.NORMAL);
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setDisplayZoomControls(false);

		mSharedPreferences = getActivity().getSharedPreferences(
				getString(R.string.sp_login), Context.MODE_PRIVATE);
		mOAuth = mSharedPreferences.getString(
				getString(R.string.sp_login_oauth), null);
		gitHubClient = new GitHubClient();
		gitHubClient.setOAuth2Token(mOAuth);

		Intent intent = getActivity().getIntent();
		owner = intent.getStringExtra(getString(R.string.webview_intent_owner));
		name = intent.getStringExtra(getString(R.string.webview_intent_name));
		sha = intent.getStringExtra(getString(R.string.webview_intent_sha));
		filename = intent
				.getStringExtra(getString(R.string.webview_intent_title));

		Log.i("DEMO", "WebViewFragment-onActivityCreated");
		/**
		 * 此处有Task没加
		 */
	}

	public String getOwner() {
		return owner;
	}

	public String getName() {
		return name;
	}

	public String getFilename() {
		return filename;
	}

	public String getSha() {
		return sha;
	}

	public WebView getmWebView() {
		return mWebView;
	}

}
