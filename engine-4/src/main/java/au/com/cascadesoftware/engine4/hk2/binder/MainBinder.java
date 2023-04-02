package au.com.cascadesoftware.engine4.hk2.binder;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import au.com.cascadesoftware.engine4.Engine4;

public class MainBinder extends AbstractBinder {
	
	private final Collection<Supplier<Binder>> binders;
	private final ServiceLocator serviceLocator;

	public MainBinder(
			final Collection<Supplier<Binder>> binders, 
			final ServiceLocator serviceLocator
	) {
		this.binders = binders;
		this.serviceLocator = serviceLocator;
	}

	@Override
	protected void configure() {
		this.binders.stream()
				.map(Supplier::get)
				.forEach(this::install);
		
		bind(Executors.newScheduledThreadPool(Engine4.ALLOCATED_SCHEDULED_THREADS)).to(ScheduledExecutorService.class);
		bind(Executors.newCachedThreadPool()).to(ExecutorService.class);
		bind(serviceLocator).to(ServiceLocator.class);
	}

}
