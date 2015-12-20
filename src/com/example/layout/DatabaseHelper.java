package com.example.layout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// execSQL函数用于执行SQL语句
		System.out.println("start create a Database");
		String sql = "CREATE TABLE spring (_id INTEGER primary key autoincrement, detail TEXT, time TEXT, source TEXT)";
		System.out.println("start create a Database...");
		db.execSQL(sql);
		System.out.println("create a Database");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	
	public void delete(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "_id" + " = ?";
		String[] whereValue = {Integer.toString(id)};
		db.delete("spring", where, whereValue);
	}
	
	public long insert(String detail, String time, String source) {
		System.out.println("start insert");
		SQLiteDatabase db = this.getWritableDatabase();
		/* 将新增的值放入ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("detail", detail);
		cv.put("time", time);
		cv.put("source", source);
		long row = db.insert("spring", null, cv);
		System.out.println("finish insert");
		return row;
	}
	
	public Cursor select(){
		System.out.println("SELECT");
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("spring", null, null, null, null, null, null);
		System.out.println("finish SELECT");
		return cursor;
	}
	
	public void update(int id, String item, String text) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "_id = ?";
		String[] whereValue = {Integer.toString(id)};
		/*将修改的值放入ContenValues*/
		ContentValues cv = new ContentValues();
		cv.put(item, text);
		db.update("spring", cv, where, whereValue);
	}
	
}
