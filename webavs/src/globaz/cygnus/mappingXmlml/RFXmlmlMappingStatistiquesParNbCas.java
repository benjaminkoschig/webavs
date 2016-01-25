/*
 * Créé le 26 janvier 2012
 */
package globaz.cygnus.mappingXmlml;

import globaz.cygnus.db.statistiques.RFDemandeJointSousTypeParNbCas;
import globaz.cygnus.db.statistiques.RFDemandeRefusJointSousTypeParNbCas;
import globaz.cygnus.db.statistiques.RFDemandesJointSousTypeParNbCasManager;
import globaz.cygnus.db.statistiques.RFDemandesRefusJointSousTypeParNbCasManager;
import globaz.cygnus.db.statistiques.RFNombrePersonnesJointDemandesStatistiquesNbCasManager;
import globaz.cygnus.db.statistiques.RFNombrePersonnesTotalJointDemandesStatistiquesNbCas;
import globaz.cygnus.db.statistiques.RFNombrePersonnesTotalJointDemandesStatistiquesNbCasManager;
import globaz.cygnus.db.statistiques.RFNombrePersonnesTotalJointQdStatistiquesNbCas;
import globaz.cygnus.db.statistiques.RFNombrePersonnesTotalJointQdStatistiquesNbCasManager;
import globaz.cygnus.db.statistiques.RFNombreQdsJointDemandesStatistiquesParNbCasManager;
import globaz.cygnus.db.statistiques.RFSousTypeSoinsJointTypeSoinsStatistiques;
import globaz.cygnus.db.statistiques.RFSousTypeSoinsJointTypeSoinsStatistiquesManager;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.cygnus.exceptions.RFXmlmlException;
import globaz.cygnus.process.RFStatistiquesParNbCasProcess;
import globaz.cygnus.utils.RFXmlmlContainer;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author MBO
 */
public class RFXmlmlMappingStatistiquesParNbCas {

    private static String dateDebutStat = null;
    private static String dateFinStat = null;
    private static String gestionnaire = null;
    private static BSession session = null;

    public static String getDateDebutStat() {
        return RFXmlmlMappingStatistiquesParNbCas.dateDebutStat;
    }

    public static String getDateFinStat() {
        return RFXmlmlMappingStatistiquesParNbCas.dateFinStat;
    }

    public static String getDetailGestionnaire() throws Exception {
        String detailGestionnaire = PRGestionnaireHelper
                .getNomGestionnaire(RFXmlmlMappingStatistiquesParNbCas.gestionnaire);

        return detailGestionnaire;
    }

    public static String getGestionnaire() {
        return RFXmlmlMappingStatistiquesParNbCas.gestionnaire;
    }

    /**
     * Récupération des sous-types de soins
     * 
     * @return Iterator <RFSousTypeSoinsJointTypeSoinsStatistiques>
     */
    private static Iterator<RFSousTypeSoinsJointTypeSoinsStatistiques> getListeSousTypesSoins() throws Exception {

        RFSousTypeSoinsJointTypeSoinsStatistiquesManager rfSousTypeMgr = new RFSousTypeSoinsJointTypeSoinsStatistiquesManager();
        rfSousTypeMgr.setSession(RFXmlmlMappingStatistiquesParNbCas.getSession());
        rfSousTypeMgr.setForOrderBy(RFTypeDeSoin.FIELDNAME_CODE + "," + RFSousTypeDeSoin.FIELDNAME_CODE);
        rfSousTypeMgr.changeManagerSize(0);
        rfSousTypeMgr.find();

        return rfSousTypeMgr.iterator();
    }

    /**
     * Recherche de nombre de cas importés et non importé, par sous-type de soins et par dates
     * 
     * @param forSousTypeDeSoin
     * @param isImportation
     * @return
     * @throws Exception
     */
    private static BigDecimal getNbCasParSousTypesDeSoins(String forSousTypeDeSoin, boolean isImportation)
            throws Exception {

        RFDemandesJointSousTypeParNbCasManager rfDemandeJoiSouTypParNbCasMgr = new RFDemandesJointSousTypeParNbCasManager();

        rfDemandeJoiSouTypParNbCasMgr.setSession(RFXmlmlMappingStatistiquesParNbCas.getSession());
        rfDemandeJoiSouTypParNbCasMgr.setGestionnaire(RFXmlmlMappingStatistiquesParNbCas.gestionnaire);
        rfDemandeJoiSouTypParNbCasMgr.setForDateDebutStat(RFXmlmlMappingStatistiquesParNbCas.dateDebutStat);
        rfDemandeJoiSouTypParNbCasMgr.setForDateFinStat(RFXmlmlMappingStatistiquesParNbCas.dateFinStat);
        rfDemandeJoiSouTypParNbCasMgr.setForSousTypeDeSoin(forSousTypeDeSoin);
        rfDemandeJoiSouTypParNbCasMgr.setImportation(isImportation);
        rfDemandeJoiSouTypParNbCasMgr.changeManagerSize(0);
        rfDemandeJoiSouTypParNbCasMgr.find();

        RFDemandeJointSousTypeParNbCas demJoiSouTypParNbCas = (RFDemandeJointSousTypeParNbCas) rfDemandeJoiSouTypParNbCasMgr
                .getFirstEntity();

        if (null != demJoiSouTypParNbCas) {
            if (!JadeStringUtil.isEmpty(demJoiSouTypParNbCas.getNombreCas())) {
                return new BigDecimal(demJoiSouTypParNbCas.getNombreCas());
            } else {
                return new BigDecimal(0);
            }
        } else {
            return new BigDecimal(0);
            // throw new
            // Exception("Erreur dans la récupération du nombre de cas par sous-types de soins / RFXmlmlMappingStatistiquesParNbCas ");
        }
    }

    /**
     * Recherche de nombre de cas refusés par sous-type de soins et par dates
     * 
     * @param forSousTypeDeSoins
     * @return
     * @throws Exception
     */
    private static BigDecimal getNbCasRefusParSousTypeDeSoins(String forSousTypeDeSoins) throws Exception {

        RFDemandesRefusJointSousTypeParNbCasManager rfDemRefJoiSouTypParNbCasMgr = new RFDemandesRefusJointSousTypeParNbCasManager();

        rfDemRefJoiSouTypParNbCasMgr.setSession(RFXmlmlMappingStatistiquesParNbCas.getSession());
        rfDemRefJoiSouTypParNbCasMgr.setGestionnaire(RFXmlmlMappingStatistiquesParNbCas.gestionnaire);
        rfDemRefJoiSouTypParNbCasMgr.setForDateDebutStat(RFXmlmlMappingStatistiquesParNbCas.dateDebutStat);
        rfDemRefJoiSouTypParNbCasMgr.setForDateFinStat(RFXmlmlMappingStatistiquesParNbCas.dateFinStat);
        rfDemRefJoiSouTypParNbCasMgr.setForSousTypeDeSoin(forSousTypeDeSoins);
        rfDemRefJoiSouTypParNbCasMgr.changeManagerSize(0);
        rfDemRefJoiSouTypParNbCasMgr.find();

        RFDemandeRefusJointSousTypeParNbCas rfDemRefJoiSouTypSoiParNbCas = (RFDemandeRefusJointSousTypeParNbCas) rfDemRefJoiSouTypParNbCasMgr
                .getFirstEntity();

        if (null != rfDemRefJoiSouTypSoiParNbCas) {
            if (!JadeStringUtil.isEmpty(rfDemRefJoiSouTypSoiParNbCas.getNombreCas())) {
                return new BigDecimal(rfDemRefJoiSouTypSoiParNbCas.getNombreCas());
            } else {
                return new BigDecimal(0);
            }
        } else {
            return new BigDecimal(0);
            // throw new
            // Exception("Erreur dans la récupération du nombre de cas refusés par sous-types de soins / RFXmlmlMappingStatistiquesParNbCas ");
        }

    }

    /**
     * Methode pour obtenir le nombre de personne (par ménage) comprises dans les statistiques
     * 
     * @param isImportation
     * @return int
     * @throws Exception
     */
    private static int getNombrePersonne(boolean isImportation) throws Exception {
        int nbPersonne = 0;

        RFNombrePersonnesJointDemandesStatistiquesNbCasManager rfNbPers = new RFNombrePersonnesJointDemandesStatistiquesNbCasManager();
        rfNbPers.setSession(RFXmlmlMappingStatistiquesParNbCas.getSession());
        rfNbPers.setGestionnaire(RFXmlmlMappingStatistiquesParNbCas.gestionnaire);
        rfNbPers.setForDateDebutPeriodeStat(RFXmlmlMappingStatistiquesParNbCas.dateDebutStat);
        rfNbPers.setForDateFinPeriodeStat(RFXmlmlMappingStatistiquesParNbCas.dateFinStat);
        rfNbPers.setImportation(isImportation);
        rfNbPers.changeManagerSize(0);
        rfNbPers.find();

        nbPersonne = rfNbPers.getSize();

        return nbPersonne;
    }

    /**
     * Methode pour obtenir le nombre de personnes total comprises dans les statistiques
     * 
     * @return int
     * @throws Exception
     */
    private static int getNombrePersonneTotal() throws Exception {
        // Déclaration d'une liste pour y insérer les id dossier
        HashSet<String> listeIdDossier = new HashSet<String>();

        /** Gestion des idDossier présent dans la table des Demandes **/
        // Iteration sur la liste des id dossier présent dans les demandes
        Iterator listePersonneJointDemande = RFXmlmlMappingStatistiquesParNbCas.getNombrePersonneTotalJointDemande();
        // Parcours de chaque élément pour les insérer dans la liste
        while (listePersonneJointDemande.hasNext()) {
            RFNombrePersonnesTotalJointDemandesStatistiquesNbCas rfPerJoiDem = (RFNombrePersonnesTotalJointDemandesStatistiquesNbCas) listePersonneJointDemande
                    .next();
            if (rfPerJoiDem != null) {
                listeIdDossier.add(rfPerJoiDem.getIdDossier());
            }
        }

        /** Gestion des idDossier présent dans la table associative des Dossiers **/
        // Iteration sur la liste des id dossier présents à travers la QD
        Iterator listePersonneJointQd = RFXmlmlMappingStatistiquesParNbCas.getNombrePersonneTotalJointQd();
        // Parcours de chaque élément pour les insérer dans la liste
        while (listePersonneJointQd.hasNext()) {
            RFNombrePersonnesTotalJointQdStatistiquesNbCas rfPerJoiQd = (RFNombrePersonnesTotalJointQdStatistiquesNbCas) listePersonneJointQd
                    .next();
            if (rfPerJoiQd != null) {
                listeIdDossier.add(rfPerJoiQd.getIdDossier());
            }
        }

        return listeIdDossier.size();
    }

    /**
     * Methode permettant d'aller chercher tous les idDossier présent dans la table des demandes
     * 
     * @return Iterator <RFNombrePersonnesTotalJointDemandesStatistiquesNbCas>
     * @throws Exception
     */
    private static Iterator<RFNombrePersonnesTotalJointDemandesStatistiquesNbCas> getNombrePersonneTotalJointDemande()
            throws Exception {

        // Récupération des id dossiers présents dans les demandes
        RFNombrePersonnesTotalJointDemandesStatistiquesNbCasManager rfNbPerJoiDemMgr = new RFNombrePersonnesTotalJointDemandesStatistiquesNbCasManager();
        rfNbPerJoiDemMgr.setSession(RFXmlmlMappingStatistiquesParNbCas.getSession());
        rfNbPerJoiDemMgr.setGestionnaire(RFXmlmlMappingStatistiquesParNbCas.gestionnaire);
        rfNbPerJoiDemMgr.setForDateDebutPeriodeStat(RFXmlmlMappingStatistiquesParNbCas.dateDebutStat);
        rfNbPerJoiDemMgr.setForDateFinPeriodeStat(RFXmlmlMappingStatistiquesParNbCas.dateFinStat);
        rfNbPerJoiDemMgr.changeManagerSize(0);
        rfNbPerJoiDemMgr.find();

        return rfNbPerJoiDemMgr.iterator();
    }

    /**
     * Methode permettant d'aller chercher tous les idDossier de la table RFAQPD (table associative des dossier) lié aux
     * Qds et au Demandes
     * 
     * @return Iterator <RFNombrePersonnesTotalJointQdStatistiquesNbCas>
     * @throws Exception
     */
    private static Iterator<RFNombrePersonnesTotalJointQdStatistiquesNbCas> getNombrePersonneTotalJointQd()
            throws Exception {

        // Récupération des id dossiers étant liés à une demande à travers une Qd
        RFNombrePersonnesTotalJointQdStatistiquesNbCasManager rfNbPerJoiQdMgr = new RFNombrePersonnesTotalJointQdStatistiquesNbCasManager();
        rfNbPerJoiQdMgr.setSession(RFXmlmlMappingStatistiquesParNbCas.getSession());
        rfNbPerJoiQdMgr.setGestionnaire(RFXmlmlMappingStatistiquesParNbCas.gestionnaire);
        rfNbPerJoiQdMgr.setForDateDebutPeriodeStat(RFXmlmlMappingStatistiquesParNbCas.dateDebutStat);
        rfNbPerJoiQdMgr.setForDateFinPeriodeStat(RFXmlmlMappingStatistiquesParNbCas.dateFinStat);
        rfNbPerJoiQdMgr.changeManagerSize(0);
        rfNbPerJoiQdMgr.find();

        return rfNbPerJoiQdMgr.iterator();

    }

    /**
     * Methode pour obtenir le nombre de Qd's comprise dans les statistiques
     * 
     * @return int
     * @throws Exception
     */
    private static int getNombreQds() throws Exception {
        int nbQds = 0;

        RFNombreQdsJointDemandesStatistiquesParNbCasManager rfNbQds = new RFNombreQdsJointDemandesStatistiquesParNbCasManager();
        rfNbQds.setSession(RFXmlmlMappingStatistiquesParNbCas.getSession());
        rfNbQds.setGestionnaire(RFXmlmlMappingStatistiquesParNbCas.gestionnaire);
        rfNbQds.setForDateDebutPeriodeStat(RFXmlmlMappingStatistiquesParNbCas.dateDebutStat);
        rfNbQds.setForDateFinPeriodeStat(RFXmlmlMappingStatistiquesParNbCas.dateFinStat);
        rfNbQds.changeManagerSize(0);
        rfNbQds.find();

        nbQds = rfNbQds.getSize();

        return nbQds;
    }

    public static BSession getSession() {
        return RFXmlmlMappingStatistiquesParNbCas.session;
    }

    /**
     * Methode permettant de creer le corps du document (log ->new String[] { typeDeMessage, idAdaptationJournaliere,
     * idTiersBeneficiaire, nss, msgErreur, idDecisionPc, numDecisionPc })
     * 
     * @param container
     * @param entity
     * @param process
     * @throws RFXmlmlException
     * @throws Exception
     */
    private static void loadDetail(RFXmlmlContainer container, String code, String libelleSousType, String nbCas,
            String nbCasNonImport, String nbCasImport, String nbCasRefus) throws RFXmlmlException, Exception {

        container.put(IRFStatistiquesParNbCasListeColumns.C_CODE, code);
        container.put(IRFStatistiquesParNbCasListeColumns.C_TYPE_SOIN, libelleSousType);
        container.put(IRFStatistiquesParNbCasListeColumns.C_NB_CAS, nbCas);
        container.put(IRFStatistiquesParNbCasListeColumns.C_NB_CAS_NON_IMPORT, nbCasNonImport);
        container.put(IRFStatistiquesParNbCasListeColumns.C_NB_CAS_IMPORT, nbCasImport);
        container.put(IRFStatistiquesParNbCasListeColumns.C_NB_CAS_REFUS, nbCasRefus);

    }

    /**
     * Methode permettant de remplir le header du document
     * 
     * @param container
     * @param process
     */
    private static void loadHeader(RFXmlmlContainer container, RFStatistiquesParNbCasProcess process) throws Exception {

        // Utilisation de variables pour récupérer les textes dans les labels
        String titreDocument = RFXmlmlMappingStatistiquesParNbCas.session
                .getLabel("EXCEL_TITRE_DOCUMENT_STATS_PAR_NB_CAS");
        String typeAssurance = RFXmlmlMappingStatistiquesParNbCas.session
                .getLabel("EXCEL_TYPE_ASSURANCE_STATS_PAR_NB_CAS");
        String titrePeriode = RFXmlmlMappingStatistiquesParNbCas.session
                .getLabel("EXCEL_TEXTE_PERIODE_STATS_PAR_NB_CAS");
        String titreGestionnaire = RFXmlmlMappingStatistiquesParNbCas.session
                .getLabel("EXCEL_TEXTE_GESTIONNAIRE_STATS_PAR_NB_CAS");
        String titreCode = RFXmlmlMappingStatistiquesParNbCas.session
                .getLabel("EXCEL_TITRE_CODE_TABLEAU_STATS_PAR_NB_CAS");
        String titreSousTypesSoins = RFXmlmlMappingStatistiquesParNbCas.session
                .getLabel("EXCEL_TITRE_SOUS_TYPES_SOINS_TABLEAU_STATS_PAR_NB_CAS");
        String titreNbCas = RFXmlmlMappingStatistiquesParNbCas.session
                .getLabel("EXCEL_TITRE_NB_CAS_TABLEAU_STATS_PAR_NB_CAS");
        String titreNbCasNonImport = RFXmlmlMappingStatistiquesParNbCas.session
                .getLabel("EXCEL_TITRE_NB_CAS_NON_IMPORT_TABLEAU_STATS_PAR_NB_CAS");
        String titreNbCasImport = RFXmlmlMappingStatistiquesParNbCas.session
                .getLabel("EXCEL_TITRE_NB_CAS_IMPORT_TABLEAU_STATS_PAR_NB_CAS");
        String titreTotal = RFXmlmlMappingStatistiquesParNbCas.session.getLabel("EXCEL_TITRE_TOTAL_STATS_PAR_NB_CAS");
        String titreRecapitulatif = RFXmlmlMappingStatistiquesParNbCas.session
                .getLabel("EXCEL_TITRE_RECAPITULATIF_PAR_NB_CAS");
        String titreNbPersonne = RFXmlmlMappingStatistiquesParNbCas.session
                .getLabel("EXCEL_TITRE_NB_PERSONNE_PAR_NB_CAS");
        String titreNbPersonneNonImport = RFXmlmlMappingStatistiquesParNbCas.session
                .getLabel("EXCEL_TITRE_NB_PERSONNE_NON_IMPORT_PAR_NB_CAS");
        String titreNbPersonneImport = RFXmlmlMappingStatistiquesParNbCas.session
                .getLabel("EXCEL_TITRE_NB_PERSONNE_IMPORT_PAR_NB_CAS");
        String titreNbQd = RFXmlmlMappingStatistiquesParNbCas.session.getLabel("EXCEL_TITRE_NB_QD_PAR_NB_CAS");
        String titreNbCasRefus = RFXmlmlMappingStatistiquesParNbCas.session
                .getLabel("EXCEL_TITRE_NB_CAS_REFUS_TABLEAU_STAT_PAR_NB_CAS");

        // Insertion des infos en haut de page
        container.put(IRFStatistiquesParNbCasListeColumns.A_TITRE_DOCUMENT, titreDocument);
        container.put(IRFStatistiquesParNbCasListeColumns.A_TYPE_ASSURANCE, typeAssurance);
        container.put(IRFStatistiquesParNbCasListeColumns.A_DATE_DOCUMENT, JACalendar.todayJJsMMsAAAA());
        container.put(IRFStatistiquesParNbCasListeColumns.A_PERIODE, titrePeriode + " : "
                + RFXmlmlMappingStatistiquesParNbCas.dateDebutStat + " - "
                + RFXmlmlMappingStatistiquesParNbCas.dateFinStat);
        container.put(IRFStatistiquesParNbCasListeColumns.A_TITRE_GESTIONNAIRE, titreGestionnaire);

        if (!JadeStringUtil.isEmpty(RFXmlmlMappingStatistiquesParNbCas.gestionnaire)) {
            container.put(IRFStatistiquesParNbCasListeColumns.A_NOM_GESTIONNAIRE, RFXmlmlMappingStatistiquesParNbCas
                    .getDetailGestionnaire().toString());
        } else {
            container.put(IRFStatistiquesParNbCasListeColumns.A_NOM_GESTIONNAIRE,
                    RFXmlmlMappingStatistiquesParNbCas.session
                            .getLabel("EXCEL_TEXTE_TOUS_GESTIONNAIRE_STATS_PAR_NB_CAS"));
        }

        // Insertion des textes dans le tableau
        container.put(IRFStatistiquesParNbCasListeColumns.B_TITRE_CODE, titreCode);
        container.put(IRFStatistiquesParNbCasListeColumns.B_TITRE_SOUS_TYPES_SOINS, titreSousTypesSoins);
        container.put(IRFStatistiquesParNbCasListeColumns.B_TITRE_NB_CAS_TOTAL, titreNbCas);
        container.put(IRFStatistiquesParNbCasListeColumns.B_TITRE_NB_CAS_NON_IMPORT, titreNbCasNonImport);
        container.put(IRFStatistiquesParNbCasListeColumns.B_TITRE_NB_CAS_IMPORT, titreNbCasImport);
        container.put(IRFStatistiquesParNbCasListeColumns.B_TITRE_TOTAL, titreTotal);
        container.put(IRFStatistiquesParNbCasListeColumns.B_TITRE_RECAPITULATIF, titreRecapitulatif);
        container.put(IRFStatistiquesParNbCasListeColumns.B_TITRE_NB_PERSONNE, titreNbPersonne);
        container.put(IRFStatistiquesParNbCasListeColumns.B_TITRE_NB_PERSONNE_NON_IMPORT, titreNbPersonneNonImport);
        container.put(IRFStatistiquesParNbCasListeColumns.B_TITRE_NB_PERSONNE_IMPORT, titreNbPersonneImport);
        container.put(IRFStatistiquesParNbCasListeColumns.B_TITRE_NB_QD, titreNbQd);
        container.put(IRFStatistiquesParNbCasListeColumns.B_TITRE_NB_CAS_REFUS, titreNbCasRefus);

    }

    /**
     * Chargement des résultats
     * 
     * @param manager
     * @param process
     * @return container RFXmlmlContainer
     * @throws RFXmlmlException
     * @throws Exception
     */
    public static RFXmlmlContainer loadResults(RFStatistiquesParNbCasProcess process) throws RFXmlmlException,
            Exception {

        RFXmlmlContainer container = new RFXmlmlContainer();

        RFXmlmlMappingStatistiquesParNbCas.loadHeader(container, process);

        // Initialisation de variables pour récupérer les montants
        BigDecimal nbCasNonImport = new BigDecimal(0);
        BigDecimal nbCasImport = new BigDecimal(0);
        BigDecimal nbCasRefus = new BigDecimal(0);
        BigDecimal nbCas = new BigDecimal(0);
        BigDecimal nbCasTotalNonImport = new BigDecimal(0);
        BigDecimal nbCasTotalImport = new BigDecimal(0);
        BigDecimal nbCasTotalRefus = new BigDecimal(0);
        BigDecimal nbCasTotal = new BigDecimal(0);

        // Récupération des sous types de soins
        RFXmlmlMappingStatistiquesParNbCas.setSession(RFXmlmlMappingStatistiquesParNbCas.session);
        RFXmlmlMappingStatistiquesParNbCas.setDateDebutStat(RFXmlmlMappingStatistiquesParNbCas.dateDebutStat);
        RFXmlmlMappingStatistiquesParNbCas.setDateFinStat(RFXmlmlMappingStatistiquesParNbCas.dateFinStat);

        Iterator listeSousTypeSoinsIter = RFXmlmlMappingStatistiquesParNbCas.getListeSousTypesSoins();

        // Parcours de chaque sous type
        while (listeSousTypeSoinsIter.hasNext()) {

            RFSousTypeSoinsJointTypeSoinsStatistiques rfSouTypSoiJoiStat = (RFSousTypeSoinsJointTypeSoinsStatistiques) listeSousTypeSoinsIter
                    .next();

            if (null != rfSouTypSoiJoiStat) {

                // Récupération du nombre de cas NON IMPORTES (= false) par sous-type de soins
                nbCasNonImport = RFXmlmlMappingStatistiquesParNbCas.getNbCasParSousTypesDeSoins(
                        rfSouTypSoiJoiStat.getIdSousTypeSoin(), false);

                // Récupération du nombre de cas IMPORTES (= true) par sous-type de soins
                nbCasImport = RFXmlmlMappingStatistiquesParNbCas.getNbCasParSousTypesDeSoins(
                        rfSouTypSoiJoiStat.getIdSousTypeSoin(), true);

                // Récupération du nombre de cas REFUSES
                nbCasRefus = RFXmlmlMappingStatistiquesParNbCas.getNbCasRefusParSousTypeDeSoins(rfSouTypSoiJoiStat
                        .getIdSousTypeSoin());

                // Récupération et addition du nombre total de cas NON IMPORTES
                nbCasTotalNonImport = nbCasTotalNonImport.add(nbCasNonImport);

                // Récupération et addition du nombre total de cas IMPORTES
                nbCasTotalImport = nbCasTotalImport.add(nbCasImport);

                // Récupération et addition du nombre total de cas REFUS
                nbCasTotalRefus = nbCasTotalRefus.add(nbCasRefus);

                // Récupération et addition du nombre total de cas par sous-type de soins
                nbCas = nbCasNonImport.add(nbCasImport).add(nbCasRefus);

                // Récupération et addition du nombre total de cas
                nbCasTotal = nbCasTotalNonImport.add(nbCasTotalImport).add(nbCasTotalRefus);

                RFXmlmlMappingStatistiquesParNbCas.loadDetail(container, rfSouTypSoiJoiStat.getCode() + "."
                        + rfSouTypSoiJoiStat.getCodeSousType(), RFXmlmlMappingStatistiquesParNbCas.getSession()
                        .getCodeLibelle(rfSouTypSoiJoiStat.getIdSousTypeSoin()), nbCas.toString(), nbCasNonImport
                        .toString(), nbCasImport.toString(), nbCasRefus.toString());

            } else {
                throw new Exception("Erreur de chargement dans loadResult / RFXmlmlMappingStatistiquesParNbCas ");
            }
        }

        // Insertion des montants totaux
        container.put(IRFStatistiquesParNbCasListeColumns.B_TOTAL_NB_CAS, nbCasTotal.toString());
        container.put(IRFStatistiquesParNbCasListeColumns.B_TOTAL_NB_CAS_NON_IMPORT, nbCasTotalNonImport.toString());
        container.put(IRFStatistiquesParNbCasListeColumns.B_TOTAL_NB_CAS_IMPORT, nbCasTotalImport.toString());
        container.put(IRFStatistiquesParNbCasListeColumns.B_TOTAL_NB_CAS_REFUS, nbCasTotalRefus.toString());

        // Insertion des infos recapitulatives
        // ************************************
        // Insertion du nombre de personne total
        int nbPersonneTotal = RFXmlmlMappingStatistiquesParNbCas.getNombrePersonneTotal();
        container.put(IRFStatistiquesParNbCasListeColumns.C_RECAP_NB_PERSONNE, String.valueOf(nbPersonneTotal));

        // Insertion du nombre de personne non importés (=false)
        int nbPersonneNonImport = RFXmlmlMappingStatistiquesParNbCas.getNombrePersonne(false);
        container.put(IRFStatistiquesParNbCasListeColumns.C_RECAP_NB_PERSONNE_NON_IMPORT,
                String.valueOf(nbPersonneNonImport));

        // Insertion du nombre de personne importés (=true)
        int nbPersonneImport = RFXmlmlMappingStatistiquesParNbCas.getNombrePersonne(true);
        container.put(IRFStatistiquesParNbCasListeColumns.C_RECAP_NB_PERSONNE_IMPORT, String.valueOf(nbPersonneImport));

        // Insertion du nombre de QD's
        int nbQd = RFXmlmlMappingStatistiquesParNbCas.getNombreQds();
        container.put(IRFStatistiquesParNbCasListeColumns.C_RECAP_NB_QD, String.valueOf(nbQd));

        return container;
    }

    public static void setDateDebutStat(String dateDebutStat) {
        RFXmlmlMappingStatistiquesParNbCas.dateDebutStat = dateDebutStat;
    }

    public static void setDateFinStat(String dateFinStat) {
        RFXmlmlMappingStatistiquesParNbCas.dateFinStat = dateFinStat;
    }

    public static void setGestionnaire(String gestionnaire) {
        RFXmlmlMappingStatistiquesParNbCas.gestionnaire = gestionnaire;
    }

    public static void setSession(BSession session) {
        RFXmlmlMappingStatistiquesParNbCas.session = session;
    }

}
