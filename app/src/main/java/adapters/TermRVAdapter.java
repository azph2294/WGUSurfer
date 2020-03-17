package adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import activities.terms_courses.EditTermActivity;
import activities.terms_courses.SelectedTermActivity;
import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.Terms;
import viewmodels.TermViewModel;

import static Utilities.Constants.ACTIVE;
import static Utilities.Constants.ALL_TERMS_RV;
import static Utilities.Constants.IN_ACTIVE;
import static Utilities.Constants.TERM_ACTIVE;
import static Utilities.Constants.TERM_ID;
import static Utilities.Constants.T_TERMFLAG;

public class TermRVAdapter extends RecyclerView.Adapter<TermRVAdapter.TermViewHolder> {

    private static final String TEST = "TEST";

    private List<Terms> termsList;
    private Context mContext;
    private Terms term;

    private OnTermClickListener onTermClickListener;
    private OnTermLongClickListener onTermLongClickListener;

    public TermRVAdapter(List<Terms> termsList, Context mContext,
                         OnTermClickListener onTermClickListener,
                         OnTermLongClickListener onTermLongClickListener) {
        this.termsList = termsList;
        this.mContext = mContext;
        this.onTermClickListener = onTermClickListener;
        this.onTermLongClickListener = onTermLongClickListener;
    }

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.terms_layout, parent, false);
        return new TermViewHolder(itemView, onTermClickListener, onTermLongClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TermViewHolder termViewHolder, int position) {
        term = termsList.get(position);
        termViewHolder.termName.setText(term.getTermName());
        termViewHolder.termDates.setText(term.getStartDate() + " - " + term.getEndDate());
    }

    @Override
    public int getItemCount() {
        return termsList.size();
    }

    public Terms removeItem(int position) {
        if (position < getItemCount() && position >= 0) {
            term = termsList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, termsList.size());
            notifyDataSetChanged();
            return term;
        }
        Log.d(ALL_TERMS_RV, "remove term @ position: " + position);
        return null;
    }

    public void restoreItem(Terms term, int position) {
        termsList.add(position, term);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, termsList.size());
        notifyDataSetChanged();

        Log.d(ALL_TERMS_RV, "restore term @ position: " + position);
    }

    public class TermViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        ImageView termImg;
        TextView termName, termDates;
        public ConstraintLayout viewForeground;
        RelativeLayout viewBackground;
        OnTermClickListener termClickListener;
        OnTermLongClickListener termLongClickListener;

        TermViewHolder(@NonNull View itemView, OnTermClickListener termClickListener,
                       OnTermLongClickListener termLongClickListener) {
            super(itemView);

            termImg = itemView.findViewById(R.id.term_icon);
            termName = itemView.findViewById(R.id.term_name);
            termDates = itemView.findViewById(R.id.term_dates);
            viewForeground = itemView.findViewById(R.id.tView_foreground);
            viewBackground = itemView.findViewById(R.id.tView_background);
            this.termClickListener = termClickListener;
            this.termLongClickListener = termLongClickListener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            termClickListener.onTermClick(getAdapterPosition());
            Intent selectTermIntent = new Intent(mContext, SelectedTermActivity.class);
            final Terms terms = termsList.get(getAdapterPosition());
            Log.d(TEST, "Term Clicked: " + terms.getId());
            selectTermIntent.putExtra(TERM_ID, terms.getId());
            mContext.startActivity(selectTermIntent);
        }

        @Override
        public boolean onLongClick(View v) {
            termLongClickListener.onTermLongClick(getAdapterPosition());
            final Terms terms = termsList.get(getAdapterPosition());
            Intent editIntent = new Intent(mContext, EditTermActivity.class);
            editIntent.putExtra(TERM_ID, terms.getId());
            editIntent.putExtra(T_TERMFLAG, term.getTermFlag());
            mContext.startActivity(editIntent);
            return true;
        }
    }

    public interface OnTermClickListener {
        void onTermClick(int position);
    }

    public interface OnTermLongClickListener {
        void onTermLongClick(int position);
    }

}
