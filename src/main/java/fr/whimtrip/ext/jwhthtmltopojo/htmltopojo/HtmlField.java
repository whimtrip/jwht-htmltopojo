/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo.htmltopojo;

import fr.whimtrip.core.util.WhimtripUtils;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.annotation.AcceptObjectIf;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.exception.DateParseException;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.exception.FieldSetException;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.exception.ParseException;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.intfr.AcceptIfResolver;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.intfr.HtmlDeserializer;
import org.joda.time.DateTime;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class HtmlField<T> {

    public static final Integer DEFAULT_NOT_FOUND_INTEGER = -1;
    public static final Float DEFAULT_NOT_FOUND_FLOAT = -1.111111f;

    Field field;
    private Selector selector;
    private String cssQuery;
    private String attribute;
    private String format;
    private static Locale locale;
    private String defValue;
    private int index;
    private int[] indexForRegexPattern;
    private boolean useDeserializer;
    private boolean preConvert;
    private boolean postConvert;
    private boolean returnDefValueOnThrow;
    private Class<? extends HtmlDeserializer> deserializer;

    HtmlField(Field field, Selector selector) {
        this.field = field;
        cssQuery = selector.value();
        attribute = selector.attr();
        format = selector.format();
        setLocaleFromTag(selector.locale());
        defValue = selector.defValue();
        index = selector.index();
        indexForRegexPattern = selector.indexForRegexPattern();
        useDeserializer = selector.useDeserializer();
        deserializer = selector.deserializer();
        preConvert = selector.preConvert();
        postConvert = selector.postConvert();
        returnDefValueOnThrow = selector.returnDefValueOnThrow();
        this.selector = selector;
    }

    private void setLocaleFromTag(String localeTag) {
        if (localeTag.equals(Selector.NO_VALUE)) {
            locale = Locale.getDefault();
        } else {
            locale = Locale.forLanguageTag(localeTag);
        }
    }

    public abstract void setValue(HtmlToPojoEngine htmlToPojoEngine, Element node, T newInstance);

    Element selectChild(Element parent) {
        return getElementAtIndexOrNull(parent);
    }

    Elements selectChildren(Element node) {
        return node.select(cssQuery);
    }

    private Element getElementAtIndexOrNull(Element parent) {

        if(cssQuery ==  null || cssQuery.length() < 1)
            return parent;

        Elements elements = selectChildren(parent);

        int size = elements.size();
        if (size == 0 || (index != -1 && size <= index))
        {
            return null;
        }

        return elements.get(index);
    }


    @SuppressWarnings("unchecked")
    <U> U instanceForNode(Element node, Class<U> clazz) {

        if (clazz.equals(Element.class)) {
            return (U) node;
        }
        String value = getValue(node, defValue, attribute);

        HtmlDeserializer<U> deserializer = null;

        if(useDeserializer)
        {
            deserializer = createDeserializer(value);
            if(preConvert)
                value = deserializer.deserializePreConversion(value);
        }

        value = editStringWithPattern(value, clazz);

        if(useDeserializer && postConvert)
        {
            return deserializer.deserializePostConversion(value);
        }
        try {
            return castValue(value, clazz);
        }catch(IllegalArgumentException e)
        {
            if(returnDefValueOnThrow)
                return castValue(defValue, clazz);
            throw new ParseException(value, locale, field);
        }
    }



    static void setFieldOrThrow(Field field, Object newInstance, Object value) {

        try {
            field.setAccessible(true);
            field.set(newInstance, value);
        } catch (IllegalAccessException e) {
            throw new FieldSetException(newInstance.getClass().getSimpleName(), field.getName());
        }
    }


    private <U> U castValue(String value, Class<U> clazz) throws IllegalArgumentException {
        return castValue(value, clazz, format);
    }

    @SuppressWarnings("unchecked")
    public static <U> U castValue(String value, Class<U> clazz, String format) throws IllegalArgumentException {

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
            DateFormat dateFormat = getDateFormat(format);
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

    private <U> String editStringWithPattern(String value, Class<U> clazz) {
        if (!clazz.equals(Date.class) && !format.equals(Selector.NO_VALUE))
        {
            Pattern pattern = Pattern.compile(format);
            Matcher matcher = pattern.matcher(value);
            boolean found = matcher.find();
            if (found)
            {
                String newValue = "";
                int i = 0;

                while((newValue == null || newValue.isEmpty()) && indexForRegexPattern.length > i ) {
                    newValue = matcher.group(indexForRegexPattern[i]);
                    i++;
                }

                value = newValue;

                if (value == null || value.isEmpty())
                {
                    value = defValue;
                }
            }
        }
        return value;
    }

    private <U> HtmlDeserializer<U> createDeserializer(String value) {
        HtmlDeserializer<U> deserializer = newInstanceOfDeserializer();
        deserializer.init(selector, field);
        return deserializer;
    }

    private <V> V newInstanceOfDeserializer() {
        return (V) WhimtripUtils.createNewInstance(deserializer);
    }

    public static String getValue(Element node, String defValue, String attribute) {
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

    private static DateFormat getDateFormat(String format) {
        if (Selector.NO_VALUE.equals(format)) {
            return DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        } else {
            return new SimpleDateFormat(format, locale);
        }
    }


    protected <T extends AcceptIfResolver> boolean shouldBeFetched(Element e, Field f, Object parentObject){
        AcceptObjectIf acceptObjectIf = f.getAnnotation(AcceptObjectIf.class);

        if(acceptObjectIf == null)
            return true;


        for(Class<? extends AcceptIfResolver> resolverClazz : acceptObjectIf.value())
        {
            T resolver = (T) WhimtripUtils.createNewInstance(resolverClazz);

            resolver.init(field, parentObject);
            if(!resolver.acceptIf(e, parentObject))
                return false;
        }

        return true;
    }
}
