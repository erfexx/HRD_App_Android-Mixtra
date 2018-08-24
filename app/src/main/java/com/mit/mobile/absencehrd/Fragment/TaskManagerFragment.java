package com.mit.mobile.absencehrd.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mit.mobile.absencehrd.Activity.CommentActivity;
import com.mit.mobile.absencehrd.Activity.CreateTaskActivity;
import com.mit.mobile.absencehrd.Activity.EditTaskActivity;
import com.mit.mobile.absencehrd.Helper.DatabaseHandler;
import com.mit.mobile.absencehrd.Helper.ListAdapter;
import com.mit.mobile.absencehrd.Helper.SessionManager;
import com.mit.mobile.absencehrd.Helper.SwipeDismissListViewTouchListener;
import com.mit.mobile.absencehrd.Helper.SynchronizeData;
import com.mit.mobile.absencehrd.Model.Comment;
import com.mit.mobile.absencehrd.Model.Task;
import com.mit.mobile.absencehrd.R;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManagerFragment extends Fragment {
    private ListView simpleListView;
    private ArrayList<Task> taskList = new ArrayList<>();
    private DatabaseHandler db;
    private ListAdapter mAdapter;
    SessionManager sessionManager;

    public TaskManagerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_task_manager, container, false);

        sessionManager = new SessionManager(getActivity());

        HashMap<String, String> user = sessionManager.getUserDetails();
        String name = user.get(SessionManager.KEY_NAME);
        final int id = Integer.parseInt(user.get(SessionManager.KEY_ID));

        simpleListView = view.findViewById(R.id.simpleListView);
        db = new DatabaseHandler(getActivity());

        //SynchronizeData.getInstance(this).syncTasks();

        taskList.addAll(db.getAllTasks(id));

        mAdapter = new ListAdapter(taskList,getActivity());
        simpleListView.setAdapter(mAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createIntent = new Intent(getActivity(), CreateTaskActivity.class);
                createIntent.putExtra("user", id);
                startActivity(createIntent);
                //finish();
            }
        });

        simpleListView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Task task = taskList.get(position);
                        Log.i("TASKMANAGER", ""+task.getTname()+ " "+ task.getTdesc()+ " "+ task.getTduedate()+ " "+task.getTassign()+ " "+ task.getTprogress());
                        showDetail(position, task.getTname(), task.getTdesc(), task.getTduedate(), task.getTassign(), task.getTprogress(), task.getTassign_by(), task.get_id());
                        return false;
                    }
                }
        );

        simpleListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long rowId) {
                        Task task = taskList.get(position);
                        Intent intent = new Intent(getActivity(), CommentActivity.class);
                        intent.putExtra("task_id", task.get_id());

                        SynchronizeData.getInstance(getActivity()).SyncComment(task.get_id());

                        startActivity(intent);
                    }
                });

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        simpleListView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks()
                        {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    /**/
                                    final Task task = taskList.get(position);

                                    Intent editIntent = new Intent(getActivity(),EditTaskActivity.class);
                                    editIntent.putExtra("pos", task.get_id());
                                    startActivity(editIntent);
                                }
                            }
                        });


        simpleListView.setOnTouchListener(touchListener);

        return view;
    }



    private void showDetail(final int pos, final String tname, String tdesc, String tduedate, final int tassign, int tprogress, int tassign_by, final int id_task) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.view_task_details, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        TextView tvName = view.findViewById(R.id.tvname);
        TextView tvDesc = view.findViewById(R.id.tvdesc);
        TextView tvDueDate = view.findViewById(R.id.tvduedate);
        TextView tvAssign = view.findViewById(R.id.tvassign);
        TextView tvAssign_by = view.findViewById(R.id.tvassign_by);
        TextView tvProgress = view.findViewById(R.id.tvprogress);

        tvName.setText(tname);
        tvDesc.setText(tdesc);
        tvDueDate.setText(tduedate+" "+id_task);
        tvAssign.setText("Mr./Mrs "+ db.searchUser(tassign));
        tvAssign_by.setText("Mr./Mrs "+ db.searchUser(tassign_by));
        tvProgress.setText(tprogress+" %");

        builder
                .setCancelable(false)
                .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteTask(taskList.get(pos));
                        //SynchronizeData.getInstance(TaskManagerFragment.this).TaskDel(id_task);
                        taskList.remove(pos);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "DELETE", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }



}
