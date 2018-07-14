package bg.unisofia.fmi.fitme.gestures;

import android.view.GestureDetector;
import android.view.MotionEvent;

import bg.unisofia.fmi.fitme.MainActivity;

public class SidewaySwipe extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        if (event2.getX() > event1.getX()) {
            MainActivity.loadPreviousWeek();
        } else if (event2.getX() < event1.getX()) {
            MainActivity.loadNextWeek();
        }

        return true;
    }
}
