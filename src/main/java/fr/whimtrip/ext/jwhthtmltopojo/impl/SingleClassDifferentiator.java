package fr.whimtrip.ext.jwhthtmltopojo.impl;

import java.lang.reflect.Field;

import org.jsoup.nodes.Element;

import fr.whimtrip.core.util.exception.ObjectCreationException;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.SubClass;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlDifferentiator;

public class SingleClassDifferentiator<T> implements HtmlDifferentiator<T>
{
    private Class<? extends T> subClass;

    @Override
    public void init(Field field, Object parentObject, Selector selector) throws ObjectCreationException
    {
        SubClass subClass = field.getAnnotation(SubClass.class);
        if (subClass == null)
        {
            throw new ObjectCreationException(field, this.getClass(), SubClass.class);
        }
        try
        {
            this.subClass = (Class<? extends T>) subClass.value();
        }
        catch (ClassCastException e)
        {
            throw new ObjectCreationException(field, this.getClass(), subClass.value());
        }
    }

    @Override
    public Class<? extends T> differentiate(Element e)
    {
        return subClass;
    }

}
