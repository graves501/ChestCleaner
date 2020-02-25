package io.github.graves501.chestcleanerx.timer;

public class Counter extends Thread {

    public void run() {

        try {
            while (true) {
                Thread.sleep(1000);
                Timer.update();
            }
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

    }

}
