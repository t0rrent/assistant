package au.com.cascadesoftware.config.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import au.com.cascadesoftware.config.hk2.extension.ConfigHK2TestExtension;
import au.com.cascadesoftware.config.model.TestConfig;
import au.com.cascadesoftware.util.annotation.AnnotationException;
import jakarta.inject.Inject;

@ExtendWith(ConfigHK2TestExtension.class)
public class ConfigLoadingServiceIT {

	private static final String TEST_CONFIG_LOCATION = "test.config";

	private static final String TEST_FAILURE_CONFIG_LOCATION = "test-fail.config";
	
	@Inject
	private ConfigService configService;

	@Test
	public void testConfigLoading() {
		final TestConfig result = configService.loadConfig(TEST_CONFIG_LOCATION, TestConfig.class);
		assertEquals(result.field1, "test");
		assertEquals(result.getField2(), true);
		assertEquals(result.getField3(), 1.5);
		assertNull(result.field4);
		assertNull(result.field4);
	}

	@Test
	public void testConfigLoadingFailure() {
		assertThrows(AnnotationException.class, () -> configService.loadConfig(TEST_FAILURE_CONFIG_LOCATION, TestConfig.class));
	}
	
}
