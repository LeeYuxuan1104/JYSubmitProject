package cn.model.tool.common;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class MTSQLiteHelper {
	private SQLiteDatabase 		 	mDB;		//  数据库件;
    private MTDBHelper 	   		 	mDBhelper;	//  辅助控件;
	
	public SQLiteDatabase getmDB() {
		return mDB;
	}

	public void setmDB(SQLiteDatabase mDB) {
		this.mDB = mDB;
	}

	public MTSQLiteHelper(Context mContext) {
		File 	file 	=	mContext.getFilesDir();
		String 	path 	= 	file.getAbsolutePath() + "/myDB.db";
		mDBhelper  		= 	new MTDBHelper(mContext, path, 1);
		mDB 			= 	mDBhelper.getReadableDatabase();
	}
	
	public void doCloseDataBase(){
		if(mDB!=null){
			mDB.close();
			mDB=null;
		}
		if(mDBhelper!=null){
			mDBhelper.close();
			mDBhelper=null;
		}
	}
}
