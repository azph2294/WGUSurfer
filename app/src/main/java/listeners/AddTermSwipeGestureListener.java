package listeners;

import android.view.GestureDetector;
import android.view.MotionEvent;

import activities.terms_courses.AllTermsActivity;

public class AddTermSwipeGestureListener extends GestureDetector.SimpleOnGestureListener {

    private static final int MIN_SWIPE_DISTANCE_Y = 100;
    private static final int MAX_SWIPE_DISTANCE_Y = 1000;

    private AllTermsActivity activity;

    public AddTermSwipeGestureListener(AllTermsActivity activity) {
        this.activity = activity;
    }

    private AllTermsActivity getActivity() {
        return activity;
    }

    public void setActivity(AllTermsActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float deltaY = e1.getY() - e2.getY();
        float deltaYAbs = Math.abs(deltaY);
        if (deltaYAbs >= MIN_SWIPE_DISTANCE_Y && deltaYAbs <= MAX_SWIPE_DISTANCE_Y) {
            if (deltaY > 0) {
                getActivity().openAddTermFragment();
            }
        }
        return true;
    }

}
