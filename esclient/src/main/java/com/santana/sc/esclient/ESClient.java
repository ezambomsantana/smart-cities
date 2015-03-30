package com.santana.sc.esclient;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.support.format.ValueFormatter.GeoHash;

public class ESClient {
	
	public void putData(int busCode, int lineCode, String name, String letters, String lat, String lon, Date date) {

		
		Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
		
		double vetor [] = new double[2];
		vetor[0] = Double.parseDouble(lon);
		vetor[1] = Double.parseDouble(lat);
		//GeoPoint point = new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lon));
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("busCode", ""+ busCode);
		data.put("lineCode", ""+ lineCode);
		data.put("location", vetor);
		//data.put("lon", lon);
		data.put("name", name);
		data.put("letters", letters);
		data.put("dateBus", date);

		IndexResponse response = client.prepareIndex("sptrans2", "onibus")
		        .setSource(data)
		        .execute()
		        .actionGet();
		
		client.close();
	}
	
//	public UserLocalization getLocalization(String id) {
//		Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
//
//	
//		SearchResponse response = client.prepareSearch("em", "em")
//		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//		        .setQuery(QueryBuilders.termQuery("id", id))             // Query
//		        .setFrom(0).setSize(60).setExplain(true)
//		        .execute()
//		        .actionGet();
//		
//		SearchHit[] results = response.getHits().getHits();
//		UserLocalization user = null;
//		for (SearchHit hit : results) {
//			user = new UserLocalization();
//		  System.out.println(hit.getId());    //prints out the id of the document
//		  Map<String,Object> result = hit.getSource();   //the retrieved document
//		  user.setId((String) result.get("id")); 
//		  user.setId((String) result.get("lat")); 
//		  user.setId((String) result.get("lon")); 
//		  
//		}
//		System.out.println("TEste");
//		
//		client.close();
//		return user;
//	}

}
