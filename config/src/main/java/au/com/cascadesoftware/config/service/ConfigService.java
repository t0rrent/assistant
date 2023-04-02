package au.com.cascadesoftware.config.service;

public interface ConfigService {

	<T> T loadConfig(String location, Class<T> type);

}
