package ch.globaz.perseus.businessimpl.services.doc.excel.impl;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.perseus.business.constantes.CSEtatLot;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.exceptions.doc.DocException;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.LotSearchModel;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.lot.PrestationSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.qd.CSTypeQD;
import ch.globaz.perseus.business.models.qd.Facture;
import ch.globaz.perseus.business.models.statistique.StatistiquesMensuellesDemPcfDec;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.statsmensuelles.Administration;
import ch.globaz.perseus.business.statsmensuelles.TypeStat;
import ch.globaz.perseus.businessimpl.services.statistiquesMensuelles.StatistiquesMensuellesDBNewPersistence;
import ch.globaz.perseus.businessimpl.services.statistiquesMensuelles.StatistiquesMensuellesDataMonitoringContainer;
import ch.globaz.perseus.businessimpl.services.statistiquesMensuelles.StatistiquesMensuellesDonnees;

/**
 * @author jsi
 * @update mbo
 * 
 */
public class StatsMensuelles extends PerseusAbstractExcelServiceImpl {

    /**
     * Commentaires : ong = onglet du classeur col = colonne de la feuille
     */

    public final static String MODEL_NAME = "statsMensuelles.xml";
    private PerseusContainer container;
    private String moisDebut = null;
    private String moisFin = null;
    private String outPutName = "statsMensuelles";

    FWCurrency totalMontant = new FWCurrency(0.00);
    int totalPersonnes = 0;
    int totalPrestations = 0;

    private FWCurrency montantRestitution;

    private FWCurrency montantRetro;
    private List<String> nbDossierDecision;
    private List<String> nbDossierRestitution;

    private int nbPrestDecision;
    private int nbPrestRestitution;

    public StatsMensuelles() {
        container = new PerseusContainer();
    }

    /**
     * @param moisDebut
     *            (MM.YYYY)
     * @param moisFin
     *            (MM.YYYY)
     * @return
     * @throws Exception
     */
    public String createDocAndSave(String moisDebut, String moisFin) throws Exception {
        if ((moisDebut == null) || (moisDebut.length() != 7)) {
            throw new DocException("Unable to execute createDoc, the moisDebut is null or size missmatch !");
        }
        if ((moisFin == null) || (moisFin.length() != 7)) {
            throw new DocException("Unable to execute createDoc, the moisFin is null or size missmatch !");
        }
        ExcelmlWorkbook wk = this.createDoc(moisDebut, moisFin);
        String nomDoc = outPutName + "_" + JadeUUIDGenerator.createStringUUID() + ".xml";
        return save(wk, nomDoc);
    }

    /*
     * ****************************************** SECTION OPTIMISEE ****************************************
     */
    @Override
    public IMergingContainer loadResults() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        // Mise en place des données pour les administrations
        TreeMap<String, Administration> administrations = StatistiquesMensuellesDBNewPersistence
                .getMapAdministrations();

        // conversion en list du modèle de recherche
        List<StatistiquesMensuellesDemPcfDec> listeStatsMensuelles = StatistiquesMensuellesDBNewPersistence
                .getListeStatistiquesMensuellesDemPcfDec(moisDebut, moisFin);

        // on fait un groupby by par idDossier
        Map<String, List<StatistiquesMensuellesDemPcfDec>> statsGroupesParDossier = JadeListUtil.groupBy(
                listeStatsMensuelles, new Key<StatistiquesMensuellesDemPcfDec>() {
                    @Override
                    public String exec(StatistiquesMensuellesDemPcfDec modele) {
                        return modele.getDemande().getIdDossier(); // On définit sur quoi on fait le GROUPBY
                    }
                });

        // Recherche de l'état d'origine des RI HorsRI
        Map<String, Map<TypeStat, Boolean>> mapDossierRI = StatistiquesMensuellesDBNewPersistence.getMapDossierRI();

        Map<String, Boolean> mapDemandeRI = new HashMap<String, Boolean>();
        Map<String, Boolean> mapAutreRI = new HashMap<String, Boolean>();

        for (String idDossier : mapDossierRI.keySet()) {
            if (mapDossierRI.get(idDossier).get(TypeStat.AUTRE) != null) {
                mapAutreRI.put(idDossier, mapDossierRI.get(idDossier).get(TypeStat.AUTRE));
            }
            if (mapDossierRI.get(idDossier).get(TypeStat.DEMANDE) != null) {
                mapDemandeRI.put(idDossier, mapDossierRI.get(idDossier).get(TypeStat.DEMANDE));
            }
        }

        StatistiquesMensuellesDonnees dataMonitoring = new StatistiquesMensuellesDonnees(administrations,
                statsGroupesParDossier, mapAutreRI, mapDemandeRI, moisDebut, moisFin);
        StatistiquesMensuellesDataMonitoringContainer container = new StatistiquesMensuellesDataMonitoringContainer();
        this.container = container.getPerseusContainer(dataMonitoring, moisDebut, moisFin);
        loadResultsQd();
        loadResultsCompta();
        return this.container;
    }

    /*
     * ****************************************** SECTION NON OPTIMISEE ****************************************
     */
    private void addStatistique(String libelle, int nbPersonnes, int nbPrestations, FWCurrency montant) {
        container.put("ong4libelle", libelle);
        container.put("ong4nbPersonnes", Integer.toString(nbPersonnes));
        container.put("ong4nbPrestations", Integer.toString(nbPrestations));
        container.put("ong4montant", montant.toString());
        totalPersonnes += nbPersonnes;
        totalPrestations += nbPrestations;
        totalMontant.add(montant);
    }

    private void addStatistiqueLotDecision() {
        addStatistique(getSession().getLabel("PF_STATS_MENSUELLES_RETROACTIF"), nbDossierDecision.size(),
                nbPrestDecision, montantRetro);
        addStatistique(getSession().getLabel("PF_STATS_MENSUELLES_RESTITUTION"), nbDossierRestitution.size(),
                nbPrestRestitution, montantRestitution);
    }

    private void addStatistiqueLotFacture() {
        addStatistique(getSession().getLabel("PF_STATS_MENSUELLES_REMBOURSEMENT_FACTURES"), nbDossierDecision.size(),
                nbPrestDecision, montantRetro);
        addStatistique(getSession().getLabel("PF_STATS_MENSUELLES_RESTITUTION_FACTURES"), nbDossierRestitution.size(),
                nbPrestRestitution, montantRestitution);
    }

    private void ajouterStatistique(String typeLot) {
        if (CSTypeLot.LOT_DECISION.getCodeSystem().equals(typeLot)) {
            addStatistiqueLotDecision();
        } else if (CSTypeLot.LOT_FACTURES.getCodeSystem().equals(typeLot)) {
            addStatistiqueLotFacture();
        }
    }

    public void compterLot(CSTypeLot typeLot, List<String> listMois)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        montantRetro = new FWCurrency();
        montantRestitution = new FWCurrency();
        nbPrestDecision = 0;
        nbPrestRestitution = 0;
        nbDossierDecision = new ArrayList<String>();
        nbDossierRestitution = new ArrayList<String>();
        for (String mois : listMois) {
            LotSearchModel lotSearchModel = LoadLotValideForCSType(typeLot, mois);
            for (JadeAbstractModel model : lotSearchModel.getSearchResults()) {
                Lot lot = (Lot) model;
                PrestationSearchModel prestationSearchModel = loadPrestationForLot(lot.getSimpleLot().getIdLot(),
                        typeLot);
                for (JadeAbstractModel m : prestationSearchModel.getSearchResults()) {
                    Prestation prest = (Prestation) m;
                    traiterPrestation(prest, typeLot);
                }
            }

        }

        ajouterStatistique(typeLot.getCodeSystem());

    }

    /**
     * @param moisDebut
     *            (MM.YYYY)
     * @param moisFin
     *            (MM.YYYY)
     * @return
     * @throws DocException
     */
    public ExcelmlWorkbook createDoc(String moisDebut, String moisFin) throws DocException {
        if ((moisDebut == null) || (moisDebut.length() != 7)) {
            throw new DocException("Unable to execute createDoc, the moisDebut is null or size missmatch !");
        }
        if ((moisFin == null) || (moisFin.length() != 7)) {
            throw new DocException("Unable to execute createDoc, the moisFin is null or size missmatch !");
        }
        this.moisDebut = moisDebut;
        this.moisFin = moisFin;
        return this.createDoc();
    }

    @Override
    public String getModelName() {
        return StatsMensuelles.MODEL_NAME;
    }

    public String getMoisDebut() {
        return moisDebut;
    }

    public String getMoisFin() {
        return moisFin;
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

    private LotSearchModel LoadLotValideForCSType(CSTypeLot typeLot, String mois)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, LotException {
        LotSearchModel lotSearchModel = new LotSearchModel();
        lotSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        lotSearchModel.setForTypeLot(typeLot.getCodeSystem());
        lotSearchModel.setForMoisComptabilisation(mois);
        lotSearchModel.setForEtatCs(CSEtatLot.LOT_VALIDE.getCodeSystem());
        return PerseusServiceLocator.getLotService().search(lotSearchModel);
    }

    private PrestationSearchModel loadPrestationForLot(String idLot, CSTypeLot typeLot) throws LotException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        PrestationSearchModel psm = new PrestationSearchModel();
        psm.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        psm.setForIdLot(idLot);
        psm.getInTypeLot().add(typeLot.getCodeSystem());
        return PerseusServiceLocator.getPrestationService().search(psm);
    }

    public IMergingContainer loadResultsCompta() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        List<String> listeMois = new ArrayList<String>();
        List<String> listeMoisPrestMensuelle = new ArrayList<String>();
        String enCours = "01." + moisDebut;
        String enCoursPrestMensuelle = "01." + moisDebut;

        String moisDebutPourPrestationMensuelle = "";

        if (JadeDateUtil.isDateAfter("01." + moisFin, "01."
                + PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt())) {
            moisDebutPourPrestationMensuelle = PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt();
        } else {
            moisDebutPourPrestationMensuelle = moisFin;
        }

        while (!JadeDateUtil.isDateAfter(enCoursPrestMensuelle, "01." + moisDebutPourPrestationMensuelle)) {
            listeMoisPrestMensuelle.add(enCoursPrestMensuelle.substring(3));
            enCoursPrestMensuelle = JadeDateUtil.addMonths(enCoursPrestMensuelle, 1);
        }

        while (!JadeDateUtil.isDateAfter(enCours, "01." + moisFin)) {
            listeMois.add(enCours.substring(3));
            enCours = JadeDateUtil.addMonths(enCours, 1);
        }

        FWCurrency totalPrestations = new FWCurrency();
        int nbPrestations = 0;
        for (String moisEnCours : listeMoisPrestMensuelle) {
            // Compter les préstation mensuelles versées pendant le mois
            List<PCFAccordee> prestationsMensuelles = PerseusServiceLocator.getPmtMensuelService().listPCFAencours(
                    moisEnCours);
            for (PCFAccordee pcfa : prestationsMensuelles) {
                totalPrestations.add(pcfa.getSimplePCFAccordee().getMontant());
            }
            nbPrestations += prestationsMensuelles.size();
        }
        // Prestations mensuelles
        addStatistique("Prestations mensuelles", nbPrestations, nbPrestations, totalPrestations);

        compterLot(CSTypeLot.LOT_DECISION, listeMois);

        compterLot(CSTypeLot.LOT_FACTURES, listeMois);

        // Ajout de la ligne de total
        container.put("ong4totalPersonnes", Integer.toString(totalPersonnes));
        container.put("ong4totalPrestations", Integer.toString(this.totalPrestations));
        container.put("ong4totalMontant", totalMontant.toString());

        return container;
    }

    // Chargement du résultat des QD
    public IMergingContainer loadResultsQd() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        HashMap<String, ArrayList<Facture>> listeQdFactures = new HashMap<String, ArrayList<Facture>>();

        // BZ 6451
        // Parcourir les factures et les mettre dans la map
        // ...récupération des dates pour les périodes de recherches
        List<String> listeMois = new ArrayList<String>();
        String enCours = "01." + moisDebut;
        while (!JadeDateUtil.isDateAfter(enCours, "01." + moisFin)) {
            listeMois.add(enCours.substring(3));
            enCours = JadeDateUtil.addMonths(enCours, 1);
        }

        for (String mois : listeMois) {
            LotSearchModel lotSearchModel = new LotSearchModel();
            lotSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            lotSearchModel.setForTypeLot(CSTypeLot.LOT_FACTURES.getCodeSystem());
            lotSearchModel.setForMoisComptabilisation(mois);
            lotSearchModel.setForEtatCs(CSEtatLot.LOT_VALIDE.getCodeSystem());
            lotSearchModel = PerseusServiceLocator.getLotService().search(lotSearchModel);
            for (JadeAbstractModel model : lotSearchModel.getSearchResults()) {
                Lot lot = (Lot) model;
                PrestationSearchModel psm = new PrestationSearchModel();
                psm.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                psm.setForIdLot(lot.getId());
                psm = PerseusServiceLocator.getPrestationService().search(psm);

                for (JadeAbstractModel modelPrestation : psm.getSearchResults()) {
                    Prestation prestation = (Prestation) modelPrestation;
                    Facture facture = prestation.getFacture();
                    String key = facture.getQd().getSimpleQD().getCsType();
                    if (!listeQdFactures.containsKey(key)) {
                        listeQdFactures.put(key, new ArrayList<Facture>());
                    }
                    listeQdFactures.get(key).add(facture);
                }
            }
        }

        int nbrTotalDossier = 0;
        int nbrTotalPrestation = 0;
        double montantTotalPrestation = 0.0;
        double montantTotalDepassementQd = 0.0;

        // Parcour la map et faire la statistiques
        for (CSTypeQD typeQd : CSTypeQD.values()) {
            int nbrPrestation = 0;
            int nbrDossier = 0;
            double montantsParTypeQD = 0.0;
            double montantsDepassantParTypeQD = 0.0;
            String nomTypeQd = null;

            nomTypeQd = getSession().getCodeLibelle(typeQd.getCodeSystem());

            if (listeQdFactures.containsKey(typeQd.getCodeSystem())) {
                List<Facture> listeFactures = listeQdFactures.get(typeQd.getCodeSystem());

                List<String> listDossier = new ArrayList<String>();
                for (Facture facture : listeFactures) {

                    if (!listDossier.contains(facture.getQd().getQdAnnuelle().getDossier().getId())) {
                        nbrDossier++;
                        listDossier.add(facture.getQd().getQdAnnuelle().getDossier().getId());
                    }

                    nbrPrestation++;

                    montantsDepassantParTypeQD += Double.valueOf(facture.getSimpleFacture().getMontantDepassant());
                    montantsParTypeQD += Double.valueOf(facture.getSimpleFacture().getMontantRembourse());

                }

            }

            nbrTotalDossier += nbrDossier;
            nbrTotalPrestation += nbrPrestation;
            montantTotalPrestation += montantsParTypeQD;
            montantTotalDepassementQd += montantsDepassantParTypeQD;

            // Mettre les données dans le fichier excel
            container.put("ong3lig1colC", String.valueOf(nomTypeQd));
            container.put("ong3lig1colD", String.valueOf(nbrDossier));
            container.put("ong3lig1colE", String.valueOf(nbrPrestation));
            container.put("ong3lig1colF", String.valueOf(montantsParTypeQD));
            container.put("ong3lig1colG", String.valueOf(montantsDepassantParTypeQD));

        }

        container.put("ong3lig2colD", String.valueOf(nbrTotalDossier));
        container.put("ong3lig2colE", String.valueOf(nbrTotalPrestation));
        container.put("ong3lig2colF", String.valueOf(montantTotalPrestation));
        container.put("ong3lig2colG", String.valueOf(montantTotalDepassementQd));

        return container;

    }

    public void setMoisDebut(String moisDebut) {
        this.moisDebut = moisDebut;
    }

    public void setMoisFin(String moisFin) {
        this.moisFin = moisFin;
    }

    private void traiterPrestation(Prestation prestation, CSTypeLot typeLot) {
        traiterPrestationPourNombrePrestation(prestation.getSimplePrestation().getMontantTotal());
        traiterPrestationPourNombreDossier(prestation, typeLot);
        traiterPrestationPourMontantDecisionEtRestitution(prestation);

    }

    private void traiterPrestationPourMontantDecisionEtRestitution(Prestation prestation) {
        FWCurrency montantPrestationSansMesureCoaching = new FWCurrency();
        FWCurrency montantPrestation = new FWCurrency(prestation.getSimplePrestation().getMontantTotal());
        FWCurrency montantMesureCoaching = new FWCurrency(prestation.getSimplePrestation().getMontantMesureCoaching());

        montantPrestationSansMesureCoaching.add(montantPrestation);
        montantPrestationSansMesureCoaching.sub(montantMesureCoaching);

        if (montantPrestationSansMesureCoaching.isNegative()) {
            montantRestitution.add(montantPrestationSansMesureCoaching);
        } else if (montantPrestationSansMesureCoaching.isPositive()) {
            montantRetro.add(montantPrestationSansMesureCoaching);
        }

        if (montantMesureCoaching.isNegative()) {
            montantRestitution.add(montantMesureCoaching);
        } else if (montantMesureCoaching.isPositive()) {
            montantRetro.add(montantMesureCoaching);
        }
    }

    private void traiterPrestationPourNombreDossier(Prestation prestation, CSTypeLot typeLot) {
        String idDossier;
        if (CSTypeLot.LOT_DECISION.equals(typeLot)) {
            idDossier = prestation.getDecision().getDemande().getDossier().getId();
        } else {
            idDossier = prestation.getFacture().getQd().getQdAnnuelle().getDossier().getId();
        }

        if (Float.parseFloat(prestation.getSimplePrestation().getMontantTotal()) > 0) {
            if (!nbDossierDecision.contains(idDossier)) {
                nbDossierDecision.add(idDossier);
            }
        } else if (Float.parseFloat(prestation.getSimplePrestation().getMontantTotal()) < 0) {
            if (!nbDossierRestitution.contains(idDossier)) {
                nbDossierRestitution.add(idDossier);
            }
        }
    }

    private void traiterPrestationPourNombrePrestation(String montantPrestation) {
        if (Float.parseFloat(montantPrestation) > 0) {
            nbPrestDecision++;
        } else if (Float.parseFloat(montantPrestation) < 0) {
            nbPrestRestitution++;
        }
    }
}
