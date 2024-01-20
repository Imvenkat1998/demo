package com.example.demo.model;

public class Item {
	private int item_id;
	private int	ITEM_CODE;
	private String DESCRIPTION;
	private int	Price;
	private int STOCK_QUANTITY;
	public int getItem_id() {
		return item_id;
	}
	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}
	public int getITEM_CODE() {
		return ITEM_CODE;
	}
	public void setITEM_CODE(int iTEM_CODE) {
		ITEM_CODE = iTEM_CODE;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public int getPrice() {
		return Price;
	}
	public void setPrice(int price) {
		Price = price;
	}
	public int getSTOCK_QUANTITY() {
		return STOCK_QUANTITY;
	}
	public void setSTOCK_QUANTITY(int sTOCK_QUANTITY) {
		STOCK_QUANTITY = sTOCK_QUANTITY;
	}

}
