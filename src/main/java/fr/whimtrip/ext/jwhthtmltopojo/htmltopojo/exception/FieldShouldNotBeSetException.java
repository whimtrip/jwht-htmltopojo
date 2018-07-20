package fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.exception;

import java.lang.reflect.Field;

public class FieldShouldNotBeSetException extends HtmlToPojoException {
    public FieldShouldNotBeSetException(Field field) {
        super(String.format("Field %s should not be set.", field.getName()));
    }
}
