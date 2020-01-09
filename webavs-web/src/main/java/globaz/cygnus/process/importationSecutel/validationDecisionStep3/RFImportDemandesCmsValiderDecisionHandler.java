package globaz.cygnus.process.importationSecutel.validationDecisionStep3;

import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityDataFind;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityPropertySavable;
import com.google.gson.Gson;
import globaz.cygnus.RFCodeTraitementDemandeSecutelCleEnum;
import globaz.cygnus.api.motifsRefus.IRFMotifsRefus;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemandeManager;
import globaz.cygnus.process.RFImportDemandesCmsData;
import globaz.cygnus.process.importationSecutel.RFImportDemandesCmsPopulation;
import globaz.cygnus.process.importationSecutel.RFProcessImportationSecutelEnum;
import globaz.cygnus.process.importationSecutel.creationDemandesStep1.RFImportDemandesCmsCreationHandler;
import globaz.cygnus.utils.RFUtils;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.prestation.tools.PRDateFormater;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

public class RFImportDemandesCmsValiderDecisionHandler implements JadeProcessEntityInterface,
        JadeProcessEntityNeedProperties, JadeProcessEntityDataFind<RFProcessImportationSecutelEnum>,
        JadeProcessEntityPropertySavable<RFProcessImportationSecutelEnum> {

    private enum PositionsImportDemandesCmsValiderDecisionHandlerEnum {

        POSITION_NOM_PRENOM_TIERS_FICHIER_SOURCE(45, 74);

        private int debut;
        private int fin;

        PositionsImportDemandesCmsValiderDecisionHandlerEnum(int debut, int fin) {
            this.debut = debut;
            this.fin = fin;
        }

        public int getDebut() {
            return debut;
        }

        public int getFin() {
            return fin;
        }
    }

    private Map<RFProcessImportationSecutelEnum, String> dataToSave = new HashMap<RFProcessImportationSecutelEnum, String>();
    private List<RFImportDemandesCmsData> entitesList = null;
    private JadeProcessEntity entity = null;
    private RFImportDemandesCmsData entityData = null;
    private String idDemande = "";
    private String messagesErreurs = "";

    private Map<Enum<?>, String> properties = null;

    public RFImportDemandesCmsValiderDecisionHandler(List<RFImportDemandesCmsData> entitesList) {
        super();
        this.entitesList = entitesList;
    }

    private void addMotifsRefusToEntitesList(Map<String, String[]> idsMotifDeRefusSysteme,
            RFAssMotifsRefusDemandeManager rfAssMotRefDemMgr) {

        if (null == entityData.getMotifsRefusList()) {
            entityData.setMotifsRefusList(new ArrayList<String>());
        }

        Collection<String[]> idsMotifDeRefusSystemeValues = idsMotifDeRefusSysteme.values();

        Iterator<RFAssMotifsRefusDemande> rfAssMotRefDemIt = rfAssMotRefDemMgr.iterator();
        while (rfAssMotRefDemIt.hasNext()) {
            RFAssMotifsRefusDemande assMotifsRefusDemandeCourant = rfAssMotRefDemIt.next();

            if (assMotifsRefusDemandeCourant != null) {
                String descriptionMotifDeRefusStr = "";
                for (String[] idMotifDeRefusSystemeValueCourant : idsMotifDeRefusSystemeValues) {
                    if (idMotifDeRefusSystemeValueCourant[0].equals(assMotifsRefusDemandeCourant.getIdMotifsRefus())) {
                        descriptionMotifDeRefusStr = idMotifDeRefusSystemeValueCourant[2];
                        break;
                    }
                }
                if (!JadeStringUtil.isBlankOrZero(descriptionMotifDeRefusStr)) {
                    entityData.getMotifsRefusList().add(descriptionMotifDeRefusStr);
                }
            }
        }
    }

    /**
     * Méthode qui permet d'ajouter un message selon le code de traitement.
     * 
     * @param code, code de traitement
     * @param label, label du message
     * @param param, a renseigner en cas de message utilisant un paramètre
     */
    private void addMessageCodeTraitement(String code, String label, String... param) {
        entityData.setCodeTraitement(code);

        if (null == entityData.getMotifsRefusList()) {
            entityData.setMotifsRefusList(new ArrayList<String>());
        }

        if (param.length <= 0) {
            entityData.getMotifsRefusList().add(BSessionUtil.getSessionFromThreadContext().getLabel(label));
        } else {
            String message = MessageFormat.format(BSessionUtil.getSessionFromThreadContext().getLabel(label),
                    param[0].toString());
            entityData.getMotifsRefusList().add(message);
        }

    }

    private void ajouterMontantDsasAuMontantAPayer(RFImportDemandesCmsData dataDemande, BSession session,
            RFDemande rfDemande, Map<String, String[]> idsMotifDeRefusSysteme,
            RFAssMotifsRefusDemandeManager rfAssMotRefDemMgr) throws Exception {

        if (isCodeErreurSelonIdMotifDeRefus(dataDemande.getIdDemandeValiderDecisionStep(), session,
                idsMotifDeRefusSysteme.get(IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE)[0], rfAssMotRefDemMgr)) {

            String montantDsas = getMontantMotifDeRefus(dataDemande.getIdDemandeValiderDecisionStep(), session,
                    idsMotifDeRefusSysteme.get(IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE)[0], rfAssMotRefDemMgr);

            dataDemande.setMontantDSAS(montantDsas);

            BigDecimal montantDsasPlusMontantAPayerBigDec = new BigDecimal("0");

            if (!JadeStringUtil.isBlankOrZero(rfDemande.getMontantAPayer())) {
                montantDsasPlusMontantAPayerBigDec = new BigDecimal(rfDemande.getMontantAPayer());
            }

            if (montantDsas != null) {
                montantDsasPlusMontantAPayerBigDec = montantDsasPlusMontantAPayerBigDec
                        .add(new BigDecimal(montantDsas));
            }
            rfDemande.setMontantAPayer(montantDsasPlusMontantAPayerBigDec.toString());
        }
    }

    private RFAssMotifsRefusDemandeManager getAssMotifsRefusDemandeManager(BSession session) throws Exception {
        RFAssMotifsRefusDemandeManager rfAssMotRefDemMgr = new RFAssMotifsRefusDemandeManager();
        rfAssMotRefDemMgr.setSession(session);
        rfAssMotRefDemMgr.setForIdDemande(idDemande);
        rfAssMotRefDemMgr.changeManagerSize(0);
        rfAssMotRefDemMgr.find();
        return rfAssMotRefDemMgr;
    }

    private String getMontantMotifDeRefus(String idDemande, BSession session, String idMotifDeRefusSysteme,
            RFAssMotifsRefusDemandeManager rfAssMotRefDemMgr) throws Exception {

        if (rfAssMotRefDemMgr.size() > 0) {
            Iterator<RFAssMotifsRefusDemande> rfAssMotRefItr = rfAssMotRefDemMgr.iterator();
            while (rfAssMotRefItr.hasNext()) {

                RFAssMotifsRefusDemande rfAssMotRefDem = rfAssMotRefItr.next();
                if (rfAssMotRefDem != null) {
                    if (rfAssMotRefDem.getIdMotifsRefus().equals(idMotifDeRefusSysteme)) {
                        return rfAssMotRefDem.getMntMotifsDeRefus();
                    }
                } else {
                    throw new Exception("[RFPreparerDemandesSecutelService.getMontantMotifDeRefus()] Demande null");
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public Map<RFProcessImportationSecutelEnum, String> getValueToSave() {
        return dataToSave;
    }

    private boolean isCodeErreurSelonIdMotifDeRefus(String idDemande, BSession session, String idMotifDeRefusSysteme,
            RFAssMotifsRefusDemandeManager rfAssMotRefDemMgr) throws Exception {

        if (rfAssMotRefDemMgr.size() > 0) {
            Iterator<RFAssMotifsRefusDemande> rfAssMotRefItr = rfAssMotRefDemMgr.iterator();
            while (rfAssMotRefItr.hasNext()) {

                RFAssMotifsRefusDemande rfAssMotRefDem = rfAssMotRefItr.next();
                if (rfAssMotRefDem != null) {
                    if (rfAssMotRefDem.getIdMotifsRefus().equals(idMotifDeRefusSysteme)) {
                        return true;
                    }
                } else {
                    throw new Exception(
                            "[RFPreparerDemandesSecutelService.isCodeErreurSelonIdMotifDeRefus()] Demande null");
                }
            }

            return false;
        } else {
            return false;
        }
    }

    private boolean isMntFactureEqualMntAPayer(String montantFacture, String montantAPayer) {

        BigDecimal montantFactureBdm = new BigDecimal(montantFacture);
        BigDecimal montantAPayerBdm = new BigDecimal(montantAPayer);

        return montantFactureBdm.compareTo(montantAPayerBdm) == 0;

    }

    /**
     * Methode pour faire un retrieve sur la demande concernée
     * 
     * @param idDataDemande
     * @return
     */
    private RFDemande loadDemandeParId(String idDataDemande, BSession session) {
        try {
            RFDemande rfDemande = new RFDemande();
            rfDemande.setSession(session);
            rfDemande.setIdDemande(idDataDemande);
            rfDemande.retrieve();

            if (!rfDemande.isNew()) {
                return rfDemande;
            } else {
                throw new IllegalArgumentException(
                        "[RFPreparerDemandesSecutelService.loadDemandeParId()]  Impossible de retrouver la demande avec son id");
            }

        } catch (Exception e) {
            throw new IllegalArgumentException(e + " : Impossible de retrouver la demande avec son id");
        }
    }

    private void retrieveCodeTraitementAvecIdDemande(RFImportDemandesCmsData dataDemande, BSession session)
            throws Exception, JadePersistenceException {

        RFDemande rfDemande = loadDemandeParId(dataDemande.getIdDemandeValiderDecisionStep(), session);

        if (rfDemande.getIsForcerPaiement().booleanValue()) {
            retrieveCodeTraitementAvecIdDemandeForcerPaiementCoche(dataDemande, session, rfDemande);
        } else {
            retrieveCodeTraitementAvecIdDemandeForcePaiementDecoche(dataDemande, session, rfDemande);
        }
    }

    private void retrieveCodeTraitementAvecIdDemandeForcePaiementDecoche(RFImportDemandesCmsData dataDemande,
            BSession session, RFDemande rfDemande) throws Exception, JadePersistenceException {
        // Si pas de montant a payer
        if (JadeStringUtil.isBlankOrZero(rfDemande.getMontantAPayer())) {

            addMessageCodeTraitement(RFCodeTraitementDemandeSecutelCleEnum.REFUS_ASV.getCode(),
                    "RF_IMPORT_SECUTEL_REFUS_FORTUNE_PLUS_GRANDE_AVS");
            entityData.setMontantPaye("0.00");
        } else {
            if (JadeStringUtil.isBlankOrZero(rfDemande.getMontantFacture())) {
                throw new JadePersistenceException(
                        "[RFPreparerDemandesSecutelService.retrieveCodeTraitementAvecIdDemandeForcePaiementDecoche()] Montant facture introuvable");
            }

            entityData.setMontantPaye(rfDemande.getMontantAPayer());

            // Si les montants sont égaux, c'est une accordée
            if (isMntFactureEqualMntAPayer(rfDemande.getMontantFacture(), rfDemande.getMontantAPayer())) {
                dataDemande.setCodeTraitement(RFCodeTraitementDemandeSecutelCleEnum.PAIEMENT_TOTAL.getCode());
                // Sinon c'est une partielle
            } else {
                addMessageCodeTraitement(RFCodeTraitementDemandeSecutelCleEnum.PAIEMENT_PARTIEL.getCode(),
                        "RF_IMPORT_SECUTEL_PAIEMENT_PARTIEL");
            }
        }
    }

    private void retrieveCodeTraitementAvecIdDemandeForcerPaiementCoche(RFImportDemandesCmsData dataDemande,
            BSession session, RFDemande rfDemande) throws Exception, JadePersistenceException {

        Map<String, String[]> idsMotifDeRefusSysteme = RFUtils.getIdsMotifDeRefusSysteme(session, null);
        RFAssMotifsRefusDemandeManager rfAssMotRefDemMgr = getAssMotifsRefusDemandeManager(session);

        // Ajout du montant DSAS (forcé paiement au montant initial)
        ajouterMontantDsasAuMontantAPayer(dataDemande, session, rfDemande, idsMotifDeRefusSysteme, rfAssMotRefDemMgr);

        // Si pas de montant à payer,
        if (JadeStringUtil.isBlankOrZero(rfDemande.getMontantAPayer())) {

            if (isCodeErreurSelonIdMotifDeRefus(dataDemande.getIdDemandeValiderDecisionStep(), session,
                    idsMotifDeRefusSysteme.get(IRFMotifsRefus.ID_MENAGE_NON_CAR_HOME)[0], rfAssMotRefDemMgr)) {
                addMessageCodeTraitement(RFCodeTraitementDemandeSecutelCleEnum.QD_DANS_HOME.getCode(),
                        "RF_IMPORT_SECUTEL_QD_DANS_HOME");

            } else if (isCodeErreurSelonIdMotifDeRefus(dataDemande.getIdDemandeValiderDecisionStep(), session,
                    idsMotifDeRefusSysteme.get(IRFMotifsRefus.ID_SOLDE_EXECEDENT_DE_REVENU)[0], rfAssMotRefDemMgr)) {
                addMessageCodeTraitement(RFCodeTraitementDemandeSecutelCleEnum.REFUS_PC.getCode(),
                        "RF_IMPORT_SECUTEL_REFUS_PC_COMPENSATION_EXCEDENT_REVENU");

            } else if (isCodeErreurSelonIdMotifDeRefus(dataDemande.getIdDemandeValiderDecisionStep(), session,
                    idsMotifDeRefusSysteme.get(IRFMotifsRefus.ID_PAS_DROIT_A_LA_PC)[0], rfAssMotRefDemMgr)
                    || isCodeErreurSelonIdMotifDeRefus(dataDemande.getIdDemandeValiderDecisionStep(), session,
                            idsMotifDeRefusSysteme.get(IRFMotifsRefus.ID_PAS_DE_DOCUMENTS_POUR_CALCULER_LA_PC)[0],
                            rfAssMotRefDemMgr)) {
                String pDate = PRDateFormater.convertDate_AAMMJJ_to_JJxMMxAAAA(entityData.getDateDeDebutTraitement());
                addMessageCodeTraitement(RFCodeTraitementDemandeSecutelCleEnum.NSS_PAS_TROUVE.getCode(),
                        "RF_IMPORT_SECUTEL_PAS_DE_QD_VALABLE_DATE_DEBUT_TRAITEMENT", pDate);

            } else {
                addMessageCodeTraitement(RFCodeTraitementDemandeSecutelCleEnum.PAIEMENT_PARTIEL.getCode(),
                        "IMPORT_SECUTEL_PAIEMENT_PARTIEL");
            }

            entityData.setMontantPaye("0.00");
        }
        // Si montants présent
        else {

            if (JadeStringUtil.isBlankOrZero(rfDemande.getMontantFacture())) {
                throw new JadePersistenceException(
                        "[RFPreparerDemandesSecutelService.retrieveCodeTraitementAvecIdDemandeForcerPaiementCoche()] Montant facture introuvable");
            }

            entityData.setMontantPaye(rfDemande.getMontantAPayer());

            // Si les montants sont égaux, c'est une accordée
            if (isMntFactureEqualMntAPayer(rfDemande.getMontantFacture(), rfDemande.getMontantAPayer())) {
                dataDemande.setCodeTraitement(RFCodeTraitementDemandeSecutelCleEnum.PAIEMENT_TOTAL.getCode());
                // Sinon c'est une partielle
            } else {
                addMessageCodeTraitement(RFCodeTraitementDemandeSecutelCleEnum.PAIEMENT_PARTIEL.getCode(),
                        "IMPORT_SECUTEL_PAIEMENT_PARTIEL");
            }
        }
    }

    /**
     * Methode qui va attribuer le code de traitement à la demande
     * 
     * @param dataDemande
     * @return
     */
    private void retrieveCodeTraitementErreur(RFImportDemandesCmsData dataDemande, BSession session)
            throws JadePersistenceException {

        try {
            // Si la demande contient un id, on va rechercher les info de la demande
            if (!JadeStringUtil.isBlankOrZero(dataDemande.getIdDemandeValiderDecisionStep())) {
                retrieveCodeTraitementAvecIdDemande(dataDemande, session);
            } else {
                retrieveCodeTraitementSansIdDemande(dataDemande);
            }
        } catch (Exception e) {
            entityData.setHasErrorsEtape3(true);
            throw new JadePersistenceException(e);
        }
    }

    private void retrieveCodeTraitementSansIdDemande(RFImportDemandesCmsData dataDemande)
            throws JadePersistenceException {

        if (dataDemande.getMessagesErreursImportationList().size() > 0) {

            String codeErreur = RFCodeTraitementDemandeSecutelCleEnum.DONNEES_INCOHERENTES.getCode();
            for (String[] messageCourant : dataDemande.getMessagesErreursImportationList()) {
                if (messageCourant[1].equals(RFCodeTraitementDemandeSecutelCleEnum.NSS_PAS_TROUVE.getCode())) {
                    codeErreur = RFCodeTraitementDemandeSecutelCleEnum.NSS_PAS_TROUVE.getCode();
                }
            }
            dataDemande.setCodeTraitement(codeErreur);

        } else {
            throw new JadePersistenceException(
                    "[RFPreparerDemandesSecutelService.attribuerCodeTraitement()] Liste d'erreurs vide");
        }
    }

    private void retrieveIdDemandeEntityFromProperties() {
        entityData.setIdDemandeValiderDecisionStep(idDemande);
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {

        if (RFImportDemandesCmsCreationHandler.isPasPremierNiDerniereLigne(entityData)) {
            retrieveIdDemandeEntityFromProperties();
            saveMessagesErreursFromProperties();
            retrieveCodeTraitementErreur(entityData, BSessionUtil.getSessionFromThreadContext());
            setNomPrenomTiersFichierSource();

            setDataToSave(entityData.getCodeTraitement());
        }

        entitesList.add(entityData);
    }

    private void saveMessagesErreursFromProperties() {

        if (messagesErreurs != null) {

            String[] messagesErreursTab = messagesErreurs
                    .split(RFImportDemandesCmsCreationHandler.SEPARATEUR_MESSAGE_ERREUR);

            List<String[]> messagesErreursList = new ArrayList<String[]>();
            int i = 0;
            while (i < messagesErreursTab.length) {
                if (!JadeStringUtil.isBlankOrZero(messagesErreursTab[i])) {
                    String[] messageLibelleErreurCode = messagesErreursTab[i]
                            .split(RFImportDemandesCmsCreationHandler.SEPARATEUR_MESSAGE_ERREUR_CODE);
                    messagesErreursList.add(messageLibelleErreurCode);
                }
                i++;
            }
            if (entityData.getMessagesErreursImportationList() == null) {
                entityData.setMessagesErreursImportationsList(new ArrayList<String[]>());
            }

            entityData.setMessagesErreursImportationsList(messagesErreursList);
        }
    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {

        if (entity != null) {
            this.entity = entity;

            Gson gson = new Gson();
            entityData = gson.fromJson(this.entity.getValue1(), RFImportDemandesCmsData.class);
        } else {
            JadeThread.logError("RFImportDemandesCmsCreationHandler.setCurrentEntity()", "Entity is null");
        }
    }

    @Override
    public void setData(Map<RFProcessImportationSecutelEnum, String> hashMap) {
        if (null != hashMap) {
            idDemande = hashMap.get(RFProcessImportationSecutelEnum.ID_DEMANDE);
            messagesErreurs = hashMap.get(RFProcessImportationSecutelEnum.MESSAGES_ERREUR_IMPORTATION);
        }
    }

    private void setDataToSave(String codeErreur) {
        if (!JadeStringUtil.isBlankOrZero(codeErreur)) {
            dataToSave.put(RFProcessImportationSecutelEnum.CODE_ERREUR, codeErreur);
        }
    }

    private void setNomPrenomTiersFichierSource() {
        entityData.setNomPrenomTiersFichierSource(RFImportDemandesCmsPopulation.traiterLigne(
                PositionsImportDemandesCmsValiderDecisionHandlerEnum.POSITION_NOM_PRENOM_TIERS_FICHIER_SOURCE
                        .getDebut(),
                PositionsImportDemandesCmsValiderDecisionHandlerEnum.POSITION_NOM_PRENOM_TIERS_FICHIER_SOURCE.getFin(),
                entityData.getLigne(), Integer.parseInt(entityData.getNumeroLigne()), ""));
    }

    @Override
    public void setProperties(Map<Enum<?>, String> map) {
        properties = map;
    }
}
