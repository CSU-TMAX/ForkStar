package com.csumax.maxgithubclient.task;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.egit.github.core.Reference;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.Tree;
import org.eclipse.egit.github.core.TreeEntry;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.DataService;

import com.csumax.maxgithubclient.adapter.RepoContentAdapter;
import com.csumax.maxgithubclient.entity.RepoContent;
import com.csumax.maxgithubclient.fragment.RepoFragment;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class RepoContentTask extends AsyncTask<Void, Integer, Boolean> {

	private RepoFragment fragment;
	private Context context;
	private int flag;

	private RepoContentAdapter adapter;
	private List<RepoContent> list;

	private DataService dataService;

	private String owner;
	private String name;

	private Tree root;
	private TreeEntry treeEntry;

	public RepoContentTask(RepoFragment fragment) {
		this.fragment = fragment;
		this.context = fragment.getActivity();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		flag = fragment.getFlag();
		adapter = fragment.getRepoContentAdapter();
		list = fragment.getRepoContents();

		GitHubClient gitHubClient = fragment.getGitHubClient();
		dataService = new DataService(gitHubClient);

		owner = fragment.getOwner();
		name = fragment.getName();
		root = fragment.getRoot();
		treeEntry = fragment.getTreeEntry();

		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		String master = "heads/master";
		RepositoryId repositoryId = RepositoryId.create(owner, name);

		Reference reference;
		String sha;

		if (!fragment.isToggle()) {

			try {
				reference = dataService.getReference(repositoryId, master);
			} catch (IOException e) {
				return false;
			}

			sha = reference.getObject().getSha();

			if (isCancelled()) {
				return false;
			}

		} else {
			sha = "max_sha";
		}
		Log.i("DEMO", "sha = " + sha);

		try {
			root = dataService.getTree(repositoryId, sha, true);
		} catch (IOException i) {
			return false;
		}

		if (isCancelled()) {
			return false;
		}

		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {

		Log.i("DEMO", "RepoContentTask-onPostExecute-result:" + result);
		if (result) {

			List<TreeEntry> treeEntries = root.getTree();

			list.clear();

			if (flag == 1) {
				for (TreeEntry t : treeEntries) {
					String[] str = t.getPath().split("/");
					if (str.length == 1) {
						list.add(new RepoContent(t));
					}
				}
			} else {
				String[] t1 = treeEntry.getPath().split("/");

				for (TreeEntry t : treeEntries) {
					String[] str = t.getPath().split("/");
					if ((str.length - 1 == t1.length)) {
						list.add(new RepoContent(t));
					}
				}
			}

			Collections.sort(list);

			if (list.size() > 0) {
				adapter.notifyDataSetChanged();
			} else {
				Log.i("Demo", "RepoContentTask-onPostExecute-ListSize<=0");
			}

		} else {
			Log.i("Demo", "RepoContentTask-onPostExecute-result=false");
		}
	}

}
