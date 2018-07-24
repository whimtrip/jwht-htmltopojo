package fr.whimtrip.ext.jwhthtmltopojo.intrf;

import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;
import fr.whimtrip.ext.jwhthtmltopojo.adapter.AbstractHtmlFieldImpl;
import fr.whimtrip.ext.jwhthtmltopojo.adapter.HtmlClassField;
import fr.whimtrip.ext.jwhthtmltopojo.adapter.HtmlListField;
import fr.whimtrip.ext.jwhthtmltopojo.adapter.HtmlSimpleField;
import fr.whimtrip.ext.jwhthtmltopojo.exception.FieldSetException;
import fr.whimtrip.ext.jwhthtmltopojo.exception.FieldShouldNotBeSetException;
import org.jsoup.nodes.Element;

import java.lang.reflect.Field;

/**
 * Part of project jwht-htmltopojo
 *
 * This interface defines the contract that an HtmlField should accept and follow.
 * An Html Field is a basic processing unit that is able to convert an input String
 * into an outpout object of whatever his type is.
 *
 * Default implementation defines an implementing abstract class {@link AbstractHtmlFieldImpl}
 * with three implementing classes for simple fields {@link HtmlSimpleField}, POJO fields
 * {@link HtmlClassField} and List field {@link HtmlListField}. This helps a lot to implement
 * custom logic depending on the type of field we have to set.
 *
 *
 * @param <T> the type of the field to assign a value to.
 * @author Louis-wht
 * @since 24/07/18
 */
public interface HtmlField<T> {


    /**
     * This method must be able to set to the corresponding field of instance {@code newInstance}
     * the value resulting of {@code node} parsing and conversion processing.
     *
     * It should call under the hood both {@link #setFieldOrThrow(Object, Object)} and
     * {@link #getRawValue(HtmlToPojoEngine, Element, Object)} method to build up the corresponding
     * values. Both those method should be public because they might be used separately from
     * outside the class for more custom implementations.
     *
     * @param htmlToPojoEngine the engine to create another {@link HtmlAdapter} for List of POJO
     *                         fields as well as POJO fields.
     * @param node the node to extract the data from.
     * @param newInstance the instance to assign to the corresponding field the resulting value.
     */
    void setValue(HtmlToPojoEngine htmlToPojoEngine, Element node, T newInstance);


    /**
     * This method is responsible of assigning the resulting {@code value} to the
     * corresponding field of {@code newInstance}
     * @param newInstance the instance to assign the corresponding field {@code value}
     * @param value the value to assign to the corresponding field of {@code newInstance}
     * @throws FieldSetException if the value could not be set to the field. This will
     *                           typically not be catch by HtmlToPojo lib and should be
     *                           handle separately in outside classes.
     */
    void setFieldOrThrow(Object newInstance, Object value) throws FieldSetException;


    /**
     * This method is supposed to extract from a node the value of the actual field.
     * @param htmlToPojoEngine to be used for list of POJOs fields and POJO fields in
     *                         order to retrieve the correct {@link HtmlAdapter} to create
     *                         the corresponding child elements.
     * @param node the node from which the data should be extracted.
     * @param parentObject the parent instance. This is used for list of POJOs fields and
     *                    POJO fields when instanciating the children POJOs in order to
     *                    perform the eventual code injection required.
     * @return the raw value to be set to the field
     * @throws FieldShouldNotBeSetException when the field should not be set. It happens when there is an
     *                                      {@link AcceptIfResolver} providing a {@code false} value in which
     *                                      case the field should not be set.
     */
    T getRawValue(HtmlToPojoEngine htmlToPojoEngine, Element node, T parentObject) throws FieldShouldNotBeSetException;

    /**
     * @return the field this adapter is supposed to handle.
     */
    Field getField();


}
