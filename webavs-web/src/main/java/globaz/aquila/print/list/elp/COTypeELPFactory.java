package globaz.aquila.print.list.elp;

import aquila.ch.eschkg.Document;
import aquila.ch.eschkg.EnvelopeType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class COTypeELPFactory {

    public static COTypeMessageELP getCOTypeMessageELPFromXml(Document doc)  {
        Method[] methods = doc.getClass().getMethods();
        for(Method method:methods) {

            if((method.getName().startsWith("get"))) {
                Object o = null;
                try {
                    o = method.invoke(doc);
                    if(o != null && !(o instanceof EnvelopeType)) {
                        COTypeMessageELP type = COTypeMessageELP.getFromCode(method.getName().substring(3, method.getName().length()).toUpperCase());
                        if(type != null) {
                            return type;
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    continue;
                }
            }

        }
        return null;
    }
}
