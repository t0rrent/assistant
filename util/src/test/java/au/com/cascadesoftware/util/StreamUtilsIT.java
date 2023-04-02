package au.com.cascadesoftware.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class StreamUtilsIT {
	
	private final String[] TEST_VALUES = { "a", "random", " string " };
	
	@Test
	public void streamifyIterableTest() {
		final Iterable<String> testIterable = getTestIterable();
		concatenateTest(StreamUtils.streamify(testIterable));
		concatenateTest(StreamUtils.streamify(testIterable));
	}
	
	@Test
	public void streamifyIteratorTest() {
		final Supplier<Iterator<String>> testIterator = () -> getTestIterator();
		concatenateTest(StreamUtils.streamify(testIterator));
		concatenateTest(StreamUtils.streamify(testIterator));
		concatenateTest(StreamUtils.streamify(getTestIterator()));
	}
	
	private void concatenateTest(final Stream<String> stream) {
		assertEquals(String.join("", TEST_VALUES), stream.collect(Collectors.joining()));
	}

	private Iterable<String> getTestIterable() {
		return new Iterable<String>() {
		    public Iterator<String> iterator() {
		       return getTestIterator();
		    }
		};
	}

	private Iterator<String> getTestIterator() {
		return new Iterator<String>() {
            private int pos=0;

            public boolean hasNext() {
               return TEST_VALUES.length > pos;
            }

            public String next() {
               return TEST_VALUES[pos++];
            }

            public void remove() {
                
            }
        };
	}

}
