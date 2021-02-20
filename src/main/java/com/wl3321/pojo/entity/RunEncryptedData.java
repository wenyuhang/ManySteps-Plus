package com.wl3321.pojo.entity;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2020/10/16 15:46
 * desc   : 微信步数实体类
 */
public class RunEncryptedData {

    /**
     * stepInfoList : [{"step":6945,"timestamp":1600185600},{"step":9789,"timestamp":1600272000},{"step":7553,"timestamp":1600358400},{"step":7526,"timestamp":1600444800},{"step":7613,"timestamp":1600531200},{"step":11174,"timestamp":1600617600},{"step":11172,"timestamp":1600704000},{"step":7926,"timestamp":1600790400},{"step":8644,"timestamp":1600876800},{"step":8598,"timestamp":1600963200},{"step":7924,"timestamp":1601049600},{"step":7278,"timestamp":1601136000},{"step":11730,"timestamp":1601222400},{"step":9049,"timestamp":1601308800},{"step":7683,"timestamp":1601395200},{"step":7033,"timestamp":1601481600},{"step":7973,"timestamp":1601568000},{"step":7820,"timestamp":1601654400},{"step":13695,"timestamp":1601740800},{"step":11074,"timestamp":1601827200},{"step":3220,"timestamp":1601913600},{"step":2983,"timestamp":1602000000},{"step":6664,"timestamp":1602086400},{"step":9790,"timestamp":1602172800},{"step":7498,"timestamp":1602259200},{"step":1100,"timestamp":1602345600},{"step":6793,"timestamp":1602432000},{"step":8821,"timestamp":1602518400},{"step":7399,"timestamp":1602604800},{"step":7077,"timestamp":1602691200},{"step":4240,"timestamp":1602777600}]
     * watermark : {"appid":"wx7daac9b7e03f1cf4","timestamp":1602834038}
     */

    private WatermarkBean watermark;
    private List<StepInfoListBean> stepInfoList;

    public WatermarkBean getWatermark() {
        return watermark;
    }

    public void setWatermark(WatermarkBean watermark) {
        this.watermark = watermark;
    }

    public List<StepInfoListBean> getStepInfoList() {
        return stepInfoList;
    }

    public void setStepInfoList(List<StepInfoListBean> stepInfoList) {
        this.stepInfoList = stepInfoList;
    }

    public static class WatermarkBean {
        /**
         * appid : wx7daac9b7e03f1cf4
         * timestamp : 1602834038
         */

        private String appid;
        private int timestamp;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class StepInfoListBean {
        /**
         * step : 6945
         * timestamp : 1600185600
         */

        private int step;
        private int timestamp;

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }
    }
}
