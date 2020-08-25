package com.ed.shuneladmin.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Notice_Schedule  implements Serializable {

    private int NOTICE_SCHEDULE_ID;

    private String NOTICE_SCHEDULE_T;

    private String NOTICE_SCHEDULE_D;

    private Timestamp NOTICE_SCHEDUL_STARTTIME;

    private Timestamp NOTICE_SCHEDUL_ENDTIME;

    private int SCHEDULE_FLAG;

    private int PRODUCT_ID;

    public Notice_Schedule(int NOTICE_SCHEDULE_ID, String NOTICE_SCHEDULE_T, String NOTICE_SCHEDULE_D, Timestamp NOTICE_SCHEDUL_STARTTIME, Timestamp NOTICE_SCHEDUL_ENDTIME, int SCHEDULE_FLAG, int PRODUCT_ID) {
        this.NOTICE_SCHEDULE_ID = NOTICE_SCHEDULE_ID;
        this.NOTICE_SCHEDULE_T = NOTICE_SCHEDULE_T;
        this.NOTICE_SCHEDULE_D = NOTICE_SCHEDULE_D;
        this.NOTICE_SCHEDUL_STARTTIME = NOTICE_SCHEDUL_STARTTIME;
        this.NOTICE_SCHEDUL_ENDTIME = NOTICE_SCHEDUL_ENDTIME;
        this.SCHEDULE_FLAG = SCHEDULE_FLAG;
        this.PRODUCT_ID = PRODUCT_ID;
    }

    public int getNOTICE_SCHEDULE_ID() {
        return NOTICE_SCHEDULE_ID;
    }

    public void setNOTICE_SCHEDULE_ID(int NOTICE_SCHEDULE_ID) {
        this.NOTICE_SCHEDULE_ID = NOTICE_SCHEDULE_ID;
    }

    public String getNOTICE_SCHEDULE_T() {
        return NOTICE_SCHEDULE_T;
    }

    public void setNOTICE_SCHEDULE_T(String NOTICE_SCHEDULE_T) {
        this.NOTICE_SCHEDULE_T = NOTICE_SCHEDULE_T;
    }

    public String getNOTICE_SCHEDULE_D() {
        return NOTICE_SCHEDULE_D;
    }

    public void setNOTICE_SCHEDULE_D(String NOTICE_SCHEDULE_D) {
        this.NOTICE_SCHEDULE_D = NOTICE_SCHEDULE_D;
    }

    public Timestamp getNOTICE_SCHEDUL_STARTTIME() {
        return NOTICE_SCHEDUL_STARTTIME;
    }

    public void setNOTICE_SCHEDUL_STARTTIME(Timestamp NOTICE_SCHEDUL_STARTTIME) {
        this.NOTICE_SCHEDUL_STARTTIME = NOTICE_SCHEDUL_STARTTIME;
    }

    public Timestamp getNOTICE_SCHEDUL_ENDTIME() {
        return NOTICE_SCHEDUL_ENDTIME;
    }

    public void setNOTICE_SCHEDUL_ENDTIME(Timestamp NOTICE_SCHEDUL_ENDTIME) {
        this.NOTICE_SCHEDUL_ENDTIME = NOTICE_SCHEDUL_ENDTIME;
    }

    public int getSCHEDULE_FLAG() {
        return SCHEDULE_FLAG;
    }

    public void setSCHEDULE_FLAG(int SCHEDULE_FLAG) {
        this.SCHEDULE_FLAG = SCHEDULE_FLAG;
    }

    public int getPRODUCT_ID() {
        return PRODUCT_ID;
    }

    public void setPRODUCT_ID(int PRODUCT_ID) {
        this.PRODUCT_ID = PRODUCT_ID;
    }
}
