



package fr.whimtrip.ext.jwhthtmltopojo.adapter;

import fr.whimtrip.core.util.WhimtripUtils;
import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;
import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoUtils;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.AcceptObjectIf;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.exception.DateParseException;
import fr.whimtrip.ext.jwhthtmltopojo.exception.FieldSetException;
import fr.whimtrip.ext.jwhthtmltopojo.exception.FieldShouldNotBeSetException;
import fr.whimtrip.ext.jwhthtmltopojo.exception.ParseException;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.AcceptIfResolver;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlAdapter;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlDeserializer;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlField;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 *
 * Default implementation of {@link HtmlField}. This defines an implementing abstract class
 * with three implementing classes for simple fields {@link HtmlSimpleField}, POJO fields
 * {@link HtmlClassField} and List field {@link HtmlListField}. This helps a lot to implement
 * custom logic depending on the type of field we have to set.
 *
 *
 * @param <T> the type of the field to assign a value to.
 */
public abstract class AbstractHtmlFieldImpl<T> implements HtmlField<T> {

    public static final Integer DEFAULT_NOT_FOUND_INTEGER = -1;
    public static final Float DEFAULT_NOT_FOUND_FLOAT = -1.111111f;

    private final Field field;
    private final Selector selector;
    private final String cssQuery;
    private final String attribute;
    private final String format;
    private final String dateFormat;
    private final String defValue;
    private final int index;
    private final int[] indexForRegexPattern;
    private final boolean useDeserializer;
    private final boolean preConvert;
    private boolean postConvert;
    private final boolean returnDefValueOnThrow;
    private final Class<? extends HtmlDeserializer> deserializer;

    private Locale locale = Locale.getDefault();

    /**
     * This constructor will read through a {@link Selector} annotation
     * in order to populate its inner values.
     * @param field the field to set values to.
     * @param selector the selector for this given field.
     */
    AbstractHtmlFieldImpl(Field field, Selector selector) {
        this.field = field;
        cssQuery = selector.value();
        attribute = selector.attr();
        format = selector.format();
        dateFormat = selector.dateFormat();
        locale = HtmlToPojoUtils.getLocaleFromTag(selector.locale());
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


    /**
     * This method must be able to set to the corresponding field of instance {@code newInstance}
     * the value resulting of {@code node} parsing and conversion processing.
     *
     * It should call under the hood both {@link #setFieldOrThrow(Object, Object)} and
     * {@link #getRawValue(HtmlToPojoEngine, Element, Object)} method to build up the corresponding
     * values. Both those method should be public because they might be used separately from
     * outside the class for more custom implementations.
     *
     * @param htmlToPojoEngine the engine to create another {@link HtmlAdapter} for List of POJO
     *                         fields as well as POJO fields.
     * @param node the node to extract the data from.
     * @param newInstance the instance to assign to the corresponding field the resulting value.
     */
    public void setValue(HtmlToPojoEngine htmlToPojoEngine, Element node, T newInstance) {

        Object rawValue = null;
        boolean fieldShouldBeSet = true;
        try {
            rawValue = getRawValue(htmlToPojoEngine, node, newInstance);
        }
        catch (FieldShouldNotBeSetException e) {
            fieldShouldBeSet = false;
        }

        if (fieldShouldBeSet)
            setFieldOrThrow(newInstance, rawValue);
    }


    /**
     * This method is responsible of assigning the resulting {@code value} to the
     * corresponding field of {@code newInstance}
     * @param newInstance the instance to assign the corresponding field {@code value}
     * @param value the value to assign to the corresponding field of {@code newInstance}
     * @throws FieldSetException if the value could not be set to the field. This will
     *                           typically not be catch by HtmlToPojo lib and should be
     *                           handle separately in outside classes.
     */
    public void setFieldOrThrow(Object newInstance, Object value) throws FieldSetException {

        try {
            field.setAccessible(true);
            field.set(newInstance, value);
        } catch (IllegalAccessException e) {
            throw new FieldSetException(newInstance.getClass().getSimpleName(), field.getName());
        }
    }


    /**
     * @return the field this adapter is supposed to handle.
     */
    public Field getField() {
        return field;
    }


    /**
     * This method can select a child element from a parent one.
     * @param parent the parent element from which the child element
     *               should be picked.
     * @return the picked child element.
     */
    protected Element selectChild(Element parent) {
        return getElementAtIndexOrNull(parent);
    }


    /**
     * This method can select children element ({@link Elements})
     * from a parent one.
     * @param parent the parent element from which the children
     *               elements should be picked.
     * @return the picked children elements.
     */
    protected Elements selectChildren(Element parent) {
        return parent.select(cssQuery);
    }


    /**
     * Will retrieve the parsed, computed and casted instance from
     * a given node. This value can then further be directly set
     * to the corresponding field via reflection.
     * @param node the node to extract the instance from.
     * @param clazz the class of the output object.
     * @param parentObject the parent object to which the returned value
     *                     will be assigned to the correct field. This is
     *                     used to properly instanciate the deserializers.
     * @param <U> the type of the field/instance to create.
     * @return the parsed, computed and casted instance resulting from the
     *         given node.
     */
    @SuppressWarnings("unchecked")
    protected <U> U instanceForNode(Element node, Class<U> clazz, Object parentObject) {

        if (clazz.equals(Element.class)) {
            return (U) node;
        }
        String value = HtmlToPojoUtils.extractRawValue(node, defValue, attribute);

        HtmlDeserializer<U> deserializer = null;

        if(useDeserializer)
        {
            deserializer = createDeserializer(parentObject);
            if(preConvert)
                value = deserializer.deserializePreConversion(value);
        }

        value = editStringWithPattern(value);

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


    /**
     * @param e the element selected for the current field.
     * @param parentObject the parent object the field to analyse belongs to.
     * @return a boolean indicating wether this field should or shouldn't be
     *         fetched.
     */
    protected boolean shouldBeFetched(Element e, Object parentObject){
        AcceptObjectIf acceptObjectIf = field.getAnnotation(AcceptObjectIf.class);

        if(acceptObjectIf == null)
            return true;


        for(Class<? extends AcceptIfResolver> resolverClazz : acceptObjectIf.value())
        {
            AcceptIfResolver resolver = WhimtripUtils.createNewInstance(resolverClazz);

            resolver.init(field, parentObject, selector);
            if(!resolver.accept(e, parentObject))
                return false;
        }

        return true;
    }


    /**
     * @param parent the parent element to retrieve the child element from.
     * @return the element with the css query given in the {@link Selector} annotation
     *         of the field/POJO class. If it's a list, it will return the one with the
     *         index given in the {@link Selector} annotation. If there is none at this
     *         index, then a null element will be returned.
     */
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


    /**
     *
     * @param value pre processed string to cast.
     * @param clazz the class it needs to be casted in.
     * @param <U> the type it needs to be casted in.
     * @return the casted value resulting from the pre processed string.
     *         The type it will be casted in must be one of the simple
     *         types.
     * @see HtmlToPojoUtils#isSimple(Class)
     * @throws DateParseException if U is a DateTime / Date field and if the
     *                            format and locale doesn't match the submitted
     *                            string input.
     */
    private <U> U castValue(String value, Class<U> clazz) throws DateParseException {
        return HtmlToPojoUtils.castValue(value, clazz, dateFormat, locale);
    }


    /**
     * This is the method that will handle regex pattern computing and processing.
     * @param value the input, eventually pre converted string
     *              (see {@link HtmlDeserializer#deserializePreConversion(String)})
     * @param <U>
     * @return
     */
    private <U> String editStringWithPattern(String value) {
        if (!format.equals(Selector.NO_VALUE))
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

    private HtmlDeserializer createDeserializer(Object parentObject) {
        HtmlDeserializer deserializer = newInstanceOfDeserializer();
        deserializer.init(field, parentObject, selector);
        return deserializer;
    }

    private HtmlDeserializer newInstanceOfDeserializer() {
        return WhimtripUtils.createNewInstance(deserializer);
    }


}
