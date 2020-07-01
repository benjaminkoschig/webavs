package globaz.cygnus.services.tmr;

import globaz.cygnus.db.demandes.RFDemandeJointDecisionJointDossierJointTiers;
import globaz.cygnus.process.RFImportDemandesCmsData;
import globaz.globall.db.BSession;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author mbo 20.06.2013
 * 
 */

public class RFPreparerDemandesTmrService {

    /**
     * Methode pour formatter le montant de la demande rejetée
     * 
     * @param montantDemande
     * @return
     */
    private String formatMontantDemandeRejetee(String montantDemande) {

        if (!JadeStringUtil.isBlankOrZero(montantDemande)) {
            int nbPosition = montantDemande.length();

            montantDemande = montantDemande.substring(0, nbPosition - 2) + "."
                    + montantDemande.substring((nbPosition - 2), nbPosition);
        }

        return montantDemande;

    }

    private Map<String, ArrayList<RFImportDemandesCmsData>> insertDemandeParCodeTraitement(String key,
            Map<String, ArrayList<RFImportDemandesCmsData>> map, RFImportDemandesCmsData dataDemande) {

        // Si la clé existe, on met la demande sous cette clé
        if (map.containsKey(key)) {
            map.get(key).add(dataDemande);
        }
        // Sinon, on crée la clé et ajout la demande
        else {
            ArrayList<RFImportDemandesCmsData> dataList = new ArrayList<RFImportDemandesCmsData>();
            dataList.add(dataDemande);
            map.put(key, dataList);
        }

        return map;
    }

    /**
     * Methode qui va rechercher des détails supplémentaires sur la demande
     * 
     * @param dataDemande
     * @return
     * @throws Exception
     */
    private RFImportDemandesCmsData rechercheDetailDemande(BSession session, RFImportDemandesCmsData dataDemande) {

        // Retrieve sur le détail de la demande
        RFDemandeJointDecisionJointDossierJointTiers rfDemandeJointDossierJointDecisionJointTiers = searchInfoDemande(
                session, dataDemande);

        // Chargement des infos complémentaires
        if (!rfDemandeJointDossierJointDecisionJointTiers.getAnneeQd().isEmpty()) {
            dataDemande.setAnneeQd(rfDemandeJointDossierJointDecisionJointTiers.getAnneeQd());
        }
        if (!rfDemandeJointDossierJointDecisionJointTiers.getNumeroDecision().isEmpty()) {
            dataDemande.setNumDecision(rfDemandeJointDossierJointDecisionJointTiers.getNumeroDecision());
        }
        if (!rfDemandeJointDossierJointDecisionJointTiers.getNss().isEmpty()) {
            dataDemande.setNssBeneficiaire(rfDemandeJointDossierJointDecisionJointTiers.getNss());
        }
        if (!rfDemandeJointDossierJointDecisionJointTiers.getNom().isEmpty()) {
            dataDemande.setNomTiers(rfDemandeJointDossierJointDecisionJointTiers.getNom());
        }
        if (!rfDemandeJointDossierJointDecisionJointTiers.getPrenom().isEmpty()) {
            dataDemande.setPrenomTiers(rfDemandeJointDossierJointDecisionJointTiers.getPrenom());
        }
        if (!rfDemandeJointDossierJointDecisionJointTiers.getMontantFacture().isEmpty()) {
            dataDemande.setMontantDemande(rfDemandeJointDossierJointDecisionJointTiers.getMontantFacture());
        } else {
            dataDemande.setMontantDemande(formatMontantDemandeRejetee(dataDemande.getMontantDemande()));
        }

        return dataDemande;
    }

    /**
     * Methode pour parcourirs chaque entite et retourner une map triée par code de traitement
     * 
     * @param entitesList
     * @return
     */
    public Map<String, ArrayList<RFImportDemandesCmsData>> regroupementDemandesParCodeTraitement(BSession session,
            List<RFImportDemandesCmsData> entitesList) throws Exception {

        // Si liste vide, on lance une exception
        if (((entitesList != null) && (entitesList.size() <= 0)) || (entitesList == null)) {
            new IllegalArgumentException(
                    "RFPreparerDemandesTmrService_regroupementDemandesParCodeTraitement : Liste 'entitesList' vide");
        }

        Map<String, ArrayList<RFImportDemandesCmsData>> map = new HashMap<String, ArrayList<RFImportDemandesCmsData>>();

        for (RFImportDemandesCmsData dataDemande : entitesList) {

            // On ne tient pas compte de la première et dernière ligne
            if (!dataDemande.isPremiereLigne() && !dataDemande.isDerniereLigne()) {

                // Chargement des infos complémentaires sur la demande
                dataDemande = rechercheDetailDemande(session, dataDemande);

                // Isnertion de la demande dans une map
                map = remplirMapParCodeTraitement(session, map, dataDemande);
            }
        }

        return map;
    }

    /**
     * TODO
     * 
     * @param session
     * @param map
     * @param dataDemande
     * @return
     */
    private Map<String, ArrayList<RFImportDemandesCmsData>> remplirMapParCodeTraitement(BSession session,
            Map<String, ArrayList<RFImportDemandesCmsData>> map, RFImportDemandesCmsData dataDemande) {

        // Insertion de la demande dans la map, selon son code de traitement
        map = insertDemandeParCodeTraitement(dataDemande.getCodeTraitement(), map, dataDemande);

        return map;
    }

    /**
     * Methode qui va recherche le détail d'une demande unique.
     * 
     * @param dataDemande
     * @return
     * @throws Exception
     */
    private RFDemandeJointDecisionJointDossierJointTiers searchInfoDemande(BSession session,
            RFImportDemandesCmsData dataDemande) {

        RFDemandeJointDecisionJointDossierJointTiers rfDemandeJointMotifsRefusJointDecisionJointDossierJointTiers = new RFDemandeJointDecisionJointDossierJointTiers();

        try {
            rfDemandeJointMotifsRefusJointDecisionJointDossierJointTiers.setIdDemande(dataDemande
                    .getIdDemandeValiderDecisionStep());
            rfDemandeJointMotifsRefusJointDecisionJointDossierJointTiers.setSession(session);
            rfDemandeJointMotifsRefusJointDecisionJointDossierJointTiers.retrieve();

        } catch (Exception e) {
            new JAException("RFPreparerDemandesTmrService:searchInfoDemande", e.getMessage());
        }
        return rfDemandeJointMotifsRefusJointDecisionJointDossierJointTiers;
    }

}
