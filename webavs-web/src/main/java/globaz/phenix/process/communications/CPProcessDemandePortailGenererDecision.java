package globaz.phenix.process.communications;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.external.ServicesFacturation;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionViewBean;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.process.CPProcessCalculCotisation;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.CPUtil;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import ch.globaz.orion.business.domaine.demandeacompte.DemandeModifAcompteStatut;
import ch.globaz.orion.db.EBDemandeModifAcompteEntity;

/**
 * Génération des décisions lors de la validation d'une décision depuis ORION
 * 
 */
public class CPProcessDemandePortailGenererDecision extends BProcess {
    private static final long serialVersionUID = -1161457272987315919L;
    private AFCotisation cotisation = null;
    private String descriptionTiers = "";
    private String forIdPlausibilite = "";
    private String forStatus = "";
    private String fromNumAffilie = "";
    private String idDecision = "";
    // Paramètres utiliser par l'écran du decisionRepriseViewBean
    private String idPassage = "";
    private String idRetour = "";
    private CPJournalRetour journal = null;
    private Boolean lettreSignature = null;
    private ArrayList<String> listIdNonTraite = null;
    private String[] listIdDemande = null;

    private ArrayList<String> listIdTraite = null;
    private Vector messages = new Vector();
    private int modeCalculArrondiCotPers = 0;
    private CPDecisionViewBean newDecision = null;

    boolean processOk = true;
    private TITiersViewBean tiers = null;

    private String typeRevenu = "";
    private boolean validationDecision = false;

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessDemandePortailGenererDecision() {
        super();
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessDemandePortailGenererDecision(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur du type BSession.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public CPProcessDemandePortailGenererDecision(BSession session) {
        super(session);
    }

    /**
     * Calcul des cotisations à partir de la réception des communications fiscales
     * 
     * @param myDecision
     * @param retour
     */
    public void _calculDemande(CPDecisionViewBean newDecision, boolean revAf) {

        // Sous contrôle d'exception
        try {
            // recherhe des données de Base de la décision
            CPDonneesBase donneesBase = new CPDonneesBase();
            donneesBase.setSession(getSession());
            donneesBase.setIdDecision(newDecision.getIdDecision());
            donneesBase.retrieve(getTransaction());
            // Initialiser le process de calcul
            CPProcessCalculCotisation calcul = new CPProcessCalculCotisation();
            calcul.setIdDecision(newDecision.getIdDecision());
            calcul.setSession(getSession());
            calcul.setModeArrondiFad(getModeCalculArrondiCotPers());
            calcul.setSendMailOnError(true);
            calcul.setSendCompletionMail(false);
            calcul.setTransaction(getTransaction());
            calcul.setIdDecision(newDecision.getIdDecision());
            if (cotisation != null) {
                calcul.setCotiAf(cotisation);
            }
            if (newDecision.getAffiliation() != null) {
                calcul.setAffiliation(newDecision.loadAffiliation());
            }
            calcul.setDecision(newDecision);
            calcul.setTiers(this.getTiers());
            calcul.setDonneeBase(donneesBase);
            if (newDecision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_INDEPENDANT)
                    || newDecision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_RENTIER)
                    || newDecision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_AGRICULTEUR)
                    || newDecision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_TSE)) {
                if (revAf && !JadeStringUtil.isEmpty(newDecision.getRevenuAutre1())) {
                    // Calcul en ignorant revenu autre 1 et en ne créant pas la
                    // coti AF
                    calcul.calculIndependant(this, 1);
                    // Calcul en tenant compte uniquement de revenu autre 1 et
                    // en créant uniquement la coti AF
                    calcul.calculIndependant(this, 2);
                } else {
                    // calcul normal
                    calcul.calculIndependant(this, 0);
                }
            }
            // Création des remarques
            calcul.createRemarqueAutomatique(getTransaction(), newDecision);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean genererDecisionByIdDemande(String idDemande) {
        // Sous controle d'exceptions
        try {
            // disable le spy
            getTransaction().disableSpy();
            // Recherche si revenuAutre = revenu AF (ex cas spéciaux AF pour GE)
            boolean revAf = new CPApplication().isRevenuAf();
            // Informations venant de la demande
            EBDemandeModifAcompteEntity demande = new EBDemandeModifAcompteEntity();
            demande.setSession(getSession());
            demande.setIdEntity(idDemande);
            demande.retrieve();

            // Génération de la décision
            if (DemandeModifAcompteStatut.A_TRAITER.equals(DemandeModifAcompteStatut.fromValue(demande.getCsStatut()))) {
                _executeProcessDemandeGenererDecision(demande, revAf);
            }

            // rétablir le contrôle du spy
            getTransaction().enableSpy();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.FATAL, this.getClass().getName());
        } // Fin de la procédure
        catch (Throwable t) {
            t.printStackTrace();
        }
        return !isOnError();
    }

    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Calcul des montants de cotisation Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        listIdTraite = new ArrayList<String>();
        listIdNonTraite = new ArrayList<String>();

        try {
            // Chargement des propriétés

            chargementProprietes();
            // Soit on donne une liste d'id retour
            if ((getListIdDemande() != null) && (getListIdDemande().length != 0)) {
                genererByIdDemande();
                // Soit on donne un id de journal
            } else {
                this._addError(getTransaction(), getSession().getLabel("DEMANDE_PORTAIL_SELECTIONNE"));
            }
        } catch (Exception e) {
            this._addError(getTransaction(),
                    getSession().getLabel("PROCRECEPTGENDEC_EMAIL_OBJECT_FAILED") + " - " + e.toString());
        } finally {

            // Remettre les erreurs des process dans le log
            for (Iterator<?> iter = messages.iterator(); iter.hasNext();) {
                getMemoryLog().getMessagesToVector().add(iter.next());
                processOk = false;
            }
        }
        return processOk;
    }

    public boolean _executeProcessDemandeGenererDecision(EBDemandeModifAcompteEntity demande, boolean revAf) {
        try {
            // Si traitement conjoint => Test si le conjoint est aussi non actif
            // et non radié sinon ignorer
            AFAffiliation affiliation = null;
            // recherche de l'affiliation
            affiliation = new AFAffiliation();
            affiliation.setSession(getSession());
            affiliation.setAffiliationId(demande.getIdAffiliation());
            affiliation.retrieve();

            this.getTiers(affiliation.getIdTiers());

            // Initialiser la décision
            _initDecision(demande, affiliation);
            // Si décision == null (Ex: caisse externe => ne pas traiter
            if (newDecision == null) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0199"));
            }
            if (!getTransaction().hasErrors()) {
                // recherche de la décisiond de base pour reprendre des données
                // d'encodage
                _calculDemande(newDecision, revAf);
                // Mise à jour décision
                CPDecision decision = new CPDecision();
                decision.setIdDecision(newDecision.getIdDecision());
                decision.setSession(getSession());
                decision.retrieve();
                decision.setIdPassage(getIdPassage());
                decision.setIdDemandePortail(demande.getId());
                decision.setDernierEtat(CPDecision.CS_VALIDATION);
                decision.update();
                EBDemandeModifAcompteEntity dem = new EBDemandeModifAcompteEntity();
                dem.setSession(getSession());
                dem.setIdEntity(demande.getId());
                dem.retrieve();
                dem.setCsStatut(DemandeModifAcompteStatut.VALIDE.getValue());
                dem.update();

            }

            String msg = "";
            if (getSession().hasErrors() || getTransaction().hasErrors()) {
                msg = formatInfoMessageErreur(demande);
                if (getSession().hasErrors()) {
                    getMemoryLog().logMessage(msg + getSession().getErrors().toString(), FWMessage.ERREUR, "");
                } else if (getTransaction().hasErrors()) {
                    getMemoryLog().logMessage(msg + getTransaction().getErrors().toString(), FWMessage.ERREUR, "");
                }
                getTransaction().rollback();
                getTransaction().clearErrorBuffer();
                getListIdNonTraite().add(demande.getId());
            } else {
                getTransaction().commit();
                getListIdTraite().add(demande.getId());
            }
            // ------------------------------------------------------------------------------------

        } catch (Exception e) {
            String msg = "";
            try {
                msg = formatInfoMessageErreur(demande);
            } catch (Exception e1) {
                msg = demande.getId() + " - ";
            }
            getMemoryLog().logMessage(msg + e.toString(), FWMessage.FATAL, this.getClass().getName());
            this._addError(getTransaction(), getSession().getLabel("PROCDEMANDEGENDEC_EMAIL_OBJECT_FAILED") + ": "
                    + msg + e.toString());

        }
        return true;
    }

    /**
     * Cette méthode est équivalente à CPDecisionViewBean _initEcran
     * 
     * @param myDecision
     * @param retour
     * @return
     */
    public CPDecisionViewBean _initDecision(EBDemandeModifAcompteEntity demande, AFAffiliation affiliation) {
        // Reprise de la décision de base
        // charger la vraie décision à partir du fake entity
        cotisation = new AFCotisation();
        // Copier la décision
        try {
            CPDecisionViewBean decisionActuelle = null;
            if (!JadeStringUtil.isBlankOrZero(demande.getIdDecision())) {
                decisionActuelle = new CPDecisionViewBean();
                decisionActuelle.setAffiliation(null);
                decisionActuelle.setIdDecision(demande.getIdDecision());
                decisionActuelle.setProcessExterne(Boolean.TRUE);
                decisionActuelle.retrieve(getTransaction());
                newDecision = (CPDecisionViewBean) decisionActuelle.clone();
            } else {
                newDecision = new CPDecisionViewBean();
            }

            // Si pas de cotisation sous affiliation (ex: caisse externe) => Ne
            // pas traiter
            if (!AFAffiliationUtil.hasCotPersActif(affiliation, "01.01." + demande.getAnnee(),
                    "31.12." + demande.getAnnee())) {
                newDecision = null;
                return null;
            }
            newDecision.setIdDecision("");
            newDecision.setSession(getSession());
            newDecision.setProcessExterne(new Boolean(true));
            // newDecision.setIdTiers(this.getTiers().getIdTiers());
            newDecision.setTiers(this.getTiers());
            if (decisionActuelle != null) {
                newDecision.setTypeDecision(CPUtil.determinerTypeDecision(demande.getAnnee(),
                        decisionActuelle.getTypeDecision()));
            } else {
                newDecision.setGenreAffilie(CPToolBox.conversionTypeAffiliationEnGenreAffilie(affiliation));
                newDecision.setTypeDecision(CPUtil.determinerTypeDecision(demande.getAnnee(), null));
            }
            newDecision.setLettreSignature(lettreSignature);
            try {
                newDecision.setResponsable(CPToolBox.getUserByCanton(this.getTiers().getIdCantonDomicile(),
                        getTransaction()));
            } catch (Exception e) {
                newDecision.setResponsable("");
            }
            // date d'information = date du jour
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            newDecision.setDateInformation(sdf.format(demande.getDateReception()));
            newDecision.setDateDecision(JACalendar.todayJJsMMsAAAA());
            newDecision.setAffiliation(affiliation);
            newDecision.setIdAffiliation(affiliation.getAffiliationId());
            newDecision.setIdTiers(affiliation.getIdTiers());
            if (JadeStringUtil.isBlankOrZero(newDecision.getTaxation())) {
                newDecision.setTaxation("N");
            }
            newDecision.setDebutDecision("01.01." + demande.getAnnee());
            newDecision.setFinDecision("31.12." + demande.getAnnee());

            // Initialisation des données pour le calcul
            _initDonneeBase(newDecision, demande);

            // Recherche période AVS
            cotisation.setSession(getSession());
            cotisation = cotisation._retourCotisation(getTransaction(), affiliation.getAffiliationId(),
                    newDecision.getAnneeDecision(), globaz.naos.translation.CodeSystem.GENRE_ASS_PERSONNEL,
                    globaz.naos.translation.CodeSystem.TYPE_ASS_COTISATION_AVS_AI);

            String varDate = "";
            int anneeFin = 0;
            int anneeDebut = 0;
            if ((cotisation != null) && !JadeStringUtil.isEmpty(cotisation.getDateDebut())) {
                anneeDebut = globaz.globall.util.JACalendar.getYear(cotisation.getDateDebut());
                varDate = cotisation.getDateDebut();
            } else if ((affiliation != null) && !JadeStringUtil.isEmpty(affiliation.getDateDebut())) {
                anneeDebut = globaz.globall.util.JACalendar.getYear(affiliation.getDateDebut());
                varDate = affiliation.getDateDebut();
            }
            // Init date début décision
            if (anneeDebut == Integer.parseInt(newDecision.getAnneeDecision())) {
                newDecision.setDebutDecision(varDate);
            }
            // Si fin d'activité
            if ((cotisation != null) && !JadeStringUtil.isEmpty(cotisation.getDateFin())) {
                anneeFin = globaz.globall.util.JACalendar.getYear(cotisation.getDateFin());
                varDate = cotisation.getDateFin();
            } else if ((affiliation != null) && !JadeStringUtil.isEmpty(affiliation.getDateFin())) {
                anneeFin = globaz.globall.util.JACalendar.getYear(affiliation.getDateFin());
                varDate = affiliation.getDateFin();
            }
            // année de décision = année de fin d'activité => fin période
            // décision = fin d'activité
            if (anneeFin == Integer.parseInt(newDecision.getAnneeDecision())) {
                newDecision.setFinDecision(varDate);
            }
            // Recherche période fiscale et date de fortune
            newDecision._initPeriodeFiscale(newDecision, demande.getAnnee().toString(), anneeDebut);
            String dateAgeAvs = this.getTiers().getDateAvs();
            int anneeAvs = JACalendar.getYear(dateAgeAvs);
            int anneeDec = JACalendar.getYear(newDecision.getDebutDecision());
            // Détermination si rentier
            if ((anneeAvs < anneeDec)
                    || ((anneeAvs == anneeDec) && BSessionUtil.compareDateFirstGreater(getSession(),
                            newDecision.getFinDecision(), dateAgeAvs))) {
                newDecision.setGenreAffilie(CPDecision.CS_RENTIER);
            }
            newDecision.setBloque(new Boolean(false));
            // Si année d'activité = année de décision => debut période
            // décision = début d'activité
            if (!JadeStringUtil.isEmpty(newDecision.getDebutExercice1())
                    && (anneeDebut == JACalendar.getYear(newDecision.getDebutExercice1()))) {
                newDecision.setDebutActivite(new Boolean(true));
            }
            newDecision.setFacturation(new Boolean(true));
            newDecision.setImpression(new Boolean(true));
            // Pas d'intérêt pour ceux qui sont assistés
            if (CPToolBox.isAffilieAssiste(getTransaction(), affiliation, newDecision.getDebutDecision())) {
                newDecision.setInteret(CAInteretMoratoire.CS_EXEMPTE);
            } else {
                newDecision.setInteret(CAInteretMoratoire.CS_AUTOMATIQUE);
            }
            newDecision.add(getTransaction());
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0142") + demande.getNumAffilie() + " "
                    + demande.getId());
        }
        return newDecision;
    }

    /**
     * Cette méthode initialise les données nécessaire pour le calcul de la décision selon la demande en prevenance
     * d'Ebusiness.
     * 
     * @param newDecision
     * @param retour
     */
    public void _initDonneeBase(CPDecisionViewBean newDecision, EBDemandeModifAcompteEntity demande) throws Exception {
        newDecision.setComplementaire(new Boolean(false));
        newDecision.setCapital(demande.getCapital().toString());
        newDecision.setRevenu1(demande.getRevenu().toString());
        newDecision.setRevenu2("");
        newDecision.setRevenuAutre1("");
        newDecision.setRevenuAutre2("");
        newDecision.setCapital(demande.getCapital().toString());
        newDecision.setOpposition(Boolean.FALSE);
        newDecision.setRecours(Boolean.FALSE);
        newDecision.setSourceInformation(CPDonneesBase.CS_VOTRE_ESTIMATION);
        // si il n'y a pas de décision de base (cas du 1er acompte de l'année ou nouvel indépendant
        if (JadeStringUtil.isBlankOrZero(demande.getIdDecision())) {
            newDecision.setAnneeDecision(demande.getAnnee().toString());
            newDecision.setDebutExercice1("01.01." + demande.getAnnee());
            newDecision.setFinExercice1("31.12." + demande.getAnnee());
            newDecision.setNombreMoisTotalDecision("0");
        }
    }

    @Override
    protected void _validate() throws java.lang.Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0101"));
        }

        // si pas d'idPassage on essai de le retrouver
        if (JadeStringUtil.isBlankOrZero(idPassage)) {
            // Recherche du prochain passage de facturation
            IFAPassage passage = ServicesFacturation.getProchainPassageFacturation(getSession(), null,
                    FAModuleFacturation.CS_MODULE_COT_PERS_PORTAIL);

            if (passage == null || JadeStringUtil.isIntegerEmpty(passage.getIdPassage())) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0102"));
            } else {
                setIdPassage(passage.getIdPassage());
            }
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
    }

    /**
     * Ajoute des informations dans l'email.
     */
    private void addMailInformations() throws Exception {
        getMemoryLog().logMessage(getSession().getLabel("PROCESS_DONNEES_TRAITEES") + " : " + getListIdTraite().size(),
                FWMessage.INFORMATION, this.getClass().getName());
        getMemoryLog().logMessage(
                getSession().getLabel("PROCESS_DONNEES_NON_TRAITEES") + " : " + getListIdNonTraite().size(),
                FWMessage.INFORMATION, this.getClass().getName());
    }

    /**
     * Chargement des propriétés
     * 
     * @throws Exception
     */
    protected void chargementProprietes() throws Exception {
        try {
            // type de revenu depuis les properties
            String typeRevenu = new CPApplication().getTypeRevenu();
            setTypeRevenu(typeRevenu);
            lettreSignature = new CPApplication().isLettreSignature();
            setModeCalculArrondiCotPers(CPToolBox.getModeArrondiFad(getTransaction()));
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0137"));
        }
    }

    private String formatInfoMessageErreur(EBDemandeModifAcompteEntity demande) throws Exception {
        return demande.getNumAffilie() + " - " + demande.getAnnee() + " - " + demande.getId() + ": ";
    }

    /**
     * Permet la génération en partant d'une liste d'id de retour
     * 
     * @return
     */
    protected boolean genererByIdDemande() {
        try {
            // Init du compteur de progression
            initProgressCounter(getListIdDemande().length);

            // Pour chaque idRetour donné
            for (int i = 0; i < getListIdDemande().length; i++) {
                String idDemande = getListIdDemande()[i];
                try {
                    if (!JadeStringUtil.isEmpty(idDemande)) {
                        genererDecisionByIdDemande(idDemande);
                    }
                } catch (Exception e) {
                    this._addError(getTransaction(),
                            getSession().getLabel("CP_MSG_0139") + idDemande + " - " + e.toString());
                }
                if (isAborted()) {
                    return false;
                }
                // On incremente le compteur
                incProgressCounter();
                if (getSession().hasErrors()) {
                    getMemoryLog().logMessage(getSession().getErrors().toString() + " - " + idRetour, FWMessage.ERREUR,
                            "");
                    getTransaction().rollback();
                    getTransaction().clearErrorBuffer();
                }
                if (getTransaction().hasErrors()) {
                    getMemoryLog().logMessage(getTransaction().getErrors().toString() + " - " + idDemande,
                            FWMessage.ERREUR, "");
                    getTransaction().rollback();
                    getTransaction().clearErrorBuffer();
                } else {
                    getTransaction().commit();
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.FATAL, this.getClass().getName());
        } finally {
            for (Iterator iter = getMemoryLog().getMessagesToVector().iterator(); iter.hasNext();) {
                messages.add(iter.next());
            }
            getMemoryLog().clear();
        }
        return true;
    }

    public String getDescriptionTiers() {
        return descriptionTiers;
    }

    @Override
    protected String getEMailObject() {

        if (getMemoryLog().hasErrors() || !processOk) {
            return getSession().getLabel("PROCDEMANDEGENDEC_EMAIL_OBJECT_FAILED");
        }

        return getSession().getLabel("SUJET_EMAIL_RECEPTION_CALCUL");
    }

    public String getForIdPlausibilite() {
        return forIdPlausibilite;
    }

    public String getForStatus() {
        return forStatus;
    }

    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public String getIdRetour() {
        return idRetour;
    }

    public CPJournalRetour getJournal() {
        return journal;
    }

    public ArrayList<String> getListIdNonTraite() {
        return listIdNonTraite;
    }

    public String[] getListIdDemande() {
        return listIdDemande;
    }

    public ArrayList<String> getListIdTraite() {
        return listIdTraite;
    }

    public int getModeCalculArrondiCotPers() {
        return modeCalculArrondiCotPers;
    }

    public TITiersViewBean getTiers() {
        return tiers;
    }

    /**
     * Récupération d'un tiers
     * 
     * @param idTiers
     * @return
     * @throws Exception
     */
    protected TITiersViewBean getTiers(String idTiers) throws Exception {

        if ((this.getTiers() == null) || !this.getTiers().getIdTiers().equals(idTiers)) {
            TITiersViewBean persAvs = new TITiersViewBean();
            persAvs.setSession(getSession());
            persAvs.setIdTiers(idTiers);
            persAvs.retrieve();

            if (!persAvs.isNew()) {
                setTiers(persAvs);
            } else {
                setTiers(null);
            }
        }

        return this.getTiers();
    }

    public String getTypeRevenu() {
        return typeRevenu;
    }

    /**
     * Permet d'initialiser la progress bar
     * 
     * @param nbOccurence
     */
    protected void initProgressCounter(int nbOccurence) {

        if (nbOccurence > 0) {
            setProgressScaleValue(nbOccurence);
        } else {
            setProgressScaleValue(1);
        }
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setDescriptionTiers(String newDescriptionTiers) {
        descriptionTiers = newDescriptionTiers;
    }

    public void setForIdPlausibilite(String forIdPlausibilite) {
        this.forIdPlausibilite = forIdPlausibilite;
    }

    // **************************************
    // * Setter
    // **************************************

    public void setForStatus(String string) {
        forStatus = string;
    }

    public void setFromNumAffilie(String string) {
        fromNumAffilie = string;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public void setIdRetour(String idRetour) {
        this.idRetour = idRetour;
    }

    public void setJournal(CPJournalRetour journal) {
        this.journal = journal;
    }

    public void setListIdNonTraite(ArrayList<String> listIdNonTraite) {
        this.listIdNonTraite = listIdNonTraite;
    }

    public void setListIdDemande(String[] listIdDemande) {
        this.listIdDemande = listIdDemande;
    }

    public void setListIdTraite(ArrayList<String> listIdTraite) {
        this.listIdTraite = listIdTraite;
    }

    public void setModeCalculArrondiCotPers(int modeCalculArrondiCotPers) {
        this.modeCalculArrondiCotPers = modeCalculArrondiCotPers;
    }

    public void setTiers(TITiersViewBean tiers) {
        this.tiers = tiers;
    }

    public void setTypeRevenu(String typeRevenu) {
        this.typeRevenu = typeRevenu;
    }

}
