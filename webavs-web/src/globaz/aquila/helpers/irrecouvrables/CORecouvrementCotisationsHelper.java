package globaz.aquila.helpers.irrecouvrables;

import globaz.aquila.db.irrecouvrables.CORecouvrementCotisationsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.osiris.db.irrecouvrable.CARecouvrementBaseAmortissementContainer;
import globaz.osiris.db.irrecouvrable.CARecouvrementBaseAmortissementContainerLoader;
import globaz.osiris.db.irrecouvrable.CARecouvrementPosteContainer;
import globaz.osiris.db.irrecouvrable.CARecouvrementVentilateur;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;

public class CORecouvrementCotisationsHelper extends FWHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        CORecouvrementCotisationsViewBean recouvrementCotisationViewBean = (CORecouvrementCotisationsViewBean) viewBean;

        CARecouvrementVentilateur ventilateur = initVentilateur(session, recouvrementCotisationViewBean);

        if (session.getAttribute("recouvrementPosteContainer") == null) {
            executerVentilateur(ventilateur);
        } else {
            CARecouvrementPosteContainer recouvrementPosteContainer = (CARecouvrementPosteContainer) session
                    .getAttribute("recouvrementPosteContainer");
            BigDecimal montantNoteDeCreditCumul = new BigDecimal(
                    recouvrementCotisationViewBean.getMontantNoteDeCreditCumul());
            ventilateur.setMontantNoteCredit(montantNoteDeCreditCumul);
            exectuteVentilateur(ventilateur, recouvrementPosteContainer);
        }

        fillViewBean(recouvrementCotisationViewBean, ventilateur, session);

        super._retrieve(recouvrementCotisationViewBean, action, session);
    }

    /**
     * 
     * @param viewBean
     * @param idCompteAnnexe
     * @param ventilateur
     * @param session
     * @throws RemoteException
     */
    private void fillViewBean(CORecouvrementCotisationsViewBean viewBean, CARecouvrementVentilateur ventilateur,
            BISession session) throws RemoteException {

        viewBean.setRecouvrementPostesMap(ventilateur.getRecouvrementPostesTries());
        viewBean.setRecouvrementCiMap(ventilateur.getRecouvrementCiTries());
        viewBean.setIdCompteAnnexe(viewBean.getIdCompteAnnexe());
        viewBean.setNumeroAffilie(ventilateur.getCompteAnnexe().getIdExterneRole());

        String nomAffilie = findNomAffilie(ventilateur.getCompteAnnexe().getIdExterneRole(), session);
        viewBean.setNomAffilie(nomAffilie);

        viewBean.setDescriptionAffilie(ventilateur.getCompteAnnexe().getDescription());

        viewBean.setEmail(session.getUserEMail());

        if (ventilateur.getCompteIndividuelAffilie() != null) {
            String compteIndividuelId = ventilateur.getCompteIndividuelAffilie().getCompteIndividuelId();
            viewBean.setIdCompteIndividuelAffilie(compteIndividuelId);
        }

        BigDecimal[] cumulMontantColonnes = ventilateur.getCumulMontantColonnes();
        viewBean.setAmortissementTotal(cumulMontantColonnes[0]);
        viewBean.setDejaRecouvertTotal(cumulMontantColonnes[1]);
        viewBean.setRecouvrementTotal(cumulMontantColonnes[2]);
        viewBean.setSoldeTotal(cumulMontantColonnes[0].subtract(cumulMontantColonnes[1]
                .subtract(cumulMontantColonnes[2])));
        viewBean.setHasRubriqueCotPers(ventilateur.hasRubriqueCotPers());
        viewBean.setTiers(ventilateur.getTiers());

        viewBean.setVentilateur(ventilateur);
    }

    /**
     * Charge le nom correspondant au numéro d'affilié
     * 
     * @param numAffilie numéro d'affilié pour lequel le nom doit être récupéré
     * @param session
     * @return
     */
    private String findNomAffilie(String numAffilie, BISession session) {

        String nomAffilie = "";

        AFAffiliationManager affiliationManager = new AFAffiliationManager();
        affiliationManager.setSession((BSession) session);
        affiliationManager.setForAffilieNumero(numAffilie);

        try {
            affiliationManager.find();

            if (affiliationManager.size() == 1) {
                AFAffiliation affiliation = (AFAffiliation) affiliationManager.get(0);
                nomAffilie = affiliation.getTiersNom();
            } else {
                // Si le nom n'a pas pu être déterminé on retourne une chaîne vide, cette information n'est pas critique
                // pour la suite du traitement
                nomAffilie = "";
            }
        } catch (Exception e) {
            nomAffilie = "";
            JadeLogger.warn(this, "Le nom de l'affilié " + numAffilie + "n'a pas pu être chargé (" + e.getMessage());
        }

        return nomAffilie;
    }

    /**
     * Exécute le ventilateur
     * 
     * @param ventilateur Le ventilateur à axécuter
     * @throws Exception
     */
    private void executerVentilateur(CARecouvrementVentilateur ventilateur) throws Exception {
        ventilateur.executerVentilation();
        ventilateur.executerRecouvrementCi();
        ventilateur.displayRecouvrementCiContainer();
    }

    /**
     * Exécute le ventilateur pour les postes contenus dans le conteneur passé en paramètre.
     * 
     * @param ventilateur Le ventilateur à axécuter
     * @param recouvrementPosteContainer Les postes à utiliser pour la ventilation
     * 
     * @throws Exception
     */
    private void exectuteVentilateur(CARecouvrementVentilateur ventilateur,
            CARecouvrementPosteContainer recouvrementPosteContainer) throws Exception {
        ventilateur.executerVentilation(recouvrementPosteContainer);
        ventilateur.executerRecouvrementCi();
        ventilateur.displayRecouvrementCiContainer();
    }

    /**
     * Initialise le ventilateur. Les données présentes dans le viewBean lui sont transmises. De plus les compteurs pour
     * pour les années sélectionnées sont chargés.
     * 
     * @param session
     * @param viewBean
     * @return
     * @throws Exception
     */
    private CARecouvrementVentilateur initVentilateur(BISession session, CORecouvrementCotisationsViewBean viewBean)
            throws Exception {

        BigDecimal montantARecouvrir = new BigDecimal(viewBean.getMontantARecouvrir());
        Integer annee = JadeNumericUtil.isEmptyOrZero(viewBean.getAnnee()) ? new Integer(0) : new Integer(
                viewBean.getAnnee());

        List<String> annees = viewBean.getSelectedBasesAmortissement();
        if (annee != 0) {
            annees.add(String.valueOf(annee));
        }

        CARecouvrementBaseAmortissementContainer basesAmortissement = CARecouvrementBaseAmortissementContainerLoader
                .loadCompteursForAnnees(session, viewBean.getIdCompteAnnexe(), annees);

        CARecouvrementVentilateur ventilateur = new CARecouvrementVentilateur((BSession) session,
                viewBean.getIdSectionsList(), viewBean.getIdCompteAnnexe(), basesAmortissement, montantARecouvrir,
                annee);
        return ventilateur;
    }
}
