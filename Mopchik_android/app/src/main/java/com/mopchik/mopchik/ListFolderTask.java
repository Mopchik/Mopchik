package com.mopchik.mopchik;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;

/**
 * Async task to list items in a folder
 */
class ListFolderTask extends AsyncTask<String, Void, ListFolderResult> {

    private final DbxClientV2 mDbxClient;
    private final Callback mCallback;
    private Exception mException=null;

    public interface Callback {
        void onDataLoaded(ListFolderResult result);

        void onError(Exception e);
    }

    public ListFolderTask(DbxClientV2 dbxClient, Callback callback) {
        mDbxClient = dbxClient;
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(ListFolderResult result) {
        super.onPostExecute(result);

        if (mException != null) {
            mCallback.onError(mException);
        } else {
            mCallback.onDataLoaded(result);
        }
    }

    @Override
    protected ListFolderResult doInBackground(String... params) {
        try {
            return mDbxClient.files().listFolder(params[0]);
        } catch (DbxException e) {
            Boolean b = mDbxClient==null;
            Log.e("hui",b.toString());
            mException = e;
        }

        return null;
    }
}
