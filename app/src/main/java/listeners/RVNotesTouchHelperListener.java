package listeners;

import androidx.recyclerview.widget.RecyclerView;

public interface RVNotesTouchHelperListener {

    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int adapterPosition);

}
