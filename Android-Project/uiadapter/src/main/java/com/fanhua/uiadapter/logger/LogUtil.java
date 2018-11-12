package com.fanhua.uiadapter.logger;

/**
 * Created by yxs on 17-8-5.
 */

public class LogUtil {
    private LogUtil() {}

    /**
     * 获取调用此方法的方法调用信息
     *
     * @param depth      调用此方法的方法级别是 0
     * @param separator  信息之间的分隔符
     * @param fileName   是否需要方法所在的文件名
     * @param lineNumber 是否需要方法所在的行号
     * @param className  是否需要方法所在的类名
     * @param threadName 是否需要方法所在的线程名
     * @return 多项数据的组合字符串
     */
    public static String getCallerInfo(int depth, String separator, boolean fileName, boolean lineNumber, boolean className, boolean threadName) {
        StackTraceElement[] stes = Thread.currentThread().getStackTrace();
        if (stes != null && stes.length > 3 + depth) {
            StackTraceElement ste = stes[3 + depth];
            StringBuilder result = new StringBuilder();
            result.append("[");
            if (fileName) {
                result.append(ste.getFileName()).append(separator);
            }
            result.append(ste.getMethodName()).append("()");
            if (lineNumber) {
                result.append(separator).append(ste.getLineNumber());
            }
            if (className) {
                result.append(separator).append(ste.getClassName());
            }
            if (threadName) {
                result.append(separator).append(Thread.currentThread().getName());
            }
            result.append("]");
            return result.toString();
        }
        return "";
    }

    public static String getCallerName() {
        return LogUtil.getCallerInfo(1, "", false, false, false, false);
    }
}
