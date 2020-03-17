package listeners;

import androidx.recyclerview.widget.RecyclerView;

public interface RVCourseTouchHelperListener {

    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);

}
