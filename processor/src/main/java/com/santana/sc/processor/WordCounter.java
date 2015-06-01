package com.santana.sc.processor;

import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

import java.util.*;

public class WordCounter {
	
	private static final Pattern SPACE = Pattern.compile(" ");
	
	public static void main(String [] args) {
		
		SparkConf conf = new SparkConf().setMaster("local").setAppName("BusProcessor");
		JavaSparkContext ctx = new JavaSparkContext(conf);
		
		JavaRDD<String> lines = ctx.textFile("C:/teste123.txt");
		
		JavaRDD<String> words = lines.flatMap(s -> Arrays.asList(SPACE.split(s)));
		JavaPairRDD<String, Integer> counts =
			    words.mapToPair(w -> new Tuple2<String, Integer>(w, 1))
			         .reduceByKey((x, y) -> x + y);
		
		for (Tuple2<String, Integer> tuple : counts.collect()) {
			System.out.println(tuple._1() + ": " + tuple._2());
		}
		
		
	}

}
