



package fr.whimtrip.ext.jwhthtmltopojo.impl;

import fr.whimtrip.core.util.exception.ObjectCreationException;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.exception.RegexDeserializerConversionException;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlDeserializer;

import java.lang.reflect.Field;

/**
 * Created by LOUISSTEIMBERG on 29/11/2017.
 */
public class StringConcatenatorAndReplacerDeserializer implements HtmlDeserializer<String> {

    private ReplacerDeserializer replacerDeserializer;
    private StringConcatenatorDeserializer concatenatorDeserializer;


    @Override
    public void init(Field field, Object parentObject, Selector selector) throws ObjectCreationException {
        replacerDeserializer = new ReplacerDeserializer();
        replacerDeserializer.init(field, parentObject, selector);
        concatenatorDeserializer = new StringConcatenatorDeserializer();
        concatenatorDeserializer.init(field, parentObject, selector);
    }

    @Override
    public String deserializePreConversion(String value) throws RegexDeserializerConversionException {
        return concatenatorDeserializer
                .deserializePreConversion(
                        replacerDeserializer.deserializePreConversion(value)
                );
    }

    @Override
    public String deserializePostConversion(String value) throws RegexDeserializerConversionException {
        return concatenatorDeserializer
                .deserializePostConversion(
                        replacerDeserializer.deserializePostConversion(value)
                );
    }
}
