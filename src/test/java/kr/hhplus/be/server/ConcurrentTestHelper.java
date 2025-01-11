package kr.hhplus.be.server;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentTestHelper {

    private ConcurrentTestHelper() {

    }

    public static void runConcurrent(int threadCount, Runnable runnable) {

        int numberOfThreads = threadCount; // 동시 실행할 스레드 수
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    latch.await(); // 모든 스레드가 준비될 때까지 대기
                    runnable.run();  // 테스트할 작업
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            latch.countDown(); // 준비 완료된 스레드 수 감소
        }

        executorService.shutdown();
    }
}
