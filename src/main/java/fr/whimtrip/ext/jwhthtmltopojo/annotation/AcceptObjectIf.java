



package fr.whimtrip.ext.jwhthtmltopojo.annotation;

import fr.whimtrip.ext.jwhthtmltopojo.intrf.AcceptIfResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by LOUISSTEIMBERG on 20/11/2017.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD })
public @interface AcceptObjectIf {

    Class<? extends AcceptIfResolver>[] value() default AcceptIfResolver.class;

}
