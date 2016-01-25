package globaz.amal.utils;

import globaz.globall.db.BSession;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.Arrays;
import java.util.Iterator;
import javax.servlet.ServletRequest;

public class AMParametresHelper {
    public static String getOngletHtml(BSession objSession, BJadePersistentObjectViewBean viewBean,
            String[][] codeOnglet, ServletRequest request, String path) {
        String theUsrAction = request.getParameter("userAction");
        String paramURL = ".afficher";
        String ongletLink = path + "?userAction=";
        String li = "";
        int cpt = 0;

        String titreMenu = "&idTitreMenu=" + request.getParameter("idTitreMenu");

        for (Iterator it = Arrays.asList(codeOnglet).iterator(); it.hasNext();) {
            cpt++;
            String[] tuple = (String[]) it.next();
            if ((tuple[2] + ".afficher").equals(theUsrAction)) {
                li = li + "<li class='selected'>" + objSession.getLabel(tuple[0]) + "</li>";
            } else {
                li = li + "<li><a id='paramLink" + cpt + "' href=\"" + ongletLink + tuple[2] + paramURL + titreMenu
                        + "&titreOnglet=" + objSession.getLabel(tuple[0]) + "\" title=\""
                        + objSession.getLabel(tuple[0]) + "\">" + objSession.getLabel(tuple[1]) + "</a>";
            }

        }
        return "<ul class='onglets'>" + li + "</ul>";
    }
}
