package com.santana.sc.sptrans;

import java.util.List;

public class Main {
	
	public static void main(String[] args) {
		Crawler c = new Crawler();
		try {
			c.getToken();
			List<Bus> buses = c.getLinhas();
			
			for (Bus b : buses) {
				System.out.println(b);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
