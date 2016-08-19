package globaz.pavo.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.commons.nss.NSUtil;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.inscriptions.DSDeclarationListeManager;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuelles;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuellesListeViewBean;
import globaz.draco.db.inscriptions.DSValideMontantDeclarationProcess;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAStringFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.releve.AFApercuReleve;
import globaz.naos.db.releve.AFApercuReleveManager;
import globaz.naos.db.releve.AFApercuReleveMontant;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.db.inscriptions.CIJournalManager;
import globaz.pavo.db.inscriptions.declaration.CIImportPucs4DetailResultBean;
import globaz.pavo.db.inscriptions.declaration.CIImportPucs4DetailResultInscriptionBean;
import globaz.pavo.db.inscriptions.declaration.CIImportPucs4ResumeBean;
import globaz.pavo.print.list.CIImportPucs4ResultList;
import globaz.pavo.service.ebusiness.CIEbusinessAccessInterface;
import globaz.pavo.util.CIUtil;
import globaz.webavs.common.CommonExcelmlContainer;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import ch.globaz.common.domaine.Montant;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.domaine.pucs.Employee;
import ch.globaz.orion.business.domaine.pucs.PeriodeSalary;
import ch.globaz.orion.business.domaine.pucs.SalaryAvs;
import ch.globaz.orion.business.domaine.pucs.SalaryCaf;
import com.google.common.base.Splitter;

/**
 * @author MMO 25.07.2016
 **/

public class CIImportPucs4Process extends BProcess {

    private static final long serialVersionUID = -314714724478084902L;

    CIImportPucs4DetailResultBean detailResultBean;
    private CIImportPucsFileProcess launcherImportPucsFileProcess = null;

    public TreeMap<String, Object> getTableJournaux() {
        return tableJournaux;
    }

    public DeclarationSalaire getDeclarationSalaire() {
        return declarationSalaire;
    }

    public void setDeclarationSalaire(DeclarationSalaire declarationSalaire) {
        this.declarationSalaire = declarationSalaire;
    }

    private DeclarationSalaire declarationSalaire;
    private CIApplication appCI;

    private String filename = "";
    private Map<String, Map<String, String>> mapAnneeMapTotalParCanton = new HashMap<String, Map<String, String>>();
    private String accepteLienDraco = "";

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private DSDeclarationViewBean declaration = null;
    private String nombreInscriptions = "";
    private DSValideMontantDeclarationProcess theCalculMasseProcess;
    private String dateReceptionForced;
    private String provenance = "";
    private String idsPucsFile = null;
    private String simulation = "";
    private TreeMap<String, String> hJournalExisteDeja = new TreeMap<String, String>();
    private String accepteAnneeEnCours = "";
    private String accepteEcrituresNegatives = "";
    private long totalAvertissement = 0;
    private long totalErreur = 0;
    private long totalTraite = 0;
    private boolean hasDifferenceAc = false;
    private boolean modeInscription = true;
    private boolean isErrorMontant = false;
    private boolean result = true;
    private String titreLog;
    private String numAffilieBase = "";
    private String Type = "";

    public String getDateReceptionForced() {
        return dateReceptionForced;
    }

    public void setDateReceptionForced(String dateReceptionForced) {
        this.dateReceptionForced = dateReceptionForced;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getNumAffilieBase() {
        return numAffilieBase;
    }

    public void setNumAffilieBase(String numAffilieBase) {
        this.numAffilieBase = numAffilieBase;
    }

    public DSDeclarationViewBean getDeclaration() {
        return declaration;
    }

    public void setDeclaration(DSDeclarationViewBean declaration) {
        this.declaration = declaration;
    }

    private boolean isErrorNbInscriptions = false;

    private String totalControle = "";

    private static final String VALUE_TO_SET_IN_RESUME_BEAN_NBR_INSCRIPTION_CI = "NBR_INSCRIPTION_CI";
    private static final String VALUE_TO_SET_IN_RESUME_BEAN_NBR_INSCRIPTION_ERREUR = "NBR_INSCRIPTION_ERREUR";
    private static final String VALUE_TO_SET_IN_RESUME_BEAN_NBR_INSCRIPTION_SUSPENS = "NBR_INSCRIPTION_SUSPENS";
    private static final String VALUE_TO_SET_IN_RESUME_BEAN_NBR_INSCRIPTION_NEGATIVE = "NBR_INSCRIPTION_NEGATIVE";
    private static final String VALUE_TO_SET_IN_RESUME_BEAN_NBR_INSCRIPTION_TRAITE = "NBR_INSCRIPTION_TRAITE";
    private static final String VALUE_TO_SET_IN_RESUME_BEAN_MONTANT_INSCRIPTION_CI = "MONTANT_INSCRIPTION_CI";
    private static final String VALUE_TO_SET_IN_RESUME_BEAN_MONTANT_INSCRIPTION_ERREUR = "MONTANT_INSCRIPTION_ERREUR";
    private static final String VALUE_TO_SET_IN_RESUME_BEAN_MONTANT_INSCRIPTION_SUSPENS = "MONTANT_INSCRIPTION_SUSPENS";
    private static final String VALUE_TO_SET_IN_RESUME_BEAN_MONTANT_INSCRIPTION_NEGATIVE = "MONTANT_INSCRIPTION_NEGATIVE";
    private static final String VALUE_TO_SET_IN_RESUME_BEAN_MONTANT_INSCRIPTION_TRAITE = "MONTANT_INSCRIPTION_TRAITE";

    private TreeMap<String, Object> hMontantInscriptionsCI = new TreeMap<String, Object>();
    private TreeMap<String, Object> hMontantInscriptionsErreur = new TreeMap<String, Object>();
    private TreeMap<String, Object> hMontantInscriptionsNegatives = new TreeMap<String, Object>();
    private TreeMap<String, Object> hMontantInscriptionsSuspens = new TreeMap<String, Object>();
    private TreeMap<String, Object> hMontantInscritionsTraites = new TreeMap<String, Object>();
    private TreeMap<String, Object> hMontantTotalControle = new TreeMap<String, Object>();
    private TreeMap<String, Object> hNbrInscriptionsCI = new TreeMap<String, Object>();
    private TreeMap<String, Object> hNbrInscriptionsErreur = new TreeMap<String, Object>();
    private TreeMap<String, Object> hNbrInscriptionsNegatives = new TreeMap<String, Object>();
    private TreeMap<String, Object> hNbrInscriptionsSuspens = new TreeMap<String, Object>();
    private TreeMap<String, Object> hNbrInscriptionsTotalControle = new TreeMap<String, Object>();
    private TreeMap<String, Object> hNbrInscriptionsTraites = new TreeMap<String, Object>();

    private static CIEbusinessAccessInterface ebusinessAccessInstance = null;

    // Cette table contient les journaux déjà crée
    // si le journal existait déjà avant le traitement, la clé est quand même mise dans cette table et la valeur sera
    // null.
    private final TreeMap<String, Object> tableJournaux = new TreeMap<String, Object>();

    public String getNombreInscriptions() {
        return nombreInscriptions;
    }

    public void setNombreInscriptions(String nombreInscriptions) {
        this.nombreInscriptions = nombreInscriptions;
    }

    public static void initEbusinessAccessInstance(CIEbusinessAccessInterface instance) {
        if (CIImportPucs4Process.ebusinessAccessInstance == null) {
            CIImportPucs4Process.ebusinessAccessInstance = instance;
        }
    }

    public String getTotalControle() {
        return totalControle;
    }

    public void setTotalControle(String totalControle) {
        this.totalControle = totalControle;
    }

    private void preparerDonnneeRapportExcelMl(DSInscriptionsIndividuellesListeViewBean declarationDraco) {
        CommonExcelmlContainer theContainerRapportExcelmlImportedPucsFile = null;
        try {
            // Préparation des données pour le rapport Excelml basé sur le modèle
            // (RapportImportedPucsFileModele.xml)
            theContainerRapportExcelmlImportedPucsFile = launcherImportPucsFileProcess
                    .getContainerRapportExcelmlImportedPucsFile();

            declarationDraco.retrieve(getTransaction());

            theContainerRapportExcelmlImportedPucsFile.put("COL_NO_AFFILIE", declarationDraco.getNumeroAffilie());
            theContainerRapportExcelmlImportedPucsFile.put("COL_NOM_AFFILIE", declarationDraco.getDesignation1());
            // info si swissDec
            if (DSDeclarationViewBean.PROVENANCE_SWISSDEC.equalsIgnoreCase(getProvenance())) {
                theContainerRapportExcelmlImportedPucsFile.put("COL_INFO_AFFILIE_TRANSMIS", declarationSalaire.getNom()
                        + "\n" + declarationSalaire.getAdresseStreet() + "\n" + declarationSalaire.getAdresseZipCode()
                        + " " + declarationSalaire.getAdresseCity());
                theContainerRapportExcelmlImportedPucsFile.put("COL_INFO_CONTACT_TRANSMIS",
                        declarationSalaire.getContactName() + "\n" + declarationSalaire.getContactMail() + "\n"
                                + declarationSalaire.getContactPhone());
            }
            theContainerRapportExcelmlImportedPucsFile.put("COL_DATE_RECEPTION", declarationDraco.getDateRetourEff());
            theContainerRapportExcelmlImportedPucsFile.put("COL_NOMBRE_INSCRIPTION",
                    JANumberFormatter.deQuote(String.valueOf(totalTraite)));
            theContainerRapportExcelmlImportedPucsFile.put("COL_ANNEE",
                    JANumberFormatter.deQuote(declarationDraco.getAnnee()));
            theContainerRapportExcelmlImportedPucsFile.put("COL_MASSE_AVS",
                    JANumberFormatter.deQuote(declarationDraco.getMasseSalTotal()));
            theContainerRapportExcelmlImportedPucsFile.put("COL_MONTANT_FACTURE",
                    JANumberFormatter.deQuote(declarationDraco.getMontantFacture().toString()));
            theContainerRapportExcelmlImportedPucsFile.put("COL_NOMBRE_ERREUR",
                    JANumberFormatter.deQuote(String.valueOf(totalErreur)));

            if (isOnError() || isAborted()) {
                launcherImportPucsFileProcess.setImportStatutAFile(CIImportPucsFileProcess.IMPORT_STATUT_KO);
            }
            theContainerRapportExcelmlImportedPucsFile.put("COL_STATUT",
                    launcherImportPucsFileProcess.getImportStatutAFile());

        } catch (Exception e) {
            String infoDeclaration = "";
            if (declarationDraco != null) {
                infoDeclaration = declarationDraco.getNumeroAffilie() + " - " + declarationDraco.getAnnee() + " - ";
            }
            launcherImportPucsFileProcess.getMemoryLog().logMessage(infoDeclaration + e.toString(),
                    FWMessage.INFORMATION, this.getClass().getName());
            launcherImportPucsFileProcess.setImportStatutAFile(CIImportPucsFileProcess.IMPORT_STATUT_KO);
        }
    }

    private void preparerDonnneeRapportExcelMlAFSeule(String annee, Map<String, String> sommeParCanton) {
        for (String mapKey : sommeParCanton.keySet()) {
            CommonExcelmlContainer theContainerRapportExcelmlImportedFileAFSeule = null;
            try {
                // Préparation des données pour le rapport Excelml basé sur le modèle
                // (RapportImportedSwissDecFileModeleAFSeule.xml)
                theContainerRapportExcelmlImportedFileAFSeule = launcherImportPucsFileProcess
                        .getContainerRapportExcelmlImportedAFSeule();

                theContainerRapportExcelmlImportedFileAFSeule.put("COL_NO_AFFILIE",
                        declarationSalaire.getNumeroAffilie());
                theContainerRapportExcelmlImportedFileAFSeule.put("COL_NOM_AFFILIE", declarationSalaire.getNom());
                theContainerRapportExcelmlImportedFileAFSeule.put("COL_INFO_AFFILIE_TRANSMIS",
                        declarationSalaire.getNom() + "\n" + declarationSalaire.getAdresseStreet() + "\n"
                                + declarationSalaire.getAdresseZipCode() + " " + declarationSalaire.getAdresseCity());
                theContainerRapportExcelmlImportedFileAFSeule.put("COL_INFO_CONTACT_TRANSMIS",
                        declarationSalaire.getContactName() + "\n" + declarationSalaire.getContactMail() + "\n"
                                + declarationSalaire.getContactPhone());
                theContainerRapportExcelmlImportedFileAFSeule.put("COL_ANNEE", JANumberFormatter.deQuote(annee));
                theContainerRapportExcelmlImportedFileAFSeule.put("COL_MASSE_AF",
                        JANumberFormatter.deQuote(sommeParCanton.get(mapKey)));
                theContainerRapportExcelmlImportedFileAFSeule.put("COL_CANTON", mapKey);
                theContainerRapportExcelmlImportedFileAFSeule.put("COL_DATE_RECEPTION", dateReceptionForced);
                if (isOnError() || isAborted() || totalErreur > 0) {
                    launcherImportPucsFileProcess.setImportStatutAFile(CIImportPucsFileProcess.IMPORT_STATUT_KO);
                }
                theContainerRapportExcelmlImportedFileAFSeule.put("COL_STATUT",
                        launcherImportPucsFileProcess.getImportStatutAFile());
            } catch (Exception e) {
                launcherImportPucsFileProcess.getMemoryLog().logMessage(e.toString(), FWMessage.INFORMATION,
                        this.getClass().getName());
                launcherImportPucsFileProcess.setImportStatutAFile(CIImportPucsFileProcess.IMPORT_STATUT_KO);
            }
        }
    }

    public String getAccepteLienDraco() {
        return accepteLienDraco;
    }

    public void setAccepteLienDraco(String accepteLienDraco) {
        this.accepteLienDraco = accepteLienDraco;
    }

    public String getSimulation() {
        return simulation;
    }

    public void setSimulation(String simulation) {
        this.simulation = simulation;
    }

    public String getAccepteAnneeEnCours() {
        return accepteAnneeEnCours;
    }

    public void setAccepteAnneeEnCours(String accepteAnneeEnCours) {
        this.accepteAnneeEnCours = accepteAnneeEnCours;
    }

    public String getAccepteEcrituresNegatives() {
        return accepteEcrituresNegatives;
    }

    public void setAccepteEcrituresNegatives(String accepteEcrituresNegatives) {
        this.accepteEcrituresNegatives = accepteEcrituresNegatives;
    }

    public boolean hasDifferenceAc() {
        return hasDifferenceAc;
    }

    public String getIdsPucsFile() {
        return idsPucsFile;
    }

    public void setIdsPucsFile(String idsPucsFile) {
        this.idsPucsFile = idsPucsFile;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public CIImportPucsFileProcess getLauncherImportPucsFileProcess() {
        return launcherImportPucsFileProcess;
    }

    public void setLauncherImportPucsFileProcess(CIImportPucsFileProcess launcherImportPucsFileProcess) {
        this.launcherImportPucsFileProcess = launcherImportPucsFileProcess;
    }

    public CIImportPucs4Process() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
        // Nothing to do

    }

    private void testPeriode(String annee, int moisDebut, int moisFin, String numeroAVS, CIEcriture ecriture,
            ArrayList<String> errors, String numeroAffilie) {

        // Plausi période
        try {

            if (((moisDebut < 1) || (moisDebut > 12)) && (99 != moisDebut) && (66 != moisDebut)) {
                errors.add(getSession().getLabel("DT_MOIS_DEBUT_INVALIDE"));

            } else {

                ecriture.setMoisDebut("" + moisDebut);

            }

            if ((moisFin < 1) || ((moisFin > 12) && (99 != moisFin) && (66 != moisFin))) {
                errors.add(getSession().getLabel("DT_MOIS_FIN_INVALIDE"));

            } else {

                ecriture.setMoisFin("" + moisFin);
            }
            if (moisDebut > moisFin) {
                errors.add(getSession().getLabel("DT_MOIS_DEBUT_PLUS_GRAND"));

            }
            if ((99 == moisDebut) && (99 == moisFin)) {
                if (!ecriture.getWrapperUtil().rechercheEcritureSemblablesDt(getTransaction(),
                        CIUtil.formatNumeroAffilie(getSession(), numeroAffilie), numeroAVS)) {
                    errors.add(getSession().getLabel("MSG_ECRITURE_99"));
                }
            }

            // année en cours et future sont interdites

            if (!"true".equalsIgnoreCase(accepteAnneeEnCours)) {
                if (Integer.parseInt(annee) >= JACalendar.today().getYear()) {
                    errors.add(getSession().getLabel("DT_ANNEE_TROP_GRANDE"));

                }
            } else {
                if (Integer.parseInt(annee) > JACalendar.today().getYear()) {
                    errors.add(getSession().getLabel("DT_ANNEE_TROP_GRANDE"));

                }
            }
        } catch (Exception ex) {
            errors.add(getSession().getLabel("DT_MOIS_INVALIDE"));

        }
    }

    public static void main(String[] args) {
        System.out.println(Integer.parseInt("01"));
    }

    private Montant initMontantTo0IfNull(Montant montant) {

        try {
            montant.getValue();
        } catch (NullPointerException e) {
            return new Montant(0);
        }

        return montant;

    }

    private void comparaisonDataControlDataCompute() {

        // ------------------------------------------------------------------------------
        // Comparaison avec somme de control (si renseigné )
        // ------------------------------------------------------------------------------
        if (!JadeStringUtil.isEmpty(totalControle)) {
            FWCurrency totalCalcule = new FWCurrency(0);
            Collection<Object> values = hMontantInscritionsTraites.values();
            Iterator<Object> it = values.iterator();
            while (it.hasNext()) {
                FWCurrency cur = (FWCurrency) it.next();
                totalCalcule.add(cur);
            }
            FWCurrency totalControleFormate = new FWCurrency(totalControle);
            if (!totalCalcule.toStringFormat().equals(totalControleFormate.toStringFormat())) {
                getMemoryLog().logMessage(getSession().getLabel("DT_LOG_TOTALE_COR_PAS"), FWMessage.ERREUR, titreLog);
                getMemoryLog().logMessage(
                        getSession().getLabel("DT_LOG_TOTALE_CTRL") + " : " + totalControleFormate.toStringFormat(),
                        FWMessage.INFORMATION, titreLog);
                getMemoryLog().logMessage(
                        getSession().getLabel("DT_LOG_TOTALE_CAL") + " : " + totalCalcule.toStringFormat(),
                        FWMessage.INFORMATION, titreLog);
                isErrorMontant = true;
                result = false;
            }
        } // fin total contrôle
          // ------------------------------------------------------------------------------
          // Comparaison avec nombre d'inscriptions (si renseigné )
          // ------------------------------------------------------------------------------
        if (!JadeStringUtil.isEmpty(nombreInscriptions)) {
            long nombreInscritionsCalcule = 0;
            Collection<Object> values = hNbrInscriptionsTraites.values();
            Iterator<Object> it = values.iterator();
            while (it.hasNext()) {
                long tmp = ((Long) it.next()).longValue();
                nombreInscritionsCalcule += tmp;
            }
            if (!(nombreInscritionsCalcule + "").equals(nombreInscriptions)) {
                getMemoryLog().logMessage(getSession().getLabel("DT_LOG_NB_INS_COR_PAS"), FWMessage.ERREUR, titreLog);
                getMemoryLog().logMessage(getSession().getLabel("DT_LOG_NB_INS") + " : " + nombreInscriptions,
                        FWMessage.INFORMATION, titreLog);
                getMemoryLog().logMessage(
                        getSession().getLabel("DT_LOG_NB_INS_FICH") + " : " + nombreInscritionsCalcule + "",
                        FWMessage.INFORMATION, titreLog);
                isErrorNbInscriptions = true;
                result = false;
            }
        } // fin nombre d'inscriptions
    }

    private void logAffilieNull(String numeroAffilie, String annee) {
        getMemoryLog()
                .logMessage(
                        numeroAffilie + "/" + annee + " : "
                                + getSession().getLabel("IMPORT_PUCS_4_AUCUNE_AFFILIATION_TROUVEE"), FWMessage.ERREUR,
                        this.getClass().getName());
    }

    private void createInscription(String numeroAVS, String nom, String prenom, PeriodeSalary periode,
            Montant montantAVS, Montant montantCAF, Montant montantAC, Montant montantAC2, String canton,
            boolean isEmployeWithCAF) throws Exception {

        if (CIDeclaration.CS_PUCS_II.equals(Type) || CIDeclaration.CS_PUCS_CCJU.equals(Type)) {
            if (JadeStringUtil.isBlank(numAffilieBase)) {
                numAffilieBase = CIUtil.formatNumeroAffilie(getSession(), declarationSalaire.getNumeroAffilie());
            }
        }

        montantAVS = initMontantTo0IfNull(montantAVS);
        montantCAF = initMontantTo0IfNull(montantCAF);
        montantAC = initMontantTo0IfNull(montantAC);
        montantAC2 = initMontantTo0IfNull(montantAC2);

        String nomPrenom = nom + "," + prenom;

        long nbrInscriptionsNegatives = 0;
        FWCurrency montantInscriptionsNegatives = new FWCurrency();

        long nbrInscriptionsCI = 0;
        FWCurrency montantInscriptionsCI = new FWCurrency();

        long nbrInscriptionsErreur = 0;
        FWCurrency montantInscriptionsErreur = new FWCurrency();

        long nbrInscriptionsSuspens = 0;
        FWCurrency montantInscriptionsSuspens = new FWCurrency();

        long nbrInscriptionsTraites = 0;
        FWCurrency montantInscritionsTraites = new FWCurrency();

        long nbrInscriptionsTotalControle = 0;
        FWCurrency montantTotalControle = new FWCurrency();

        ArrayList<String> info = new ArrayList<String>();
        ArrayList<String> errors = new ArrayList<String>();
        ArrayList<String> ciAdd = new ArrayList<String>();

        String annee = periode.getDateDebut().getAnnee();
        int moisDebut = Integer.parseInt(periode.getDateDebut().getMois());
        int moisFin = Integer.parseInt(periode.getDateFin().getMois());
        int jourDebut = Integer.parseInt(periode.getDateDebut().getJour());
        int jourFin = Integer.parseInt(periode.getDateFin().getJour());

        AFAffiliation affilie = appCI.getAffilieByNo(getSession(),
                CIUtil.formatNumeroAffilie(getSession(), declarationSalaire.getNumeroAffilie()), true, false, "", "",
                annee, "", "");

        if (affilie == null) {
            logAffilieNull(declarationSalaire.getNumeroAffilie(), annee);
            return;
        }

        String dateDebutAffiliation = null;
        String dateFinAffiliation = null;

        dateDebutAffiliation = affilie.getDateDebut();
        dateFinAffiliation = affilie.getDateFin();

        CIImportPucs4DetailResultInscriptionBean aDetailResultInscriptionBean = new CIImportPucs4DetailResultInscriptionBean();
        aDetailResultInscriptionBean.setNss(numeroAVS);
        aDetailResultInscriptionBean.setNom(nomPrenom);
        aDetailResultInscriptionBean.setMoisDebut(periode.getDateDebut().getMois());
        aDetailResultInscriptionBean.setMoisFin(periode.getDateFin().getMois());
        aDetailResultInscriptionBean.setAnnee(annee);

        aDetailResultInscriptionBean.setRevenuAVS(montantAVS.toStringFormat());

        aDetailResultInscriptionBean.setRevenuCAF(montantCAF.toStringFormat());

        numeroAVS = CIUtil.unFormatAVS(numeroAVS);

        if (declarationSalaire.isAfSeul()) {

            if (launcherImportPucsFileProcess != null) {
                launcherImportPucsFileProcess.setTraitementAFSeul(true);
            }

            if (affilie != null) {
                if (!JadeStringUtil.isBlankOrZero(canton)) {

                    Map<String, String> mapTotalParCanton = mapAnneeMapTotalParCanton.get(annee);
                    if (mapTotalParCanton == null) {
                        mapTotalParCanton = new HashMap<String, String>();
                    }
                    // Cumul par canton
                    if (mapTotalParCanton.containsKey(canton)) {
                        // Cumul du montant
                        FWCurrency cumul = new FWCurrency(mapTotalParCanton.get(canton));
                        cumul.add(montantCAF.getValue());
                        mapTotalParCanton.put(canton, cumul.toString());
                    } else {
                        mapTotalParCanton.put(canton, montantCAF.getValue());
                    }

                    mapAnneeMapTotalParCanton.put(annee, mapTotalParCanton);

                }

            } else {
                if (launcherImportPucsFileProcess != null) {
                    launcherImportPucsFileProcess.getMemoryLog()
                            .logMessage(
                                    getSession().getLabel("MSG_AFFILIE_NON_VALIDE") + " - Affilié  "
                                            + declarationSalaire.getNumeroAffilie() + " - Année " + annee,
                                    FWMessage.ERREUR, "");
                }

            }

        }

        CIEcriture ecriture = new CIEcriture();
        ecriture.setForAffilieParitaire(true);
        if (!"true".equalsIgnoreCase(getAccepteLienDraco())) {
            ecriture.setSession((BSession) getSessionCI(getSession()));
        } else {
            ecriture.setSession(getSession());
        }

        // Pour les déclarations de salaire:
        // trouver le journal à utiliser pour ce record.
        // il y a un journal par année/affilié.
        // si le journal n'existe pas on le crée et on le garde dans une table car
        // il peut être utilisé par plusieurs ligne du fichier.
        // si le journal existe préalablement au traitement du fichier, on génère une erreur.
        // findJournal retourne le journal à utiliser, ou null si le jounal à utilisé existait déjà
        // avant ce traitement,
        // ce qui n'est pas autorisé.
        // Pour les cot. pers: les inscriptions qui concernent l'année en cours vont dans le journal
        // de l'année en cours, les autres sont dans le même journal.
        CIJournal journal = null;
        String key = "";

        if (!declarationSalaire.isAfSeul()) {

            journal = _findJournal(annee, affilie.getTiersNom());

            key = _getKey(annee, affilie.getTiersNom());

        }

        if (journal == null && !declarationSalaire.isAfSeul()) {

            // Erreur, ce journal existe déjà avant ce traitement
            hJournalExisteDeja.put(key,
                    getSession().getLabel("DT_JOURNAL_NON_CREE") + " " + declarationSalaire.getNumeroAffilie() + "/"
                            + annee);
        } else {

            ecriture.setAnnee(annee);

            boolean breakTests = false;

            // Plausi période
            testPeriode(annee, moisDebut, moisFin, numeroAVS, ecriture, errors, declarationSalaire.getNumeroAffilie());

            // Plausi montant
            boolean montantPositif = montantAVS.isPositive() || montantAVS.isZero();

            String montantEcr = montantAVS.getValue();

            if (montantPositif) {
                montantEcr = testAndSetPourMontantPositif(ecriture, errors, montantEcr);
            } else {
                montantEcr = montantAVS.negate().getValue();
                nbrInscriptionsNegatives++;
                montantInscriptionsNegatives.sub(montantEcr);
                montantEcr = testAndSetPourMontantNegatif(ecriture, errors, montantEcr);
            }
            // Période affiliation
            if (!_isInPeriodeAffiliation(dateDebutAffiliation, dateFinAffiliation, annee, moisDebut, moisFin)) {
                // Les dates ne correspondent pas avec la période d'affiliation
                errors.add(getSession().getLabel("DT_ERR_DATE_AFFILIATION"));
            }
            // Plausi no avs
            String noAvs = testEndSetInfoPourNumAvs(numeroAVS, nomPrenom, ecriture, errors);

            ecriture.setNomPrenom(CIDeclaration.getNomFormatCI(nomPrenom));

            /************************************************************
             * Modif. 03.05.2006 Plus de plausi sur le nom, car on insère plus la virgule
             * automatiquement
             * */
            int anneeNaissance = determineAnneeNaissance(numeroAVS, ecriture);

            if (!JadeStringUtil.isBlankOrZero(montantAVS.getValue())
                    && !JadeStringUtil.isBlankOrZero(montantCAF.getValue()) && !declarationSalaire.isAfSeul()) {
                BigDecimal mntAf = new BigDecimal(montantCAF.getValue());
                BigDecimal mntAvs = new BigDecimal(montantAVS.getValue());
                if (mntAf.compareTo(mntAvs) != 0) {
                    errors.add(getSession().getLabel("DT_AF_DIFF_AVS"));
                }

            }

            //
            // !!! Attention test basé sur le numéro AVS pour calculé l'age
            //
            if ((anneeNaissance + 1918) > Integer.parseInt(annee)) {
                // test de l'age à partir du n°AVS
                errors.add(getSession().getLabel("DT_ERR_AGE_MIN"));
                breakTests = true;
            } else {
                if (CIDeclaration.isAvs0(noAvs)) {
                    ecriture.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE_SUSPENS);
                    info.add(getSession().getLabel("DT_AVS_0"));
                } else {
                    if (noAvs.length() < 11) {
                        // avs trop court
                        ecriture.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE_SUSPENS);
                        info.add(getSession().getLabel("DT_ERR_AVS_11"));
                    }
                }
                if (!ecriture.rechercheCI(getTransaction(), null, false, false) || getTransaction().hasErrors()) {
                    // erreur de création de CI
                    info.add(getSession().getLabel("DT_NUM_AVS_INVALIDE"));
                    breakTests = true;
                }
            }

            // test sur le total des inscriptions pour l'affiliation et l'année en cours
            if (declarationSalaire.isAfSeul()) {
                breakTests = true;
            } else if ("true".equalsIgnoreCase(accepteEcrituresNegatives) && !montantPositif) {
                if (CICompteIndividuel.CS_REGISTRE_PROVISOIRE.equals(ecriture.getCI(getTransaction(), false)
                        .getRegistre())) {
                    errors.add(getSession().getLabel("MSG_DT_INCONNU_ET_NEG"));
                } else {

                    BigDecimal totalPourAff = new BigDecimal("0");
                    try {
                        totalPourAff = ecriture.getWrapperUtil().rechercheEcritureEmpResult(getTransaction(),
                                CIUtil.formatNumeroAffilie(getSession(), declarationSalaire.getNumeroAffilie()));
                        totalPourAff = totalPourAff.subtract(new BigDecimal(montantEcr.trim()));
                    } catch (Exception e) {
                        totalPourAff = new BigDecimal("0");

                    }
                    int res = totalPourAff.compareTo(new BigDecimal("0"));
                    if (res < 0) {
                        errors.add(getSession().getLabel("MSG_ECRITURE_SUMEMP"));
                    }
                }
            }

            if (!breakTests && !JadeStringUtil.isBlankOrZero(montantAVS.getValue())) {
                if (CICompteIndividuel.CS_REGISTRE_PROVISOIRE.equals(ecriture.getCI(getTransaction(), false)
                        .getRegistre())) {
                    // CI Provisoire
                    traitementSiRegistreProvisoire(annee, moisDebut, moisFin, ecriture, errors, info,
                            declarationSalaire.getNumeroAffilie(), noAvs);
                } else {
                    // ci ok, recherche des écritures identiques
                    CIEcritureManager ecrMgr = new CIEcritureManager();
                    ecrMgr.setSession(getSession());
                    ecrMgr.setForAnnee(annee);
                    ecrMgr.setForCompteIndividuelId(ecriture.getCI(getTransaction(), false).getCompteIndividuelId());
                    ecrMgr.setForAffilie(CIUtil.formatNumeroAffilie(getSession(), declarationSalaire.getNumeroAffilie()));
                    ecrMgr.find(getTransaction(), BManager.SIZE_NOLIMIT);
                    for (int i = 0; i < ecrMgr.size(); i++) {
                        CIEcriture ecr = (CIEcriture) ecrMgr.getEntity(i);
                        if (CIEcriture.CS_CODE_PROVISOIRE.equals(ecr.getCode())) {
                            // erreur: écriture provisoire déjà présente
                            errors.add(getSession().getLabel("DT_INSCR_PROV"));
                            break;
                        }
                        if ((ecr.getMoisDebut().equals(moisDebut + ""))
                                && (ecr.getMoisFin().equals(moisFin + ""))
                                && ("01".equals(ecr.getGreFormat()) || "11".equals(ecr.getGreFormat())
                                        || "07".equals(ecr.getGreFormat()) || "17".equals(ecr.getGreFormat()))
                                && ((ecr.getMontant().substring(0, ecr.getMontant().length() - 3)).equals(ecriture
                                        .getMontant().substring(0, ecriture.getMontant().length() - 3)))) {

                            if ((JadeStringUtil.isEmpty(ecriture.getExtourne()) && "0".equals(ecr.getExtourne()))
                                    || ecr.getExtourne().equals(ecriture.getExtourne())) {

                                // erreur: écriture identique
                                errors.add(getSession().getLabel("DT_INSCR_IDENTIQUE"));

                                break;
                            }

                        }
                    }
                    // au CI, tester clôture
                    String clo = ecriture.getCI(getTransaction(), false).getDerniereCloture(true);
                    if (ecriture.aCloturer(new JADate(clo))) {
                        // écriture avant clôture
                        ciAdd.add(getSession().getLabel("DT_CI_ADDITIONEL"));
                        if ((errors.size() == 0) && (info.size() == 0)) {
                            nbrInscriptionsCI++;

                            if (montantPositif) {
                                montantInscriptionsCI.add(montantEcr);
                            } else {
                                montantInscriptionsCI.sub(montantEcr);
                            }
                        }
                    } else {
                        if (!ecriture.getCI(getTransaction(), false).isCiOuvert().booleanValue()) {
                            if (ecriture.isPeriodeDeCotisationACheval(getTransaction(), new JADate(clo))) {
                                errors.add(getSession().getLabel("MSG_IK_PERIODE_A_CHEVAL"));
                            } else {
                                info.add(getSession().getLabel("DT_ECR_APRES_CLOTURE"));
                            }
                        } else {
                            if (ecriture.isPeriodeDeCotisationACheval(getTransaction(), new JADate(clo))) {
                                errors.add(getSession().getLabel("MSG_IK_PERIODE_A_CHEVAL"));
                            }
                            if ((errors.size() == 0) && (info.size() == 0)) {
                                nbrInscriptionsCI++;
                                if (montantPositif) {
                                    montantInscriptionsCI.add(montantEcr);
                                } else {
                                    montantInscriptionsCI.sub(montantEcr);
                                }
                            }
                        }
                    }
                }
            }

            if (errors.size() != 0) {
                // si il y a eu des erreurs
                nbrInscriptionsErreur++;
                if (montantPositif) {
                    montantInscriptionsErreur.add(montantEcr);
                } else {
                    montantInscriptionsErreur.sub(montantEcr);
                }
                // result = false;
            } else {
                // pas d'erreur
                if (info.size() != 0) {
                    nbrInscriptionsSuspens++;
                    if (montantPositif) {
                        montantInscriptionsSuspens.add(montantEcr);
                    } else {
                        montantInscriptionsSuspens.sub(montantEcr);
                    }
                }
                // -------------------------------------------------------------------------------
                // Ajout écriture et mis a jour du journal
                // -------------------------------------------------------------------------------
                if (modeInscription && !declarationSalaire.isAfSeul()) {
                    // journal trouvé
                    ecriture.setIdJournal(journal.getIdJournal());
                    // Si on est en mode linkDraco, on ne passe pas par l'écriture, mais par
                    // l'inscription DRACO
                    if ("true".equalsIgnoreCase(accepteLienDraco)) {
                        DSInscriptionsIndividuelles insc = new DSInscriptionsIndividuelles();
                        // recherche de la déclaration en question
                        DSDeclarationListeManager decMgr = new DSDeclarationListeManager();
                        decMgr.setForIdJournal(journal.getIdJournal());
                        decMgr.setSession((BSession) getSessionDS(getSession()));
                        decMgr.wantCallMethodAfter(false);
                        decMgr.find(getTransaction(), BManager.SIZE_NOLIMIT);
                        if (decMgr.size() > 0) {
                            DSInscriptionsIndividuellesListeViewBean declarationDraco = (DSInscriptionsIndividuellesListeViewBean) decMgr
                                    .getFirstEntity();
                            insc.setDeclaration(declarationDraco);
                            insc.setSession((BSession) getSessionDS(getSession()));
                            insc.setIdDeclaration(declarationDraco.getIdDeclaration());
                            insc.setGenreEcriture(ecriture.getGre());
                            if (!JadeStringUtil.isIntegerEmpty(String.valueOf(jourDebut))) {
                                insc.setPeriodeDebut(jourDebut + "." + moisDebut);
                            } else {
                                insc.setPeriodeDebut(ecriture.getMoisDebutPad());
                            }
                            if (!JadeStringUtil.isIntegerEmpty(String.valueOf(jourFin))) {
                                insc.setPeriodeFin(jourFin + "." + moisFin);
                            } else {
                                insc.setPeriodeFin(ecriture.getMoisFinPad());
                            }
                            if (!JadeStringUtil.isIntegerEmpty(canton)) {
                                try {
                                    String codeCanton = CIUtil.codeUtilisateurToCodeSysteme(getTransaction(), canton,
                                            "PYCANTON", getSession());
                                    insc.setCodeCanton(codeCanton);
                                } catch (Exception e) {
                                }
                            } else {
                                String codeCanton = AFAffiliationUtil.getCantonAFForDS(affilie,
                                        JACalendar.todayJJsMMsAAAA());
                                insc.setCodeCanton(codeCanton);

                            }
                            insc.setFromPucs(true);
                            insc.setMontant(JANumberFormatter.deQuote(ecriture.getMontant()));
                            insc.setNumeroAvs(CIUtil.unFormatAVS(ecriture.getAvs()));
                            insc.setNomPrenom(ecriture.getNomPrenom());
                            insc.setAnneeInsc(ecriture.getAnnee());

                            if (!JadeStringUtil.isBlankOrZero(montantCAF.getValue())) {
                                insc.setMontantAf(JANumberFormatter.deQuote(montantCAF.getValue()));
                            }

                            if (isEmployeWithCAF) {
                                insc.setMontantCAFToSetWithMontantAVS(false);
                            }

                            if (!JadeStringUtil.isBlankOrZero(montantCAF.getValue())
                                    && JadeStringUtil.isBlankOrZero(montantAVS.getValue())) {
                                insc.setSoumis(false);
                            }

                            insc.add(getTransaction());

                            boolean differenceAc = false;
                            if (!DeclarationSalaireProvenance.fromValueWithOutException(provenance).isDan()
                                    && !JadeStringUtil.isBlank(montantAC.getValue())
                                    && !JadeStringUtil.isBlankOrZero(montantAVS.getValue())) {
                                BigDecimal montantCommunique = new BigDecimal(montantAC.getValue());
                                BigDecimal montantEcriture = null;
                                if (!JadeStringUtil.isBlank(insc.getACI())) {
                                    montantEcriture = new BigDecimal(insc.getACI());
                                } else {
                                    montantEcriture = new BigDecimal("0");
                                }
                                if (montantCommunique.compareTo(montantEcriture) != 0) {
                                    info.add(getSession().getLabel("MSG_MONTANT_AC") + " "
                                            + new FWCurrency(montantCommunique.toString()).toStringFormat() + " / "
                                            + new FWCurrency(montantEcriture.toString()).toStringFormat());
                                    if ((nbrInscriptionsSuspens == 0) && (nbrInscriptionsSuspens == 0)) {
                                        totalAvertissement = totalAvertissement + 1;
                                    }
                                    differenceAc = true;
                                    hasDifferenceAc = true;
                                }

                            }
                            if (!DeclarationSalaireProvenance.fromValueWithOutException(provenance).isDan()
                                    && !JadeStringUtil.isBlank(montantAC2.getValue())
                                    && !JadeStringUtil.isBlankOrZero(montantAVS.getValue())) {
                                BigDecimal montantCommunique = new BigDecimal(montantAC2.getValue());
                                BigDecimal montantEcriture = null;
                                if (!JadeStringUtil.isBlank(insc.getACII())) {
                                    montantEcriture = new BigDecimal(insc.getACII());
                                } else {
                                    montantEcriture = new BigDecimal("0");
                                }
                                if (montantCommunique.compareTo(montantEcriture) != 0) {
                                    info.add(getSession().getLabel("MSG_MONTANT_AC2") + " "
                                            + new FWCurrency(montantCommunique.toString()).toStringFormat() + " / "
                                            + new FWCurrency(montantEcriture.toString()).toStringFormat());
                                    if ((nbrInscriptionsSuspens == 0) && (nbrInscriptionsSuspens == 0)) {
                                        totalAvertissement = totalAvertissement + 1;
                                    }
                                    differenceAc = true;
                                    hasDifferenceAc = true;
                                }

                            }
                            // Si différence AC ou AC2 pour SwissDec, il faut remettre les montants
                            // communiqués via SwissDec
                            // car le montant ne doit pas être recalculé (période fausse)
                            if (differenceAc && DSDeclarationViewBean.PROVENANCE_SWISSDEC.equals(provenance)
                                    && !insc.isNew() && !insc.hasErrors()) {
                                insc.setACI(montantAC.getValue());
                                insc.setACII(montantAC2.getValue());
                                insc.wantCallValidate(false);
                                insc.update(getTransaction());
                            }

                            if (CIUtil.isRetraite(ecriture.getAvs(), Integer.parseInt(ecriture.getAnnee()),
                                    ecriture.getAvsNNSS(), getSession())) {
                                info.add(getSession().getLabel("MSG_PUCS_ASSURE_RETRAITE"));
                            }
                        } else {
                            getTransaction().addErrors(getSession().getLabel("DECL_NON_EXISTANTE"));
                        }
                    } else {
                        ecriture.setNoSumNeeded(true);
                        // Modif. mettre le kbbatt à 2 pour concordance nnss
                        ecriture.setWantForDeclaration(new Boolean(false));

                        if (!JadeStringUtil.isBlankOrZero(montantAVS.getValue())) {
                            ecriture.add(getTransaction());
                        }

                    }
                    getTransaction().disableSpy();
                    // journal.updateInscription(getTransaction());
                    // getTransaction().enableSpy();
                    if (!getTransaction().hasErrors()) {
                        getTransaction().commit();
                    }
                }
            }

            aDetailResultInscriptionBean.setGenre(ecriture.getGre());
            aDetailResultInscriptionBean.setCiAdd(ciAdd);
            aDetailResultInscriptionBean.setErrors(errors);
            aDetailResultInscriptionBean.setInfos(info);

            List<CIImportPucs4DetailResultInscriptionBean> listInscriptionsBean = detailResultBean
                    .getMapAnneeListInscriptions().get(annee);
            if (listInscriptionsBean == null) {
                listInscriptionsBean = new ArrayList<CIImportPucs4DetailResultInscriptionBean>();
            }
            listInscriptionsBean.add(aDetailResultInscriptionBean);
            detailResultBean.getMapAnneeListInscriptions().put(annee, listInscriptionsBean);

            // tableLogAssure.put(_getFullKey(rec, line), rec);
            if (getTransaction().hasErrors()) {
                if (declarationSalaire.isAfSeul()) {
                    getTransaction().rollback();
                }
                errors.add(getTransaction().getErrors().toString());
                nbrInscriptionsErreur++;
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), titreLog);
                getMemoryLog().logMessage(ecriture.getNomPrenom(), FWMessage.ERREUR, titreLog);
                getTransaction().clearErrorBuffer();
            }
            nbrInscriptionsTraites++;
            if (montantPositif) {
                montantInscritionsTraites.add(montantEcr);
            } else {
                montantInscritionsTraites.sub(montantEcr);
            }
            if (!modeInscription) {

                getTransaction().rollback();
            }

            // -------------------------------------------------------------------------------
            // met a jour les TreeMaps pour les summaries
            // -------------------------------------------------------------------------------
            nbrInscriptionsTotalControle = nbrInscriptionsCI + nbrInscriptionsSuspens;
            montantTotalControle.add(montantInscriptionsCI);
            montantTotalControle.add(montantInscriptionsSuspens);

            if (!JadeStringUtil.isBlankOrZero(montantAVS.getValue()) || declarationSalaire.isAfSeul()) {
                _updateSummary(hNbrInscriptionsTraites, hMontantInscritionsTraites, hNbrInscriptionsErreur,
                        hMontantInscriptionsErreur, hNbrInscriptionsSuspens, hMontantInscriptionsSuspens,
                        hNbrInscriptionsCI, hMontantInscriptionsCI, hNbrInscriptionsNegatives,
                        hMontantInscriptionsNegatives, nbrInscriptionsTraites, montantInscritionsTraites,
                        nbrInscriptionsErreur, montantInscriptionsErreur, nbrInscriptionsSuspens,
                        montantInscriptionsSuspens, nbrInscriptionsCI, montantInscriptionsCI, nbrInscriptionsNegatives,
                        montantInscriptionsNegatives, hNbrInscriptionsTotalControle, nbrInscriptionsTotalControle,
                        hMontantTotalControle, montantTotalControle, key);
            }

        }

    }

    private void _doLogJournauxExistant(TreeMap<String, String> hJournalExisteDeja) {
        Set<String> journalSet = hJournalExisteDeja.keySet();
        Iterator<String> it = journalSet.iterator();
        while (it.hasNext()) {
            String str = it.next();
            getMemoryLog().logMessage(hJournalExisteDeja.get(str), FWMessage.ERREUR, "Déclaration");
        }
    }

    private void importeInscriptionsProvisoires(DSInscriptionsIndividuellesListeViewBean declaration) throws Exception {
        if (JadeStringUtil.isBlankOrZero(declaration.getAffiliationId())) {
            return;
        }
        CIEcritureManager mgrEcrCI = new CIEcritureManager();
        mgrEcrCI.setSession(getSession());
        mgrEcrCI.setForAffilie(declaration.getNumeroAffilie());
        mgrEcrCI.setForAnnee(declaration.getAnnee());
        mgrEcrCI.setForCode(CIEcriture.CS_CODE_PROVISOIRE);
        mgrEcrCI.find(getTransaction(), BManager.SIZE_NOLIMIT);

        for (int i = 0; i < mgrEcrCI.size(); i++) {
            CIEcriture ecrCI = (CIEcriture) mgrEcrCI.getEntity(i);
            DSInscriptionsIndividuelles insc = new DSInscriptionsIndividuelles();
            insc.setSession(getSession());
            insc.setIdEcrtirueCI(ecrCI.getEcritureId());
            insc.setCompteIndividuelId(ecrCI.getCompteIndividuelId());
            insc.setMontant(ecrCI.getMontant());
            insc.setIdDeclaration(declaration.getIdDeclaration());
            insc.setMontantAf(ecrCI.getMontantSigne());
            // insc.setNotWantCI(true);
            insc.setDeclaration(declaration);
            insc.setNumeroAvs(ecrCI.getAvs());
            insc.setCategoriePerso(ecrCI.getCategoriePersonnel());
            insc.setAnneeInsc(ecrCI.getAnnee());
            insc.setProvisoire(true);
            try {
                insc.add(getTransaction());
                getMemoryLog().logMessage(
                        "Le numéro : " + JAStringFormatter.formatAVS(ecrCI.getAvs())
                                + " a été réaffecté à la déclaration", FWMessage.INFORMATION,
                        "Pré-remplissage de la déclaration");
                if (!getTransaction().hasErrors()) {
                    declaration.retrieve(getTransaction());
                    declaration.setIdJournal(insc.donneIdJournal(getTransaction()));
                    declaration.wantCallMethodAfter(false);
                    declaration.wantCallMethodBefore(false);
                    declaration.update(getTransaction());
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            } else {
                getTransaction().rollback();
            }
        }

    }

    private void creationReleve(String annee, AFAffiliation affilie, Map<String, String> sommeParCanton)
            throws Exception {

        String typeReleve = CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA;

        // Détermination du type (Si relevé déjà existant => complément sinon final)
        // Il faut tester dans les relevés et en compta
        AFApercuReleveManager manager = new AFApercuReleveManager();
        manager.setSession(getSession());
        manager.setForIdTiers(affilie.getIdTiers());
        manager.setForAffilieNumero(affilie.getAffilieNumero());
        manager.setFromDateDebut("01.01." + annee);
        manager.setUntilDateFin("31.12." + annee);
        manager.find(BManager.SIZE_NOLIMIT);
        for (int i = 0; i < manager.size(); i++) {
            AFApercuReleve releve = (AFApercuReleve) manager.getEntity(i);
            // Détermination du type (Si relevé déjà existant => complément sinon final)
            if (CodeSystem.TYPE_RELEVE_DECOMP_FINAL.equalsIgnoreCase(releve.getType())
                    || CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equalsIgnoreCase(releve.getType())) {
                typeReleve = CodeSystem.TYPE_RELEVE_RECTIF;
            }
            // Si relevé en cours de facturation pour l'année => erreur
            if (CodeSystem.ETATS_RELEVE_SAISIE.equalsIgnoreCase(releve.getEtat())
                    || CodeSystem.ETATS_RELEVE_FACTURER.equalsIgnoreCase(releve.getEtat())) {

                _addError(getTransaction(), getSession().getLabel("AFSEUL_RELEVE_EXISTANT") + " - "
                        + getSession().getLabel("DEC_AFFILIE") + " " + affilie.getAffilieNumero() + " - "
                        + getSession().getLabel("DEC_ANNEE") + " " + annee);
                totalErreur++;
                break;
            }
        }
        if (totalErreur == 0) {
            // Vérifier en compta s'il n'existe pas déjà un décompte => cas du traitement manuel
            if (CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equalsIgnoreCase(typeReleve)) {
                String role = (CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(getSession()
                        .getApplication()));
                // Récupérer le compte annexe
                CACompteAnnexe ca = new CACompteAnnexe();
                ca.setISession(getSession());
                ca.setIdRole(role);
                ca.setIdExterneRole(affilie.getAffilieNumero());
                ca.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                ca.retrieve();
                if (!ca.isNew()) {
                    // Si le compte existe
                    CASectionManager mgr = new CASectionManager();
                    mgr.setISession(getSession());
                    mgr.setForIdCompteAnnexe(ca.getIdCompteAnnexe());
                    mgr.setLikeIdExterne(annee + "13");
                    mgr.find(BManager.SIZE_NOLIMIT);
                    if (mgr.getSize() > 0) {
                        typeReleve = CodeSystem.TYPE_RELEVE_RECTIF;
                    }
                }
            }

            // Pas de DS (car ne gère pas les AF seul et met en CI) => Génération d'un relevé
            AFApercuReleve releve = new AFApercuReleve();
            releve.setSession(getSession());
            releve.setAffilieNumero(affilie.getAffilieNumero());
            // Recherche id tiers
            releve.setIdTiers(affilie.getIdTiers());
            releve.setType(typeReleve);
            releve.setDateDebut("01.01." + annee);
            releve.setDateFin("31.12." + annee);
            releve.setInterets(CodeSystem.INTERET_MORATOIRE_AUTOMATIQUE);
            releve.setDateReception(dateReceptionForced);
            releve.setNewEtat(CodeSystem.ETATS_RELEVE_SAISIE);

            FWCurrency totalReleve = new FWCurrency(0.00);
            for (Entry<String, String> entry : sommeParCanton.entrySet()) {
                totalReleve.add(entry.getValue());
            }

            releve.setTotalCalculer(totalReleve.toString());
            // releve.setTotalControl(rec.getMontantAf());
            releve.retrieveIdPassage();

            String dateJour = JACalendar.todayJJsMMsAAAA();
            String moisCourant = dateJour.substring(3, 5);
            releve.setIdExterneFacture(dateJour.substring(6, 10) + moisCourant + "000");
            if (CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equalsIgnoreCase(typeReleve)) {
                releve.setIdSousTypeFacture("227013");
            } else {
                releve.setIdSousTypeFacture("2270" + moisCourant);
            }
            releve.setWantControleCotisation(false);
            releve.add(getTransaction());
            if (!getTransaction().hasErrors()) {
                // Parcours de la map pour insérer les totaux par canton
                for (String mapKey : sommeParCanton.keySet()) {
                    String csCanton = CIUtil.codeUtilisateurToCodeSysteme(getTransaction(), mapKey, "PYCANTON",
                            getSession());
                    if (JadeStringUtil.isEmpty(csCanton)) {
                        _addError(getTransaction(), getSession().getLabel("AFSEUL_CANTON_ERRONE") + " (" + mapKey + ")"
                                + " - " + getSession().getLabel("DEC_AFFILIE") + " " + affilie.getAffilieNumero()
                                + " - " + getSession().getLabel("DEC_ANNEE") + " " + annee);
                        return;
                    }
                    // Recherche coti AF pour le canton concerné
                    AFCotisationManager cotisationManager = new AFCotisationManager();
                    cotisationManager.setForAffiliationId(affilie.getAffiliationId());
                    cotisationManager.setSession(getSession());
                    cotisationManager.setForAssuranceCanton(csCanton);
                    cotisationManager.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
                    cotisationManager.changeManagerSize(BManager.SIZE_NOLIMIT);
                    cotisationManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
                    if (cotisationManager.getSize() == 0) {
                        _addError(getTransaction(), getSession().getLabel("ERREUR_AUCUNE_COTISATION_AF") + " "
                                + affilie.getAffilieNumero() + " - " + getSession().getLabel("CANTON") + " " + mapKey);
                        totalErreur++;
                        return;
                    }
                    for (int i = 0; i < cotisationManager.size(); i++) {
                        AFCotisation cotisation = (AFCotisation) cotisationManager.getEntity(i);
                        if ((BSessionUtil.compareDateFirstLowerOrEqual(getSession(), releve.getDateDebut(),
                                cotisation.getDateFin()) || JadeStringUtil.isBlankOrZero(cotisation.getDateFin()))
                                && BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), releve.getDateFin(),
                                        cotisation.getDateDebut())) {
                            if (!cotisation.getDateDebut().equals(cotisation.getDateFin())) {
                                creationLigneReleve(sommeParCanton.get(mapKey), releve, cotisation);
                            }
                        }
                    }
                }
            }
        }
    }

    private void creationLigneReleve(String totalParCanton, AFApercuReleve releve, AFCotisation cotisation)
            throws Exception {
        AFApercuReleveMontant montant = new AFApercuReleveMontant();
        montant.setSession(getSession());
        montant.setIdReleve(releve.getIdReleve());
        montant.setAssuranceId(cotisation.getAssuranceId());
        montant.setCotisationId(cotisation.getCotisationId());
        montant.setDateDebut(releve.getDateDebut());
        montant.setMasse(totalParCanton);
        //
        montant.setMasse(AFUtil.plafonneMasse(totalParCanton, releve.getType(), cotisation.getAssuranceId(),
                releve.getDateDebut(), getSession(), ""));
        float taux = Float.parseFloat(JANumberFormatter.deQuote(cotisation.getTaux(releve.getDateFin(),
                montant.getMasse())));
        float cotiAnnuelleBrut = (Float.parseFloat(montant.getMasse()) * taux) / 100;
        cotiAnnuelleBrut = JANumberFormatter.round(cotiAnnuelleBrut, 0.05, 2, JANumberFormatter.NEAR);

        montant.setMontantCalculer(Float.toString(cotiAnnuelleBrut));
        montant.add(getTransaction());
    }

    /*
     * Met à jour les TreeMap utilisés pour les summary par affilié/Année
     */
    private void _updateSummary(TreeMap<String, Object> hNbrInscriptionsTraites,
            TreeMap<String, Object> hMontantInscritionsTraites, TreeMap<String, Object> hNbrInscriptionsErreur,
            TreeMap<String, Object> hMontantInscriptionsErreur, TreeMap<String, Object> hNbrInscriptionsSuspens,
            TreeMap<String, Object> hMontantInscriptionsSuspens, TreeMap<String, Object> hNbrInscriptionsCI,
            TreeMap<String, Object> hMontantInscriptionsCI, TreeMap<String, Object> hNbrInscriptionsNegatives,
            TreeMap<String, Object> hMontantInscriptionsNegatives, long nbrInscriptionsTraites,
            FWCurrency montantInscritionsTraites, long nbrInscriptionsErreur, FWCurrency montantInscriptionsErreur,
            long nbrInscriptionsSuspens, FWCurrency montantInscriptionsSuspens, long nbrInscriptionsCI,
            FWCurrency montantInscriptionsCI, long nbrInscritptionsNegatives, FWCurrency montantInscriptionsNegatives,
            TreeMap<String, Object> hNbrInscriptionsTotalControle, long nbrInscriptionsTotalControle,
            TreeMap<String, Object> hMontantTotalControle, FWCurrency montantTotalControle, String key) {
        // compteur
        Long value = new Long(0L);
        if (hNbrInscriptionsTraites.get(key) == null) {
            hNbrInscriptionsTraites.put(key, new Long(0L));
        }
        if (hNbrInscriptionsErreur.get(key) == null) {
            hNbrInscriptionsErreur.put(key, new Long(0L));
        }
        if (hNbrInscriptionsSuspens.get(key) == null) {
            hNbrInscriptionsSuspens.put(key, new Long(0L));
        }
        if (hNbrInscriptionsCI.get(key) == null) {
            hNbrInscriptionsCI.put(key, new Long(0L));
        }
        if (hNbrInscriptionsNegatives.get(key) == null) {
            hNbrInscriptionsNegatives.put(key, new Long(0L));
        }
        if (hNbrInscriptionsTotalControle.get(key) == null) {
            hNbrInscriptionsTotalControle.put(key, new Long(0L));
        }
        value = (Long) hNbrInscriptionsTraites.get(key);
        hNbrInscriptionsTraites.put(key, new Long(value.longValue() + nbrInscriptionsTraites));
        totalTraite = totalTraite + nbrInscriptionsTraites;
        value = (Long) hNbrInscriptionsErreur.get(key);
        hNbrInscriptionsErreur.put(key, new Long(value.longValue() + nbrInscriptionsErreur));
        totalErreur = totalErreur + nbrInscriptionsErreur;
        value = (Long) hNbrInscriptionsSuspens.get(key);
        hNbrInscriptionsSuspens.put(key, new Long(value.longValue() + nbrInscriptionsSuspens));
        totalAvertissement = totalAvertissement + nbrInscriptionsSuspens;
        value = (Long) hNbrInscriptionsCI.get(key);
        hNbrInscriptionsCI.put(key, new Long(value.longValue() + nbrInscriptionsCI));
        value = (Long) hNbrInscriptionsNegatives.get(key);
        hNbrInscriptionsNegatives.put(key, new Long(value.longValue() + +nbrInscritptionsNegatives));
        value = (Long) hNbrInscriptionsTotalControle.get(key);
        hNbrInscriptionsTotalControle.put(key, new Long(value.longValue() + nbrInscriptionsTotalControle));
        // montants
        FWCurrency montant = new FWCurrency(0L);
        if (hMontantInscritionsTraites.get(key) == null) {
            hMontantInscritionsTraites.put(key, new FWCurrency("0"));
        }
        if (hMontantInscriptionsErreur.get(key) == null) {
            hMontantInscriptionsErreur.put(key, new FWCurrency("0"));
        }
        if (hMontantInscriptionsSuspens.get(key) == null) {
            hMontantInscriptionsSuspens.put(key, new FWCurrency("0"));
        }
        if (hMontantInscriptionsCI.get(key) == null) {
            hMontantInscriptionsCI.put(key, new FWCurrency("0"));
        }
        if (hMontantInscriptionsNegatives.get(key) == null) {
            hMontantInscriptionsNegatives.put(key, new FWCurrency("0"));
        }
        if (hMontantTotalControle.get(key) == null) {
            hMontantTotalControle.put(key, new FWCurrency("0"));
        }
        montant = (FWCurrency) hMontantInscritionsTraites.get(key);
        montant.add(montantInscritionsTraites);
        hMontantInscritionsTraites.put(key, montant);
        montant = (FWCurrency) hMontantInscriptionsErreur.get(key);
        montant.add(montantInscriptionsErreur);
        hMontantInscriptionsErreur.put(key, montant);
        montant = (FWCurrency) hMontantInscriptionsSuspens.get(key);
        montant.add(montantInscriptionsSuspens);
        hMontantInscriptionsSuspens.put(key, montant);
        montant = (FWCurrency) hMontantInscriptionsCI.get(key);
        montant.add(montantInscriptionsCI);
        hMontantInscriptionsCI.put(key, montant);
        montant = (FWCurrency) hMontantInscriptionsNegatives.get(key);
        montant.add(montantInscriptionsNegatives);
        hMontantInscriptionsNegatives.put(key, montant);
        montant = (FWCurrency) hMontantTotalControle.get(key);
        montant.add(montantTotalControle);
        hMontantTotalControle.put(key, montant);
    }

    private void traitementSiRegistreProvisoire(String annee, int moisDebut, int moisFin, CIEcriture ecriture,
            ArrayList<String> errors, ArrayList<String> info, String numeroAffilie, String noAvs) throws Exception {
        // assuré inconnu
        info.add(getSession().getLabel("DT_ASSURE_INCONNU"));
        CICompteIndividuel ci = new CICompteIndividuel();
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        if ((!JadeStringUtil.isEmpty(ecriture.getAvs()) || !JadeStringUtil.isEmpty(ecriture.getNomPrenom()))
                && !"00000000".equals(ecriture.getAvs())) {

            if (!JadeStringUtil.isEmpty(ecriture.getAvs())) {
                ciMgr.setForNumeroAvs(ecriture.getAvs());
            } else {
                ciMgr.setForNomPrenom(ecriture.getNomPrenom());
            }
        } else if ("00000000".equals(ecriture.getAvs())) {
            ciMgr.setForNumeroAvs(noAvs);
        }
        if (!JadeStringUtil.isEmpty(ciMgr.getForNumeroAvs()) || !JadeStringUtil.isEmpty(ciMgr.getForNomPrenom())) {
            ciMgr.setSession(getSession());
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
            ciMgr.find(getTransaction(), BManager.SIZE_NOLIMIT);
            if (ciMgr.size() != 0) {
                ci = (CICompteIndividuel) ciMgr.getFirstEntity();
                CIEcritureManager ecrMgr = new CIEcritureManager();
                ecrMgr.setSession(getSession());
                ecrMgr.setForAnnee(annee);
                ecrMgr.setForCompteIndividuelId(ci.getCompteIndividuelId());
                ecrMgr.setForAffilie(CIUtil.formatNumeroAffilie(getSession(), numeroAffilie));
                ecrMgr.find(getTransaction(), BManager.SIZE_NOLIMIT);
                for (int i = 0; i < ecrMgr.size(); i++) {
                    CIEcriture ecr = (CIEcriture) ecrMgr.getEntity(i);
                    if ((ecr.getMoisDebut().equals(moisDebut + ""))
                            && (ecr.getMoisFin().equals(moisFin + ""))
                            && ("01".equals(ecr.getGreFormat()) || "11".equals(ecr.getGreFormat())
                                    || "07".equals(ecr.getGreFormat()) || "17".equals(ecr.getGreFormat()))

                            // && (ecr.getMontant().equals(ecriture.getMontant()))) {
                            && ((ecr.getMontant().substring(0, ecr.getMontant().length() - 3)).equals(ecriture
                                    .getMontant().substring(0, ecriture.getMontant().length() - 3)))) {

                        if ((JadeStringUtil.isEmpty(ecriture.getExtourne()) && "0".equals(ecr.getExtourne()))
                                || ecr.getExtourne().equals(ecriture.getExtourne())) {

                            // erreur: écriture identique
                            errors.add(getSession().getLabel("DT_INSCR_IDENTIQUE"));

                            break;
                        }
                    }
                }
            }
        }
    }

    private int determineAnneeNaissance(String numeroAVS, CIEcriture ecriture) throws JAException {

        int anneeNaissance = 60;

        if (numeroAVS.trim().length() < 13) {
            try {
                anneeNaissance = Integer.parseInt(numeroAVS.substring(3, 5));
            } catch (Exception e) {
                System.out.println("Erreur : " + numeroAVS);
            }
        } else {
            CICompteIndividuel ci = ecriture.getForcedCi(getTransaction());
            if (ci != null) {
                JADate dateNaiss = new JADate(ci.getDateNaissance());
                anneeNaissance = dateNaiss.getYear();
                String anneeString = String.valueOf(anneeNaissance);
                if (anneeString.length() == 4) {
                    anneeString = anneeString.substring(2, 4);
                    anneeNaissance = Integer.parseInt(anneeString);
                }

            } else {
                anneeNaissance = 70;
            }

        }

        return anneeNaissance;
    }

    private String testEndSetInfoPourNumAvs(String numeroAVS, String nomPrenom, CIEcriture ecriture,
            ArrayList<String> errors) {
        String noAvs = numeroAVS.trim();
        // Modif v4.12 => dans pucs, le no peut être vide, pour avoir un identiant, on set le no
        if (JadeStringUtil.isBlank(noAvs)) {
            noAvs = "00000000000";
        }
        if (noAvs.endsWith("000") && (noAvs.trim().length() != 13)) {
            noAvs = numeroAVS.substring(0, numeroAVS.lastIndexOf("000"));
        }
        // pour les cas ersam catherine
        if (noAvs.trim().startsWith("000")) {
            if (!JadeStringUtil.isEmpty(nomPrenom.trim())) {
                ecriture.setAvs("");
            } else {
                ecriture.setAvs(noAvs);
            }
        } else {
            ecriture.setAvs(noAvs);
        }
        if (CIUtil.isNNSSlengthOrNegate(noAvs)) {
            ecriture.setNumeroavsNNSS("true");
            ecriture.setAvsNNSS("true");
        }
        if ("true".equalsIgnoreCase(ecriture.getNumeroavsNNSS()) && !NSUtil.nssCheckDigit(ecriture.getAvs())) {
            errors.add(getSession().getLabel("MSG_CI_VAL_AVS"));

        }
        return noAvs;
    }

    private boolean _isInPeriodeAffiliation(String dateDebutAffiliation, String dateFinAffiliation, String annee,
            int moisDebut, int moisFin) throws Exception {
        if ((dateDebutAffiliation == null) || (dateFinAffiliation == null)) {
            return false;
        }
        JADate debAff = new JADate(dateDebutAffiliation);
        JADate finAff = new JADate(("".equals(dateFinAffiliation)) ? "01.01.9999" : dateFinAffiliation);
        // Si c'est 99-99, on compare juste l'année
        if (((99 == moisDebut) && (99 == moisFin)) || ((66 == moisDebut) && (66 == moisFin))) {
            int anneeInt = Integer.parseInt(annee);
            int anneeDebutAff = debAff.getYear();
            if (anneeInt < anneeDebutAff) {
                return false;
            }
            if (!JAUtil.isDateEmpty(dateFinAffiliation)) {
                int anneeFinAff = finAff.getYear();
                if (anneeInt > anneeFinAff) {
                    return false;
                }
            }
        } else {
            JADate deb = new JADate(1, moisDebut, Integer.parseInt(annee));
            JADate fin = new JADate(1, moisFin, Integer.parseInt(annee));
            if (!BSessionUtil.compareDateBetweenOrEqual(getTransaction().getSession(), debAff.toStr("."),
                    finAff.toStr("."), deb.toStr("."))) {
                return false;
            }
            if (!BSessionUtil.compareDateBetweenOrEqual(getTransaction().getSession(), debAff.toStr("."),
                    finAff.toStr("."), fin.toStr("."))) {
                return false;
            }
        }
        return true;
    }

    private String testAndSetPourMontantNegatif(CIEcriture ecriture, ArrayList<String> errors, String montantEcr) {

        if (!"true".equalsIgnoreCase(getAccepteEcrituresNegatives())) {
            FWCurrency cur = new FWCurrency(montantEcr);
            errors.add(getSession().getLabel("DT_ECRITURE_NEGATIVE"));
            ecriture.setGre("11");
            ecriture.setExtourne(CIEcriture.CS_EXTOURNE_1);
            ecriture.setMontant(cur.toStringFormat());
        } else {
            try {
                FWCurrency cur = new FWCurrency(montantEcr);
                if (!cur.isZero() && cur.compareTo(new FWCurrency("1")) == -1) {
                    errors.add(getSession().getLabel("DT_MONTANT_INF_1CHF"));
                } else {
                    ecriture.setGre("11");
                    ecriture.setExtourne(CIEcriture.CS_EXTOURNE_1);
                    ecriture.setMontant(cur.toStringFormat());
                }
            } catch (Exception inex) {
                errors.add(getSession().getLabel("DT_MONTANT_INVALIDE"));
                montantEcr = "0.00";

            }
        }
        return montantEcr;
    }

    private String testAndSetPourMontantPositif(CIEcriture ecriture, ArrayList<String> errors, String montantEcr) {
        try {
            FWCurrency cur = new FWCurrency(montantEcr);
            if (!cur.isZero() && cur.compareTo(new FWCurrency("1")) == -1) {
                errors.add(getSession().getLabel("DT_MONTANT_INF_1CHF"));
                ecriture.setMontant(montantEcr);
            } else {
                ecriture.setGre("01");
                ecriture.setMontant(cur.toStringFormat());
            }
        } catch (Exception inex) {
            errors.add(getSession().getLabel("DT_MONTANT_INVALIDE"));
            montantEcr = "        0.00";

        }
        return montantEcr;
    }

    /*
     * Rretourne un clé qui sera utilisée pour trier les treemaps du process cette clé est composée du n° affilié et de
     * l'année
     */
    private String _getKey(String annee, String nomAffilie) throws Exception {

        return CIUtil.UnFormatNumeroAffilie(getSession(), declarationSalaire.getNumeroAffilie()) + "/" + annee + "#"
                + nomAffilie;

    }

    private BISession getSessionDS(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionDraco");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("DRACO").newSession(local);
            local.setAttribute("sessionDraco", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /*
     * Cherche le journal à utiliser pour ce record Si le journal a déjà été ouvert dans ce process, on le réutilise
     * sinon on le crée (si modeInscription)
     */
    private CIJournal _findJournal(String annee, String nomAffilie) throws Exception {

        // cle pour pouvoir stock un journal par affilié/année
        String key = _getKey(annee, nomAffilie);
        CIJournal journal = null;
        String numAffFormate = CIUtil.formatNumeroAffilie(getSession(), declarationSalaire.getNumeroAffilie());
        if (tableJournaux.containsKey(key)) {
            journal = (CIJournal) tableJournaux.get(key);
        } else {
            // on a pas encore eu à traité ce journal.
            // si il existe dejà dans la DB, on génère une erreur
            CIJournalManager jrnMgr = new CIJournalManager();
            jrnMgr.setSession(getSession());
            jrnMgr.setForAnneeCotisation(annee);
            jrnMgr.setForIdTypeInscription(CIJournal.CS_DECLARATION_SALAIRES);
            jrnMgr.setForIdAffiliation(numAffFormate);
            int size = jrnMgr.getCount(getTransaction());
            journal = new CIJournal();
            journal.setSession(getSession());
            journal.setAnneeCotisation(annee);
            journal.setIdAffiliation(numAffFormate, true, false);
            journal.setLibelle(getSession().getLabel("MSG_LIBELLE_JOURNAL_DECLARATION"));
            journal.setIdTypeCompte(CIJournal.CS_PROVISOIRE);
            journal.setDateReception(dateReceptionForced);
            boolean wantCreatePrincipale = true;
            if (size == 0) {
                // si il n'existe pas encore dans la DB, on le crée (sauf en mode simulation)
                journal.setIdTypeInscription(CIJournal.CS_DECLARATION_SALAIRES);
            } else {
                wantCreatePrincipale = false;
                journal.setIdTypeInscription(CIJournal.CS_DECLARATION_COMPLEMENTAIRE);
            }
            BTransaction transactionJournalDS = null;
            try {
                if (modeInscription) {

                    transactionJournalDS = new BTransaction(getSession());
                    transactionJournalDS.openTransaction();

                    // mode inscription
                    journal.add(transactionJournalDS);

                    if (!getTransaction().hasErrors() && !transactionJournalDS.hasErrors()) {
                        if ("true".equalsIgnoreCase((accepteLienDraco))) {
                            declaration = null;
                            DSDeclarationListViewBean dsMgr = new DSDeclarationListViewBean();
                            dsMgr.setSession((BSession) getSessionDS(getSession()));
                            dsMgr.setForAffiliationId(journal.getIdAffiliation());
                            dsMgr.setForAnnee(journal.getAnneeCotisation());
                            dsMgr.setForEtat(DSDeclarationViewBean.CS_OUVERT);
                            if (wantCreatePrincipale) {
                                dsMgr.setForTypeDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
                            } else {
                                dsMgr.setForTypeDeclaration(DSDeclarationViewBean.CS_COMPLEMENTAIRE);
                            }
                            dsMgr.find(getTransaction(), BManager.SIZE_NOLIMIT);
                            if (dsMgr.size() > 0) {
                                declaration = (DSDeclarationViewBean) dsMgr.getFirstEntity();
                                declaration.setIdJournal(journal.getIdJournal());

                                declaration.setProvenance(getProvenance());

                                declaration.setIdPucsFile(idsPucsFile);
                                declaration.update(getTransaction());
                            } else {
                                declaration = new DSDeclarationViewBean();
                                declaration.setAffiliationId(journal.getIdAffiliation());
                                declaration.setSession((BSession) getSessionDS(getSession()));
                                declaration.setNumeroAffilie(declarationSalaire.getNumeroAffilie());
                                declaration.setAnnee(journal.getAnneeCotisation());
                                declaration.setProvenance(getProvenance());
                                declaration.setIdPucsFile(idsPucsFile);

                                if (wantCreatePrincipale) {
                                    declaration.setTypeDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
                                } else {
                                    declaration.setTypeDeclaration(DSDeclarationViewBean.CS_COMPLEMENTAIRE);
                                }
                                declaration.setEtat(DSDeclarationViewBean.CS_OUVERT);
                                declaration.setIdJournal(journal.getIdJournal());
                                // Modif FER 1-5-6-1
                                if (!JadeStringUtil.isBlankOrZero(dateReceptionForced)) {
                                    declaration.setDateRetourEff(dateReceptionForced);
                                }
                                declaration.add(transactionJournalDS);

                            }
                        }
                    }
                    // Maj type de déclaration de salaire swissdec
                    if (!getTransaction().hasErrors() && !transactionJournalDS.hasErrors() && declaration != null
                            && DSDeclarationViewBean.PROVENANCE_SWISSDEC.equalsIgnoreCase(getProvenance())
                            && !JadeStringUtil.isBlankOrZero(declaration.getAffiliationId())) {
                        AFAffiliation affiliation = declaration.getAffiliation();
                        if (affiliation != null && !affiliation.isNew()) {
                            affiliation.setDeclarationSalaire(CodeSystem.DS_SWISSDEC);
                            affiliation.setWantGenerationSuiviLAALPP(false);
                            affiliation.update(getTransaction());
                        }
                    }

                    if (getTransaction().hasErrors() || transactionJournalDS.hasErrors()) {
                        journal = null;
                        if (launcherImportPucsFileProcess != null) {
                            launcherImportPucsFileProcess
                                    .setImportStatutAFile(CIImportPucsFileProcess.IMPORT_STATUT_KO);
                        }
                        getMemoryLog().logMessage(
                                "Erreur transaction:" + getTransaction().getErrors().toString() + " "
                                        + transactionJournalDS.getErrors().toString(), FWMessage.INFORMATION,
                                "Importation CI");
                        // Pour éviter de logger dans le mail et d'ajouter une erreur dans la transaction alors que le
                        // cas est géré
                        getTransaction().clearErrorBuffer();
                    }

                    if (transactionJournalDS.isRollbackOnly()) {
                        transactionJournalDS.rollback();
                    } else {
                        transactionJournalDS.commit();
                    }
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
                journal = null;
            } finally {
                if (transactionJournalDS != null) {
                    transactionJournalDS.closeTransaction();
                }
            }
            tableJournaux.put(key, journal);
        }
        return journal;
    }

    private BISession getSessionCI(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionPavo");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("PAVO").newSession(local);
            local.setAttribute("sessionPavo", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    private void initProcess() throws Exception {
        appCI = (CIApplication) GlobazServer.getCurrentSystem().getApplication(CIApplication.DEFAULT_APPLICATION_PAVO);

        if (JadeStringUtil.isBlankOrZero(dateReceptionForced) && declarationSalaire.getTransmissionDate() != null) {
            dateReceptionForced = declarationSalaire.getTransmissionDate().getSwissValue();
        }

        if (JadeStringUtil.isBlankOrZero(dateReceptionForced)) {
            dateReceptionForced = JACalendar.todayJJsMMsAAAA();
        }

        titreLog = getSession().getLabel("IMPORT_PUCS_4_PROCESS_TITRE_LOG");
        modeInscription = JadeStringUtil.isBlankOrZero(getSimulation());
    }

    private void createInscriptionAVSAF(Employee employe, SalaryAvs salaireAVS, SalaryCaf salaireCAF) throws Exception {
        createInscription(employe.getNss(), employe.getNom(), employe.getPrenom(), salaireAVS.getPeriode(),
                salaireAVS.getMontantAvs(), salaireCAF.getMontant(), salaireAVS.getMontantAc1(),
                salaireAVS.getMontantAc2(), salaireCAF.getCanton(), isEmployeWithCAF(employe));

    }

    private void createInscriptionAVS(Employee employe, SalaryAvs salaireAVS) throws Exception {

        createInscription(employe.getNss(), employe.getNom(), employe.getPrenom(), salaireAVS.getPeriode(),
                salaireAVS.getMontantAvs(), new Montant(0), salaireAVS.getMontantAc1(), salaireAVS.getMontantAc2(), "",
                isEmployeWithCAF(employe));

    }

    private void createInscriptionAF(Employee employe, SalaryCaf salaireCAF) throws Exception {

        createInscription(employe.getNss(), employe.getNom(), employe.getPrenom(), salaireCAF.getPeriode(),
                new Montant(0), salaireCAF.getMontant(), new Montant(0), new Montant(0), salaireCAF.getCanton(),
                isEmployeWithCAF(employe));

    }

    private boolean isEmployeWithCAF(Employee employe) {
        return employe.getSalariesCaf().size() >= 1;
    }

    private List<SalaryCaf> traiterSalaireAVSAF(Employee employe) throws Exception {

        List<SalaryCaf> listSalaryCAFDejaTraite = new ArrayList<SalaryCaf>();

        for (SalaryAvs salaireAVS : employe.getSalariesAvs()) {

            SalaryCaf salaireCAFMemePeriode = null;

            for (SalaryCaf salaireCAF : employe.getSalariesCaf()) {
                if (salaireAVS.getPeriode().equals(salaireCAF.getPeriode())) {
                    salaireCAFMemePeriode = salaireCAF;
                    listSalaryCAFDejaTraite.add(salaireCAF);
                    break;
                }
            }

            if (salaireCAFMemePeriode != null) {
                createInscriptionAVSAF(employe, salaireAVS, salaireCAFMemePeriode);
            } else {
                createInscriptionAVS(employe, salaireAVS);
            }

        }

        return listSalaryCAFDejaTraite;

    }

    private void updateTotauxAndCalculMasse() throws Exception {

        try {
            // totauxJournaux = new TreeMap<String, Object>();

        } catch (Exception err) {
            JadeLogger.error(this, err);
        }
        if (modeInscription && !declarationSalaire.isAfSeul() && !isAborted()) {
            // maj des totaux des journaux

            Set<String> keys = tableJournaux.keySet();
            Iterator<String> iter = keys.iterator();

            getTransaction().disableSpy();
            while (iter.hasNext()) {

                String key = iter.next();
                CIJournal journal = (CIJournal) tableJournaux.get(key);

                // Mettre à jour les inscriptions
                if (journal != null && !JadeStringUtil.isIntegerEmpty(journal.getIdJournal())) {
                    journal.updateInscription(getTransaction());
                }

                if ("true".equalsIgnoreCase(accepteLienDraco) && journal != null
                        && !JadeStringUtil.isBlankOrZero(journal.getIdJournal())) {
                    DSDeclarationListeManager decMgr = new DSDeclarationListeManager();
                    decMgr.setForIdJournal(journal.getIdJournal());
                    decMgr.setSession((BSession) getSessionDS(getSession()));
                    decMgr.find(getTransaction(), BManager.SIZE_NOLIMIT);
                    if (decMgr.size() > 0) {
                        DSInscriptionsIndividuellesListeViewBean declarationDraco = (DSInscriptionsIndividuellesListeViewBean) decMgr
                                .getFirstEntity();
                        declarationDraco.calculeTotauxAcAf();

                        FWCurrency montantControleDec = (FWCurrency) hMontantTotalControle.get(key);
                        declarationDraco.setTotalControleDS(montantControleDec.toString());
                        journal.setTotalControle(montantControleDec.toString());

                        declarationDraco.update(getTransaction());
                        importeInscriptionsProvisoires(declarationDraco);

                        // InfoRom363 calcul automatique des masses
                        try {
                            theCalculMasseProcess = new DSValideMontantDeclarationProcess();
                            // Tant que la transaction n'est pas commitée, certaines tables (par exemple
                            // DSDECLP) sont lockées
                            // et le calcul des masses ne peut pas s'effectuer
                            if (getTransaction().hasErrors()) {
                                getTransaction().rollback();
                            } else {
                                getTransaction().commit();
                            }

                            theCalculMasseProcess.setSession((BSession) getSessionDS(getSession()));
                            theCalculMasseProcess.setEMailAddress(getEMailAddress());
                            theCalculMasseProcess.setIdDeclaration(declarationDraco.getIdDeclaration());
                            theCalculMasseProcess.executeProcess();

                            if (theCalculMasseProcess.isAborted() || theCalculMasseProcess.isOnError()) {
                                if (launcherImportPucsFileProcess != null) {
                                    launcherImportPucsFileProcess
                                            .setImportStatutAFile(CIImportPucsFileProcess.IMPORT_STATUT_KO);
                                }

                                // le calcul des masses doit envoyer un mail qu'en cas d'erreurs
                                // le mode synchrone ne gère pas l'envoi de mails
                                // c'est pourquoi l'envoi est fait manuellement dans ce process
                                JadeSmtpClient.getInstance().sendMail(
                                        theCalculMasseProcess.getEMailAddress(),
                                        declarationDraco.getNumeroAffilie() + " - " + declarationDraco.getAnnee()
                                                + " - " + theCalculMasseProcess.getSubject(),
                                        theCalculMasseProcess.getSubjectDetail(), new String[0]);
                            }

                        } catch (Exception e) {
                            if (launcherImportPucsFileProcess != null) {

                                String infoDeclaration = "";
                                if (declarationDraco != null) {
                                    infoDeclaration = declarationDraco.getNumeroAffilie() + " - "
                                            + declarationDraco.getAnnee() + " - ";
                                }

                                launcherImportPucsFileProcess.getMemoryLog().logMessage(infoDeclaration + e.toString(),
                                        FWMessage.INFORMATION, this.getClass().getName());
                                launcherImportPucsFileProcess
                                        .setImportStatutAFile(CIImportPucsFileProcess.IMPORT_STATUT_KO);
                            }
                        }

                        if (launcherImportPucsFileProcess != null) {
                            preparerDonnneeRapportExcelMl(declarationDraco);
                        }
                    }
                }

                FWCurrency montantTotal = (FWCurrency) hMontantTotalControle.get(key);

                // Mettre à jour le journal
                if (journal != null && !JadeStringUtil.isEmpty(journal.getIdJournal())) {
                    journal.setIdJournal(journal.getIdJournal());
                    journal.setSession(getSession());
                    journal.retrieve();
                    if (!JadeStringUtil.isDecimalEmpty(montantTotal.toString())) {
                        journal.setTotalControle(montantTotal.toString());
                    }
                    journal.update();
                }

            }

            // notifiyFinished pour e-Business
            if (!"true".equals(getAccepteLienDraco())) {
                if (!JadeStringUtil.isBlankOrZero(idsPucsFile)
                        && (CIImportPucs4Process.ebusinessAccessInstance != null)) {
                    try {

                        List<String> ids = Splitter.on(";").trimResults().splitToList(idsPucsFile);
                        for (String id : ids) {
                            CIImportPucs4Process.ebusinessAccessInstance.notifyFinishedPucsFile(id, provenance,
                                    getSession());
                        }

                    } catch (Exception e) {
                        JadeLogger.error(this, e);
                        getMemoryLog().logMessage("unable to change status", FWMessage.ERREUR, titreLog);
                    }
                }
            }

            getTransaction().enableSpy();
        }

    }

    private void traiterSoldeSalaireCAF(Employee employe, List<SalaryCaf> listSalaryCAFDejaTraite) throws Exception {

        for (SalaryCaf salaireCAF : employe.getSalariesCaf()) {
            if (!listSalaryCAFDejaTraite.contains(salaireCAF)) {
                createInscriptionAF(employe, salaireCAF);
            }
        }
    }

    private void generationLog() throws IOException {

        _doLogJournauxExistant(hJournalExisteDeja);

    }

    private void initResultBean() {
        detailResultBean = new CIImportPucs4DetailResultBean();
        detailResultBean.setNumeroAffilie(declarationSalaire.getNumeroAffilie());
        detailResultBean.setDesignationAffilie(declarationSalaire.getNom());
        detailResultBean.setAFSeul(declarationSalaire.isAfSeul());

    }

    private void sendResultMail(String[] filesPath) throws Exception {

        JadeSmtpClient.getInstance().sendMail(getEMailAddress(), getEMailObject(), getSubjectDetail(), filesPath);

    }

    private void prepareDataForResumePartInResultList() {

        prepareDataForResumePartInResultList(hMontantInscriptionsCI, VALUE_TO_SET_IN_RESUME_BEAN_MONTANT_INSCRIPTION_CI);
        prepareDataForResumePartInResultList(hMontantInscriptionsErreur,
                VALUE_TO_SET_IN_RESUME_BEAN_MONTANT_INSCRIPTION_ERREUR);
        prepareDataForResumePartInResultList(hMontantInscriptionsSuspens,
                VALUE_TO_SET_IN_RESUME_BEAN_MONTANT_INSCRIPTION_SUSPENS);
        prepareDataForResumePartInResultList(hMontantInscriptionsNegatives,
                VALUE_TO_SET_IN_RESUME_BEAN_MONTANT_INSCRIPTION_NEGATIVE);
        prepareDataForResumePartInResultList(hMontantInscritionsTraites,
                VALUE_TO_SET_IN_RESUME_BEAN_MONTANT_INSCRIPTION_TRAITE);

        prepareDataForResumePartInResultList(hNbrInscriptionsCI, VALUE_TO_SET_IN_RESUME_BEAN_NBR_INSCRIPTION_CI);
        prepareDataForResumePartInResultList(hNbrInscriptionsErreur, VALUE_TO_SET_IN_RESUME_BEAN_NBR_INSCRIPTION_ERREUR);
        prepareDataForResumePartInResultList(hNbrInscriptionsSuspens,
                VALUE_TO_SET_IN_RESUME_BEAN_NBR_INSCRIPTION_SUSPENS);
        prepareDataForResumePartInResultList(hNbrInscriptionsNegatives,
                VALUE_TO_SET_IN_RESUME_BEAN_NBR_INSCRIPTION_NEGATIVE);
        prepareDataForResumePartInResultList(hNbrInscriptionsTraites,
                VALUE_TO_SET_IN_RESUME_BEAN_NBR_INSCRIPTION_TRAITE);

    }

    private void prepareDataForResumePartInResultList(TreeMap<String, Object> map, String valueToSetInResumeBean) {

        for (Entry<String, Object> entry : map.entrySet()) {

            String[] tabSplittedKey = entry.getKey().split("/");
            String annee = tabSplittedKey[1];
            annee = annee.trim();
            annee = annee.substring(0, 4);

            CIImportPucs4ResumeBean resumeBean = detailResultBean.getMapAnneeResume().get(annee);

            if (resumeBean == null) {
                resumeBean = new CIImportPucs4ResumeBean();
            }

            if (VALUE_TO_SET_IN_RESUME_BEAN_MONTANT_INSCRIPTION_CI.equalsIgnoreCase(valueToSetInResumeBean)) {
                resumeBean.setMontantInscriptionsCI((FWCurrency) entry.getValue());
            } else if (VALUE_TO_SET_IN_RESUME_BEAN_MONTANT_INSCRIPTION_ERREUR.equalsIgnoreCase(valueToSetInResumeBean)) {
                resumeBean.setMontantInscriptionsErreur((FWCurrency) entry.getValue());
            } else if (VALUE_TO_SET_IN_RESUME_BEAN_MONTANT_INSCRIPTION_SUSPENS.equalsIgnoreCase(valueToSetInResumeBean)) {
                resumeBean.setMontantInscriptionsSuspens((FWCurrency) entry.getValue());
            } else if (VALUE_TO_SET_IN_RESUME_BEAN_MONTANT_INSCRIPTION_NEGATIVE
                    .equalsIgnoreCase(valueToSetInResumeBean)) {
                resumeBean.setMontantInscriptionsNegatives((FWCurrency) entry.getValue());
            } else if (VALUE_TO_SET_IN_RESUME_BEAN_MONTANT_INSCRIPTION_TRAITE.equalsIgnoreCase(valueToSetInResumeBean)) {
                resumeBean.setMontantInscriptionsTraites((FWCurrency) entry.getValue());
            }

            else if (VALUE_TO_SET_IN_RESUME_BEAN_NBR_INSCRIPTION_CI.equalsIgnoreCase(valueToSetInResumeBean)) {
                resumeBean.setNbrInscriptionsCI((Long) entry.getValue());
            } else if (VALUE_TO_SET_IN_RESUME_BEAN_NBR_INSCRIPTION_ERREUR.equalsIgnoreCase(valueToSetInResumeBean)) {
                resumeBean.setNbrInscriptionsErreur((Long) entry.getValue());
            } else if (VALUE_TO_SET_IN_RESUME_BEAN_NBR_INSCRIPTION_SUSPENS.equalsIgnoreCase(valueToSetInResumeBean)) {
                resumeBean.setNbrInscriptionsSuspens((Long) entry.getValue());
            } else if (VALUE_TO_SET_IN_RESUME_BEAN_NBR_INSCRIPTION_NEGATIVE.equalsIgnoreCase(valueToSetInResumeBean)) {
                resumeBean.setNbrInscriptionsNegatives((Long) entry.getValue());
            } else if (VALUE_TO_SET_IN_RESUME_BEAN_NBR_INSCRIPTION_TRAITE.equalsIgnoreCase(valueToSetInResumeBean)) {
                resumeBean.setNbrInscriptionsTraites((Long) entry.getValue());
            }

            detailResultBean.getMapAnneeResume().put(annee, resumeBean);

        }

    }

    private String generationResultList() {

        CIImportPucs4ResultList importPucs4ResultList = new CIImportPucs4ResultList(getSession(), detailResultBean);
        importPucs4ResultList.createResultList();

        return importPucs4ResultList.getOutputFile();
    }

    private void creationDesReleve() throws Exception {

        // Création du relevé à l'état saisi

        for (Entry<String, Map<String, String>> entry : mapAnneeMapTotalParCanton.entrySet()) {

            AFAffiliation affilie = appCI.getAffilieByNo(getSession(),
                    CIUtil.formatNumeroAffilie(getSession(), declarationSalaire.getNumeroAffilie()), true, false, "",
                    "", entry.getKey(), "", "");

            if (affilie != null) {
                creationReleve(entry.getKey(), affilie, entry.getValue());

                if (launcherImportPucsFileProcess != null) {
                    preparerDonnneeRapportExcelMlAFSeule(entry.getKey(), entry.getValue());
                }
            }

        }

    }

    @Override
    public String getSubjectDetail() {

        try {
            // Récupération des éventuelles erreurs oubliées dans la transaction
            if (getTransaction() != null) {
                if (getTransaction().hasErrors()) {
                    getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
                }
            }
        } catch (Exception e) {
            JadeLogger.warn(this, "ERROR in process " + this.getClass().getName() + " (" + e.toString() + ")");
            JadeLogger.warn(this, e);
        }

        return super.getSubjectDetail();
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {
            initProcess();
            initResultBean();

            for (Employee employe : declarationSalaire.getEmployees()) {
                traiterSoldeSalaireCAF(employe, traiterSalaireAVSAF(employe));
            }

            if (declarationSalaire.isAfSeul()) {
                creationDesReleve();
            }

            updateTotauxAndCalculMasse();

            generationLog();

            comparaisonDataControlDataCompute();

            if (!declarationSalaire.isAfSeul()) {
                prepareDataForResumePartInResultList();
            }

            String[] filesPath = new String[1];
            filesPath[0] = generationResultList();

            sendResultMail(filesPath);

        } catch (Exception ioe) {
            JadeLogger.error(this, ioe);
            result = false;
            getMemoryLog().logMessage(ioe.toString(), FWMessage.FATAL, titreLog);
            if (launcherImportPucsFileProcess != null) {
                launcherImportPucsFileProcess.getMemoryLog().logMessage(ioe.toString(), FWMessage.INFORMATION,
                        this.getClass().getName());
                launcherImportPucsFileProcess.setImportStatutAFile(CIImportPucsFileProcess.IMPORT_STATUT_KO);
            }
        }

        return result;

    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    @Override
    protected String getEMailObject() {

        String mailObject = numAffilieBase + " " + getSession().getLabel("DECLARATION_NB_ERREUR") + " "
                + String.valueOf(totalErreur) + " " + getSession().getLabel("DECLARATION_NB_AVERT") + " "
                + String.valueOf(totalAvertissement) + " " + getSession().getLabel("DECLARATION_NB_TOTAL")
                + String.valueOf(totalTraite);
        if (isOnError() || isErrorMontant || isErrorNbInscriptions || isAborted()) {
            if (declarationSalaire.isAfSeul()) {
                return mailObject + " " + getSession().getLabel("EMAIL_IMPORTATION_AFSEULE_ECHEC");
            } else {
                return mailObject + " " + getSession().getLabel("EMAIL_IMPORTATION_ECHEC");
            }
        } else {
            if (declarationSalaire.isAfSeul()) {
                return mailObject + " " + getSession().getLabel("EMAIL_IMPORTATION_AFSEULE_SUCCES");
            } else {
                return mailObject + " " + getSession().getLabel("EMAIL_IMPORTATION_SUCCES");
            }
        }
    }

}
