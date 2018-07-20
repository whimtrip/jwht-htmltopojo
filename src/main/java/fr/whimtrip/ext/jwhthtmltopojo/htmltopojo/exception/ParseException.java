/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.exception;

import java.lang.reflect.Field;
import java.util.Locale;

public class ParseException extends HtmlToPojoException {

    public ParseException(String value, Locale locale, Field field) {
        super(String.format(Locale.ENGLISH, "Cannot parse field %s.%s with value %s and locale: %s.",
                field.getName(), field.getDeclaringClass(),value, locale));
    }
}
