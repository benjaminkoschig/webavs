package globaz.hermes.utils;

import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

/**
 * Insérez la description du type ici. Date de création : (15.10.2002 14:36:21)
 * 
 * @author: Administrator
 */
public class HttpUtils {
    public static HashMap getParamsAsMap(HttpServletRequest request) {
        HashMap map = new HashMap();
        for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
            String param = (String) e.nextElement();
            if (HEAnnoncesViewBean.isCustomField(param)) {
                String newValue = "";
                for (int i = 0; i < request.getParameterValues(param).length; i++) {
                    newValue += request.getParameterValues(param)[i];
                }
                map.put(param, newValue);
            } else if (HEAnnoncesViewBean.isReferenceInterne(param)) {
                String[] ref = request.getParameterValues(param);
                if (ref.length > 1 && !JadeStringUtil.isEmpty(ref[0])) {
                    map.put(param, ref[0] + "/" + ref[1]);
                } else {
                    map.put(param, ref[0]);
                }
            } else {
                map.put(param, request.getParameter(param));
            }
        }
        return map;
    }

    /*
     * public static String displayURL(HttpServletRequest req) { String retour = req.getRequestURI(); boolean isFirst =
     * true; for (Enumeration e = req.getParameterNames(); e.hasMoreElements();) { if (isFirst) { retour += "?"; isFirst
     * = false; } else { retour += "&"; } String param = (String) e.nextElement(); retour += param + "=" +
     * req.getParameter(param); } return retour; }
     */

    /**
     * Commentaire relatif au constructeur HttpUtils.
     */
    public HttpUtils() {
        super();
    }
}
