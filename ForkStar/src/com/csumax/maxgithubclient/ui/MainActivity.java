package com.csumax.maxgithubclient.ui;

import com.csumax.maxgithubclient.fragment.HomeFragment;
import com.csumax.maxgithubclient.fragment.MenuFragment;
import com.csumax.maxgithubclient.fragment.MenuFragment.SLMenuListOnItemClickListener;
import com.csumax.maxgithubclient.fragment.RepoFragment;
import com.csumax.maxgithubclient.fragment.UserFragment;
import com.csumax.maxgithubclient.slidingmenu.SlidingMenu;
import com.csumax.maxgithubclient.slidingmenu.app.SlidingFragmentActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends SlidingFragmentActivity implements
		SLMenuListOnItemClickListener {

	private SlidingMenu mSlidingMenu;
	private Fragment fragment = null;

	private SharedPreferences mSharedPreferences;
	private Editor editor;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("Home");
		// setTitle(R.string.sliding_title);
		setContentView(R.layout.main_frame_content);

		// set the Behind View
		setBehindContentView(R.layout.main_frame_menu);

		// customize the SlidingMenu
		mSlidingMenu = getSlidingMenu();
		// mSlidingMenu.setSecondaryMenu(R.layout.frame_menu); 设置右侧菜单的布屄1�7文件

		// mSlidingMenu.setShadowWidth(5);
		// mSlidingMenu.setBehindOffset(100);
		mSlidingMenu.setShadowDrawable(R.drawable.drawer_shadow);// 设置阴影图片
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width); // 设置阴影图片的宽庄1�7
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset); // SlidingMenu划出时主页面显示的剩余宽庄1�7
		mSlidingMenu.setFadeDegree(0.35f);
		// 设置SlidingMenu 的手势模弄1�7
		// TOUCHMODE_FULLSCREEN 全屏模式，在整个content页面中，滑动，可以打弄1�7SlidingMenu
		// TOUCHMODE_MARGIN
		// 边缘模式，在content页面中，如果想打弄1�7SlidingMenu,你需要在屏幕边缘滑动才可以打弄1�7SlidingMenu
		// TOUCHMODE_NONE 不能通过手势打开SlidingMenu
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		// 设置 SlidingMenu 内容
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		MenuFragment menuFragment = new MenuFragment();
		fragmentTransaction.replace(R.id.menu, menuFragment);
		fragmentTransaction.replace(R.id.content, new HomeFragment());
		fragmentTransaction.commit();

		getActionBar().setDisplayHomeAsUpEnabled(true);

		mSharedPreferences = getSharedPreferences(getString(R.string.sp_login),
				MODE_PRIVATE);
		editor = mSharedPreferences.edit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * ActionBar相关按钮的响应消息
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			toggle(); // 动�1�7�判断自动关闭或弄1�7启SlidingMenu
			// getSlidingMenu().showMenu();//显示SlidingMenu
			// getSlidingMenu().showContent();//显示内容
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void selectItem(int position, String title) {
		// update the main content by replacing fragments

		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new UserFragment();
			break;
		case 2:
			fragment = new RepoFragment();
			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content, fragment)
					.commit();
			// update selected item and title, then close the drawer
			setTitle(title);
			mSlidingMenu.showContent();
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
}
