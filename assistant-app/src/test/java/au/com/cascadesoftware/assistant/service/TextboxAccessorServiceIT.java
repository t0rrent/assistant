package au.com.cascadesoftware.assistant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import au.com.cascadesoftware.assistant.hk2.extension.AssistantHK2TestExtension;
import au.com.cascadesoftware.engine3.gui.GUITextboxDesktop;
import jakarta.inject.Inject;

@ExtendWith(AssistantHK2TestExtension.class)
public class TextboxAccessorServiceIT {

	@Inject
	private TextboxAccessorService textboxAcccessorService;
	
	@Test
	public void testTextboxControl() {
		final GUITextboxDesktop mockTextbox = getMockTextbox();
		textboxAcccessorService.setTextbox(mockTextbox);
		textboxAcccessorService.append('s');
		textboxAcccessorService.backspace();
		assertEquals("test", textboxAcccessorService.getContent());
		verify(mockTextbox, atLeast(3)).getInputText();
		verify(mockTextbox, atLeast(2)).setInputText(anyString());
		
	}

	private GUITextboxDesktop getMockTextbox() {
		final GUITextboxDesktop mockTextbox = mock(GUITextboxDesktop.class);
		when(mockTextbox.getInputText()).thenReturn("test");
		return mockTextbox;
	}
	
}
