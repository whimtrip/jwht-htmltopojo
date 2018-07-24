



package fr.whimtrip.ext.jwhthtmltopojo.exception;

import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoUtils;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.Locale;



/**
 *
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>
 *     Thrown by {@link HtmlToPojoUtils#castValue(String, Class, String, Locale)} when
 *     the input pre-processed string cannot be converted into a proper {@link Date} /
 *     {@link DateTime} (or any other supported date field, see {@link HtmlToPojoUtils#isDate(Class)}).
 *
 *     This is either caused by a pattern misuse, a locale and pattern problem or a bad
 *     string scrapping resulting in an unparsable Date.
 * </p>
 *
 * @author Louis-wht
 * @since 24/07/18
 */
public class DateParseException extends ParseException {

    /**
     *
     * @param value the pre-processed value that could not be parsed
     * @param format the format of the date expected
     * @param locale the locale of the date format expected
     */
    public DateParseException(String value, String format, Locale locale) {
        super(String.format(Locale.ENGLISH, "Cannot parse date %s with format: %s and locale: %s.", value, format, locale.toLanguageTag()));
    }
}
