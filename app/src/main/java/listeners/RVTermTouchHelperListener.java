package listeners;

import androidx.recyclerview.widget.RecyclerView;

public interface RVTermTouchHelperListener{

    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);

}
