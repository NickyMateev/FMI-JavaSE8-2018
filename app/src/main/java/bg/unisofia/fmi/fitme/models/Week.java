package bg.unisofia.fmi.fitme.models;

import com.orm.SugarRecord;

import java.util.List;

public class Week extends SugarRecord {

    private String startDate;
    private String endDate;
    private int dailyCalories;
    private int numberOfWorkouts;
    private int avgSteps;

    public Week() {
    }

    public Week(String startDate, String endDate, int dailyCalories, int numberOfWorkouts, int avgSteps) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyCalories = dailyCalories;
        this.numberOfWorkouts = numberOfWorkouts;
        this.avgSteps = avgSteps;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDailyCalories() {
        return dailyCalories;
    }

    public void setDailyCalories(int dailyCalories) {
        this.dailyCalories = dailyCalories;
    }

    public int getNumberOfWorkouts() {
        return numberOfWorkouts;
    }

    public void setNumberOfWorkouts(int numberOfWorkouts) {
        this.numberOfWorkouts = numberOfWorkouts;
    }

    public int getAvgSteps() {
        return avgSteps;
    }

    public void setAvgSteps(int avgSteps) {
        this.avgSteps = avgSteps;
    }

    public List<Day> getDays(){
        return Day.find(Day.class, "week = ?", String.valueOf(this.getId()));
    }

    @Override
    public String toString() {
        return "Week{" +
                "startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", dailyCalories=" + dailyCalories +
                ", numberOfWorkouts=" + numberOfWorkouts +
                ", avgSteps=" + avgSteps +
                '}';
    }
}
