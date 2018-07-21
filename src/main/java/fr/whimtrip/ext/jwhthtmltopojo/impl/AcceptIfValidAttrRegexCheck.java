/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo.impl;

import fr.whimtrip.core.util.exception.ObjectCreationException;
import fr.whimtrip.ext.jwhthtmltopojo.adapter.HtmlField;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.AttrRegexCheck;
import fr.whimtrip.ext.jwhthtmltopojo.intfr.AcceptIfResolver;
import org.jsoup.nodes.Element;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * Created by LOUISSTEIMBERG on 20/11/2017.
 */
public class AcceptIfValidAttrRegexCheck implements AcceptIfResolver {

    private Field field;
    private AttrRegexCheck attrRegexCheck;

    @Override
    public void init(Field field, Object parentObject) throws ObjectCreationException {
        this.field = field;
        attrRegexCheck = field.getAnnotation(AttrRegexCheck.class);

        if(attrRegexCheck == null)
            throw new ObjectCreationException("Field " + field.getName() + " from object " + field.getDeclaringClass()
                    + " hasn't got @AttrRegexCheck annotation that should be used with class " + this.getClass());
    }

    @Override
    public boolean acceptIf(Element element, Object parentObject) {
        String attr = HtmlField.getValue(element, "", attrRegexCheck.attr());
        return Pattern
                .compile(attrRegexCheck.value())
                .matcher(attr)
                .find();
    }
}
