package fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.exception;

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
