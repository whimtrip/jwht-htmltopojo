/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo.htmltopojo;

import fr.whimtrip.core.util.WhimtripUtils;
import fr.whimtrip.core.util.exception.ObjectCreationException;
import fr.whimtrip.ext.jwhthtmltopojo.htmltopojo.annotation.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts HTML strings to Java.
 */
public class HtmlAdapter<T> {
    private HtmlToPojoEngine htmlToPojoEngine;
    private Class<T> clazz;
    private Map<String, HtmlField<T>> htmlFieldCache;
    
    private List<HtmlToPojoAnnotationMap<Inject>> injectFields = new ArrayList<>();
    private List<HtmlToPojoAnnotationMap<Injected>> injectedFields = new ArrayList<>();
    private List<HtmlToPojoAnnotationMap<HasLink>> hasListFields = new ArrayList<>();
    private List<HtmlToPojoAnnotationMap<InjectParent>> injectParentFields = new ArrayList<>();



    public static class HtmlToPojoAnnotationMap<U>
    {
        String name;
        U annotation;
        Field field;

        public String getName() {
            return name;
        }

        public U getAnnotation() {
            return annotation;
        }

        public Field getField() {
            return field;
        }
    }

    private Map<Class<?>, List> annotatedFields =  new LinkedHashMap<>();


    HtmlAdapter(HtmlToPojoEngine htmlToPojoEngine, Class<T> clazz) {
        this.htmlToPojoEngine = htmlToPojoEngine;
        this.clazz = clazz;
        htmlFieldCache = new LinkedHashMap<>();

        annotatedFields.put(Injected.class, injectedFields);
        annotatedFields.put(Inject.class, injectFields);
        annotatedFields.put(HasLink.class, hasListFields);
        annotatedFields.put(InjectParent.class, injectParentFields);

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            Class<?> fieldClass = field.getType();

            // Annotated field
            Selector selector = field.getAnnotation(Selector.class);

            // Not annotated field of annotated class
            if (selector == null) {
                selector = fieldClass.getAnnotation(Selector.class);
            }

            // Not annotated field - List of annotated type
            if (selector == null && List.class.isAssignableFrom(fieldClass)) {
                selector = getSelectorFromListType(field);
            }

            if (selector != null) {
                addCachedHtmlField(field, selector, fieldClass);
            }

            addAnnotatedField(field);

        }
    }

    private <U> void addAnnotatedField(final Field field)
    {

        Annotation[] annotations = field.getAnnotations();
        for (Annotation a : annotations)
        {
            U annotation = (U) a;

            List<HtmlToPojoAnnotationMap<U>> fieldsConcerned = (List<HtmlToPojoAnnotationMap<U>>) annotatedFields.get(a.getClass());
            if(fieldsConcerned == null)
            {
                fieldsConcerned = new ArrayList<>();
                annotatedFields.put(a.annotationType(), fieldsConcerned);
            }


            HtmlToPojoAnnotationMap<U> htmlToPojoAnnotationMap = new HtmlToPojoAnnotationMap<U>();
            htmlToPojoAnnotationMap.annotation = annotation;
            htmlToPojoAnnotationMap.field = field;
            htmlToPojoAnnotationMap.name = field.getName();

            fieldsConcerned.add(htmlToPojoAnnotationMap);

        }
    }


    public <U> List<HtmlToPojoAnnotationMap<U>> getFieldList(Class<? extends U> clazz)
    {
        return (List<HtmlToPojoAnnotationMap<U>>) annotatedFields.get(clazz);
    }

    public <U> HtmlToPojoAnnotationMap<U> getfield(String name, Class<? extends U> clazz)
    {
        List<HtmlToPojoAnnotationMap<U>> htmlToPojoAnnotationMaps = getFieldList(clazz);
        for(HtmlToPojoAnnotationMap<U> htmlToPojoAnnotationMap :  htmlToPojoAnnotationMaps)
        {
            if(htmlToPojoAnnotationMap.name.equals(name))
                return htmlToPojoAnnotationMap;
        }
        return null;
    }



    /**
     * Converts html string to {@code T} object.
     * @param htmlContent String with HTML content
     * @return Created object
     */
    public T fromHtml(String htmlContent) {
        Element pageRoot = Jsoup.parse(htmlContent);
        return loadFromNode(pageRoot);
    }

    public T fromHtml(String htmlContent, T obj) {
        Element pageRoot = Jsoup.parse(htmlContent);
        return loadFromNode(pageRoot, obj);
    }

    private Selector getSelectorFromListType(Field field) {
        Type genericType = field.getGenericType();
        Class<?> listClass = (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
        return listClass.getAnnotation(Selector.class);
    }

    private void addCachedHtmlField(Field field, Selector selector, Class<?> fieldClass) {
        HtmlField<T> htmlField;
        if (List.class.isAssignableFrom(fieldClass)) {
            htmlField = new HtmlListField<>(field, selector);
        } else if (HtmlToPojoUtils.isSimple(fieldClass)) {
            htmlField = new HtmlSimpleField<>(field, selector);
        } else {
            htmlField = new HtmlClassField<>(field, selector);
        }
        htmlFieldCache.put(field.getName(), htmlField);
    }

    private T loadFromNode(Element node) {
        return loadFromNode(node, createNewInstance());
    }

    protected T loadFromNode(Element node, T newInstance) {
        for (HtmlField<T> htmlField : htmlFieldCache.values()) {
            htmlField.setValue(htmlToPojoEngine, node, newInstance);
        }
        return newInstance;
    }

    private T createNewInstance() {
        return WhimtripUtils.createNewInstance(clazz);
    }


    public <M> T createNewInstance(M parentObj ) {
        return buildInstance((T) WhimtripUtils.createNewInstance(clazz), parentObj);
    }

    private <M, R> T buildInstance(T newInstance, M parentObj) {
        for(HtmlToPojoAnnotationMap<Inject> injector : injectFields)
        {
            List<HtmlToPojoAnnotationMap<Injected>> injectedFields = htmlToPojoEngine
                    .adapter(parentObj.getClass())
                    .getFieldList(Injected.class);

            HtmlToPojoAnnotationMap<Injected> injected = null;

            for(HtmlToPojoAnnotationMap<Injected> injectedField : injectedFields)
            {
                if(injectedField.getAnnotation().value().equals(injector.annotation.value()))
                    injected = injectedField;
            }


            List<HtmlToPojoAnnotationMap<InjectParent>> injectParentFields = htmlToPojoEngine
                    .adapter(newInstance.getClass())
                    .getFieldList(InjectParent.class);

            for(HtmlToPojoAnnotationMap<InjectParent> injectParent : injectParentFields)
            {
                try {
                    WhimtripUtils.setObjectToField(injectParent.getField(), newInstance, parentObj);
                }
                catch(IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }

            if(injected == null)
                throw new ObjectCreationException(
                        ". field with name " + injector.getAnnotation().value() + " wasn't found on model "
                                + parentObj.getClass() + " while creating model of class " + newInstance.getClass()
                );

            try {
                R toBeInjectedObj = WhimtripUtils.getObjectFromField(injected.getField(), parentObj);
                WhimtripUtils.setObjectToField(injector.getField(), newInstance, toBeInjectedObj);
            }
            catch(IllegalAccessException e)
            {
                throw new ObjectCreationException(e);
            }

        }
        return newInstance;
    }


}
