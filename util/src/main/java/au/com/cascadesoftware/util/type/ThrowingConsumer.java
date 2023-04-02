package au.com.cascadesoftware.util.type;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Throwable> {
	
	void apply(T value) throws E;

}
