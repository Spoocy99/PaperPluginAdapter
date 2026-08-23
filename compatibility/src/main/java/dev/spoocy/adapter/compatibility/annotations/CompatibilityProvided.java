package dev.spoocy.adapter.compatibility.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface CompatibilityProvided {

    boolean paper() default false;

    boolean spigot() default false;

}
