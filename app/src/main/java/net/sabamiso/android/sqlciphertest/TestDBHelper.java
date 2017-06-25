package net.sabamiso.android.sqlciphertest;

import android.content.Context;
import android.util.Log;

// ORMLiteとSQLCipherを併用する場合は、次の修正をおこなう
//  (1) build.gradleに以下を追加
//      compile 'com.j256.ormlite.cipher:ormlite-sqlcipher:1.1@aar'
//      compile 'com.j256.ormlite:ormlite-core:5.0'
//      compile 'com.j256.ormlite:ormlite-android:5.0'
//      compile 'net.zetetic:android-database-sqlcipher:3.5.7@aar'
//
//  (2) パッケージを変更する
//       import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
//       import android.database.sqlite.SQLiteDatabase;
//     の代わりに
//       import com.j256.ormlite.cipher.android.apptools.OrmLiteSqliteOpenHelper;
//       import net.sqlcipher.database.SQLiteDatabase;
//     を使用する。
//
//  (3) HelperクラスにgetPassword()メソッドを追加する
//  (4) アプリ起動時にSQLiteDatabase.loadLibs(context);を実行する
//      これを実行するとJNIライブラリを読み込む。実行しないとデータベースファイルのオープンに失敗する…
//
// 参考 → https://github.com/Andoctorey/ormlite-sqlcipher
//
import com.j256.ormlite.cipher.android.apptools.OrmLiteSqliteOpenHelper;
import net.sqlcipher.database.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class TestDBHelper  extends OrmLiteSqliteOpenHelper {
    public static final String TAG = TestDBHelper.class.getSimpleName();

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "test.db";

    public TestDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static TestDBHelper helper;
    private static Dao<Test, Integer> testDao;

    public static Dao<Test, Integer> getTestDao(Context context) {
        if (helper == null) {
            helper = new TestDBHelper(context);
        }

        if (testDao == null) {
            try {
                testDao = helper.getDao(Test.class);
            }
            catch (SQLException e) {
                Log.e(TAG, "getDao() failed...", e);
            }
        }
        return testDao;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        Log.i(TAG, "onCreate()");
        try {
            TableUtils.createTable(connectionSource, Test.class);
        } catch (SQLException e) {
            Log.e(TAG, "TableUtils.createTable() failed...", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(TAG, "onUpgrade()");
            TableUtils.dropTable(connectionSource, Test.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "TableUtils.dropTable() failed...", e);
        }
    }

    protected String getPassword() {
        return "9b9e41d0ae3b7df97c277896a2205cae"; // test password...
    }

    @Override
    public void close() {
        super.close();
        testDao = null;
    }
}
