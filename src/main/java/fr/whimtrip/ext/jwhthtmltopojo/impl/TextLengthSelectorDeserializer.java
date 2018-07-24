package fr.whimtrip.ext.jwhthtmltopojo.impl;

import fr.whimtrip.core.util.exception.ObjectCreationException;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.TextLengthSelector;
import fr.whimtrip.ext.jwhthtmltopojo.exception.RegexDeserializerConversionException;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlDeserializer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;

public class TextLengthSelectorDeserializer implements HtmlDeserializer<String> {


    private static final Logger log = LoggerFactory.getLogger(TextLengthSelectorDeserializer.class);

    private TextLengthSelector textLengthSelector;

    private static final Pattern END_OF_SENTENCE = Pattern.compile("\\.\\s+");
    private static final Pattern END_OF_WORD = Pattern.compile("(\\b[^\\s]+\\b)");


    /**
     * Initialization requires a {@code {@link TextLengthSelector}} annotation to be built.
     * @param field The actual field being deserialized.
     * @param parentObject the parent object that will contain the deserialized object
     * @param selector automatically submitted {@code {@link Selector }} annotation available
     *                 on the given field.
     * @return the deserializer itself.
     */
    @Override
    public void init(Field field, Object parentObject, Selector selector) throws ObjectCreationException {
        if(!field.isAnnotationPresent(TextLengthSelector.class))
            throw new ObjectCreationException(field, this.getClass(), TextLengthSelector.class);
        textLengthSelector = field.getAnnotation(TextLengthSelector.class);
    }

    /**
     * Both post and pre deserialization will call the same method. It should only be used for one
     * of both purposes.
     * @param value the value to convert
     * @return the converted value
     * @throws RegexDeserializerConversionException
     */
    @Override
    public String deserializePreConversion(String value) throws RegexDeserializerConversionException
    {
        return filterValue(value);
    }


    /**
     *
     * Both post and pre deserialization will call the same method. It should only be used for one
     * of both purposes.
     * @param value the value to convert
     * @return the converted value
     * @throws RegexDeserializerConversionException
     */
    @Override
    public String deserializePostConversion(String value) throws RegexDeserializerConversionException
    {
        return filterValue(value);
    }


    /**
     * This core value filtering method will handle any correctly parametizered text length selection
     * processing. This will depending on the case separate by text length (in characters) then text
     * length in words or text length in words. If the max character limit is reached with any of the
     * two last methods mentionned, we shall cut it accordingly. If on the opposite, the min limit is
     * not reached, we should try if possible to take the next characters of the string until we have
     * the accounted min number of characters required.
     * @param value The value to filter
     * @return the filtered string.
     */
    private String filterValue(String value) {
        StringBuilder finalStringBuilder = new StringBuilder();
        int startIndex = 0;

        value = trim(value);

        if(textLengthSelector.countWith() == TextLengthSelector.CountWith.CHARS)
            return separateChars(value);
        
        if(textLengthSelector.countWith() == TextLengthSelector.CountWith.SENTENCES)
            startIndex = separateSentences(value, finalStringBuilder);
        
        if(textLengthSelector.countWith() == TextLengthSelector.CountWith.WORDS)
            startIndex = separateWords(value, finalStringBuilder);

        if(textLengthSelector.backupLowerLimit() != -1 && finalStringBuilder.length() < textLengthSelector.backupLowerLimit())
        {
            return computeBackupValue(value, startIndex, textLengthSelector.backupLowerLimit());
        }

        if(textLengthSelector.backupUpperLimit() != -1 && finalStringBuilder.length() > textLengthSelector.backupUpperLimit())
        {
            return computeBackupValue(value, startIndex, textLengthSelector.backupUpperLimit());
        }

        return finalStringBuilder.toString();

    }

    /**
     * Simply select the chars required. starts at indicated index and stops at this index + the
     * length required or the max index of the string in order to avoid ArrayOutOfBoundException
     * @param value the value to be separated
     * @return the separated value
     */
    @NotNull
    private String separateChars(String value) {
        return value.substring(
                Math.min(
                        value.length(),
                        textLengthSelector.startAt()
                ),
                Math.min(
                        value.length(),
                        textLengthSelector.length() + textLengthSelector.startAt()
                )
        );
    }


    /**
     * The method that will compute the backup value in the case wre not enough or too many
     * characters were found.
     * @param value the original value
     * @param startIndex the index from which to start
     * @param limit the maximum number of elements to retrieve
     * @return the computed backup string while avoiding any ArrayOutOfBound exception
     */
    private String computeBackupValue(String value, int startIndex, int limit) {
        return value.substring(
                Math.min(startIndex, value.length()),
                Math.min(value.length(), startIndex + limit)
        );
    }

    /**
     * Calling the element separation method with the END_OF_WORD regex
     * @param value
     * @param finalStringBuilder
     * @return
     */
    private int separateWords(String value, StringBuilder finalStringBuilder) {
        return separateElements(value, finalStringBuilder, END_OF_WORD);
    }


    /**
     * Calling the sentences separation method with the END_OF_SENTENCE regex
     * @param value
     * @param finalStringBuilder
     * @return
     */
    private int separateSentences(String value, StringBuilder finalStringBuilder) {
        return separateElements(value, finalStringBuilder, END_OF_SENTENCE);
    }

    /**
     * The method in charge of separating the elements and then rebuilding them together
     * in a string builder.
     * @param value the value to split and rebuild
     * @param finalStringBuilder the string builder to append text to
     * @param pattern the regex pattern to separate items
     * @return the start index of the string which is not directly equals to the value of the starting
     *          element as we are using either words or sentences but not chars directly.
     */
    private int separateElements(String value, StringBuilder finalStringBuilder, Pattern pattern) {

        /*
         * We will first split the elements in an array of elements and then turned it into
         * an iterator. We will also instanciate our indexes here to be used later on.
         */
        String[] elements = pattern.split(value);

        Iterator<String> elementsIterator = Arrays.asList(elements).iterator();
        int i = 0;
        int firstStringIndex = 0;

        /*
         * While there is a next element and we didn't reach our limit elements quantity.
         */
        while (elementsIterator.hasNext() && i < textLengthSelector.length() + textLengthSelector.startAt())
        {
            /*
             * We will further pick the next element of the iterator, and if the actual index is in the range
             * of the lements we want to use, then we shall append it to the string builder and the part of
             * the string we removed as well.
             */
            String element = elementsIterator.next();

            log.debug("Found element " + element + ".");

            if(i >= textLengthSelector.startAt())
            {
                log.debug("This element will be selected");
                finalStringBuilder
                        .append(element)
                        .append(pattern == END_OF_SENTENCE ? ". " : " ");
            }
            else
            {
                /*
                 * If we yet didn't reached our first element, then we will add it's length to the starting
                 * offset as well as 2 or 1 character depending on the count of characters we removed when
                 * splitting.
                 */
                firstStringIndex += element.length() + (pattern == END_OF_SENTENCE ? 2 :  1);
            }

            i++;
        }
        return firstStringIndex;
    }


    /**
     * This method will replace all occurences of the found forbidden part in the
     * given part. The string submitted will be used as a regex.
     * @param element the element
     * @return the submitted string trimmed.
     */
    private String trim(String element)
    {
        return textLengthSelector.removeIfPresent().equals(TextLengthSelector.NONE) ?
                    element :
                    element.replaceAll(textLengthSelector.removeIfPresent(), "");
    }


    /**
     * Should be called if this operator needs to be used outside of the context of the Jspoon Html
     * adapter.
     * @param textLengthSelector the textlength selector annotation to configure this deserializer.
     * @return the deserializer itself.
     */
    public TextLengthSelectorDeserializer setTextLengthSelector(TextLengthSelector textLengthSelector) {
        this.textLengthSelector = textLengthSelector;
        return this;
    }

}
