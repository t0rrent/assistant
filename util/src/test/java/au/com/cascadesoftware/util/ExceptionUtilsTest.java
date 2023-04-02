package au.com.cascadesoftware.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import au.com.cascadesoftware.util.exception.WrappedException;
import au.com.cascadesoftware.util.type.ThrowingConsumer;
import au.com.cascadesoftware.util.type.ThrowingFunction;

public class ExceptionUtilsTest {
	
	@Test
	public void testWrapFunction() {
		final ThrowingFunction<Integer, Integer, Throwable> throwingFunction = getExampleThrowingFunction();
		final Function<Integer, Integer> result = ExceptionUtils.wrap(
				throwingFunction,
				IllegalArgumentException.class,
				IllegalStateException.class
		);
		assertEquals(result.apply(0), 0);
		testWrappedException(IllegalArgumentException.class, () -> result.apply(1));
		testWrappedException(IllegalStateException.class, () -> result.apply(2));

		final Function<Integer, Integer> result2 = ExceptionUtils.wrap(
				throwingFunction,
				IllegalArgumentException.class,
				null,
				IllegalStateException.class
		);
		testWrappedException(IllegalArgumentException.class, () -> result2.apply(1));
	}
	
	@Test
	public void testInvalidWrapFunction() {
		final ThrowingFunction<Integer, Integer, Throwable> throwingFunction = getExampleThrowingFunction();
		final Function<Integer, Integer> result = ExceptionUtils.wrap(throwingFunction);
		assertThrows(IllegalStateException.class, () -> result.apply(1));
		assertThrows(NullPointerException.class, () -> ExceptionUtils.wrap((ThrowingFunction<?, ?, ?>) null));
	}
	
	@Test
	public void testWrapConsumer() {
		final ThrowingConsumer<Integer, Throwable> throwingConsumer = getExampleThrowingConsumer();
		final Consumer<Integer> result = ExceptionUtils.wrap(
				throwingConsumer,
				IllegalArgumentException.class,
				IllegalStateException.class
		);
		assertDoesNotThrow(() -> result.accept(0));
		testWrappedException(IllegalArgumentException.class, () -> result.accept(1));
		testWrappedException(IllegalStateException.class, () -> result.accept(2));

		final Consumer<Integer> result2 = ExceptionUtils.wrap(
				throwingConsumer,
				IllegalArgumentException.class,
				null,
				IllegalStateException.class
		);
		testWrappedException(IllegalArgumentException.class, () -> result2.accept(1));
	}
	
	@Test
	public void testInvalidWrapConsumer() {
		final ThrowingConsumer<Integer, Throwable> throwingConsumer = getExampleThrowingConsumer();
		final Consumer<Integer> result = ExceptionUtils.wrap(throwingConsumer);
		assertThrows(IllegalStateException.class, () -> result.accept(1));
		assertThrows(NullPointerException.class, () -> ExceptionUtils.wrap((ThrowingConsumer<?, ?>) null));
	}

	private void testWrappedException(final Class<? extends Throwable> exceptionType, final Runnable result) {
		try {
			result.run();
		} catch (final WrappedException wrappedException) {
			assertInstanceOf(exceptionType, wrappedException.getCause());
		}
	}

	private ThrowingFunction<Integer, Integer, Throwable> getExampleThrowingFunction() {
		return (i) -> {
			if (i == 1) {
				throw new IllegalArgumentException("test exception 1");
			} else if (i == 2) {
				throw new IllegalStateException("test exception 2");
			} else {
				return i;
			}
		};
	}

	private ThrowingConsumer<Integer, Throwable> getExampleThrowingConsumer() {
		return (i) -> {
			if (i == 1) {
				throw new IllegalArgumentException("test exception 1");
			} else if (i == 2) {
				throw new IllegalStateException("test exception 2");
			}
		};
	}

}
