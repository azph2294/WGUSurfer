package listeners;

import androidx.recyclerview.widget.RecyclerView;

public interface RVObjAssessTouchHelperListener {

    void onSwipedObj(RecyclerView.ViewHolder objViewHolder, int direction, int position);

}
