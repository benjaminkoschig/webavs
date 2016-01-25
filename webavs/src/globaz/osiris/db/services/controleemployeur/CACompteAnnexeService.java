package globaz.osiris.db.services.controleemployeur;

import globaz.globall.db.BSession;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexe;

/**
 * @author SCO
 * @since 26 nov. 2010
 */
public class CACompteAnnexeService {

    /**
     * Permet de trouver le numéro de compte annexe suivant le numéro d'affilié et l'idrole.
     * 
     * @param session
     * @param idRole
     * @param idExterneRole
     * @return
     * @throws Exception
     */
    public static String getIdCompteAnnexeByRole(BSession session, String idRole, String idExterneRole)
            throws Exception {

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setIdRole(idRole);
        compteAnnexe.setIdExterneRole(idExterneRole);
        compteAnnexe.setSession(session);

        try {
            compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
            compteAnnexe.retrieve();

            if (!compteAnnexe.isNew()) {
                return compteAnnexe.getIdCompteAnnexe();
            }

        } catch (Exception e) {
            throw e;
        }

        return null;
    }

    /**
     * Constructeur de CACompteAnnexeService
     */
    protected CACompteAnnexeService() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
