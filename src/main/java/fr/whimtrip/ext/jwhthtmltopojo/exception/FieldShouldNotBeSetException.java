package fr.whimtrip.ext.jwhthtmltopojo.exception;

import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.AcceptIfResolver;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlField;
import org.jsoup.nodes.Element;

import java.lang.reflect.Field;



/**
 *
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>
 *     This exception is used by {@link HtmlField#getRawValue(HtmlToPojoEngine, Element, Object)}
 *     when the corresponding element was resolved as not settable because of an
 *     {@link AcceptIfResolver} marking  this field for the given input as not settable.
 *     This error should be catched by {@link HtmlField#setValue(HtmlToPojoEngine, Element, Object)}
 *     and only result in the value not being further parsed and the field remaining unset.
 * </p>
 *
 * @author Louis-wht
 * @since 24/07/18
 */
public class FieldShouldNotBeSetException extends HtmlToPojoException {

    /**
     *
     * @param field the field that should not be set for a given parent object.
     */
    public FieldShouldNotBeSetException(Field field) {
        super(String.format("Field %s should not be set.", field.getName()));
    }
}
