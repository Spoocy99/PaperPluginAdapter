package dev.spoocy.adapter.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LogAs {

    String value();

    boolean explicit() default true;

}
