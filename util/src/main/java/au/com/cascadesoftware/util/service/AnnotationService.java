package au.com.cascadesoftware.util.service;

import au.com.cascadesoftware.util.annotation.AnnotationException;

public interface AnnotationService {

	<T> T processAnnotations(T unprocessed) throws AnnotationException;

}
