package com.santana.sc.processor;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;



import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.serializer.KryoSerializer;
import org.elasticsearch.hadoop.mr.EsInputFormat;


import org.elasticsearch.hadoop.mr.WritableArrayWritable;

import scala.Tuple2;

public class BusProcessing {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setMaster("local[3]").setAppName("BusProcessor");
		conf.set("spark.serializer", KryoSerializer.class.getName());
		JavaSparkContext ctx = new JavaSparkContext(conf);
		
		JobConf jc = new JobConf();    
		jc.set("es.nodes","localhost");
	    jc.set("es.port","9200");
		jc.set("es.resource", "sptrans2/onibus");       
		//jc.set("es.query", "?q=me*");                 

		JavaPairRDD<String, Map<String, Object>> esRDD = ctx.hadoopRDD(jc, EsInputFormat.class,
		                                        Position.class, MapWritable.class); 
		//esRDD.collect();
		//JavaRDD<String> words = lines.flatMap(s -> Arrays.asList(s));
		JavaPairRDD<String, Position> counts = esRDD
				.mapToPair(w -> {
					WritableArrayWritable address =(WritableArrayWritable) w._2.get(new Text("location"));
	                Text lat = (Text) address.get()[0];
	                Text lon = (Text) address.get()[0];
					
					
					return new Tuple2<String, Position>(w._2().get(new Text("name")).toString(),
							new Position(Float.parseFloat(lat.toString()), Float
									.parseFloat(lon.toString()), null));
				});

		JavaPairRDD<String, Iterable<Position>> grades = counts.groupByKey();

		for (Tuple2<String, Iterable<Position>> tuple : grades.collect()) {
			Iterable<Position> list = tuple._2();
			Position lastPosition = null;
			System.out.println(tuple._1());
			for (Position p : list) {
				if (lastPosition == null) {
					lastPosition = p;
				} else {

					double d = distance(lastPosition.getLatitude(),
							lastPosition.getLongitude(), p.getLatitude(),
							p.getLongitude(), 'K');
					
					

					System.out.println(d);
					lastPosition = p;
				}
			}
		}

	}

	private static double distance(double lat1, double lon1, double lat2,
			double lon2, char unit) {

		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));

		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		if (unit == 'K') {
			dist = dist * 1.609344;
		} else if (unit == 'N') {
			dist = dist * 0.8684;
		}

		return (dist);

	}

	private static double rad2deg(double rad) {

		return (rad * 180 / Math.PI);

	}

	private static double deg2rad(double deg) {

		return (deg * Math.PI / 180.0);

	}

}
