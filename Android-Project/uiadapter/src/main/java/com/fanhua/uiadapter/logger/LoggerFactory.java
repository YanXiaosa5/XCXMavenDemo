package com.fanhua.uiadapter.logger;

import android.text.TextUtils;

import java.lang.reflect.Constructor;

/**
 * Created by yxs on 2017/7/26.
 */
public class LoggerFactory {
    private static String sDefaultTag = "DefaultLogger";
    private static final int TAG_MAX_LENGTH = 23;

    private static ILogger sDefaultLogger;

    /**
     * 创建一个logger
     *
     * @param tag logger的TAG
     *            @return log接口
     */
    public static ILogger getLogger(String tag) {
        return getNamedLogger(LoggerImpl.class, tag);
    }

    /**
     * 获取默认TAG的logger
     * @return log接口
     */
    public static ILogger getDefault() {
        if (sDefaultLogger == null) {
            sDefaultLogger = getLogger(sDefaultTag);
        }
        return sDefaultLogger;
    }

    /**
     * 获取默认TAG
     * @return 默认的log
     */
    public static String getDefaultTag() {
        return sDefaultTag;
    }

    /**
     * 改变默认TAG
     *
     * @param tag 改变后的TAG
     * @return 改变前的TAG
     */
    public static String changeDefaultTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            throw new IllegalArgumentException("tag can not be empty");
        }

        String oldTag = sDefaultTag;
        if (!tag.equals(sDefaultTag)) {
            sDefaultTag = tag;
            sDefaultLogger = null;
        }
        return oldTag;
    }

    /**
     * 获取一个logger,TAG为tagClass.getSimpleName()
     * @param tagClass 目标类
     * @return log接口
     */
    public static ILogger getLogger(Class<?> tagClass) {
        return tagClass == null ? getDefault() : getLogger(tagClass.getSimpleName());
    }

    public static <T extends AbstractNamedLogger> ILogger getNamedLogger(Class<T> loggerClass, Class<?> tagClass) {//获取一个logger
        return getNamedLogger(loggerClass, tagClass.getSimpleName());
    }

    public static <T extends AbstractNamedLogger> ILogger getNamedLogger(Class<T> loggerClass, String name) {
        if (loggerClass == null) {
            throw new IllegalArgumentException("loggerClass cannot be null");
        }
        String tag = loggerNameToTag(name);
        ILogger logger = null;
        try {
            Constructor<T> constructor = loggerClass.getConstructor(String.class);
            logger = constructor.newInstance(tag);
        } catch (Exception e) {
            throw new RuntimeException("Instantiation Logger Error ", e);
        }
        return logger;
    }

    private static String loggerNameToTag(String loggerName) {
        if (TextUtils.isEmpty(loggerName)) {
            return sDefaultTag;
        }

        int length = loggerName.length();
        if (length <= TAG_MAX_LENGTH) {
            return loggerName;
        }

        int tagLength = 0;
        int lastTokenIndex = 0;
        int lastPeriodIndex;
        StringBuilder tagName = new StringBuilder(TAG_MAX_LENGTH + 3);
        while ((lastPeriodIndex = loggerName.indexOf('.', lastTokenIndex)) != -1) {
            tagName.append(loggerName.charAt(lastTokenIndex));
            int tokenLength = lastPeriodIndex - lastTokenIndex;
            if (tokenLength > 1) {
                tagName.append('*');
            }
            tagName.append('.');
            lastTokenIndex = lastPeriodIndex + 1;

            tagLength = tagName.length();
            if (tagLength > TAG_MAX_LENGTH) {
                return getSimpleName(loggerName);
            }
        }

        int tokenLength = length - lastTokenIndex;
        if (tagLength == 0 || (tagLength + tokenLength) > TAG_MAX_LENGTH) {
            return getSimpleName(loggerName);
        }

        tagName.append(loggerName, lastTokenIndex, length);
        return tagName.toString();
    }

    private static String getSimpleName(String loggerName) {
        int length = loggerName.length();
        int lastPeriodIndex = loggerName.lastIndexOf('.');
        return lastPeriodIndex != -1 && length - (lastPeriodIndex + 1) <= TAG_MAX_LENGTH ? loggerName.substring(lastPeriodIndex + 1) : '*' + loggerName
                .substring(length - TAG_MAX_LENGTH + 1);
    }
}