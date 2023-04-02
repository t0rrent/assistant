package au.com.cascadesoftware.engine3.gui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//import au.com.cascadesoftware.engine3.Platform;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface NativeUI {

	//Platform platform();

}
