package com.fanhua.tominiprogram.async;

/**
 * Created by YuanZezhong.
 * Date: 2018/10/26
 * Time: 13:33
 */
public class RunnableTask extends TagTask<Object> {
    private Runnable task;

    public RunnableTask(Runnable task, String tag) {
        super(tag);
        if (task == null) {
            throw new IllegalArgumentException("task == null");
        }
        this.task = task;
    }

    @Override
    public Object call(Call<Object> originalCall) throws Exception {
        task.run();
        return null;
    }
}
