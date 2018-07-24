



package fr.whimtrip.ext.jwhthtmltopojo.exception;

import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlField;

import java.lang.reflect.Field;
import java.util.Locale;


/**
 *
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>
 *     Occures when a value could not be set to a field in
 *     {@link HtmlField#setFieldOrThrow(Object, Object)}.
 * </p>
 *
 * @author Louis-wht
 * @since 24/07/18
 */
public class FieldSetException extends HtmlToPojoException {

    /**
     * @param className parent class name
     * @param fieldName field that could not be set name
     */
    private FieldSetException(String className, String fieldName) {
        super(String.format(Locale.ENGLISH, "Error while setting %s in %s object", fieldName, className));
    }

    /**
     * @param field the field whose value could not be set
     */
    public FieldSetException(Field field) {
        this(field.getDeclaringClass().getName(), field.getName());
    }
}
