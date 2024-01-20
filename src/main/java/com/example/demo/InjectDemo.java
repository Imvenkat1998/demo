package com.example.demo;

import org.springframework.context.support.GenericXmlApplicationContext;

public class InjectDemo {

	public String message;
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int age;
	
	public static void main(String[] args) {
		GenericXmlApplicationContext ctx= new GenericXmlApplicationContext();
		ctx.load("app-context.xml");
		ctx.refresh();
		InjectDemo ij=(InjectDemo)ctx.getBean("inject");
		System.out.println(ij.getMessage()+" "+ij.getAge());
		ctx.close();
	}

}
