package com.csumax.maxgithubclient.adapter;

import java.util.List;

import com.csumax.maxgithubclient.entity.RepoItem;
import com.csumax.maxgithubclient.fragment.RepoFragment;
import com.csumax.maxgithubclient.ui.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

public class RepoItemAdapter extends ArrayAdapter<RepoItem> {

	private RepoFragment fragment;
	private Context context;
	private int layoutResId;
	private List<RepoItem> list;

	public RepoItemAdapter(Context context, RepoFragment fragment,
			int layoutResId, List<RepoItem> list) {
		super(context, layoutResId, list);
		this.fragment = fragment;
		this.context = context;
		this.layoutResId = layoutResId;
		this.list = list;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		Holder holder = null;

		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(layoutResId, parent, false);
			holder = new Holder();
			holder.icon = (ImageView) convertView
					.findViewById(R.id.repo_item_icon);
			holder.name = (TextView) convertView
					.findViewById(R.id.repo_item_name);
			holder.date = (TextView) convertView
					.findViewById(R.id.repo_item_date);
			holder.description = (TextView) convertView
					.findViewById(R.id.repo_item_description);
			holder.info = (TextView) convertView
					.findViewById(R.id.repo_item_info);
			holder.owner = (TextView) convertView
					.findViewById(R.id.repo_item_owner);
			holder.overflow = (ImageButton) convertView
					.findViewById(R.id.repo_item_overflow);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		RepoItem repoItem = list.get(position);

		holder.icon.setImageDrawable(context.getResources().getDrawable(
				R.drawable.ic_type_repo));
		holder.name.setText(repoItem.getName());
		holder.date.setText(repoItem.getDate());
		// ///////////////////////////////////////
		// /其他还有部分没写
		PopupMenu popupMenu = new PopupMenu(context, holder.overflow);
		popupMenu.getMenuInflater().inflate(R.menu.repo_item_overflow,
				popupMenu.getMenu());

		return convertView;
	}

	class Holder {

		ImageView icon;
		TextView name;
		TextView date;
		TextView description;
		TextView info;
		TextView owner;
		ImageButton overflow;

	}

}
