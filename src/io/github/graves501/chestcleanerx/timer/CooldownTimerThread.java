package io.github.graves501.chestcleanerx.timer;

public class CooldownTimerThread extends Thread {

    public void run() {

        try {
            while (true) {
                Thread.sleep(1000);
                CooldownTimer.update();
            }
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

    }

}
