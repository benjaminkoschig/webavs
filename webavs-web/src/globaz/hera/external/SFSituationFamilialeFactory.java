package globaz.hera.external;

import globaz.globall.db.BSession;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Descpription
 * 
 * Factory Permet d'avoir une instance de la situation familiale en fonction d'un domaine
 * 
 * Si aucun requ�rant n'existe dans le domaine concern�, l'impl�mentation de l'interface sera toujours celle du domaine
 * (csDomaine) par contre le domaine sera le domaine Standard.
 * 
 * Ex. csDomaine = IJ, idTiers = 123 Si le tiers 123 n'existe pas dans le domaine IJ, l'impl�mentation de la situation
 * familiale sera l'impl�mentation du domaine IJ, par contre le domaine dans lequel se feront les recherches sur les
 * membres de la famille se feront dans le domaine standard.
 * 
 * @author SCR
 */
public class SFSituationFamilialeFactory {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Permet d'avoir une instance de la situation familiale en fonction d'un domaine
     * 
     * Si aucun requ�rant n'existe dans le domaine concern�, l'impl�mentation de l'interface sera toujours celle du
     * domaine (csDomaine) par contre le domaine sera le domaine Standard.
     * 
     * Ex. csDomaine = IJ, idTiers = 123 Si le tiers 123 n'existe pas dans le domaine IJ, l'impl�mentation de la
     * situation familiale sera l'impl�mentation du domaine IJ, par contre le domaine dans lequel se feront les
     * recherches sur les membres de la famille se feront dans le domaine standard.
     * 
     * 
     * @param session
     * @param domaine
     *            : Domaine d'application, donner une valeure static de la classe ex:
     *            SFSituationFamilialeFactory.CS_DOMAINE_STANDARD
     * 
     * @return la situation familiale du domaine consid�r�..
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static ISFSituationFamiliale getSituationFamiliale(BSession session, String csDomaine,
            String idTiersRequerant) throws Exception {

        ISFSituationFamiliale sf = null;

        // Si pas de domaine de donn�, le domaine par d�faut est le domaine
        // standard
        if (JadeStringUtil.isEmpty(csDomaine) || ISFSituationFamiliale.CS_DOMAINE_STANDARD.equals(csDomaine)) {
            sf = (ISFSituationFamiliale) session.getAPIFor(globaz.hera.api.ISFSituationFamiliale.class);
        } else if (ISFSituationFamiliale.CS_DOMAINE_INDEMNITEE_JOURNALIERE.equals(csDomaine)) {
            sf = (ISFSituationFamiliale) session
                    .getAPIFor(globaz.hera.api.indemniteJournaliere.ISFSituationFamiliale.class);
        } else if (ISFSituationFamiliale.CS_DOMAINE_RENTES.equals(csDomaine)) {
            sf = (ISFSituationFamiliale) session.getAPIFor(globaz.hera.api.rentes.ISFSituationFamiliale.class);
        } else if (ISFSituationFamiliale.CS_DOMAINE_CALCUL_PREVISIONNEL.equals(csDomaine)) {
            sf = (ISFSituationFamiliale) session
                    .getAPIFor(globaz.hera.api.calculprevisionnel.ISFSituationFamiliale.class);
        } else {
            throw new Exception("Domaine invalide:" + csDomaine);
        }

        // Recherche hirarchique de la situation familiale
        // 1er niveau : dans le domaine concern�
        // 2�me niveau : dans le domaine standard

        // Exception pour le calcul pr�visionnel :
        // 1er niveau : Calcul pr�visionnel
        // 2�me niveau : Rente
        // 3�me niveau : Standard

        // Reflexion ne suporte pas les null
        if (idTiersRequerant == null) {
            idTiersRequerant = "";
        }

        if (csDomaine == null) {
            csDomaine = "";
        }

        Boolean isRequerant = sf.isRequerant(idTiersRequerant, csDomaine);
        if (isRequerant != null && isRequerant.booleanValue()) {
            return sf;
        } else {
            if (ISFSituationFamiliale.CS_DOMAINE_CALCUL_PREVISIONNEL.equals(csDomaine)) {
                sf = (ISFSituationFamiliale) session.getAPIFor(globaz.hera.api.rentes.ISFSituationFamiliale.class);
                isRequerant = sf.isRequerant(idTiersRequerant, csDomaine);
                if (isRequerant != null && isRequerant.booleanValue()) {
                    return sf;
                }
            }
            sf.setDomaine(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
            return sf;
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private SFSituationFamilialeFactory() {
        super();
    }
}
