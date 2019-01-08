package com.fanhua.tominiprogram.download;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fanhua.tominiprogram.R;
import com.fanhua.tominiprogram.download.util.queue.QueueController;
import com.fanhua.tominiprogram.download.util.queue.QueueRecyclerAdapter;
import com.liulishuo.okdownload.DownloadContext;
import com.liulishuo.okdownload.DownloadContextListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.cause.EndCause;

public class QueueTaskActivity extends AppCompatActivity {

    private QueueController controller;
    private QueueRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_task);

        initQueueActivity(findViewById(R.id.actionView), (TextView) findViewById(R.id.actionTv),
                (AppCompatRadioButton) findViewById(R.id.serialRb),
                (AppCompatRadioButton) findViewById(R.id.parallelRb),
                (RecyclerView) findViewById(R.id.recyclerView),
                (CardView) findViewById(R.id.deleteActionView), findViewById(R.id.deleteActionTv));
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        this.controller.stop();
    }

    private void initQueueActivity(final View actionView, final TextView actionTv,
                                   final AppCompatRadioButton serialRb,
                                   final AppCompatRadioButton parallelRb,
                                   RecyclerView recyclerView,
                                   final CardView deleteActionView, final View deleteActionTv) {
        initController(actionView, actionTv, serialRb, parallelRb,
                deleteActionView, deleteActionTv);
        initRecyclerView(recyclerView);
        initAction(actionView, actionTv, serialRb, parallelRb, deleteActionView, deleteActionTv);
    }

    private void initController(final View actionView, final TextView actionTv,
                                final AppCompatRadioButton serialRb,
                                final AppCompatRadioButton parallelRb,
                                final CardView deleteActionView, final View deleteActionTv) {
        final QueueController controller = new QueueController();
        this.controller = controller;
        controller.initTasks(this, new DownloadContextListener() {
            @Override
            public void taskEnd(@NonNull DownloadContext context, @NonNull DownloadTask task,
                                @NonNull EndCause cause,
                                @android.support.annotation.Nullable Exception realCause,
                                int remainCount) {
            }

            @Override
            public void queueEnd(@NonNull DownloadContext context) {
                actionView.setTag(null);
                actionTv.setText(R.string.start);
                // to cancel
                controller.stop();

                serialRb.setEnabled(true);
                parallelRb.setEnabled(true);

                deleteActionView.setEnabled(true);
                deleteActionView.setCardElevation((Float) deleteActionView.getTag());
                deleteActionTv.setEnabled(true);

                adapter.notifyDataSetChanged();
            }
        });

    }

    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final QueueRecyclerAdapter adapter = new QueueRecyclerAdapter(controller);
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    private void initAction(final View actionView, final TextView actionTv,
                            final AppCompatRadioButton serialRb,
                            final AppCompatRadioButton parallelRb,
                            final CardView deleteActionView, final View deleteActionTv) {
        deleteActionView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                controller.deleteFiles();
                adapter.notifyDataSetChanged();
            }
        });

        actionTv.setText(R.string.start);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                final boolean started = v.getTag() != null;

                if (started) {
                    controller.stop();
                } else {
                    v.setTag(new Object());
                    actionTv.setText(R.string.cancel);

                    // to start
                    controller.start(serialRb.isChecked());
                    adapter.notifyDataSetChanged();

                    serialRb.setEnabled(false);
                    parallelRb.setEnabled(false);
                    deleteActionView.setEnabled(false);
                    deleteActionView.setTag(deleteActionView.getCardElevation());
                    deleteActionView.setCardElevation(0);
                    deleteActionTv.setEnabled(false);
                }
            }
        });
    }
}
