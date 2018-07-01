package bg.unisofia.fmi.fitme;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

import bg.unisofia.fmi.fitme.bg.unisofia.fmi.fitme.models.Week;
import bg.unisofia.fmi.fitme.bg.unisofia.fmi.fitme.persistency.MyDBHandler;

public class MainActivity extends AppCompatActivity {

    private static int dailyCalorieGoal;
    private static int weeklyCalorieGoal;

    private static int[] calorieEntries = new int[7];
    private static double[] weightEntries = new double[7];

    private static MyDBHandler db;

    private static TextView currentWeekLabel, avgWeight, progressBarProportion;
    Button previousWeekBtn, nextWeekBtn;
    Button dailyCalorieGoalBtn;
    EditText mondayWeight, tuesdayWeight, wednesdayWeight, thursdayWeight, fridayWeight, saturdayWeight, sundayWeight;
    EditText mondayCalories, tuesdayCalories, wednesdayCalories, thursdayCalories, fridayCalories, saturdayCalories, sundayCalories;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new MyDBHandler(this, null, null, 1);
        initializeReferences();

        // Current week setup:
        String weekStartDate = getMonthDayString(getWeekStartDate());
        String weekEndDate = getMonthDayString(getWeekEndDate());
        currentWeekLabel.setText(weekStartDate + " - " + weekEndDate);

        dailyCalorieGoal = db.getDailyCaloriesForWeek(getWeekStartDate().getTime(), getWeekEndDate().getTime());
        initializeWeeklyData();
        attachHandlers();

        // Progress bar setup:
        Drawable draw = getResources().getDrawable(R.drawable.custom_progress_bar);
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
        dailyCalorieGoalBtn.setText(String.valueOf(dailyCalorieGoal));

        calorieEntries[0] = Integer.parseInt(mondayCalories.getText().toString());
        calorieEntries[1] = Integer.parseInt(tuesdayCalories.getText().toString());
        calorieEntries[2] = Integer.parseInt(wednesdayCalories.getText().toString());
        calorieEntries[3] = Integer.parseInt(thursdayCalories.getText().toString());
        calorieEntries[4] = Integer.parseInt(fridayCalories.getText().toString());
        calorieEntries[5] = Integer.parseInt(saturdayCalories.getText().toString());
        calorieEntries[6] = Integer.parseInt(sundayWeight.getText().toString());

        weightEntries[0] = Double.parseDouble(mondayWeight.getText().toString());
        weightEntries[1] = Double.parseDouble(tuesdayWeight.getText().toString());
        weightEntries[2] = Double.parseDouble(wednesdayWeight.getText().toString());
        weightEntries[3] = Double.parseDouble(thursdayWeight.getText().toString());
        weightEntries[4] = Double.parseDouble(fridayWeight.getText().toString());
        weightEntries[5] = Double.parseDouble(saturdayWeight.getText().toString());
        weightEntries[6] = Double.parseDouble(sundayWeight.getText().toString());

        avgWeight.setText(String.format("%.2f", calculateAverageWeight()));

        weeklyCalorieGoal = 7 * dailyCalorieGoal;
        refreshProgressBar();
    }

    private void attachHandlers() {
        attachWeekNavigationListeners();
        attachWeightListeners();
        attachCalorieGoalListeners();
    }

    private void attachWeekNavigationListeners() {
        previousWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO:
            }
        });
    }

    private void attachWeightListeners() {
        mondayWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weightEntries[0] = Double.parseDouble(mondayWeight.getText().toString());
                avgWeight.setText(String.format("%.2f", calculateAverageWeight()));
            }
        });

        tuesdayWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weightEntries[1] = Double.parseDouble(tuesdayWeight.getText().toString());
                avgWeight.setText(String.format("%.2f", calculateAverageWeight()));
            }
        });

        wednesdayWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weightEntries[2] = Double.parseDouble(wednesdayWeight.getText().toString());
                avgWeight.setText(String.format("%.2f", calculateAverageWeight()));
            }
        });

        thursdayWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weightEntries[3] = Double.parseDouble(thursdayWeight.getText().toString());
                avgWeight.setText(String.format("%.2f", calculateAverageWeight()));
            }
        });

        fridayWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weightEntries[4] = Double.parseDouble(fridayWeight.getText().toString());
                avgWeight.setText(String.format("%.2f", calculateAverageWeight()));
            }
        });

        saturdayWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weightEntries[5] = Double.parseDouble(saturdayWeight.getText().toString());
                avgWeight.setText(String.format("%.2f", calculateAverageWeight()));
            }
        });

        sundayWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weightEntries[6] = Double.parseDouble(sundayWeight.getText().toString());
                avgWeight.setText(String.format("%.2f", calculateAverageWeight()));
            }
        });
    }

    private void attachCalorieGoalListeners() {
        dailyCalorieGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View mView = getLayoutInflater().inflate(R.layout.daily_popup, null);
                final EditText dailyCalInput = (EditText) mView.findViewById(R.id.dailyCalInput);
                Button btnUpdate = (Button) mView.findViewById(R.id.btnUpdateGoal);

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dailyCalInput.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this,
                                    R.string.success_update_msg,
                                    Toast.LENGTH_LONG).show();
                            dailyCalorieGoalBtn.setText(dailyCalInput.getText());
                            updateGoalCaloriesInDB(Integer.parseInt(dailyCalInput.getText().toString()));
                            refreshCalorieGoalData();
                            refreshProgressBar();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this,
                                    R.string.error_update_msg,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialog.show();
            }
        });

        mondayCalories.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                calorieEntries[0] = Integer.parseInt(mondayCalories.getText().toString());
                refreshProgressBar();
            }
        });

        tuesdayCalories.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                calorieEntries[1] = Integer.parseInt(tuesdayCalories.getText().toString());
                refreshProgressBar();
            }
        });

        wednesdayCalories.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                calorieEntries[2] = Integer.parseInt(wednesdayCalories.getText().toString());
                refreshProgressBar();
            }
        });

        thursdayCalories.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                calorieEntries[3] = Integer.parseInt(thursdayCalories.getText().toString());
                refreshProgressBar();
            }
        });

        fridayCalories.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                calorieEntries[4] = Integer.parseInt(fridayCalories.getText().toString());
                refreshProgressBar();
            }
        });

        saturdayCalories.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                calorieEntries[5] = Integer.parseInt(saturdayCalories.getText().toString());
                refreshProgressBar();
            }
        });

        sundayCalories.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                calorieEntries[6] = Integer.parseInt(sundayCalories.getText().toString());
                refreshProgressBar();
            }
        });
    }

    private void updateGoalCaloriesInDB(int dailyCalorieGoal) {
        db.updateDailyCaloriesGoalForWeek(new Week(getWeekStartDate().getTime(), getWeekEndDate().getTime(), dailyCalorieGoal));
    }

    private void refreshCalorieGoalData() {
        dailyCalorieGoal = Integer.parseInt(dailyCalorieGoalBtn.getText().toString());
        weeklyCalorieGoal = 7 * dailyCalorieGoal;
    }

    private void refreshProgressBar() {
        int totalCalories = calculateCalorieEntryTotal();
        progressBarProportion.setText(totalCalories + " / " +  weeklyCalorieGoal);
        progressBar.setProgress(totalCalories);
        progressBar.setMax(weeklyCalorieGoal);
    }

    private int calculateCalorieEntryTotal() {
        int totalCalories = 0;
        for (int dailyCalories : calorieEntries) {
            totalCalories += dailyCalories;
        }
        return totalCalories;
    }

    private double calculateAverageWeight() {
        double totalWeight = 0.0;
        int validEntries = 0;
        for (double dailyWeight : weightEntries) {
            if (dailyWeight > 0) {
                totalWeight += dailyWeight;
                validEntries++;
            }
        }

        return validEntries > 0 ? (totalWeight / validEntries) : 0.0;
    }

    private void initializeReferences() {
        currentWeekLabel = (TextView) findViewById(R.id.weekText);

        previousWeekBtn = (Button) findViewById(R.id.previousWeekBtn);
        nextWeekBtn = (Button) findViewById(R.id.nextWeekBtn);

        avgWeight = (TextView) findViewById(R.id.avgWeight);
        dailyCalorieGoalBtn = (Button) findViewById(R.id.btnDailyCal);

        mondayCalories = (EditText) findViewById(R.id.mondayCalories);
        tuesdayCalories = (EditText) findViewById(R.id.tuesdayCalories);
        wednesdayCalories = (EditText) findViewById(R.id.wednesdayCalories);
        thursdayCalories = (EditText) findViewById(R.id.thursdayCalories);
        fridayCalories = (EditText) findViewById(R.id.fridayCalories);
        saturdayCalories = (EditText) findViewById(R.id.saturdayCalories);
        sundayCalories = (EditText) findViewById(R.id.sundayCalories);

        mondayWeight = (EditText) findViewById(R.id.mondayWeight);
        tuesdayWeight = (EditText) findViewById(R.id.tuesdayWeight);
        wednesdayWeight = (EditText) findViewById(R.id.wednesdayWeight);
        thursdayWeight = (EditText) findViewById(R.id.thursdayWeight);
        fridayWeight = (EditText) findViewById(R.id.fridayWeight);
        saturdayWeight = (EditText) findViewById(R.id.saturdayWeight);
        sundayWeight = (EditText) findViewById(R.id.sundayWeight);

        progressBarProportion = (TextView) findViewById(R.id.progressBarProportion);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }
}
