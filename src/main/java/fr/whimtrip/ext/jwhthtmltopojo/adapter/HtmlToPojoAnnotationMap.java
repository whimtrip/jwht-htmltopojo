package fr.whimtrip.ext.jwhthtmltopojo.adapter;

import java.lang.reflect.Field;

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
