



package fr.whimtrip.ext.jwhthtmltopojo.exception;

import java.util.Locale;

public class DateParseException extends HtmlToPojoException {

    public DateParseException(String value, String format, Locale locale) {
        super(String.format(Locale.ENGLISH, "Cannot parse date %s with format: %s and locale: %s.", value, format, locale.toLanguageTag()));
    }
}
