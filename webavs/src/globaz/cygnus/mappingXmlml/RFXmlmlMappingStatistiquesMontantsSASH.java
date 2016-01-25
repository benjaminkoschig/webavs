/*
 * Créé le 16 janvier 2012
 */
package globaz.cygnus.mappingXmlml;

import globaz.cygnus.api.motifsRefus.IRFMotifsRefus;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemandeManager;
import globaz.cygnus.db.statistiques.RFDemandeJointSousTypeParMontantSash;
import globaz.cygnus.db.statistiques.RFDemandeJointSousTypeParMontantSashManager;
import globaz.cygnus.db.statistiques.RFNombrePersonnesJointDemandesStatistiquesSashManager;
import globaz.cygnus.db.statistiques.RFNombrePersonnesTotalJointDemandesStatistiquesMontantSash;
import globaz.cygnus.db.statistiques.RFNombrePersonnesTotalJointDemandesStatistiquesMontantSashManager;
import globaz.cygnus.db.statistiques.RFNombrePersonnesTotalJointQdStatistiquesMontantSash;
import globaz.cygnus.db.statistiques.RFNombrePersonnesTotalJointQdStatistiquesMontantSashManager;
import globaz.cygnus.db.statistiques.RFNombreQdsJointDemandesStatistiquesSashManager;
import globaz.cygnus.db.statistiques.RFSousTypeSoinsJointTypeSoinsStatistiques;
import globaz.cygnus.db.statistiques.RFSousTypeSoinsJointTypeSoinsStatistiquesManager;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.cygnus.exceptions.RFXmlmlException;
import globaz.cygnus.process.RFStatistiquesParMontantsSashProcess;
import globaz.cygnus.utils.RFUtils;
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
public class RFXmlmlMappingStatistiquesMontantsSASH {

    private static String dateDebutStat = null;
    private static String dateFinStat = null;
    private static String gestionnaire = null;
    private static String idMotifDeRefusDepassementGrandeQd = "";
    private static BSession session = null;

    public static String getDateDebutStat() {
        return RFXmlmlMappingStatistiquesMontantsSASH.dateDebutStat;
    }

    public static String getDateFinStat() {
        return RFXmlmlMappingStatistiquesMontantsSASH.dateFinStat;
    }

    public static String getDetailGestionnaire() throws Exception {
        String detailGestionnaire = PRGestionnaireHelper
                .getNomGestionnaire(RFXmlmlMappingStatistiquesMontantsSASH.gestionnaire);

        return detailGestionnaire;
    }

    public static String getGestionnaire() {
        return RFXmlmlMappingStatistiquesMontantsSASH.gestionnaire;
    }

    /**
     * Récupération des sous-types de soins
     * 
     * @return Iterator
     */
    private static Iterator<RFSousTypeSoinsJointTypeSoinsStatistiques> getListeSousTypesSoins() throws Exception {

        RFSousTypeSoinsJointTypeSoinsStatistiquesManager rfSousTypeMgr = new RFSousTypeSoinsJointTypeSoinsStatistiquesManager();
        rfSousTypeMgr.setSession(RFXmlmlMappingStatistiquesMontantsSASH.getSession());
        rfSousTypeMgr.setForOrderBy(RFTypeDeSoin.FIELDNAME_CODE + "," + RFSousTypeDeSoin.FIELDNAME_CODE);
        rfSousTypeMgr.changeManagerSize(0);
        rfSousTypeMgr.find();

        return rfSousTypeMgr.iterator();
    }

    private static BigDecimal getMontantDepassementQd(String idDemande) throws Exception {

        if (JadeStringUtil.isBlankOrZero(RFXmlmlMappingStatistiquesMontantsSASH.idMotifDeRefusDepassementGrandeQd)) {
            RFXmlmlMappingStatistiquesMontantsSASH.idMotifDeRefusDepassementGrandeQd = RFUtils
                    .getIdsMotifDeRefusSysteme(RFXmlmlMappingStatistiquesMontantsSASH.session, null).get(
                            IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE)[0];
        }

        RFAssMotifsRefusDemandeManager rfAssMotRefDemMgr = new RFAssMotifsRefusDemandeManager();
        rfAssMotRefDemMgr.setSession(RFXmlmlMappingStatistiquesMontantsSASH.getSession());
        rfAssMotRefDemMgr.setForIdDemande(idDemande);
        rfAssMotRefDemMgr
                .setForIdMotifDeRefus(RFXmlmlMappingStatistiquesMontantsSASH.idMotifDeRefusDepassementGrandeQd);
        rfAssMotRefDemMgr.changeManagerSize(0);
        rfAssMotRefDemMgr.find();

        if (rfAssMotRefDemMgr.size() == 1) {
            RFAssMotifsRefusDemande rfAssMotRefDem = (RFAssMotifsRefusDemande) rfAssMotRefDemMgr.getFirstEntity();
            if (null != rfAssMotRefDem) {
                return new BigDecimal(rfAssMotRefDem.getMntMotifsDeRefus());
            } else {
                return new BigDecimal(0);
            }
        }

        return new BigDecimal(0);
    }

    /**
     * Recherche de montants nets importés et non importé, par sous-type de soins et par dates
     * 
     * @return
     */
    private static BigDecimal getMontantParSousTypesDeSoins(String forSousTypeDeSoin, boolean isImportation)
            throws Exception {

        BigDecimal montantAPayerPlusMontantForceTotalBigDec = new BigDecimal(0);

        RFDemandeJointSousTypeParMontantSashManager rfDemandeParTypeMgr = new RFDemandeJointSousTypeParMontantSashManager();

        rfDemandeParTypeMgr.setSession(RFXmlmlMappingStatistiquesMontantsSASH.getSession());
        rfDemandeParTypeMgr.setForDateDebutStat(RFXmlmlMappingStatistiquesMontantsSASH.dateDebutStat);
        rfDemandeParTypeMgr.setForDateFinStat(RFXmlmlMappingStatistiquesMontantsSASH.dateFinStat);
        rfDemandeParTypeMgr.setGestionnaire(RFXmlmlMappingStatistiquesMontantsSASH.gestionnaire);
        rfDemandeParTypeMgr.setForSousTypeDeSoin(forSousTypeDeSoin);
        rfDemandeParTypeMgr.setImportation(isImportation);

        rfDemandeParTypeMgr.changeManagerSize(0);
        rfDemandeParTypeMgr.find();

        Iterator<RFDemandeJointSousTypeParMontantSash> rfDemandeParTypeItr = rfDemandeParTypeMgr.iterator();

        while (rfDemandeParTypeItr.hasNext()) {

            RFDemandeJointSousTypeParMontantSash demJoiSouTypParMonSas = rfDemandeParTypeItr.next();

            if (null != demJoiSouTypParMonSas) {

                if (!JadeStringUtil.isEmpty(demJoiSouTypParMonSas.getMontantAPayer())) {
                    montantAPayerPlusMontantForceTotalBigDec = montantAPayerPlusMontantForceTotalBigDec
                            .add(new BigDecimal(demJoiSouTypParMonSas.getMontantAPayer()));
                }

                if (demJoiSouTypParMonSas.getIsForcerPaiement()) {
                    montantAPayerPlusMontantForceTotalBigDec = montantAPayerPlusMontantForceTotalBigDec
                            .add(RFXmlmlMappingStatistiquesMontantsSASH.getMontantDepassementQd(demJoiSouTypParMonSas
                                    .getIdDemande()));
                }

            }
        }

        return montantAPayerPlusMontantForceTotalBigDec;

    }

    /**
     * Methode pour obtenir le nombre de personne (par ménage) comprises dans les statistiques
     * 
     * @param isImportation
     * @return
     * @throws Exception
     */
    private static int getNombrePersonne(boolean isImportation) throws Exception {
        int nbPersonne = 0;

        RFNombrePersonnesJointDemandesStatistiquesSashManager rfNbPers = new RFNombrePersonnesJointDemandesStatistiquesSashManager();
        rfNbPers.setSession(RFXmlmlMappingStatistiquesMontantsSASH.getSession());
        rfNbPers.setForDateDebutPeriodeStat(RFXmlmlMappingStatistiquesMontantsSASH.dateDebutStat);
        rfNbPers.setForDateFinPeriodeStat(RFXmlmlMappingStatistiquesMontantsSASH.dateFinStat);
        rfNbPers.setGestionnaire(RFXmlmlMappingStatistiquesMontantsSASH.gestionnaire);
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
        Iterator listePersonneJointDemande = RFXmlmlMappingStatistiquesMontantsSASH
                .getNombrePersonneTotalJointDemande();
        // Parcours de chaque élément pour les insérer dans la liste
        while (listePersonneJointDemande.hasNext()) {
            RFNombrePersonnesTotalJointDemandesStatistiquesMontantSash rfPerJoiDem = (RFNombrePersonnesTotalJointDemandesStatistiquesMontantSash) listePersonneJointDemande
                    .next();
            if (rfPerJoiDem != null) {
                listeIdDossier.add(rfPerJoiDem.getIdDossier());
            }
        }

        /** Gestion des idDossier présent dans la table associative des Dossiers **/
        // Iteration sur la liste des id dossier présents à travers la QD
        Iterator listePersonneJointQd = RFXmlmlMappingStatistiquesMontantsSASH.getNombrePersonneTotalJointQd();
        // Parcours de chaque élément pour les insérer dans la liste
        while (listePersonneJointQd.hasNext()) {
            RFNombrePersonnesTotalJointQdStatistiquesMontantSash rfPerJoiQd = (RFNombrePersonnesTotalJointQdStatistiquesMontantSash) listePersonneJointQd
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
    private static Iterator<RFNombrePersonnesTotalJointDemandesStatistiquesMontantSash> getNombrePersonneTotalJointDemande()
            throws Exception {

        // Récupération des id dossiers présents dans les demandes
        RFNombrePersonnesTotalJointDemandesStatistiquesMontantSashManager rfNbPerJoiDemMgr = new RFNombrePersonnesTotalJointDemandesStatistiquesMontantSashManager();
        rfNbPerJoiDemMgr.setSession(RFXmlmlMappingStatistiquesMontantsSASH.getSession());
        rfNbPerJoiDemMgr.setGestionnaire(RFXmlmlMappingStatistiquesMontantsSASH.gestionnaire);
        rfNbPerJoiDemMgr.setForDateDebutPeriodeStat(RFXmlmlMappingStatistiquesMontantsSASH.dateDebutStat);
        rfNbPerJoiDemMgr.setForDateFinPeriodeStat(RFXmlmlMappingStatistiquesMontantsSASH.dateFinStat);
        rfNbPerJoiDemMgr.setDemandeRefusee(true);
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
    private static Iterator<RFNombrePersonnesTotalJointQdStatistiquesMontantSash> getNombrePersonneTotalJointQd()
            throws Exception {

        // Récupération des id dossiers étant liés à une demande à travers une Qd
        RFNombrePersonnesTotalJointQdStatistiquesMontantSashManager rfNbPerJoiQdMgr = new RFNombrePersonnesTotalJointQdStatistiquesMontantSashManager();
        rfNbPerJoiQdMgr.setSession(RFXmlmlMappingStatistiquesMontantsSASH.getSession());
        rfNbPerJoiQdMgr.setGestionnaire(RFXmlmlMappingStatistiquesMontantsSASH.gestionnaire);
        rfNbPerJoiQdMgr.setForDateDebutPeriodeStat(RFXmlmlMappingStatistiquesMontantsSASH.dateDebutStat);
        rfNbPerJoiQdMgr.setForDateFinPeriodeStat(RFXmlmlMappingStatistiquesMontantsSASH.dateFinStat);
        rfNbPerJoiQdMgr.setDemandeRefusee(true);
        rfNbPerJoiQdMgr.changeManagerSize(0);
        rfNbPerJoiQdMgr.find();

        return rfNbPerJoiQdMgr.iterator();

    }

    /**
     * Methode pour obtenir le nombre de Qd's comprise dans les statistiques
     */
    private static int getNombreQds() throws Exception {
        int nbQds = 0;

        RFNombreQdsJointDemandesStatistiquesSashManager rfNbQds = new RFNombreQdsJointDemandesStatistiquesSashManager();
        rfNbQds.setSession(RFXmlmlMappingStatistiquesMontantsSASH.getSession());
        rfNbQds.setForDateDebutPeriodeStat(RFXmlmlMappingStatistiquesMontantsSASH.dateDebutStat);
        rfNbQds.setForDateFinPeriodeStat(RFXmlmlMappingStatistiquesMontantsSASH.dateFinStat);
        rfNbQds.setGestionnaire(RFXmlmlMappingStatistiquesMontantsSASH.gestionnaire);
        rfNbQds.setDemandeRefusee(true);
        rfNbQds.changeManagerSize(0);
        rfNbQds.find();

        nbQds = rfNbQds.getSize();

        return nbQds;
    }

    public static BSession getSession() {
        return RFXmlmlMappingStatistiquesMontantsSASH.session;
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
    private static void loadDetail(RFXmlmlContainer container, String code, String libelleSousType, String montantBrut,
            String montantNet, String montantNetImportation) throws RFXmlmlException, Exception {

        container.put(IRFStatistiquesMontantsSashListeColumns.C_CODE, code);
        container.put(IRFStatistiquesMontantsSashListeColumns.C_TYPE_SOIN, libelleSousType);
        container.put(IRFStatistiquesMontantsSashListeColumns.C_MONTANT_BRUT, montantBrut);
        container.put(IRFStatistiquesMontantsSashListeColumns.C_MONTANT_NET, montantNet);
        container.put(IRFStatistiquesMontantsSashListeColumns.C_MONTANT_NET_IMPORT, montantNetImportation);

    }

    /**
     * Methode permettant de remplir le header du document
     * 
     * @param container
     * @param process
     */
    private static void loadHeader(RFXmlmlContainer container, RFStatistiquesParMontantsSashProcess process)
            throws Exception {

        // Utilisation de variables pour récupérer les textes dans les labels
        String titreDocument = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TITRE_DOCUMENT_STATS_PAR_MONTANTS");
        String typeAssurance = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TYPE_ASSURANCE_STATS_PAR_MONTANTS");
        String titrePeriode = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TEXTE_PERIODE_STATS_PAR_MONTANTS");
        String titreGestionnaire = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TITRE_GESTIONNAIRE_STATS_PAR_MONTANTS");
        String titreCode = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TITRE_CODE_TABLEAU_STATS_PAR_MONTANTS");
        String titreSousTypesSoins = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TITRE_SOUS_TYPES_SOINS_TABLEAU_STATS_PAR_MONTANTS");
        String titreMontantsBrut = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TITRE_MONTANTS_BRUT_TABLEAU_STATS_PAR_MONTANTS");
        String titreMontantsNet = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TITRE_MONTANTS_NET_TABLEAU_STATS_PAR_MONTANTS");
        String titreMontantsNetImport = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TITRE_MONTANTS_NET_IMPORT_TABLEAU_STATS_PAR_MONTANTS");
        String titreTotal = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TITRE_TOTAL_STATS_PAR_MONTANTS");
        String titreRecapitulatif = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TITRE_RECAPITULATIF_STATS_PAR_MONTANTS");
        String titreNombrePersonne = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TITRE_RECAP_NOMBRE_PERSONNES_STATS_PAR_MONTANTS");
        String titreNombrePersonneImport = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TITRE_RECAP_NOMBRE_PERSONNES_IMPORT_STATS_PAR_MONTANTS");
        String titreNombrePersonneNonImport = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TITRE_NOMBRE_PERSONNE_NON_IMPORT_STATS_PAR_MONTANTS");
        String titreNombreQD = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TITRE_RECAP_NOMBRE_QD_STATS_PAR_MONTANTS");
        String titreMontantTotalNetNonImport = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TITRE_RECAP_MONTANT_NET_NON_IMPORT_STATS_PAR_MONTANTS");
        String titreMontantTotalNetImport = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TITRE_RECAP_MONTANT_NET_IMPORTE_STATS_PAR_MONTANTS");
        String titreMontantTotalBrut = RFXmlmlMappingStatistiquesMontantsSASH.session
                .getLabel("EXCEL_TITRE_RECAP_MONTANT_BRUT_STATS_PAR_MONTANTS");

        // Insertion des infos en haut de page
        container.put(IRFStatistiquesMontantsSashListeColumns.A_TITRE_DOCUMENT, titreDocument);
        container.put(IRFStatistiquesMontantsSashListeColumns.A_TYPE_ASSURANCE, typeAssurance);
        container.put(IRFStatistiquesMontantsSashListeColumns.A_DATE_DOCUMENT, JACalendar.todayJJsMMsAAAA());
        container.put(IRFStatistiquesMontantsSashListeColumns.A_PERIODE, titrePeriode + " : "
                + RFXmlmlMappingStatistiquesMontantsSASH.dateDebutStat + " - "
                + RFXmlmlMappingStatistiquesMontantsSASH.dateFinStat);
        container.put(IRFStatistiquesMontantsSashListeColumns.A_TITRE_GESTIONNAIRE, titreGestionnaire);
        // Affiche le nom du gestionnaire si celui-ci à été sélectionné
        if (!JadeStringUtil.isEmpty(RFXmlmlMappingStatistiquesMontantsSASH.gestionnaire)) {
            container.put(IRFStatistiquesMontantsSashListeColumns.A_NOM_GESTIONNAIRE,
                    RFXmlmlMappingStatistiquesMontantsSASH.getDetailGestionnaire());
        } else {
            // Sinon affiche : "Tous"
            container.put(IRFStatistiquesMontantsSashListeColumns.A_NOM_GESTIONNAIRE,
                    RFXmlmlMappingStatistiquesMontantsSASH.session
                            .getLabel("EXCEL_TEXTE_TOUS_GESTIONNAIRES_STATS_PAR_MONTANTS"));
        }

        // Insertion des textes dans le tableau
        container.put(IRFStatistiquesMontantsSashListeColumns.B_TITRE_CODE, titreCode);
        container.put(IRFStatistiquesMontantsSashListeColumns.B_TITRE_SOUS_TYPES_SOINS, titreSousTypesSoins);
        container.put(IRFStatistiquesMontantsSashListeColumns.B_TITRE_MONTANTS_BRUTS, titreMontantsBrut);
        container.put(IRFStatistiquesMontantsSashListeColumns.B_TITRE_MONTANTS_NETS, titreMontantsNet);
        container.put(IRFStatistiquesMontantsSashListeColumns.B_TITRE_MONTANTS_NETS_IMPORT, titreMontantsNetImport);
        container.put(IRFStatistiquesMontantsSashListeColumns.B_TITRE_TOTAL, titreTotal);
        container.put(IRFStatistiquesMontantsSashListeColumns.B_TITRE_RECAPITULATIF, titreRecapitulatif);
        container.put(IRFStatistiquesMontantsSashListeColumns.B_TITRE_NB_PERSONNE, titreNombrePersonne);
        container.put(IRFStatistiquesMontantsSashListeColumns.B_TITRE_NB_PERSONNE_IMPORT, titreNombrePersonneImport);
        container.put(IRFStatistiquesMontantsSashListeColumns.B_TITRE_NB_PERSONNE_NON_IMPORT,
                titreNombrePersonneNonImport);
        container.put(IRFStatistiquesMontantsSashListeColumns.B_TITRE_NB_QD, titreNombreQD);

    }

    /**
     * Chargement des résultats
     * 
     * @param manager
     * @param process
     * @return
     * @throws RFXmlmlException
     * @throws Exception
     */
    public static RFXmlmlContainer loadResults(RFStatistiquesParMontantsSashProcess process) throws RFXmlmlException,
            Exception {

        RFXmlmlMappingStatistiquesMontantsSASH.idMotifDeRefusDepassementGrandeQd = "";

        RFXmlmlContainer container = new RFXmlmlContainer();

        RFXmlmlMappingStatistiquesMontantsSASH.loadHeader(container, process);

        // Initialisation de variables pour récupérer les montants
        BigDecimal montantTotalNet = new BigDecimal(0);
        BigDecimal montantTotalNetImporte = new BigDecimal(0);
        BigDecimal montantTotalBrut = new BigDecimal(0);
        BigDecimal montantTotalFinalBrut = new BigDecimal(0);
        BigDecimal montantTotalFinalNetImporte = new BigDecimal(0);
        BigDecimal montantTotalFinalNet = new BigDecimal(0);

        // Récupération des sous types de soins
        RFXmlmlMappingStatistiquesMontantsSASH.setSession(RFXmlmlMappingStatistiquesMontantsSASH.session);
        RFXmlmlMappingStatistiquesMontantsSASH.setDateDebutStat(RFXmlmlMappingStatistiquesMontantsSASH.dateDebutStat);
        RFXmlmlMappingStatistiquesMontantsSASH.setDateFinStat(RFXmlmlMappingStatistiquesMontantsSASH.dateFinStat);

        Iterator<RFSousTypeSoinsJointTypeSoinsStatistiques> listeSousTypeSoinsIter = RFXmlmlMappingStatistiquesMontantsSASH
                .getListeSousTypesSoins();

        // Parcours de chaque sous type
        while (listeSousTypeSoinsIter.hasNext()) {

            RFSousTypeSoinsJointTypeSoinsStatistiques rfSouTypSoiJoiStat = listeSousTypeSoinsIter.next();

            if (null != rfSouTypSoiJoiStat) {

                // Récupération de l'ensemble des montants NON IMPORTES (= false) par sous-type de soin
                montantTotalNet = RFXmlmlMappingStatistiquesMontantsSASH.getMontantParSousTypesDeSoins(
                        rfSouTypSoiJoiStat.getIdSousTypeSoin(), false);
                // Récupération de l'ensemble des montants IMPORTES (= true) par sous-type de soin
                montantTotalNetImporte = RFXmlmlMappingStatistiquesMontantsSASH.getMontantParSousTypesDeSoins(
                        rfSouTypSoiJoiStat.getIdSousTypeSoin(), true);
                // Récupération de l'ensemble des montants par type de soins
                montantTotalBrut = montantTotalNet.add(montantTotalNetImporte);

                // Addition de l'emsemble des montants totaux provenant de tous les types de soins
                montantTotalFinalBrut = montantTotalFinalBrut.add(montantTotalBrut);
                montantTotalFinalNetImporte = montantTotalFinalNetImporte.add(montantTotalNetImporte);
                montantTotalFinalNet = montantTotalFinalNet.add(montantTotalNet);

                RFXmlmlMappingStatistiquesMontantsSASH.loadDetail(container, rfSouTypSoiJoiStat.getCode() + "."
                        + rfSouTypSoiJoiStat.getCodeSousType(), RFXmlmlMappingStatistiquesMontantsSASH.getSession()
                        .getCodeLibelle(rfSouTypSoiJoiStat.getIdSousTypeSoin()), montantTotalBrut.toString(),
                        montantTotalNet.toString(), montantTotalNetImporte.toString());

            } else {
                throw new Exception("Erreur de chargement dans loadResult / RFXmlmlMappingStatistiquesMontantsSASH ");
            }
        }

        // Insertion des montants totaux
        container.put(IRFStatistiquesMontantsSashListeColumns.C_TOTAL_MONTANTS_BRUTS, montantTotalFinalBrut.toString());
        container.put(IRFStatistiquesMontantsSashListeColumns.C_TOTAL_MONTANTS_NETS, montantTotalFinalNet.toString());
        container.put(IRFStatistiquesMontantsSashListeColumns.C_TOTAL_MONTANTS_NETS_IMPORT,
                montantTotalFinalNetImporte.toString());

        // Insertion des infos recapitulatives
        // ************************************
        // Insertion du nombre de personnes total
        int nbPersonneTotal = RFXmlmlMappingStatistiquesMontantsSASH.getNombrePersonneTotal();
        container.put(IRFStatistiquesMontantsSashListeColumns.C_NB_PERSONNE, String.valueOf(nbPersonneTotal));

        // Insertion du nombre de personnes non importés
        int nbrPersonneNonImportes = RFXmlmlMappingStatistiquesMontantsSASH.getNombrePersonne(false);
        container.put(IRFStatistiquesMontantsSashListeColumns.C_NB_PERSONNE_NON_IMPORT,
                String.valueOf(nbrPersonneNonImportes));

        // Insertion du nombre de personnes importés
        int nbrPersonneImportes = RFXmlmlMappingStatistiquesMontantsSASH.getNombrePersonne(true);
        container
                .put(IRFStatistiquesMontantsSashListeColumns.C_NB_PERSONNE_IMPORT, String.valueOf(nbrPersonneImportes));

        // Insertion du nombre de personnes total
        // int nbPersonneTotal = nbrPersonneNonImportes + nbrPersonneImportes;

        // Insertion du nombre de QDs
        int nbQdsTotal = RFXmlmlMappingStatistiquesMontantsSASH.getNombreQds();
        container.put(IRFStatistiquesMontantsSashListeColumns.C_NB_QD, String.valueOf(nbQdsTotal));

        return container;
    }

    public static void setDateDebutStat(String dateDebutStat) {
        RFXmlmlMappingStatistiquesMontantsSASH.dateDebutStat = dateDebutStat;
    }

    public static void setDateFinStat(String dateFinStat) {
        RFXmlmlMappingStatistiquesMontantsSASH.dateFinStat = dateFinStat;
    }

    public static void setGestionnaire(String gestionnaire) {
        RFXmlmlMappingStatistiquesMontantsSASH.gestionnaire = gestionnaire;
    }

    public static void setSession(BSession session) {
        RFXmlmlMappingStatistiquesMontantsSASH.session = session;
    }

}
