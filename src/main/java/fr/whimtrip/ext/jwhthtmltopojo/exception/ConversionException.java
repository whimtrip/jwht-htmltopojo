package fr.whimtrip.ext.jwhthtmltopojo.exception;


import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlDeserializer;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlField;
import org.jsoup.nodes.Element;

/**
 *
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>
 *     Thrown by {@link HtmlField#getRawValue(HtmlToPojoEngine, Element, Object)}
 *     when an {@link HtmlDeserializer} fails to operate properly pre or post
 *     conversion operations. This exception class features all common
 *     {@link RuntimeException} constructors because it is meant to be overrided
 *     with more specific Exceptions types depending on the use case.
 * </p>
 *
 * @author Louis-wht
 * @since 1.0.0
 */
public class ConversionException extends HtmlToPojoException {

    public ConversionException() {

    }

    public ConversionException(String message) {

        super(message);
    }

    public ConversionException(String message, Throwable cause) {

        super(message, cause);
    }

    public ConversionException(Throwable cause) {

        super(cause);
    }
}
