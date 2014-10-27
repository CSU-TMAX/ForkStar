package com.csumax.maxgithubclient.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Authorization;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.OAuthService;

import com.csumax.maxgithubclient.utils.StringUtil;
import com.csumax.maxgithubclient.utils.ToastUtil;
import com.csumax.maxgithubclient.view.CustomProgressDialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private SharedPreferences mSharedPreferences;
	private Editor editor;

	private String mOAuth;

	private EditText et_login_username;
	private EditText et_login_password;
	private Button bt_login_ok;

	private ProgressDialog progressDialog;
	private CustomProgressDialog mCustomProgressDialog;

	private String username;
	private String password;

	private Runnable oAuthThread;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		/*
		 * 检测用户登陆状态
		 * 
		 * 如果SharedPreferences中存在用户信息， 则说明用户已经登陆，此时直接跳转到MainActivity即可； 否则进入登陆界面
		 */
		mSharedPreferences = getSharedPreferences(getString(R.string.sp_login),
				MODE_PRIVATE);
		mOAuth = mSharedPreferences.getString(
				getString(R.string.sp_login_oauth), null);

		if (mOAuth != null) {

			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, MainActivity.class);
			intent.putExtra(getString(R.string.login_intent), false);
			startActivity(intent);
			finish();

		}

		getActionBar().setDisplayShowHomeEnabled(false);

		initView();

		login();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void initView() {

		et_login_username = (EditText) findViewById(R.id.et_login_username);
		et_login_password = (EditText) findViewById(R.id.et_login_password);
		bt_login_ok = (Button) findViewById(R.id.bt_login_ok);
		/* 保持EditText字体的一致性 */
		et_login_password.setTypeface(Typeface.DEFAULT);
		et_login_password
				.setTransformationMethod(new PasswordTransformationMethod());

	}

	public void login() {

		bt_login_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				username = et_login_username.getText().toString();
				password = et_login_password.getText().toString();

				if (StringUtil.isEmpty(username)
						|| StringUtil.isEmpty(password)) {
					et_login_username.setText("");
					et_login_password.setText("");
					ToastUtil.showShort(getApplicationContext(),
							R.string.login_username_password_null);
				} else {
					// progressDialog = new ProgressDialog(LoginActivity.this);
					// progressDialog.setMessage(getString(R.string.login_loginning));
					// progressDialog.setCancelable(false);
					// progressDialog.show();

					startCustomProgressDialog();

					/* 开启新的线程用于认证 */
					HandlerThread handlerThread = new HandlerThread(
							getString(R.string.login_thread));
					handlerThread.start();
					Handler handler = new Handler(handlerThread.getLooper());
					handler.post(oAuthThread);
				}

			}
		});

		oAuthThread = new Runnable() {

			@Override
			public void run() {

				GitHubClient gitHubClient = new GitHubClient();
				gitHubClient.setCredentials(username, password);
				gitHubClient.setUserAgent(getString(R.string.app_name));

				// 获取认证信息
				Authorization authorization = null;
				OAuthService oAuthService = null;

				try {
					oAuthService = new OAuthService(gitHubClient);

					// 判断当前应用是否已经认证
					List<Authorization> authorizationList = oAuthService
							.getAuthorizations();

					for (Authorization a : authorizationList) {
						if (getString(R.string.app_name).equals(a.getNote())) {
							authorization = a;
							break;
						}
					}

					// 遍历之后还是没有，则新建认证
					if (authorization == null) {
						Log.i("Demo", "authorization == null");
						authorization = new Authorization();
						authorization.setNote(getString(R.string.app_name));
						authorization.setUrl(getString(R.string.app_url));

						List<String> scopes = new ArrayList<String>();
						scopes.add(getString(R.string.login_permission_notifications));
						scopes.add(getString(R.string.login_permission_repo));
						scopes.add(getString(R.string.login_permission_user));
						authorization.setScopes(scopes);

						authorization = oAuthService
								.createAuthorization(authorization);
					}

					/* 将获取到的认证信息写入SharedPreferences */
					editor = mSharedPreferences.edit();
					editor.putString(getString(R.string.sp_login_username),
							username);
					editor.putString(getString(R.string.sp_login_oauth),
							authorization.getToken());
					editor.putInt(getString(R.string.sp_login_highlight_num), 0);
					editor.putString(
							getString(R.string.sp_login_highlight_css),
							getString(R.string.webview_default_css));
					editor.commit();

					// progressDialog.dismiss();
					stopCustomProgressDialog();

					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, MainActivity.class);
					intent.putExtra(getString(R.string.login_intent), false);
					startActivity(intent);
					finish();

				} catch (Exception e) {
					// TODO: handle exception
					// progressDialog.dismiss();
					stopCustomProgressDialog();
					et_login_username.setText("");
					et_login_password.setText("");
					Log.i("Demo", "Exception");
					Log.i("Demo", e.getMessage());

				}

			}

		};

	}

	/**
	 * 显示自定义的ProgressDialog
	 */
	private void startCustomProgressDialog() {
		if (mCustomProgressDialog == null) {
			mCustomProgressDialog = CustomProgressDialog.createDialog(this);
			mCustomProgressDialog
					.setMessage(getString(R.string.login_loginning));
		}
		mCustomProgressDialog.show();
	}

	/**
	 * 关闭自定义的ProgressDialog
	 */
	private void stopCustomProgressDialog() {
		if (mCustomProgressDialog != null) {
			mCustomProgressDialog.dismiss();
			mCustomProgressDialog = null;
		}
	}

}
