package com.santana.sc.processor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import java.util.Date;

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
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.collect.Lists;

import scala.Tuple2;

public class BusProcessing {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setMaster("local[3]").setAppName("BusProcessor");
		conf.set("spark.serializer", KryoSerializer.class.getName());
		JavaSparkContext ctx = new JavaSparkContext(conf);
		
		JobConf jc = new JobConf();    
		jc.set("es.nodes","localhost");
	    jc.set("es.port","9200");
		jc.set("es.resource", "sptrans3/onibus");  
		
		

		JavaPairRDD<String, Map<String, Object>> esRDD = ctx.hadoopRDD(jc, EsInputFormat.class, Object.class, MapWritable.class); 
		JavaPairRDD<String, Position> counts = esRDD
				.mapToPair(w -> {
					WritableArrayWritable address =(WritableArrayWritable) w._2.get(new Text("location"));
	                Text lat = (Text) address.get()[0];
	                Text lon = (Text) address.get()[1];
	               
	                String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	                DateTimeFormatter dtf = DateTimeFormat.forPattern(pattern);
	                DateTime dateTime = dtf.parseDateTime(w._2().get(new Text("dateBus")).toString());
	                Date date = dateTime.toDate();					
					
					return new Tuple2<String, Position>(w._2().get(new Text("busCode")).toString(),
							new Position(Float.parseFloat(lat.toString()), Float
									.parseFloat(lon.toString()), date));
				});

		JavaPairRDD<String, Iterable<Position>> grades = counts.groupByKey();
		
		for (Tuple2<String, Iterable<Position>> tuple : grades.collect()) {
			Iterable<Position> list = tuple._2();
			Position lastPosition = null;
			System.out.println(tuple._1());
			ArrayList<Position> pos = Lists.newArrayList(list);
			Collections.sort(pos);
			for (Position p : pos) {
				if (lastPosition == null) {
					lastPosition = p;
				} else {

					double d = distance(lastPosition.getLatitude(),
							lastPosition.getLongitude(), p.getLatitude(),
							p.getLongitude(), 'K');
					
					
					LocalDateTime dateLast = new LocalDateTime(lastPosition.getDate());
					LocalDateTime date = new LocalDateTime(p.getDate());
					
					Seconds time = Seconds.secondsBetween(dateLast, date);
					

					System.out.println(d + "   " + date + "   " + dateLast + "   " + time.getSeconds() + "   VEL: " + (d/(time.getSeconds() / 3600.0)));
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
