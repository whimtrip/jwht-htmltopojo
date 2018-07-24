







package fr.whimtrip.ext.jwhthtmltopojo.impl;

import fr.whimtrip.core.util.exception.ObjectCreationException;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.ReplaceWith;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlDeserializer;

import java.lang.reflect.Field;

/**
 *
 * Part of project jwht-htmltopojo
 *
 * This implementation provided out of the box will replace any valid
 * regex pattern matched with another static string provided on top of
 * the corresponding field with an {@link ReplaceWith} annotation.
 *
 * @author Louis-wht
 * @since 24/07/18
 */
public class ReplacerDeserializer implements HtmlDeserializer<String> {

    private ReplaceWith replaceWith;

    @Override
    public void init(Field field, Object parentObject, Selector selector) throws ObjectCreationException {

        replaceWith = field.getAnnotation(ReplaceWith.class);

        if(replaceWith == null)
            throw new ObjectCreationException(field, this.getClass(), ReplaceWith.class);
    }

    @Override
    public String deserializePreConversion(String value) {
        return value.replaceAll(replaceWith.value(), replaceWith.with());
    }

    @Override
    public String deserializePostConversion(String value) {
        return value.replaceAll(replaceWith.value(), replaceWith.with());
    }
}
