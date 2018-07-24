







package fr.whimtrip.ext.jwhthtmltopojo.impl;

import fr.whimtrip.core.util.exception.ObjectCreationException;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.ReplaceWith;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.exception.RegexDeserializerConversionException;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlDeserializer;

import java.lang.reflect.Field;

/**
 * Created by LOUISSTEIMBERG on 19/11/2017.
 */
public class ReplacerDeserializer implements HtmlDeserializer<String> {

    private ReplaceWith replaceWith;

    @Override
    public void init(Field field, Object parentObject, Selector selector) throws ObjectCreationException {

        replaceWith = field.getAnnotation(ReplaceWith.class);

        if(replaceWith == null)
            throw new ObjectCreationException("Field " + field.getName() + " from object " + field.getDeclaringClass()
                    + " hasn't got @" + ReplaceWith.class.getSimpleName()
                    + "annotation that should be used with class " + this.getClass());
    }

    @Override
    public String deserializePreConversion(String value) throws RegexDeserializerConversionException {
        return value.replaceAll(replaceWith.value(), replaceWith.with());
    }

    @Override
    public String deserializePostConversion(String value) throws RegexDeserializerConversionException {
        return value.replaceAll(replaceWith.value(), replaceWith.with());
    }
}
