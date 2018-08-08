



package fr.whimtrip.ext.jwhthtmltopojo.annotation;

import fr.whimtrip.ext.jwhthtmltopojo.impl.StringConcatenatorDeserializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>
 *     Used together with {@link StringConcatenatorDeserializer},
 *     this annotation will provide a way to easily concatenate
 *     static string to the input string, pre or post conversion.
 * </p>
 *
 * @author Louis-wht
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD })
public @interface StringConcatenator {

    /**
     * @return the static string that should be added before the input string.
     */
    String before() default "";

    /**
     * @return the static string that should be added after the input string.
     */
    String after() default "";
}
