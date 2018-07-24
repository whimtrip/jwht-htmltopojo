







package fr.whimtrip.ext.jwhthtmltopojo.impl;

import fr.whimtrip.core.util.exception.ObjectCreationException;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.StringConcatenator;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlDeserializer;

import java.lang.reflect.Field;

/**
 * Part of project jwht-htmltopojo
 *
 * <p>
 *     This is an example implementation of an {@link HtmlDeserializer}
 *     provided out of the box. This one will concatenate your string
 *     with a static before / after value. You have to provide an
 *     {@link StringConcatenator} annotation on top of the corresponding
 *     field in order for it to work properly.
 * </p>
 * <p>
 *     This can be particularly helpful if you're trying to use a link
 *     to another HTTP ressource and an id is hidden somewhere in a HTML
 *     tag. You can then concatenate before and after this id to build a
 *     full valid url.
 * </p>
 *
 * @author Louis-wht
 * @since 24/07/18
 */
public class StringConcatenatorDeserializer implements HtmlDeserializer<String> {

    private StringConcatenator stringConcatenator;


    @Override
    public void init(Field field, Object parentObject, Selector selector) throws ObjectCreationException {
        stringConcatenator = field.getAnnotation(StringConcatenator.class);
        if(stringConcatenator == null)
            throw new ObjectCreationException(field, this.getClass(), StringConcatenator.class);
    }

    @Override
    public String deserializePreConversion(String value) {
        return stringConcatenator.before() + value + stringConcatenator.after();
    }

    @Override
    public String deserializePostConversion(String value) {
        return stringConcatenator.before() + value + stringConcatenator.after();
    }
}
