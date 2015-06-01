package com.santana.sc.monitor;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.santana.sc.geocoding.Main;

public class TimerExample extends TimerTask {

    public void functionToRepeat() {
        System.out.println("Teste");
    }

    public void run() {
        try {
			com.santana.sc.twitter.Main.main(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    public static void main(String[] args) {
      Timer timer = new Timer();
      // Schedule to run every Sunday in midnight
      timer.schedule(
        new  TimerExample(),
        new Date(),
       1000 * 60 * 2
      );
    }//Main method ends
}