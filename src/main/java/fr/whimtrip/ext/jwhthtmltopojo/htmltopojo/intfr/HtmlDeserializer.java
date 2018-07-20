/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.intfr;

import fr.whimtrip.core.util.exception.ConstructorNotFoundException;
import fr.whimtrip.core.util.exception.ObjectCreationException;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.exception.RegexDeserializerConversionException;

import java.lang.reflect.Field;

/**
 * Created by LOUISSTEIMBERG on 19/11/2017.
 */
public interface HtmlDeserializer<T> {

    HtmlDeserializer<T> init(Selector selector, Field field) throws ObjectCreationException, ConstructorNotFoundException;

    String deserializePreConversion(String value) throws RegexDeserializerConversionException;

    T deserializePostConversion(String value) throws RegexDeserializerConversionException;

}
