



package fr.whimtrip.ext.jwhthtmltopojo.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>
 *     Used together with {@link Inject}, this annotation will
 *     provide a way to easily inject the field it is annotated
 *     on to any field that is annotated with an {@link Inject}
 *     annotation that has the same {@link Inject#value()} as
 *     this annotation {@link #value()}.
 * </p>
 *
 * @author Louis-wht
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD })
public @interface Injected {
    /**
     * @return the name of the injection. Any children POJO field annotated
     *         with {@link Inject} annotation with same {@link Inject#value()}
     *         will have the current annotated field value injected in it.
     *         Please make sure that {@link Inject} field and {@link Injected}
     *         fields have the same type.
     */
    String value() default "";
}