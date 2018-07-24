







package fr.whimtrip.ext.jwhthtmltopojo.impl;

import fr.whimtrip.core.util.exception.ObjectCreationException;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.StringConcatenator;
import fr.whimtrip.ext.jwhthtmltopojo.exception.RegexDeserializerConversionException;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlDeserializer;

import java.lang.reflect.Field;

/**
 * Created by LOUISSTEIMBERG on 19/11/2017.
 */
public class StringConcatenatorDeserializer implements HtmlDeserializer<String> {

    private StringConcatenator stringConcatenator;


    @Override
    public void init(Field field, Object parentObject, Selector selector) throws ObjectCreationException {
        stringConcatenator = field.getAnnotation(StringConcatenator.class);
        if(stringConcatenator == null)
            throw new ObjectCreationException("Please add a @StringConcatenator annotation when using StringConcatenatorDeserializer");
    }

    @Override
    public String deserializePreConversion(String value) throws RegexDeserializerConversionException {
        return stringConcatenator.before() + value + stringConcatenator.after();
    }

    @Override
    public String deserializePostConversion(String value) throws RegexDeserializerConversionException {
        return stringConcatenator.before() + value + stringConcatenator.after();
    }
}
