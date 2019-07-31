



package fr.whimtrip.ext.jwhthtmltopojo.annotation;

import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlAdapter;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlDeserializer;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlDifferentiator;
import org.joda.time.DateTime;
import org.jsoup.nodes.Element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;

/**
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>Annotates a field to be mapped from an html element.</p>
 *
 * <p>
 *     A field annotated with this will receive the value corresponding to it's CSS
 *     selector when the {@link HtmlAdapter#fromHtml(String)} is called.
 * </p>
 *
 * <p>Can be applied to any field of the following types (or their primitive equivalents)</p>
 * <ul>
 *     <li>String</li>
 *     <li>Float</li>
 *     <li>Double</li>
 *     <li>Integer</li>
 *     <li>Long</li>
 *     <li>Boolean</li>
 *     <li>{@link Date}</li>
 *     <li>{@link DateTime}</li>
 *     <li>{@link Element}</li>
 *     <li>Any POJO class annotated with {@link Selector} on fields to populate</li>
 *     <li>List of supported types</li>
 * </ul>
 *
 * @author Louis-wht
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD })
public @interface Selector {

    String NO_VALUE = "NO_VALUE";

    /**
     * @return Css query
     * @see <a href="https://jsoup.org/apidocs/org/jsoup/select/Selector.html">Jsoup Selector</a>
     */
    String value();

    /** @return Attribute or property of selected field. "text" is default. Also "html", "innerHtml"
     *          or "outerHtml" is supported. Any other attribute can also be stated but it might
     *          result in null values so be careful not to mistype those.
     */
    String attr() default "";

    /**
     *  @return Regex to use to format the input string.
     */
    String format() default NO_VALUE;

    /**
     * @return date format to use to convert the string to date objects. Depending on
     * if you use standard java date or joda time {@link DateTime}, please refer to
     * their documentation for date format.
     */
    String dateFormat() default NO_VALUE;

    /**
     * @return Locale string, used for Date and Float
     */
    String locale() default NO_VALUE;

    /**
     * @return Default String value if selected HTML element is empty
     */
    String defValue() default NO_VALUE;

    /**
     * @return Index of found HTML element. If the css query has several results, then which one
     *          should be picked ? You can give this information with this parameter.
     */
    int index() default 0;

    /**
     *  @return The index of the group whose value will be taken for regex pattern matching.
     *          For example, if your regex is as following : {@code ^(Restaurant|Hotel) n\*([0-9]+)$}
     *          and the input string is {@code Restaurant n*912} and you only want {@code 912}, then
     *          you should give this parameter the value {@code 2} to select the second regex group.
     **/
    int[] indexForRegexPattern() default 0;

    /**
     * @return Whether we need to use a deserializer or not
     */
    boolean useDeserializer() default false;

    /**
     * @return If set to true the deserializer class will be used in order to pre convert the string into another one
     * before being normally treated
     * @see HtmlDeserializer
     *
     */
    boolean preConvert() default false;

    /**
     * @return If set to true the deserializer class will also be used in order to post convert the element
     * into it's final type after regex filter
     * @see HtmlDeserializer
     */
    boolean postConvert() default false;

    /**
     * @return the deserializer class we will use
     */
    Class<? extends HtmlDeserializer> deserializer() default HtmlDeserializer.class;

    /**
     * @return Wether default value should be used on cast exception or if error should be thrown.
     */
    boolean returnDefValueOnThrow() default true;

    /**
     * @return the differentiator class we will use
     */
    Class<? extends HtmlDifferentiator> differentiator() default HtmlDifferentiator.class;


}
