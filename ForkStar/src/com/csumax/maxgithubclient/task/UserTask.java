package com.csumax.maxgithubclient.task;

import java.io.IOException;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;

import com.csumax.maxgithubclient.fragment.MenuFragment;
import com.csumax.maxgithubclient.utils.BitmapUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

public class UserTask extends AsyncTask<Void, Integer, User> {

	private MenuFragment fragment;
	private Context context;

	private GitHubClient gitHubClient;
	private UserService userService;

	private String userImageUrl;
	private Bitmap userHeaderBitmap;

	public UserTask(MenuFragment fragment) {
		this.fragment = fragment;
		this.context = fragment.getActivity();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub

		gitHubClient = fragment.getGitHubClient();
		userService = new UserService(gitHubClient);

		super.onPreExecute();
	}

	@Override
	protected User doInBackground(Void... params) {
		// TODO Auto-generated method stub
		User user = null;
		try {
			user = userService.getUser();
			userImageUrl = user.getAvatarUrl();
			Log.i("DEMO", "userImageUrl:" + userImageUrl);
			userHeaderBitmap = BitmapUtil.getBitmap(userImageUrl,
					fragment.getActivity());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	@Override
	protected void onPostExecute(User user) {
		// TODO Auto-generated method stub
		if (user != null) {

			fragment.setUser(user);
			fragment.getmUserCircleImageView().setImageBitmap(userHeaderBitmap);
			fragment.getMenuFullname().setText(user.getName());
			fragment.getMenuUsername().setText(user.getLogin());
		}
		super.onPostExecute(user);
	}

}
