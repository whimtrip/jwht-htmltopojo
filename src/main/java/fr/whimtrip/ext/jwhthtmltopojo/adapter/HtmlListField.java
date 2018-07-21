/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo.adapter;

import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;
import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoUtils;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class HtmlListField<T> extends HtmlField<T> {
    HtmlListField(Field field, Selector selector) {
        super(field, selector);
    }


    @Override
    public Object getRawValue(HtmlToPojoEngine htmlToPojoEngine, Element node, T newInstance) {
        Elements nodes = selectChildren(node);

        Type genericType = field.getGenericType();
        Type type = ((ParameterizedType) genericType).getActualTypeArguments()[0];
        Class<?> listClass = (Class<?>) type;

        return populateList(htmlToPojoEngine, nodes, listClass, newInstance);
    }

    private <V> List<V> populateList(HtmlToPojoEngine htmlToPojoEngine, Elements nodes, Class<V> listClazz, T parentObj) {
        List<V> newInstanceList = new ArrayList<>();
        if (HtmlToPojoUtils.isSimple(listClazz)) {
            for (Element node : nodes) {
                newInstanceList.add(instanceForNode(node, listClazz));
            }
        } else {
            HtmlAdapter<V> htmlAdapter = htmlToPojoEngine.adapter(listClazz);
            for (Element node : nodes) {
                if(shouldBeFetched(node, field, parentObj))
                {
                    newInstanceList.add(htmlAdapter.loadFromNode(node, htmlAdapter.createNewInstance(parentObj)));
                }
            }
        }
        return newInstanceList;
    }
}