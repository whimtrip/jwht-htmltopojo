/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo.intfr;

import fr.whimtrip.core.util.exception.ObjectCreationException;
import org.jsoup.nodes.Element;

import java.lang.reflect.Field;

/**
 * Created by LOUISSTEIMBERG on 20/11/2017.
 */
public interface AcceptIfResolver {

    void init(Field field, Object parentObject) throws ObjectCreationException;

    boolean acceptIf(Element element, Object parentObject);
}
