package globaz.lynx.utils;

import globaz.globall.db.BSession;
import globaz.lynx.db.organeexecution.LXOrganeExecution;

public class LXOrganeExecutionUtil {

    /**
     * Return le libellé d'un organe d'execution. Utilisé pour les écrans.
     * 
     * @param session
     * @param idOrdreGroupe
     * @return
     */
    public static String getNom(BSession session, String idOrganeExecution) {
        LXOrganeExecution organeExecution = new LXOrganeExecution();
        organeExecution.setSession(session);

        organeExecution.setIdOrganeExecution(idOrganeExecution);

        try {
            organeExecution.retrieve();
        } catch (Exception e) {
            return "";
        }

        if (organeExecution.hasErrors() || organeExecution.isNew()) {
            return "";
        }

        return organeExecution.getNom();
    }

    /**
     * Constructeur
     */
    protected LXOrganeExecutionUtil() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }

}
