



package fr.whimtrip.ext.jwhthtmltopojo.intrf;

import fr.whimtrip.ext.jwhthtmltopojo.impl.AcceptIfFirst;
import fr.whimtrip.ext.jwhthtmltopojo.impl.AcceptIfValidAttrRegexCheck;
import org.jsoup.nodes.Element;

/**
 *
 * Part of project jwht-htmltopojo
 *
 * Accept If Resolver implementation can be used to provide conditional parsing
 * of some fields. Some good examples of custom implementations can be found here :
 * {@link AcceptIfFirst} or here {@link AcceptIfValidAttrRegexCheck}.
 *
 * Any implementation can then be used as following :
 *
 *<pre>
 *     &#64;AcceptObjectIf(MyCustomAcceptIfResolver.class)
 *     private SomePojo myConditionalPojoField;
 *</pre>
 *
 * @author Louis-wht
 * @since 1.0.0
 */
public interface AcceptIfResolver extends Initiate {

    /**
     * @param element the Html element we need to resolve the acceptancy for.
     * @param parentObject the parent object the field belongs to.
     * @return false if the field shouldn't be fetched or true if it should be.
     */
    boolean accept(Element element, Object parentObject);
}
