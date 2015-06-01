package com.santana.sc.processor;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class GradindProcess {
	
	public static void main(String [] args) {
		
		SparkConf conf = new SparkConf().setMaster("local").setAppName("BusProcessor");
		JavaSparkContext ctx = new JavaSparkContext(conf);
		
		JavaRDD<String> lines = ctx.textFile("C:/teste.txt");
		
		JavaRDD<String> words = lines.flatMap(s -> Arrays.asList(s));
		JavaPairRDD<String, Integer> counts = words.mapToPair(w ->  { String[] values = w.split(" ");  return new Tuple2<String, Integer>(values[0], Integer.parseInt(values[1])); });
		
		JavaPairRDD<String, Iterable<Integer>> grades = counts.groupByKey();
		
		for (Tuple2<String, Iterable<Integer>> tuple : grades.collect()) {
			System.out.println(tuple._1() + ": " + tuple._2());
		}
		
		
	}


}
