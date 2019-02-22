package com.fanhua.tominiprogram.download;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanhua.tominiprogram.R;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.SpeedCalculator;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed;
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

public class SingleTaskActivity extends AppCompatActivity {

    private static final String TAG = "SingleActivity";
    private DownloadTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_task);

        initSingleDownload(
                (TextView) findViewById(R.id.statusTv),
                (ProgressBar) findViewById(R.id.progressBar),
                findViewById(R.id.actionView),
                (TextView) findViewById(R.id.actionTv));
    }

    private void initSingleDownload(TextView statusTv, ProgressBar progressBar, View actionView,
                                    TextView actionTv) {
        initTask();
        initStatus(statusTv, progressBar);
        initAction(actionView, actionTv, statusTv, progressBar);
    }

    private void initTask() {
        final String url = "https://cdn.llscdn.com/yy/files/xs8qmxn8-lls-LLS-5.8-800-20171207-111607.apk";
        String[] split = url.split("/");
        final String filename = split == null?url.substring(url.length() - 20,url.length() - 1):(split.length > 0?split[split.length - 1]:url.substring(url.length() - 20,url.length() - 1));
        //确定文件存放目录
        final File parentFile = DemoUtil.getParentFile(this);
        //初始化任务
        task = new DownloadTask.Builder(url, parentFile)//任务地址和存放目录
                .setFilename(filename)//文件名称
                // the minimal interval millisecond for callback progress
                .setMinIntervalMillisCallbackProcess(16)
                // ignore the same task has already completed in the past.
                .setPassIfAlreadyCompleted(false)
                .build();
    }

    /**
     * 初始化状态
     * @param statusTv
     * @param progressBar
     */
    private void initStatus(TextView statusTv, ProgressBar progressBar) {

        final StatusUtil.Status status = StatusUtil.getStatus(task);
        if (status == StatusUtil.Status.COMPLETED) {
            progressBar.setProgress(progressBar.getMax());
        }

        statusTv.setText(status.toString());
        final BreakpointInfo info = StatusUtil.getCurrentInfo(task);
        if (info != null) {
            Log.d(TAG, "init status with: " + info.toString());

            DemoUtil.calcProgressToView(progressBar, info.getTotalOffset(), info.getTotalLength());
        }
    }

    private void initAction(final View actionView, final TextView actionTv, final TextView statusTv,
                            final ProgressBar progressBar) {
        actionTv.setText("开始");
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                final boolean started = task.getTag() != null;

                if (started) {
                    // to cancel
                    task.cancel();
                } else {
                    actionTv.setText("取消");
                    // to start
                    startTask(statusTv, progressBar, actionTv);
                    // mark
                    task.setTag("mark-task-started");
                }
            }
        });
    }

    /**
     * 开始下载任务
     * @param statusTv
     * @param progressBar
     * @param actionTv
     */
    private void startTask(final TextView statusTv, final ProgressBar progressBar,
                           final TextView actionTv) {

        task.enqueue(new DownloadListener4WithSpeed() {
            private long totalLength;
            private String readableTotalLength;

            @Override public void taskStart(@NonNull DownloadTask task) {
                //进度显示
                statusTv.setText("任务开始");
            }

            @Override
            public void infoReady(@NonNull DownloadTask task, @NonNull BreakpointInfo info,
                                  boolean fromBreakpoint,
                                  @NonNull Listener4SpeedAssistExtend.Listener4SpeedModel model) {
                statusTv.setText("准备信息");
                //总长度
                totalLength = info.getTotalLength();
                readableTotalLength = Util.humanReadableBytes(totalLength, true);
                //计算进度
                DemoUtil.calcProgressToView(progressBar, info.getTotalOffset(), totalLength);
            }

            @Override
            public void connectStart(@NonNull DownloadTask task, int blockIndex,
                                               @NonNull Map<String, List<String>> requestHeaders) {
                final String status = "Connect Start " + blockIndex;
                statusTv.setText(status);
            }

            @Override
            public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode,
                                   @NonNull Map<String, List<String>> responseHeaders) {

                final String status = "Connect End " + blockIndex;
                statusTv.setText(status);
            }

            @Override
            public void progressBlock(@NonNull DownloadTask task, int blockIndex,
                                      long currentBlockOffset,
                                      @NonNull SpeedCalculator blockSpeed) {
            }

            @Override public void progress(@NonNull DownloadTask task, long currentOffset,
                                           @NonNull SpeedCalculator taskSpeed) {
                final String readableOffset = Util.humanReadableBytes(currentOffset, true);
                final String progressStatus = readableOffset + "/" + readableTotalLength;
                final String speed = taskSpeed.speed();
                final String progressStatusWithSpeed = progressStatus + "(" + speed + ")";

                statusTv.setText(progressStatusWithSpeed);
                DemoUtil.calcProgressToView(progressBar, currentOffset, totalLength);

//                NumberFormat numberFormat = NumberFormat.getInstance();
//                numberFormat.setMaximumFractionDigits(2);
//                String result = numberFormat.format(currentOffset / totalLength * 100);
//
//                Log.i(TAG,result+"%");
            }

            @Override
            public void blockEnd(@NonNull DownloadTask task, int blockIndex, BlockInfo info,
                                 @NonNull SpeedCalculator blockSpeed) {
            }

            @Override public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause,
                                           Exception realCause,
                                          @NonNull SpeedCalculator taskSpeed) {
                final String statusWithSpeed = cause.toString() + " " + taskSpeed.averageSpeed();
                statusTv.setText(statusWithSpeed);

                actionTv.setText("开始");
                // mark
                task.setTag(null);
                if (cause == EndCause.COMPLETED) {
                    final String realMd5 = fileToMD5(task.getFile().getAbsolutePath());
                    if (!realMd5.equalsIgnoreCase("f836a37a5eee5dec0611ce15a76e8fd5")) {
                        Log.e(TAG, "file is wrong because of md5 is wrong " + realMd5);
                    }
                }

                Log.i(TAG,"任务结束:"+task.getFile().getAbsolutePath());
            }
        });
    }

    public static String fileToMD5(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[1024];
            MessageDigest digest = MessageDigest.getInstance("MD5");
            int numRead = 0;
            while (numRead != -1) {
                numRead = inputStream.read(buffer);
                if (numRead > 0) {
                    digest.update(buffer, 0, numRead);
                }
            }
            byte[] md5Bytes = digest.digest();
            return convertHashToString(md5Bytes);
        } catch (Exception ignored) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    Log.e(TAG, "file to md5 failed", e);
                }
            }
        }
    }

    private static String convertHashToString(byte[] md5Bytes) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            buf.append(Integer.toString((md5Bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return buf.toString().toUpperCase();
    }


    private boolean isTaskRunning() {
        final StatusUtil.Status status = StatusUtil.getStatus(task);
        return status == StatusUtil.Status.PENDING || status == StatusUtil.Status.RUNNING;
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (task != null) task.cancel();
    }
}
