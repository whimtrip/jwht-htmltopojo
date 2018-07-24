package fr.whimtrip.ext.jwhthtmltopojo.exception;


/**
 *
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>
 *     Default HtmlToPojo exception that all other exceptions
 *     of this project extends. It only overrides default
 *     constructors of {@link RuntimeException} with default
 *     implementations so that they can be used easily in extended
 *     exceptions classes.
 * </p>
 *
 * @author Louis-wht
 * @since 24/07/18
 */
public class HtmlToPojoException extends RuntimeException {

    public HtmlToPojoException() {

    }

    public HtmlToPojoException(String message) {

        super(message);
    }

    public HtmlToPojoException(String message, Throwable cause) {

        super(message, cause);
    }

    public HtmlToPojoException(Throwable cause) {

        super(cause);
    }

    public HtmlToPojoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {

        super(message, cause, enableSuppression, writableStackTrace);
    }
}
