package fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface TextLengthSelector {

    String NONE = "-_|_NONE_|_-";

    int startAt() default 0;

    int length();

    enum CountWith{
        CHARS,
        WORDS,
        SENTENCES
    }

    CountWith countWith() default CountWith.WORDS;

    int backupUpperLimit() default -1;

    int backupLowerLimit() default -1;

    String removeIfPresent() default NONE;

}
