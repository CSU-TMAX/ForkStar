package com.csumax.maxgithubclient.fragment;

import java.util.ArrayList;
import java.util.List;

import com.csumax.maxgithubclient.adapter.ContentFragmentPagerAdapter;
import com.csumax.maxgithubclient.entity.ContentBean;
import com.csumax.maxgithubclient.ui.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {

	private ViewPager mViewPager;
	private static final String[] titles = { "One", "Two", "Three", "Four",
			"Five" };
	private List<ContentBean> list = new ArrayList<ContentBean>();
	private ContentFragmentPagerAdapter mAdapter;

	public HomeFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);

		initData();

		initView(rootView);

		return rootView;
	}

	@Override
	public void onStart() {

		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}

		super.onStart();
	}

	private void initData() {
		for (int i = 0; i < titles.length; i++) {

			ContentBean contentBean = new ContentBean();
			contentBean.setTitle(titles[i]);
			contentBean.setContent(titles[i] + "_" + (i + 1));

			list.add(contentBean);
		}
	}

	private void initView(View rootView) {

		mViewPager = (ViewPager) rootView.findViewById(R.id.mViewPager);
		mAdapter = new ContentFragmentPagerAdapter(
				this.getChildFragmentManager(), list);
		mViewPager.setAdapter(mAdapter);

	}

}
