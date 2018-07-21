/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo.impl;

import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.exception.RegexDeserializerConversionException;
import fr.whimtrip.ext.jwhthtmltopojo.intfr.HtmlDeserializer;

import java.lang.reflect.Field;

/**
 * Created by LOUISSTEIMBERG on 29/11/2017.
 */
public class StringConcatenatorAndReplacerDeserializer implements HtmlDeserializer<String> {

    private ReplacerDeserializer replacerDeserializer;
    private StringConcatenatorDeserializer concatenatorDeserializer;


    @Override
    public HtmlDeserializer<String> init(Selector selector, Field field) {
        replacerDeserializer = new ReplacerDeserializer();
        replacerDeserializer.init(selector, field);
        concatenatorDeserializer = new StringConcatenatorDeserializer();
        concatenatorDeserializer.init(selector, field);
        return this;
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
