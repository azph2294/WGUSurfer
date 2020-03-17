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

import bottomSheets.EditNoteBottomSheet;
import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.Notes;

import static Utilities.Constants.N_CREATEDATE;
import static Utilities.Constants.N_ID;
import static Utilities.Constants.N_LASTUPDATE;
import static Utilities.Constants.N_NOTE_ID;

public class NotesRVAdapter extends RecyclerView.Adapter<NotesRVAdapter.NotesViewHolder> {

    private static final String NOTE_RV = "NotesRVAdapter";

    private List<Notes> notesList;
    private Context mContext;
    private Notes note;

    private OnNoteClickListener noteClickListener;

    private Bundle args = new Bundle();

    public NotesRVAdapter(List<Notes> notesList, Context mContext, OnNoteClickListener noteClickListener) {
        this.notesList = notesList;
        this.mContext = mContext;
        this.noteClickListener = noteClickListener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_layout, parent, false);
        return new NotesViewHolder(itemView, noteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder notesViewHolder, int position) {
        note = notesList.get(position);
        notesViewHolder.last_upd_lbl.setText(R.string.lastUpdate_txt);
        notesViewHolder.last_upd.setText(String.valueOf(note.getLastUpdate()));
        notesViewHolder.noteName.setText(note.getCourseNote());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public Notes removeItem(int position) {
        if (position < getItemCount() && position >= 0) {
            note = notesList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, notesList.size());
            notifyDataSetChanged();
            return note;
        }
        Log.d(NOTE_RV, "remove note @ position: " + position);
        return null;
    }

    public void restoreItem(Notes note, int position) {
        notesList.add(position, note);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, notesList.size());
        notifyDataSetChanged();

        Log.d(NOTE_RV, "restore note @ position: " + position);
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        TextView noteName, last_upd, last_upd_lbl;
        public ConstraintLayout viewForeground;
        RelativeLayout viewBackground;
        OnNoteClickListener noteClickListener;

        NotesViewHolder(@NonNull View itemView, OnNoteClickListener noteClickListener) {
            super(itemView);

            last_upd_lbl = itemView.findViewById(R.id.lastUpdate_lbl);
            noteName = itemView.findViewById(R.id.note_Name);
            last_upd = itemView.findViewById(R.id.time_last_upd);
            viewForeground = itemView.findViewById(R.id.view_foreground5);
            viewBackground = itemView.findViewById(R.id.view_background5);
            this.noteClickListener = noteClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            noteClickListener.onNoteClick(getAdapterPosition(),
                    notesList.get(getAdapterPosition()).getN_id(),
                    notesList.get(getAdapterPosition()).getNote_Id(),
                    notesList.get(getAdapterPosition()).getCourseNote(),
                    notesList.get(getAdapterPosition()).getCreateDate(),
                    notesList.get(getAdapterPosition()).getLastUpdate());
            EditNoteBottomSheet editNoteBottomSheet =
                    new EditNoteBottomSheet();
            args.putInt(N_ID, note.getN_id());
            args.putInt(N_NOTE_ID, note.getNote_Id());
            args.putString(N_CREATEDATE, note.getCreateDate());
            args.putString(N_LASTUPDATE, note.getLastUpdate());
            editNoteBottomSheet.setArguments(args);
            if (editNoteBottomSheet.getFragmentManager() != null) {
                editNoteBottomSheet.getFragmentManager()
                        .beginTransaction().add(editNoteBottomSheet,
                        "EditNoteBottomSheet").commit();
            }
        }
    }

    public interface OnNoteClickListener {
        void onNoteClick(int position, int id, int note_Id, String courseNote,
                         String createDate, String lastUpdate);
    }

}
