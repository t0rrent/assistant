package au.com.cascadesoftware.assistant;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.glassfish.hk2.utilities.Binder;

import au.com.cascadesoftware.assistant.hk2.binder.AssistantModuleBinder;
import au.com.cascadesoftware.config.hk2.binder.ConfigModuleBinder;
import au.com.cascadesoftware.engine4.Engine4;
import au.com.cascadesoftware.http.hk2.binder.HttpModuleBinder;
import au.com.cascadesoftware.json.hk2.binder.JsonModuleBinder;
import au.com.cascadesoftware.legacyui.config.WindowConfig;
import au.com.cascadesoftware.legacyui.hk2.binder.LegacyUIModuleBinder;
import au.com.cascadesoftware.openai.hk2.binder.OpenAiModuleBinder;
import au.com.cascadesoftware.util.hk2.binder.UtilModuleBinder;
import au.com.cascadesoftware.voice.hk2.binder.VoiceModuleBinder;

public class AssistantApplication {

	private static final @NonNull Collection<Supplier<Binder>> BINDERS = Arrays.asList(
			JsonModuleBinder::new,
			LegacyUIModuleBinder::new,
			VoiceModuleBinder::new,
			AssistantModuleBinder::new,
			OpenAiModuleBinder::new,
			HttpModuleBinder::new,
			ConfigModuleBinder::new,
			UtilModuleBinder::new
	);

	public static void main(String[] args) {
		final Engine4 engine = new Engine4(BINDERS);
		WindowConfig.MAIN_WINDOW_CONFIG = "assistant-window.config";
		engine.start();
	}

}
