package au.com.cascadesoftware.engine3.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderedHashMap<KEY, VALUE> extends HashMap<KEY, VALUE> {

	private static final long serialVersionUID = -1117024552864139729L;
	
	private List<KEY> order;
	
	public OrderedHashMap(){
		order = new ArrayList<KEY>();
	}
	
	public OrderedHashMap(int initialCapacity){
		super(initialCapacity);
		order = new ArrayList<KEY>();
	}
	
	public OrderedHashMap(int initialCapacity, float loadFactor){
		super(initialCapacity, loadFactor);
		order = new ArrayList<KEY>();
	}
	
	public OrderedHashMap(Map<? extends KEY,? extends VALUE> m){
		super(m);
		order = new ArrayList<KEY>();
	}
	
	@Override
	public VALUE put(KEY key, VALUE value) {
		if(containsKey(key)){
			order.remove(getOrderOf(key));
		}
		order.add(key);
		return super.put(key, value);
	}
	
	@Override
	public VALUE remove(Object key) {
		int i = getOrderOf(key);
		if(i != -1) order.remove(i);
		return super.remove(key);
	}
	
	public KEY getFirst(){
		return getOrdered(0);
	}
	
	public KEY getLast(){
		return getOrdered(order.size() - 1);
	}

	public KEY getOrdered(int i) {
		return order.get(i);
	}
	
	public int getSize(){
		return order.size();
	}
	
	@Override
	public void clear() {
		order.clear();
		super.clear();
	}

	private int getOrderOf(Object key) {
		int out = -1;
		for(int i = 0; i < order.size(); i++){
			if(order.get(i).equals(key) || order.get(i) == key){
				out = i;
				break;
			}
		}
		return out;
	}

}
