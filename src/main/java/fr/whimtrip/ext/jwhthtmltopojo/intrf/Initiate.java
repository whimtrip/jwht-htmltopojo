package fr.whimtrip.ext.jwhthtmltopojo.intrf;

import fr.whimtrip.core.util.exception.ObjectCreationException;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;

import java.lang.reflect.Field;

/**
 * This interface will be extended by other interfaces to standardize the init
 * process of the resolver so that they can easily use context vars within their
 * respective scopes.
 *
 *
 * Part of project jwht-htmltopojo
 *
 * @author Louis-wht
 * @since 24/07/18
 */
public interface Initiate {


    /**
     * Init method to prepare this resolver. This method can do nothing if all of
     * the submitted values won't be of any help for the implementing resolver.
     *
     * @param field the field concerned by the implementing class.
     * @param parentObject the parent object containing the field object.
     * @param selector the selector used by the corresponding field.
     * @throws ObjectCreationException if the provided field does not have
     *                                 one of the types required by your
     *                                 resolver or if it hasn't got any
     *                                 correct annotation for your resolver.
     */
    void init(Field field, Object parentObject, Selector selector) throws ObjectCreationException;

}
