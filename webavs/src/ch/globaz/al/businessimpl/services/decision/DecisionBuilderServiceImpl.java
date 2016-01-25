package ch.globaz.al.businessimpl.services.decision;

import globaz.globall.parameters.FWParametersCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstDecisions;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstJournalisation;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.exceptions.decision.ALDecisionException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierDecisionComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.decision.DecisionBuilderService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.utils.ALErrorsUtils;
import ch.globaz.libra.business.exceptions.LibraException;
import ch.globaz.libra.business.services.LibraServiceLocator;
import ch.globaz.libra.constantes.ILIConstantesExternes;
import ch.globaz.naos.business.model.AffiliationSearchSimpleModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Implémentation du service de construction des décisions
 * 
 * @author jts
 * 
 */
public class DecisionBuilderServiceImpl extends ALAbstractBusinessServiceImpl implements DecisionBuilderService {

    @Override
    public JadePrintDocumentContainer addCopies(JadePrintDocumentContainer containerDecision, DocumentData docData) {
        JadePublishDocumentInfo archiveInfo = new JadePublishDocumentInfo();
        archiveInfo.setArchiveDocument(false);
        archiveInfo.setPublishDocument(false);
        containerDecision.addDocument(docData, archiveInfo);
        return containerDecision;
    }

    @Override
    public JadePrintDocumentContainer addDecision(DossierComplexModel dossier, DocumentData docData, boolean isGed,
            JadePrintDocumentContainer containerDecision, String email, String dateImpression)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException {
        JadePublishDocumentInfo archiveInfo = new JadePublishDocumentInfo();

        archiveInfo.setArchiveDocument(isGed);
        archiveInfo.setPublishDocument(false);
        archiveInfo.setDocumentType("DecisionAF");
        archiveInfo.setDocumentTypeNumber("DecisionAF");

        archiveInfo.setOwnerId(JadeThread.currentUserId());
        archiveInfo.setOwnerEmail(email);
        archiveInfo.setDocumentDate(dateImpression);
        archiveInfo.setDocumentTitle("");
        archiveInfo.setDocumentSubject(JadeThread.getMessage("al.decision.publication.titre",
                new String[] { dateImpression }));

        String nssAllocataire = dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                .getPersonneEtendue().getNumAvsActuel();
        String numAffilie = dossier.getDossierModel().getNumeroAffilie();

        String nomPrenom = dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                .getDesignation1()
                + " "
                + dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2();

        archiveInfo.setPublishProperty("numero.role.formatte", numAffilie);
        archiveInfo.setPublishProperty("numero.affilie.formatte", numAffilie);
        archiveInfo.setPublishProperty("numero.avs.formatte", nssAllocataire);
        archiveInfo.setPublishProperty("numero.role.non.formatte", JadeStringUtil.removeChar(numAffilie, '.'));
        archiveInfo.setPublishProperty("pyxis.tiers.numero.avs.formatte", nssAllocataire);
        archiveInfo.setPublishProperty("pyxis.tiers.numero.avs.non.formatte",
                JadeStringUtil.removeChar(nssAllocataire, '.'));

        archiveInfo.setPublishProperty("pyxis.tiers.nom.prenom", nomPrenom);
        archiveInfo.setPublishProperty("type.dossier",
                ALServiceLocator.getGedBusinessService().getTypeSousDossier(dossier.getDossierModel()));
        try {
            TIBusinessServiceLocator.getDocInfoService().fill(archiveInfo,
                    dossier.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(), null, null,
                    ALCSTiers.ROLE_AF, dossier.getDossierModel().getNumeroAffilie(),
                    JadeStringUtil.removeChar(dossier.getDossierModel().getNumeroAffilie(), '.'), null);
        } catch (Exception e) {
            throw new ALDecisionException(e.getMessage(), e);
        }

        // stockage de la décision dans la liste des décisions à
        // imprimer
        containerDecision.addDocument(docData, archiveInfo);
        return containerDecision;
    }

    @Override
    public HashMap<String, ArrayList<DocumentData>> getDecision(String idDossier, String email, String userName,
            String phone, String visa, String dateImpression, boolean gestionCopie) throws JadeApplicationException,
            JadePersistenceException {

        return this.getDecision(idDossier, email, userName, phone, visa, dateImpression, gestionCopie, "", true);
    }

    @Override
    public HashMap<String, ArrayList<DocumentData>> getDecision(String idDossier, String email, String userName,
            String phone, String visa, String dateImpression, String texteLibre, boolean gestionTexteLibre)
            throws JadeApplicationException, JadePersistenceException {
        return this.getDecision(idDossier, email, userName, phone, visa, dateImpression, true, texteLibre,
                gestionTexteLibre);
    }

    @Override
    public HashMap<String, ArrayList<DocumentData>> getDecision(String idDossier, String email, String userName,
            String phone, String visa, String dateImpression) throws JadeApplicationException, JadePersistenceException {
        return this.getDecision(idDossier, email, userName, phone, visa, dateImpression);
    }

    @Override
    public HashMap<String, ArrayList<DocumentData>> getDecision(String idDossier, String email, String userName,
            String phone, String visa, String dateImpression, boolean gestionCopie, String texteLibre,
            boolean gestionTexteLibre) throws JadeApplicationException, JadePersistenceException {

        DossierDecisionComplexModel dossier = ALServiceLocator.getDossierDecisionComplexeModelService().read(idDossier);

        // préparation des information de l'utilisateur
        HashMap<String, String> userInfos = new HashMap<String, String>();

        userInfos.put(ALConstDocument.USER_MAIL, email);
        userInfos.put(ALConstDocument.USER_NAME, userName);
        userInfos.put(ALConstDocument.USER_PHONE, phone);
        userInfos.put(ALConstDocument.USER_VISA, visa);

        String langue = null;
        // langue affilié
        if (dossier.getAllocataireComplexModel().getAllocataireModel().getLangueAffilie()) {
            langue = ALServiceLocator.getLangueAllocAffilieService().langueTiersAffilie(
                    dossier.getDossierModel().getNumeroAffilie());

            // langue allocataire
        } else {
            langue = ALServiceLocator.getLangueAllocAffilieService().langueTiersAlloc(
                    dossier.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(),
                    dossier.getDossierModel().getNumeroAffilie());
        }

        Class<?> serviceClass = ALServiceLocator.getDecisionProviderService().getDecisionService(dossier);

        // récupération des décisions (original & copies + lettres d'accompagnement)
        return ALServiceLocator.getDecisionService(serviceClass).loadData(dossier, dateImpression, langue, userInfos,
                gestionCopie, texteLibre, gestionTexteLibre);
    }

    @Override
    public JadePrintDocumentContainer getDecision(JadePrintDocumentContainer containerDecision,
            DossierComplexModel dossier, String email, String userName, String phone, String visa,
            String dateImpression, boolean isGED, boolean gestionCopie) throws JadeApplicationException,
            JadePersistenceException {
        return this.getDecision(containerDecision, dossier, email, userName, phone, visa, dateImpression, isGED,
                gestionCopie, "", true);
    }

    @Override
    public JadePrintDocumentContainer getDecision(JadePrintDocumentContainer containerDecision,
            DossierComplexModel dossier, String email, String userName, String phone, String visa,
            String dateImpression, boolean isGED, boolean gestionCopie, String texteLibre, boolean gestionTexteLibre)
            throws JadeApplicationException, JadePersistenceException {

        HashMap<String, ArrayList<DocumentData>> decision = this.getDecision(dossier.getId(), email, userName, phone,
                visa, dateImpression, gestionCopie, texteLibre, gestionTexteLibre);

        addDecision(dossier, decision.get(ALConstDecisions.DECISION_ORIGINALE).get(0), isGED, containerDecision, email,
                dateImpression);
        for (int i = 0; i < decision.get(ALConstDecisions.DECISION_COPIES).size(); i++) {
            addCopies(containerDecision, decision.get(ALConstDecisions.DECISION_COPIES).get(i));
        }

        return containerDecision;
    }

    @Override
    public JadePrintDocumentContainer getDecisionEtCopies(JadePrintDocumentContainer containerDecision,
            DossierComplexModel dossier, String email, String userName, String phone, String visa,
            String dateImpression, boolean isGED) throws JadeApplicationException, JadePersistenceException {
        return this.getDecisionEtCopies(containerDecision, dossier, email, userName, phone, visa, dateImpression,
                isGED, "", true);
    }

    @Override
    public JadePrintDocumentContainer getDecisionEtCopies(JadePrintDocumentContainer containerDecision,
            DossierComplexModel dossier, String email, String userName, String phone, String visa,
            String dateImpression, boolean isGED, String texteLibre, boolean gestionTexteLibre)
            throws JadeApplicationException, JadePersistenceException {

        return this.getDecision(containerDecision, dossier, email, userName, phone, visa, dateImpression, isGED, true,
                texteLibre, gestionTexteLibre);
    }

    @Override
    public JadePublishDocumentInfo getMergedDocumentInfos(String email, String dateImpression) {

        // préparation des données de publication pour le document fusionné
        JadePublishDocumentInfo publishInfo = new JadePublishDocumentInfo();
        publishInfo.setArchiveDocument(false);
        publishInfo.setPublishDocument(true);
        publishInfo.setDocumentType("DecisionAF");
        publishInfo.setDocumentTypeNumber("DecisionAF");
        publishInfo.setDocumentDate(dateImpression);

        String[] date = { dateImpression };
        publishInfo.setDocumentTitle(JadeThread.getMessage("al.decision.publication.titre", date));
        publishInfo.setDocumentSubject(JadeThread.getMessage("al.decision.publication.titre", date));

        publishInfo.setOwnerId(JadeThread.currentUserId());
        publishInfo.setOwnerEmail(email);

        return publishInfo;
    }

    @Override
    public void journaliserDecision(DossierComplexModel dossier) throws LibraException,
            JadeApplicationServiceNotAvailableException {

        // Libra considère les messages de niveau WARN comme des erreurs et empêche le commit
        // de la transaction
        // Pour éviter ce problème les messages sont sauvegardés et retirés du log le temps de
        // l'appel du service de
        // création de la journalisation puis sont réinjectés dans le log une fois l'appel
        // terminé.
        JadeBusinessMessage[] logMessages = ALErrorsUtils.getMessageFromJadeThreadLog();

        try {
            LibraServiceLocator.getJournalisationService().createJournalisationAvecRemarqueWithTestDossier(
                    dossier.getId(), ALConstJournalisation.DECISION_MOTIF_JOURNALISATION, "Décision mise en ged",
                    dossier.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(),
                    ILIConstantesExternes.CS_DOMAINE_AF, true);
        } catch (LibraException e) {
            throw e;
        } finally {
            ALErrorsUtils.addMessages(logMessages);
        }
    }

    private String retournerNomAffilie(String numeroAffilie, HashMap<String, String> nomAffilieMap)
            throws JadeApplicationException {
        if (!nomAffilieMap.containsKey(numeroAffilie)) {

            AffiliationSearchSimpleModel searchModel = new AffiliationSearchSimpleModel();
            searchModel.setForNumeroAffilie(numeroAffilie);
            try {
                searchModel = AFBusinessServiceLocator.getAffiliationService().find(searchModel);
                if (searchModel.getSize() >= 1) {
                    AffiliationSimpleModel affilieSM = (AffiliationSimpleModel) searchModel.getSearchResults()[0];
                    nomAffilieMap.put(numeroAffilie, affilieSM.getRaisonSocialeCourt());
                }
            } catch (Exception e) {
                throw new ALDecisionException(
                        "DecisionBuilderServiceImpl#getListeDossiersCSV : Mapping AffiliationName error", e);
            }
        }
        return nomAffilieMap.get(numeroAffilie);
    }

    private String buildListeDossiersCSV(Collection<DossierComplexModel> dossiers) throws JadeApplicationException {

        StringBuffer csv = new StringBuffer();

        HashMap<String, String> activiteMap = new HashMap<String, String>();
        HashMap<String, String> statutMap = new HashMap<String, String>();
        HashMap<String, String> nomAffilieMap = new HashMap<String, String>();

        FWParametersSystemCodeManager cmActiviteDossier = new FWParametersSystemCodeManager();
        cmActiviteDossier.setForActif(true);
        cmActiviteDossier.setForIdGroupe("ALDOSACTAL");
        cmActiviteDossier.setForIdLangue(JadeThread.currentLanguage().toUpperCase().substring(0, 1));

        FWParametersSystemCodeManager cmStatutDossier = new FWParametersSystemCodeManager();
        cmStatutDossier.setForActif(true);
        cmStatutDossier.setForIdGroupe("ALDOSSTATU");
        cmStatutDossier.setForIdLangue(JadeThread.currentLanguage().toUpperCase().substring(0, 1));
        try {
            cmActiviteDossier.find();
            for (int i = 0; i < cmActiviteDossier.size(); i++) {
                FWParametersCode code = (FWParametersCode) cmActiviteDossier.getEntity(i);
                activiteMap.put(code.getIdCode(), JadeCodesSystemsUtil.getCodeLibelle(code.getIdCode()));
            }
            cmStatutDossier.find();
            for (int i = 0; i < cmStatutDossier.size(); i++) {
                FWParametersCode code = (FWParametersCode) cmStatutDossier.getEntity(i);
                statutMap.put(code.getIdCode(), JadeCodesSystemsUtil.getCodeLibelle(code.getIdCode()));
            }
        } catch (Exception e) {
            throw new ALDecisionException("DecisionBuilderServiceImpl#getListeDossiersCSV : Mapping CodeSysteme error",
                    e);
        }

        Iterator it = dossiers.iterator();

        while (it.hasNext()) {
            DossierComplexModel doss = (DossierComplexModel) it.next();

            // N° dossier Nom allocataire Type d'activité Statut dossier Type de bonification
            csv.append(doss.getDossierModel().getIdDossier()).append(";");
            // N° affilié
            csv.append(doss.getDossierModel().getNumeroAffilie()).append(";");
            // Nom affilié
            csv.append(retournerNomAffilie(doss.getDossierModel().getNumeroAffilie(), nomAffilieMap)).append(";");
            // Nom allocataire
            csv.append(
                    doss.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1()
                            + " "
                            + doss.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                                    .getDesignation2()).append(";");
            // Type d'activité
            csv.append(
                    activiteMap != null ? activiteMap.get(doss.getDossierModel().getActiviteAllocataire()) : doss
                            .getDossierModel().getActiviteAllocataire()).append(";");
            // Statut dossier
            csv.append(
                    statutMap != null ? statutMap.get(doss.getDossierModel().getStatut()) : doss.getDossierModel()
                            .getStatut()).append(";");
            // Type de bonification
            csv.append(
                    getPaiementMode(doss).equalsIgnoreCase(ALCSDossier.PAIEMENT_INDIRECT) ? JadeThread
                            .getMessage("al.protocoles.listeDecisionsMasse.csv.liste.typeBoni.indirect") : JadeThread
                            .getMessage("al.protocoles.listeDecisionsMasse.csv.liste.typeBoni.direct")).append("\n");
        }

        return csv.toString();

    }

    /**
     * Méthode retournant le mode de paiement du dossier en fonction du tiers bénéficiaire défini
     * 
     * @return <ul>
     *         <li><code>ALCSDossier.PAIEMENT_DIRECT</code></li>
     *         <li><code>ALCSDossier.PAIEMENT_INDIRECT</code></li>
     *         <li><code>ALCSDossier.PAIEMENT_TIERS</code></li>
     *         </ul>
     * 
     */
    public String getPaiementMode(DossierComplexModel dossierComplexModel) {

        String idTiersBeneficiaire = dossierComplexModel.getDossierModel().getIdTiersBeneficiaire();
        String idTiersAllocataire = dossierComplexModel.getAllocataireComplexModel().getAllocataireModel()
                .getIdTiersAllocataire();

        if (idTiersBeneficiaire.equals(idTiersAllocataire)) {
            return ALCSDossier.PAIEMENT_DIRECT;
        } else if (JadeStringUtil.isBlankOrZero(idTiersBeneficiaire)) {
            return ALCSDossier.PAIEMENT_INDIRECT;
        } else {
            return ALCSDossier.PAIEMENT_TIERS;
        }

    }

    @Override
    public String getListeDossiersCSV(Collection<DossierComplexModel> dossiers, HashMap<String, String> params)
            throws JadeApplicationException {

        StringBuffer csv = new StringBuffer();
        // title
        csv.append(JadeThread.getMessage("al.protocoles.listeDecisionsMasse.csv.title")).append("\n");
        csv.append("\n");
        // protocole headers
        csv.append(JadeThread.getMessage("al.protocoles.commun.critere.inNumeroAffilie.label")).append(";")
                .append(params.get(ALConstProtocoles.CRITERE_AFFILIE)).append(";")
                .append(JadeThread.getMessage("al.protocoles.commun.critere.inActivites.label")).append(";")
                .append(params.get(ALConstProtocoles.CRITERE_ACTIVITE)).append(";")
                .append(JadeThread.getMessage("al.protocoles.commun.critere.inStatut.label")).append(";")
                .append(params.get(ALConstProtocoles.CRITERE_STATUT)).append("\n");

        csv.append(JadeThread.getMessage("al.protocoles.commun.critere.inTarif.label")).append(";")
                .append(params.get(ALConstProtocoles.CRITERE_TARIF)).append(";")
                .append(JadeThread.getMessage("al.protocoles.commun.critere.inTypeDroit.label")).append(";")
                .append(params.get(ALConstProtocoles.CRITERE_DROIT)).append(";")
                .append(JadeThread.getMessage("al.protocoles.commun.critere.etat.label")).append(";")
                .append(JadeThread.getMessage(params.get(ALConstProtocoles.CRITERE_ETAT))).append("\n");

        csv.append(JadeThread.getMessage("al.protocoles.commun.critere.dateValiditeGREAT.label")).append(";")
                .append(params.get(ALConstProtocoles.CRITERE_VALID_GREAT)).append(";")
                .append(JadeThread.getMessage("al.protocoles.commun.critere.dateValiditeLESS.label")).append(";")
                .append(params.get(ALConstProtocoles.CRITERE_VALID_LESS)).append(";")
                .append(JadeThread.getMessage("al.protocoles.commun.critere.liste.dossier")).append(";")
                .append(params.get(ALConstProtocoles.CRITERE_LISTEDOSSIER)).append("\n");

        csv.append(JadeThread.getMessage("al.protocoles.commun.critere.dateFinValiditeGREAT.label")).append(";")
                .append(params.get(ALConstProtocoles.CRITERE_VALID_FIN_GREAT)).append(";")
                .append(JadeThread.getMessage("al.protocoles.commun.critere.dateFinValiditeLESS.label")).append(";")
                .append(params.get(ALConstProtocoles.CRITERE_VALID_FIN_LESS)).append("\n");

        csv.append("\n");
        csv.append(JadeThread.getMessage("al.protocoles.commun.info.texte.libre.label")).append(";")
                .append(params.get(ALConstProtocoles.INFO_TEXTE_LIBRE)).append("\n");
        csv.append("\n");
        csv.append(JadeThread.getMessage("al.protocoles.commun.info.date.debut.validite.label")).append(";")
                .append(";").append(params.get(ALConstProtocoles.INFO_DATE_DEBUT_VALIDITE)).append("\n");
        csv.append("\n");
        csv.append(JadeThread.getMessage("al.protocoles.commun.info.gestion.copie.label")).append(";")
                .append(JadeThread.getMessage(params.get(ALConstProtocoles.INFO_GESTION_COPIE))).append(";")
                .append(";").append(JadeThread.getMessage("al.protocoles.commun.info.gestion.texte.libre.label"))
                .append(";").append(JadeThread.getMessage(params.get(ALConstProtocoles.INFO_GESTION_TEXTE_LIBRE)))
                .append(";").append(JadeThread.getMessage("al.protocoles.commun.info.date.impression.label"))
                .append(";").append(params.get(ALConstProtocoles.INFO_DATE_IMPRESSION)).append("\n");
        csv.append(JadeThread.getMessage("al.protocoles.commun.info.insertion.ged.label")).append(";")
                .append(JadeThread.getMessage(params.get(ALConstProtocoles.INFO_INSERTION_GED))).append(";")
                .append(";").append(JadeThread.getMessage("al.protocoles.commun.info.tri.impression.label"))
                .append(";").append(params.get(ALConstProtocoles.INFO_TRI_IMPRESSION)).append(";")
                .append(JadeThread.getMessage("al.protocoles.commun.info.email.label")).append(";")
                .append(params.get(ALConstProtocoles.INFO_EMAIL)).append("\n");
        csv.append("\n");
        // table header
        csv.append(JadeThread.getMessage("al.protocoles.listeDecisionsMasse.csv.liste.header.numDossier")).append(";");
        csv.append(JadeThread.getMessage("al.protocoles.listeDecisionsMasse.csv.liste.header.numAffilie")).append(";");
        csv.append(JadeThread.getMessage("al.protocoles.listeDecisionsMasse.csv.liste.header.nomAffilie")).append(";");
        csv.append(JadeThread.getMessage("al.protocoles.listeDecisionsMasse.csv.liste.header.nameAlloc")).append(";");
        csv.append(JadeThread.getMessage("al.protocoles.listeDecisionsMasse.csv.liste.header.typeActivite"))
                .append(";");
        csv.append(JadeThread.getMessage("al.protocoles.listeDecisionsMasse.csv.liste.header.statutDossier")).append(
                ";");
        csv.append(JadeThread.getMessage("al.protocoles.listeDecisionsMasse.csv.liste.header.typeBoni")).append(";");
        csv.append("\n");

        if (dossiers == null) {
            throw new ALDecisionException("DecisionBuilderServiceImpl#getListeDossiersCSV : dossiers is null");
        }
        // Table content
        csv.append(buildListeDossiersCSV(dossiers));

        return (csv.toString());

    }

}
