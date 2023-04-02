package au.com.cascadesoftware.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {
	
	public static <T> Stream<T> streamify(final Spliterator<T> spliterator) {
		return StreamSupport.stream(spliterator, false);
	}

	public static <T> Stream<T> streamify(final Iterable<T> iterable) {
		return streamify(iterable.spliterator());
	}

	/** Note: iterators cannot be reused */
	public static <T> Stream<T> streamify(final Iterator<T> iterator) {
		return streamify(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED));
	}

	public static <T> Stream<T> streamify(final Supplier<Iterator<T>> iteratorSupplier) {
		return streamify(iteratorSupplier.get());
	}
	
	public static <T, U> Function<Collection<T>, Collection<U>> nestedMapper(final Function<T, U> mapper) {
		return (collection) -> collection.stream()
				.map(mapper)
				.toList();
	}

	public static <T> Predicate<T> not(final Predicate<T> predicate) {
		return (parameter) -> {
			return !predicate.test(parameter);
		};
	}
	
}
