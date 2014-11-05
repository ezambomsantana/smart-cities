package com.santana.sc.twitter;

import java.util.List;

import com.santana.sc.geocoding.Geocode;
import com.santana.sc.sptrans.Bus;
import com.santana.sc.sptrans.Crawler;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Main {

	public static void main(String[] args) throws Exception {
		
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("fHLUGP8K7Q3tpQ5eW0cB3fBSn")
		  .setOAuthConsumerSecret("hPBXA8Oq3pqi25dtyuftrMYPvB6sozNP0M3ZSyZdVvWrc9DxF6")
		  .setOAuthAccessToken("84158599-XRjPcQlw7qGRbxgr2wGzVBC6wnuxslAxX1lbOEfbE")
		  .setOAuthAccessTokenSecret("BGzr2SPpglTTsdYT1rfjSDAEiIajtPkWg53Sys5xKcoX1");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		
		Crawler c = new Crawler();
		c.getToken();
		List<Bus> buses = c.getLinhas();
		
		Geocode geo = new Geocode();
		for (Bus b : buses) {
			String address = geo.getLinhas(String.valueOf(b.getLat()), String.valueOf((b.getLon())));

			Status status = twitter.updateStatus("Onibus: " + b.getCode() + " Linha: " + b.getLetters() + " Local: " + address);
			System.out.println("Successfully updated the status to [" + status.getText() + "].");

		}
		
		
	}

}
