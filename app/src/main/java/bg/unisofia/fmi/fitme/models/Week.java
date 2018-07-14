package bg.unisofia.fmi.fitme.models;

import com.orm.SugarRecord;

import java.util.List;

public class Week extends SugarRecord {

    private String startDate;
    private String endDate;
    private int dailyCalories;

    public Week() {
    }

    public Week(String startDate, String endDate, int dailyCalories) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyCalories = dailyCalories;
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

    public List<Day> getDays(){
        return Day.find(Day.class, "week = ?", String.valueOf(this.getId()));
    }

    @Override
    public String toString() {
        return "Week{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", dailyCalories=" + dailyCalories +
                '}';
    }
}
