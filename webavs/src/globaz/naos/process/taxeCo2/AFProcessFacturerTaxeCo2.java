package globaz.naos.process.taxeCo2;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.api.IFAPassage;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.taxeCo2.AFTaxeCo2;
import globaz.naos.db.taxeCo2.AFTaxeCo2Manager;
import globaz.naos.translation.CodeSystem;
import java.math.BigDecimal;

/**
 * Process pour la facturation des Cotisations Personnelles, Paritaires
 * 
 * @author: mmu, sau
 */

abstract class AFProcessFacturerTaxeCo2 extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected String anneeFacturation = null;
    // protected String anneeFacturationTaxe = null;
    protected String dateFacture = null;
    protected AFTaxeCo2 dernieresDonnees = null;
    protected AFTaxeCo2 donneesFacturation = null;

    // protected CalculCotisation calculCotisation = null;
    protected FAEnteteFacture enteteFacture = null;
    protected String idModuleFacturation = null;
    protected String idPassage = null;
    protected AFTaxeCo2Manager manager = null;
    protected String moisFacturation = null;
    protected FAApplication muscaApp = null;
    protected BSession muscaSession = null;

    protected BSession naosSession = null;
    protected IFAPassage passage = null;
    protected String roleCoti = null;

    /**
     * Constructeur de AFProcessFacturation.
     */
    public AFProcessFacturerTaxeCo2() {
        super();
    }

    /**
     * Constructeur de AFProcessFacturation.
     * 
     * @param parent
     *            globaz.framework.process.FWProcess
     */
    public AFProcessFacturerTaxeCo2(BProcess parent) {
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

    /**
     * Traitement de l'affiliation Création de la facturation périodique, personnel, paritaire ou annuel au 30 juin.
     * -------------------------------- ---------------------------------------------------- Ne prendre que les
     * affiliations concernées par le passage et qui n'ont pas été traitées
     * 
     * @param passage
     * @return
     */
    private boolean facturer(globaz.musca.api.IFAPassage passage) {

        getMemoryLog().logMessage("Process Started", FWMessage.INFORMATION, this.getClass().getName());
        AFTaxeCo2Manager manager = null;
        BTransaction cursorTransaction = null;
        BStatement statement = null;

        try {
            muscaSession = getSession(); // (BSession)passage.getISession();
            muscaApp = (FAApplication) muscaSession.getApplication();
            naosSession = (BSession) muscaApp.getSessionNaos(muscaSession);

            manager = new AFTaxeCo2Manager();
            manager.setISession(naosSession);

            idPassage = passage.getIdPassage();
            dateFacture = passage.getDateFacturation();
            moisFacturation = passage.getDateFacturation().substring(3, 5);
            anneeFacturation = dateFacture.substring(6);
            // this.anneeFacturationTaxe = "" + (JadeStringUtil.toInt(this.anneeFacturation) - 1);
            boolean addError = false;

            roleCoti = CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(getSession().getApplication());

            if (isMoisFacturationOk(moisFacturation)) {

                // ************************************************************
                // Initialisation des paramètres pour la recherche des
                // Cotisations
                // ************************************************************

                manager.setForAnneeRedistri(anneeFacturation);
                manager.setForEtatTaxe(CodeSystem.ETAT_TAXE_CO2_A_TRAITER);

                // ************************************************************
                // Création du cursorTransaction
                // ************************************************************
                cursorTransaction = (BTransaction) getSession().newTransaction();
                cursorTransaction.openTransaction();
                statement = manager.cursorOpen(cursorTransaction);

                // ************************************************************
                // Parcourir toutes les Cotisations a facturer
                // ************************************************************
                donneesFacturation = null;
                dernieresDonnees = null;
                // calculCotisation = null;
                enteteFacture = null;
                int progressCounter = manager.getCount();
                int cpt = 0;
                idModuleFacturation = getIdModule();
                FAAfact lineFacture = null;

                // ****************************************************************
                // POUR CHAQUE COTISATION
                // ****************************************************************
                while (((donneesFacturation = (AFTaxeCo2) manager.cursorReadNext(statement)) != null) && !addError) {
                    cpt++;
                    setProgressDescription(donneesFacturation.getNumAffilie() + " <br>" + cpt + "/" + progressCounter
                            + "<br>");
                    if (isAborted()) {
                        setProgressDescription("Traitement interrompu<br> sur l'affilié : "
                                + donneesFacturation.getNumAffilie() + " <br>" + cpt + "/" + progressCounter + "<br>");
                        if ((getParent() != null) && getParent().isAborted()) {
                            getParent().setProcessDescription(
                                    "Traitement interrompu<br> sur l'affilié : " + donneesFacturation.getNumAffilie()
                                            + " <br>" + cpt + "/" + progressCounter + "<br>");
                        }
                        break;
                    } else {
                        if (tenirCompteMontantMinime(donneesFacturation, anneeFacturation)) {
                            donneesFacturation.setEtat(CodeSystem.ETAT_TAXE_CO2_ABANDONNE);
                            donneesFacturation.update(getTransaction());
                        } else {
                            // calculCotisation = calculerCotisation(this,
                            // donneesFacturation, anneeFacturation);
                            enteteFacture = getEnteteFacture(getSession());
                            if (enteteFacture != null) {
                                if ((dernieresDonnees != null)
                                        && dernieresDonnees.getNumAffilie().equals(donneesFacturation.getNumAffilie())) {
                                    miseAJourAfact(lineFacture, enteteFacture);
                                } else {
                                    lineFacture = new FAAfact();
                                    lineFacture.setMasseFacture(donneesFacturation.getMasse());
                                    lineFacture.setISession(getSession());
                                    lineFacture.setIdEnteteFacture(enteteFacture.getIdEntete());
                                    lineFacture.setIdPassage(idPassage);
                                    lineFacture.setIdModuleFacturation(idModuleFacturation);
                                    lineFacture.setIdTypeAfact(CodeSystem.TYPE_FACT_FACT_STANDARD);
                                    lineFacture.setNonImprimable(Boolean.FALSE);
                                    lineFacture.setNonComptabilisable(Boolean.FALSE);
                                    lineFacture.setAQuittancer(Boolean.FALSE);
                                    lineFacture.setAnneeCotisation(anneeFacturation);
                                    lineFacture.setLibelle(donneesFacturation.getAssuranceTaxeCo2()
                                            .getAssuranceLibelle(donneesFacturation.getTiers().getLangueIso()));
                                    lineFacture.setIdRubrique(donneesFacturation.getAssuranceTaxeCo2().getRubriqueId());

                                    // Si on a saisie un taux forcé dans l'écran des
                                    // taxes CO2 pendre celui-là.
                                    if (JadeStringUtil.isBlankOrZero(donneesFacturation.getTauxForce())) {
                                        lineFacture.setTauxFacture(donneesFacturation.getAssuranceTaxeCo2()
                                                .getTaux("0101" + anneeFacturation).getTauxSansFraction());
                                    } else {
                                        lineFacture.setTauxFacture(donneesFacturation.getTauxForce());
                                    }
                                    // ajout de la caisse métier
                                    lineFacture.setNumCaisse(AFAffiliationUtil.getIdCaissePrincipale(
                                            donneesFacturation.getAffiliation(), true, anneeFacturation));

                                    lineFacture.add(getTransaction());
                                }

                                // Mise à jour de la taxe.
                                donneesFacturation.setEtat(CodeSystem.ETAT_TAXE_CO2_FACTURE);
                                donneesFacturation.setIdPassage(idPassage);
                                donneesFacturation.setIdEnteteFacture(enteteFacture.getIdEntete());
                                donneesFacturation.update(getTransaction());
                            } else {
                                continue;
                            }
                        }
                    }
                    dernieresDonnees = donneesFacturation;
                } // end while

                // Validation finale
                if (!getTransaction().hasErrors()) {
                    getTransaction().commit();

                } else {
                    rollbackTransaction(getSession().getLabel("691") + donneesFacturation.getNumAffilie());
                    getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
                }
                // Sortie si erreur fatale
                if (getMemoryLog().isOnFatalLevel()) {
                    return false;
                }
            }
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
                        cursorTransaction.commit();
                    } finally {
                        statement.closeStatement();
                    }
                }
            } catch (Exception g) {
                getMemoryLog().logMessage(g.getMessage(), FWMessage.FATAL, this.getClass().getName());
                JadeLogger.error(this, g);
            }
            if ((cursorTransaction != null) && (cursorTransaction.isOpened())) {
                try {
                    cursorTransaction.closeTransaction();
                } catch (Exception e) {
                    JadeLogger.error(this, e);
                }
            }
        }
        getMemoryLog().logMessage("Process Terminated", FWMessage.INFORMATION, this.getClass().getName());
        return !isOnError();
    }

    public String getAnneeFacturation() {
        return anneeFacturation;
    }

    public String getDateFacture() {
        return dateFacture;
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

    // /**
    // * Calcul une cotisation à partir d'une ligne du AFProcessFactureManager
    // * <br>
    // *
    // * @param process process appellant la méthode: process naos ou musca
    // * @param donneeFacturation entity retourné par
    // AFProcessFacturationViewBean
    // * @param anneeFacturation
    // * @param moisFacturation
    // * @param isParitaire true is on veut calculer une paritaire
    // * @param isPersonel true is on veut calculer une Personelle
    // * @param cotisationAffilie liste de cotisation déjà calculées pour un
    // affilié
    // */
    // public static final CalculCotisation calculerCotisation(BProcess process,
    // AFTaxeCo2 donneesFacturation, String anneeFacturation) throws Exception {
    //
    // String periodiciteAff = null;
    // String idExterneFacture = null;
    // String idSousTypeFacture = null;
    // String noTrimestre = null;
    //
    // periodiciteAff = donneesFacturation.getPeriodicite();
    //
    // if (periodiciteAff.equals(CodeSystem.PERIODICITE_MENSUELLE)) {
    // idExterneFacture = anneeFacturation + "06000";
    // idSousTypeFacture = "227006";
    //
    // } else if (periodiciteAff.equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
    // noTrimestre = "2";
    // idExterneFacture = anneeFacturation + "4" + noTrimestre + "000";
    // idSousTypeFacture = "22704" + noTrimestre;
    //
    // } else if (periodiciteAff.equals(CodeSystem.PERIODICITE_ANNUELLE)){
    //
    // noTrimestre = "6";
    // idExterneFacture = anneeFacturation + "4" + noTrimestre + "000";
    // idSousTypeFacture = "22704" + noTrimestre;
    // }
    //
    // CalculCotisation calcul = new CalculCotisation();
    //
    // calcul.setPeriodiciteAff(periodiciteAff);
    // calcul.setIdExterneFacture(idExterneFacture);
    // calcul.setIdSousTypeFacture(idSousTypeFacture);
    //
    // return calcul;
    // }

    abstract FAEnteteFacture getEnteteFacture(BSession session) throws Exception;

    abstract String getIdModule();

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public FAApplication getMuscaApp() {
        return muscaApp;
    }

    public BSession getMuscaSession() {
        return muscaSession;
    }

    public BSession getNaosSession() {
        return naosSession;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.05.2003 11:48:36)
     * 
     * @return globaz.musca.api.IFAPassage
     */
    public globaz.musca.api.IFAPassage getPassage() {
        return passage;
    }

    public String getRoleCoti() {
        return roleCoti;
    }

    // public String getMoisFacturation() {
    // return moisFacturation;
    // }
    //
    // public void setMoisFacturation(String moisFacturation) {
    // this.moisFacturation = moisFacturation;
    // }

    abstract boolean isMoisFacturationOk(String moisFacturation) throws Exception;

    /**
     * Renvoie la Job Queue à utiliser pour soumettre le process.
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private void miseAJourAfact(FAAfact lineFacture, FAEnteteFacture entete) {

        BigDecimal tauxFacture = JAUtil.createBigDecimal(lineFacture.getTauxFacture());
        lineFacture.setMasseFacture(""
                + (JadeStringUtil.toDouble(JANumberFormatter.deQuote(lineFacture.getMasseFacture())) + JadeStringUtil
                        .toDouble(JANumberFormatter.deQuote(donneesFacturation.getMasse()))));
        double montantFacture = JANumberFormatter.round(
                (new FWCurrency(lineFacture.getMasseFacture()).doubleValue() * tauxFacture.doubleValue()) / 100, 0.05,
                2, JANumberFormatter.NEAR);
        lineFacture.setMontantFacture(JANumberFormatter.formatNoQuote(montantFacture));
        try {
            lineFacture.update(getTransaction());
            entete.updateTotal(getTransaction(), lineFacture);
        } catch (Exception e) {
            // Fait remonter l'erreur au processus parent
            getTransaction().addErrors(e.getMessage());
            getMemoryLog().logMessage(
                    e.getMessage() + "Impossible de regrouper les lignes de taxe CO2 pour l'affilié : "
                            + enteteFacture.getIdExterneRole(), FWMessage.FATAL, this.getClass().getName());
            JadeLogger.error(this, e);
            try {
                getTransaction().rollback();
            } catch (Exception f) {
                getMemoryLog().logMessage(f.getMessage(), FWMessage.FATAL, this.getClass().getName());
                JadeLogger.error(this, f);
            }
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

    public void setAnneeFacturation(String anneeFacturation) {
        this.anneeFacturation = anneeFacturation;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }

    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public void setMuscaApp(FAApplication muscaApp) {
        this.muscaApp = muscaApp;
    }

    public void setMuscaSession(BSession muscaSession) {
        this.muscaSession = muscaSession;
    }

    public void setNaosSession(BSession naosSession) {
        this.naosSession = naosSession;
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

    public void setRoleCoti(String roleCoti) {
        this.roleCoti = roleCoti;
    }

    abstract Boolean tenirCompteMontantMinime(AFTaxeCo2 donneesFacturation, String anneeFacturation) throws Exception;

    // /**
    // * Classe contenant les données issues d'un calcul de cotisation.
    // * Permet d'enregistrer les valeurs de facturation avant insertion dans la
    // BD
    // *
    // * @author mmu
    // */
    // public static class CalculCotisation {
    // private String affilieNumero = null;
    // private String periodiciteAff = null;
    // private String idSousTypeFacture = null;
    // private String idExterneFacture = null;
    //
    //
    // public CalculCotisation() {
    // super();
    // }
    //
    // public String getAffilieNumero() {
    // return affilieNumero;
    // }
    //
    // public void setAffilieNumero(String string) {
    // affilieNumero = string;
    // }
    //
    // public String getPeriodiciteAff() {
    // return periodiciteAff;
    // }
    //
    // public void setPeriodiciteAff(String string) {
    // periodiciteAff = string;
    // }
    //
    // public String getIdExterneFacture() {
    // return idExterneFacture;
    // }
    //
    // public String getIdSousTypeFacture() {
    // return idSousTypeFacture;
    // }
    //
    // public void setIdExterneFacture(String string) {
    // idExterneFacture = string;
    // }
    //
    // public void setIdSousTypeFacture(String string) {
    // idSousTypeFacture = string;
    // }
    // }
}
