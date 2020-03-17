package adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bottomSheets.EditObjAssessBottomSheet;
import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.ObjectiveAssessments;

import static Utilities.Constants.OBJ_ASSESS__RV;
import static Utilities.Constants.O_ID;
import static Utilities.Constants.O_OBJ_ID;
import static Utilities.Constants.O_STATUS_ID;

public class ObjAssessRVAdapter extends RecyclerView.Adapter<ObjAssessRVAdapter.ObjAssessViewHolder> {

    private static final String TEST = "ObjAssessRVAdapter";

    private List<ObjectiveAssessments> oAssessments;
    private ObjectiveAssessments objAssessment;
    private Context mContext;

    private OnObjAssessClickListener onObjAssessClickListener;
    private Bundle args = new Bundle();

    public ObjAssessRVAdapter(Context mContext, List<ObjectiveAssessments> oAssessments,
                              OnObjAssessClickListener onObjAssessClickListener) {
        this.oAssessments = oAssessments;
        this.mContext = mContext;
        this.onObjAssessClickListener = onObjAssessClickListener;
    }

    @NonNull
    @Override
    public ObjAssessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.obj_assess_layout,
                parent, false);
        return new ObjAssessViewHolder(itemView, onObjAssessClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ObjAssessViewHolder objAssessViewHolder, int position) {
        objAssessment = oAssessments.get(position);
        objAssessViewHolder.objAssessName.setText(objAssessment.getObjAssesName());
        objAssessViewHolder.objED.setText(objAssessment.getObj_due_date());

    }


    public ObjectiveAssessments removeItem(int position) {
        if (position < getItemCount() && position >= 0) {
            objAssessment = oAssessments.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, oAssessments.size());
            notifyDataSetChanged();
            return objAssessment;
        }
        Log.d(OBJ_ASSESS__RV, "remove obj. assessment @ position: " + position);
        return null;
    }

    public void restoreItem(ObjectiveAssessments objAssessment, int position) {
        oAssessments.add(position, objAssessment);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, oAssessments.size());
        notifyDataSetChanged();

        Log.d(OBJ_ASSESS__RV, "restore obj. assessment @ position: " + position);
    }

    @Override
    public int getItemCount() {
        return oAssessments.size();
    }

    public class ObjAssessViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        TextView objAssessName, objED;
        public ConstraintLayout viewForeground;
        RelativeLayout viewBackground;
        OnObjAssessClickListener objAssessClickListener;

        ObjAssessViewHolder(@NonNull View itemView, OnObjAssessClickListener
                objAssessClickListener) {
            super(itemView);

            objAssessName = itemView.findViewById(R.id.obj_assess_Name);
            objED = itemView.findViewById(R.id.obj_assess_ED);
            viewForeground = itemView.findViewById(R.id.view_foreground4);
            viewBackground = itemView.findViewById(R.id.view_background4);
            this.objAssessClickListener = objAssessClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            objAssessClickListener.onObjAssessClick(getAdapterPosition(),
                    oAssessments.get(getAdapterPosition()).getObj_Id(),
                    oAssessments.get(getAdapterPosition()).getId(),
                    oAssessments.get(getAdapterPosition()).getStatus_Id());
            EditObjAssessBottomSheet editObjAssessBottomSheet =
                    new EditObjAssessBottomSheet();
            args.putInt(O_OBJ_ID, objAssessment.getObj_Id());
            args.putInt(O_ID, objAssessment.getId());
            args.putInt(O_STATUS_ID, objAssessment.getStatus_Id());
            editObjAssessBottomSheet.setArguments(args);
            if (editObjAssessBottomSheet.getFragmentManager() != null) {
                editObjAssessBottomSheet.getFragmentManager()
                        .beginTransaction().add(editObjAssessBottomSheet,
                        "EditObjAssessBottomSheet").commit();
            }
        }
    }

    public interface OnObjAssessClickListener {
        void onObjAssessClick(int position, int obj_Id, int id, int status_Id);
    }

}
