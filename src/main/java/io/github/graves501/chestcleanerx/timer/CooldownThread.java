package io.github.graves501.chestcleanerx.timer;

public class CooldownThread extends Thread {


    public void run() {

        final int oneSecond = 1000;

        try {
            while (true) {
                Thread.sleep(oneSecond);
                CooldownHandler.update();
            }
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

    }

}
