package globaz.lynx.translation;

import globaz.framework.translation.FWTranslation;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCodeManager;

public class LXCodeSystem {

    private static FWParametersSystemCodeManager csCategories = null;
    private static FWParametersSystemCodeManager csCodeIsoMonnaie = null;
    private static FWParametersSystemCodeManager csCodeTva = null;
    private static FWParametersSystemCodeManager csEtatJour = null;
    private static FWParametersSystemCodeManager csEtatOper = null;
    private static FWParametersSystemCodeManager csEtatOrdreGroupe = null;
    private static FWParametersSystemCodeManager csMotifBlocage = null;
    private static FWParametersSystemCodeManager csTypeOperation = null;

    public static FWParametersSystemCodeManager getCsCategories(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("LXCATEG", session, LXCodeSystem.csCategories);
    }

    public static FWParametersSystemCodeManager getCsCodeIsoMonnaie(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("PYMONNAIE", session, LXCodeSystem.csCodeIsoMonnaie);
    }

    public static FWParametersSystemCodeManager getCsCodeTva(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("LXCODETVA", session, LXCodeSystem.csCodeTva);
    }

    public static FWParametersSystemCodeManager getCsEtatJournal(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("LXETATJOUR", session, LXCodeSystem.csEtatJour);
    }

    public static FWParametersSystemCodeManager getCsEtatOperation(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("LXETATOPER", session, LXCodeSystem.csEtatOper);
    }

    public static FWParametersSystemCodeManager getCsEtatOrdreGroupe(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("LXETATORGR", session, LXCodeSystem.csEtatOrdreGroupe);
    }

    public static FWParametersSystemCodeManager getCsMotifBlocage(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("LXMOTIFBL", session, LXCodeSystem.csMotifBlocage);
    }

    public static FWParametersSystemCodeManager getCsTypeOperation(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("LXTYPEOPER", session, LXCodeSystem.csTypeOperation);
    }

    /**
     * @param httpSession
     * @throws Exception
     */
    public LXCodeSystem() throws Exception {
        super();
    }
}
