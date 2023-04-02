package au.com.cascadesoftware.config.hk2.factory;

import org.glassfish.hk2.api.Factory;

import au.com.cascadesoftware.config.service.ConfigService;

public abstract class AbstractConfigFactory<T> implements Factory<T> {
	
	private final ConfigService configService;
	
	protected AbstractConfigFactory(final ConfigService configService) {
		this.configService = configService;
	}

	@Override
	public T provide() {
		return configService.loadConfig(getLocation(), getType());
	}
	
	@Override
	public void dispose(final T instance) {
	}

	protected abstract String getLocation();

	protected abstract Class<T> getType();
	
}
