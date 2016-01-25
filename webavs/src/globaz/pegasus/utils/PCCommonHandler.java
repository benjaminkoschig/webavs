package globaz.pegasus.utils;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;
import java.util.Arrays;
import java.util.Iterator;
import javax.servlet.ServletRequest;

public class PCCommonHandler {

    public static String getCurrencyFormtedDefault(String text) {
        return PCCommonHandler.getCurrencyFormtedDefault(text, "");
    }

    public static String getCurrencyFormtedDefault(String text, String textDefault) {
        String s = new FWCurrency(text).toStringFormat();
        return PCCommonHandler.getNumeriqueDefault(s, textDefault);
    }

    /**
     * replace the value 0 of the text by ""
     * 
     * @return string
     * @param text
     *            the string to test
     */
    public static String getNumeriqueDefault(String text) {
        return PCCommonHandler.getNumeriqueDefault(text, "");
    }

    /**
     * replace the value 0 of the text by the param textDefault
     * 
     * @return string
     * @param text
     *            the string to test
     * @param textDefault
     *            the string to return if value of text is "null" or "0"
     */
    public static String getNumeriqueDefault(String text, String textDefault) {
        return JadeNumericUtil.isEmptyOrZero(text) ? textDefault : text;
    }

    public static String getOngletHtml(BSession objSession, PCAbstractRequerantDonneeFinanciereViewBean viewBean,
            String[][] codeOnglet, String userAction, String path) {
        String paramURL = ".afficher&noVersion=" + viewBean.getNoVersion() + "&idVersionDroit="
                + viewBean.getIdVersion();
        String theUsrAction = userAction;
        String ongletLink = path + "?idDroit=" + viewBean.getDroit().getId() + "&selectedId="
                + viewBean.getDroit().getId() + "&userAction=";
        String li = "";
        for (Iterator it = Arrays.asList(codeOnglet).iterator(); it.hasNext();) {
            String[] tuple = (String[]) it.next();
            if ((tuple[2] + ".afficher").equals(theUsrAction)) {
                li = li + "<li class='selected'>" + objSession.getLabel(tuple[0]) + "</li>";
            } else {
                li = li + "<li><a href='" + ongletLink + tuple[2] + paramURL + "' title='"
                        + objSession.getLabel(tuple[0]) + "'>" + objSession.getLabel(tuple[1]) + "</a>";
            }

        }
        return "<ul class='onglets'>" + li + "</ul>";
    }

    /**
     * replace the value of "null" or "0" of the text by ""
     * 
     * @return string
     * @param text
     *            the string to test
     */
    public static String getStringDefault(String s) {
        return PCCommonHandler.getStringDefault(s, "");
    }

    /**
     * replace the value of "null" or "0" of the text by the param textDefault
     * 
     * @return string
     * @param text
     *            the string to test
     * @param textDefault
     *            the string to return if value of text is "null" or "0"
     */
    public static String getStringDefault(String text, String textDefault) {
        return JadeStringUtil.isBlankOrZero(text) ? textDefault : text;
    }

    public static String getStrOrNumDefault(String text) {
        return PCCommonHandler.getStrOrNumDefault(text, "");
    }

    public static String getStrOrNumDefault(String text, String textDefault) {
        String s = PCCommonHandler.getNumeriqueDefault(text, textDefault);
        return PCCommonHandler.getStringDefault(s, textDefault);
    }

    public static String getTitre(BSession objSession, ServletRequest request) {
        String idsousGategorie = request.getParameter("idTitreOnglet");
        String sousGategorie = null;
        if (idsousGategorie != null) {
            sousGategorie = objSession.getLabel(idsousGategorie);
        } else {
            sousGategorie = request.getParameter("titreOnglet");
        }
        String titreMenu = objSession.getLabel((request.getParameter("idTitreMenu")));
        return "<span id='titreMenu'>" + titreMenu + "</span> - <span id='titreOnglet'>" + sousGategorie + "</span>";
    }
}
