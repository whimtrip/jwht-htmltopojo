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

class HtmlClassField<T> extends HtmlField<T> {
    HtmlClassField(Field field, Selector selector) {
        super(field, selector);
    }

    @Override
    public void setValue(HtmlToPojoEngine htmlToPojoEngine, Element node, T parentObject) {

        HtmlAdapter htmlAdapter = htmlToPojoEngine.adapter(field.getType());
        Element selectedNode = selectChild(node);


        if (selectedNode != null && shouldBeFetched(node, field, parentObject)) {
            setFieldOrThrow(
                    // Parent field
                    field,
                    parentObject,
                    // New instance of child object
                    htmlAdapter
                            .loadFromNode(
                                    selectedNode,
                                    htmlAdapter.createNewInstance(parentObject)
                            )
            );
        }


    }
}