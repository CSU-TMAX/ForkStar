package com.csumax.maxgithubclient.adapter;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.egit.github.core.TreeEntry;

import com.csumax.maxgithubclient.entity.RepoContent;
import com.csumax.maxgithubclient.ui.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RepoContentAdapter extends ArrayAdapter<RepoContent> {

	private Context context;
	private int layoutResId;
	private List<RepoContent> list;

	public RepoContentAdapter(Context context, int layoutResId,
			List<RepoContent> list) {
		super(context, layoutResId, list);
		this.context = context;
		this.layoutResId = layoutResId;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		Holder holder;

		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(layoutResId, parent, false);

			holder = new Holder();
			holder.icon = (ImageView) convertView
					.findViewById(R.id.content_item_icon);
			holder.title = (TextView) convertView
					.findViewById(R.id.content_item_title);
			holder.info = (TextView) convertView
					.findViewById(R.id.content_item_info);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		RepoContent repoContent = list.get(position);
		TreeEntry treeEntry = repoContent.getTreeEntry();

		if (treeEntry.getType().equals("tree")) {
			holder.icon.setImageDrawable(context.getResources().getDrawable(
					R.drawable.ic_type_folder));
			holder.info.setText("#");
		} else {
			holder.icon.setImageDrawable(context.getResources().getDrawable(
					R.drawable.ic_type_file));
			if (treeEntry.getSize() < 1024) {
				holder.info.setText(treeEntry.getSize() + " " + "B");
			} else {
				double d = round(treeEntry.getSize() / 1024);
				holder.info.setText(d + " " + "KB");
			}
		}

		holder.title.setText(getName(treeEntry.getPath()));

		return convertView;
	}

	class Holder {
		ImageView icon;
		TextView title;
		TextView info;
	}

	private static double round(double value) {
		BigDecimal bigDecimal = new BigDecimal(value);
		bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		double d = bigDecimal.doubleValue();
		return d;
	}

	private static String getName(String path) {
		String[] str = path.split("/");
		return str[str.length - 1];
	}

}
