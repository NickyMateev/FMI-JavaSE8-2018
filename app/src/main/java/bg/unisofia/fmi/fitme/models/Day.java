package bg.unisofia.fmi.fitme.models;

import com.orm.SugarRecord;

public class Day extends SugarRecord {

    private int dayOfWeek;
    private double weight;
    private int calories;

    private Week week;

    public Day() {
    }

    public Day(int dayOfWeek, double weight, int calories, Week week) {
       this.dayOfWeek = dayOfWeek;
       this.weight = weight;
       this.calories = calories;
       this.week = week;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public Week getWeek() {
        return week;
    }

    public void setWeek(Week week) {
        this.week = week;
    }

    @Override
    public String toString() {
        return "Day{" +
                "dayOfWeek=" + dayOfWeek +
                ", weight=" + weight +
                ", calories=" + calories +
                ", week=" + week +
                '}';
    }
}
