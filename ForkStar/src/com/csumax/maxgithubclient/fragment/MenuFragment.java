package com.csumax.maxgithubclient.fragment;

import java.util.ArrayList;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;

import com.csumax.maxgithubclient.adapter.NavDrawerListAdapter;
import com.csumax.maxgithubclient.entity.NavDrawerItem;
import com.csumax.maxgithubclient.task.UserTask;
import com.csumax.maxgithubclient.ui.R;
import com.csumax.maxgithubclient.view.CircleImageView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MenuFragment extends Fragment implements OnItemClickListener {

	private CircleImageView mUserCircleImageView;
	private TextView menuFullname;
	private TextView menuUsername;
	private ListView mDrawerList;

	private String[] mNavMenuTitles;
	private TypedArray mNavMenuIconsTypeArray;
	private ArrayList<NavDrawerItem> mNavDrawerItems;
	private NavDrawerListAdapter mAdapter;
	private SLMenuListOnItemClickListener mCallback;
	private int selected = -1;

	private UserTask userTask;

	private User user;

	private SharedPreferences mSharedPreferences;
	private String mOAuth;
	private GitHubClient gitHubClient;

	@Override
	public void onAttach(Activity activity) {
		try {
			mCallback = (SLMenuListOnItemClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnResolveTelsCompletedListener");
		}
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_menu, null);

		initView(rootView);

		return rootView;

	}

	private void initView(View rootView) {
		mUserCircleImageView = (CircleImageView) rootView
				.findViewById(R.id.menu_userimage);
		menuFullname = (TextView) rootView.findViewById(R.id.menu_fullname);
		menuUsername = (TextView) rootView.findViewById(R.id.menu_username);
		mDrawerList = (ListView) rootView.findViewById(R.id.left_menu);

		mNavMenuTitles = getResources()
				.getStringArray(R.array.nav_drawer_items);
		mNavMenuIconsTypeArray = getResources().obtainTypedArray(
				R.array.nav_drawer_icons);

		mNavDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[0],
				mNavMenuIconsTypeArray.getResourceId(0, -1)));
		// People
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[1],
				mNavMenuIconsTypeArray.getResourceId(1, -1)));
		// Photos
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[2],
				mNavMenuIconsTypeArray.getResourceId(2, -1), true, "22"));

		// Recycle the typed array
		mNavMenuIconsTypeArray.recycle();

		mAdapter = new NavDrawerListAdapter(getActivity(), mNavDrawerItems);
		mDrawerList.setAdapter(mAdapter);

		// //////////////////////////////////////////////
		mSharedPreferences = getActivity().getSharedPreferences(
				getString(R.string.sp_login), Context.MODE_PRIVATE);
		mOAuth = mSharedPreferences.getString(
				getString(R.string.sp_login_oauth), null);
		gitHubClient = new GitHubClient();
		gitHubClient.setOAuth2Token(mOAuth);

		userTask = new UserTask(MenuFragment.this);
		userTask.execute();

		// 见下方 onItemClick 方法
		mDrawerList.setOnItemClickListener(this);

		if (selected != -1) {
			mDrawerList.setItemChecked(selected, true);
			mDrawerList.setSelection(selected);
		} else {
			mDrawerList.setItemChecked(0, true);
			mDrawerList.setSelection(0);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);

		if (mCallback != null) {
			mCallback.selectItem(position, mNavMenuTitles[position]);
		}
		selected = position;

	}

	/**
	 * 左侧菜单 点击回调接口
	 * 
	 */
	public interface SLMenuListOnItemClickListener {

		public void selectItem(int position, String title);
	}

	public GitHubClient getGitHubClient() {
		return gitHubClient;
	}

	public CircleImageView getmUserCircleImageView() {
		return mUserCircleImageView;
	}

	public TextView getMenuFullname() {
		return menuFullname;
	}

	public TextView getMenuUsername() {
		return menuUsername;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
