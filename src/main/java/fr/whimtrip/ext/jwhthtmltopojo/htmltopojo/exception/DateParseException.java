/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.exception;

import java.util.Locale;

public class DateParseException extends HtmlToPojoException {

    public DateParseException(String value, String format, Locale locale) {
        super(String.format(Locale.ENGLISH, "Cannot parse date %s with format: %s and locale: %s.", value, format, locale.toLanguageTag()));
    }
}
