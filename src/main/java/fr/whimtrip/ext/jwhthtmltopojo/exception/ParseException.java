



package fr.whimtrip.ext.jwhthtmltopojo.exception;

import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlField;
import org.jsoup.nodes.Element;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 *
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>
 *     Thrown by {@link HtmlField#getRawValue(HtmlToPojoEngine, Element, Object)} when
 *     the input pre-processed string cannot be converted into a proper raw value.
 * </p>
 *
 * @author Louis-wht
 * @since 24/07/18
 */
public class ParseException extends HtmlToPojoException {

    /**
     * Default public constructor for this exception type.
     * @param value the raw value of the field.
     * @param locale the locale used to parse/cast it.
     * @param field the field that we were trying to retrieve.
     */
    public ParseException(String value, Locale locale, Field field) {
        super(String.format(Locale.ENGLISH, "Cannot parse field %s.%s with value %s and locale: %s.",
                field.getName(), field.getDeclaringClass(),value, locale));
    }


    /**
     * Constructor used for extending exception classes.
     * @param message the message to set for this exception.
     */
    protected ParseException(String message) {
        super(message);
    }
}
