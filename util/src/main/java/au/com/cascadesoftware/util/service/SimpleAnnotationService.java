package au.com.cascadesoftware.util.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import au.com.cascadesoftware.util.annotation.AnnotationException;
import au.com.cascadesoftware.util.annotation.NonNull;

public class SimpleAnnotationService implements AnnotationService {

	@Override
	public <T> T processAnnotations(final T unprocessed) throws AnnotationException {
		for (final Field field : unprocessed.getClass().getFields()) {
			if (field.isEnumConstant() || Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			final Annotation[] annotations = field.getAnnotations();
			try {
				Object fieldValue = field.get(unprocessed);
				Object newFieldValue = applyAnnotations(fieldValue, annotations, field);
				if (fieldValue != newFieldValue) {
					field.set(unprocessed, newFieldValue);
				}
				if (newFieldValue != null) {
					processAnnotations(newFieldValue);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return unprocessed;
	}

	private Object applyAnnotations(final Object fieldValue, final Annotation[] annotations, final Field field) throws AnnotationException {
		for (final Annotation annotation : annotations) {
			if (annotation.annotationType().equals(NonNull.class)) {
				if (fieldValue == null) {
					throw new AnnotationException("NonNull field {} is null".formatted(field));
				}
			}
		}
		return fieldValue;
	}

}
