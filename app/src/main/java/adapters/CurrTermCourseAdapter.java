package adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import activities.terms_courses.CoursesActivity;
import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.Courses;

import static Utilities.Constants.COMPLETED;
import static Utilities.Constants.COURSE_ID;
import static Utilities.Constants.COURSE_RV_SELECTED_TERM;
import static Utilities.Constants.C_STATUS_ID;
import static Utilities.Constants.DROPPED;
import static Utilities.Constants.IN_PROGRESS;
import static Utilities.Constants.PLAN_TO_TAKE;
import static Utilities.Constants.TERM_ID;

public class CurrTermCourseAdapter extends RecyclerView.Adapter<CurrTermCourseAdapter.CourseViewHolder> {

    private List<Courses> coursesList;
    private Context mContext;

    private OnCourseClickListener onCourseClickListener;

    public CurrTermCourseAdapter(List<Courses> coursesList, Context mContext, OnCourseClickListener onCourseClickListener) {
        this.coursesList = coursesList;
        this.mContext = mContext;
        this.onCourseClickListener = onCourseClickListener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.curr_tcourses_layout, parent, false);
        return new CourseViewHolder(itemView, onCourseClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrTermCourseAdapter.CourseViewHolder courseViewHolder, int position) {
        Courses course = coursesList.get(position);
        courseViewHolder.courseName.setText(course.getCourseName());

        if (course.getStatusId() == IN_PROGRESS) {
            courseViewHolder.courseName.setTextColor(Color.GREEN);
        }
        if (course.getStatusId() == PLAN_TO_TAKE) {
            courseViewHolder.courseName.setTextColor(Color.YELLOW);
        }
        if (course.getStatusId() == DROPPED) {
            courseViewHolder.courseName.setTextColor(Color.RED);
        }
        if (course.getStatusId() == COMPLETED) {
            courseViewHolder.courseName.setTextColor(Color.CYAN);
        }
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    public Courses removeItem(int position) {
        if (position < getItemCount() && position >= 0) {
            Courses course = coursesList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, coursesList.size());
            notifyDataSetChanged();
            return course;
        }
        Log.d(COURSE_RV_SELECTED_TERM, "remove course @ position: " + position);
        return null;
    }

    public void restoreItem(Courses course, int position) {
        coursesList.add(position, course);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, coursesList.size());
        notifyDataSetChanged();

        Log.d(COURSE_RV_SELECTED_TERM, "restore course @ position: " + position);
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView courseImg;
        TextView courseName;
        public RelativeLayout viewForeground;
        RelativeLayout viewBackground;

        OnCourseClickListener courseClickListener;

        CourseViewHolder(@NonNull View itemView, OnCourseClickListener courseClickListener) {
            super(itemView);

            courseImg = itemView.findViewById(R.id.course_icon);
            courseName = itemView.findViewById(R.id.course_Name);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            viewBackground = itemView.findViewById(R.id.view_background);
            this.courseClickListener = courseClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            courseClickListener.onCourseClick(getAdapterPosition());
            Intent selectCourseIntent = new Intent(mContext, CoursesActivity.class);
            final Courses courses = coursesList.get(getAdapterPosition());
            selectCourseIntent.putExtra(TERM_ID, courses.getTermId());
            selectCourseIntent.putExtra(COURSE_ID, courses.getId());
            selectCourseIntent.putExtra(C_STATUS_ID, courses.getStatusId());
            mContext.startActivity(selectCourseIntent);
        }
    }

    public interface OnCourseClickListener {
        void onCourseClick(int position);
    }

}