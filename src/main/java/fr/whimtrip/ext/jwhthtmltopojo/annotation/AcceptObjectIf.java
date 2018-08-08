



package fr.whimtrip.ext.jwhthtmltopojo.annotation;

import fr.whimtrip.ext.jwhthtmltopojo.intrf.AcceptIfResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>
 *     Used together with a custom or provided {@link AcceptIfResolver},
 *     this annotation will provide a way to easily accept or refuse
 *     elements in a list or fields to be set (depending on if it a List
 *     field or not).
 * </p>
 *
 * @author Louis-wht
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD })
public @interface AcceptObjectIf {

    /**
     * @return the {@link AcceptIfResolver} to use to know if
     * the field can be set / value can be added to the list.
     *
     * Most of the time, an AcceptIfResolver will require another
     * custom annotation to operate properly. You can provide
     * your own implementation of an {@link AcceptIfResolver}.
     */
    Class<? extends AcceptIfResolver>[] value() default AcceptIfResolver.class;

}
