package com.csumax.maxgithubclient.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.csumax.maxgithubclient.db.GitHubSQLiteOpenHelper;
import com.csumax.maxgithubclient.entity.RepoItem;

public class RepoItemDao {

	private GitHubSQLiteOpenHelper gitHubSQLiteOpenHelper;
	private SQLiteDatabase sqLiteDatabase;

	public RepoItemDao(Context context) {
		gitHubSQLiteOpenHelper = new GitHubSQLiteOpenHelper(context);
	}

	public void openDatabase(boolean isWritable) throws SQLException {

		if (isWritable) {
			sqLiteDatabase = gitHubSQLiteOpenHelper.getWritableDatabase();
		} else {
			sqLiteDatabase = gitHubSQLiteOpenHelper.getReadableDatabase();
		}

	}

	public void closeDatabase() {
		sqLiteDatabase.close();
	}

	public void addRepoItem(RepoItem repoItem) {

		//sqLiteDatabase = gitHubSQLiteOpenHelper.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		contentValues.put(RepoItem.NAME, repoItem.getName());
		contentValues.put(RepoItem.DATE, repoItem.getDate());
		contentValues.put(RepoItem.DESCRIPTION, repoItem.getDescription());
		contentValues.put(RepoItem.LANG, repoItem.getLang());
		contentValues.put(RepoItem.STAR, repoItem.getStar());
		contentValues.put(RepoItem.FORK, repoItem.getFork());
		contentValues.put(RepoItem.OWNER, repoItem.getOwner());
		contentValues.put(RepoItem.GIT, repoItem.getGit());

		sqLiteDatabase.insert(RepoItem.TABLE, null, contentValues);

		//sqLiteDatabase.close();

	}

	public void deleteRepoItem(String git) {

		//sqLiteDatabase = gitHubSQLiteOpenHelper.getWritableDatabase();

		sqLiteDatabase.execSQL("DELETE FROM " + RepoItem.TABLE + " WHERE "
				+ RepoItem.GIT + " like \"" + git + "\"");

		//sqLiteDatabase.close();

	}

	public void deleteAllRepoItem() {

		//sqLiteDatabase = gitHubSQLiteOpenHelper.getWritableDatabase();

		sqLiteDatabase.delete(RepoItem.TABLE, null, null);

		//sqLiteDatabase.close();

	}

	public boolean checkRepoItem(String git) {

		//sqLiteDatabase = gitHubSQLiteOpenHelper.getReadableDatabase();

		Cursor cursor = sqLiteDatabase.query(RepoItem.TABLE,
				new String[] { RepoItem.GIT }, RepoItem.GIT + "=?",
				new String[] { git }, null, null, null);

		if (cursor != null) {
			boolean result = false;
			if (cursor.moveToFirst()) {
				result = true;
			}
			cursor.close();
			//sqLiteDatabase.close();
			return result;
		} else {
			//sqLiteDatabase.close();
		}
		return false;
	}

	/**
	 * 从 Cursor 中读取 Repo
	 * 
	 * @param cursor
	 * @return
	 */
	private RepoItem readRepoItemFromCursor(Cursor cursor) {
		RepoItem repoItem = new RepoItem();

		repoItem.setName(cursor.getString(0));
		repoItem.setDate(cursor.getString(1));
		repoItem.setDescription(cursor.getString(2));
		repoItem.setLang(cursor.getString(3));
		repoItem.setStar(cursor.getInt(4));
		repoItem.setFork(cursor.getInt(5));
		repoItem.setOwner(cursor.getString(6));
		repoItem.setGit(cursor.getString(7));

		return repoItem;
	}

	/**
	 * 获取 Repo 列表
	 * 
	 * @return
	 */
	public List<RepoItem> getRepoItemList() {
		List<RepoItem> repoItemList = new ArrayList<RepoItem>();
	//	sqLiteDatabase = gitHubSQLiteOpenHelper.getReadableDatabase();

		Cursor cursor = sqLiteDatabase.query(RepoItem.TABLE, new String[] {
				RepoItem.NAME, RepoItem.DATE, RepoItem.DESCRIPTION,
				RepoItem.LANG, RepoItem.STAR, RepoItem.FORK, RepoItem.OWNER,
				RepoItem.GIT }, null, null, null, null, RepoItem.STAR);

		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				RepoItem repoItem = readRepoItemFromCursor(cursor);
				repoItemList.add(repoItem);
				cursor.moveToNext();
			}
		}

		cursor.close();
	//	sqLiteDatabase.close();

		return repoItemList;
	}
}
