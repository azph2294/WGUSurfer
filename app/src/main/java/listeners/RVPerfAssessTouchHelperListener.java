package listeners;

import androidx.recyclerview.widget.RecyclerView;

public interface RVPerfAssessTouchHelperListener {

    void onSwipedPerf(RecyclerView.ViewHolder perfViewHolder, int direction, int position);

}
