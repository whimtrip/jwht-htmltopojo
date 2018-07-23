/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo;

import fr.whimtrip.ext.jwhthtmltopojo.adapter.HtmlAdapter;
import fr.whimtrip.ext.jwhthtmltopojo.intfr.HtmlAdapterFactory;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Coordinates binding between HTML values and Java objects.
 */
public class HtmlToPojoEngine {
    private final Map<String, HtmlAdapter<?>> adapterCache;
    private final HtmlAdapterFactory htmlAdapterFactory;

    /**
     * Creates a new HtmlToPojoEngine instance.
     * @return a new HtmlToPojoEngine instance
     */
    public static HtmlToPojoEngine create() {
        return new HtmlToPojoEngine(new DefaultHtmlAdapterFactory());
    }

    /**
     * Creates a new HtmlToPojoEngine instance.
     * @param htmlAdapterFactory the factory that will instanciate HtmlAdapters
     * @return a new HtmlToPojoEngine instance
     */
    public static HtmlToPojoEngine create(HtmlAdapterFactory htmlAdapterFactory) {
        return new HtmlToPojoEngine(htmlAdapterFactory);
    }

    private HtmlToPojoEngine(@NotNull final HtmlAdapterFactory htmlAdapterFactory) {

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
