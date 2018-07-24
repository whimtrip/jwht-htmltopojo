package fr.whimtrip.ext.jwhthtmltopojo.annotation;


import fr.whimtrip.ext.jwhthtmltopojo.impl.TextLengthSelectorDeserializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 *
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>
 *     Used together with {@link TextLengthSelectorDeserializer},
 *     this annotation will provide a way to pick only a certain
 *     amount of chars, words or sentences in a long string, with
 *     several backup technics.
 * </p>
 *
 * @author Louis-wht
 * @since 24/07/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface TextLengthSelector {

    String NONE = "-_|_NONE_|_-";

    /**
     * @return the quantity of elements that should be skipped before
     *         starting to add elements to the resulting string.
     */
    int startAt() default 0;

    /**
     * @return the number of elements that should be added to the
     *         resulting string starting from {@link #startAt()}.
     */
    int length();

    /**
     * This enum will define the type of elements to count for in the
     * resulting string.
     */
    enum CountWith {
        /** Will count characters */
        CHARS,
        /**
         *  Will count words identified with the pattern
         *  {@link TextLengthSelectorDeserializer#END_OF_WORD}
         */
        WORDS,
        /**
         * Will count sentences identified with the pattern
         * {@link TextLengthSelectorDeserializer#END_OF_SENTENCE}
         */
        SENTENCES
    }

    /**
     * @return the type of elements that will be counted and selected by {@link TextLengthSelectorDeserializer}.
     */
    CountWith countWith() default CountWith.WORDS;

    /**
     * @return the maximum number of characters that can be used in the resulting string.
     *         If the result has more chars than this limit, it will be cut to this backup limit.
     *         {@code -1} will make this limit + infinite.
     */
    int backupUpperLimit() default -1;

    /**
     * @return the minimum number of characters that should be found in the resulting string.
     *         If the result has less than this minimum number of chars and if the string still
     *         has some remaining chars, they will be added to the string until either this
     *         minimum limit is reached or the input string hasn't got any more chars left.
     *         {@code -1} will account for "no minimum limit" which means that whatever happens,
     *         no remaining chars will be added to fix the lower limit pattern.
     */
    int backupLowerLimit() default -1;

    /**
     * @return a regex pattern that will be replaced by an empty string if matched each time it
     *         is found in the original input string. This is useful when each sentence has a
     *         specific character sequence that you don't want to keep for example.
     */
    String removeIfPresent() default NONE;

}
