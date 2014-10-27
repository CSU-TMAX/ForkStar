package com.csumax.maxgithubclient.db;

import com.csumax.maxgithubclient.entity.RepoItem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GitHubSQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "github.db";
	private static final int DATABASE_VERSION = 1;

	private static final String CREATE_REPO_TABLE = "CREATE TABLE "
			+ RepoItem.TABLE + " (" + " NAME text," + " DATE text,"
			+ " DESCRIPTION text," + " LANG text," + " STAR integer,"
			+ " FORK integer," + " OWNER text," + " GIT text" + ")";

	public GitHubSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_REPO_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		// TODO Auto-generated method stub

	}

}
