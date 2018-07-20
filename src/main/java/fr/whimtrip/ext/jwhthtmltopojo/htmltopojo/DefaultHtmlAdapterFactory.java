package fr.whimtrip.ext.jwhthtmltopojo.htmltopojo;

import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.intfr.HtmlAdapterFactory;

public class DefaultHtmlAdapterFactory implements HtmlAdapterFactory {
    @Override
    public <T> HtmlAdapter<T> instanciateAdapter(HtmlToPojoEngine htmlToPojoEngine, Class<T> tClass) {
        return new HtmlAdapter<>(htmlToPojoEngine, tClass);
    }
}
