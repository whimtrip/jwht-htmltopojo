/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo.exception;


/**
 * Created by LOUISSTEIMBERG on 19/11/2017.
 */
public class LinkException extends HtmlToPojoException{

    public LinkException(String errorMessage) {
        super(errorMessage);
    }

    public LinkException(Throwable e)
    {
        this(e.getMessage());
        setStackTrace(e.getStackTrace());
    }
}
