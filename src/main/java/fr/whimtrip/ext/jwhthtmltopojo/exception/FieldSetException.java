/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo.exception;

import java.util.Locale;

public class FieldSetException extends HtmlToPojoException {

    public FieldSetException(String className, String field) {
        super(String.format(Locale.ENGLISH, "Error while setting %s in %s object", field, className));
    }
}
