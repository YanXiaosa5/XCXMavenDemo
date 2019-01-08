package com.fanhua.tominiprogram;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fanhua.tominiprogram.SeatTable.SeatTable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池学习
 */
public class ThreadPoolActivity extends AppCompatActivity {

    /**
     * 选座
     */
    private SeatTable seatTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_pool);
        seatTable = findViewById(R.id.seatTable);
        seatTable.setScreenName("9号屏幕");
        seatTable.setMaxSelected(60);
        seatTable.setSeatChecker(new SeatTable.SeatChecker() {
            @Override
            public boolean isValidSeat(int row, int column) {
                if(column==0) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean isSold(int row, int column) {
                return false;
            }

            @Override
            public void checked(int row, int column) {
                System.out.println(row+"行"+column+"列");
            }

            @Override
            public void unCheck(int row, int column) {
                System.out.println(row+"行"+column+"列");
            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return new String[0];
            }
        });

        seatTable.setData(10,12);
    }

    public void createPool(View view) {
        /**
         * 当核心线程数2，任务队列2，最大线程数4时，任务队列为链表结构时，当循环次数i<4时，
         * 线程的执行应该是先创建两个核心线程，执行任务，剩下的放到任务队列，
         * 待核心线程空闲时再执行任务队列的任务，所以任务0、1永远在任务2、3之前运行。打印结果如下：
         */
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(2/*核心线程*/, 4/*最大线程*/, 10/*存活时间*/, TimeUnit.SECONDS/*存活时间单位*/, new LinkedBlockingDeque<Runnable>(2) {
//
//        }/*任务队列*/, new ThreadFactory() {
//            @Override
//            public Thread newThread(@NonNull Runnable r) {
//                return null;
//            }
//        }/*线程工厂*/, new RejectedExecutionHandler() {
//            @Override
//            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//
//            }
//        }/*拒绝策略*/);

//
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(2/*核心线程*/, 4/*最大线程*/,
//                10/*存活时间*/, TimeUnit.SECONDS/*存活时间单位*/,
//                new LinkedBlockingDeque<Runnable>(2)/*任务队列*/);
//
//        for (int i = 0; i < 6; i++) {
//            final int temp = i;
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    SystemClock.sleep(1000);
//                    Log.d("yxs", "" + temp);
//                }
//            };
//            executor.execute(runnable);
//        }

//        fixPool();

        schedulePool();
    }

    /**
     * 定长线程池
     */
    public void fixPool() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(1000);
                    Log.d("yxs", "" + temp);
                }
            };
            executorService.execute(runnable);
        }
    }

    /**
     * 周期性执行任务
     */
    public void schedulePool() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        //延迟1s执行任务(子线程执行)
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                Log.d("yxs", "schedule-Runnable"+threadName());
            }
        }, 1, TimeUnit.SECONDS);


        //延迟2s执行任务
        ScheduledFuture<String> schedule = executorService.schedule(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "schedule-Callable";
            }
        }, 2, TimeUnit.SECONDS);
        try {
            String s = schedule.get();
            Log.d("yxs", s+"线程名称");
        } catch (Exception e) {
            Log.d("yxs", "Exception");
        }

        //表示延迟3秒后每4秒执行一次.
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Log.d("yxs", "scheduleAtFixedRate线程名称"+threadName());
            }
        }, 3, 4, TimeUnit.SECONDS);

        //表示延迟3秒后每5秒执行一次
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Log.d("yxs", "scheduleWithFixedDelay线程名称"+threadName());
            }
        }, 3, 5, TimeUnit.SECONDS);
    }

    /**
     * 获取当前线程名称
     * @return
     */
    public String threadName(){
        return Thread.currentThread().getName();
    }

}