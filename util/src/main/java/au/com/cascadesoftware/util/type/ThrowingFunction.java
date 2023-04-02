package au.com.cascadesoftware.util.type;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {
	
	R apply(T value) throws E;

}
