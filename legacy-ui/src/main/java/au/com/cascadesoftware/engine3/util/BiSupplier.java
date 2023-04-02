package au.com.cascadesoftware.engine3.util;

import java.util.function.Supplier;

public class BiSupplier<V, R> {
	
	private final Supplier<V> first;
	private final Supplier<R> second;
	
	public BiSupplier(Supplier<V> first, Supplier<R> second){
		this.first = first;
		this.second = second;
	}
	
	public V getFirst(){
		return first.get();
	}
	
	public R getSecond(){
		return second.get();
	}

}
