package au.com.cascadesoftware.engine4;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

import au.com.cascadesoftware.engine4.hk2.binder.MainBinder;
import au.com.cascadesoftware.engine4.service.CriticalLifeCycle;
import au.com.cascadesoftware.engine4.service.LifeCycle;

public class Engine4 {

	public static int ALLOCATED_SCHEDULED_THREADS = 4;
	
	private final Collection<Supplier<Binder>> binders;
	private final ServiceLocator serviceLocator;
	
	public Engine4(
			@NonNull final Collection<Supplier<Binder>> binders
	) {
		this.binders = binders;
		this.serviceLocator = initializeServiceLocator();
	}
	
	public void start() {
		startLifeCycles();
	}
	
	private void startLifeCycles() {
		startLifeCycles(serviceLocator.getAllServices(CriticalLifeCycle.class));
		startLifeCycles(serviceLocator.getAllServices(LifeCycle.class));
	}

	private void startLifeCycles(final List<? extends LifeCycle> lifeCycles) {
		lifeCycles.forEach(LifeCycle::start);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> lifeCycles.forEach(LifeCycle::stop)));
	}

	private ServiceLocator initializeServiceLocator() {
		final ServiceLocator serviceLocator = ServiceLocatorUtilities.bind();
		ServiceLocatorUtilities.enableImmediateScope(serviceLocator);
		ServiceLocatorUtilities.bind(serviceLocator, new MainBinder(binders, serviceLocator));
		return serviceLocator;
	}
	
	public ServiceLocator getServiceLocator() {
		return serviceLocator;
	}
	
}
