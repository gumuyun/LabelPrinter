package cn.zcbdqn.labelprinter.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.zcbdqn.labelprinter.util.Object2Values;

/**
 * 数据库操作类
 * @author gumuyun
 *
 */
public class MyDataOpenHelper<T> extends SQLiteOpenHelper {
	private Class<T> clazz;
	private static final int VERSION = 1;
	private static final String DB_NAME = "print.db";
	

	public MyDataOpenHelper(Context context, String name,
							CursorFactory factory, int version,Class<T> clazz) {
		super(context, name, factory, version);
		this.clazz=clazz;
	}

	public MyDataOpenHelper(Context context,Class<T> clazz) {
		this(context,DB_NAME,null,VERSION,clazz);
		this.clazz=clazz;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {

		StringBuffer createTemplateTb=new StringBuffer();
		createTemplateTb.append("create table sys_template (");
		createTemplateTb.append("layoutId integer NOT NULL PRIMARY KEY,");
		createTemplateTb.append("templateName varchar(32),");
		createTemplateTb.append("viewType varchar(100),");
		createTemplateTb.append("printType integer,");
		createTemplateTb.append("paperType integer,");
		createTemplateTb.append("width integer NOT NULL,");
		createTemplateTb.append("height integer NOT NULL,");
		createTemplateTb.append("createBy integer NOT NULL,");
		createTemplateTb.append("createDate varchar(32) NOT NULL,");
		createTemplateTb.append("updateBy integer,");
		createTemplateTb.append("updateDate varchar(32),");
		createTemplateTb.append("remarks varchar(255)");
		createTemplateTb.append(")");
		db.execSQL(createTemplateTb.toString());

		StringBuffer createViewTb=new StringBuffer();
		createViewTb.append("create table sys_template_edit_view (");
		createViewTb.append("id integer NOT NULL PRIMARY KEY AUTOINCREMENT,");
		createViewTb.append("viewId integer NOT NULL,");
		createViewTb.append("viewType varchar(100),");
		createViewTb.append("text varchar(32),");
		createViewTb.append("parentViewId integer NOT NULL,");
		createViewTb.append("width integer NOT NULL,");
		createViewTb.append("height integer NOT NULL,");
		createViewTb.append("gravity integer,");
		createViewTb.append("topMargin integer NOT NULL,");
		createViewTb.append("rightMargin integer NOT NULL,");
		createViewTb.append("bottomMargin integer NOT NULL,");
		createViewTb.append("leftMargin integer NOT NULL,");
		createViewTb.append("fillParent integer,");
		createViewTb.append("matchParent integer,");
		createViewTb.append("wrapContent integer,");
		createViewTb.append("textSize integer ,");
		createViewTb.append("textColor varchar,");
		createViewTb.append("createBy integer NOT NULL,");
		createViewTb.append("createDate varchar(32) NOT NULL,");
		createViewTb.append("updateBy integer,");
		createViewTb.append("updateDate varchar(32),");
		createViewTb.append("remarks varchar(255)");
		createViewTb.append(")");
		db.execSQL(createViewTb.toString());

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

	/**
	 * 增加数据
	 * @param tableName 表名
	 * @param nullColumnHack 空列 
	 * @param values 字段-值
	 */
	public long insert(String tableName,String nullColumnHack,ContentValues values){
		SQLiteDatabase db = getReadableDatabase();
		return db.insert(tableName, nullColumnHack, values);
	}

	public void execSQL(String sql,Object[] args){
		SQLiteDatabase db = getReadableDatabase();
		db.execSQL(sql,args);
	}
	/**
	 * 更新
	 * @param tableName 表名
	 * @param values 字段-值
	 * @param whereClause 条件
	 * @param whereArgs 条件的值
	 * @return 影响的行数
	 */
	public int update(String tableName,ContentValues values,String whereClause,String... whereArgs){
		SQLiteDatabase db = getReadableDatabase();
		return db.update(tableName, values, whereClause, whereArgs);
	}
	
	public List<T> queryList(String table,String selection ,String... selectionArgs ){
		List<T> list=new ArrayList<T>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(table, null, selection, selectionArgs, null, null, null);
		while (cursor.moveToNext()){ 
			list.add(getEntity(cursor));
		}
		return list;
	}
	/**
	 * 查询数量
	 * @param selectionArgs
	 * @return
	 */
	public int queryCount(String sql ,String... selectionArgs){
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		if(cursor.moveToFirst()){
			return cursor.getInt(0);
		}
		return -1;
	}

	/**
	 * 单独
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public T queryUnique(String sql , String... selectionArgs){
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		if (cursor.moveToFirst()){
			return getEntity(cursor);
		}
		return null;
	}

	/**
	 * 使用反射获得取数库中的数据
	 * @param cursor
	 * @return
	 */
	public T getEntity(Cursor cursor)  {
		//创建目标对象
		T entity = null;
		try {
			//创建目标对象
			entity = clazz.newInstance();
			//获得所有声明的属性
			Field[] fields = clazz.getDeclaredFields();
			//授权
			AccessibleObject.setAccessible(fields,true);
			if (fields!=null){
				for (Field field:fields){
					if (Object2Values.isMetaData(field.getType())){
						if (field.getClass().equals(String.class)){
							//如果属性是string
							field.set(entity,cursor.getString(cursor.getColumnIndex(field.getName())));
						}else if (field.getClass().equals(Integer.class) || field.getClass().equals(int.class)){
							//如果是int
							field.set(entity,cursor.getInt(cursor.getColumnIndex(field.getName())));
						}else if (field.getClass().equals(Double.class) || field.getClass().equals(double.class)){
							//如果是double
							field.set(entity,cursor.getDouble(cursor.getColumnIndex(field.getName())));
						}else if (field.getClass().equals(Float.class) || field.getClass().equals(float.class)){
							//如果是float
							field.set(entity,cursor.getFloat(cursor.getColumnIndex(field.getName())));
						}else if (field.getClass().equals(Short.class) || field.getClass().equals(short.class)){
							//如果是short
							field.set(entity,cursor.getShort(cursor.getColumnIndex(field.getName())));
						}else if (field.getClass().equals(Long.class) || field.getClass().equals(long.class)){
							//如果是long
							field.set(entity,cursor.getLong(cursor.getColumnIndex(field.getName())));
						}
						//其它类型的不支持
					}
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return entity;
	}
}
