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

package fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.impl;

import fr.whimtrip.core.util.exception.ObjectCreationException;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.annotation.ReplaceWith;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.exception.RegexDeserializerConversionException;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.intfr.HtmlDeserializer;

import java.lang.reflect.Field;

/**
 * Created by LOUISSTEIMBERG on 19/11/2017.
 */
public class ReplacerDeserializer implements HtmlDeserializer<String> {

    private ReplaceWith replaceWith;

    @Override
    public HtmlDeserializer<String> init(Selector selector, Field field) {

        replaceWith = field.getAnnotation(ReplaceWith.class);

        if(replaceWith == null)
            throw new ObjectCreationException("Field " + field.getName() + " from object " + field.getDeclaringClass()
                    + " hasn't got @" + ReplaceWith.class.getSimpleName()
                    + "annotation that should be used with class " + this.getClass());

        return this;
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
