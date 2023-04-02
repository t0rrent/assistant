package au.com.cascadesoftware.engine3.in;

public interface ResourceLoader {

	/** "/assets/img/" + resource   NOTE: include .png*/
	public ResourceLoader change(String resource);
	
	public String getLocation();

	void resize(int w, int h);

	public ResourceLoader combine(ResourceLoader resourceLoader2);

}
