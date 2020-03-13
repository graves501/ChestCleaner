package io.github.graves501.chestcleanerx.timer;

public class CooldownTimerThread extends Thread {


    public void run() {

        final int oneSecond = 1000;

        try {
            while (true) {
                Thread.sleep(oneSecond);
                CooldownTimer.update();
            }
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

    }

}
