package com.TETOSOFT.tilegame;

import java.util.Timer;
import java.util.TimerTask;

public class ScoreTimer {

    int secondPassed = 0;
    Timer timer = new Timer();

    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            secondPassed++;
            //System.out.println("Second passed: " + secondPassed);
        }
    };

    public void start(){

        timer.scheduleAtFixedRate(task, 1000, 1000);
    }
}
