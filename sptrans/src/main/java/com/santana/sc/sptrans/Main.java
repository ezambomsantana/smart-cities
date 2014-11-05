package com.santana.sc.sptrans;

public class Main {
	
	public static void main(String[] args) {
		Crawler c = new Crawler();
		try {
			c.getToken();
			c.getLinhas();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
