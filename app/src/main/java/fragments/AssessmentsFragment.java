package fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapters.ObjAssessRVAdapter;
import adapters.PerfAssessRVAdapter;
import bottomSheets.AddObjAssessBottomSheet;
import bottomSheets.AddPerfAssessBottomSheet;
import bottomSheets.EditObjAssessBottomSheet;
import bottomSheets.EditPerfAssessBottomSheet;
import dev.alvatech.c196_wgusurfer_ericbsmith.R;
import entity.ObjectiveAssessments;
import entity.PerformanceAssessments;
import entity.Terms;
import helpers.RVObjAssessTouchHelper;
import helpers.RVPerfAssessTouchHelper;
import listeners.RVObjAssessTouchHelperListener;
import listeners.RVPerfAssessTouchHelperListener;
import viewmodels.CourseViewModel;
import viewmodels.ObjAssessmentsVM;
import viewmodels.PerfAssessmentsVM;
import viewmodels.TermViewModel;

import static Utilities.Constants.ASSESSMENTS_FRAGMENT;
import static Utilities.Constants.COURSEACTIVITY_IN_ASSESS;
import static Utilities.Constants.COURSE_ID;
import static Utilities.Constants.O_ID;
import static Utilities.Constants.O_OBJ_ID;
import static Utilities.Constants.O_STATUS_ID;
import static Utilities.Constants.P_ID;
import static Utilities.Constants.P_PER_ID;
import static Utilities.Constants.P_STATUS_ID;

public class AssessmentsFragment extends Fragment implements
        RVPerfAssessTouchHelperListener, RVObjAssessTouchHelperListener,
        PerfAssessRVAdapter.OnPerfAssessClickListener,
        ObjAssessRVAdapter.OnObjAssessClickListener {

    private static final String TEST = "ASSESSMENTS_FRAG";

    private RecyclerView objRV, perfRV;
    private PerfAssessRVAdapter perfRVAdapter;
    private ObjAssessRVAdapter objRVAdapter;

    private TextView courseHeaderLbl;

    private PerfAssessmentsVM perfAssessmentsVM;
    private ObjAssessmentsVM objAssessmentsVM;

    private List<PerformanceAssessments> perfList = new ArrayList<>();
    private List<ObjectiveAssessments> objList = new ArrayList<>();

    private List<Terms> termsList = new ArrayList<>();

    private int vmPer_Id, vmObj_Id;

    private Bundle args = new Bundle();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.assessments_fragment, container, false);

        courseHeaderLbl = view.findViewById(R.id.assess_course_Header_lbl);
        ImageButton perfAssessBtn = view.findViewById(R.id.perf_add_btn);
        ImageButton objAssessBtn = view.findViewById(R.id.obj_add_btn);
        ImageButton infoBtn = view.findViewById(R.id.info_btn2);

        infoBtn.setOnClickListener(v -> infoAlert());

        perfRV = view.findViewById(R.id.perf_asses_list);
        objRV = view.findViewById(R.id.obj_asses_list);

        perfAssessBtn.setOnClickListener(v -> {
            AddPerfAssessBottomSheet addPABottomSheet = new
                    AddPerfAssessBottomSheet();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            addPABottomSheet.setArguments(args);
            addPABottomSheet.show(getActivity().getSupportFragmentManager(),
                    "AddPABottomSheet");
            Log.d(TEST, "ARGS: " + args.toString());
        });
        objAssessBtn.setOnClickListener(v -> {
            AddObjAssessBottomSheet addOABottomSheet = new
                    AddObjAssessBottomSheet();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            addOABottomSheet.setArguments(args);
            addOABottomSheet.show(getActivity().getSupportFragmentManager(),
                    "AddOABottomSheet");
        });
        initTermViewModel();

        initPerfAssessmentRV();
        initPerfAssessVM();
        initObjAssessmentsRV();
        initObjAssessVM();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(COURSEACTIVITY_IN_ASSESS, "onViewCreated: AssessmentsFragment was created..");
        initCourseHeader();
    }

    private void initPerfAssessmentRV() {
        Log.d(ASSESSMENTS_FRAGMENT, "initPerfAssessmentRV: init perf. assessments recyclerview");

        perfRV.setHasFixedSize(true);
        perfRVAdapter = new PerfAssessRVAdapter(getContext(), perfList,
                this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        perfRV.setLayoutManager(layoutManager);
        perfRV.setItemAnimator(new DefaultItemAnimator());
        perfRV.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()),
                DividerItemDecoration.VERTICAL));
        ItemTouchHelper.SimpleCallback simpleCallback = new RVPerfAssessTouchHelper(0,
                ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(perfRV);
        perfRV.setAdapter(perfRVAdapter);

    }

    private void initObjAssessmentsRV() {
        Log.d(ASSESSMENTS_FRAGMENT, "initObjAssessmentsRV: init obj. assessments recyclerview");

        objRV.setHasFixedSize(true);
        objRVAdapter = new ObjAssessRVAdapter(getContext(), objList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        objRV.setLayoutManager(layoutManager);
        objRV.setItemAnimator(new DefaultItemAnimator());
        objRV.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()),
                DividerItemDecoration.VERTICAL));
        ItemTouchHelper.SimpleCallback simpleCallback2 = new RVObjAssessTouchHelper(0,
                ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback2).attachToRecyclerView(objRV);
        objRV.setAdapter(objRVAdapter);
    }

    private void initCourseHeader() {
        CourseViewModel courseViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(CourseViewModel.class);
        courseViewModel.courseLiveData.observe(getViewLifecycleOwner(),
                courses -> courseHeaderLbl.setText(courses.getCourseName()));
    }

    private void initTermViewModel() {
        final Observer<List<Terms>> termsObserver =
                termEntities -> {
                    termsList.clear();
                    termsList.addAll(termEntities);
                };
        TermViewModel termViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(TermViewModel.class);
        termViewModel.mTerms.observe(getViewLifecycleOwner(), termsObserver);
    }

    private void initPerfAssessVM() {
        final Observer<List<PerformanceAssessments>> perfObserver =
                performanceAssessments -> {
                    args = AssessmentsFragment.this.getArguments();
                    if (args != null) {
                        vmPer_Id = args.getInt(COURSE_ID);
                        perfList.clear();
                        for (PerformanceAssessments pAssess : performanceAssessments) {
                            if (pAssess.getPer_Id() == vmPer_Id) {
                                perfList.add(pAssess);
                                perfAssessmentsVM.setPerfAssessments(vmPer_Id);
                            }
                        }
                        Log.d(TEST, "initPerfAssessVM: " + perfList);
                    }
                    if (perfRVAdapter == null) {
                        perfRVAdapter = new PerfAssessRVAdapter(AssessmentsFragment.this.getActivity(), perfList,
                                AssessmentsFragment.this);
                        perfRV.setAdapter(perfRVAdapter);
                    } else {
                        perfRVAdapter.notifyDataSetChanged();
                    }
                };
        perfAssessmentsVM = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(PerfAssessmentsVM.class);
        perfAssessmentsVM.mPerfAssessments.observe(getViewLifecycleOwner(), perfObserver);
    }

    private void initObjAssessVM() {
        final Observer<List<ObjectiveAssessments>> objObserver =
                (List<ObjectiveAssessments> objectiveAssessments) -> {
                    args = getArguments();
                    if (args != null) {
                        vmObj_Id = args.getInt(COURSE_ID);
                        objList.clear();
                        for (ObjectiveAssessments oAssess : objectiveAssessments) {
                            if (oAssess.getObj_Id() == vmObj_Id) {
                                objAssessmentsVM.setObjAssessments(vmObj_Id);
                                objList.add(oAssess);
                            }
                        }
                        Log.d(TEST, "initObjAssessVM: " + vmObj_Id);
                    }
                    if (objRVAdapter == null) {
                        objRVAdapter = new ObjAssessRVAdapter(getActivity(), objList,
                                this);
                        objRV.setAdapter(objRVAdapter);
                    } else {
                        objRVAdapter.notifyDataSetChanged();
                    }
                };
        objAssessmentsVM = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(ObjAssessmentsVM.class);
        objAssessmentsVM.mObjAssessments.observe(getViewLifecycleOwner(), objObserver);
    }

    private void infoAlert() {
        AlertDialog.Builder infoAlert = new AlertDialog.Builder(Objects.requireNonNull(getContext()),
                R.style.AlertDialogTheme);
        infoAlert.setMessage(R.string.assess_info);
        infoAlert.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        final AlertDialog alertDialog = infoAlert.create();
        alertDialog.show();
    }

    @Override
    public void onPerfAssessClick(int position, int per_Id, int id, int status_Id) {
        EditPerfAssessBottomSheet editPerfAssessBottomSheet =
                new EditPerfAssessBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putInt(P_PER_ID, per_Id);
        bundle.putInt(P_ID, id);
        bundle.putInt(P_STATUS_ID, status_Id);
        editPerfAssessBottomSheet.setArguments(bundle);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        editPerfAssessBottomSheet.show(getActivity().getSupportFragmentManager(),
                "EditPerfAssessBottomSheet");
    }

    @Override
    public void onObjAssessClick(int position, int obj_Id, int id, int status_Id) {
        EditObjAssessBottomSheet editObjAssessBottomSheet =
                new EditObjAssessBottomSheet();
        Bundle bundle1 = new Bundle();
        bundle1.putInt(O_OBJ_ID, obj_Id);
        bundle1.putInt(O_ID, id);
        bundle1.putInt(O_STATUS_ID, status_Id);
        editObjAssessBottomSheet.setArguments(bundle1);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        editObjAssessBottomSheet.show(getActivity().getSupportFragmentManager(),
                "EditObjAssessBottomSheet");
    }

    //TODO:Delete assessments
    private void deletePerfs(View v, int position) {
        final PerformanceAssessments p = perfRVAdapter.removeItem(position);
        final Snackbar undo = Snackbar.make(v, "Performance Assessment: "
                + p.getPerAssesName() + " has been deleted.", Snackbar.LENGTH_LONG);
        undo.setAction("UNDO", v1 -> perfRVAdapter.restoreItem(p, position));
        undo.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if (event == DISMISS_EVENT_TIMEOUT) {
                    perfRVAdapter.removeItem(position);
                    perfAssessmentsVM.deletePA(p);
                }
            }
        });
        undo.setActionTextColor(Color.BLACK);
        undo.show();
    }

    private void deleteObjs(View v, int position) {
        final ObjectiveAssessments o = objRVAdapter.removeItem(position);
        final Snackbar undo = Snackbar.make(v, "Objective Assessment: "
                + o.getObjAssesName() + " has been deleted.", Snackbar.LENGTH_LONG);
        undo.setAction("UNDO", v1 -> objRVAdapter.restoreItem(o, position));
        undo.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if (event == DISMISS_EVENT_TIMEOUT) {
                    objRVAdapter.removeItem(position);
                    objAssessmentsVM.deleteOA(o);
                }
            }
        });
        undo.setActionTextColor(Color.BLACK);
        undo.show();
    }

    @Override
    public void onSwipedPerf(RecyclerView.ViewHolder perfViewHolder, int direction, int position) {
        deletePerfs(this.perfRV, position);
    }

    @Override
    public void onSwipedObj(RecyclerView.ViewHolder objViewHolder, int direction, int position) {
        deleteObjs(this.objRV, position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
