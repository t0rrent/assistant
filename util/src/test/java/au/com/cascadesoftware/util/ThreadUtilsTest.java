package au.com.cascadesoftware.util;

import static au.com.cascadesoftware.testutil.Executions.multiThreadedRepetitionTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

public class ThreadUtilsTest {
	
	private static final int NUM_THREADS = 64;
	private static final int NUM_ITERATIONS = 1000;
	
	@Test
	public void testSynchronizedFunction() throws InterruptedException {
    	testSynchronized((counter) -> {
			final Function<Object, Integer> synchronizedFunction = ThreadUtils.synchronize(
					(Function<Object, Integer>) ((v) -> counter.incrementAndGet())
			);
    		return () -> synchronizedFunction.apply(null);
		});
	}
	
	@Test
	public void testSynchronizedRunnable() throws InterruptedException {
    	testSynchronized((counter) -> {
			final Runnable synchronizedRunnable = ThreadUtils.synchronize(() -> counter.increment());
    		return synchronizedRunnable;
		});
	}
	
	@Test
	public void testSynchronizedConsumer() throws InterruptedException {
    	testSynchronized((counter) -> {
			final Consumer<Object> synchronizedFunction = ThreadUtils.synchronize(
					(Consumer<Object>) ((v) -> counter.increment())
			);
    		return () -> synchronizedFunction.accept(null);
		});
	}
	
	@Test
	public void testSynchronizedSupplier() throws InterruptedException {
    	testSynchronized((counter) -> {
			final Supplier<Integer> synchronizedFunction = ThreadUtils.synchronize(
					(Supplier<Integer>) (() -> counter.incrementAndGet())
			);
    		return () -> synchronizedFunction.get();
		});
	}

	@Test
	public void testUnsynchronized() throws InterruptedException {
    	final Counter unsynchronizedCounter = new Counter();
    	multiThreadedRepetitionTest(NUM_THREADS, NUM_ITERATIONS, () -> unsynchronizedCounter.increment());
        assertNotEquals(NUM_THREADS * NUM_ITERATIONS, unsynchronizedCounter.getValue());
	}
	
	private void testSynchronized(final Function<Counter, Runnable> incrementerTransform) throws InterruptedException {
    	final Counter synchronizedCounter = new Counter();
    	final Runnable incrementer = incrementerTransform.apply(synchronizedCounter);
    	multiThreadedRepetitionTest(NUM_THREADS, NUM_ITERATIONS, incrementer);
        assertEquals(NUM_THREADS * NUM_ITERATIONS, synchronizedCounter.getValue());
	}
	
	private class Counter {
		
	    int value = 0;
	    
	    void increment() {
	    	value++;
	    }
	    
	    int incrementAndGet() {
	    	return value++;
	    }
	    
	    int getValue() {
	    	return value;
	    }
	    
	}

}
