package fr.whimtrip.ext.jwhthtmltopojo.intrf;

import fr.whimtrip.ext.jwhthtmltopojo.DefaultHtmlAdapterFactory;
import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;
import fr.whimtrip.ext.jwhthtmltopojo.adapter.AbstractHtmlFieldImpl;

/**
 *
 * <p>
 *     An Html Adapter Factory can help creating custom
 *     {@link HtmlAdapter} to plug them instead of the default
 *     implementation provided here {@link AbstractHtmlFieldImpl}
 *     by the default factory {@link DefaultHtmlAdapterFactory}.
 * </p>
 *
 * <p>
 *     It can be fetch when creating your {@link HtmlToPojoEngine}
 *     with method {@link HtmlToPojoEngine#create(HtmlAdapterFactory)}.
 * </p>
 *
 * Part of project jwht-htmltopojo
 *
 * @author Louis-wht
 * @since 1.0.0
 *
 */
public interface HtmlAdapterFactory{

    /**
     * @param htmlToPojoEngine the engine that required the creation of
     *                         a new adapter.
     * @param tClass the class for which the adapter shall be created.
     * @param <T> the type T of the class to create an {@link HtmlAdapter}
     *            for.
     * @return instanciated HtmlAdater for the submitted {@code tClass}.
     */
    <T> HtmlAdapter<T> instanciateAdapter(HtmlToPojoEngine htmlToPojoEngine, Class<T> tClass);
}
