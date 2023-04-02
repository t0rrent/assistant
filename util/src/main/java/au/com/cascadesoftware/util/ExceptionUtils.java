package au.com.cascadesoftware.util;

import java.util.function.Consumer;
import java.util.function.Function;

import au.com.cascadesoftware.util.exception.WrappedException;
import au.com.cascadesoftware.util.type.ThrowingConsumer;
import au.com.cascadesoftware.util.type.ThrowingFunction;

public class ExceptionUtils {

	@SafeVarargs
	public static <T, R> Function<T, R> wrap(
			final ThrowingFunction<T, R, ? extends Throwable> throwingFunction,
			final Class<? extends Throwable>... exceptions
	) {
		if (throwingFunction == null) {
			throw new NullPointerException();
		}
		return (parameter) -> {
			try {
				return throwingFunction.apply(parameter);
			} catch (final Throwable exception) {
				for (final Class<? extends Throwable> exceptionClass : exceptions) {
				    if (exceptionClass.isInstance(exception)) {
				    	throw new WrappedException(exception);
				    }
				}
				throw new IllegalStateException("The exception throw is not included in the list of exceptions", exception);
			}
		};
	}
	
	@SafeVarargs
	public static <T> Consumer<T> wrap(
			final ThrowingConsumer<T, ? extends Throwable> throwingConsumer,
			final Class<? extends Throwable>... exceptions
	) {
		if (throwingConsumer == null) {
			throw new NullPointerException();
		}
		return (parameter) -> {
			wrap(
					(ThrowingFunction<T, Void, ? extends Throwable>) (innerParameter) -> {
						throwingConsumer.apply(innerParameter);
						return null;
					},
					exceptions
			).apply(parameter);
		};
	}

}
