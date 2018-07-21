/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo;

import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.exception.FieldShouldNotBeSetException;
import org.jsoup.nodes.Element;

import java.lang.reflect.Field;

class HtmlSimpleField<T> extends HtmlField<T> {
    HtmlSimpleField(Field field, Selector selector) {
        super(field, selector);
    }

    @Override
    public Object getRawValue(HtmlToPojoEngine htmlToPojoEngine, Element node, T newInstance) throws FieldShouldNotBeSetException {
        Element selectedNode = selectChild(node);

        if(shouldBeFetched(node, field, newInstance))
        {
           return instanceForNode(selectedNode, field.getType());
        }
        throw new FieldShouldNotBeSetException(field);
    }
}