



package fr.whimtrip.ext.jwhthtmltopojo.adapter;

import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.exception.FieldShouldNotBeSetException;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.AcceptIfResolver;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlAdapter;
import org.jsoup.nodes.Element;

import java.lang.reflect.Field;

public class HtmlSimpleField<T> extends AbstractHtmlFieldImpl<T> {
    HtmlSimpleField(Field field, Selector selector) {
        super(field, selector);
    }



    /**
     * This method is supposed to extract from a node the value of the actual field.
     * @param htmlToPojoEngine to be used for list of POJOs fields and POJO fields in
     *                         order to retrieve the correct {@link HtmlAdapter} to create
     *                         the corresponding child elements.
     * @param node the node from which the data should be extracted.
     * @param parentObject the parent instance. This is used for list of POJOs fields and
     *                    POJO fields when instanciating the children POJOs in order to
     *                    perform the eventual code injection required.
     * @return the raw value to be set to the field
     * @throws FieldShouldNotBeSetException when the field should not be set. It happens when there is an
     *                                      {@link AcceptIfResolver} providing a {@code false} value in which
     *                                      case the field should not be set.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T getRawValue(HtmlToPojoEngine htmlToPojoEngine, Element node, T parentObject) throws FieldShouldNotBeSetException {
        Element selectedNode = selectChild(node);

        if(shouldBeFetched(node, parentObject))
        {
           return (T) instanceForNode(selectedNode, field.getType(), parentObject);
        }
        throw new FieldShouldNotBeSetException(field);
    }
}