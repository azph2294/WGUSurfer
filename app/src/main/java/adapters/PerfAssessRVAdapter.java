package adapters;

import android.content.Context;
import android.graphics.Color;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import bottomSheets.EditPerfAssessBottomSheet;
import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.PerformanceAssessments;

import static Utilities.Constants.PERF_ASSESS__RV;
import static Utilities.Constants.P_ID;
import static Utilities.Constants.P_PER_ID;
import static Utilities.Constants.P_STATUS_ID;
import static Utilities.TimeFiles.formatStringToDate;

public class PerfAssessRVAdapter extends
        RecyclerView.Adapter<PerfAssessRVAdapter.PerfAssessViewHolder> {

    private List<PerformanceAssessments> pAssessments;
    private PerformanceAssessments perfAssessment;
    private Context mContext;

    private OnPerfAssessClickListener onPerfAssessClickListener;
    private Bundle args = new Bundle();

    public PerfAssessRVAdapter(Context mContext, List<PerformanceAssessments> pAssessments,
                               OnPerfAssessClickListener onPerfAssessClickListener) {
        this.pAssessments = pAssessments;
        this.mContext = mContext;
        this.onPerfAssessClickListener = onPerfAssessClickListener;
    }

    @NonNull
    @Override
    public PerfAssessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.perf_assess_layout,
                parent, false);
        return new PerfAssessViewHolder(itemView, onPerfAssessClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PerfAssessViewHolder perfAssessViewHolder, int position) {
        perfAssessment = pAssessments.get(position);
        perfAssessViewHolder.perfAssessName.setText(perfAssessment.getPerAssesName());
        perfAssessViewHolder.perfED.setText(perfAssessment.getPer_due_date());

    }

    public PerformanceAssessments removeItem(int position) {
        if (position < getItemCount() && position >= 0) {
            perfAssessment = pAssessments.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, pAssessments.size());
            notifyDataSetChanged();
            return perfAssessment;
        }
        Log.d(PERF_ASSESS__RV, "remove perf. assessment @ position: " + position);
        return null;
    }

    public void restoreItem(PerformanceAssessments perfAssessment, int position) {
        pAssessments.add(position, perfAssessment);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, pAssessments.size());
        notifyDataSetChanged();

        Log.d(PERF_ASSESS__RV, "restore perf. assessment @ position: " + position);
    }

    @Override
    public int getItemCount() {
        return pAssessments.size();
    }

    public class PerfAssessViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        TextView perfAssessName, perfED;
        public ConstraintLayout viewForeground;
        RelativeLayout viewBackground;
        OnPerfAssessClickListener perfAssessClickListener;

        PerfAssessViewHolder(@NonNull View itemView,
                             OnPerfAssessClickListener perfAssessClickListener) {
            super(itemView);

            perfAssessName = itemView.findViewById(R.id.assess_Name);
            perfED = itemView.findViewById(R.id.assess_ED);
            viewForeground = itemView.findViewById(R.id.view_foreground3);
            viewBackground = itemView.findViewById(R.id.view_background3);
            this.perfAssessClickListener = perfAssessClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            perfAssessClickListener.onPerfAssessClick(getAdapterPosition(),
                    pAssessments.get(getAdapterPosition()).getPer_Id(),
                    pAssessments.get(getAdapterPosition()).getId(),
                    pAssessments.get(getAdapterPosition()).getStatus_Id());
            EditPerfAssessBottomSheet editPerfAssessBottomSheet =
                    new EditPerfAssessBottomSheet();
            args.putInt(P_PER_ID, perfAssessment.getPer_Id());
            args.putInt(P_ID, perfAssessment.getId());
            args.putInt(P_STATUS_ID, perfAssessment.getStatus_Id());
            editPerfAssessBottomSheet.setArguments(args);
            if (editPerfAssessBottomSheet.getFragmentManager() != null) {
                editPerfAssessBottomSheet.getFragmentManager()
                        .beginTransaction().add(editPerfAssessBottomSheet,
                        "EditPerfAssessBottomSheet").commit();
            }
        }

    }

    public interface OnPerfAssessClickListener {
        void onPerfAssessClick(int position, int per_Id, int id, int status_Id);
    }

}
