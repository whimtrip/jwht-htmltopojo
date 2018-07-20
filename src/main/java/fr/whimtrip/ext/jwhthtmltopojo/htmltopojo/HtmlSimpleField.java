/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo.htmltopojo;

import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.annotation.Selector;
import org.jsoup.nodes.Element;

import java.lang.reflect.Field;

class HtmlSimpleField<T> extends HtmlField<T> {
    HtmlSimpleField(Field field, Selector selector) {
        super(field, selector);
    }

    @Override
    public void setValue(HtmlToPojoEngine htmlToPojoEngine, Element node, T newInstance) {
        Element selectedNode = selectChild(node);

        if(shouldBeFetched(node, field, newInstance))
        {
            setFieldOrThrow(field, newInstance, instanceForNode(selectedNode, field.getType()));
        }
    }
}