package fr.whimtrip.ext.jwhthtmltopojo.adapter;

import java.lang.reflect.Field;

 /**
 *
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>
 *     Basic POJO to hold information regarding a given annotation for
 *     a given field.
 * </p>
 *
 * @author Louis-wht
 * @since 1.0.0
 */
public class HtmlToPojoAnnotationMap<U>
{
    private String name;
    private U annotation;
    private Field field;

    public String getName() {
        return name;
    }

    public U getAnnotation() {
        return annotation;
    }

    public Field getField() {
        return field;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAnnotation(U annotation) {
        this.annotation = annotation;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
