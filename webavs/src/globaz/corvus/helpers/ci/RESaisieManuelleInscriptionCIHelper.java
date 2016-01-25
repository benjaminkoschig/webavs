/*
 * Créé le 13 juil. 07
 */

package globaz.corvus.helpers.ci;

import globaz.corvus.db.annonces.REAnnonceInscriptionCI;
import globaz.corvus.db.ci.REInscriptionCI;
import globaz.corvus.vb.ci.REInscriptionCIListViewBean;
import globaz.corvus.vb.ci.REInscriptionCIViewBean;
import globaz.corvus.vb.ci.RESaisieManuelleInscriptionCIViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author BSC
 */

public class RESaisieManuelleInscriptionCIHelper extends PRAbstractHelper {

    // ~ Fields
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_init(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        RESaisieManuelleInscriptionCIViewBean vb = (RESaisieManuelleInscriptionCIViewBean) viewBean;

        // on recherche les inscriptions pour ce rassemblement
        REInscriptionCIListViewBean rciManager = new REInscriptionCIListViewBean();
        rciManager.setSession((BSession) session);
        rciManager.setForIdRCI(vb.getIdRCI());
        rciManager.setOrderBy(REInscriptionCI.FIELDNAME_ID_INSCRIPTION + " DESC");
        rciManager.find();

        if (rciManager.size() > 0) {

            REInscriptionCIViewBean lastInscription = null;

            // // si il y a deja des inscriptions, on se base sur la plus
            // recente
            // // pour construire la nouvelle inscription.
            // for (int i = 0; i < rciManager.size(); i++) {
            // REInscriptionCIViewBean currentInscription =
            // (REInscriptionCIViewBean)rciManager.getEntity(i);
            //
            // if(lastInscription == null ||
            // Integer.parseInt(currentInscription.getAnneeCotisations()) >
            // Integer.parseInt(lastInscription.getAnneeCotisations()) ){
            // lastInscription = currentInscription;
            // }
            // }

            // on se base sur la derniere des inscriptions
            lastInscription = (REInscriptionCIViewBean) rciManager.getFirstEntity();

            vb.setNumeroCaisse(lastInscription.getNumeroCaisse());
            vb.setNumeroAgence(lastInscription.getNumeroAgence());

            // annee + 1
            if (!JadeStringUtil.isIntegerEmpty(lastInscription.getAnneeCotisations())) {
                vb.setAnneeCotisations(Integer.toString(Integer.parseInt(lastInscription.getAnneeCotisations()) + 1));
            }
            vb.setMoisDebutCotisations(lastInscription.getMoisDebutCotisations());
            vb.setMoisFinCotisations(lastInscription.getMoisFinCotisations());
            vb.setBrancheEconomique(lastInscription.getBrancheEconomique());
            vb.setGenreCotisation(lastInscription.getGenreCotisation());
            vb.setRevenu("");
            vb.setCodeADS(lastInscription.getCodeADS());
            vb.setPartBonifAssist(lastInscription.getPartBonifAssist());
            vb.setCodeSpecial(lastInscription.getCodeSpecial());
            vb.setNoAffilie(lastInscription.getNoAffilie());
            vb.setCodeExtourne(lastInscription.getCodeExtourne());
            // codeParticulier = codeSplitting
            vb.setCodeParticulier(lastInscription.getCodeParticulier());
        }
    }

    /**
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface ajouterInscriptionCI(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        // Reprise du viewBean
        RESaisieManuelleInscriptionCIViewBean saisieInsCI = (RESaisieManuelleInscriptionCIViewBean) viewBean;

        BTransaction transaction = null;

        try {
            // Création de la transaction
            transaction = (BTransaction) (session).newTransaction();
            transaction.openTransaction();

            // Creation de la nouvelle annonce CI
            REAnnonceInscriptionCI annCI = new REAnnonceInscriptionCI();
            annCI.setSession(session);
            annCI.setIdTiers(saisieInsCI.getIdTiers());
            annCI.setNumeroCaisse(saisieInsCI.getNumeroCaisse());
            if (JadeStringUtil.isEmpty(saisieInsCI.getNumeroAgence())) {
                annCI.setNumeroAgence("000");
            } else {
                annCI.setNumeroAgence(saisieInsCI.getNumeroAgence());
            }
            annCI.setMoisDebutCotisations(saisieInsCI.getMoisDebutCotisations());
            annCI.setMoisFinCotisations(saisieInsCI.getMoisFinCotisations());
            annCI.setAnneeCotisations(saisieInsCI.getAnneeCotisations());
            annCI.setBrancheEconomique(saisieInsCI.getBrancheEconomique());
            annCI.setGenreCotisation(saisieInsCI.getGenreCotisation());
            annCI.setRevenu(saisieInsCI.getRevenu());
            annCI.setCodeADS(saisieInsCI.getCodeADS());
            annCI.setPartBonifAssist(saisieInsCI.getPartBonifAssist());
            annCI.setCodeSpeciale(saisieInsCI.getCodeSpecial());
            annCI.setNoAffilie(saisieInsCI.getNoAffilie());
            annCI.setProvenance("2");
            annCI.setCodeExtourne(saisieInsCI.getCodeExtourne());
            annCI.setCodeParticulier(saisieInsCI.getCodeParticulier());
            annCI.setAttenteCIAdditionnelCS(saisieInsCI.getAttenteCIAdditionnel());
            // annCI.setActif(saisieInsCI.getActif());
            // annCI.setAttenteCiAdd(saisieInsCI.getAttenteCiAdd());
            annCI.add(transaction);

            // Creation de la nouvelle inscription
            REInscriptionCI insCI = new REInscriptionCI();
            insCI.setSession(session);
            insCI.setIdArc(annCI.getIdAnnonce());
            insCI.setIdRCI(saisieInsCI.getIdRCI());
            insCI.setAttenteCiAdd(saisieInsCI.getAttenteCiAdd());

            insCI.add(transaction);

            if (!transaction.hasErrors() || !session.hasErrors()) {
                transaction.commit();
            } else {
                saisieInsCI._addError(transaction.getErrors().toString());
                transaction.rollback();
            }
        } catch (Exception e) {
            saisieInsCI._addError(e.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }

        return saisieInsCI;
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }
}
