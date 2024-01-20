package com.example.demo;

import java.util.Set;

import org.springframework.context.support.GenericXmlApplicationContext;

public class CollectionInject {
	private Set<Object> set;
	public Set<Object> getSet() {
		return set;
	}
	public void setSet(Set<Object> set) {
		this.set = set;
	}
	public static void main(String[] args) {
		GenericXmlApplicationContext ctx=new GenericXmlApplicationContext();
		ctx.load("app-context.xml");
		ctx.refresh();
		CollectionInject ci=(CollectionInject)ctx.getBean("injectCollection");
		ci.display();
		ctx.close();
		
	}
	public void display() {
		//Here we have inject the direct String values as well as the values from the lyrics class
		set.forEach(obj->System.out.println(obj));
	}
	

}
