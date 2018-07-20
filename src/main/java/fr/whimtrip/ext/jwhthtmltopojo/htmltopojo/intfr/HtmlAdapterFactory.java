package fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.intfr;

import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.HtmlAdapter;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.HtmlToPojoEngine;

public interface HtmlAdapterFactory{

    <T> HtmlAdapter<T> instanciateAdapter(HtmlToPojoEngine htmlToPojoEngine, Class<T> tClass);
}
