package fr.whimtrip.ext.jwhthtmltopojo.intrf;

import org.jsoup.nodes.Element;

/**
 * Interface for class differentiation.
 * @author Sam Cheshire
 * <p>
 * @param <T> The parent class to differentiate from
 */
public interface HtmlDifferentiator<T>
{
    /**
     * Determine which subclass of T should be instantiated and deserialised to for a given element.
     * @param e The element selected for the deserialisation of the object
     * @return The subclass of T to use
     */
    Class<? extends T> differentiate(Element e);
}
