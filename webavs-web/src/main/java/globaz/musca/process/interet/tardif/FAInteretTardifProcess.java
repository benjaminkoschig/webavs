package globaz.musca.process.interet.tardif;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.interets.CADetailInteretMoratoire;
import globaz.osiris.db.interets.CADetailInteretMoratoireManager;
import globaz.osiris.db.interets.CAGenreInteret;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.osiris.utils.CAUtil;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.ArrayList;

public class FAInteretTardifProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idModuleFacturation = "";
    private FAPassage passage = null;

    /**
     * Commentaire relatif au constructeur CAProcessFacturationInteretTardif.
     */
    public FAInteretTardifProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CAProcessFacturationInteretTardif.
     * 
     * @param parent
     *            BProcess
     */
    public FAInteretTardifProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Cette méthode exécute le processus de facturation des intérêts moratoires tardifs. Date de création : (14.02.2002
     * 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        BStatement statement = null;
        CAInteretMoratoireManager manager = new CAInteretMoratoireManager();

        try {
            BSession osirisSession = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                    .newSession(getSession());
            BSession pyxisSession = (BSession) GlobazSystem.getApplication(TIApplication.DEFAULT_APPLICATION_PYXIS)
                    .newSession(getSession());

            if (!initialiseDecision(osirisSession)) {
                return false;
            }

            initInteretMoratoireManager(osirisSession, manager);

            initProgressScaleValue(manager);

            long progressCounter = -1;

            CAInteretMoratoire interetMoratoire = null;

            statement = manager.cursorOpen(getTransaction());

            while ((interetMoratoire = (CAInteretMoratoire) manager.cursorReadNext(statement)) != null) {
                getParent().setProgressCounter(progressCounter++);
                try {
                    // Contrôler que l'intérêt est soumis
                    if (interetMoratoire.isSoumis() || interetMoratoire.isManuel()) {
                        interetMoratoire.setSession(osirisSession);

                        if ((interetMoratoire.getSection() == null) || interetMoratoire.getSection().isNew()) {
                            getMemoryLog().logMessage(
                                    osirisSession.getLabel("5126") + " " + interetMoratoire.getIdSection(),
                                    FWMessage.ERREUR, this.getClass().getName());
                        } else {
                            String newIdExterne;

                            if (!JadeStringUtil.isIntegerEmpty(interetMoratoire.getNumeroFactureGroupe())) {
                                newIdExterne = interetMoratoire.getNumeroFactureGroupe();
                            } else {
                                // On change le numéro de la section pour
                                // l'entête de facture en passant
                                // la 3ème avant dernière position de 0 --> 9
                                // ... si une section de ce type existe déjà
                                // incrémenter de 1

                                newIdExterne = CAUtil.creerNumeroSectionUniquePourInteretMoratoire(osirisSession,
                                        getTransaction(), interetMoratoire.getCompteAnnexe().getIdRole(),
                                        interetMoratoire.getCompteAnnexe().getIdExterneRole(), "1",
                                        "" + JACalendar.getYear(interetMoratoire.getSection().getDateFinPeriode()),
                                        interetMoratoire.getSection().getCategorieSection(), interetMoratoire
                                                .getSection().getIdExterne());
                            }

                            // Mise à jour des tables
                            if (updateTables(osirisSession, pyxisSession, interetMoratoire, newIdExterne)) {
                                // Validation finale
                                if (getTransaction().hasErrors()) {
                                    rollbackTransaction(osirisSession.getLabel("7367")
                                            + interetMoratoire.getIdInteretMoratoire());
                                    getMemoryLog().logStringBuffer(getTransaction().getErrors(),
                                            this.getClass().getName());
                                } else {
                                    getTransaction().commit();
                                }

                                // Sortie si erreur fatale
                                if (getMemoryLog().isOnFatalLevel()) {
                                    return false;
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                    this._addError(e.getMessage() + " Erreur lors de la création d'un intérêt tardif pour l'affilié : "
                            + interetMoratoire.getIdExterneRoleEcran());
                    getMemoryLog().logMessage(
                            e.getMessage() + " Erreur lors de la création d'un intérêt tardif pour l'affilié : "
                                    + interetMoratoire.getIdExterneRoleEcran(), FWMessage.FATAL,
                            this.getClass().getName());
                }
            }

        } catch (Exception e) {
            this._addError(e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());

            try {
                getTransaction().rollback();
            } catch (Exception f) {
                getMemoryLog().logMessage(f.getMessage(), FWMessage.FATAL, this.getClass().getName());
            } finally {
                try {
                    if (statement != null) {
                        manager.cursorClose(statement);
                        statement = null;
                    }
                } catch (Exception g) {
                    getMemoryLog().logMessage(g.getMessage(), FWMessage.FATAL, this.getClass().getName());
                }
            }

            return false;
        }

        return !isOnError();
    }

    /**
     * Si aucun en-tête trouvé, création d'un nouvel en-tête de facture.
     * 
     * @param osirisSession
     * @param pyxisSession
     * @param interetMoratoire
     * @param idExterneFacture
     * @return
     * @throws Exception
     */
    private FAEnteteFacture createNewEnteteFacture(BSession osirisSession, BSession pyxisSession,
            CAInteretMoratoire interetMoratoire, String idExterneFacture) throws Exception {
        FAEnteteFacture nouveauEntFacture = new FAEnteteFacture();
        nouveauEntFacture.setISession(getSession());
        nouveauEntFacture.setIdPassage(getPassage().getIdPassage());
        nouveauEntFacture.setIdTiers(interetMoratoire.getSection().getCompteAnnexe().getIdTiers());

        nouveauEntFacture.setIdExterneRole(interetMoratoire.getSection().getCompteAnnexe().getIdExterneRole());
        nouveauEntFacture.setIdRole(interetMoratoire.getSection().getCompteAnnexe().getIdRole());
        nouveauEntFacture.setIdTypeFacture(APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION);
        nouveauEntFacture.setIdExterneFacture(idExterneFacture);

        nouveauEntFacture.setIdTypeCourrier(interetMoratoire.getSection().getTypeAdresse());
        nouveauEntFacture.setIdDomaineCourrier(interetMoratoire.getSection().getDomaine());

        // DGI init plan
        nouveauEntFacture.initDefaultPlanValue(interetMoratoire.getSection().getCompteAnnexe().getIdRole());
        nouveauEntFacture.add(getTransaction());

        return nouveauEntFacture;
    }

    /**
     * Cette méthode détermine l'objet du message en fonction du code d'erreur
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        return "";
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    /**
     * Return le montant total de l'intérêt moratoire.
     * 
     * @param osirisSession
     * @param interetMoratoire
     * @return
     * @throws Exception
     */
    private FWCurrency getMontantTotalInteretMoratoire(BSession osirisSession, CAInteretMoratoire interetMoratoire)
            throws Exception {
        CADetailInteretMoratoireManager detailInteretMoratoireManager = new CADetailInteretMoratoireManager();
        detailInteretMoratoireManager.setForIdInteretMoratoire(interetMoratoire.getIdInteretMoratoire());
        detailInteretMoratoireManager.setISession(osirisSession);
        detailInteretMoratoireManager.find(getTransaction());

        FWCurrency montantTotal = new FWCurrency("0");
        for (int j = 0; j < detailInteretMoratoireManager.size(); j++) {
            CADetailInteretMoratoire detailInteretMoratoire = (CADetailInteretMoratoire) detailInteretMoratoireManager
                    .getEntity(j);
            // On récupère les montants et on les cumule
            montantTotal.add(detailInteretMoratoire.getMontantInteret());
        }
        return montantTotal;
    }

    public FAPassage getPassage() {
        return passage;
    }

    /**
     * Cette méthode effectue l'initialisation de la décision.<br/>
     * Elle remet idJouFac à 0 ainsi que la dateFacturation pour les décisions qui un idpassage identique à celui donné
     * par le processus<br/>
     * <br/>
     * (InitialiseDecision lit les désisions qui ont l'idPassge identique à l'idPassage, donné par le processus et remet
     * idJouFac et dateFacturation à zéro)
     */
    private boolean initialiseDecision(BSession osirisSession) throws Exception {
        // Lecture des décisions contenant dans idJouFac le n° de passage
        CAInteretMoratoireManager manager = new CAInteretMoratoireManager();
        manager.setISession(osirisSession);
        manager.setForIdGenreInteret(CAGenreInteret.CS_TYPE_TARDIF);
        manager.setForIdJournalFacturation(getPassage().getIdPassage());
        manager.find(getTransaction());

        if (manager.isEmpty()) {
            return true;
        }

        for (int i = 0; i < manager.size(); i++) {
            CAInteretMoratoire intmoratoire = (CAInteretMoratoire) manager.getEntity(i);
            // mise à 0 de la date de facturation et de l'idJournal de
            // facturation
            intmoratoire.setIdJournalFacturation("0");
            intmoratoire.setDateFacturation("0");
            intmoratoire.update(getTransaction());

            if (getTransaction().hasErrors()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Instancier le manager des intérêts moratoires.
     * 
     * @param osirisSession
     * @param manager
     */
    private void initInteretMoratoireManager(BSession osirisSession, CAInteretMoratoireManager manager) {
        manager.setISession(osirisSession);
        manager.setForIdJournalFacturation("0");

        ArrayList<String> idGenreInteretIn = new ArrayList<String>();
        idGenreInteretIn.add(CAGenreInteret.CS_TYPE_TARDIF);
        idGenreInteretIn.add(CAGenreInteret.CS_TYPE_COTISATIONS_PERSONNELLES);
        manager.setForIdGenreInteretIn(idGenreInteretIn);

        manager.setForFacturable(true);
    }

    /**
     * Initiliser la progression du process.
     * 
     * @param manager
     * @throws Exception
     */
    private void initProgressScaleValue(CAInteretMoratoireManager manager) throws Exception {
        int shouldNbCas = manager.getCount(getTransaction()); // provisoire
        // Entrer les informations pour l' état du process
        getParent().setState("(" + passage.getIdPassage() + ") Intérêt moratoire");
        if (shouldNbCas > 0) {
            getParent().setProgressScaleValue(shouldNbCas);
        } else {
            getParent().setProgressScaleValue(1);
        }
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Cette méthode permet d'effectuer un rollback de la transaction
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
        }
    }

    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
    }

    /**
     * Method setPassage. Utilise le passage passé en paramètre depuis la facturation
     * 
     * @param passage
     *            passage
     */
    public void setPassage(FAPassage passage) {
        this.passage = passage;
    }

    /**
     * Contrôler que le tiers existe.
     * 
     * @param osirisSession
     * @param pyxisSession
     * @param compteAnnexe
     * @return
     * @throws Exception
     */
    private boolean tiersExist(BSession osirisSession, BSession pyxisSession, APICompteAnnexe compteAnnexe)
            throws Exception {
        TITiersViewBean tiers = new TITiersViewBean();
        tiers.setISession(pyxisSession);
        tiers.setIdTiers(compteAnnexe.getIdTiers());
        tiers.retrieve(getTransaction());

        if (!tiers.isNew()) {
            return true;
        } else {
            getMemoryLog().logMessage(osirisSession.getLabel("5049") + " " + compteAnnexe.getIdExterneRole(),
                    FWMessage.ERREUR, this.getClass().getName());
            return false;
        }
    }

    /**
     * Cette méthode effectue la mise à jour des tables suivantes : <br/>
     * CAIMDCP Décisions intérêts moratoires<br/>
     * FAAFACP Ligne de facture<br/>
     * FAENTFP Entête facture.
     * 
     * @param osirisSession
     * @param pyxisSession
     * @param interetMoratoire
     * @param idExterneFacture
     * @return
     * @throws Exception
     */
    private boolean updateTables(BSession osirisSession, BSession pyxisSession, CAInteretMoratoire interetMoratoire,
            String idExterneFacture) throws Exception {
        // Recherche de l'entête de facture
        FAEnteteFactureManager entFactureManager = new FAEnteteFactureManager();
        entFactureManager.setISession(getSession());
        entFactureManager.setForIdTiers(interetMoratoire.getSection().getCompteAnnexe().getIdTiers());
        entFactureManager.setForIdPassage(getPassage().getIdPassage());
        entFactureManager.setForIdTypeFacture("1");
        entFactureManager.setForIdExterneRole(interetMoratoire.getSection().getCompteAnnexe().getIdExterneRole());
        entFactureManager.setForIdExterneFacture(idExterneFacture);
        entFactureManager.setForIdRole(interetMoratoire.getSection().getCompteAnnexe().getIdRole());
        entFactureManager.find(getTransaction());

        FAAfact afact = new FAAfact();
        if (entFactureManager.isEmpty()) {
            if (!tiersExist(osirisSession, pyxisSession, interetMoratoire.getSection().getCompteAnnexe())) {
                return false;
            }

            FAEnteteFacture nouveauEntFacture = createNewEnteteFacture(osirisSession, pyxisSession, interetMoratoire,
                    idExterneFacture);

            if (getTransaction().hasErrors()) {
                rollbackTransaction(FWMessageFormat.format(osirisSession.getLabel("7368"),
                        interetMoratoire.getIdInteretMoratoire()));
                return false;
            }

            afact.setIdEnteteFacture(nouveauEntFacture.getIdEntete());
        } else {
            // Si l'en-tête existe on récupère l'idEntete
            FAEnteteFacture enteteFacture = (FAEnteteFacture) entFactureManager.getFirstEntity();
            afact.setIdEnteteFacture(enteteFacture.getIdEntete());
        }

        FWCurrency montantTotal = getMontantTotalInteretMoratoire(osirisSession, interetMoratoire);

        // Bug 3172 - Renseigner la caisse prof. lors de la création des sections de type "50" et "900".
        if (interetMoratoire.getSection() != null) {
            afact.setNumCaisse(interetMoratoire.getSection().getIdCaisseProfessionnelle());
        }

        if (!montantTotal.isZero()) {
            // Création des AFACT pour l'intérêt moratoire séléctionné
            // Création de la ligne de facture
            afact.setISession(getSession());
            afact.setIdPassage(getPassage().getIdPassage());

            // Choix du module
            afact.setIdModuleFacturation(getIdModuleFacturation());
            afact.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
            afact.setIdRubrique(interetMoratoire.getIdRubrique());
            afact.setMontantFacture(montantTotal.toString());
            afact.setReferenceExterne(interetMoratoire.getIdInteretMoratoire());
            afact.add(getTransaction());

            // Mise à jour de la décision
            interetMoratoire.setDateFacturation(getPassage().getDateFacturation());
            interetMoratoire.setIdJournalFacturation(getPassage().getIdPassage());
            interetMoratoire.setIdSectionFacture(afact.getIdEnteteFacture());
            interetMoratoire.update(getTransaction());

            if (getTransaction().hasErrors()) {
                rollbackTransaction(FWMessageFormat.format(osirisSession.getLabel("7369"),
                        interetMoratoire.getIdInteretMoratoire()));
                return false;
            }
        }

        return true;
    }

}
