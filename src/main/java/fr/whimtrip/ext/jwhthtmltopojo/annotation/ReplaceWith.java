



package fr.whimtrip.ext.jwhthtmltopojo.annotation;

import fr.whimtrip.ext.jwhthtmltopojo.impl.ReplacerDeserializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>
 *     Used together with {@link ReplacerDeserializer},
 *     this annotation will provide a way to easily replace
 *     matching regex patterns in the input string with
 *     static string.
 * </p>
 *
 * @author Louis-wht
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD })
public @interface ReplaceWith {

    /**
     * @return the regex pattern to replace
     */
    String value();

    /**
     * @return the static string to replace matching patterns with
     */
    String with();
}
