package bg.unisofia.fmi.fitme.bg.unisofia.fmi.fitme.persistency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

import bg.unisofia.fmi.fitme.bg.unisofia.fmi.fitme.models.Week;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "fitme.db";

    private static final String TABLE_WEEKS = "weeks";
    private static final String TABLE_WEEKS_COLUMN_ID = "_id";
    private static final String TABLE_WEEKS_COLUMN_START_DATE = "start_date";
    private static final String TABLE_WEEKS_COLUMN_END_DATE = "end_date";
    private static final String TABLE_WEEKS_COLUMN_DAILY_CAL_GOAL = "daily_cal_goal";


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_WEEKS + "(" +
                TABLE_WEEKS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TABLE_WEEKS_COLUMN_START_DATE + " DATE," +
                TABLE_WEEKS_COLUMN_END_DATE + " DATE," +
                TABLE_WEEKS_COLUMN_DAILY_CAL_GOAL + " INTEGER" +
                ");";

        db.execSQL(query);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEEKS + ";");
        onCreate(db);
    }

    public void addWeek(Week week) {
        ContentValues values = new ContentValues();
        values.put(TABLE_WEEKS_COLUMN_START_DATE, week.get_startDate().toString());
        values.put(TABLE_WEEKS_COLUMN_END_DATE, week.get_endDate().toString());
        values.put(TABLE_WEEKS_COLUMN_DAILY_CAL_GOAL, week.get_dailyCalorieGoal());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_WEEKS, null, values);
        db.close();
    }

    public void deleteWeek(Date startDate, Date endDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_WEEKS +
                " WHERE " + TABLE_WEEKS_COLUMN_START_DATE + "=\"" + startDate.toString() + "\"" +
                " AND " + TABLE_WEEKS_COLUMN_END_DATE + "=\"" + endDate.toString() + "\";");
        db.close();
    }

    public Week getWeek(Date startDate, Date endDate) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_WEEKS +
                " WHERE " + TABLE_WEEKS_COLUMN_START_DATE + "=\"" + startDate.toString() + "\"" +
                " AND " + TABLE_WEEKS_COLUMN_END_DATE + "=\"" + endDate.toString() + "\";";

        Week week = new Week();

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if(!c.isAfterLast()) {
            week.set_startDate(new Date(c.getString(c.getColumnIndex(TABLE_WEEKS_COLUMN_START_DATE))));
            week.set_endDate(new Date(c.getString(c.getColumnIndex(TABLE_WEEKS_COLUMN_END_DATE))));
            week.set_dailyCalorieGoal(c.getInt(c.getColumnIndex(TABLE_WEEKS_COLUMN_DAILY_CAL_GOAL)));
        } else {
            week = null;
        }

        db.close();
        return week;
    }

    public void updateDailyCaloriesGoalForWeek(Week week) {
        Week weekFromDB = getWeek(week.get_startDate(), week.get_endDate());
        if (weekFromDB == null) {
            addWeek(week);
        } else {
            SQLiteDatabase db = getWritableDatabase();
            String query = "UPDATE " + TABLE_WEEKS +
                    " SET " + TABLE_WEEKS_COLUMN_DAILY_CAL_GOAL + "=" + week.get_dailyCalorieGoal() +
                    " WHERE " + TABLE_WEEKS_COLUMN_START_DATE + "=\"" + week.get_startDate().toString() + "\"" +
                    " AND " + TABLE_WEEKS_COLUMN_END_DATE + "=\"" + week.get_endDate().toString() + "\";";
            db.execSQL(query);
            db.close();
        }
    }

    public int getDailyCaloriesForWeek(Date startDate, Date endDate) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_WEEKS +
                " WHERE " + TABLE_WEEKS_COLUMN_START_DATE + "=\"" + startDate.toString() + "\"" +
                " AND " + TABLE_WEEKS_COLUMN_END_DATE + "=\"" + endDate.toString() + "\";";

        int dailyCalories;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if(!c.isAfterLast()) {
            dailyCalories = c.getInt(c.getColumnIndex(TABLE_WEEKS_COLUMN_DAILY_CAL_GOAL));
        } else {
            dailyCalories = -1;
        }

        db.close();
        return dailyCalories;
    }
}
