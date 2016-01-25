package globaz.corvus.dao;

import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.external.IntRole;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;

/**
 * 
 * <H1>Description</H1>
 * 
 * 
 * 
 * 
 * 
 * @author scr
 */

public final class REInfoCompta {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * Création ou Retrieve du compte annexe, et maj de son ID dans la zone infoCompta. Doit être appelée lors de la
     * validation de la décision.
     * 
     * @return idCompteAnnexe
     */
    public static String initCompteAnnexe_noCommit(BSession session, BITransaction transaction,
            String idTiersBeneficiaire, REInformationsComptabilite ic, int validationLevel) throws Exception {

        ic.setSession(session);

        // La création du compte annexe doit se faire lors de la validation de
        // la décision.
        // Ceci pour éviter de créer potentiellement des compte annexes
        // inutiles.

        // instanciation du processus de compta
        BISession sessionOsiris = PRSession.connectSession(session, "OSIRIS");
        APICompteAnnexe cptAnnexe = (APICompteAnnexe) sessionOsiris.getAPIFor(APICompteAnnexe.class);

        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, idTiersBeneficiaire);
        if (tw == null || JadeStringUtil.isEmpty(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL))) {
            throw new Exception("Tiers not found, or NSS is blank. idTiers = " + idTiersBeneficiaire);
        }
        APICompteAnnexe ca = cptAnnexe.createCompteAnnexe(sessionOsiris, transaction, idTiersBeneficiaire,
                IntRole.ROLE_RENTIER, tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
        ic.setIdCompteAnnexe(ca.getIdCompteAnnexe());
        ic.update(transaction);

        return ca.getIdCompteAnnexe();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private REInfoCompta() {
        // peut pas creer d'instances
    }
}
