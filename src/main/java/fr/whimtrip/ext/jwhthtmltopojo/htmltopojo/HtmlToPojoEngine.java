/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo.htmltopojo;

import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.intfr.HtmlAdapterFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Coordinates binding between HTML values and Java objects.
 */
public class HtmlToPojoEngine {
    private final Map<String, HtmlAdapter<?>> adapterCache;
    private final HtmlAdapterFactory htmlAdapterFactory;

    /**
     * Creates a new Jspoon instance.
     * @return a new Jspoon instance
     */
    public static HtmlToPojoEngine create() {
        return new HtmlToPojoEngine(null);
    }

    /**
     * Creates a new Jspoon instance.
     * @param htmlAdapterFactory the factory that will instanciate HtmlAdapters
     * @return a new Jspoon instance
     */
    public static HtmlToPojoEngine create(HtmlAdapterFactory htmlAdapterFactory) {
        return new HtmlToPojoEngine(htmlAdapterFactory);
    }

    private HtmlToPojoEngine(HtmlAdapterFactory htmlAdapterFactory) {

        this.htmlAdapterFactory = htmlAdapterFactory;
        this.adapterCache = new LinkedHashMap<>();
    }

    /**
     * Returns a HTML adapter for {@code clazz}, creating it if necessary.
     * @param clazz Class for creating objects
     * @param <T> Class for creating objects
     * @return {@link HtmlAdapter} instance
     */
    @SuppressWarnings("unchecked")
    public <T> HtmlAdapter<T> adapter(Class<T> clazz) {
        String key = clazz.getName();
        if (adapterCache.containsKey(key)) {
            return (HtmlAdapter<T>) adapterCache.get(key);
        } else {
            HtmlAdapter<T> htmlAdapter = htmlAdapterFactory.instanciateAdapter(this, clazz);
            adapterCache.put(key, htmlAdapter);
            return htmlAdapter;
        }
    }

}
