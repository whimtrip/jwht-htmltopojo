package fr.whimtrip.ext.jwhthtmltopojo.exception;

public class RegexDeserializerConversionException extends ConversionException {


    public RegexDeserializerConversionException(String value, String regex) {
        super(String.format("Cannot deserialize field %s with regex %s.", value, regex));
    }

    public RegexDeserializerConversionException(String message) {

        super(message);
    }
}
