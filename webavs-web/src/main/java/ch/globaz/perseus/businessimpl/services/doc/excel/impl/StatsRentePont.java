package ch.globaz.perseus.businessimpl.services.doc.excel.impl;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ch.globaz.perseus.business.constantes.CSEtatLot;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.constantes.CSTypeSoin;
import ch.globaz.perseus.business.exceptions.doc.DocException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.LotSearchModel;
import ch.globaz.perseus.business.models.lot.PrestationRP;
import ch.globaz.perseus.business.models.lot.PrestationSearchModel;
import ch.globaz.perseus.business.models.rentepont.FactureRentePont;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.models.rentepont.RentePontSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author jsi
 * @update mbo
 * 
 */
public class StatsRentePont extends PerseusAbstractExcelServiceImpl {

    /**
     * Commentaires : ong = onglet du classeur col = colonne de la feuille
     */

    public final static String MODEL_NAME = "statsRentePont.xml";
    private CommonExcelmlContainer container;
    private String moisDebut = null;
    private String moisFin = null;
    private String outPutName = "statsRentePont";

    double totalMontant = 0.0;
    int totalPersonnes = 0;
    int totalPrestations = 0;

    public StatsRentePont() {
        container = new CommonExcelmlContainer();
    }

    private void addStatistique(String libelle, int nbPersonnes, int nbPrestations, double montant) {
        container.put("ong4libelle", libelle);
        container.put("ong4nbPersonnes", Integer.toString(nbPersonnes));
        container.put("ong4nbPrestations", Integer.toString(nbPrestations));
        container.put("ong4montant", Double.toString(montant));
        totalPersonnes += nbPersonnes;
        totalPrestations += nbPrestations;
        totalMontant += montant;
    }

    public void compterLot(CSTypeLot typeLot, List<String> listeMois, String titre, boolean restit)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        double montant = 0.0;
        int nbPrest = 0;
        List<String> nbrDossiers = new ArrayList<String>();
        for (String mois : listeMois) {
            LotSearchModel lotSearchModel = new LotSearchModel();
            lotSearchModel.setForTypeLot(typeLot.getCodeSystem());
            lotSearchModel.setForMoisComptabilisation(mois);
            lotSearchModel.setForEtatCs(CSEtatLot.LOT_VALIDE.getCodeSystem());
            lotSearchModel = PerseusServiceLocator.getLotService().search(lotSearchModel);
            for (JadeAbstractModel model : lotSearchModel.getSearchResults()) {
                Lot lot = (Lot) model;
                PrestationSearchModel psm = new PrestationSearchModel();
                psm.setForIdLot(lot.getId());
                psm.getInTypeLot().add(typeLot.getCodeSystem());
                psm.setModelClass(PrestationRP.class);
                psm.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                psm = PerseusServiceLocator.getPrestationService().search(psm);
                for (JadeAbstractModel m : psm.getSearchResults()) {
                    PrestationRP prest = (PrestationRP) m;
                    Float mPr = Float.parseFloat(prest.getSimplePrestation().getMontantTotal());
                    // Voir si on compte que les réstits
                    if ((restit && (mPr < 0)) || (!restit && (mPr > 0))) {
                        montant += mPr;
                        nbPrest++;
                        String idDossier = null;
                        if (CSTypeLot.LOT_DECISION_RP.equals(typeLot)) {
                            idDossier = prest.getRentePont().getDossier().getId();
                        } else {
                            idDossier = prest.getFacture().getQdRentePont().getDossier().getId();
                        }
                        if (!nbrDossiers.contains(idDossier)) {
                            nbrDossiers.add(idDossier);
                        }
                    }
                }
            }
        }

        addStatistique(titre, nbrDossiers.size(), nbPrest, montant);
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

    @Override
    public String getModelName() {
        return StatsRentePont.MODEL_NAME;
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

    @Override
    public IMergingContainer loadResults() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        // Charger les demandes
        HashMap<String, ArrayList<Demande>> listeDemandesParAdministrationCommunale = new HashMap<String, ArrayList<Demande>>();

        int nbDemandesEnregistrees = 0;
        double montantDemandesEnregistrees = 0.0;
        int nbNewOctrois = 0;
        double montantNewOctrois = 0.0;
        int nbNewRefus = 0;

        RentePontSearchModel rentePontSearchModel = new RentePontSearchModel();
        rentePontSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        rentePontSearchModel = PerseusServiceLocator.getRentePontService().search(rentePontSearchModel);
        for (JadeAbstractModel model : rentePontSearchModel.getSearchResults()) {
            RentePont rentePont = (RentePont) model;
            // Demandes enregistrées dans la période
            String dateMoisDepot = rentePont.getCreationDate().substring(3);
            if (dateMoisDepot.equals(moisFin)
                    || dateMoisDepot.equals(moisDebut)
                    || (JadeDateUtil.isDateMonthYearBefore(dateMoisDepot, moisFin) && JadeDateUtil
                            .isDateMonthYearAfter(dateMoisDepot, moisDebut))) {
                nbDemandesEnregistrees++;
                montantDemandesEnregistrees += Double.parseDouble(rentePont.getSimpleRentePont().getMontant());
            }
            // Demande qui démarre dans la période
            // String dateMoisDebut = rentePont.getSimpleRentePont().getDateDebut().substring(3);
            String dateMoisDebut = rentePont.getSimpleRentePont().getDateDecision();
            if (dateMoisDebut.equals(moisFin)
                    || dateMoisDebut.equals(moisDebut)
                    || (JadeDateUtil.isDateMonthYearBefore(dateMoisDebut, moisFin) && JadeDateUtil
                            .isDateMonthYearAfter(dateMoisDebut, moisDebut))) {
                if (JadeNumericUtil.isEmptyOrZero(rentePont.getSimpleRentePont().getMontant())) {
                    nbNewRefus++;
                } else {
                    nbNewOctrois++;
                    montantNewOctrois += Double.parseDouble(rentePont.getSimpleRentePont().getMontant());
                }
            }
        }

        // Dates période
        container.put("ong1Periodes", "Période : de " + moisDebut + " à " + moisFin);
        container.put("ong3Periodes", "Période : de " + moisDebut + " à " + moisFin);
        container.put("ong4Periodes", "Période : de " + moisDebut + " à " + moisFin);

        container.put("ong1Libelle", "Demandes enregistrées (date de réception)");
        container.put("ong1Nbr", String.valueOf(nbDemandesEnregistrees));
        container.put("ong1Montant", String.valueOf(montantDemandesEnregistrees));

        container.put("ong1Libelle", "Décisions d'octroi");
        container.put("ong1Nbr", String.valueOf(nbNewOctrois));
        container.put("ong1Montant", String.valueOf(montantNewOctrois));

        container.put("ong1Libelle", "Décisions de refus");
        container.put("ong1Nbr", String.valueOf(nbNewRefus));
        container.put("ong1Montant", "-");

        loadResultsQd();

        loadResultsCompta();

        return container;
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

        double totalPrestations = 0.0;
        int nbPrestations = 0;
        for (String moisEnCours : listeMoisPrestMensuelle) {

            // Compter les préstation mensuelles versées pendant le mois
            List<RentePont> prestationsMensuelles = PerseusServiceLocator.getPmtMensuelRentePontService()
                    .listRentePontEnCours(moisEnCours, false);
            for (RentePont rentePont : prestationsMensuelles) {
                totalPrestations += Float.parseFloat(rentePont.getSimpleRentePont().getMontant());
                if (Float.parseFloat(rentePont.getSimpleRentePont().getMontant()) > 0) {
                    nbPrestations++;
                }
            }
        }
        // Prestations mensuelles
        addStatistique("Prestations mensuelles", nbPrestations, nbPrestations, totalPrestations);

        compterLot(CSTypeLot.LOT_DECISION_RP, listeMois, "Décisions", false);
        compterLot(CSTypeLot.LOT_DECISION_RP, listeMois, "Réstitutions", true);

        compterLot(CSTypeLot.LOT_FACTURES_RP, listeMois, "Remboursement de factures", false);
        compterLot(CSTypeLot.LOT_FACTURES_RP, listeMois, "Réstitutions de factures", true);

        // Ajout de la ligne de total
        container.put("ong4totalPersonnes", Integer.toString(totalPersonnes));
        container.put("ong4totalPrestations", Integer.toString(this.totalPrestations));
        container.put("ong4totalMontant", Double.toString(totalMontant));

        return container;
    }

    // Chargement du résultat des QD
    public IMergingContainer loadResultsQd() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        HashMap<String, ArrayList<FactureRentePont>> listeQdFactures = new HashMap<String, ArrayList<FactureRentePont>>();

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
            lotSearchModel.setForTypeLot(CSTypeLot.LOT_FACTURES_RP.getCodeSystem());
            lotSearchModel.setForMoisComptabilisation(mois);
            lotSearchModel.setForEtatCs(CSEtatLot.LOT_VALIDE.getCodeSystem());
            lotSearchModel = PerseusServiceLocator.getLotService().search(lotSearchModel);
            for (JadeAbstractModel model : lotSearchModel.getSearchResults()) {
                Lot lot = (Lot) model;
                PrestationSearchModel psm = new PrestationSearchModel();
                psm.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                psm.setForIdLot(lot.getId());
                psm.setModelClass(PrestationRP.class);
                psm = PerseusServiceLocator.getPrestationService().search(psm);

                for (JadeAbstractModel modelPrestation : psm.getSearchResults()) {
                    PrestationRP prestation = (PrestationRP) modelPrestation;
                    FactureRentePont facture = prestation.getFacture();
                    String key = facture.getSimpleFactureRentePont().getCsSousTypeSoinRentePont();
                    if (!listeQdFactures.containsKey(key)) {
                        listeQdFactures.put(key, new ArrayList<FactureRentePont>());
                    }
                    listeQdFactures.get(key).add(facture);
                }
            }
        }

        // Préparer une liste de type de soins à parcourir hiérarchiquement
        HashMap<String, CSTypeSoin> mapCsTypeSoin = new HashMap<String, CSTypeSoin>();
        HashMap<CSTypeSoin, List<CSTypeSoin>> mapTypesSoins = new HashMap<CSTypeSoin, List<CSTypeSoin>>();
        for (CSTypeSoin typeSoin : CSTypeSoin.values()) {
            if (JadeStringUtil.isEmpty(typeSoin.getCodeSystemParent())) {
                mapTypesSoins.put(typeSoin, new ArrayList<CSTypeSoin>());
            }
            mapCsTypeSoin.put(typeSoin.getCodeSystem(), typeSoin);
        }
        for (CSTypeSoin typeSoin : CSTypeSoin.values()) {
            if (!JadeStringUtil.isEmpty(typeSoin.getCodeSystemParent())) {
                mapTypesSoins.get(mapCsTypeSoin.get(typeSoin.getCodeSystemParent())).add(typeSoin);
            }
        }

        int nbrTotalDossier = 0;
        int nbrTotalPrestation = 0;
        double montantTotalPrestation = 0.0;
        double montantTotalDepassementQd = 0.0;

        for (CSTypeSoin typeSoin : mapTypesSoins.keySet()) {
            int nbrPrestationType = 0;
            List<String> listDossierType = new ArrayList<String>();
            int nbrDossierType = 0;
            double montantType = 0.0;
            double montantDepassantType = 0.0;
            String nomType = null;

            nomType = "Total " + getSession().getCodeLibelle(typeSoin.getCodeSystem());

            for (CSTypeSoin sousTypeSoin : mapTypesSoins.get(typeSoin)) {
                int nbrPrestationSousType = 0;
                int nbrDossierSousType = 0;
                List<String> listDossierSousType = new ArrayList<String>();
                double montantSousType = 0.0;
                double montantDepassantSousType = 0.0;
                String nomSousType = null;

                nomSousType = getSession().getCodeLibelle(sousTypeSoin.getCodeSystem());

                if (listeQdFactures.containsKey(sousTypeSoin.getCodeSystem())) {
                    List<FactureRentePont> listeFactures = listeQdFactures.get(sousTypeSoin.getCodeSystem());

                    for (FactureRentePont facture : listeFactures) {

                        if (!listDossierType.contains(facture.getQdRentePont().getDossier().getId())) {
                            nbrDossierType++;
                            listDossierType.add(facture.getQdRentePont().getDossier().getId());
                        }
                        if (!listDossierSousType.contains(facture.getQdRentePont().getDossier().getId())) {
                            nbrDossierSousType++;
                            listDossierSousType.add(facture.getQdRentePont().getDossier().getId());
                        }

                        nbrPrestationSousType++;

                        montantDepassantSousType += Double.valueOf(facture.getSimpleFactureRentePont()
                                .getMontantDepassant());
                        montantSousType += Double.valueOf(facture.getSimpleFactureRentePont().getMontantRembourse());

                    }

                }

                nbrPrestationType += nbrPrestationSousType;
                montantType += montantSousType;
                montantDepassantType += montantDepassantSousType;

                // Mettre les données dans le fichier excel
                container.put("ong3lig1colC", " - " + String.valueOf(nomSousType));
                container.put("ong3lig1colD", String.valueOf(nbrDossierSousType));
                container.put("ong3lig1colE", String.valueOf(nbrPrestationSousType));
                container.put("ong3lig1colF", String.valueOf(montantSousType));
                container.put("ong3lig1colG", String.valueOf(montantDepassantSousType));
            }

            nbrTotalDossier += nbrDossierType;
            nbrTotalPrestation += nbrPrestationType;
            montantTotalPrestation += montantType;
            montantTotalDepassementQd += montantDepassantType;

            // Mettre les données dans le fichier excel
            container.put("ong3lig1colC", String.valueOf(nomType));
            container.put("ong3lig1colD", String.valueOf(nbrDossierType));
            container.put("ong3lig1colE", String.valueOf(nbrPrestationType));
            container.put("ong3lig1colF", String.valueOf(montantType));
            container.put("ong3lig1colG", String.valueOf(montantDepassantType));

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

}
