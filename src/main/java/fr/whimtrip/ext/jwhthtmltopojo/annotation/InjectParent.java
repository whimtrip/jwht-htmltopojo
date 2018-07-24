



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
 *     This annotation provide an efficient way to inject parent POJO
 *     inside a child POJO before the mapping process would start.
 * </p>
 *
 * @author Louis-wht
 * @since 24/07/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD })
public @interface InjectParent {
}
