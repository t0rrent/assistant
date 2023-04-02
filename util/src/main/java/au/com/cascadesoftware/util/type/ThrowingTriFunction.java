package au.com.cascadesoftware.util.type;

import java.util.Objects;
import java.util.function.Function;

public interface ThrowingTriFunction<T, U, V, R, E extends Throwable> {

    R apply(T t, U u, V v) throws E;


    default <W> ThrowingTriFunction<T, U, V, W, E> andThen(Function<? super R, ? extends W> after) {
        Objects.requireNonNull(after);
        return (T t, U u, V v) -> after.apply(apply(t, u, v));
    }
    
}
