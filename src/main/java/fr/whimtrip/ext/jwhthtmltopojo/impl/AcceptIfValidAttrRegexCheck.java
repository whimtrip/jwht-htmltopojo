





package fr.whimtrip.ext.jwhthtmltopojo.impl;

import fr.whimtrip.core.util.exception.ObjectCreationException;
import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoUtils;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.AttrRegexCheck;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.AcceptIfResolver;
import org.jsoup.nodes.Element;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>Example implementation of an  {@link AcceptIfResolver}.</p>
 *
 * <p>
 *     This one will extract one attribute of the given HTML Element
 *     {@code {@link Element}} and check if it matches with the
 *     submitted regex.
 * </p>
 *
 * <p>
 *     You have to provide an {@link AttrRegexCheck} annotation on top of
 *     the corresponding field in order for this deserializer to work properly.
 * </p>
 *
 * @author Louis-wht
 * @since 24/07/18
 */
public class AcceptIfValidAttrRegexCheck implements AcceptIfResolver {

    private AttrRegexCheck attrRegexCheck;

    @Override
    public void init(Field field, Object parentObject, Selector selector) throws ObjectCreationException {
        attrRegexCheck = field.getAnnotation(AttrRegexCheck.class);

        if(attrRegexCheck == null)
            throw new ObjectCreationException(field, this.getClass(), AttrRegexCheck.class);
    }

    @Override
    public boolean accept(Element element, Object parentObject) {
        String attr = HtmlToPojoUtils.extractRawValue(element, "", attrRegexCheck.attr());
        return Pattern
                .compile(attrRegexCheck.value())
                .matcher(attr)
                .find();
    }
}
