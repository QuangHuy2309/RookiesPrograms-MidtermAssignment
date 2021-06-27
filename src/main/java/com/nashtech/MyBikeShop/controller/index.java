package com.nashtech.MyBikeShop.controller;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.nashtech.MyBikeShop.entity.Products;



public class index {
	public static void main(String[] args) {
		StringBuilder str = new StringBuilder();
		str.append("CC");
		
		Logger logger = LogManager.getLogger(index.class);  
		BasicConfigurator.configure();  
		logger.info("info");
		logger.error("error");
		
		Products products = new Products();
		products.setId("A");
		products.setName("B");
		products.setPrice(0);
		products.setQuantity(0);
		products.setProductType("C");
		System.out.println(products);
		System.out.print("Ex1: ");
		List<String> strings1 = Arrays.asList("this","is","a","tests","list","of","alias","string","test","perform","technical");
		Collections.sort(strings1, (x,y) -> x.compareTo(y));
		strings1.forEach(System.out::print);
		
		System.out.println();
		System.out.print("Ex2: ");
		List<String> strings2  = strings1.stream().filter(t -> t.startsWith("t")).collect(Collectors.toList());
		strings2.forEach(System.out::print);
		
		System.out.println();
		System.out.print("Ex3: ");
		System.out.print(strings1.stream().filter(t -> t.length()> 3).collect(Collectors.toList()));//.findFirst().get());
		
	}
}
