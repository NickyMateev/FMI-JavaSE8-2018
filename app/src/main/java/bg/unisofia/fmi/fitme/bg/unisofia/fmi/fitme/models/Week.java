package bg.unisofia.fmi.fitme.bg.unisofia.fmi.fitme.models;

import java.util.Date;

public class Week {

    private int _id;
    private Date _startDate;
    private Date _endDate;
    private int _dailyCalorieGoal;

    public Week() {
    }

    public Week(Date startDate, Date endDate, int dailyCalorieGoal) {
        this._startDate = startDate;
        this._endDate = endDate;
        this._dailyCalorieGoal = dailyCalorieGoal;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Date get_startDate() {
        return _startDate;
    }

    public void set_startDate(Date _startDate) {
        this._startDate = _startDate;
    }

    public Date get_endDate() {
        return _endDate;
    }

    public void set_endDate(Date _endDate) {
        this._endDate = _endDate;
    }

    public int get_dailyCalorieGoal() {
        return _dailyCalorieGoal;
    }

    public void set_dailyCalorieGoal(int _dailyCalorieGoal) {
        this._dailyCalorieGoal = _dailyCalorieGoal;
    }
}
