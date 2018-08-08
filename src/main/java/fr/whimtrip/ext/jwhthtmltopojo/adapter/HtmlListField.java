



package fr.whimtrip.ext.jwhthtmltopojo.adapter;

import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;
import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoUtils;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.AcceptObjectIf;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.exception.ConversionException;
import fr.whimtrip.ext.jwhthtmltopojo.exception.ParseException;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.AcceptIfResolver;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlAdapter;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlDeserializer;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * <p>Part of project jwht-htmltopojo</p>
 *
 * <p>
 *     List fields html parser. Given an HTML node and a selector, this
 *     class field can parse it to a corresponding List
 *     (dates, int, long, float, double, boolean, strings) of simple types
 *     or a list of POJO using an internal {@link HtmlAdapter} for serializing
 *     each corresponding sub element of the HTML node into a sub POJO type.
 * </p>
 *
 * @param <T> the type of the field
 * @author Louis-wht
 * @since 1.0.0
 */
public class HtmlListField<T> extends AbstractHtmlFieldImpl<T> {


    private HtmlAdapter innerAdapter;

    HtmlListField(Field field, Selector selector) {
        super(field, selector);
    }



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
     * @throws ParseException if the HTML element cannot be properly parsed.
     * @throws ConversionException when the node result cannot be pre or post
     *                             converted with the field attributed
     *                             {@link HtmlDeserializer}.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T getRawValue(HtmlToPojoEngine htmlToPojoEngine, Element node, T parentObject) throws ParseException, ConversionException {
        Elements nodes = selectChildren(node);

        Type genericType = getField().getGenericType();
        Type type = ((ParameterizedType) genericType).getActualTypeArguments()[0];
        Class<?> listClass = (Class<?>) type;

        return (T) populateList(htmlToPojoEngine, nodes, listClass, parentObject);
    }

    @SuppressWarnings("unchecked")
    private <V> List<V> populateList(HtmlToPojoEngine htmlToPojoEngine, Elements nodes, Class<V> listClazz, T parentObj)
            throws ParseException, ConversionException
    {
        List<V> newInstanceList = new ArrayList<>();
        if (HtmlToPojoUtils.isSimple(listClazz)) {
            for (Element node : nodes) {
                newInstanceList.add(instanceForNode(node, listClazz, parentObj));
            }
        } else {
            HtmlAdapter<V> htmlAdapter = (HtmlAdapter<V>) getHtmlAdapter(listClazz, htmlToPojoEngine);

            AcceptObjectIf acceptObjectIf = getField().getAnnotation(AcceptObjectIf.class);
            boolean alwaysFetch = false;
            if(acceptObjectIf == null)
                alwaysFetch = true;

            // This will ensure only one Resolver per type instanciated
            Map<Class<? extends AcceptIfResolver>, AcceptIfResolver> resolverCache = new HashMap<>();

            for (Element node : nodes) {
                if(alwaysFetch || innerShoudlBeFetched(node, parentObj, acceptObjectIf, resolverCache))
                {
                    newInstanceList.add(htmlAdapter.loadFromNode(node, htmlAdapter.createNewInstance(parentObj)));
                }
            }
        }
        return newInstanceList;
    }

    @SuppressWarnings("unchecked")
    private HtmlAdapter getHtmlAdapter(Class listClazz, HtmlToPojoEngine htmlToPojoEngine) {
        if(innerAdapter == null)
            innerAdapter = htmlToPojoEngine.adapter(listClazz);
        return innerAdapter;
    }
}