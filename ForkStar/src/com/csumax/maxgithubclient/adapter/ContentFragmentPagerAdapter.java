package com.csumax.maxgithubclient.adapter;

import java.util.List;

import com.csumax.maxgithubclient.entity.ContentBean;
import com.csumax.maxgithubclient.fragment.TestContentFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ContentFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<ContentBean> list;

	public ContentFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public ContentFragmentPagerAdapter(FragmentManager fm,
			List<ContentBean> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int position) {
		return TestContentFragment.newInstance(list.get(position).getContent());
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return list.get(position).getTitle();
	}

}
