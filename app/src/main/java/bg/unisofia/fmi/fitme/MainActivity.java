package bg.unisofia.fmi.fitme;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
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
    private static double[] weeklyWeight = new double[7];

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
        attachHandlers();

        // Progress bar setup:
        Drawable draw= getResources().getDrawable(R.drawable.custom_progress_bar);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgressDrawable(draw);

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

        weeklyWeight[0] = Double.parseDouble(((EditText)findViewById(R.id.mondayWeight)).getText().toString());
        weeklyWeight[1] = Double.parseDouble(((EditText)findViewById(R.id.tuesdayWeight)).getText().toString());
        weeklyWeight[2] = Double.parseDouble(((EditText)findViewById(R.id.wednesdayWeight)).getText().toString());
        weeklyWeight[3] = Double.parseDouble(((EditText)findViewById(R.id.thursdayWeight)).getText().toString());
        weeklyWeight[4] = Double.parseDouble(((EditText)findViewById(R.id.fridayWeight)).getText().toString());
        weeklyWeight[5] = Double.parseDouble(((EditText)findViewById(R.id.saturdayWeight)).getText().toString());
        weeklyWeight[6] = Double.parseDouble(((EditText)findViewById(R.id.sundayWeight)).getText().toString());

        setupAverageWeight();
        setupProgressBar();
    }

    private void attachHandlers() {
        attachWeekNavigationListeners();
        attachWeightListeners();
        attachCalorieListeners();
    }

    private void attachWeekNavigationListeners() {
        ImageView previousWeekBtn = (ImageView) findViewById(R.id.previousWeekBtn);
        previousWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO:
            }
        });
    }

    private void attachWeightListeners() {
        final EditText mondayWeightText = (EditText)findViewById(R.id.mondayWeight);
        mondayWeightText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weeklyWeight[0] = Double.parseDouble(mondayWeightText.getText().toString());
                setupAverageWeight();
            }
        });

        final EditText tuesdayWeightText = (EditText)findViewById(R.id.tuesdayWeight);
        tuesdayWeightText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weeklyWeight[1] = Double.parseDouble(tuesdayWeightText.getText().toString());
                setupAverageWeight();
            }
        });

        final EditText wednesdayWeightText = (EditText)findViewById(R.id.wednesdayWeight);
        wednesdayWeightText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weeklyWeight[2] = Double.parseDouble(wednesdayWeightText.getText().toString());
                setupAverageWeight();
            }
        });

        final EditText thursdayWeightText = (EditText)findViewById(R.id.thursdayWeight);
        thursdayWeightText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weeklyWeight[3] = Double.parseDouble(thursdayWeightText.getText().toString());
                setupAverageWeight();
            }
        });

        final EditText fridayWeightText = (EditText)findViewById(R.id.fridayWeight);
        fridayWeightText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weeklyWeight[4] = Double.parseDouble(fridayWeightText.getText().toString());
                setupAverageWeight();
            }
        });

        final EditText saturdayCalorieText = (EditText)findViewById(R.id.saturdayWeight);
        saturdayCalorieText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weeklyWeight[5] = Double.parseDouble(saturdayCalorieText.getText().toString());
                setupAverageWeight();
            }
        });

        final EditText sundayWeightText = (EditText)findViewById(R.id.sundayWeight);
        sundayWeightText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weeklyWeight[6] = Double.parseDouble(sundayWeightText.getText().toString());
                setupAverageWeight();
            }
        });
    }

    private void attachCalorieListeners() {
        EditText dailyCal = (EditText) findViewById(R.id.dailyCal);
        dailyCal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                refreshCalorieGoals();
                setupProgressBar();
            }
        });

        final EditText mondayCalorieText = (EditText)findViewById(R.id.mondayCalories);
        mondayCalorieText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weeklyCalories[0] = Integer.parseInt(mondayCalorieText.getText().toString());
                setupProgressBar();
            }
        });

        final EditText tuesdayCalorieText = (EditText)findViewById(R.id.tuesdayCalories);
        tuesdayCalorieText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weeklyCalories[1] = Integer.parseInt(tuesdayCalorieText.getText().toString());
                setupProgressBar();
            }
        });

        final EditText wednesdayCalorieText = (EditText)findViewById(R.id.wednesdayCalories);
        wednesdayCalorieText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weeklyCalories[2] = Integer.parseInt(wednesdayCalorieText.getText().toString());
                setupProgressBar();
            }
        });

        final EditText thursdayCalorieText = (EditText)findViewById(R.id.thursdayCalories);
        thursdayCalorieText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weeklyCalories[3] = Integer.parseInt(thursdayCalorieText.getText().toString());
                setupProgressBar();
            }
        });

        final EditText fridayCalorieText = (EditText)findViewById(R.id.fridayCalories);
        fridayCalorieText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weeklyCalories[4] = Integer.parseInt(fridayCalorieText.getText().toString());
                setupProgressBar();
            }
        });

        final EditText saturdayCalorieText = (EditText)findViewById(R.id.saturdayCalories);
        saturdayCalorieText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weeklyCalories[5] = Integer.parseInt(saturdayCalorieText.getText().toString());
                setupProgressBar();
            }
        });

        final EditText sundayCalorieText = (EditText)findViewById(R.id.sundayCalories);
        sundayCalorieText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weeklyCalories[6] = Integer.parseInt(sundayCalorieText.getText().toString());
                setupProgressBar();
            }
        });
    }

    private void refreshCalorieGoals() {
        EditText dailyCal = (EditText) findViewById(R.id.dailyCal);
        dailyCalorieGoal = Integer.parseInt(dailyCal.getText().toString());
        weeklyCalorieGoal = 7 * dailyCalorieGoal;
    }

    private void setupAverageWeight() {
        double totalWeight = 0.0;
        int validEntries = 0;
        for (double dailyWeight : weeklyWeight) {
            if (dailyWeight > 0) {
                totalWeight += dailyWeight;
                validEntries++;
            }
        }

        double averageWeight = totalWeight != 0 ? (totalWeight / validEntries) : 0.0;
        TextView avgWeight = (TextView) findViewById(R.id.avgWeight);
        avgWeight.setText(String.format("%.2f", averageWeight));
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
