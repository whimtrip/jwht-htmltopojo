package fr.whimtrip.ext.jwhthtmltopojo.intrf;


import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;
import fr.whimtrip.ext.jwhthtmltopojo.adapter.HtmlToPojoAnnotationMap;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Inject;
import fr.whimtrip.ext.jwhthtmltopojo.exception.HtmlToPojoException;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 *
 * Part of project jwht-htmltopojo
 *
 *
 * This class defines the contracts a custom HtmlAdapter should follow
 * in order to be used with a custom HtmlToPojoEngine.
 * @see HtmlToPojoEngine#create(HtmlAdapterFactory)
 * @param <T> the type this adapter will convert any HTML string to.
 * @author Louis-wht
 * @since 1.0.0
 */
public interface HtmlAdapter<T> {

    /**
     * This method should return a list of fields (as {@link HtmlToPojoAnnotationMap})
     * that have the corresponding annotation.
     *
     * @param clazz the annotation clazz
     * @param <U> annotation type
     * @return the fields (as {@link HtmlToPojoAnnotationMap}) that have the corresponding
     *         annotation.
     */
    <U> List<HtmlToPojoAnnotationMap<U>> getFieldList(@NotNull Class<? extends U> clazz);

    /**
     * This method should return a field (as {@link HtmlToPojoAnnotationMap}) (or null)
     * that has both the field {@code name} required and the corresponding annotation
     * {@code clazz}.
     *
     * @param name the name of the field to search for.
     * @param clazz the clazz of the annotation that the corresponding field should have.
     * @param <U> the type of the annotation
     * @return the corresponding {@link HtmlToPojoAnnotationMap}.
     */
    <U> HtmlToPojoAnnotationMap<U> getfield(String name, Class<? extends U> clazz);


    /**
     * Converts html string to {@code T} object.
     * @param htmlContent String with HTML content
     * @return Created and populated object
     * @throws HtmlToPojoException if any loading step failed during the process.
     */
    T fromHtml(String htmlContent) throws HtmlToPojoException;

    /**
     * Converts html string to {@code T} object. When using this method, the object is already
     * instanciated. This might help when performing code injection for example, but also if you
     * already have the reference of the object to create.
     *
     * @param htmlContent String with HTML content
     * @param obj the already instanciated object that needs to be populated.
     * @return Populated opulated object
     * @throws HtmlToPojoException if any loading step failed during the process.
     */
    T fromHtml(String htmlContent, T obj) throws HtmlToPojoException;

    /**
     * This method should create and build a new instance. This means that both reflection object
     * creation and reflection code injection must be performed. That's why {@code parentObj}
     * must be submitted in order to correctly perform code injection (with {@link Inject}
     * annotations for example).
     *
     * @param parentObj the parent object to build an inner object for.
     * @param <M> the type of the parent object
     * @return {@code T} child object to populate one of the parent object field.
     */
    <M> T createNewInstance(M parentObj);


    /**
     * Load a node to a POJO
     * @param node the {@link Jsoup} element to parse into a {@code T} instance.
     * @return Created, parsed and populated {@code T} instance.
     * @throws HtmlToPojoException if any loading step failed during the process.
     */
    T loadFromNode(Element node) throws HtmlToPojoException;

    /**
     * Load a node to a POJO
     * @param node the {@link Jsoup} element to parse into a {@code T} instance.
     * @param newInstance the {@code T} instance to populate with the corresponding node.
     * @return Populated {@code T} instance.
     * @throws HtmlToPojoException if any loading step failed during the process.
     */
    T loadFromNode(Element node, T newInstance) throws HtmlToPojoException;
}
