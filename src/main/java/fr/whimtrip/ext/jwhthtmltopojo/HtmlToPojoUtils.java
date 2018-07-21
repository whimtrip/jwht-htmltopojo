/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhthtmltopojo;

import org.joda.time.DateTime;
import org.jsoup.nodes.Element;

import java.util.Date;
import java.util.List;

public class HtmlToPojoUtils {
    public static boolean isSimple(Class clazz) {
        return clazz.equals(String.class) ||
                clazz.equals(Integer.class) || clazz.equals(int.class) ||
                clazz.equals(Long.class) || clazz.equals(long.class) ||
                clazz.equals(Float.class) || clazz.equals(float.class) ||
                clazz.equals(Double.class) || clazz.equals(double.class) ||
                clazz.equals(Boolean.class) || clazz.equals(boolean.class) ||
                clazz.equals(Element.class) ||
                clazz.equals(List.class) ||
                clazz.equals(Date.class) || clazz.equals(DateTime.class);
    }
}
