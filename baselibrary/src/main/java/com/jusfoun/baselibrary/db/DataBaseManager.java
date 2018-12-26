package com.jusfoun.baselibrary.db;

import android.content.Context;
import android.text.TextUtils;

import com.jusfoun.baselibrary.Util.LogUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhaoyapeng on 2017/9/26.
 * describe
 */

public class DataBaseManager {
    private final static String TAG="DataBaseManager";
    private Map<String, SqlDataBase> mDatabases = new HashMap<String, SqlDataBase>();
    private static DataBaseManager mInstance = null;

    private static final class SingletonHolder{
        private static final DataBaseManager INSTANCE=new DataBaseManager();
    }

    public static DataBaseManager getInstance(){
        return SingletonHolder.INSTANCE;
    }
    private DataBaseManager(){

    }

    public void init(Context context){

    }

    /**
     * 注册一个数据库
     * @param db 用户自定义数据库
     */
    public void registerDB(SqlDataBase db){
        if(db == null){
            return;
        }
//        closeDb(db);
        mDatabases.put(db.getDatabaseName(), db);
        LogUtil.d(TAG, "registerDB() getDatabaseName() is " + db.getDatabaseName());
    }

    /**
     * 解注册一个数据库
     * @param db 用户自定义数据库
     */
    public void closeDb(SqlDataBase db){
        if(db == null){
            return;
        }
        closeDb(db.getDatabaseName());
    }

    /**
     * 解注册一个数据库
     * @param dbName 数据库名称
     */
    public void closeDb(String dbName){
        SqlDataBase db = getDb(dbName);
        if(db != null){
            db.close();
            mDatabases.remove(dbName);
        }
    }

    /**
     * 释放数据库
     */
    public void closeAllDB(){
        Iterator<Map.Entry<String, SqlDataBase>> it = mDatabases.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, SqlDataBase> entry= it.next();
//           String key= entry.getKey();
            try {
                if(entry.getValue() != null){
                    entry.getValue().close();
                }
            } catch (Exception e) {
            }
        }
        mDatabases.clear();
    }

    /**
     * 返回一个数据库对象
     * @param dbName 数据库名
     */
    public SqlDataBase getDb(String dbName){
        if(TextUtils.isEmpty(dbName)){
            return null;
        }
        return mDatabases.get(dbName);
    }

    /**
     * 获得表实例
     * @param table
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseTable<?>> T getTable(String table){
        Iterator<Map.Entry<String, SqlDataBase>> it = mDatabases.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, SqlDataBase> entry= it.next();
            BaseTable<?> baseTable = getTable(entry.getValue(),table);
            if(baseTable != null){
                return (T) baseTable;
            }
        }
        return null;
    }

    /**
     * 获得表实例
     * @param table
     * @return
     */
    public <T extends BaseTable<?>> T getTable(SqlDataBase db,String table){
        if(db == null){
            return null;
        }
        return (T) db.getTable(table);
    }

    public <T extends BaseTable<?>> SqlDataBase getData(String table){
        Iterator<Map.Entry<String, SqlDataBase>> it = mDatabases.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, SqlDataBase> entry= it.next();
            BaseTable<?> baseTable = getTable(entry.getValue(),table);
            if (baseTable!=null)
                return entry.getValue();

        }
        return null;
    }
}
