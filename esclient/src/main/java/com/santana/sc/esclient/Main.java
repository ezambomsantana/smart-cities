package com.santana.sc.esclient;

import java.util.Date;
import java.util.List;

import com.santana.sc.sptrans.Bus;
import com.santana.sc.sptrans.Crawler;

public class Main {

	
	public static void main(String [] args) throws Exception {
		
		Crawler c = new Crawler();
		c.getToken();
		List<Bus> buses = c.getLinhas();
		
		ESClient client = new ESClient();
		for (Bus b :  buses) {
			client.putDataBus(b.getCode(), 0, b.getName(), b.getLetters(), "" + b.getLat(), "" + b.getLon(), new Date());
		}
		
	}
	
}
