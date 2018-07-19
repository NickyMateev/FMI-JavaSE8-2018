package bg.unisofia.fmi.fitme;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bg.unisofia.fmi.fitme.errors.WeekNotFoundException;
import bg.unisofia.fmi.fitme.gestures.SidewaySwipe;
import bg.unisofia.fmi.fitme.models.Day;
import bg.unisofia.fmi.fitme.models.Week;
import bg.unisofia.fmi.fitme.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private static int DAYS_IN_WEEK = 7;

    private static GestureDetectorCompat gestureDetector;

    private static Calendar currentFirstWeekDay;
    private static Calendar currentLastWeekDay;

    private static Week currentWeek;
    private static List<Day> currentWeekDays;

    private static EditText[] weightFields = new EditText[DAYS_IN_WEEK];
    private static EditText[] calorieFields = new EditText[DAYS_IN_WEEK];
    private static EditText avgStepsField;

    private static Toolbar toolbar;
    private static TextView currentWeekLabel, avgWeight, progressBarProportion, progressBarText;
    private static Button previousWeekBtn, nextWeekBtn;
    private static Button dailyCalorieGoalBtn;

    private static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bootstrapApplication();
    }

    private void bootstrapApplication() {
        initializeSidewayGestureDetector();
        initializeViewReferences();
        initializeToolbar();
        drawProgressBar();
        attachHandlers();
        initializeCurrentWeek();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
               break;
            case R.id.delete:
                onDeletePress();
                break;
            case R.id.settings:
                onSettingsPress();
                break;
            case R.id.exit:
                onExitPress();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSettingsPress() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void onDeletePress() {
        View mView = getLayoutInflater().inflate(R.layout.clear_week_popup, null);
        Button confirmClearUpBtn = (Button) mView.findViewById(R.id.confirmClearUp);
        Button cancelClearUpBtn = (Button) mView.findViewById(R.id.cancelClearUp);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        confirmClearUpBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 currentWeek.setDailyCalories(0);
                 for (Day day : currentWeekDays) {
                     day.setWeight(0);
                     day.setCalories(0);
                     day.save();
                     for (int i = 0; i < DAYS_IN_WEEK; i++) {
                         weightFields[i].setText("0");
                         calorieFields[i].setText("0");
                         refreshAvgWeightTextView();
                     }
                 }
                 currentWeek.save();
                 dailyCalorieGoalBtn.setText("0");
                 refreshProgressBar();

                 Toast.makeText(MainActivity.this,
                         R.string.success_week_clean_up_msg,
                         Toast.LENGTH_LONG).show();

                dialog.dismiss();
            }
        });

        cancelClearUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void onExitPress() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private static void refreshAvgWeightTextView() {
        avgWeight.setText(String.format("%.2f", Utils.calculateAverageWeight(currentWeekDays)));
    }

    private static void refreshProgressBar() {
        int totalCalories = Utils.calculateCalorieEntryTotal(currentWeekDays);
        int weeklyCalories = currentWeek.getDailyCalories() * DAYS_IN_WEEK;

        progressBarProportion.setText(totalCalories + " / " + weeklyCalories);
        progressBar.setMax(weeklyCalories);
        progressBar.setProgress(totalCalories);
        progressBarText.setText(weeklyCalories - totalCalories + " left");
    }

    private void initializeSidewayGestureDetector() {
        gestureDetector = new GestureDetectorCompat(this, new SidewaySwipe());
    }

    private void initializeViewReferences() {
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

        avgStepsField = (EditText) findViewById(R.id.avgSteps);

        progressBarProportion = (TextView) findViewById(R.id.progressBarProportion);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBarText = (TextView) findViewById(R.id.progressBarText);
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setLogo(R.mipmap.fitme_logo);
    }

    private void attachHandlers() {
        attachWeekNavigationListeners();
        attachWeightListeners();
        attachCalorieGoalListeners();
        attachAvgStepsListener();
    }

    private void attachAvgStepsListener() {
        avgStepsField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
               String avgStepsTextValue = avgStepsField.getText().toString();
               int steps = !avgStepsTextValue.isEmpty() ?  Integer.parseInt(avgStepsTextValue) : 0;
               currentWeek.setAvgSteps(steps);
               currentWeek.save();
            }
        });
    }

    private void attachWeekNavigationListeners() {
        previousWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPreviousWeek();
            }
        });

        nextWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNextWeek();
            }
        });
    }

    private void attachWeightListeners() {

        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            final int j = i;
            weightFields[j].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    String weightTextValue = weightFields[j].getText().toString();
                    double weight = !weightTextValue.isEmpty() ? Double.parseDouble(weightTextValue) : 0;
                    Day day = currentWeekDays.get(j);
                    day.setWeight(weight);
                    day.save();
                    refreshAvgWeightTextView();
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
                    String caloriesTextValue = calorieFields[j].getText().toString();
                    if (!caloriesTextValue.isEmpty()) {
                        Day day = currentWeekDays.get(j);
                        day.setCalories(Integer.parseInt(caloriesTextValue));
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

    private void drawProgressBar() {
        Drawable draw = getResources().getDrawable(R.drawable.custom_progress_bar);
        progressBar.setProgressDrawable(draw);
    }

    private void initializeCurrentWeek() {
        determineCurrentWeek();
        initializeWeek();
    }

    private static void initializeWeek() {
        setWeekTextView();
        loadWeekData();
        refreshAvgWeightTextView();
        refreshProgressBar();
    }

    private void determineCurrentWeek() {
        currentFirstWeekDay = Utils.getCurrentWeekStartDate();
        currentLastWeekDay = (Calendar)currentFirstWeekDay.clone();
        currentLastWeekDay.add(Calendar.DAY_OF_WEEK, 6);
    }

    private static void setWeekTextView() {
        String currWeekLabel = Utils.constructCurrentWeekLabel(currentFirstWeekDay, currentLastWeekDay);
        currentWeekLabel.setText(currWeekLabel);
    }

    private static void loadWeekData() {
        try {
            currentWeek = retrieveCurrentWeekFromDB();
            loadRetrievedWeekData();
        } catch (WeekNotFoundException e) {
            currentWeek = new Week(currentFirstWeekDay.getTime().toString(), currentLastWeekDay.getTime().toString(), 0, 0);
            currentWeek.save();
            loadNewlyCreatedWeek();
        }
    }

    private static Week retrieveCurrentWeekFromDB() throws WeekNotFoundException {
        List<Week> result = Week.find(Week.class, "start_date = ?", new String(currentFirstWeekDay.getTime().toString()));

        if (result.isEmpty()) {
            throw new WeekNotFoundException("Unable to retrieve week");
        }

        return result.get(0);
    }

    private static void loadRetrievedWeekData() {
        dailyCalorieGoalBtn.setText(String.valueOf(currentWeek.getDailyCalories()));
        currentWeekDays = currentWeek.getDays();
        for (Day day : currentWeekDays) {
            weightFields[day.getDayOfWeek()].setText(day.getWeight() > 0 ? String.valueOf(day.getWeight()) : "");
            calorieFields[day.getDayOfWeek()].setText(day.getCalories() > 0 ? String.valueOf(day.getCalories()) : "");
        }
        avgStepsField.setText(String.valueOf(currentWeek.getAvgSteps()));
    }

    private static void loadNewlyCreatedWeek() {
        currentWeekDays = new ArrayList<>();
        for(int i = 0; i < DAYS_IN_WEEK; i ++) {
            Day day = new Day(i, 0, 0, currentWeek);
            day.save();
            currentWeekDays.add(day);
        }
    }

    public static void loadPreviousWeek() {
        changeCurrentWeek(-1);
        initializeWeek();
    }

    public static void loadNextWeek() {
        changeCurrentWeek(1);
        initializeWeek();
    }

    private static void changeCurrentWeek(int weeksToAdd) {
        currentFirstWeekDay.add(Calendar.DAY_OF_WEEK, weeksToAdd * DAYS_IN_WEEK);
        currentLastWeekDay.add(Calendar.DAY_OF_WEEK, weeksToAdd * DAYS_IN_WEEK);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
