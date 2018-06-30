package bg.unisofia.fmi.fitme;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static int dailyCalorieGoal;
    private static int weeklyCalorieGoal;

    private static int[] weeklyCalories = new int[7];
    private static int[] weeklyWeight = new int[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Current week setup:
        TextView currentWeekText = (TextView) findViewById(R.id.weekText);

        String weekStartDate = getMonthDayString(getWeekStartDate());
        String weekEndDate = getMonthDayString(getWeekEndDate());

        currentWeekText.setText(weekStartDate + " - " + weekEndDate);

        initializeWeeklyData();

        // Progress bar setup:
        Drawable draw= getResources().getDrawable(R.drawable.custom_progress_bar);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgressDrawable(draw);

        setupProgressBar();


    }

    private Calendar getWeekStartDate() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }

        return calendar;
    }

    private Calendar getWeekEndDate() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        calendar.add(Calendar.DATE, -1);
        return calendar;
    }

    private String getMonthDayString(Calendar calendar) {
        int monthNumber = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        return getMonthName(monthNumber) + " " + dayOfMonth;
    }

    private String getMonthName(int monthNumber) {
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

    private void initializeWeeklyData() {
        EditText dailyCal = (EditText) findViewById(R.id.dailyCal);
        dailyCalorieGoal = Integer.parseInt(dailyCal.getText().toString());
        weeklyCalorieGoal = 7 * dailyCalorieGoal;

        weeklyCalories[0] = Integer.parseInt(((EditText)findViewById(R.id.mondayCalories)).getText().toString());
        weeklyCalories[1] = Integer.parseInt(((EditText)findViewById(R.id.tuesdayCalories)).getText().toString());
        weeklyCalories[2] = Integer.parseInt(((EditText)findViewById(R.id.wednesdayCalories)).getText().toString());
        weeklyCalories[3] = Integer.parseInt(((EditText)findViewById(R.id.thursdayCalories)).getText().toString());
        weeklyCalories[4] = Integer.parseInt(((EditText)findViewById(R.id.fridayCalories)).getText().toString());
        weeklyCalories[5] = Integer.parseInt(((EditText)findViewById(R.id.saturdayCalories)).getText().toString());
        weeklyCalories[6] = Integer.parseInt(((EditText)findViewById(R.id.sundayCalories)).getText().toString());

        weeklyWeight[0] = Integer.parseInt(((EditText)findViewById(R.id.mondayWeight)).getText().toString());
        weeklyWeight[1] = Integer.parseInt(((EditText)findViewById(R.id.tuesdayWeight)).getText().toString());
        weeklyWeight[2] = Integer.parseInt(((EditText)findViewById(R.id.wednesdayWeight)).getText().toString());
        weeklyWeight[3] = Integer.parseInt(((EditText)findViewById(R.id.thursdayWeight)).getText().toString());
        weeklyWeight[4] = Integer.parseInt(((EditText)findViewById(R.id.fridayWeight)).getText().toString());
        weeklyWeight[5] = Integer.parseInt(((EditText)findViewById(R.id.saturdayWeight)).getText().toString());
        weeklyWeight[6] = Integer.parseInt(((EditText)findViewById(R.id.sundayWeight)).getText().toString());
    }

    private void setupProgressBar() {
        TextView progressBarProportion = (TextView) findViewById(R.id.progressBarProportion);
        int totalCalories = 0;
        for (int dailyCalories : weeklyCalories) {
            totalCalories += dailyCalories;
        }
        progressBarProportion.setText(totalCalories + " / " +  weeklyCalorieGoal);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(totalCalories);
        progressBar.setMax(weeklyCalorieGoal);
    }
}
