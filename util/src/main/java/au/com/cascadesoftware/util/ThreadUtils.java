package au.com.cascadesoftware.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadUtils {

	public static <T, V> Function<T, V> synchronize(final Function<T, V> unsynchronized) {
		return (t) -> {
			synchronized (ThreadUtils.class) {
				return unsynchronized.apply(t);
			}
		};
	}

	public static <T> Consumer<T> synchronize(final Consumer<T> unsynchronized) {
		return (t) -> {
			synchronized (ThreadUtils.class) {
				unsynchronized.accept(t);
			}
		};
	}

	public static <T> Supplier<T> synchronize(final Supplier<T> unsynchronized) {
		return () -> {
			synchronized (ThreadUtils.class) {
				return unsynchronized.get();
			}
		};
	}

	public static Runnable synchronize(final Runnable unsynchronized) {
		return () -> {
			synchronized (ThreadUtils.class) {
				unsynchronized.run();
			}
		};
	}

}
