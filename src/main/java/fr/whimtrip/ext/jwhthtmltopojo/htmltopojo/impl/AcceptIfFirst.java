/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.impl;

import fr.whimtrip.core.util.exception.ObjectCreationException;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.annotation.AcceptObjectIf;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.annotation.FilterFirstResultsOnly;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.intfr.AcceptIfResolver;
import org.jsoup.nodes.Element;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LOUISSTEIMBERG on 23/11/2017.
 */
public class AcceptIfFirst implements AcceptIfResolver {

    private Field field;
    private FilterFirstResultsOnly filterFirstResultsOnly;

    private static final Map<Object, Integer> fieldsStats = new HashMap();

    @Override
    public void init(Field field, Object parentObject) throws ObjectCreationException {
        this.field = field;
        filterFirstResultsOnly = field.getAnnotation(FilterFirstResultsOnly.class);

        if(filterFirstResultsOnly == null)
            throw new ObjectCreationException(field, this.getClass(), FilterFirstResultsOnly.class);

        if(!List.class.isAssignableFrom(field.getType()))
            throw new ObjectCreationException("Field " + field.getName() + " from object " + field.getDeclaringClass()
                    + " has @" + AcceptObjectIf.class.getName() + " annotation on a non " + List.class.getName() + " field.");

        if(fieldsStats.get(parentObject) == null)
            fieldsStats.put(parentObject, 1);
        else
        {
            for(Map.Entry<Object, Integer> entry : fieldsStats.entrySet())
            {
                if(entry.getKey().equals(parentObject))
                {
                    entry.setValue(entry.getValue() + 1);
                    break;
                }
            }
        }
    }

    @Override
    public boolean acceptIf(Element element, Object parentObject) {
        Integer index = fieldsStats.get(parentObject);
        return index > filterFirstResultsOnly.after()
                && fieldsStats.get(parentObject) <= filterFirstResultsOnly.before();
    }
}
