package com.fanhua.tominiprogram;

import java.io.Serializable;

public class ResponseBean implements Serializable {


    /**
     * code : 200
     * message : SUCCESS
     * data : {"echostr":"706b372fb39a4b749ff3e5fd855c56ad","encryptstr":"llllll","sign":"532CD89D048E1AC995B375BB7173D865","timestamp":"1540884465077"}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * echostr : 706b372fb39a4b749ff3e5fd855c56ad
         * encryptstr : llllll
         * sign : 532CD89D048E1AC995B375BB7173D865
         * timestamp : 1540884465077
         */

        private String echostr;
        private String encryptstr;
        private String sign;
        private String timestamp;

        public String getEchostr() {
            return echostr;
        }

        public void setEchostr(String echostr) {
            this.echostr = echostr;
        }

        public String getEncryptstr() {
            return encryptstr;
        }

        public void setEncryptstr(String encryptstr) {
            this.encryptstr = encryptstr;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
