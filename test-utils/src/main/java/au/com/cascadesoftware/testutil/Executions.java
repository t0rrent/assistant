package au.com.cascadesoftware.testutil;

import java.util.ArrayList;
import java.util.List;

public class Executions {

	public static void multiThreadedRepetitionTest(final int numberOfThreads, final int repetitions, final Runnable runnable) throws InterruptedException {
		final List<Thread> threads = new ArrayList<>();
		for (int i = 0; i < numberOfThreads; i++) {
			final Thread thread = new Thread(() -> {
				for (int j = 0; j < repetitions; j++) {
					runnable.run();
				}
			});
			thread.start();
			threads.add(thread);
		}
		for (final Thread thread : threads) {
			thread.join();
		}
	}
	
}
