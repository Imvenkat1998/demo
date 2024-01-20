package com.example.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.Item;
import com.example.utils.ConnectionUtils;

public class ItemDAO {
	Connection conn=null;
	ConnectionUtils c=null;
	public List<Item> getItems() throws Exception {
		
		List<Item> itemlist=new ArrayList<>();
		String query="SELECT * FROM ITEMS";
		c=new ConnectionUtils();
		conn=c.getConnection();
		PreparedStatement ps=conn.prepareStatement(query); 
		ResultSet rs=ps.executeQuery();
		while(rs.next()) {
			Item item=new Item();
			item.setITEM_CODE(rs.getInt(2));
			item.setDESCRIPTION(rs.getString(3));
			item.setPrice(rs.getInt(4));
			item.setSTOCK_QUANTITY(rs.getInt(5));
			itemlist.add(item);
		}
		return itemlist;
	}

	public String insertItem(Item item) {
		
		ItemDAO idao=new ItemDAO();
		c=new ConnectionUtils();
		String query="INSERT INTO ITEMS VALUES (ITEM_SEQUENCE.NEXTVAL,?,?,?,?)";
		try {
			conn=c.getConnection();
			PreparedStatement ps=conn.prepareStatement(query);
			ps.setInt(1,item.getITEM_CODE());
			ps.setString(2, item.getDESCRIPTION());
			ps.setInt(3, item.getPrice());
			ps.setInt(4, item.getSTOCK_QUANTITY());
			ps.execute();
		}catch(Exception e) {
			System.out.println(e);
		}
		return "Item inserted Successfully";
	}
	}


