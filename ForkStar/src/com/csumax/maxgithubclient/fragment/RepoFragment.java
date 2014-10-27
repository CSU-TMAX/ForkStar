package com.csumax.maxgithubclient.fragment;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Tree;
import org.eclipse.egit.github.core.TreeEntry;
import org.eclipse.egit.github.core.client.GitHubClient;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;

import com.csumax.maxgithubclient.adapter.RepoContentAdapter;
import com.csumax.maxgithubclient.adapter.RepoItemAdapter;
import com.csumax.maxgithubclient.entity.RepoContent;
import com.csumax.maxgithubclient.entity.RepoItem;
import com.csumax.maxgithubclient.task.RepoContentTask;
import com.csumax.maxgithubclient.task.RepoTask;
import com.csumax.maxgithubclient.ui.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RepoFragment extends Fragment {

	public static final int TYPE_REPO = 0;
	public static final int TYPE_REPOCONTENT = 1;

	private int flag = 1;

	private int currentType = TYPE_REPO;

	private ActionBar actionBar;
	private ListView listView;
	private PullToRefreshLayout pullToRefreshLayout;

	private String title;
	private String subTitle;

	private List<RepoItem> repoItemList = new ArrayList<RepoItem>();
	private RepoItemAdapter repoItemAdapter;
	private RepoTask repoTask;

	private List<RepoContent> repoContents = new ArrayList<RepoContent>();
	private RepoContentAdapter repoContentAdapter;
	private RepoContentTask repoContentTask;

	private TreeEntry treeEntry;
	private Tree root;
	private String prefix;
	private boolean toggle = false;

	private SharedPreferences mSharedPreferences;
	private String mOAuth;
	private GitHubClient gitHubClient;

	private String owner;
	private String name;

	public RepoFragment() {

	}

	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		setHasOptionsMenu(true);

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(R.layout.fragment_repo, container,
				false);

		initView(rootView);

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
	}

	@SuppressLint("NewApi")
	private void initView(View rootView) {

		listView = (ListView) rootView
				.findViewById(R.id.repo_fragment_listview);
		actionBar = getActivity().getActionBar();

		ViewGroup group = (ViewGroup) rootView;
		pullToRefreshLayout = new PullToRefreshLayout(group.getContext());

		ActionBarPullToRefresh.from(getActivity()).insertLayoutInto(group)
				.setup(pullToRefreshLayout);

		repoItemAdapter = new RepoItemAdapter(rootView.getContext(),
				RepoFragment.this, R.layout.repo_item, repoItemList);
		repoItemAdapter.notifyDataSetChanged();
		listView.setAdapter(repoItemAdapter);

		repoContentAdapter = new RepoContentAdapter(rootView.getContext(),
				R.layout.content_item, repoContents);
		repoContentAdapter.notifyDataSetChanged();

		mSharedPreferences = getActivity().getSharedPreferences(
				getString(R.string.sp_login), Context.MODE_PRIVATE);
		mOAuth = mSharedPreferences.getString(
				getString(R.string.sp_login_oauth), null);
		gitHubClient = new GitHubClient();
		gitHubClient.setOAuth2Token(mOAuth);

		repoTask = new RepoTask(RepoFragment.this);
		repoTask.execute();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (currentType) {
				case TYPE_REPO:
					Log.i("DEMO", "type:" + currentType);
					repoItemClick(position);
					break;
				case TYPE_REPOCONTENT:
					Log.i("DEMO", "type:" + currentType);
					repoContentClick(position);
					break;
				default:
					break;
				}
			}
		});

	}

	@SuppressLint("NewApi")
	private void repoItemClick(int position) {
		allTaskDown();

		RepoItem repoItem = repoItemList.get(position);

		owner = repoItem.getOwner();
		name = repoItem.getName();

		title = name;
		subTitle = name;
		actionBar.setTitle(title);
		actionBar.setSubtitle(subTitle);
		actionBar.setDisplayHomeAsUpEnabled(true);

		Log.i("DEMO", "RepoFragment-repoItemClick-owner:" + owner);
		Log.i("DEMO", "RepoFragment-repoItemClick-name:" + name);
		Log.i("DEMO", "RepoFragment-repoItemClick-title:" + title);
		Log.i("DEMO", "RepoFragment-repoItemClick-subTitle:" + subTitle);

		root = null;
		treeEntry = null;
		toggle = false;
		prefix = "/";

		listView.setAdapter(repoContentAdapter);
		repoContentAdapter.notifyDataSetChanged();

		currentType = TYPE_REPOCONTENT;

		repoContentTask = new RepoContentTask(RepoFragment.this);
		repoContentTask.execute();

	}

	@SuppressLint("NewApi")
	private void repoContentClick(int position) {

		allTaskDown();

		RepoContent repoContent = repoContents.get(position);
		treeEntry = repoContent.getTreeEntry();

		if (treeEntry.getType().equals("tree")) {
			String[] str = treeEntry.getPath().split("/");
			title = str[str.length - 1];

			if (toggle) {
				if (prefix.equals("/")) {
					subTitle = name + "/" + treeEntry.getPath();
				} else {
					subTitle = name + "/" + prefix + "/" + treeEntry.getPath();
				}
			} else {
				subTitle = name + "/" + treeEntry.getPath();
			}

			actionBar.setTitle(title);
			actionBar.setSubtitle(subTitle);
			actionBar.setDisplayHomeAsUpEnabled(true);

			flag = 2;

			repoContentTask = new RepoContentTask(RepoFragment.this);
			repoContentTask.execute();
		}

	}

	public void allTaskDown() {

		Log.i("DEMO", "RepoFragment-allTaskDown");

		if (repoTask != null
				&& repoTask.getStatus() == AsyncTask.Status.RUNNING) {
			repoTask.cancel(true);
			Log.i("DEMO", "RepoFragment-allTaskDown-repoTask-down");
		}

		if (repoContentTask != null
				&& repoContentTask.getStatus() == AsyncTask.Status.RUNNING) {
			repoContentTask.cancel(true);
			Log.i("DEMO", "RepoFragment-allTaskDown-repoContentTask-down");
		}

	}

	@SuppressLint("NewApi")
	public void repoContentBack(int status) {
		allTaskDown();

		actionBar.setTitle(R.string.app_name);
		actionBar.setSubtitle(null);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(false);

		treeEntry = null;
		root = null;
		toggle = false;
		prefix = "/";

		listView.setAdapter(repoItemAdapter);
		repoItemAdapter.notifyDataSetChanged();

		currentType = TYPE_REPO;
		flag = status;

		repoTask = new RepoTask(RepoFragment.this);
		repoTask.execute();
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public RepoItemAdapter getRepoItemAdapter() {
		return repoItemAdapter;
	}

	public void setRepoItemAdapter(RepoItemAdapter repoItemAdapter) {
		this.repoItemAdapter = repoItemAdapter;
	}

	public List<RepoItem> getRepoItemList() {
		return repoItemList;
	}

	public void setRepoItemList(List<RepoItem> repoItemList) {
		this.repoItemList = repoItemList;
	}

	public List<RepoContent> getRepoContents() {
		return repoContents;
	}

	public void setRepoContents(List<RepoContent> repoContents) {
		this.repoContents = repoContents;
	}

	public RepoContentAdapter getRepoContentAdapter() {
		return repoContentAdapter;
	}

	public void setRepoContentAdapter(RepoContentAdapter repoContentAdapter) {
		this.repoContentAdapter = repoContentAdapter;
	}

	public GitHubClient getGitHubClient() {
		return gitHubClient;
	}

	public void setGitHubClient(GitHubClient gitHubClient) {
		this.gitHubClient = gitHubClient;
	}

	public String getOwner() {
		return owner;
	}

	public String getName() {
		return name;
	}

	public TreeEntry getTreeEntry() {
		return treeEntry;
	}

	public Tree getRoot() {
		return root;
	}

	public boolean isToggle() {
		return toggle;
	}
}
