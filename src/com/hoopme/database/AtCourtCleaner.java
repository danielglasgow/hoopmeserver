package com.hoopme.database;

public class AtCourtCleaner {

    public static final int SLEEP_TIME_MINUTES = 30;

    private static final CleanerThread cleanerThread = new CleanerThread();
    private static boolean started = false;

    public static synchronized void Start() {
        if (!started) {
            cleanerThread.start();
        }
        started = true;
    }

    public static synchronized void Stop() {
        if (started) {
            cleanerThread.isDone = true;
            cleanerThread.interrupt();
            try {
                cleanerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        started = false;
    }

    private static class CleanerThread extends Thread {

        private boolean isDone;

        public CleanerThread() {
            this.isDone = false;
        }

        @Override
        public void run() {
            while (!isDone) {
                DatabaseInterface.getInstance().cleanAtCourt();
                try {
                    Thread.sleep(SLEEP_TIME_MINUTES * 6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
