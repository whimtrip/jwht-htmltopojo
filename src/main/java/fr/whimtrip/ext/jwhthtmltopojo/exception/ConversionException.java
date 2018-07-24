package fr.whimtrip.ext.jwhthtmltopojo.exception;

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
