package com.ed.shuneladmin.bean;

public class Order_Detail {

    //宣告區
    private int order_ID;
    private int order_Detail_Amount;
    private int Product_ID;
    private int order_Detail_Buy_Price;
    private String color;
    private int imageId;
    private String product_Name;

//    private int order_ID;
//	private int order_Detail_Amount;
//	private int Product_ID;
//	private int order_Detail_Buy_Price;
//	private String color;
//	private String product_Name;

    public Order_Detail(int order_ID, int order_Detail_Amount, int Product_ID, int order_Detail_Buy_Price,
                        String color, int imageId, String product_Name) {
        super();
        this.order_ID = order_ID;
        this.order_Detail_Amount = order_Detail_Amount;
        this.Product_ID = Product_ID;
        this.order_Detail_Buy_Price = order_Detail_Buy_Price;
        this.color = color;
        this.imageId = imageId;
        this.product_Name = product_Name;
    }
    //建構子

    public Order_Detail(String product_Name, int buy_Price) {
        this.order_Detail_Buy_Price = buy_Price;
        this.product_Name = product_Name;//setting of product name?
    }


    public int getorderDetOrderId() {
        return order_ID;
    }

    public void setorderDetOrderId(int order_ID) {
        this.order_ID = order_ID;
    }

    public int getOrder_Detail_Amount() {
        return order_Detail_Amount;
    }

    public void setOrder_Detail_Amount(int order_Detail_Amount) {
        this.order_Detail_Amount = order_Detail_Amount;
    }

    public int getorderDetProductId() {
        return Product_ID;
    }

    public void setorderDetProductId(int Product_ID) {
        this.Product_ID = Product_ID;
    }

    public int getOrder_Detail_Buy_Price() {
        return order_Detail_Buy_Price;
    }

    public void setOrder_Detail_Buy_Price(int order_Detail_Buy_Price) {
        this.order_Detail_Buy_Price = order_Detail_Buy_Price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getorderDetImageId() {
        return imageId;
    }

    public void setorderDetImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getorderDetProductName() {
        return product_Name;
    }

    public void setorderDetProductName(String product_Name) {
        this.product_Name = product_Name;
    }
}
