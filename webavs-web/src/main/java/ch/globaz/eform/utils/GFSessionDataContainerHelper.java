package ch.globaz.eform.utils;

import ch.globaz.eform.business.search.GFFormulaireSearch;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

public class GFSessionDataContainerHelper implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String KEY_EFORM_PARAMETRE = "ch.globaz.eform.utils.GFSessionDataContainerHelper.EFORM_PARAMETRE";

    public static void putGFFormulaireSearchOnSession(HttpSession session, GFFormulaireSearch viewBean) {
        session.setAttribute(KEY_EFORM_PARAMETRE, viewBean);
    }

    public static GFFormulaireSearch getGFFormulaireSearchFromSession(HttpSession session) {
        Object attribute = session.getAttribute(KEY_EFORM_PARAMETRE);
        return  attribute instanceof GFFormulaireSearch ? (GFFormulaireSearch) attribute : null;
    }

    private GFSessionDataContainerHelper() {
        // ne peut être instanciée.
    }
}
