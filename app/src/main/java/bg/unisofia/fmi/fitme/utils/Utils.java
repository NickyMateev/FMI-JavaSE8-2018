package bg.unisofia.fmi.fitme.utils;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import bg.unisofia.fmi.fitme.models.Day;

public class Utils {

    public static Calendar getCurrentWeekStartDate() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }

        Utils.nullifyTimeInDay(calendar);
        return calendar;
    }

    public static Calendar getWeekEndDate() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        calendar.add(Calendar.DATE, -1);
        return calendar;
    }


    public static String constructCurrentWeekLabel(Calendar startDate, Calendar endDate) {
        String weekStartDate = Utils.generateMonthAndDayString(startDate);
        String weekEndDate = Utils.generateMonthAndDayString(endDate);

        return weekStartDate + " - " + weekEndDate;
    }

    private static String generateMonthAndDayString(Calendar calendar) {
        int monthNumber = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        return getMonthName(monthNumber) + " " + dayOfMonth;
    }

    public static int calculateCalorieEntryTotal(List<Day> days) {
        int totalCalories = 0;
        for (Day day : days) {
            totalCalories += day.getCalories();
        }
        return totalCalories;
    }

    public static double calculateAverageWeight(List<Day> days) {
        double totalWeight = 0.0;
        int validEntries = 0;

        for (Day day : days) {
            double dailyWeight = day.getWeight();
            if (dailyWeight > 0) {
                totalWeight += dailyWeight;
                validEntries++;
            }
        }

        return validEntries > 0 ? (totalWeight / validEntries) : 0.0;
    }

    public static void nullifyTimeInDay(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    }

    public static String getMonthName(int monthNumber) {
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
}
