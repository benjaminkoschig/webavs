package globaz.perseus.process.statistiques;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.perseus.process.PFAbstractJob;
import globaz.perseus.utils.PFUserHelper;
import globaz.pyxis.constantes.IConstantes;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeDemande;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.DonneeFinanciere;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuType;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamilleSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

public class PFStatsDetailsProcess extends PFAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private StringBuffer contentStat = new StringBuffer();
    private String dateDebut = null;
    private String dateFin = null;
    private String mailAd = null;

    private String createFile(String content, String name) throws JadeApplicationException, IOException {

        String paht = Jade.getInstance().getPersistenceDir() + "_" + name + "_" + JadeUUIDGenerator.createStringUUID()
                + ".csv";
        java.io.File f = new java.io.File(paht);
        FileWriter fstream;

        fstream = new FileWriter(f);
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(content);
        out.close();

        return paht;
    }

    private List<String> doTraitement(BSession session, List<String> dossierDejaTraite,
            DecisionSearchModel decisionSearchModel) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        for (JadeAbstractModel model : decisionSearchModel.getSearchResults()) {
            Decision decision = (Decision) model;

            if (!dossierDejaTraite.contains(decision.getDemande().getDossier().getId())
                    && JadeStringUtil.isBlankOrZero(decision.getSimpleDecision().getCsChoix())) {

                contentStat.append(decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                        .getPersonneEtendue().getNumAvsActuel());
                contentStat.append(";");
                contentStat.append(decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                        .getTiers().getDesignation1());
                contentStat.append(";");
                contentStat.append(decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                        .getTiers().getDesignation2());
                contentStat.append(";");
                contentStat.append(decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                        .getPersonne().getDateNaissance());
                contentStat.append(";");

                // Récupération de l'adresse
                AdresseTiersDetail detailTiers = PFUserHelper.getAdresseAssure(decision.getDemande().getDossier()
                        .getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers(),
                        IConstantes.CS_AVOIR_ADRESSE_DOMICILE, JadeDateUtil.getGlobazFormattedDate(new Date()));

                if (detailTiers.getFields() != null) {
                    contentStat.append(detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE));
                    contentStat.append(";");
                    contentStat.append(detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NUMERO));
                    contentStat.append(";");
                    contentStat.append(detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA));
                    contentStat.append(";");
                    contentStat.append(detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE));
                    contentStat.append(";");
                } else {
                    contentStat.append(";");
                    contentStat.append(";");
                    contentStat.append(";");
                    contentStat.append(";");
                }

                if (decision.getDemande().getSimpleDemande().getFromRI()) {
                    contentStat.append("X");
                }
                contentStat.append(";");
                contentStat.append(session.getCodeLibelle(decision.getSimpleDecision().getCsTypeDecision()));
                contentStat.append(";");
                if ((CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem().equals(decision.getSimpleDecision()
                        .getCsTypeDecision()))
                        || (CSTypeDecision.NON_ENTREE_MATIERE.getCodeSystem().equals(decision.getSimpleDecision()
                                .getCsTypeDecision()))) {

                    EnfantFamilleSearchModel enfantFamilleSearchModel = new EnfantFamilleSearchModel();
                    enfantFamilleSearchModel.setForIdSituationFamiliale(decision.getDemande().getSituationFamiliale()
                            .getId());
                    enfantFamilleSearchModel = PerseusImplServiceLocator.getEnfantFamilleService().search(
                            enfantFamilleSearchModel);

                    for (JadeAbstractModel abstractModel : enfantFamilleSearchModel.getSearchResults()) {
                        EnfantFamille enfantfamille = (EnfantFamille) abstractModel;

                        contentStat.append(enfantfamille.getEnfant().getMembreFamille().getPersonneEtendue()
                                .getPersonne().getDateNaissance());
                        contentStat.append(";");
                    }

                    if (enfantFamilleSearchModel.getSize() < 8) {
                        for (int i = enfantFamilleSearchModel.getSize() + 1; i <= 8; i++) {
                            contentStat.append(";");
                        }
                    }

                    contentStat.append("0;");
                    contentStat.append("0;");
                    contentStat.append("0;");
                    contentStat.append("0;");
                    contentStat.append("0;");
                    contentStat.append("0;");
                    contentStat.append("0;");
                    contentStat.append("0;");
                    contentStat.append("0;");
                    contentStat.append("0;");
                    contentStat.append("0;");
                    contentStat.append("0;");
                    contentStat.append("0;");
                    contentStat.append("0;");
                    contentStat.append(";");
                    contentStat.append("0;");

                } else {
                    // reprise des informations de la PCFAccordée
                    PCFAccordee pcfa = PerseusServiceLocator.getPCFAccordeeService().readForDemande(
                            decision.getSimpleDecision().getIdDemande());

                    // Calcul de l'aide aux études
                    Float aidesEtudes = pcfa.getCalcul().getDonnee(OutputData.REVENUS_AIDE_FORMATION);
                    EnfantFamilleSearchModel enfantFamilleSearchModel = new EnfantFamilleSearchModel();
                    enfantFamilleSearchModel.setForIdSituationFamiliale(decision.getDemande().getSituationFamiliale()
                            .getId());
                    enfantFamilleSearchModel = PerseusImplServiceLocator.getEnfantFamilleService().search(
                            enfantFamilleSearchModel);

                    for (JadeAbstractModel abstractModel : enfantFamilleSearchModel.getSearchResults()) {
                        EnfantFamille enfantfamille = (EnfantFamille) abstractModel;

                        contentStat.append(enfantfamille.getEnfant().getMembreFamille().getPersonneEtendue()
                                .getPersonne().getDateNaissance());
                        contentStat.append(";");

                        RevenuSearchModel searchModel = new RevenuSearchModel();
                        searchModel.setForIdDemande(decision.getDemande().getSimpleDemande().getIdDemande());
                        searchModel.setForIdMembreFamille(enfantfamille.getEnfant().getMembreFamille().getId());
                        List<String> listType = new ArrayList<String>();
                        listType.add(RevenuType.AIDE_FORMATION.getId().toString());
                        searchModel.setInType(listType);
                        searchModel = PerseusServiceLocator.getRevenuService().search(searchModel);
                        DonneeFinanciere df = null;
                        if (searchModel.getSearchResults().length > 0) {
                            df = (DonneeFinanciere) searchModel.getSearchResults()[0];
                            aidesEtudes += df.getValeur();
                        }
                    }

                    if (enfantFamilleSearchModel.getSize() < 8) {
                        for (int i = enfantFamilleSearchModel.getSize() + 1; i <= 8; i++) {
                            contentStat.append(";");
                        }
                    }

                    contentStat.append(pcfa.getSimplePCFAccordee().getMontant());
                    contentStat.append(";");
                    contentStat.append(pcfa.getSimplePCFAccordee().getExcedantRevenu());
                    contentStat.append(";");

                    contentStat.append(pcfa.getCalcul().getDonneeString(OutputData.FORTUNE_NETTE));
                    contentStat.append(";");
                    contentStat.append(pcfa.getCalcul().getDonneeString(OutputData.REVENUS_DETERMINANT));
                    contentStat.append(";");
                    contentStat.append(pcfa.getCalcul().getDonneeString(OutputData.REVENUS_ACTIVITE));
                    contentStat.append(";");
                    contentStat.append(pcfa.getCalcul().getDonneeString(OutputData.DEPENSES_RECONNUES));
                    contentStat.append(";");
                    contentStat.append(pcfa.getCalcul().getDonnee(
                            OutputData.DEPENSES_FRAIS_TRANSPORT_CONJOINT_MODIF_TAXATEUR)
                            + pcfa.getCalcul().getDonnee(OutputData.DEPENSES_FRAIS_TRANSPORT_REQUERANT_MODIF_TAXATEUR));
                    contentStat.append(";");
                    contentStat.append(pcfa.getCalcul().getDonnee(OutputData.DEPENSES_FRAIS_REPAS_CONJOINT_MODIF)
                            + pcfa.getCalcul().getDonnee(OutputData.DEPENSES_FRAIS_REPAS_REQUERANT_MODIF));
                    contentStat.append(";");
                    contentStat.append(pcfa.getCalcul().getDonneeString(OutputData.DEPENSES_BESOINS_VITAUX));
                    contentStat.append(";");
                    contentStat.append(pcfa.getCalcul().getDonneeString(OutputData.REVENUS_AIDES_LOGEMENT));
                    contentStat.append(";");
                    contentStat.append(aidesEtudes.toString());
                    contentStat.append(";");
                    // Plafond de la prestation
                    contentStat.append(pcfa.getCalcul().getDonnee(OutputData.PRESTATION_ANNUELLE_MODIF)
                            - pcfa.getCalcul().getDonnee(OutputData.PRESTATION_ANNUELLE));
                    contentStat.append(";");
                    contentStat.append(pcfa.getCalcul().getDonneeString(OutputData.MESURE_ENCOURAGEMENT));
                    contentStat.append(";");
                    contentStat.append(pcfa.getCalcul().getDonneeString(OutputData.MESURE_CHARGES_LOYER));
                    contentStat.append(";");
                    if (decision.getDemande().getSimpleDemande().getCoaching()) {
                        contentStat.append("X");
                        contentStat.append(";");
                    } else {
                        contentStat.append(";");
                    }

                    contentStat.append(pcfa.getCalcul().getDonneeString(OutputData.MESURE_COACHING));
                    contentStat.append(";");
                }

                // ajouter colonne pour la mesure de coaching
                contentStat.append(decision.getSimpleDecision().getMontantToucheAuRI());
                contentStat.append(";");
                contentStat.append(decision.getDemande().getSimpleDemande().getDateDebut());
                contentStat.append(";");
                contentStat.append(decision.getDemande().getSimpleDemande().getDateFin());
                contentStat.append(";");

                DemandeSearchModel demSearch = new DemandeSearchModel();
                demSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                demSearch.setForCsEtatDemande(CSEtatDemande.VALIDE.getCodeSystem());
                demSearch.setForCsTypeDemande(CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem());
                demSearch.setForIdDossier(decision.getDemande().getDossier().getDossier().getIdDossier());
                contentStat.append(PerseusServiceLocator.getDemandeService().count(demSearch));
                contentStat.append(";");
                contentStat.append(decision.getDemande().getSimpleDemande().getIdGestionnaire());
                contentStat.append(";");
                contentStat.append("\n");

                dossierDejaTraite.add(decision.getDemande().getDossier().getId());
            }
        }
        return dossierDejaTraite;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getMailAd() {
        return mailAd;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void process() throws Exception {
        try {
            BSession session = BSessionUtil.getSessionFromThreadContext();

            List<String> dossierDejaTraite = new ArrayList<String>();

            contentStat.append("NSS;");
            contentStat.append("Nom;");
            contentStat.append("Prénom;");
            contentStat.append("Date de naissance;");
            contentStat.append("Rue;");
            contentStat.append("Numéro;");
            contentStat.append("NPA;");
            contentStat.append("Localité;");
            contentStat.append("Vient du RI;");
            contentStat.append("Type de décision;");
            contentStat.append("Enfant 1;");
            contentStat.append("Enfant 2;");
            contentStat.append("Enfant 3;");
            contentStat.append("Enfant 4;");
            contentStat.append("Enfant 5;");
            contentStat.append("Enfant 6;");
            contentStat.append("Enfant 7;");
            contentStat.append("Enfant 8;");
            contentStat.append("Montant de la prestation mensuelle;");
            contentStat.append("Excedant de revenu;");
            contentStat.append("Fortune nette;");
            contentStat.append("Revenu déterminant;");
            contentStat.append("Revenu d'activités;");
            contentStat.append("Dépenses reconnues;");
            contentStat.append("Frais de transport plafonnés;");
            contentStat.append("Frais de repas;");
            contentStat.append("Besoins vitaux;");
            contentStat.append("AIL;");
            contentStat.append("Bourses d'études;");
            contentStat.append("Montant plafonné (annuel);");
            contentStat.append("Mesure (encouragement);");
            contentStat.append("Mesure (charges loyer);");
            contentStat.append("Mesure de coaching ;");
            contentStat.append("Montant de la mesure de coaching;");
            contentStat.append("Montant touché au RI;");
            contentStat.append("Date de début de la prestation;");
            contentStat.append("Date de fin de la prestation;");
            contentStat.append("Nombre de révision extraordinaire;");
            contentStat.append("Gestionnaire");
            contentStat.append("\n");

            DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
            decisionSearchModel.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
            decisionSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            decisionSearchModel.setBetweenDateValidationDebut("01." + dateDebut);
            decisionSearchModel.setBetweenDateValidationFin(JadeDateUtil.addDays(
                    JadeDateUtil.addMonths("01." + dateFin, 1), -1));
            decisionSearchModel
                    .setOrderKey(DecisionSearchModel.ORDER_BY_DATE_FIN_AND_DATE_VALIDATION_AND_NUM_DECISION_DESC);
            decisionSearchModel = PerseusServiceLocator.getDecisionService().search(decisionSearchModel);
            dossierDejaTraite = doTraitement(session, dossierDejaTraite, decisionSearchModel);

            JadeSmtpClient.getInstance().sendMail(
                    mailAd,
                    getSession().getLabel("CSV_STATISTIQUES_DETAILLEES_D_TTIRE_MAIL") + " " + dateDebut + " - "
                            + dateFin,
                    getSession().getLabel("CSV_STATISTIQUES_DETAILLEES_D_TTIRE_MAIL") + " " + dateDebut + " - "
                            + dateFin,
                    new String[] { createFile(contentStat.toString(),
                            getSession().getLabel("CSV_STATISTIQUES_DETAILLEES_D_TTIRE_MAIL") + " " + dateDebut + " - "
                                    + dateFin) });

        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(),
                    e.getMessage() + System.getProperty("line.separator") + System.getProperty("line.separator")
                            + "Erreur : " + System.getProperty("line.separator") + e.getClass());
            e.printStackTrace();
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)
                || JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            JadeBusinessMessage[] messages = JadeThread.logMessages();
            for (int i = 0; i < messages.length; i++) {
                getLogSession().addMessage(messages[i]);
            }
        }
        if (getLogSession().hasMessages()) {
            List<String> email = new ArrayList<String>();
            email.add(mailAd);
            this.sendCompletionMail(email);
        }
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setMailAd(String mailAd) {
        this.mailAd = mailAd;
    }

}
