



package fr.whimtrip.ext.jwhthtmltopojo.impl;

import fr.whimtrip.core.util.exception.ObjectCreationException;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.AcceptObjectIf;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.FilterFirstResultsOnly;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.AcceptIfResolver;
import org.jsoup.nodes.Element;

import java.lang.reflect.Field;
import java.util.List;

/**
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>Example implementation of an  {@link AcceptIfResolver}.</p>
 *
 * <p>
 *     This one will only keep some results out of all inside a given List
 *     of elements.  You can basically give a start and an end index to pick
 *     from the list. Very useful when you only need for example the first
 *     three elements of a list for example.
 * </p>
 *
 * <p>
 *     You have to provide an {@link FilterFirstResultsOnly} annotation on top of
 *     the corresponding field in order for this deserializer to work properly.
 * </p>
 *
 * @author Louis-wht
 * @since 24/07/18
 */
public class AcceptIfFirst implements AcceptIfResolver {

    private FilterFirstResultsOnly filterFirstResultsOnly;

    private int index = 0;

    @Override
    public void init(Field field, Object parentObject, Selector selector) throws ObjectCreationException {
        filterFirstResultsOnly = field.getAnnotation(FilterFirstResultsOnly.class);

        if(filterFirstResultsOnly == null)
            throw new ObjectCreationException(field, this.getClass(), FilterFirstResultsOnly.class);

        if(!List.class.isAssignableFrom(field.getType()))
            throw new ObjectCreationException("Field " + field.getName() + " from object " + field.getDeclaringClass()
                    + " has @" + AcceptObjectIf.class.getName() + " annotation on a non " + List.class.getName() + " field.");

        index ++;
    }

    @Override
    public boolean accept(Element element, Object parentObject) {
        return     index > filterFirstResultsOnly.start()
                && index <= filterFirstResultsOnly.end();
    }
}
