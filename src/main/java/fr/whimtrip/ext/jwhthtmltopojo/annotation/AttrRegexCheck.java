



package fr.whimtrip.ext.jwhthtmltopojo.annotation;

import fr.whimtrip.ext.jwhthtmltopojo.impl.AcceptIfValidAttrRegexCheck;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;




/**
 *
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>
 *     Used together with {@link AcceptIfValidAttrRegexCheck},
 *     this annotation will provide a way to populate a given
 *     field only if the HTML element selected attribute value
 *     matches a custom regex.
 * </p>
 *
 * @author Louis-wht
 * @since 24/07/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD })
public @interface AttrRegexCheck {

    /**
     * @return the regex that should be matched.
     */
    String value() default "";

    /**
     * @return the attribute whose value should be looked at.
     * Selected element will be the one mentionned in {@link Selector}
     * annotation but you can provide a custom attribute to execute
     * your regex matching check.
     * @see Selector#attr()
     */
    String attr() default "class";
}
