package com.ed.shuneladmin.bean;

//import java.security.Timestamp;
import java.sql.Timestamp;

public class orderStatistics {
    private int PRODUCT_ID;
    private int CATEGORY_ID;
    private int countCATEGORY_ID;
    private int sumBUY_PRICE;
    private Timestamp ORDER_DATE;

    public orderStatistics(int pRODUCT_ID, int cATEGORY_ID, int countCATEGORY_ID, int sumBUY_PRICE, Timestamp ORDER_DATE) {
        super();
        PRODUCT_ID = pRODUCT_ID;
        CATEGORY_ID = cATEGORY_ID;
        this.countCATEGORY_ID = countCATEGORY_ID;
        this.sumBUY_PRICE = sumBUY_PRICE;
        this.ORDER_DATE = ORDER_DATE;
    }


    public orderStatistics(int pRODUCT_ID, int cATEGORY_ID) {
        super();
        PRODUCT_ID = pRODUCT_ID;
        CATEGORY_ID = cATEGORY_ID;
    }


    public int getPRODUCT_ID() {
        return PRODUCT_ID;
    }
    public void setPRODUCT_ID(int pRODUCT_ID) {
        PRODUCT_ID = pRODUCT_ID;
    }
    public int getCATEGORY_ID() {
        return CATEGORY_ID;
    }
    public void setCATEGORY_ID(int cATEGORY_ID) {
        CATEGORY_ID = cATEGORY_ID;
    }
    public int getCountCATEGORY_ID() {
        return countCATEGORY_ID;
    }
    public void setCountCATEGORY_ID(int countCATEGORY_ID) {
        this.countCATEGORY_ID = countCATEGORY_ID;
    }
    public int getSumBUY_PRICE() {
        return sumBUY_PRICE;
    }
    public void setSumBUY_PRICE(int sumBUY_PRICE) {
        this.sumBUY_PRICE = sumBUY_PRICE;
    }

    public Timestamp getORDER_DATE() {
        return ORDER_DATE;
    }

    public void setORDER_DATE(Timestamp oRDER_DATE) {
        ORDER_DATE = oRDER_DATE;
    }
}
