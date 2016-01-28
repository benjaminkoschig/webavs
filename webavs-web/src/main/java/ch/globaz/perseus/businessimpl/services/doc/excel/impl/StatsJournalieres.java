package ch.globaz.perseus.businessimpl.services.doc.excel.impl;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.exceptions.doc.DocException;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.models.qd.Facture;
import ch.globaz.perseus.business.models.qd.FactureSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author mbo
 * 
 */
public class StatsJournalieres extends PerseusAbstractExcelServiceImpl {

    public final static String MODEL_NAME = "statsJournalieres.xml";
    private PerseusContainer container;
    private String jourDebut = null;
    private String jourFin = null;
    private String outPutName = "statsJournalieres";

    public StatsJournalieres() {
        container = new PerseusContainer();
    }

    /**
     * @param jourDebut
     *            (JJ.MM.YYYY)
     * @param jourFin
     *            (JJ.MM.YYYY)
     * @return
     * @throws DocException
     */
    public ExcelmlWorkbook createDoc(String jourDebut, String jourFin) throws DocException {
        if ((jourDebut == null)) {
            throw new DocException("Unable to execute createDoc, the jourDebut is null !");
        }
        if ((jourFin == null)) {
            throw new DocException("Unable to execute createDoc, the jourFin is null !");
        }
        this.jourDebut = jourDebut;
        this.jourFin = jourFin;
        return this.createDoc();
    }

    /**
     * @param jourDebut
     *            (JJ.MM.YYYY)
     * @param jourFin
     *            (JJ.MM.YYYY)
     * @return
     * @throws Exception
     */
    public String createDocAndSave(String jourDebut, String jourFin) throws Exception {
        if ((jourDebut == null)) {
            throw new DocException("Unable to execute createDoc, the jourDebut is null !");
        }
        if ((jourFin == null)) {
            throw new DocException("Unable to execute createDoc, the jourFin is null !");
        }
        ExcelmlWorkbook wk = this.createDoc(jourDebut, jourFin);
        String nomDoc = outPutName + "_" + JadeUUIDGenerator.createStringUUID() + ".xml";
        return save(wk, nomDoc);
    }

    public String getJourDebut() {
        return jourDebut;
    }

    public String getJourFin() {
        return jourFin;
    }

    @Override
    public String getModelName() {
        return StatsJournalieres.MODEL_NAME;
    }

    @Override
    public String getOutPutName() {
        return outPutName;
    }

    /**
     * Retourne la session
     * 
     * @return
     */
    protected BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

    @Override
    public IMergingContainer loadResults() throws Exception {

        // Chargement de toutes les demandes dans une map
        HashMap<String, ArrayList<Demande>> listeDemandesParGestionnaire = new HashMap<String, ArrayList<Demande>>();

        DemandeSearchModel demandeSearchModel = new DemandeSearchModel();
        demandeSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        demandeSearchModel = PerseusServiceLocator.getDemandeService().search(demandeSearchModel);

        for (JadeAbstractModel demandeAbstractModel : demandeSearchModel.getSearchResults()) {
            Demande demande = (Demande) demandeAbstractModel;
            if (!listeDemandesParGestionnaire.containsKey(demande.getSimpleDemande().getIdGestionnaire())) {
                listeDemandesParGestionnaire.put(demande.getSimpleDemande().getIdGestionnaire(),
                        new ArrayList<Demande>());
            }
            listeDemandesParGestionnaire.get(demande.getSimpleDemande().getIdGestionnaire()).add(demande);
        }

        // Chargement des décisions
        HashMap<String, ArrayList<Decision>> listeDecisionsParGestionnaire = new HashMap<String, ArrayList<Decision>>();

        DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
        decisionSearchModel.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
        decisionSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        decisionSearchModel = PerseusServiceLocator.getDecisionService().search(decisionSearchModel);

        for (JadeAbstractModel model : decisionSearchModel.getSearchResults()) {
            Decision decision = (Decision) model;
            if (!listeDecisionsParGestionnaire
                    .containsKey(decision.getDemande().getSimpleDemande().getIdGestionnaire())) {
                listeDecisionsParGestionnaire.put(decision.getDemande().getSimpleDemande().getIdGestionnaire(),
                        new ArrayList<Decision>());
            }
            listeDecisionsParGestionnaire.get(decision.getDemande().getSimpleDemande().getIdGestionnaire()).add(
                    decision);
        }

        String[] gestionnaires = JadeAdminServiceLocatorProvider
                .getLocator()
                .getUserGroupService()
                .findAllIdUserForIdGroup(
                        GlobazSystem.getApplication("PERSEUS").getProperty("groupe.perseus.gestionnaire"));

        int demandeEnregistreeHorsRiTotal = 0;
        int demandeEnregistreeRiTotal = 0;
        int demandeValideeHorsRiTotal = 0;
        int demandeValideeRiTotal = 0;
        int demandeSuspensHorsRiTotal = 0;
        int demandeSuspensRiTotal = 0;

        for (String gestionnaire : gestionnaires) {
            int demandeEnregistreeHorsRi = 0;
            int demandeEnregistreeRi = 0;
            int demandeValideeHorsRi = 0;
            int demandeValideeRi = 0;
            int demandeSuspensHorsRi = 0;
            int demandeSuspensRi = 0;

            if (listeDemandesParGestionnaire.containsKey(gestionnaire)) {
                for (Demande demande : listeDemandesParGestionnaire.get(gestionnaire)) {
                    String dateDepot = demande.getCreationDate();

                    // Demande enregistrées dans la période
                    if (dateDepot.equals(jourDebut)
                            || dateDepot.equals(jourFin)
                            || (JadeDateUtil.isDateBefore(dateDepot, jourFin) && JadeDateUtil.isDateAfter(dateDepot,
                                    jourDebut))) {
                        // Traitement des demandes HorsRI
                        if (!demande.getSimpleDemande().getFromRI()) {
                            demandeEnregistreeHorsRi++;
                            demandeEnregistreeHorsRiTotal++;
                        } else {
                            demandeEnregistreeRi++;
                            demandeEnregistreeRiTotal++;
                        }
                    }

                    // Traitement des demandes en suspens
                    if (!CSEtatDemande.VALIDE.getCodeSystem().equals(demande.getSimpleDemande().getCsEtatDemande())) {
                        // Traitement des demandes HorsRI
                        if (!demande.getSimpleDemande().getFromRI()) {
                            demandeSuspensHorsRi++;
                            demandeSuspensHorsRiTotal++;
                        } else {
                            demandeSuspensRi++;
                            demandeSuspensRiTotal++;
                        }
                    }
                }
            }

            if (listeDecisionsParGestionnaire.containsKey(gestionnaire)) {
                for (Decision decision : listeDecisionsParGestionnaire.get(gestionnaire)) {
                    String dateValidation = decision.getSimpleDecision().getDateValidation();

                    if (dateValidation.equals(jourDebut)
                            || dateValidation.equals(jourFin)
                            || (JadeDateUtil.isDateBefore(dateValidation, jourFin) && JadeDateUtil.isDateAfter(
                                    dateValidation, jourDebut))) {
                        if (!decision.getDemande().getSimpleDemande().getFromRI()) {
                            demandeValideeHorsRi++;
                            demandeValideeHorsRiTotal++;
                        } else {
                            demandeValideeRi++;
                            demandeValideeRiTotal++;
                        }
                    }

                }
            }

            container.put("ong1lig1colA", gestionnaire);
            container.put("ong1lig1colB", String.valueOf(demandeEnregistreeHorsRi));
            container.put("ong1lig1colC", String.valueOf(demandeEnregistreeRi));
            container.put("ong1lig1colD", String.valueOf(demandeEnregistreeRi + demandeEnregistreeHorsRi));
            container.put("ong1lig1colE", String.valueOf(demandeValideeHorsRi));
            container.put("ong1lig1colF", String.valueOf(demandeValideeRi));
            container.put("ong1lig1colG", String.valueOf(demandeValideeRi + demandeValideeHorsRi));
            container.put("ong1lig1colH", String.valueOf(demandeSuspensHorsRi));
            container.put("ong1lig1colI", String.valueOf(demandeSuspensRi));
            container.put("ong1lig1colJ", String.valueOf(demandeSuspensRi + demandeSuspensHorsRi));

        }

        container.put("ong1lig2colB", String.valueOf(demandeEnregistreeHorsRiTotal));
        container.put("ong1lig2colC", String.valueOf(demandeEnregistreeRiTotal));
        container.put("ong1lig2colD", String.valueOf(demandeEnregistreeRiTotal + demandeEnregistreeHorsRiTotal));
        container.put("ong1lig2colE", String.valueOf(demandeValideeHorsRiTotal));
        container.put("ong1lig2colF", String.valueOf(demandeValideeRiTotal));
        container.put("ong1lig2colG", String.valueOf(demandeValideeRiTotal + demandeValideeHorsRiTotal));
        container.put("ong1lig2colH", String.valueOf(demandeSuspensHorsRiTotal));
        container.put("ong1lig2colI", String.valueOf(demandeSuspensRiTotal));
        container.put("ong1lig2colJ", String.valueOf(demandeSuspensRiTotal + demandeSuspensHorsRiTotal));

        // Dates période
        container.put("ong1Periodes", "Période : du " + jourDebut + " au " + jourFin);

        loadResultsFactures(gestionnaires);

        return container;
    }

    public IMergingContainer loadResultsFactures(String[] gestionnaires) throws Exception {

        // On charge toutes les factures
        FactureSearchModel factureSearchModel = new FactureSearchModel();
        factureSearchModel = PerseusServiceLocator.getFactureService().search(factureSearchModel);

        Map<String, List<Facture>> listeFactureParGestionnaire = new HashMap<String, List<Facture>>();

        for (JadeAbstractModel model : factureSearchModel.getSearchResults()) {
            Facture facture = (Facture) model;
            if (!listeFactureParGestionnaire.containsKey(facture.getSimpleFacture().getIdGestionnaire())) {
                listeFactureParGestionnaire.put(facture.getSimpleFacture().getIdGestionnaire(),
                        new ArrayList<Facture>());
            }
            listeFactureParGestionnaire.get(facture.getSimpleFacture().getIdGestionnaire()).add(facture);
        }

        int nbFacturesTotal = 0;
        double montantFacturesTotal = 0.0;

        for (String gestionnaire : gestionnaires) {
            int nbFactures = 0;
            double montantFactures = 0.0;

            if (listeFactureParGestionnaire.containsKey(gestionnaire)) {
                for (Facture facture : listeFactureParGestionnaire.get(gestionnaire)) {
                    String dateReception = facture.getSimpleFacture().getDateReception();

                    if (dateReception.equals(jourDebut)
                            || dateReception.equals(jourFin)
                            || (JadeDateUtil.isDateBefore(dateReception, jourFin) && JadeDateUtil.isDateAfter(
                                    dateReception, jourDebut))) {
                        nbFactures++;
                        nbFacturesTotal++;
                        montantFactures += Double.parseDouble(facture.getSimpleFacture().getMontantRembourse());
                        montantFacturesTotal += Double.parseDouble(facture.getSimpleFacture().getMontantRembourse());
                    }

                }
            }

            container.put("ong2lig1colA", gestionnaire);
            container.put("ong2lig1colB", String.valueOf(nbFactures));
            container.put("ong2lig1colC", String.valueOf(montantFactures));

        }

        container.put("ong2lig2colB", String.valueOf(nbFacturesTotal));
        container.put("ong2lig2colC", String.valueOf(montantFacturesTotal));

        container.put("ong2Periodes", "Période : du " + jourDebut + " au " + jourFin);

        return container;
    }

    public void setJourDebut(String jourDebut) {
        this.jourDebut = jourDebut;
    }

    public void setJourFin(String jourFin) {
        this.jourFin = jourFin;
    }

}
