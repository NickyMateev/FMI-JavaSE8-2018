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

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import bg.unisofia.fmi.fitme.bg.unisofia.fmi.fitme.models.Day;
import bg.unisofia.fmi.fitme.bg.unisofia.fmi.fitme.models.Week;
import bg.unisofia.fmi.fitme.bg.unisofia.fmi.fitme.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private static Calendar currentFirstWeekDay;
    private static Calendar currentLastWeekDay;

    private static Week currentWeek;

    private static int dailyCalorieGoal;
    private static int weeklyCalorieGoal;

    private static int[] calorieEntries = new int[7];
    private static double[] weightEntries = new double[7];


    Toolbar toolbar;
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

        initializeReferences();
        setSupportActionBar(toolbar);
        drawProgressBar();
        attachHandlers();

        dailyCalorieGoalBtn.setText("123");
        currentFirstWeekDay = Utils.getCurrentWeekStartDate();
        currentLastWeekDay = (Calendar)currentFirstWeekDay.clone();
        currentLastWeekDay.add(Calendar.DAY_OF_WEEK, 6);
        setCurrentWeekLabel();

        initializeData();
        initializeWeeklyData();
    }


    private void initializeData() {
        List<Week> result = Week.find(Week.class, "start_date = ?", new String(currentFirstWeekDay.getTime().toString()));

        if (result == null || result.size() == 0) {
            currentWeek = new Week(currentFirstWeekDay.getTime().toString(), currentLastWeekDay.getTime().toString(), 0);
            currentWeek.save();
        } else {
            currentWeek = result.get(0);
            dailyCalorieGoalBtn.setText(String.valueOf(currentWeek.getDailyCalories()));
            List<Day> weekDays = currentWeek.getDays();
            for (Day day : weekDays) {
                weightEntries[day.getDayOfWeek() - 1] = day.getWeight();
                calorieEntries[day.getDayOfWeek() - 1] = day.getCalories();
            }
        }

        Iterator<Week> weeks = Week.findAll(Week.class);
        while(weeks.hasNext()) {
            System.out.println(weeks.next());
        }

        System.out.println(">>> Weeks: " + Week.count(Week.class));
        System.out.println(">>> Days: " + Day.count( Day.class));
    }

    private void refreshCalorieGoalData() {
        dailyCalorieGoal = Integer.parseInt(dailyCalorieGoalBtn.getText().toString());
        weeklyCalorieGoal = 7 * dailyCalorieGoal;
    }

    private void refreshProgressBar() {
        int totalCalories = Utils.calculateCalorieEntryTotal(calorieEntries);
        progressBarProportion.setText(totalCalories + " / " +  weeklyCalorieGoal);
        progressBar.setProgress(totalCalories);
        progressBar.setMax(weeklyCalorieGoal);
    }

    private void initializeReferences() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

    private void initializeWeeklyData() {
        /* dailyCalorieGoalBtn.setText(String.valueOf(dailyCalorieGoal));

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
        */

        avgWeight.setText(String.format("%.2f", Utils.calculateAverageWeight(weightEntries)));

        weeklyCalorieGoal = 7 * currentWeek.getDailyCalories();
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
                currentFirstWeekDay.add(Calendar.DAY_OF_WEEK, -7);
                currentLastWeekDay.add(Calendar.DAY_OF_WEEK, -7);
                setCurrentWeekLabel();
            }
        });

        nextWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFirstWeekDay.add(Calendar.DAY_OF_WEEK, 7);
                currentLastWeekDay.add(Calendar.DAY_OF_WEEK, 7);
                setCurrentWeekLabel();
            }
        });
    }

    private void attachWeightListeners() {
        mondayWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weightEntries[0] = Double.parseDouble(mondayWeight.getText().toString());
                avgWeight.setText(String.format("%.2f", Utils.calculateAverageWeight(weightEntries)));
            }
        });

        tuesdayWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weightEntries[1] = Double.parseDouble(tuesdayWeight.getText().toString());
                avgWeight.setText(String.format("%.2f", Utils.calculateAverageWeight(weightEntries)));
            }
        });

        wednesdayWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weightEntries[2] = Double.parseDouble(wednesdayWeight.getText().toString());
                avgWeight.setText(String.format("%.2f", Utils.calculateAverageWeight(weightEntries)));
            }
        });

        thursdayWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weightEntries[3] = Double.parseDouble(thursdayWeight.getText().toString());
                avgWeight.setText(String.format("%.2f", Utils.calculateAverageWeight(weightEntries)));
            }
        });

        fridayWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weightEntries[4] = Double.parseDouble(fridayWeight.getText().toString());
                avgWeight.setText(String.format("%.2f", Utils.calculateAverageWeight(weightEntries)));
            }
        });

        saturdayWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weightEntries[5] = Double.parseDouble(saturdayWeight.getText().toString());
                avgWeight.setText(String.format("%.2f", Utils.calculateAverageWeight(weightEntries)));
            }
        });

        sundayWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                weightEntries[6] = Double.parseDouble(sundayWeight.getText().toString());
                avgWeight.setText(String.format("%.2f", Utils.calculateAverageWeight(weightEntries)));
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
                        Editable e = dailyCalInput.getText();
                        if (!e.toString().isEmpty()) {
                            Toast.makeText(MainActivity.this,
                                    R.string.success_update_msg,
                                    Toast.LENGTH_LONG).show();
                            int dailyCalories = Integer.parseInt(e.toString());
                            currentWeek.setDailyCalories(dailyCalories);
                            currentWeek.save();

                            System.out.println(Week.findById(Week.class, 1));

                            dailyCalorieGoalBtn.setText(e);
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

    private void setCurrentWeekLabel() {
        String currWeekLabel = Utils.constructCurrentWeekLabel(currentFirstWeekDay, currentLastWeekDay);
        currentWeekLabel.setText(currWeekLabel);
    }

    private void drawProgressBar() {
        Drawable draw = getResources().getDrawable(R.drawable.custom_progress_bar);
        progressBar.setProgressDrawable(draw);
    }
}
