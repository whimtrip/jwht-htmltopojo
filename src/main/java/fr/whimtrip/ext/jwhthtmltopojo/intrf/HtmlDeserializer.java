





package fr.whimtrip.ext.jwhthtmltopojo.intrf;

import fr.whimtrip.ext.jwhthtmltopojo.exception.ConversionException;
import fr.whimtrip.ext.jwhthtmltopojo.impl.ReplacerDeserializer;
import fr.whimtrip.ext.jwhthtmltopojo.impl.StringConcatenatorDeserializer;
import fr.whimtrip.ext.jwhthtmltopojo.impl.TextLengthSelectorDeserializer;

/**
 *
 * An Html Deserializer can be used to define deserialization hooks.
 *
 * <p>There is two different deserialization processes, pre and post
 * deserialization. </p>
 *  <ul>
 *      <li>
 *          Pre deserialization happens just after the raw string value
 *          has been gathered from the HTML element, it must return a
 *          string.
 *      </li>
 *      <li>
 *          Post deserialization happens after regex matching and pre
 *          deserialization and must return an object whose type converts
 *          back to the field's type.
 *      </li>
 *  </ul>
 *
 *
 *  Implementations of an HtmlDeserializer features  {@link TextLengthSelectorDeserializer},
 *  {@link StringConcatenatorDeserializer}, {@link ReplacerDeserializer}.
 *
 *  <p>
 *      To use an Html Deserializer on one of your fields, you should process
 *      as following :
 *  </p>
 *
 *  <pre>
 *      &#64;Selector(
 *          value = "some-css-query",
 *          useDeserializer = true,
 *          // if you want the pre conversion method to be called
 *          preConvert = true,
 *          // if you want the post conversion method to be called
 *          postConvert = true,
 *          deserializer = MyCustomDeserializer.class
 *      )
 *      private String myDeserializedString;
 *  </pre>
 *
 *
 * Part of project jwht-htmltopojo
 *
 * @author Louis-wht
 * @since 24/07/18
 */
public interface HtmlDeserializer<T> extends Initiate {

    /**
     * <p>Pre deserialization of the given String input raw value.</p>
     *
     * <p>
     *     Pre deserialization happens just after the raw string value
     *     has been gathered from the HTML element, it must return a
     *     string.
     * </p>
     *
     * <p>
     *     A given HtmlDeserializer might implement this method with only :
     * </p>
     * <pre>
     *     &#64;Override
     *     public String deserializePreConversion(String value){
     *         return value;
     *     }
     * </pre>
     *
     * @param value the value to pre deserialize
     * @return a string representing the pre deserialized value
     * @throws ConversionException if some input value is incorrect
     *                             and cannot be processed.
     */
    String deserializePreConversion(String value) throws ConversionException;


    /**
     * <p>Post deserialization of the given String input raw value.</p>
     *
     * <p>
     *    Post deserialization happens after regex matching and pre
     *    deserialization and must return an object whose type converts
     *    back to the field's type.
     * </p>
     *
     * <p>
     *     A given HtmlDeserializer might implement this method with only :
     * </p>
     * <pre>
     *     &#64;Override
     *     public String deserializePostConversion(String value){
     *         return value;
     *     }
     * </pre>
     *
     * @param value the value to post deserialize
     * @return an output object directly of the type expected by the input
     *
     * @throws ConversionException if some input value is incorrect
     *                             and cannot be processed.
     */
    T deserializePostConversion(String value) throws ConversionException;

}
