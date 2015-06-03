package com.santana.sc.processor.streaming;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
















import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.OutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Seconds;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;
















import scala.Function;
import scala.Tuple2;
import twitter4j.Status;

public class CityStream implements Serializable {

	private static final Pattern SPACE = Pattern.compile(" ");
	public static void main(String [] args) {
		
		boolean log4jInitialized = Logger.getRootLogger().getAllAppenders().hasMoreElements();

			    	Logger.getLogger(CityStream.class).info("Setting log level to [WARN] for streaming example." +
			        " To override add a custom log4j.properties to the classpath.");
			      Logger.getRootLogger().setLevel(Level.WARN);
			      
		System.setProperty("twitter4j.oauth.consumerKey", "hGFBaAJKJiK1uh99uyZ2pdNpf");
	    System.setProperty("twitter4j.oauth.consumerSecret", "rvK7pj1pS4Y68Y2XYkdMU3m8sgbjyqIzJOdzkzeXLCmaX2YSFC");
	    System.setProperty("twitter4j.oauth.accessToken", "84158599-g2VgFmSKGr7ZpVKiK055kiwK4xhwPb1CfGpXWgE2c");
	    System.setProperty("twitter4j.oauth.accessTokenSecret", "cqqECq8kSoJk3WKyrCJCohR5s5YJL0AqVlstgWa4zUlJQ");
	    
	    SparkConf conf = new SparkConf().setMaster("local[3]").setAppName("BusProcessor");	
		JavaStreamingContext sc = new JavaStreamingContext(conf, new Duration(10000));
		
		String [] query = new String[4];
		query[0] = "São Paulo";
		query[1] = "teste";
		query[2] = "maria";
		query[3] = "Blatter";
 		
		JavaDStream<Status> tweets = TwitterUtils.createStream(sc, null, query);
		JavaDStream<String> x = tweets.map(s -> s.getText());
		JavaPairDStream<String, String> t = tweets.mapToPair(s -> new Tuple2<String, String>(s.getUser().getName(), s.getText()));
	    t.saveAsHadoopFiles("c:/teste.txt", "txt");
	    
		//t.foreachRDD(y -> { y.foreach(a -> {System.out.println(a._1);} ); });
		
		t.print();
		
		sc.start();
	    sc.awaitTermination();
		
	}
	
}
