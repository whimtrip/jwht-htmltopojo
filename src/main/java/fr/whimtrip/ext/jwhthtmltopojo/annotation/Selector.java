



package fr.whimtrip.ext.jwhthtmltopojo.annotation;

import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlAdapter;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlDeserializer;
import org.jsoup.nodes.Element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a field to be mapped to a html element.
 *
 * A field annotated with this will receive the value corresponding to it's CSS
 * selector when the {@link HtmlAdapter#fromHtml(String)} is called.
 *
 * Can be applied to any field of the following types (or their primitive equivalents)
 * String
 * Float
 * Double
 * Integer
 * Long
 * Boolean
 * Date
 * {@link Element}
 * Any class with default contructor
 * List of supported type
 *
 * It can also be used with a class, then you don't need to annotate every field inside it.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD })
public @interface Selector {
    String NO_VALUE = "NO_VALUE";

    /** @return Css query */
    String value();

    /** @return Attribute or property of selected field. "text" is default. Also "html"/"innerHtml" or "outerHtml" is supported. */
    String attr() default "";

    /** @return Regex for numbers and String, date format for Date. */
    String format() default NO_VALUE;

    String dateFormat() default NO_VALUE;

    /** @return Locale string, used for Date and Float */
    String locale() default NO_VALUE;

    /** @return Default String value if selected HTML element is empty */
    String defValue() default NO_VALUE;

    /** @return Index of found HTML element */
    int index() default 0;

    /**  @return The index of the group whose value will be taken for regex pattern matching **/
    int[] indexForRegexPattern() default 0;

    /**
     * @return Wether we need to use a deserializer or not
     */
    boolean useDeserializer() default false;

    /**
     * @return
     * If set to true the deserializer class will be used in order to pre convert the string into another one
     * before being normally treated
     *
     */
    boolean preConvert() default false;

    /**
     * @return
     * If set to true the deserializer class will also be used in order to post convert the element
     * into it's final type after regex filter
     *
     */
    boolean postConvert() default false;

    /**
     *
     * @return the deserializer class we will use
     */
    Class<? extends HtmlDeserializer> deserializer() default HtmlDeserializer.class;

    /**
     * Wether default value should be used on cast exception or if error should be thrown
     */
    boolean returnDefValueOnThrow() default true;


}
