package com.jusfoun.baselibrary.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.jusfoun.baselibrary.Util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by zhaoyapeng on 2017/9/26.
 * describe 数据库 基类
 */

public abstract class SqlDataBase extends SQLiteOpenHelper {
    private static final String TAG="SqlDataBase";
    private boolean mCheckDatabaseAtAsserting = false;
    private Hashtable<String, BaseTable<?>> mDbTables = new Hashtable<String, BaseTable<?>>();
    private SQLiteDatabase mDefaultWritableDatabase = null;
    private Context mContext;

    public SqlDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
        onRegistTables();
    }

    /**
     * 注册数据库中所有的表,必须调用registTable进行注册
     */
    protected abstract void onRegistTables();

    /**
     * 注册数据库中的表
     */
    protected void registTable(String tableName,
                               BaseTable<?> baseTable) {
        if(tableName == null || baseTable == null){
            LogUtil.e(TAG,
                    "registTable() --- table == null || baseTable == null");
            return;
        }
        BaseTable<?> tmpBaseTable = getTable(tableName);
        if(tmpBaseTable == null){
            mDbTables.put(tableName, baseTable);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
// 创建数据库表
        this.mDefaultWritableDatabase = db;
        createAllTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.d(TAG, "+++SqlDataBase,fromVersion:" + oldVersion
                + ",toVersion:" + newVersion);
        this.mDefaultWritableDatabase = db;
        if(mDbTables.size()>0) {
            for (BaseTable<?> baseTable : mDbTables.values()) {
                if (!baseTable.tableIsExist()) {
                    createTable(baseTable, db);
                    //createTable(baseTable, mDefaultWritableDatabase);
                } else {
                    baseTable.onUpgraded(this, oldVersion, newVersion);
                }
            }
        }
    }

        /**
         * 创建所有数据库表
         */
        protected void createAllTable(SQLiteDatabase db) {
            if(mCheckDatabaseAtAsserting){
                LogUtil.e(TAG,
                        "createAllTable() --- mCheckDatabaseAtAsserting is true");
                return;
            }

            if(mDbTables.size()>0){
                for(BaseTable<?> baseTable : mDbTables.values()){
                    createTable(baseTable, db);
                }
            }
        }


    // 在数据库中创建一个新表
    private void createTable(BaseTable<?> baseTable, SQLiteDatabase db) {
        if (baseTable == null || db == null) {
            LogUtil.d(TAG, "createTable() --- baseTable == null || db == null");
            return;
        }
        if (baseTable.tableIsExist()) {
            LogUtil.d(TAG, baseTable.getTableName() + " has created!");
            return;
        }
        List<String> sqls = new ArrayList<String>();
        String sql = baseTable.sqlCmdCreate();
        if(!TextUtils.isEmpty(sql)){
            sqls.add(sql);
            execSQL(sqls, db);
            baseTable.onCreated(this);
        }
    }

    /**
     * 重写getWritableDatabase方法防止递归的出现
     */
    @Override
    public SQLiteDatabase getWritableDatabase() {
        final SQLiteDatabase db;
        if(mDefaultWritableDatabase != null){
            db = mDefaultWritableDatabase;
        } else {
            db = super.getWritableDatabase();
        }
        return db;
    }

    private static String DATABASE_PATH = "/data/data/%s/databases";

    private String getDatabaseFilepath(){
        return String.format(DATABASE_PATH, mContext
                .getApplicationInfo().packageName);
    }

    private String getDatabaseFile(String dbfile){
        return getDatabaseFilepath()+"/"+dbfile;
    }

    /**
     * 执行 SQLexec cmd
     *
     * @return
     */
    public void execSQL(List<String> sqls,SQLiteDatabase db){
        if(mCheckDatabaseAtAsserting){
            LogUtil.d(TAG, "execSQL() --- mCheckDatabaseAtAsserting is true");
            return;
        }
        if(sqls != null && sqls.size() > 0){
            for(int i=0; i< sqls.size(); i++){
                String sql = sqls.get(i);
                if(!TextUtils.isEmpty(sql)){
                    db.execSQL(sql);
                }
            }
        }
    }

    /**
     * 删除一个数据库表
     *
     * @return
     */
    public void dropTable(SQLiteDatabase db,String strTableName) {
        if(mCheckDatabaseAtAsserting){
            LogUtil.d(TAG, "dropTable() --- mCheckDatabaseAtAsserting is true");
            return;
        }
        try {
            db.execSQL("DROP TABLE IF EXISTS " + strTableName);
        } catch (SQLException ex) {
            LogUtil.e(TAG, "can not  drop table " + strTableName);
            ex.printStackTrace();
        }
    }

    /**
     * 获得表实例
     *
     * @param table
     * @return T extends BaseTable
     */
    @SuppressWarnings({ "unchecked" })
    public <T extends BaseTable<?>> T getTable(String table){
        if(mCheckDatabaseAtAsserting){
            LogUtil.e(TAG, "getTable() --- mCheckDatabaseAtAsserting is true");
            return null;
        }
        return (T) mDbTables.get(table);
    }
}
