/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo.impl;

import fr.whimtrip.core.util.exception.ObjectCreationException;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.StringConcatenator;
import fr.whimtrip.ext.jwhthtmltopojo.exception.RegexDeserializerConversionException;
import fr.whimtrip.ext.jwhthtmltopojo.intfr.HtmlDeserializer;

import java.lang.reflect.Field;

/**
 * Created by LOUISSTEIMBERG on 19/11/2017.
 */
public class StringConcatenatorDeserializer implements HtmlDeserializer<String> {

    private StringConcatenator stringConcatenator;

    @Override
    public HtmlDeserializer<String> init(Selector selector, Field field) throws ObjectCreationException {
        stringConcatenator = field.getAnnotation(StringConcatenator.class);
        if(stringConcatenator == null)
            throw new ObjectCreationException("Please add a @StringConcatenator annotation when using StringConcatenatorDeserializer");
        return this;
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
