package au.com.cascadesoftware.util.annotation;

import java.lang.reflect.Field;

public class AnnotationException extends RuntimeException {

	private static final long serialVersionUID = -3401958030408473730L;
	
	private final Field field;

	public AnnotationException(final Field field, final String message) {
		super(message + '\n' + field.getName());
		this.field = field;
	}

	public Field getField() {
		return field;
	}

}
