package com.example.demo.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.DAO.ItemDAO;
import com.example.demo.model.Item;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Controller
public class ItemController extends HttpServlet{
	@PostMapping("/additems")
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException {
		 Item item=new Item();
		 ItemDAO idao=new ItemDAO();
		 int itemcode=Integer.parseInt(req.getParameter("itemCode"));
		 int price=Integer.parseInt(req.getParameter("price"));
		 int quantity=Integer.parseInt(req.getParameter("stockQuantity"));
		
		 item.setITEM_CODE(itemcode);
		 item.setDESCRIPTION(req.getParameter("description"));
		 item.setPrice(price);
		 item.setSTOCK_QUANTITY(quantity);
		 idao.insertItem(item); 
		 res.sendRedirect("showitems.html");
	}
	
	@GetMapping("/getitems")
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException{
		List<Item> itemlist=new ArrayList<>();
		ItemDAO idao=new ItemDAO();
		try {
			itemlist=idao.getItems();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		res.getWriter().write(new Gson().toJson(itemlist));
		
		
	}

}
