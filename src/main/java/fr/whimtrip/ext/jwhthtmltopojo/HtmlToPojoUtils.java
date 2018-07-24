



package fr.whimtrip.ext.jwhthtmltopojo;

import fr.whimtrip.ext.jwhthtmltopojo.adapter.HtmlSimpleField;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.exception.DateParseException;
import org.joda.time.DateTime;
import org.jsoup.nodes.Element;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * jwht-htmltopojo Utils class.
 *
 * Part of project jwht-htmltopojo
 *
 * @author Louis-wht
 * @since 24/07/18
 */
public class HtmlToPojoUtils {


    public static final Integer DEFAULT_NOT_FOUND_INTEGER = -1;
    public static final Float DEFAULT_NOT_FOUND_FLOAT = -1.111111f;

    /**
     *
     * @param clazz the class to analyze
     * @return wether the corresponding type is considered simple or not. It's used
     *         under the hood to know if a {@link HtmlSimpleField} can be instanciated
     *         to parse the corresponding field.
     */
    public static boolean isSimple(Class clazz) {
        return clazz.equals(String.class) ||
                clazz.equals(Integer.class) || clazz.equals(int.class) ||
                clazz.equals(Long.class) || clazz.equals(long.class) ||
                clazz.equals(Float.class) || clazz.equals(float.class) ||
                clazz.equals(Double.class) || clazz.equals(double.class) ||
                clazz.equals(Boolean.class) || clazz.equals(boolean.class) ||
                clazz.equals(Element.class) ||
                clazz.equals(List.class) ||
                isDate(clazz);
    }


    /**
     *
     * @param clazz the class to analyse
     * @return a boolean indicating wether it's a date class or not.
     */
    public static boolean isDate(Class clazz) {
        return clazz.equals(Date.class) || clazz.equals(DateTime.class);
    }

    /**
     * This method will cast a String raw value into a simple type
     * @see #isSimple(Class)
     * @param value the raw string value to cast
     * @param clazz the class to cast the value into
     * @param format the format to use (if it's a date)
     * @param locale the locale to use (if it's a date)
     * @param <U> the type to cast the raw value in
     * @return the casted value ready to be set to its corresponding field
     * @throws DateParseException if parsing the date resulted in a parsing
     *                            because of an invalid format/locale.
     * @throws IllegalArgumentException if parsing the string to another class
     *                                  resulted in an IllegalArgumentException.
     *                                  For example if the string is "example" and
     *                                  needs to be casted to an int, an
     *                                  IllegalArgumentException will be thrown.
     */
    @SuppressWarnings("unchecked")
    public static <U> U castValue(String value, Class<U> clazz, String format, Locale locale)
            throws DateParseException,  IllegalArgumentException
    {

        if (clazz.equals(String.class)) {
            return (U) value;
        }
        if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            if(value.equals(Selector.NO_VALUE))
                return (U) DEFAULT_NOT_FOUND_INTEGER;
            return (U) Integer.valueOf(value);
        }

        if (clazz.equals(Long.class) || clazz.equals(long.class)) {
            if(value.equals(Selector.NO_VALUE))
                return (U) Long.valueOf(DEFAULT_NOT_FOUND_INTEGER);
            return (U) Long.valueOf(value);
        }

        if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            if(value.equals(Selector.NO_VALUE))
                return (U) Boolean.valueOf(false);
            return (U) Boolean.valueOf(value);
        }

        if (clazz.equals(Date.class) || clazz.equals(DateTime.class)) {
            DateFormat dateFormat = getDateFormat(format, locale);
            try {
                Date date = dateFormat.parse(value);

                if(clazz.equals(DateTime.class))
                    return (U) new DateTime(date);

                return (U) date;

            } catch (java.text.ParseException e) {
                throw new DateParseException(value, format, locale);
            }
        }

        if (clazz.equals(Float.class) || clazz.equals(float.class)) {
            if(value.equals(Selector.NO_VALUE))
                return (U) DEFAULT_NOT_FOUND_FLOAT;
            return (U) Float.valueOf(Float.parseFloat(value));
        }

        if (clazz.equals(Double.class) || clazz.equals(double.class)) {
            if(value.equals(Selector.NO_VALUE))
                return (U)Double.valueOf(DEFAULT_NOT_FOUND_FLOAT);
            return (U) Double.valueOf(Double.parseDouble(value));
        }

        return (U) value;
    }


    /**
     * Extract the raw untransformed value of a sub node.
     * @param node the subnode to extract the data from
     * @param defValue the default value in case the node is null.
     * @param attribute the attribute to pick from this node.
     * @return the resulting raw untransformed extracted string value.
     */
    public static String extractRawValue(Element node, String defValue, String attribute) {
        if (node == null) {
            return defValue;
        }
        String value;
        switch (attribute) {
            case "":
                value = node.text();
                break;
            case "html":
            case "innerHtml":
                value = node.html();
                break;
            case "outerHtml":
                value = node.outerHtml();
                break;
            default:
                value = node.attr(attribute);
                break;
        }
        return value;
    }

    /**
     * Instanciate a date format out of a string format
     * @param format the format to build a DateFormat for
     * @param locale the locale to use
     * @return the built DateFormat
     */
    public static DateFormat getDateFormat(String format, Locale locale) {
        if (Selector.NO_VALUE.equals(format)) {
            return DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        } else {
            return new SimpleDateFormat(format, locale);
        }
    }


    /**
     * @param localeTag the tag to find a locale for.
     * @return locale corresponding to the input locale tag.
     */
    public static Locale getLocaleFromTag(String localeTag) {
        if (localeTag.equals(Selector.NO_VALUE)) {
            return Locale.getDefault();
        } else {
            return Locale.forLanguageTag(localeTag);
        }
    }

}
