/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo.adapter;

import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.exception.FieldShouldNotBeSetException;
import org.jsoup.nodes.Element;

import java.lang.reflect.Field;

class HtmlClassField<T> extends HtmlField<T> {
    HtmlClassField(Field field, Selector selector) {
        super(field, selector);
    }

    @Override
    public Object getRawValue(HtmlToPojoEngine htmlToPojoEngine, Element node, T parentObject) throws
            FieldShouldNotBeSetException {

        HtmlAdapter htmlAdapter = htmlToPojoEngine.adapter(field.getType());
        Element selectedNode = selectChild(node);


        if (selectedNode != null && shouldBeFetched(node, field, parentObject)) {
            return
                    htmlAdapter
                            .loadFromNode(
                                    selectedNode,
                                    htmlAdapter.createNewInstance(parentObject)
                            );

        }

        throw new FieldShouldNotBeSetException(field);
    }
}