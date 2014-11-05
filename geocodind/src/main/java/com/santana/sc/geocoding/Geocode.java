package com.santana.sc.geocoding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Geocode {


	public String getLinhas(String lat, String lon) throws Exception {


			String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon;

			URL u = new URL(url);

			HttpURLConnection connection = (HttpURLConnection) u
					.openConnection();

	
			connection.toString();

			int status = connection.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			String total = "";

			while ((inputLine = in.readLine()) != null) {
				total = total + inputLine;
			}
			System.out.println(total);
			
			JSONParser parser = new JSONParser();

			JSONObject result = (JSONObject) parser.parse(total);
			
			
			JSONArray s = (JSONArray) result.get("results");
			JSONObject o = (JSONObject) s.get(0);
			JSONArray address = (JSONArray) o.get("address_components");
			
			String number = (String) ((JSONObject) address.get(0)).get("long_name");
			String street = (String) ((JSONObject) address.get(1)).get("long_name");
			
			String address1 = street + " " + number;
	//		Iterator unitsIterator = units.iterator();
		//	while (unitsIterator.hasNext()) {
		//		jsonObject = (JSONObject) unitsIterator.next();

			
			//}

			in.close();
		//}

		return address1;
	}

	
}
