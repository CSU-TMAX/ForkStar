package com.csumax.maxgithubclient.task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

import com.csumax.maxgithubclient.adapter.RepoItemAdapter;
import com.csumax.maxgithubclient.db.dao.RepoItemDao;
import com.csumax.maxgithubclient.entity.RepoItem;
import com.csumax.maxgithubclient.fragment.RepoFragment;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

public class RepoTask extends AsyncTask<Void, Integer, Boolean> {

	private RepoFragment fragment;
	private Context context;
	private int flag;

	private RepoItemAdapter adapter;
	private List<RepoItem> list;

	private GitHubClient gitHubClient;
	private RepositoryService repositoryService;

	public RepoTask(RepoFragment fragment) {
		this.fragment = fragment;
		this.context = fragment.getActivity();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub

		flag = fragment.getFlag();

		adapter = fragment.getRepoItemAdapter();
		list = fragment.getRepoItemList();

		gitHubClient = fragment.getGitHubClient();
		repositoryService = new RepositoryService(gitHubClient);

		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		Log.i("Demo", "RepoTask-doInBackground");

		RepoItemDao repoItemDao = new RepoItemDao(context);
		try {
			repoItemDao.openDatabase(true);
		} catch (SQLException s) {
			repoItemDao.closeDatabase();
			return false;
		}

		List<Repository> repositories = null;

		try {
			repositories = repositoryService.getRepositories();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (isCancelled()) {
			repoItemDao.closeDatabase();
			return false;
		}
		// //////////////////////////
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// for (Repository r : repositories) {
		// Log.i("Demo",
		// "RepoTask-doInBackground-" + r.getForks() + " "
		// + r.getLanguage());
		//
		// list.add(new RepoItem(r.getName(), format.format(r.getCreatedAt()),
		// r.getDescription(), r.getLanguage(), r.getWatchers(), r
		// .getForks(), r.getOwner().getLogin(), r.getGitUrl()));
		// }
		// ///////////////////////////////////////////

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		for (Repository r : repositories) {

			if (!repoItemDao.checkRepoItem(r.getGitUrl())) {

				RepoItem repoItem = new RepoItem();
				repoItem.setName(r.getName());
				repoItem.setDate(format.format(r.getCreatedAt()));
				repoItem.setDescription(r.getDescription());
				repoItem.setLang(r.getLanguage());
				repoItem.setStar(r.getWatchers());
				repoItem.setFork(r.getForks());
				repoItem.setOwner(r.getOwner().getLogin());
				repoItem.setGit(r.getGitUrl());

				repoItemDao.addRepoItem(repoItem);
			}

		}

		if (isCancelled()) {
			repoItemDao.closeDatabase();
			return false;
		}
		repoItemDao.closeDatabase();
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		Log.i("Demo", "RepoTask-onPostExecute");

		if (result) {

			RepoItemDao repoItemDao = new RepoItemDao(context);

			try {
				repoItemDao.openDatabase(true);
			} catch (SQLException s) {
				Log.i("Demo", "RepoTask-onPostExecute-SQLException");
				return;
			}

			List<RepoItem> repoItems = repoItemDao.getRepoItemList();
			Collections.sort(repoItems);

			List<Map<String, String>> autoList = new ArrayList<Map<String, String>>();

			list.clear();

			autoList.clear();

			for (RepoItem r : repoItems) {
				list.add(new RepoItem(r.getName(), r.getDate(), r
						.getDescription(), r.getLang(), r.getStar(), r
						.getFork(), r.getOwner(), r.getGit()));
				Map<String, String> map = new HashMap<String, String>();
				map.put("owner", r.getOwner());
				map.put("name", r.getName());
				autoList.add(map);
			}

			repoItemDao.closeDatabase();

			if (list.size() > 0) {
				adapter.notifyDataSetChanged();
			} else {
				Log.i("Demo", "RepoTask-onPostExecute-ListSize<=0");
			}

		}

		super.onPostExecute(result);
	}
}
