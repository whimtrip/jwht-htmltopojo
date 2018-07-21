package fr.whimtrip.ext.jwhthtmltopojo.intfr;

import fr.whimtrip.ext.jwhthtmltopojo.HtmlAdapter;
import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;

public interface HtmlAdapterFactory{

    <T> HtmlAdapter<T> instanciateAdapter(HtmlToPojoEngine htmlToPojoEngine, Class<T> tClass);
}
