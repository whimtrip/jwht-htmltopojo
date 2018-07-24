package fr.whimtrip.ext.jwhthtmltopojo;

import fr.whimtrip.ext.jwhthtmltopojo.adapter.DefaultHtmlAdapterImpl;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlAdapter;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlAdapterFactory;

/**
 *
 * Default implementation to provide a {@link DefaultHtmlAdapterImpl}
 * factory for the default {@link HtmlToPojoEngine}.
 *
 * Part of project jwht-htmltopojo
 *
 * @author Louis-wht
 * @since 24/07/18
 */
public class DefaultHtmlAdapterFactory implements HtmlAdapterFactory {
    @Override
    public <T> HtmlAdapter<T> instanciateAdapter(HtmlToPojoEngine htmlToPojoEngine, Class<T> tClass) {
        return new DefaultHtmlAdapterImpl<>(htmlToPojoEngine, tClass);
    }
}
