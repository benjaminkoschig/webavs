package globaz.naos.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.processFacturation.AFProcessFacturationManager;
import globaz.naos.db.processFacturation.AFProcessFacturationViewBean;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CACompteur;
import globaz.osiris.db.comptes.CARubrique;
import globaz.pyxis.util.TISQL;
import globaz.pyxis.util.TIToolBox;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.auriga.business.constantes.AUDecisionEtat;
import ch.globaz.auriga.business.constantes.AUParametrePlageValeur;
import ch.globaz.auriga.business.models.DecisionCAPSearchModel;
import ch.globaz.auriga.business.models.SimpleDecisionCAP;
import ch.globaz.auriga.business.services.AurigaServiceLocator;
import ch.globaz.auriga.business.services.DecisionCAPService;
import ch.globaz.auriga.web.application.AUApplication;
import ch.globaz.common.business.services.ParametreService;

/**
 * Process pour la facturation des Cotisations CAP/CGAS
 * 
 * @author: sbr
 */

public final class AFProcessFacturationCAPCGAS extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Classe contenant les données issues d'un calcul de cotisation. Permet d'enregistrer les valeurs de facturation
     * avant insertion dans la BD
     * 
     * @author sbr
     */
    public static class CalculCotisation {
        private String affilieNumero = null;
        private List<String> assuranceException = null;
        private String idExterneFacture = null;
        private String idModuleFacturation = null;
        private String idSousTypeFacture = null;

        private double montant;
        private List<LineFacturation> montantList = null;

        private int nbException = 0;

        private String periodiciteAff = null;

        public CalculCotisation() {
            super();
        }

        public String getAffilieNumero() {
            return affilieNumero;
        }

        public List<String> getAssuranceException() {
            return assuranceException;
        }

        public String getIdExterneFacture() {
            return idExterneFacture;
        }

        public String getIdModuleFacturation() {
            return idModuleFacturation;
        }

        public String getIdSousTypeFacture() {
            return idSousTypeFacture;
        }

        public double getMontant() {
            return montant;
        }

        public List<LineFacturation> getMontantList() {
            return montantList;
        }

        public int getNbException() {
            return nbException;
        }

        public String getPeriodiciteAff() {
            return periodiciteAff;
        }

        public int incNbException() {
            return nbException++;
        }

        public void setAffilieNumero(String string) {
            affilieNumero = string;
        }

        public void setAssuranceException(List<String> list) {
            assuranceException = list;
        }

        public void setIdExterneFacture(String string) {
            idExterneFacture = string;
        }

        public void setIdModuleFacturation(String string) {
            idModuleFacturation = string;
        }

        public void setIdSousTypeFacture(String string) {
            idSousTypeFacture = string;
        }

        public void setMontant(double d) {
            montant = d;
        }

        public void setMontantList(List<LineFacturation> list) {
            montantList = list;
        }

        public void setNbException(int i) {
            nbException = i;
        }

        public void setPeriodiciteAff(String string) {
            periodiciteAff = string;
        }

    }

    /**
     * Class utilisée pour enregistrer les valeurs de facturation avant insertion dans la DB.
     * 
     * 
     * @author mmu, sau
     */
    public static class LineFacturation {

        private String anneeCotisation = null;

        private String assuranceId = null;

        private String caisse = null;
        private String debutPeriode = null;
        private String finPeriode = null;
        private String idRubrique = null;
        // taux caché
        private boolean isTauxCache = false;
        private String libelle = null;

        private double masse = 0.0;
        private double montant = 0.0;
        private double taux = 0.0;

        /**
         * Retourne un nouvelle enregistrement de facturation initialisé avec les paramètres d'entrées.
         * 
         * @param donneesFacturation
         * @param anneeFacturation
         * @param debutPeriode
         *            DD.MM.YYYY
         * @param finPeriode
         *            DD.MM.YYYY
         * @param masse
         * @param montant
         * @param taux
         * @return Un nouvelle enregistrement
         */
        public LineFacturation(AFProcessFacturationViewBean donneesFacturation, String anneeFacturation,
                String debutPeriode, String finPeriode, double masse, double montant, double taux) {
            this.debutPeriode = debutPeriode;
            this.finPeriode = finPeriode;
            this.masse = masse;
            this.montant = montant;
            this.taux = taux;

            if (donneesFacturation.getNatureRubrique().equals(CodeSystem.NAT_RUBRIQUE_COTI_AVEC_MASSE)
                    || donneesFacturation.getNatureRubrique().equals(CodeSystem.NAT_RUBRIQUE_COTI_SANS_MASSE)) {
                anneeCotisation = anneeFacturation;
            } else {
                // Cas d'utilisation avec relevés
                anneeCotisation = "";
            }

            String langue = donneesFacturation.getLangue();
            if (langue.equalsIgnoreCase(CodeSystem.LANGUE_FRANCAIS)) {
                libelle = donneesFacturation.getAssuranceLibelleFr();
            } else if (langue.equalsIgnoreCase(CodeSystem.LANGUE_ALLEMAND)) {
                libelle = donneesFacturation.getAssuranceLibelleAl();
            } else if (langue.equalsIgnoreCase(CodeSystem.LANGUE_ITALIEN)) {
                libelle = donneesFacturation.getAssuranceLibelleIt();
            }
            assuranceId = donneesFacturation.getAssuranceId();
            idRubrique = donneesFacturation.getAssuranceRubriqueId();

        }

        public String getAnneeCotisation() {
            return anneeCotisation;
        }

        public String getAssuranceId() {
            return assuranceId;
        }

        /**
         * @return
         */
        public String getCaisse() {
            return caisse;
        }

        public String getDebutPeriode() {
            return debutPeriode;
        }

        public String getFinPeriode() {
            return finPeriode;
        }

        public String getIdRubrique() {
            return idRubrique;
        }

        public String getLibelle() {
            return libelle;
        }

        public double getMasse() {
            return masse;
        }

        public double getMontant() {
            return montant;
        }

        public double getTaux() {
            return taux;
        }

        public boolean isTauxCache() {
            return isTauxCache;
        }

        public void setAnneeCotisation(String string) {
            anneeCotisation = string;
        }

        public void setAssuranceId(String string) {
            assuranceId = string;
        }

        /**
         * @param string
         */
        public void setCaisse(String string) {
            caisse = string;
        }

        public void setDebutPeriode(String string) {
            debutPeriode = string;
        }

        public void setFinPeriode(String string) {
            finPeriode = string;
        }

        public void setIdRubrique(String string) {
            idRubrique = string;
        }

        public void setLibelle(String string) {
            libelle = string;
        }

        public void setMasse(double d) {
            masse = d;
        }

        public void setMontant(double d) {
            montant = d;
        }

        public void setTaux(double d) {
            taux = d;
        }

        public void setTauxCache(boolean isTauxCache) {
            this.isTauxCache = isTauxCache;
        }

    }

    public final static int PERIODIQUE_COT_PERS = 0;

    public final static int PERIODIQUE_COT_PERS_IND = 1;

    public final static int PERIODIQUE_COT_PERS_NAC = 2;

    /**
     * Calcul une cotisation à partir d'une ligne du AFProcessFactureManager <br>
     * 
     * @param process
     *            process appellant la méthode: process naos ou musca
     * @param donneeFacturation
     *            entity retourné par AFProcessFacturationViewBean
     * @param anneeFacturation
     * @param moisFacturation
     * @param isParitaire
     *            true is on veut calculer une paritaire
     * @param isPersonel
     *            true is on veut calculer une Personelle
     * @param cotisationAffilie
     *            liste de cotisation déjà calculées pour un affilié
     */
    public static final CalculCotisation calculerCotisation(BProcess process,
            AFProcessFacturationViewBean donneesFacturation, String anneeFacturation, String moisFacturation,
            boolean isParitaire, boolean isPersonnel, boolean isCAP_CGAS, List<LineFacturation> cotisationAffilie,
            String idModuleFacturationCAPCGAS) throws Exception {

        BSession sessionNaos = null;

        String applicationId = process.getSession().getApplicationId();

        if ("MUSCA".equalsIgnoreCase(applicationId)) {
            FAApplication applicationMusca = (FAApplication) process.getSession().getApplication();
            sessionNaos = (BSession) applicationMusca.getSessionNaos(process.getSession());
        } else {
            // Cas d'utilisation avec relevés
            sessionNaos = process.getSession();
        }

        String affilieNumero = null;
        String periodiciteAff = null;
        String noTrimestre = null;
        String idExterneFacture = null;
        String idSousTypeFacture = null;

        String debutPeriodeFacturation = null;
        String finPeriodeFacturation = null;

        String dateEffectiveDebutFacturation = null;
        String dateEffectiveFinFacturation = null;
        String dateDebutCotisation = null;
        String dateFinCotisation = null;
        String dateRetraite = null;

        List<LineFacturation> montantList = new ArrayList<LineFacturation>();

        List<String> assuranceException = new ArrayList<String>();
        double montant = 0.0;

        int nbException = 0;

        affilieNumero = donneesFacturation.getAffilieNumero();

        // ************************************************************
        // Calcul de la date de Debut et de Fin de Facturation et
        // des code pour la facturation en fonction de la période
        // ************************************************************

        // Afin de gérer des périodicité différentes
        // le no de décompte est calculé en fonction de la coti lorsque
        // l'affiliation est mensuelle (mensuelle et trimestrielle au niveau des
        // cotis)
        periodiciteAff = donneesFacturation.getPeriodiciteAff();

        if (periodiciteAff.equals(CodeSystem.PERIODICITE_MENSUELLE)
                && !donneesFacturation.getPeriodiciteCoti().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {

            // Période de Facturation
            debutPeriodeFacturation = "01." + moisFacturation + "." + anneeFacturation;
            finPeriodeFacturation = AFUtil.getDateEndOfMonth(debutPeriodeFacturation);

            // Code de facturation
            idExterneFacture = anneeFacturation + moisFacturation + "000";
            idSousTypeFacture = "2270" + moisFacturation;

        } else if (periodiciteAff.equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)
                || donneesFacturation.getPeriodiciteCoti().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {

            // Période de Facturation
            if ((Integer.parseInt(moisFacturation) - 2) < 10) {
                debutPeriodeFacturation = "01.0" + Integer.toString(Integer.parseInt(moisFacturation) - 2) + "."
                        + anneeFacturation;
            } else {
                debutPeriodeFacturation = "01." + Integer.toString(Integer.parseInt(moisFacturation) - 2) + "."

                + anneeFacturation;
            }
            finPeriodeFacturation = AFUtil.getDateEndOfMonth("01." + moisFacturation + "." + anneeFacturation);

            // Code de facturation
            if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.MARS_TRIMESTRE_1)) {
                noTrimestre = "1";
            } else if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.JUIN_TRIMESTRE_2)) {
                noTrimestre = "2";
            } else if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.SEPTEMBRE_TRIMESTRE_3)) {
                noTrimestre = "3";
            } else if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.ANNUEL)) {
                noTrimestre = "4";
            } else {
                // TODO Traiter proprement ce cas de figure
                noTrimestre = "0";
            }

            idExterneFacture = anneeFacturation + "4" + noTrimestre + "000";
            idSousTypeFacture = "22704" + noTrimestre;

        } else if (periodiciteAff.equals(CodeSystem.PERIODICITE_ANNUELLE)) {

            // Période de Facturation
            debutPeriodeFacturation = "01.01." + anneeFacturation;
            finPeriodeFacturation = "31.12." + anneeFacturation;

            // Code de facturation
            if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.MARS_TRIMESTRE_1)) {
                noTrimestre = "5";
            } else if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.JUIN_TRIMESTRE_2)) {
                noTrimestre = "6";
            } else if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.SEPTEMBRE_TRIMESTRE_3)) {
                noTrimestre = "7";
            } else {
                // Anuelle facturée en décembre
                noTrimestre = "0";
            }
            idExterneFacture = anneeFacturation + "4" + noTrimestre + "000";
            idSousTypeFacture = "22704" + noTrimestre;
        }

        // ************************************************************
        // Calcul de la periode exacte de facturation
        // ************************************************************
        // Date début : le plus grand entre début Facturation, début Cotisation
        // Date fin : le plus petit entre fin Facturation, fin Cotisation,

        // Date de début
        dateEffectiveDebutFacturation = debutPeriodeFacturation;

        dateDebutCotisation = AFUtil.getDateBeginingOfMonth(donneesFacturation.getDateDebutCoti());
        if (BSessionUtil.compareDateFirstGreater(sessionNaos, dateDebutCotisation, dateEffectiveDebutFacturation)) {
            dateEffectiveDebutFacturation = dateDebutCotisation;
        }

        // Date de Fin
        dateEffectiveFinFacturation = finPeriodeFacturation;

        dateFinCotisation = AFUtil.getDateEndOfMonth(donneesFacturation.getDateFinCoti());

        if ((!JadeStringUtil.isIntegerEmpty(dateFinCotisation))
                && BSessionUtil.compareDateFirstLower(sessionNaos, dateFinCotisation, dateEffectiveFinFacturation)) {

            dateEffectiveFinFacturation = dateFinCotisation;
        }

        // ************************************************************
        // Calcul le nombre de mois a facturer
        // ************************************************************

        int nbMoisFacturer = AFUtil.nbMoisPeriode(sessionNaos, dateEffectiveDebutFacturation,
                dateEffectiveFinFacturation);

        // *******************************
        // PERSONNEL
        // *******************************
        if (isCAP_CGAS) {
            montantList.clear();

            if (nbMoisFacturer == 0) {
                montant = 0.0;
            } else {
                if (nbMoisFacturer == 12) {
                    montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMontantAnnuelCoti()));

                } else if (nbMoisFacturer == 3) {
                    montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation
                            .getMontantTrimestrielCoti()));
                } else {
                    montant = nbMoisFacturer
                            * Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMontantMensuelCoti()));
                }
                // Si périodicité annuelle faire une soustraction du montant
                // déjà payer

                // TODO Voir si l'utilisation du mois de facturation n'est pas plus adapté
                if (donneesFacturation.getPeriodiciteAff().equalsIgnoreCase(CodeSystem.PERIODICITE_ANNUELLE)) {
                    CACompteur cnt = AFProcessFacturationCAPCGAS.getCompteur(
                            donneesFacturation.getAssuranceRubriqueId(), donneesFacturation.getAffilieNumero(),
                            anneeFacturation, sessionNaos);
                    FWCurrency montantCompta = new FWCurrency(montant);
                    if (cnt != null) {
                        montantCompta.sub(cnt.getCumulCotisation());
                    }

                    montant = montantCompta.doubleValue();

                }

                montantList.add(new LineFacturation(donneesFacturation, anneeFacturation,
                        dateEffectiveDebutFacturation, dateEffectiveFinFacturation, 0.0, montant, 0.0));

            }
        } else {
            montant = 0.0;
            process.getMemoryLog().logMessage(
                    FWMessageFormat.format(sessionNaos.getLabel("1820"), donneesFacturation.getCotisationId(),
                            donneesFacturation.getTypeAffiliation()), FWMessage.AVERTISSEMENT,
                    AFProcessFacturationCAPCGAS.class.getName());
        }

        CalculCotisation calcul = new CalculCotisation();

        calcul.setAffilieNumero(affilieNumero);
        calcul.setPeriodiciteAff(periodiciteAff);
        calcul.setIdExterneFacture(idExterneFacture);
        calcul.setIdSousTypeFacture(idSousTypeFacture);
        calcul.setIdModuleFacturation(idModuleFacturationCAPCGAS);
        calcul.setNbException(nbException);
        calcul.setMontant(montant);
        calcul.setMontantList(montantList);
        calcul.setAssuranceException(assuranceException);

        return calcul;
    }

    /**
     * Permet de récuperer un compte annexe Date de création : (22.05.2003 10:12:15)
     * 
     * @return globaz.osiris.db.comptes.CACompteAnnexe
     */
    public static CACompteAnnexe getCompteAnnexe(String numAff, BSession sessionNaos) throws Exception {
        // Chargement du manager
        CACompteAnnexe compteAnnexe = null;
        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        manager.setSession(sessionNaos);
        manager.setForIdRole(CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(sessionNaos.getApplication()));
        manager.setForIdExterneRole(numAff);
        manager.find();
        if (!manager.isEmpty()) {
            compteAnnexe = (CACompteAnnexe) manager.getEntity(0);
        }

        return compteAnnexe;
    }

    public static CACompteur getCompteur(String rubriqueId, String numAff, String dateFin, BSession sessionNaos)
            throws Exception {
        // Chargement du compteur
        CACompteur compteur = new CACompteur();
        compteur.setSession(sessionNaos);
        compteur.setAlternateKey(CACompteur.AK_CPTA_RUB_ANNEE);
        compteur.setAnnee(dateFin);
        if (AFProcessFacturationCAPCGAS.getCompteAnnexe(numAff, sessionNaos) == null) {
            return null;
        }
        compteur.setIdCompteAnnexe(AFProcessFacturationCAPCGAS.getCompteAnnexe(numAff, sessionNaos).getIdCompteAnnexe());
        compteur.setIdRubrique(rubriqueId);
        compteur.retrieve();
        if (!compteur.isNew()) {
            return compteur;
        } else {
            return null;
        }
    }

    private boolean facturerCAP_CGAS = false;

    private boolean facturerParitaire = false;

    private boolean facturerPersonnel = false;

    private String idModuleFacturationCAPCGAS = "";

    private globaz.musca.api.IFAPassage passage = null;

    /**
     * Constructeur de AFProcessFacturation.
     */
    public AFProcessFacturationCAPCGAS() {
        super();
    }

    /**
     * Constructeur de AFProcessFacturation.
     * 
     * @param parent
     *            globaz.framework.process.FWProcess
     */
    public AFProcessFacturationCAPCGAS(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage après erreur ou exécution.
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Process de Facturation.
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {

        try {
            facturer(getPassage());
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            getTransaction().addErrors(e.getMessage());
            JadeLogger.error(this, e);
        }
        return !isOnError();
    }

    private int createAfactAFCAP(String idPassage, String idEnteteFacture, LineFacturation aLine,
            double montantAFAFacturer, String idModuleFacturation, int nbLineFactureToAdd,
            CARubrique rubriqueAllocationEnfantCAP, String affilieNumero, String periodiciteAffiliation)
            throws Exception {

        double montantAFDejaFacture = 0;
        if (CodeSystem.PERIODICITE_ANNUELLE.equalsIgnoreCase(periodiciteAffiliation)) {
            CACompteur cnt = AFProcessFacturationCAPCGAS.getCompteur(rubriqueAllocationEnfantCAP.getIdRubrique(),
                    affilieNumero, aLine.getAnneeCotisation(), getSession());

            if (cnt != null) {
                montantAFDejaFacture = Double.valueOf(cnt.getCumulCotisation());
            }

        }

        montantAFAFacturer = montantAFAFacturer - Math.abs(montantAFDejaFacture);

        FAAfact lineFacture = new FAAfact();
        lineFacture.setISession(getSession());
        lineFacture.setIdEnteteFacture(idEnteteFacture);
        lineFacture.setIdPassage(idPassage);
        lineFacture.setIdModuleFacturation(idModuleFacturation);
        lineFacture.setIdTypeAfact(CodeSystem.TYPE_FACT_FACT_STANDARD);
        lineFacture.setNonImprimable(Boolean.FALSE);
        lineFacture.setNonComptabilisable(Boolean.FALSE);
        lineFacture.setAQuittancer(Boolean.FALSE);
        lineFacture.setAnneeCotisation(aLine.getAnneeCotisation());
        lineFacture.setIdRubrique(rubriqueAllocationEnfantCAP.getIdRubrique());
        lineFacture.setDebutPeriode(aLine.getDebutPeriode());
        lineFacture.setFinPeriode(aLine.getFinPeriode());

        // Comme on travail en positif (Math.abs(montantAFDejaFacture))
        // Il faut faire * -1 pour l'afact puisqu'il s'agit d'une allocation
        // Si montantAFAFacturer est négatif cela signifie que trop d'AF ont été versée et il faut donc avoir un afact
        // positif
        lineFacture.setMontantFacture(Double.toString(JANumberFormatter.round(montantAFAFacturer * -1, 0.05, 2,
                JANumberFormatter.NEAR)));

        // ajout de la caisse métier
        String caisseMetier = aLine.getCaisse();
        if (caisseMetier == null) {
            caisseMetier = "";
        }
        lineFacture.setNumCaisse(caisseMetier);

        lineFacture.add(getTransaction());

        return nbLineFactureToAdd++;

    }

    /**
     * Traitement de l'affiliation Création de la facturation périodique, CAP/CGAS Ne prendre que les affiliations
     * concernées par le passage et qui n'ont pas été traitées
     * 
     * @param passage
     */
    private boolean facturer(globaz.musca.api.IFAPassage passage) {

        getMemoryLog().logMessage("Process Started", FWMessage.INFORMATION, this.getClass().getName());
        AFProcessFacturationManager manager = null;
        BTransaction cursorTransaction = null;
        BStatement statement = null;

        try {
            BSession muscaSession = getSession(); // (BSession)passage.getISession();
            FAApplication muscaApp = (FAApplication) muscaSession.getApplication();
            BSession naosSession = (BSession) muscaApp.getSessionNaos(muscaSession);

            manager = new AFProcessFacturationManager();
            manager.setISession(naosSession);

            String idPassage = passage.getIdPassage();
            String datePeriode = passage.getDatePeriode();
            String moisFacturation = datePeriode.substring(0, 2);
            String anneeFacturation = datePeriode.substring(3);

            getMemoryLog().logMessage("idPassage           = " + idPassage, FWMessage.INFORMATION,
                    this.getClass().getName());
            getMemoryLog().logMessage("datePeriode         = " + datePeriode, FWMessage.INFORMATION,
                    this.getClass().getName());
            getMemoryLog().logMessage("isFacturerCAP_CGAS = " + isFacturerCAP_CGAS(), FWMessage.INFORMATION,
                    this.getClass().getName());
            getMemoryLog().logMessage("------------------------", FWMessage.INFORMATION, this.getClass().getName());

            // ************************************************************
            // Initialisation des paramètres pour la recherche des Cotisations
            // ************************************************************

            manager.setWantSousEnsembleAffilie(hasSousEnsembleAffilies(idPassage));
            manager.setForIdPassage(idPassage);

            manager.setForDateFacturation(datePeriode);

            if (isFacturerCAP_CGAS()) {
                // ne devrait plus arriver car les modules sont séparés
                manager.setForTypeAffiliation(AFProcessFacturationManager.TYPE_AFFILIATION__CAP_CGAS);
                manager.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
            }

            // ************************************************************
            // Création du cursorTransaction
            // ************************************************************
            cursorTransaction = (BTransaction) getSession().newTransaction();
            cursorTransaction.openTransaction();
            statement = manager.cursorOpen(cursorTransaction);

            // ************************************************************
            // Parcourir toutes les Cotisations a facturer
            // ************************************************************
            AFProcessFacturationViewBean donneesFacturation = null;

            String previousKey = null;

            // stoque toutes les lignes de factures pour un même affilié
            List<LineFacturation> cotisationAffilie = new ArrayList<LineFacturation>();
            CalculCotisation calculCotisation = null;
            FAEnteteFacture enteteFacture = null;

            String affilieNumero = null;

            int nbCotisation = 0;
            int nbEnteteToAdd = 0;
            int nbLineFactureToAdd = 0;
            int nbEnteteOK = 0;
            int nbLineFactureOK = 0;
            int nbEnteteError = 0;
            int nbLineFactureError = 0;
            int nbMontantZero = 0;
            int nbException = 0;

            CARubrique rubriqueAllocationEnfantCAP = loadRubriqueAllocationEnfantCAP(anneeFacturation);
            String idAffiliation = "";

            // ****************************************************************
            // POUR CHAQUE COTISATION
            // ****************************************************************
            while ((donneesFacturation = (AFProcessFacturationViewBean) manager.cursorReadNext(statement)) != null) {

                // les données sont triées par tiers
                // Si le tiers a changé, on inscrit tous les afacts de l'entete
                // précédant stockés dans cotisationAffilié
                if ((previousKey != null)
                        && !previousKey.equals(donneesFacturation.getIdTiers() + ","
                                + donneesFacturation.getIdPlanAffiliation())) {

                    if (enteteFacture != null) {
                        for (int i = 0; i < cotisationAffilie.size(); i++) {
                            LineFacturation aLine = cotisationAffilie.get(i);

                            int nbMoisAFacturer = AFUtil.nbMoisPeriode(getSession(), aLine.getDebutPeriode(),
                                    aLine.getFinPeriode());

                            AFAssurance theAssurance = loadAssurance(aLine.getAssuranceId());

                            if (isAssuranceCAPWithAF(theAssurance)) {
                                double montantAFAFacturer = getMontantAFAFacturer(anneeFacturation, idAffiliation,
                                        affilieNumero, nbMoisAFacturer, aLine.getDebutPeriode(), aLine.getFinPeriode());

                                // Comme l'allocation familiale se trouve dans un afact négatif séparé
                                // Il faut augmenter l'afact de cotisation avec la valeur de l'allocation familiale
                                aLine.setMontant(aLine.getMontant() + montantAFAFacturer);

                                if (montantAFAFacturer != 0) {
                                    nbLineFactureToAdd = createAfactAFCAP(idPassage, enteteFacture.getIdEntete(),
                                            aLine, montantAFAFacturer, calculCotisation.getIdModuleFacturation(),
                                            nbLineFactureToAdd, rubriqueAllocationEnfantCAP,
                                            calculCotisation.getAffilieNumero(), calculCotisation.getPeriodiciteAff());
                                }

                            }

                            if (aLine.getMontant() != 0) {
                                FAAfact lineFacture = new FAAfact();
                                lineFacture.setISession(getSession());
                                lineFacture.setIdEnteteFacture(enteteFacture.getIdEntete());
                                lineFacture.setIdPassage(idPassage);
                                lineFacture.setIdModuleFacturation(calculCotisation.getIdModuleFacturation());
                                lineFacture.setIdTypeAfact(CodeSystem.TYPE_FACT_FACT_STANDARD);
                                lineFacture.setNonImprimable(Boolean.FALSE);
                                lineFacture.setNonComptabilisable(Boolean.FALSE);
                                lineFacture.setAQuittancer(Boolean.FALSE);
                                lineFacture.setAnneeCotisation(aLine.getAnneeCotisation());
                                lineFacture.setLibelle(aLine.getLibelle());
                                lineFacture.setIdRubrique(aLine.getIdRubrique());
                                lineFacture.setDebutPeriode(aLine.getDebutPeriode());
                                lineFacture.setFinPeriode(aLine.getFinPeriode());
                                lineFacture.setMasseFacture(Double.toString(JANumberFormatter.round(aLine.getMasse(),
                                        0.05, 2, JANumberFormatter.NEAR)));
                                lineFacture.setMontantFacture(Double.toString(JANumberFormatter.round(
                                        aLine.getMontant(), 0.05, 2, JANumberFormatter.NEAR)));
                                lineFacture.setTauxFacture(Double.toString(aLine.getTaux()));
                                // ajout de la caisse métier
                                String caisseMetier = aLine.getCaisse();
                                if (caisseMetier == null) {
                                    caisseMetier = "";
                                }
                                lineFacture.setNumCaisse(caisseMetier);
                                // indication si taux à cacher
                                lineFacture.setAffichtaux(new Boolean(!aLine.isTauxCache()));
                                lineFacture.add(getTransaction());

                                nbLineFactureToAdd++;
                            }

                        }
                    }

                    // Validation finale
                    if (!getTransaction().hasErrors()) {
                        getTransaction().commit();

                        nbEnteteOK += nbEnteteToAdd;
                        nbLineFactureOK += nbLineFactureToAdd;
                    } else {
                        rollbackTransaction(getSession().getLabel("691") + affilieNumero);
                        getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());

                        nbEnteteError += nbEnteteToAdd;
                        nbLineFactureError += nbLineFactureToAdd;
                    }

                    enteteFacture = null;
                    cotisationAffilie.clear();
                    nbEnteteToAdd = 0;
                    nbLineFactureToAdd = 0;

                    // Sortie si erreur fatale
                    if (getMemoryLog().isOnFatalLevel()) {
                        return false;
                    }
                } // end if

                calculCotisation = AFProcessFacturationCAPCGAS.calculerCotisation(this, donneesFacturation,
                        anneeFacturation, moisFacturation, isFacturerParitaire(), isFacturerPersonnel(),
                        isFacturerCAP_CGAS(), cotisationAffilie, getIdModuleFacturationCAPCGAS());
                nbException += calculCotisation.getNbException();

                // recherche du rôle pour la coti en cours
                String roleCoti = CodeSystem.ROLE_AFFILIE;
                if (CodeSystem.GENRE_ASS_PERSONNEL.equals(donneesFacturation.getGenreAssurance())) {
                    // assurance personnelle
                    roleCoti = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(
                            getSession().getApplication());
                }
                if (CodeSystem.GENRE_ASS_PARITAIRE.equals(donneesFacturation.getGenreAssurance())) {
                    // assurance personnelle
                    roleCoti = CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                            getSession().getApplication());
                }

                affilieNumero = calculCotisation.getAffilieNumero();

                // ******************************************************************
                // Insertion de l'en-tete facture, Si un nouveau tier
                // ******************************************************************
                if ((previousKey == null)
                        || ((previousKey != null) && !previousKey.equals(donneesFacturation.getIdTiers() + ","
                                + donneesFacturation.getIdPlanAffiliation()))) {

                    // on vérifie que l'entete n'existe pas déjà ou cet
                    // affilié
                    // clé unique: idPassage, idRole, idExterneRole,
                    // idTypeFacture, idExterneFacture
                    enteteFacture = new FAEnteteFacture();

                    FAEnteteFactureManager entete = new FAEnteteFactureManager();
                    entete.setSession(getSession());
                    entete.setForIdPassage(idPassage);
                    // recherche des en-tête de la même période

                    entete.setLikeIdExterneFacture(calculCotisation.getIdExterneFacture().substring(0, 6));
                    entete.setForIdExterneRole(calculCotisation.getAffilieNumero());
                    entete.setForIdTiers(donneesFacturation.getIdTiers());
                    // entete.setForIdRole(roleCoti);
                    entete.find();
                    boolean enteteFound = false;
                    int idFacturationExt = 0;
                    // recherche sur les en-tête déjà existantes
                    for (int iEntete = 0; (iEntete < entete.size()) && !enteteFound; iEntete++) {
                        FAEnteteFacture ef = (FAEnteteFacture) entete.getEntity(iEntete);
                        // recherche et sauvegarde du dernier id externe
                        // facturation
                        int idFact = Integer.parseInt(ef.getIdExterneFacture());
                        if (idFacturationExt < idFact) {
                            idFacturationExt = idFact;
                        }
                        if (roleCoti.equals(ef.getIdRole())
                                && donneesFacturation.getLibelleFacture().equals(ef.getLibelle())) {
                            // il existe une en-tête avec le bon rôle et les
                            // mêmes domaines -> l'utiliser
                            enteteFound = true;
                            enteteFacture.setSession(getSession());
                            enteteFacture.setIdEntete(ef.getIdEntete());
                            enteteFacture.retrieve();
                            if (donneesFacturation.getIsRentier().booleanValue()) {
                                // si retraite, le renseigner dans le
                                // décompte
                                enteteFacture.setEstRentierNa(donneesFacturation.getIsRentier());
                                enteteFacture.update(getTransaction());
                            }
                        }
                    } // for
                    if (!enteteFound) {
                        // n'existe pas --> on ajoute l'entete de facture
                        enteteFacture.setIdTiers(donneesFacturation.getIdTiers());
                        enteteFacture.setISession(getSession());
                        enteteFacture.setIdPassage(idPassage);
                        enteteFacture.setIdTypeFacture("88");
                        enteteFacture.setIdRole(roleCoti);
                        enteteFacture.setIdExterneRole(calculCotisation.getAffilieNumero());
                        if (idFacturationExt == 0) {
                            // aucune entête trouvée précédemment
                            enteteFacture.setIdExterneFacture(calculCotisation.getIdExterneFacture());
                        } else {
                            // incrémenter l'id externe facturation de la
                            // dernière en-tête trouvée
                            enteteFacture.setIdExterneFacture(String.valueOf(idFacturationExt + 1));
                        }
                        enteteFacture.setIdSousType(calculCotisation.getIdSousTypeFacture());
                        enteteFacture.setNonImprimable(Boolean.FALSE);
                        enteteFacture.setIdSoumisInteretsMoratoires(CodeSystem.INTERET_MORATOIRE_AUTOMATIQUE);
                        enteteFacture.setIdModeRecouvrement(CodeSystem.MODE_RECOUV_AUTOMATIQUE);
                        enteteFacture.setIdDomaineCourrier(donneesFacturation.getDomaineCourrier());
                        enteteFacture.setIdDomaineLSV(donneesFacturation.getDomaineRecouvrement());
                        enteteFacture.setIdDomaineRemboursement(donneesFacturation.getDomaineRemboursement());
                        enteteFacture.setNonImprimable(donneesFacturation.getBlocageEnvoi());
                        enteteFacture.setLibelle(donneesFacturation.getLibelleFacture());
                        // si retraite, le renseigner dans le décompte
                        enteteFacture.setEstRentierNa(donneesFacturation.getIsRentier());
                        enteteFacture.add(getTransaction());
                        nbEnteteToAdd++;
                    }
                }

                // ******************************************************************
                // Creation de(s) ligne(s) de facturation
                // ******************************************************************
                for (int i = calculCotisation.getMontantList().size() - 1; i >= 0; i--) {
                    LineFacturation aLine = calculCotisation.getMontantList().get(i);
                    cotisationAffilie.add(aLine);
                }

                idAffiliation = donneesFacturation.getAffiliationId();
                previousKey = donneesFacturation.getIdTiers() + "," + donneesFacturation.getIdPlanAffiliation();

                nbCotisation++;

            }

            if (enteteFacture != null) {
                for (int i = 0; i < cotisationAffilie.size(); i++) {
                    LineFacturation aLine = cotisationAffilie.get(i);

                    int nbMoisAFacturer = AFUtil.nbMoisPeriode(getSession(), aLine.getDebutPeriode(),
                            aLine.getFinPeriode());

                    AFAssurance theAssurance = loadAssurance(aLine.getAssuranceId());

                    if (isAssuranceCAPWithAF(theAssurance)) {
                        double montantAFAFacturer = getMontantAFAFacturer(anneeFacturation, idAffiliation,
                                affilieNumero, nbMoisAFacturer, aLine.getDebutPeriode(), aLine.getFinPeriode());

                        // Comme l'allocation familiale se trouve dans un afact négatif séparé
                        // Il faut augmenter l'afact de cotisation avec la valeur de l'allocation familiale
                        aLine.setMontant(aLine.getMontant() + montantAFAFacturer);

                        if (montantAFAFacturer != 0) {
                            nbLineFactureToAdd = createAfactAFCAP(idPassage, enteteFacture.getIdEntete(), aLine,
                                    montantAFAFacturer, calculCotisation.getIdModuleFacturation(), nbLineFactureToAdd,
                                    rubriqueAllocationEnfantCAP, calculCotisation.getAffilieNumero(),
                                    calculCotisation.getPeriodiciteAff());
                        }
                    }

                    if (aLine.getMontant() != 0) {
                        FAAfact lineFacture = new FAAfact();
                        lineFacture.setISession(getSession());
                        lineFacture.setIdEnteteFacture(enteteFacture.getIdEntete());
                        lineFacture.setIdPassage(idPassage);
                        lineFacture.setIdModuleFacturation(calculCotisation.getIdModuleFacturation());
                        lineFacture.setIdTypeAfact(CodeSystem.TYPE_FACT_FACT_STANDARD);
                        lineFacture.setNonImprimable(Boolean.FALSE);
                        lineFacture.setNonComptabilisable(Boolean.FALSE);
                        lineFacture.setAQuittancer(Boolean.FALSE);
                        lineFacture.setAnneeCotisation(aLine.getAnneeCotisation());
                        lineFacture.setLibelle(aLine.getLibelle());
                        lineFacture.setIdRubrique(aLine.getIdRubrique());
                        lineFacture.setDebutPeriode(aLine.getDebutPeriode());
                        lineFacture.setFinPeriode(aLine.getFinPeriode());
                        lineFacture.setMasseFacture(Double.toString(JANumberFormatter.round(aLine.getMasse(), 0.05, 2,
                                JANumberFormatter.NEAR)));
                        lineFacture.setMontantFacture(Double.toString(JANumberFormatter.round(aLine.getMontant(), 0.05,
                                2, JANumberFormatter.NEAR)));
                        lineFacture.setTauxFacture(Double.toString(aLine.getTaux()));
                        // ajout de la caisse métier
                        String caisseMetier = aLine.getCaisse();
                        if (caisseMetier == null) {
                            caisseMetier = "";
                        }
                        lineFacture.setNumCaisse(caisseMetier);
                        // indication si taux à cacher
                        lineFacture.setAffichtaux(new Boolean(!aLine.isTauxCache()));
                        lineFacture.add(getTransaction());
                        nbLineFactureToAdd++;
                    }

                }
            }

            // Validation finale
            if (!getTransaction().hasErrors()) {
                getTransaction().commit();

                nbEnteteOK += nbEnteteToAdd;
                nbLineFactureOK += nbLineFactureToAdd;
            } else {
                rollbackTransaction(getSession().getLabel("691") + calculCotisation.getAffilieNumero());
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());

                nbEnteteError += nbEnteteToAdd;
                nbLineFactureError += nbLineFactureToAdd;
            }
            // Sortie si erreur fatale
            if (getMemoryLog().isOnFatalLevel()) {
                return false;
            }
            getMemoryLog().logMessage("------------------------", FWMessage.INFORMATION, this.getClass().getName());
            getMemoryLog().logMessage("Cotisation handeled    : " + nbCotisation, FWMessage.INFORMATION,
                    this.getClass().getName());
            getMemoryLog().logMessage("------------------------", FWMessage.INFORMATION, this.getClass().getName());
            getMemoryLog().logMessage("Nb HeaderFacture OK    : " + nbEnteteOK, FWMessage.INFORMATION,
                    this.getClass().getName());
            getMemoryLog().logMessage("Nb LineFacture   OK    : " + nbLineFactureOK, FWMessage.INFORMATION,
                    this.getClass().getName());
            getMemoryLog().logMessage("Nb Exception           : " + nbException, FWMessage.INFORMATION,
                    this.getClass().getName());
            getMemoryLog().logMessage("Nb Sum = 0             : " + nbMontantZero, FWMessage.INFORMATION,
                    this.getClass().getName());
            getMemoryLog().logMessage("Nb HeaderFacture Error : " + nbEnteteError, FWMessage.INFORMATION,
                    this.getClass().getName());
            getMemoryLog().logMessage("Nb LineFacture   Error : " + nbLineFactureError, FWMessage.INFORMATION,
                    this.getClass().getName());
        } catch (Exception e) {
            // Fait remonter l'erreur au processus parent
            getTransaction().addErrors(e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            JadeLogger.error(this, e);

            try {
                getTransaction().rollback();
            } catch (Exception f) {
                getMemoryLog().logMessage(f.getMessage(), FWMessage.FATAL, this.getClass().getName());
                JadeLogger.error(this, f);
            }
            return false;
        } finally {
            try {
                if (statement != null) {
                    try {
                        manager.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
            } catch (Exception g) {
                getMemoryLog().logMessage(g.getMessage(), FWMessage.FATAL, this.getClass().getName());
                JadeLogger.error(this, g);
            } finally {
                if ((cursorTransaction != null) && (cursorTransaction.isOpened())) {
                    try {
                        cursorTransaction.closeTransaction();
                    } catch (Exception e) {
                        JadeLogger.error(this, e);
                    }
                }
            }
        }
        getMemoryLog().logMessage("Process Terminated", FWMessage.INFORMATION, this.getClass().getName());
        return !isOnError();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        // Déterminer l'objet du message en fonction du code erreur
        String obj = "";

        /*
         * if (getMemoryLog().hasErrors()) obj = FWMessage.getMessageFromId("5031")+ " " + getIdPassage(); else obj =
         * FWMessage.getMessageFromId("5030")+ " " + getIdDecision();
         */
        // Restituer l'objet
        return obj;
    }

    public String getIdModuleFacturationCAPCGAS() {
        return idModuleFacturationCAPCGAS;
    }

    private double getMontantAFAFacturer(String anneeFacturation, String idAffiliation, String affilieNumero,
            int nbMoisAFacturer, String debutPeriode, String finPeriode) throws Exception {

        try {

            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            DecisionCAPService decisionCAPService = AurigaServiceLocator.getDecisionCAPService();

            DecisionCAPSearchModel decisionCAPSearchModel = new DecisionCAPSearchModel();
            decisionCAPSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            decisionCAPSearchModel.setForIdAffiliation(idAffiliation);
            decisionCAPSearchModel.setForAnnee(anneeFacturation);
            decisionCAPSearchModel.setInEtat(AUDecisionEtat.getListEtatActif());

            decisionCAPSearchModel = decisionCAPService.search(decisionCAPSearchModel);

            SimpleDecisionCAP decisionCAP = null;
            for (JadeAbstractModel abstractModel : decisionCAPSearchModel.getSearchResults()) {
                decisionCAP = (SimpleDecisionCAP) abstractModel;

                if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), decisionCAP.getDateDebut(), debutPeriode)
                        && BSessionUtil
                                .compareDateFirstLowerOrEqual(getSession(), finPeriode, decisionCAP.getDateFin())) {
                    break;
                }

            }

            if (decisionCAP == null) {
                throw new Exception("Aucune décision pour l'année " + anneeFacturation
                        + " et l'affiliation suivante : " + affilieNumero + " (id affiliation : " + idAffiliation + ")");
            }

            double montantAFAFacturer = decisionCAPService.getMontantAFAFacturer(decisionCAP, nbMoisAFacturer);

            return montantAFAFacturer;

        } finally {

            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());

        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.05.2003 11:48:36)
     * 
     * @return globaz.musca.api.IFAPassage
     */
    public globaz.musca.api.IFAPassage getPassage() {
        return passage;
    }

    private boolean hasSousEnsembleAffilies(String idPassage) {
        try {
            String sql = "FROM " + TIToolBox.getCollection() + "FAFIAFP";
            sql += " WHERE " + TIToolBox.getCollection() + "FAFIAFP.IDPASSAGE = " + idPassage;
            int count = (int) TISQL.count(getSession(), sql);
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isAssuranceCAPWithAF(AFAssurance theAssurance) throws Exception {

        return CodeSystem.TYPE_ASS_CAP_10.equalsIgnoreCase(theAssurance.getTypeAssurance())
                || CodeSystem.TYPE_ASS_CAP_20.equalsIgnoreCase(theAssurance.getTypeAssurance());

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 14:44:20)
     * 
     * @return boolean
     */
    public boolean isFacturerCAP_CGAS() {
        return facturerCAP_CGAS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 14:42:58)
     * 
     * @return boolean
     */
    public boolean isFacturerParitaire() {
        return facturerParitaire;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 14:44:20)
     * 
     * @return boolean
     */
    public boolean isFacturerPersonnel() {
        return facturerPersonnel;
    }

    /**
     * Renvoie la Job Queue à utiliser pour soumettre le process.
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private AFAssurance loadAssurance(String idAssurance) throws Exception {
        AFAssurance theAssurance = new AFAssurance();
        theAssurance.setSession(getSession());
        theAssurance.setAssuranceId(idAssurance);
        theAssurance.retrieve(getTransaction());

        return theAssurance;

    }

    private CARubrique loadRubriqueAllocationEnfantCAP(String anneeFacturation) throws Exception {
        try {

            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            ParametreService parametreService = AurigaServiceLocator.getParametreService();
            String numeroRubriqueAllocationEnfantCAP = parametreService.getValeurAlpha(
                    AUParametrePlageValeur.RUBRIQUE_ALLOCATION_ENFANT, "01.01." + anneeFacturation,
                    AUApplication.DEFAULT_APPLICATION_AURIGA);

            CARubrique rubriqueAllocationEnfantCAP = new CARubrique();
            rubriqueAllocationEnfantCAP.setSession(getSession());
            rubriqueAllocationEnfantCAP.setIdExterne(numeroRubriqueAllocationEnfantCAP);
            rubriqueAllocationEnfantCAP.setAlternateKey(APIRubrique.AK_IDEXTERNE);
            rubriqueAllocationEnfantCAP.retrieve(getTransaction());

            return rubriqueAllocationEnfantCAP;

        } finally {

            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());

        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.06.2003 13:59:19)
     * 
     * @param message
     *            java.lang.String
     */
    private void rollbackTransaction(String message) {
        // Logger l'erreur
        getMemoryLog().logMessage(message, FWMessage.ERREUR, this.getClass().getName());
        // Logger les messages d'erreur de la transaction
        getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
        try {
            getTransaction().rollback();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            JadeLogger.error(this, e);
        }
    }

    public void setFacturerCAP_CGAS(boolean newFactureCAP_CGAS) {
        facturerCAP_CGAS = newFactureCAP_CGAS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 14:42:58)
     * 
     * @param newFacturerParitaire
     *            boolean
     */
    public void setFacturerParitaire(boolean newFacturerParitaire) {
        facturerParitaire = newFacturerParitaire;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 14:44:20)
     * 
     * @param newFacturerPersonnel
     *            boolean
     */
    public void setFacturerPersonnel(boolean newFacturerPersonnel) {
        facturerPersonnel = newFacturerPersonnel;
    }

    public void setIdModuleFacturationCAPCGAS(String idModuleFacturationCAPCGAS) {
        this.idModuleFacturationCAPCGAS = idModuleFacturationCAPCGAS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.05.2003 11:48:36)
     * 
     * @param newPassage
     *            globaz.musca.api.IFAPassage
     */
    public void setPassage(globaz.musca.api.IFAPassage newPassage) {
        passage = newPassage;
    }

}
