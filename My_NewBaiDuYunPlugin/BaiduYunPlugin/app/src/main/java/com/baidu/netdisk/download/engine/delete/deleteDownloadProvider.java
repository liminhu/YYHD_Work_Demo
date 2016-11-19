package com.baidu.netdisk.download.engine.delete;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.gameassist.plugin.bob.CustomGGZSBroadReceiver;

public class deleteDownloadProvider extends ContentProvider {
	public static final String AUTHORITY = "com.baidu.netdisk.download.engine.delete";
	private static final int URLINFOS = 1;
	private static final UriMatcher mUriMatcher;

	//Uri uri = Uri.parse("content://com.baidu.netdisk.download.engine.delete/downloadtasks/1");
	private static final String METHOD_DELETE_GAMEID = "deleteGameId";

	@Override
	public int delete(Uri uri, String arg1, String[] arg2) {
		/*
		 * Bundle extras = new Bundle(); extras.putString("gameid",
		 * "dfsafadffdaf"); getContext().getContentResolver().call(uri,
		 * "deleteGameId", null, extras);
		 * 
		 */
		return 0;
	}

	@Override
	public Bundle call(String method, String arg, Bundle extras) {
		if(method.equals(METHOD_DELETE_GAMEID)){
			Log.i("hook_call_delete_method",METHOD_DELETE_GAMEID);
			String gameId = extras.getString("gameId");
			if (!TextUtils.isEmpty(gameId)) {
				Context context = getContext();
				SharedPreferences share = context.getSharedPreferences(
						"ggzs_gameId", 0);
				String db_id = share.getString(gameId, "");
				if (!TextUtils.isEmpty(db_id)) {
					int downloadId = Integer.valueOf(db_id);
					CustomGGZSBroadReceiver.uri_bduss = "content://com.baidu.netdisk/transfer/downloadtasks?bduss="
							+ CustomGGZSBroadReceiver.getLoginBdussInfo(context);
					CustomGGZSBroadReceiver.UpdateDB_Delete(downloadId, context,
							gameId);
				}
			}
		}
		return null;
	}

	@Override
	public String getType(Uri arg0) {
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		return null;
	}

	@Override
	public boolean onCreate() {
		return false;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		return 0;
	}

	static {
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatcher.addURI(AUTHORITY, "downloadtasks", URLINFOS);
	}

}
