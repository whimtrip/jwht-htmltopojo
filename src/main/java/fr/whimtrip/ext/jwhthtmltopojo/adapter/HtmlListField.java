



package fr.whimtrip.ext.jwhthtmltopojo.adapter;

import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;
import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoUtils;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.exception.FieldShouldNotBeSetException;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.AcceptIfResolver;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlAdapter;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * List fields html parser. Given an HTML node and a selector, this
 * class field can parse it to a corresponding List
 * (dates, int, long, float, double, boolean, strings) of simple types
 * or a list of POJO using an internal {@link HtmlAdapter} for serializing
 * each corresponding sub element of the HTML node into a sub POJO type.
 *
 * @param <T> the type of the field
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
     * @throws FieldShouldNotBeSetException when the field should not be set. It happens when there is an
     *                                      {@link AcceptIfResolver} providing a {@code false} value in which
     *                                      case the field should not be set.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T getRawValue(HtmlToPojoEngine htmlToPojoEngine, Element node, T parentObject) {
        Elements nodes = selectChildren(node);

        Type genericType = field.getGenericType();
        Type type = ((ParameterizedType) genericType).getActualTypeArguments()[0];
        Class<?> listClass = (Class<?>) type;

        return (T) populateList(htmlToPojoEngine, nodes, listClass, parentObject);
    }

    @SuppressWarnings("unchecked")
    private <V> List<V> populateList(HtmlToPojoEngine htmlToPojoEngine, Elements nodes, Class<V> listClazz, T parentObj) {
        List<V> newInstanceList = new ArrayList<>();
        if (HtmlToPojoUtils.isSimple(listClazz)) {
            for (Element node : nodes) {
                newInstanceList.add(instanceForNode(node, listClazz, parentObj));
            }
        } else {
            HtmlAdapter<V> htmlAdapter = (HtmlAdapter<V>) getHtmlAdapter(listClazz, htmlToPojoEngine);
            for (Element node : nodes) {
                if(shouldBeFetched(node, parentObj))
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