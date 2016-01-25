package ch.globaz.pegasus.navigation;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Map;
import java.util.Map.Entry;
import com.google.gson.Gson;

public class NavigatorLinkHandler {

    private static String createParamters(Map<String, String> map) {
        String param = "";
        for (Entry<String, String> entry : map.entrySet()) {
            param = param + "&" + entry.getKey() + "=" + entry.getValue();
        }
        return param;
    }

    public static NavigatorViewBean createViewBean(NavigatorInterface navigator, Map<String, String> ids)
            throws JadeApplicationException, JadePersistenceException {
        NavigatorViewBean vb = new NavigatorViewBean();
        vb.setLibelle(JadeThread.getMessage(navigator.getLibelle()));
        navigator.setIdsMap(ids);
        navigator.computParmetresLink();
        ids.putAll(navigator.getIdsMap());
        vb.setLink(navigator.getLink() + NavigatorLinkHandler.createParamters(navigator.getParameters()));
        if ((navigator.getNavigatorLinks() != null) && (navigator != null)) {
            for (NavigatorInterface nav : navigator.getNavigatorLinks()) {
                vb.getList().add(NavigatorLinkHandler.createViewBean(nav, ids));
            }
        }
        return vb;
    }

    public static String toJson(NavigatorInterface navigator) throws JadeApplicationException, JadePersistenceException {
        NavigatorViewBean vb = NavigatorLinkHandler.createViewBean(navigator, navigator.getIdsMap());
        Gson gson = new Gson();
        return gson.toJson(vb);
    }
}
