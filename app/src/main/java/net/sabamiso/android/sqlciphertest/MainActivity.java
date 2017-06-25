package net.sabamiso.android.sqlciphertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import net.sqlcipher.database.SQLiteDatabase;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public final String TAG = getClass().getSimpleName();

    TextView textCount;
    Button buttonInsertTestData;
    Button buttonDeleteAll;
    Button buttonGetRanking;
    Button buttonRangeData;

    void log_d(String msg) {
        Log.d(TAG, msg);
    }

    void log_e(String msg, Exception e) {
        Log.e(TAG, msg, e);
        Toast.makeText(this, msg + ":" + e.toString(), Toast.LENGTH_LONG).show();
    }

    void log_i(String msg) {
        Log.i(TAG, msg);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    long st = System.currentTimeMillis();
    void startStopWatch() {
        st = System.currentTimeMillis();
    }

    void endStopWatch() {
        long diff = System.currentTimeMillis() - st;
        String msg = String.format(Locale.US, "t=%.2f", diff / 1000.0);
        log_i(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize sqlcipher JNI libraries
        SQLiteDatabase.loadLibs(this);

        textCount = (TextView)findViewById(R.id.textCount);
        updateCount();

        buttonInsertTestData = (Button)findViewById(R.id.buttonInsertTestData);
        buttonInsertTestData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonInsertTestData();
            }
        });

        buttonDeleteAll = (Button)findViewById(R.id.buttonDeleteAll);
        buttonDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonDeleteAll();
            }
        });

        buttonGetRanking = (Button)findViewById(R.id.buttonGetRankingTest);
        buttonGetRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonGetRanking();
            }
        });

        buttonRangeData = (Button)findViewById(R.id.buttonRangeTest);
        buttonRangeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonRangeData();
            }
        });
    }

    void updateCount() {
        Dao<Test, Integer> dao = TestDBHelper.getTestDao(this);
        try {
            QueryBuilder queryBuilder = dao.queryBuilder();
            queryBuilder.setCountOf(true);
            int count = (int) dao.countOf(queryBuilder.prepare());
            textCount.setText("count="+count);
        } catch (SQLException e) {
            log_e("dao.countOf() failed...", e);
        }
    }

    void onButtonInsertTestData() {
        log_d("onButtonInsertTestData()");

        Dao<Test, Integer> dao = TestDBHelper.getTestDao(this);

        Random rand = new Random();
        Calendar cal = Calendar.getInstance();

        startStopWatch();
        for (int uid = 0; uid < 100; ++uid) {
            for (int i = 0; i < 1000; ++i) {
                // n日前の日付を作る
                Date d = new Date();
                cal.setTime(d);
                cal.add(Calendar.DATE, -i);
                d = cal.getTime();

                Test t = new Test(
                        String.format("name%03d", uid),
                        d,
                        rand.nextInt(100),
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + i,
                        "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb" + i,
                        "cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc" + i
                );
                try {
                    dao.create(t);
                } catch (SQLException e) {
                    log_e("dao.create() failed...", e);
                    return;
                }
            }
        }

        endStopWatch();
        updateCount();
    }

    void onButtonDeleteAll() {
        Dao<Test, Integer> dao = TestDBHelper.getTestDao(this);

        startStopWatch();
        try {
            dao.deleteBuilder().delete();
        } catch (SQLException e) {
            log_e("dao.delete() failed...", e);
        }
        endStopWatch();
        updateCount();
    }

    void onButtonGetRanking() {
        Dao<Test, Integer> dao = TestDBHelper.getTestDao(this);

        startStopWatch();
        int rank = -1;
        try {
            QueryBuilder queryBuilder = dao.queryBuilder();
            queryBuilder.setCountOf(true);
            queryBuilder.where().gt("score", 50);
            rank = (int) dao.countOf(queryBuilder.prepare()) + 1;
            log_i("rank=" + rank);
        } catch (SQLException e) {
            Log.e(TAG, "dao.countOf() failed..." + e);
        }

        endStopWatch();
    }

    void onButtonRangeData() {
        Dao<Test, Integer> dao = TestDBHelper.getTestDao(this);

        startStopWatch();

        // 100日前→今
        Date start = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        cal.add(Calendar.DATE, -100);
        start = cal.getTime();

        Date end = new Date();

        try {
            QueryBuilder query_builder = dao.queryBuilder();
            query_builder.where()
                    .eq("name", "name001")
                    .and()
                    .ge("date", start)
                    .and()
                    .le("date", end);
            query_builder.orderBy("date", true);
            List<Test> rv = query_builder.query();
            log_i("count=" + rv.size());
        } catch (SQLException e) {
            log_e("query_builder.query() failed...", e);
        }
        endStopWatch();
    }
}
