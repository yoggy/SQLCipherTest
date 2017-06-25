package net.sabamiso.android.sqlciphertest;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.SimpleDateFormat;
import java.util.Date;

@DatabaseTable(tableName = "test")
public class Test {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_TMP1 = "tmp1";
    public static final String COLUMN_TMP2 = "tmp2";
    public static final String COLUMN_TMP3 = "tmp3";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true, allowGeneratedIdInsert = true)
    public int id;

    @DatabaseField(columnName = COLUMN_NAME, canBeNull = false, indexName = "range_query_idx")
    public String name;

    @DatabaseField(columnName = COLUMN_DATE, canBeNull = true, indexName = "ranking_idx")
    public Date date;

    @DatabaseField(columnName = COLUMN_SCORE, canBeNull = true, indexName = "range_query_idx")
    public int score;

    @DatabaseField(columnName = COLUMN_TMP1, canBeNull = true)
    public String tmp1;

    @DatabaseField(columnName = COLUMN_TMP2, canBeNull = true)
    public String tmp2;

    @DatabaseField(columnName = COLUMN_TMP3, canBeNull = true)
    public String tmp3;

    public Test() {
    }

    public Test(String name, Date date, int score, String tmp1, String tmp2, String tmp3) {
        this.name = name;
        this.date = date;
        this.score = score;
        this.tmp1 = tmp1;
        this.tmp2 = tmp2;
        this.tmp3 = tmp3;
    }

    public String toString() {
        return String.format("{id:%d, %s, %s, %d}", id, name, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date), score);
    }
}
