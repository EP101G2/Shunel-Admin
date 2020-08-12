package com.ed.shuneladmin.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Promotion implements Serializable {

	private int Promotion_ID;
	private String product_Name;
	private int Product_ID;
	private int Promotion_Price;
	private int Product_Price;
	private Timestamp Date_Start;
	private Timestamp Date_End;



	public Promotion() {
	}

	// 促銷消息使用，不包含原先價格
	public Promotion(int promotion_ID, String product_Name, int product_ID, int promotion_Price, Timestamp date_Start,
			Timestamp date_End) {
		super();
		Promotion_ID = promotion_ID;
		this.product_Name = product_Name;
		Product_ID = product_ID;
		Promotion_Price = promotion_Price;
		Date_Start = date_Start;
		Date_End = date_End;
	}

	public Promotion(int promotion_ID, String product_Name, int product_ID, int promotion_Price, int product_Price,
			Timestamp date_Start, Timestamp date_End) {
		super();
		Promotion_ID = promotion_ID;
		this.product_Name = product_Name;
		Product_ID = product_ID;
		Promotion_Price = promotion_Price;
		Product_Price = product_Price;
		Date_Start = date_Start;
		Date_End = date_End;
	}

	public Promotion(int promotion_ID, int product_ID, int product_Price, Timestamp date_Start,Timestamp date_End) {
		super();
		Promotion_ID = promotion_ID;
		Product_ID = product_ID;
		Product_Price = product_Price;
		Date_Start = date_Start;
		Date_End = date_End;
	}

	public int getPromotion_ID() {
		return Promotion_ID;
	}

	public void setPromotion_ID(int promotion_ID) {
		Promotion_ID = promotion_ID;
	}

	public String getProduct_Name() {
		return product_Name;
	}

	public void setProduct_Name(String product_Name) {
		this.product_Name = product_Name;
	}

	public int getProduct_ID() {
		return Product_ID;
	}

	public void setProduct_ID(int product_ID) {
		Product_ID = product_ID;
	}

	public int getPromotion_Price() {
		return Promotion_Price;
	}

	public void setPromotion_Price(int promotion_Price) {
		Promotion_Price = promotion_Price;
	}

	public int getProduct_Price() {
		return Product_Price;
	}

	public void setProduct_Price(int product_Price) {
		Product_Price = product_Price;
	}

	public Timestamp getDate_Start() {
		return Date_Start;
	}

	public void setDate_Start(Timestamp date_Start) {
		Date_Start = date_Start;
	}

	public Timestamp getDate_End() {
		return Date_End;
	}

	public void setDate_End(Timestamp date_End) {
		Date_End = date_End;
	}
}
