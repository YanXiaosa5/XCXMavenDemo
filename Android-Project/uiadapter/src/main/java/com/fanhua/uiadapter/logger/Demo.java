package com.fanhua.uiadapter.logger;

public class Demo {
    public void printStackTrace() {
        LoggerFactory.getDefault().printStackTrace();
    }

    public void customTag() {
        ILogger logger = LoggerFactory.getLogger("Yuan");
        logger.d("this is a demo");
        logger.d("Demo class: {}, Current time: {}", Demo.class.getName(), System.currentTimeMillis());
        logger.e("occer exception", new RuntimeException("Just for demo"));
        logger.setEnabled(false).d("logger enable false").setEnabled(true).d("logger enable true");
    }

    public void forMethod() {
        LoggerFactory.getDefault().beginMethod();
        LoggerFactory.getDefault().d("fist log");
        LoggerFactory.getDefault().d("second log");
        LoggerFactory.getDefault().d("third log");
        LoggerFactory.getDefault().d("forth log");
        LoggerFactory.getDefault().endMethod();
    }
}
