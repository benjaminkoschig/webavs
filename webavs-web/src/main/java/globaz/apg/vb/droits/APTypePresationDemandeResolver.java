package globaz.apg.vb.droits;

import globaz.prestation.api.PRTypeDemande;
import globaz.prestation.tools.PRSessionDataContainerHelper;

import javax.servlet.http.HttpSession;

public class APTypePresationDemandeResolver {

    public static String resolveTypePrestation(HttpSession httpSession) {
        return (String) httpSession.getAttribute(PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION);
    }

    public static PRTypeDemande resolveEnumTypePrestation(HttpSession httpSession) {
        return PRTypeDemande.toEnumByCs(resolveTypePrestation(httpSession));
    }

}
