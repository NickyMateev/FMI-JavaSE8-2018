package bg.unisofia.fmi.fitme;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView currentWeekText = (TextView) findViewById(R.id.weekText);

        String weekStartDate = getMonthDayString(getWeekStartDate());
        String weekEndDate = getMonthDayString(getWeekEndDate());

        currentWeekText.setText(weekStartDate + " - " + weekEndDate);
    }

    private static Calendar getWeekStartDate() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }

        return calendar;
    }

    private static Calendar getWeekEndDate() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        calendar.add(Calendar.DATE, -1);
        return calendar;
    }

    private static String getMonthDayString(Calendar calendar) {
        int monthNumber = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        return getMonthName(monthNumber) + " " + dayOfMonth;
    }

    private static String getMonthName(int monthNumber) {
        switch (monthNumber) {
            case 0: return "Jan";
            case 1: return "Feb";
            case 2: return "Mar";
            case 3: return "Apr";
            case 4: return "May";
            case 5: return "Jun";
            case 6: return "Jul";
            case 7: return "Aug";
            case 8: return "Sep";
            case 9: return "Oct";
            case 10: return "Nov";
            case 11: return "Dec";
            default: return "???";
        }
    }

}
