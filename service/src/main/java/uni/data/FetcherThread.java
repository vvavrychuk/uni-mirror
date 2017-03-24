package uni.data;

import java.util.logging.Logger;

public class FetcherThread extends Thread {
    private static Logger logger = Logger.getLogger(FetcherThread.class.getName());

    private Fetcher fetcher;
    private int interval;

    public FetcherThread(Fetcher fetcher, int interval) {
        this.fetcher = fetcher;
        this.interval = interval;
    }

    @Override
    public void run() {
        while (isInterrupted()) {
            logger.info("Starting fetch...");
            fetcher.fetch();
            logger.info("Finished fetch.");
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
