



package fr.whimtrip.ext.jwhthtmltopojo.annotation;

import fr.whimtrip.ext.jwhthtmltopojo.impl.AcceptIfFirst;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 *
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>
 *     Used together with {@link AcceptIfFirst}, this annotation
 *     will provide a way to easily accept elements in a list of
 *     elements that remains within a certain range of indexes.
 * </p>
 *
 * @author Louis-wht
 * @since 24/07/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD })
public @interface FilterFirstResultsOnly {

    /**
     * @return the last index that should be used. The element
     * with this index will be the first one skipped. Index
     * starts at 0. For example, if there {@code start = 0, end = 2}
     * and you are parsing the 3rd element, as it has an index
     * of 2, this will be the first element skipped because only
     * {@code 2 - 0} elements will be kept here.
     */
    int end();

    /**
     * @return the first index that should be used. 0 means none will be skipped. Index starts at 0.
     */
    int start() default 0;
}
