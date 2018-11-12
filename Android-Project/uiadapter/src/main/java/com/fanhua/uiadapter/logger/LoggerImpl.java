package com.fanhua.uiadapter.logger;

import android.util.Log;

/**
 * Created by yxs on 2017/7/26.
 *
 * 实现ILogger的基本功能
 */
public class LoggerImpl extends AbstractNamedLogger {
    private boolean mIsForceLog = true;
    private boolean mIsEnabled = true;

    public LoggerImpl(String name) {
        super(name);
    }

    @Override
    public boolean isVerboseEnabled() {
        return isLoggable(Log.VERBOSE);
    }

    @Override
    public ILogger v(String msg) {
        log(Log.VERBOSE, msg, null);
        return this;
    }

    @Override
    public ILogger v(String format, Object... args) {
        formatAndLog(Log.VERBOSE, format, args);
        return this;
    }

    @Override
    public ILogger v(String msg, Throwable t) {
        log(Log.VERBOSE, msg, t);
        return this;
    }

    @Override
    public ILogger traceIn(String msg) {
        log(Log.VERBOSE, "-----------------" + msg + "--BEGIN-----------------", null);
        return this;
    }

    @Override
    public ILogger traceOut(String msg) {
        log(Log.VERBOSE, "-----------------" + msg + "--END-----------------", null);
        return this;
    }

    @Override
    public ILogger beginMethod() {
        String callerInfo = LogUtil.getCallerInfo(1, ", ", true, true, false, false);
        traceIn(callerInfo);
        return this;
    }

    @Override
    public ILogger endMethod() {
        String callInfo = LogUtil.getCallerInfo(1, ", ", true, true, false, false);
        traceOut(callInfo);
        return this;
    }

    @Override
    public boolean isDebugEnabled() {
        return isLoggable(Log.DEBUG);
    }

    @Override
    public ILogger d(String msg) {
        log(Log.DEBUG, msg, null);
        return this;
    }

    @Override
    public ILogger d(String format, Object... args) {
        formatAndLog(Log.DEBUG, format, args);
        return this;
    }

    @Override
    public ILogger d(String msg, Throwable t) {
        log(Log.DEBUG, msg, t);
        return this;
    }

    @Override
    public boolean isInfoEnabled() {
        return isLoggable(Log.INFO);
    }

    @Override
    public ILogger i(String msg) {
        log(Log.INFO, msg, null);
        return this;
    }

    @Override
    public ILogger i(String format, Object... args) {
        formatAndLog(Log.INFO, format, args);
        return this;
    }

    @Override
    public ILogger i(String msg, Throwable t) {
        log(Log.INFO, msg, t);
        return this;
    }

    @Override
    public boolean isWarnEnabled() {
        return isLoggable(Log.WARN);
    }

    @Override
    public ILogger w(String msg) {
        log(Log.WARN, msg, null);
        return this;
    }

    @Override
    public ILogger w(String format, Object... args) {
        formatAndLog(Log.WARN, format, args);
        return this;
    }

    @Override
    public ILogger w(String msg, Throwable t) {
        log(Log.WARN, msg, t);
        return this;
    }

    @Override
    public boolean isErrorEnabled() {
        return isLoggable(Log.ERROR);
    }

    @Override
    public ILogger e(Throwable t) {
        log(Log.ERROR, "", t);
        return this;
    }

    @Override
    public ILogger e(String msg) {
        log(Log.ERROR, msg, null);
        return this;
    }

    @Override
    public ILogger e(String format, Object... args) {
        formatAndLog(Log.ERROR, format, args);
        return this;
    }

    @Override
    public ILogger e(String msg, Throwable t) {
        log(Log.ERROR, msg, t);
        return this;
    }

    @Override
    public ILogger printStackTrace() {
        if (isEnabled() && (isForceLog() || isLoggable(Log.VERBOSE))) {
            StackTraceElement[] stes = Thread.currentThread().getStackTrace();
            StringBuilder result = new StringBuilder();
            for (int i = 3; i < stes.length; i++) {
                StackTraceElement ste = stes[i];
                if (ste.isNativeMethod()) {
                    continue;
                }
                result.append(ste.toString()).append("\n");
            }
            logInternal(Log.VERBOSE, result.toString(), null);
        }
        return this;
    }

    @Override
    public boolean isEnabled() {
        return mIsEnabled;
    }

    @Override
    public ILogger setEnabled(boolean isEnabled) {
        mIsEnabled = isEnabled;
        return this;
    }

    @Override
    public boolean isForceLog() {
        return mIsForceLog;
    }

    @Override
    public ILogger setForceLog(boolean isForceLog) {
        mIsForceLog = isForceLog;
        return this;
    }

    protected boolean isLoggable(int priority) {
        return Log.isLoggable(mName, priority);
    }

    protected void formatAndLog(int priority, String format, Object... argArray) {
        if (isEnabled() && (isForceLog() || isLoggable(priority))) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            logInternal(priority, ft.getMessage(), ft.getThrowable());
        }
    }

    protected void log(int priority, String msg, Throwable t) {
        if (isEnabled() && (isForceLog() || isLoggable(priority))) {
            logInternal(priority, msg, t);
        }
    }

    private void logInternal(int priority, String msg, Throwable t) {
        if (t != null) {
            msg += '\n' + Log.getStackTraceString(t);
        }
        Log.println(priority, mName, msg);
    }
}
