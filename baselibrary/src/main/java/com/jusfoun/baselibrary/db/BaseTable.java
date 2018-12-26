package com.jusfoun.baselibrary.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.jusfoun.baselibrary.Util.LogUtil;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyapeng on 2017/9/26.
 * describe 数据库表基类
 */

public abstract class BaseTable<T> implements BaseColumns {

    private String mTableName;
    private SqlDataBase mSqLiteOpenHelper;

    public BaseTable(String tableName, SqlDataBase sqlHelper) {
        this.mTableName = tableName;
        this.mSqLiteOpenHelper = sqlHelper;
    }

    /**
     * 获取表名
     * @return
     */
    public String getTableName(){
        return mTableName;
    }

    /**
     * cursor转换为model
     * @param cursor
     * @return
     */
    public abstract T getItemFromCursor(Cursor cursor);

    /**
     * 创建表 sql 语句
     * @return
     */
    public abstract String sqlCmdCreate();

    /**
     * 对象转成contentValues
     *
     * @param item
     * @return
     */
    public abstract ContentValues getContentValues(T item);

    /**
     * 检索所有数据到列表中
     *
     * @return
     */
    public List<T> queryAll() {
        return queryByCase(null, null, null);
    }

    /**
     * 通过特定条件检索数据
     *
     * @param where
     * @param orderBy
     * @return
     */
    public List<T> queryByCase(String where, String args[], String orderBy) {
        List<T> items = null;
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getWritableDatabase();
            if (db == null)
                return null;
            items = new ArrayList<T>();
            cursor = db.query(getTableName(), null, where, args, null, null, orderBy);
            while (cursor.moveToNext()) {
                items.add(getItemFromCursor(cursor));
            }
        } catch (Exception e) {
            LogUtil.e(getTableName(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return items;
    }

    public T queryFirstByCase(String where, String args[], String orderBy) {
        T firstItem = null;
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getWritableDatabase();
            if (db == null)
                return null;
            cursor = db.query(getTableName(), null, where, args, null, null, orderBy);
            if (cursor.moveToNext()) {
                firstItem = getItemFromCursor(cursor);
            }
        } catch (Exception e) {
            LogUtil.e(getTableName(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return firstItem;
    }

    /**
     * 获得数据行数
     *
     * @param where
     * @param args
     * @return
     */
    public int getCount(String where, String args[]) {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getWritableDatabase();
            if (db == null) {
                return 0;
            }

            cursor = db.query(getTableName(), null, where, args, null, null, null);
            return cursor.getCount();
        } catch (Exception e) {
            LogUtil.e(getTableName(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    /**
     * 表是否已被创建
     *
     * @return true 已经在数据库中创建
     * @author HYG HParty(modify)
     */
    public boolean tableIsExist() {
        boolean result = false;
        String tableName = getTableName();
        if (tableName == null) {
            return false;
        }
//         SQLiteDatabase db = getDatabase();
        Cursor cursor = null;
        String whereClause = "type ='table' and name ='" + tableName.trim() + "' ";
        try {
//             String sql = "select count(*) as c from "+ AppConstant.DataBaseName+" where type ='table' and name ='"+tableName.trim()+"' ";
//             cursor = db.rawQuery(sql, null);
            SQLiteDatabase db = getWritableDatabase();
            if (db == null) {
                return false;
            }
            //sqlite_master
            //getTableName()
            cursor = db.query("sqlite_master", null, whereClause, null, null, null, null);
//             while(cursor.moveToNext()){
//                 items.add(getItemFromCursor(cursor));
//             }

             /*if(cursor.moveToNext()){
                 int count = cursor.getInt(0);
                 if(count > 0){
                     result = true;
                 }
             }*/
            if (cursor.getCount() > 0) {
                result = true;
            }
        } catch (Exception e) {
            LogUtil.e(getTableName(), e.toString());
        }
        return result;
    }

    /**
     * 插入数据
     *
     * @param item
     * @return
     */
    public long insert(T item) {
        Cursor c = null;
        try {
            SQLiteDatabase db = getWritableDatabase();
            if (db == null) {
                return -1;
            }
            return db.insertWithOnConflict(getTableName(), null, getContentValues(item), SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            LogUtil.e(getTableName(), e);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return -1;
    }

    public long insert(ContentValues cv) {
        Cursor c = null;
        try {
            SQLiteDatabase db = getWritableDatabase();
            if (db == null)
                return -1;
            return db.insertWithOnConflict(getTableName(), null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            LogUtil.e(getTableName(), e);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return -1;
    }

    /**
     * 通过特定条件删除数据
     *
     * @param where
     * @param args
     * @return
     */
    public int deleteByCase(String where, String args[]) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            if (db == null)
                return -1;
            return db.delete(getTableName(), where, args);
        } catch (Exception e) {
            LogUtil.e(getTableName(), e);
        }
        return -1;
    }

    /**
     * 更新记录
     *
     * @param item
     * @param where
     * @param args
     * @return
     */
    public int updateByCase(T item, String where, String args[]) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            if (db == null)
                return -1;

            ContentValues values = getContentValues(item);
            return db.updateWithOnConflict(getTableName(), values, where, args, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            LogUtil.e(getTableName(), e);
        }
        return -1;
    }

    /**
     * 获得String
     *
     * @param cursor
     * @param columnName
     * @return
     */
    @SuppressLint("UseValueOf")
    @SuppressWarnings({"hiding", "unchecked"})
    public <T> T getValue(Cursor cursor, String columnName, Class<T> t) {
        int index = cursor.getColumnIndex(columnName);
        if (String.class.getName().equals(t.getName())) {
            if (index >= 0)
                return (T) cursor.getString(index);
            return null;
        } else if (Integer.class.getName().equals(t.getName())) {
            if (index >= 0)
                return (T) new Integer(cursor.getInt(index));
            return (T) new Integer(0);
        } else if (Long.class.getName().equals(t.getName())) {
            if (index >= 0)
                return (T) new Long(cursor.getLong(index));
            return (T) new Long(0);
        } else if (Float.class.getName().equals(t.getName())) {
            if (index >= 0)
                return (T) new Float(cursor.getFloat(index));
            return (T) new Float(0);
        } else if (Double.class.getName().equals(t.getName())) {
            if (index >= 0)
                return (T) new Double(cursor.getDouble(index));
            return (T) new Double(0);
        } else if (Date.class.getName().equals(t.getName())) {
            if (index >= 0)
                return (T) new Date(cursor.getLong(index));
            return (T) new Date(System.currentTimeMillis());
        }
        return null;
    }

    /**
     * 表被创建完后回调
     *
     * @param db
     */
    public void onCreated(SqlDataBase db) {
        LogUtil.d(getTableName(), getTableName() + "  Created!");
    }

    /**
     * 表被升级更新后回调
     *
     * @param db
     */
    public void onUpgraded(SqlDataBase db, int oldDBVersion, int newDBVersion) {
        LogUtil.d(getTableName(), getTableName() + " Upgraded!");
        if (!tableIsExist()) {
            LogUtil.e(getTableName(), getTableName() + " is not Exist! After add a new table,please upgrade DataBase version!");
        }
    }

    /**
     * 获得可写的数据库
     *
     * @return SQLiteDatabase
     */
    public SQLiteDatabase getWritableDatabase() {
        if (mSqLiteOpenHelper != null)
            return mSqLiteOpenHelper.getWritableDatabase();

        SqlDataBase helper = DataBaseManager.getInstance().getData(getTableName());
        if (helper == null)
            return null;
        return helper.getWritableDatabase();
    }
}
