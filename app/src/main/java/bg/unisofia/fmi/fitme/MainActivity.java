package bg.unisofia.fmi.fitme;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bg.unisofia.fmi.fitme.bg.unisofia.fmi.fitme.models.Day;
import bg.unisofia.fmi.fitme.bg.unisofia.fmi.fitme.models.Week;
import bg.unisofia.fmi.fitme.bg.unisofia.fmi.fitme.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private static int DAYS_IN_WEEK = 7;

    private static Calendar currentFirstWeekDay;
    private static Calendar currentLastWeekDay;

    private static Week currentWeek;
    private static List<Day> currentWeekDays;

    EditText[] weightFields = new EditText[DAYS_IN_WEEK];
    EditText[] calorieFields = new EditText[DAYS_IN_WEEK];

    Toolbar toolbar;
    TextView currentWeekLabel, avgWeight, progressBarProportion;
    Button previousWeekBtn, nextWeekBtn;
    Button dailyCalorieGoalBtn;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeReferences();
        setSupportActionBar(toolbar);
        drawProgressBar();
        attachHandlers();

        currentFirstWeekDay = Utils.getCurrentWeekStartDate();
        currentLastWeekDay = (Calendar)currentFirstWeekDay.clone();
        currentLastWeekDay.add(Calendar.DAY_OF_WEEK, 6);
        setCurrentWeekLabel();

        initializeData();
    }


    private void initializeData() {
        List<Week> result = Week.find(Week.class, "start_date = ?", new String(currentFirstWeekDay.getTime().toString()));

        if (result == null || result.size() == 0) {
            currentWeek = new Week(currentFirstWeekDay.getTime().toString(), currentLastWeekDay.getTime().toString(), 0);
            currentWeek.save();
            currentWeekDays = new ArrayList<>();
            for(int i = 0; i < DAYS_IN_WEEK; i ++) {
                Day day = new Day(i, 0, 0, currentWeek);
                day.save();
                currentWeekDays.add(day);
            }
        } else {
            currentWeek = result.get(0);
            dailyCalorieGoalBtn.setText(String.valueOf(currentWeek.getDailyCalories()));
            currentWeekDays = currentWeek.getDays();
            for (Day day : currentWeekDays) {
                System.out.println(day);
                if (day.getWeight() > 0) {
                   weightFields[day.getDayOfWeek()].setText(String.valueOf(day.getWeight()));
                }
                if (day.getCalories() > 0) {
                    calorieFields[day.getDayOfWeek()].setText(String.valueOf(day.getCalories()));
                }
            }
        }

        avgWeight.setText(String.format("%.2f", Utils.calculateAverageWeight(currentWeekDays)));
        refreshProgressBar();
    }

    private void refreshProgressBar() {
        int totalCalories = Utils.calculateCalorieEntryTotal(currentWeekDays);
        int weeklyCalories = currentWeek.getDailyCalories() * DAYS_IN_WEEK;

        progressBarProportion.setText(totalCalories + " / " + weeklyCalories);
        progressBar.setProgress(totalCalories);
        progressBar.setMax(weeklyCalories);
    }

    private void initializeReferences() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        currentWeekLabel = (TextView) findViewById(R.id.weekText);

        previousWeekBtn = (Button) findViewById(R.id.previousWeekBtn);
        nextWeekBtn = (Button) findViewById(R.id.nextWeekBtn);

        avgWeight = (TextView) findViewById(R.id.avgWeight);
        dailyCalorieGoalBtn = (Button) findViewById(R.id.btnDailyCal);

        weightFields[0] = (EditText) findViewById(R.id.mondayWeight);
        weightFields[1] = (EditText) findViewById(R.id.tuesdayWeight);
        weightFields[2] = (EditText) findViewById(R.id.wednesdayWeight);
        weightFields[3] = (EditText) findViewById(R.id.thursdayWeight);
        weightFields[4] = (EditText) findViewById(R.id.fridayWeight);
        weightFields[5] = (EditText) findViewById(R.id.saturdayWeight);
        weightFields[6] = (EditText) findViewById(R.id.sundayWeight);

        calorieFields[0] = (EditText) findViewById(R.id.mondayCalories);
        calorieFields[1] = (EditText) findViewById(R.id.tuesdayCalories);
        calorieFields[2] = (EditText) findViewById(R.id.wednesdayCalories);
        calorieFields[3] = (EditText) findViewById(R.id.thursdayCalories);
        calorieFields[4] = (EditText) findViewById(R.id.fridayCalories);
        calorieFields[5] = (EditText) findViewById(R.id.saturdayCalories);
        calorieFields[6] = (EditText) findViewById(R.id.sundayCalories);


        progressBarProportion = (TextView) findViewById(R.id.progressBarProportion);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
                currentFirstWeekDay.add(Calendar.DAY_OF_WEEK, -DAYS_IN_WEEK);
                currentLastWeekDay.add(Calendar.DAY_OF_WEEK, -DAYS_IN_WEEK);
                setCurrentWeekLabel();
            }
        });

        nextWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFirstWeekDay.add(Calendar.DAY_OF_WEEK, DAYS_IN_WEEK);
                currentLastWeekDay.add(Calendar.DAY_OF_WEEK, DAYS_IN_WEEK);
                setCurrentWeekLabel();
            }
        });
    }

    private void attachWeightListeners() {

        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            final int j = i;
            weightFields[j].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    String weight = weightFields[j].getText().toString();
                    if (!weight.isEmpty()) {
                        Day day = currentWeekDays.get(j);
                        day.setWeight(Double.parseDouble(weight));
                        day.save();
                        avgWeight.setText(String.format("%.2f", Utils.calculateAverageWeight(currentWeekDays)));
                    }
                }
            });
        }
    }

    private void attachCalorieGoalListeners() {

        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            final int j = i;
            calorieFields[j].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    String calories = calorieFields[j].getText().toString();
                    if (!calories.isEmpty()) {
                        Day day = currentWeekDays.get(j);
                        day.setCalories(Integer.parseInt(calories));
                        day.save();
                        refreshProgressBar();
                    }
                }
            });
        }

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
                        Editable e = dailyCalInput.getText();
                        if (!e.toString().isEmpty()) {
                            Toast.makeText(MainActivity.this,
                                    R.string.success_update_msg,
                                    Toast.LENGTH_LONG).show();
                            int dailyCalories = Integer.parseInt(e.toString());
                            currentWeek.setDailyCalories(dailyCalories);
                            currentWeek.save();

                            dailyCalorieGoalBtn.setText(e);
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
    }

    private void setCurrentWeekLabel() {
        String currWeekLabel = Utils.constructCurrentWeekLabel(currentFirstWeekDay, currentLastWeekDay);
        currentWeekLabel.setText(currWeekLabel);
    }

    private void drawProgressBar() {
        Drawable draw = getResources().getDrawable(R.drawable.custom_progress_bar);
        progressBar.setProgressDrawable(draw);
    }
}
