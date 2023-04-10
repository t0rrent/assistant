package au.com.cascadesoftware.assistant.ui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import au.com.cascadesoftware.assistant.AssistantApplication;
import au.com.cascadesoftware.assistant.service.ConversationLifeCycle;
import au.com.cascadesoftware.assistant.service.TextboxAccessorService;
import au.com.cascadesoftware.config.hk2.binder.ConfigModuleBinder;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.engine4.Engine4;
import au.com.cascadesoftware.json.hk2.binder.JsonModuleBinder;
import au.com.cascadesoftware.legacyui.hk2.binder.LegacyUIShowTestBinder;
import au.com.cascadesoftware.legacyui.service.TestGUIInjectorService;
import au.com.cascadesoftware.legacyui.service.UILifeCycle;
import au.com.cascadesoftware.util.hk2.binder.UtilModuleBinder;

public class ShowTestGUIMessageView {
	
	public static void main(final String[] args) {
		final Engine4 engine = new Engine4(Arrays.asList(
				LegacyUIShowTestBinder::new,
				ConfigModuleBinder::new,
				JsonModuleBinder::new,
				UtilModuleBinder::new
		));
		AssistantApplication.initWindowConfig();
		engine.start();
		final UILifeCycle uiLifeCycle = engine.getServiceLocator().getService(UILifeCycle.class);
		interceptInjectables(engine.getServiceLocator().getService(TestGUIInjectorService.class), uiLifeCycle.getWindow());
		
		final GUIMessageView messageView = new GUIMessageView(uiLifeCycle.getWindow(), new Boundary());
		uiLifeCycle.setTopUI(messageView);
		messageView.initialize();
	}

	private static void interceptInjectables(final TestGUIInjectorService injectorService, final Window window) {
		injectorService.intercept(GUIChatbox.class, ShowTestGUIChatbox.getMockChatbox(window));
		injectorService.intercept(GUIConversationTextbox.class, getMockTextbox(window));
	}

	private static GUIConversationTextbox getMockTextbox(Window window) {
		final ConversationLifeCycle mockConversationLifeCycle = mock(ConversationLifeCycle.class);
		when(mockConversationLifeCycle.getConversation()).thenReturn(ShowTestGUIChatbox.getConversation());
		
		final TextboxAccessorService mockTextboxAccessorService = mock(TextboxAccessorService.class);
		
		final GUIConversationTextbox mockTextbox = new GUIConversationTextbox(
				window,
				new Boundary(),
				mockConversationLifeCycle,
				mockTextboxAccessorService
		);
		return mockTextbox;
	}
	
}
