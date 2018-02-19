package globaz.phenix.process.communications;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommentaireCommunication;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourNEViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVSViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.communications.CPReglePlausibilite;
import globaz.phenix.db.communications.CPSedexContribuable;
import globaz.phenix.db.communications.CPSedexDonneesBase;
import globaz.phenix.db.communications.CPSedexDonneesCommerciales;
import globaz.phenix.db.communications.CPValidationCalculCommunication;
import globaz.phenix.db.communications.CPValidationCalculCommunicationManager;
import globaz.phenix.db.communications.CPValidationJournalRetourViewBean;
import globaz.phenix.db.divers.CPParametreCanton;
import globaz.phenix.db.divers.CPRevenuCotisationCanton;
import globaz.phenix.db.divers.CPRevenuCotisationCantonManager;
import globaz.phenix.db.divers.CPTableIndependant;
import globaz.phenix.db.divers.CPTableIndependantManager;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionViewBean;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.phenix.interfaces.ICommunicationRetour;
import globaz.phenix.interfaces.ICommunicationrRetourManager;
import globaz.phenix.process.CPProcessCalculCotisation;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.CPProperties;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import ch.globaz.common.domaine.Date;

/**
 * @author btc
 * @revision SCO 09-12-2009
 */
public class CPProcessReceptionGenererDecision extends BProcess {

    private static final long serialVersionUID = -1161457272987315919L;
    private int anneeChgt = 0;
    private String codeCanton = "";
    private AFCotisation cotisation = null;
    private String descriptionTiers = "";
    private String forIdPlausibilite = "";
    private String forStatus = "";
    private String fromNumAffilie = "";
    private String idDecision = "";
    private String idJournal = "";
    // Paramètres utiliser par l'écran du decisionRepriseViewBean
    private String idPassage = "";
    private String idRetour = "";
    private Boolean impressionMontantIdentique = Boolean.FALSE;
    private CPJournalRetour journal = null;
    private Boolean lettreSignature = null;
    private String[] listIdJournalRetour = null;
    private ArrayList<String> listIdNonTraite = null;
    private String[] listIdRetour = null;

    private ArrayList<String> listIdTraite = null;
    private Vector messages = new Vector();
    private int modeCalculArrondiCotPers = 0;
    private CPDecisionViewBean newDecision = null;
    private String numAffilieTraite = "";

    boolean processOk = true;
    private ICommunicationRetour retour = null;
    private TITiersViewBean tiers = null;

    private String tillNumAffilie = "";
    private boolean traitementConjoint = false;
    private String typeRevenu = "";
    private boolean validationDecision = false;

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessReceptionGenererDecision() {
        super();
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessReceptionGenererDecision(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur du type BSession.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public CPProcessReceptionGenererDecision(BSession session) {
        super(session);
    }

    /**
     * Calcul des cotisations à partir de la réception des communications fiscales
     * 
     * @param myDecision
     * @param retour
     */
    public void _calculRetour(CPDecisionViewBean newDecision, boolean revAf) {

        // Sous contrôle d'exception
        try {
            // recherhe des données de Base de la décision
            CPDonneesBase donneesBase = new CPDonneesBase();
            donneesBase.setSession(getSession());
            donneesBase.setIdDecision(newDecision.getIdDecision());
            donneesBase.retrieve(getTransaction());
            // Insertion ou radiation période assurance (ex: AFI)
            if (CPCommunicationFiscaleRetourViewBean.CS_IND_AFI.equalsIgnoreCase(retour.getChangementGenre())) {
                // Création AFI
                CPToolBox.ajoutRadiationCotisation(newDecision.getIdAffiliation(), CodeSystem.TYPE_ASS_AFI,
                        newDecision.getDebutDecision(), newDecision.getFinDecision(), 1, getSession());
            }
            if (CPCommunicationFiscaleRetourViewBean.CS_AFI_IND.equalsIgnoreCase(retour.getChangementGenre())) {
                // Suppression AFI
                CPToolBox.ajoutRadiationCotisation(newDecision.getIdAffiliation(), CodeSystem.TYPE_ASS_AFI,
                        newDecision.getDebutDecision(), newDecision.getFinDecision(), 2, getSession());
            }
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
            if (newDecision.isNonActif()) {
                calcul.calculNonActif(this);
            } else if (newDecision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_INDEPENDANT)
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

    /**
     * @param manager
     * @return
     */
    public boolean _executeBoucleRetour(ICommunicationrRetourManager manager) {

        BStatement statement = null;
        BTransaction transactionLecture = null;
        // Sous controle d'exceptions
        try {
            // disabler le spy
            getTransaction().disableSpy();
            // itérer sur toutes les affiliations
            transactionLecture = (BTransaction) getSession().newTransaction();
            transactionLecture.openTransaction();
            statement = manager.cursorOpen(transactionLecture);
            initProgressCounter(manager.getCount(transactionLecture));
            // Recherche si revenuAutre = revenu AF (ex cas spéciaux AF pour GE)
            boolean revAf = ((CPApplication) getSession().getApplication()).isRevenuAf();
            while (((retour = (ICommunicationRetour) manager.cursorReadNext(statement)) != null) && (!retour.isNew())
                    && !isAborted()) {
                traitementConjoint = false;
                // Si etat = générer => suppression des décisions
                retour.setJournalRetour(getJournal());
                String canton = getJournal().getCanton();
                if (CPJournalRetour.CS_CANTON_SEDEX.equalsIgnoreCase(canton)) {
                    CPSedexContribuable contri = new CPSedexContribuable();
                    contri.setSession(getSession());
                    contri.setIdRetour(retour.getIdRetour());
                    contri.retrieve();
                    codeCanton = contri.getSenderId().substring(2, 4);
                    // Conversion code canton en code système : Ex: VD -> 505022
                    if (!JadeStringUtil.isBlankOrZero(codeCanton)) {
                        canton = globaz.phenix.translation.CodeSystem.getCodeSysteme(getSession(), getTransaction(),
                                "PYCANTON", codeCanton, getSession().getIdLangue());
                    }
                }

                _executeProcessRetourGenererDecision(retour, revAf);
                // Tester si la décision du conjoint doit être traitée
                if ((traitementConjoint == false) && !JadeStringUtil.isIntegerEmpty(newDecision.getIdConjoint())
                        && !getTransaction().hasErrors()) {
                    String modeReception = "";
                    if (!JadeStringUtil.isBlankOrZero(canton)) {
                        modeReception = CPParametreCanton.findCodeWhitTypeAndCanton(getSession(), canton,
                                CPParametreCanton.CS_MODE_RECEPTION_SEDEX, "01.01." + retour.getAnnee1());
                    }
                    if (JadeStringUtil.isEmpty(modeReception)) {
                        modeReception = CPParametreCanton.CS_SEDEXRECEPTION_CJT;
                    }
                    if (modeReception.equalsIgnoreCase(CPParametreCanton.CS_SEDEXRECEPTION_CJT)
                            || (modeReception.equalsIgnoreCase(CPParametreCanton.CS_SEDEXRECEPTION_CJT_NA)
                                    && CPDecision.CS_NON_ACTIF.equalsIgnoreCase(retour.getGenreAffilie()) && CPDecision.CS_NON_ACTIF
                                        .equalsIgnoreCase(retour.getGenreConjoint()))) { // PO 9251
                        retour.setIdTiers(newDecision.getIdConjoint());
                        traitementConjoint = true;
                        _executeProcessRetourGenererDecision(retour, revAf);
                    }
                }
                incProgressCounter();

            }

            // rétablir le contrôle du spy
            getTransaction().enableSpy();
        } catch (Exception e) {
            String msg = "";
            try {
                msg = formatInfoMessageErreur(retour);
            } catch (Exception e1) {
                msg = retour.getIdRetour() + " - ";
            }
            getMemoryLog().logMessage(msg + e.toString(), FWMessage.FATAL, this.getClass().getName());
        } // Fin de la procédure
        catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                try {
                    manager.cursorClose(statement);
                } finally {
                    if (transactionLecture != null) {
                        transactionLecture.closeTransaction();
                    }
                    manager = null;
                    statement = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return !isOnError();
    }

    public boolean _executeBoucleRetour(String idRetour) {
        // Sous controle d'exceptions
        try {
            // disabler le spy
            getTransaction().disableSpy();
            // Recherche si revenuAutre = revenu AF (ex cas spéciaux AF pour GE)
            boolean revAf = ((CPApplication) getSession().getApplication()).isRevenuAf();
            CPJournalRetour jrn = recuperationJournal(idRetour, getIdJournal());
            setJournal(jrn);
            ICommunicationRetour communication = jrn.determinationEntity();
            communication.setIdRetour(idRetour);
            communication.setSession(getSession());
            communication.retrieve();
            communication.setJournalRetour(jrn);
            retour = communication;
            String canton = getJournal().getCanton();
            if (CPJournalRetour.CS_CANTON_SEDEX.equalsIgnoreCase(canton)) {
                CPSedexContribuable contri = new CPSedexContribuable();
                contri.setSession(getSession());
                contri.setIdRetour(retour.getIdRetour());
                contri.retrieve();
                codeCanton = contri.getSenderId().substring(2, 4);
                // Conversion code canton en code système : Ex: VD -> 505022
                if (!JadeStringUtil.isBlankOrZero(codeCanton)) {
                    canton = globaz.phenix.translation.CodeSystem.getCodeSysteme(getSession(), getTransaction(),
                            "PYCANTON", codeCanton, getSession().getIdLangue());
                }
            }
            // PO 9090
            traitementConjoint = false;

            _executeProcessRetourGenererDecision(retour, revAf);
            // Tester si la décision du conjoint doit être traitée
            if ((traitementConjoint == false) && !JadeStringUtil.isBlankOrZero(newDecision.getIdConjoint())) {
                String modeReception = "";
                if (!JadeStringUtil.isBlankOrZero(canton)) {
                    modeReception = CPParametreCanton.findCodeWhitTypeAndCanton(getSession(), canton,
                            CPParametreCanton.CS_MODE_RECEPTION_SEDEX, "01.01." + retour.getAnnee1());
                }
                if (JadeStringUtil.isEmpty(modeReception)) {
                    modeReception = CPParametreCanton.CS_SEDEXRECEPTION_CJT;
                }
                if (modeReception.equalsIgnoreCase(CPParametreCanton.CS_SEDEXRECEPTION_CJT)
                        || (modeReception.equalsIgnoreCase(CPParametreCanton.CS_SEDEXRECEPTION_CJT_NA)
                                && CPDecision.CS_NON_ACTIF.equalsIgnoreCase(retour.getGenreAffilie()) && CPDecision.CS_NON_ACTIF
                                    .equalsIgnoreCase(retour.getGenreConjoint()))) { // PO 9251
                    retour.setIdTiers(newDecision.getIdConjoint());
                    traitementConjoint = true;
                    _executeProcessRetourGenererDecision(retour, revAf);
                }
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
            if ((getListIdRetour() != null) && (getListIdRetour().length != 0)) {
                genererByIdRetour();

                // Soit on donne un id de journal
            } else if ((getListIdJournalRetour() != null) && (getListIdJournalRetour().length != 0)) {
                genererByIdJournalRetour();

                // On trace un message dans le mail
            } else {
                this._addError(getTransaction(), getSession().getLabel("COM_RETOUR_SELECTIONNE"));
            }

            // Ajout du corps de l'email
            addMailInformations();

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

    public boolean _executeProcessRetourGenererDecision(ICommunicationRetour retour, boolean revAf) {
        numAffilieTraite = "";
        try {
            impressionMontantIdentique = ((CPApplication) getSession().getApplication()).isImpressionMontantIdentique();
        } catch (Exception e) {
            impressionMontantIdentique = Boolean.FALSE;
        }
        try {
            // cas ou il n'y a que le conjoint à traiter -> couple dont le mari
            // est à la retraite (spécif VS)
            if (!traitementConjoint && JadeStringUtil.isBlankOrZero(retour.getIdAffiliation())
                    && !JadeStringUtil.isBlankOrZero(retour.getIdConjoint())) {
                traitementConjoint = true;
            }
            // Chargement du tiers
            if (traitementConjoint) {
                this.getTiers(retour.getIdConjoint());
            } else {
                this.getTiers(retour.getIdTiers());
            }
            // Supression des décisions de l'affilié
            this.retour._suppressionDecision(getTransaction(), this.getTiers().getIdTiers());
            // Si traitement conjoint => Test si le conjoint est aussi non actif
            // et non radié sinon ignorer
            AFAffiliation affiliation = null;
            if (traitementConjoint) {
                if (IConstantes.CS_LOCALITE_CANTON_VALAIS.equalsIgnoreCase(getJournal().getCanton())
                        || "620001".equalsIgnoreCase(getJournal().getCanton())) {
                    if (!JadeStringUtil.isBlankOrZero(retour.getIdAffiliationConjoint())) {
                        affiliation = new AFAffiliation();
                        affiliation.setSession(getSession());
                        affiliation.setAffiliationId(retour.getIdAffiliationConjoint());
                        affiliation.retrieve();
                    } else {
                        return true;
                    }
                } else {
                    affiliation = CPToolBox.returnAffiliation(getSession(), getTransaction(), this.getTiers()
                            .getIdTiers(), newDecision.getAnneeDecision(), CPDecision.CS_NON_ACTIF, 1);
                    if (affiliation == null) {
                        affiliation = CPToolBox.returnAffiliation(getSession(), getTransaction(), this.getTiers()
                                .getIdTiers(), newDecision.getAnneeDecision(), CPDecision.CS_ETUDIANT, 1);
                        if (affiliation == null) {
                            return true;
                        }
                    } else if (!AFAffiliationUtil.hasCotPersActif(affiliation,
                            "01.01." + newDecision.getAnneeDecision(), "31.12." + newDecision.getAnneeDecision())) {
                        return true;
                    }
                }
            } else {
                // recherche de l'affiliation
                affiliation = new AFAffiliation();
                affiliation.setSession(getSession());
                affiliation.setAffiliationId(retour.getIdAffiliation());
                affiliation.retrieve();
            }
            // Initialiser la décision avec les valeurs de la reprise
            if (!retour.getStatus().equalsIgnoreCase(CPCommunicationFiscaleRetourViewBean.CS_ERREUR)) {
                _initDecision(retour, affiliation);
            }
            // Si décision == null (Ex: caisse externe => ne pas traiter
            if (newDecision == null) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0199"));
            }
            if (!getTransaction().hasErrors()) {
                // recherche de la décisiond de base pour reprendre des données
                // d'encodage
                retour.setDecisionDeBase(newDecision.loadDerniereDecision(2));
                if (retour.getDecisionDeBase() != null) {
                    newDecision.setNombreMoisTotalDecision(retour.getDecisionDeBase().getNombreMoisTotalDecision());
                }
                if (!retour.getStatus().equalsIgnoreCase(CPCommunicationFiscaleRetourViewBean.CS_ERREUR)) {
                    _calculRetour(newDecision, revAf);
                    retour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_SANS_ANOMALIE);
                }
                // Re- stockage des décisions pour plausibilités car celle-ci a pu
                // être modifé par le calcul
                if (retour.getDecisionDeBase() != null) {
                    retour.setDecisionDeBase(newDecision.loadDerniereDecision(2));
                }
                retour.setDecisionGeneree(newDecision);
                if (!getTransaction().hasErrors()) {
                    // On va vérifier les plausibilités après la génération des
                    // décisions
                    CPProcessValiderPlausibilite processValidation = new CPProcessValiderPlausibilite();
                    processValidation.setSession(getSession());
                    processValidation.setCommunicationRetour(retour);
                    processValidation.setJournal(retour.getJournalRetour());
                    processValidation.setSendCompletionMail(false);
                    processValidation.setDeclenchement(CPReglePlausibilite.CS_APRES_GENERATION);
                    processValidation.executeProcess();
                }
                // ---------------------------------------------------------------------------------
                // Si cas sans anomalie et validation directe => mettre état
                // décision et com. fiscale à l'état valider sinon
                // alimenter la table validation des communications fiscales pour
                // validation "manuelle".
                // Mise à jour état
                CPDecision decision = new CPDecision();
                decision.setIdDecision(newDecision.getIdDecision());
                decision.setSession(getSession());
                decision.retrieve();
                if (!getTransaction().hasErrors()) {
                    if (CPCommunicationFiscaleRetourViewBean.CS_SANS_ANOMALIE.equalsIgnoreCase(retour.getStatus())
                            && validationDecision) {
                        retour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_VALIDE);
                        decision.setDernierEtat(CPDecision.CS_VALIDATION);

                    } else {
                        decision.setDernierEtat(CPDecision.CS_CALCUL);
                        validationCalculCommunicationRetour(newDecision);
                    }
                    try {
                        if (impressionMontantIdentique.booleanValue()) {
                            if (isDecisionIdentiqueProvisoire(decision)) {
                                decision.setImpression(Boolean.FALSE);
                                decision.setFacturation(Boolean.FALSE);
                            }
                        }
                        decision.setSpecification(newDecision.getSpecification());
                        decision.update(getTransaction());
                    } catch (Exception e) {
                        this._addError(getTransaction(), getSession().getLabel("CP_MSG_0140")
                                + retour.getTiers().getNumContribuableActuel() + " - " + e.toString());
                    }
                }
            }
            // Validation de la transaction
            if (!getTransaction().hasErrors() && (traitementConjoint == false)) {
                getTransaction().disableSpy();
                retour.wantCallValidate(false);
                retour.updateCas(getTransaction());
                getTransaction().enableSpy();
                retour.wantCallValidate(true);
            }
            String msg = "";
            if (getSession().hasErrors() || getTransaction().hasErrors()) {
                msg = formatInfoMessageErreur(retour);
                if (getSession().hasErrors()) {
                    getMemoryLog().logMessage(msg + getSession().getErrors().toString(), FWMessage.ERREUR, "");
                } else if (getTransaction().hasErrors()) {
                    getMemoryLog().logMessage(msg + getTransaction().getErrors().toString(), FWMessage.ERREUR, "");
                }
                getTransaction().rollback();
                getTransaction().clearErrorBuffer();
                getListIdNonTraite().add(idRetour);
            } else {
                getTransaction().commit();
                getListIdTraite().add(idRetour);
            }
            // ------------------------------------------------------------------------------------

        } catch (Exception e) {
            String msg = "";
            try {
                msg = formatInfoMessageErreur(retour);
            } catch (Exception e1) {
                msg = retour.getIdRetour() + " - ";
            }
            getMemoryLog().logMessage(msg + e.toString(), FWMessage.FATAL, this.getClass().getName());
            this._addError(getTransaction(), getSession().getLabel("PROCRECEPTGENDEC_EMAIL_OBJECT_FAILED") + ": " + msg
                    + e.toString());

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
    public CPDecisionViewBean _initDecision(ICommunicationRetour retour, AFAffiliation affiliation) {
        newDecision = new CPDecisionViewBean();
        cotisation = new AFCotisation();
        // Copier la décision
        try {
            numAffilieTraite = affiliation.getAffilieNumero(); // PO 7877
            // Si pas de cotisation sous affiliation (ex: caisse externe) => Ne
            // pas traiter
            if (!AFAffiliationUtil.hasCotPersActif(affiliation, "01.01." + retour.getAnnee1(),
                    "31.12." + retour.getAnnee1())) {
                newDecision = null;
                return null;
            }
            newDecision.setIdDecision("");
            newDecision.setSession(getSession());
            newDecision.setProcessExterne(new Boolean(true));
            newDecision.setIdPassage(getIdPassage());
            // vider l'id de la communication fiscale
            newDecision.setIdCommunication(retour.getIdRetour());
            newDecision.setIdTiers(this.getTiers().getIdTiers());
            newDecision.setTiers(this.getTiers());
            newDecision.setAnneeDecision(retour.getAnnee1());
            if (Integer.parseInt(newDecision.getAnneeDecision()) < anneeChgt) {
                newDecision.setTaxation("A");
            } else {
                newDecision.setTaxation("N");
            }
            newDecision.setTypeDecision(determinationTypeDecision());
            newDecision.setLettreSignature(lettreSignature);
            try {
                newDecision.setResponsable(CPToolBox.getUserByCanton(this.getTiers().getIdCantonDomicile(),
                        getTransaction()));
            } catch (Exception e) {
                newDecision.setResponsable("");
            }
            // date d'information = date du jour
            newDecision.setDateInformation(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
            newDecision.setDateDecision(retour.getDateRetour());
            newDecision.setNombreMoisTotalDecision("0");
            newDecision.setAffiliation(affiliation);
            newDecision.setIdAffiliation(affiliation.getAffiliationId());
            if (!retour.getStatus().equalsIgnoreCase(CPCommunicationFiscaleRetourViewBean.CS_ERREUR)) {
                // Recherche période AVS
                cotisation.setSession(getSession());
                cotisation = cotisation._retourCotisation(getTransaction(), affiliation.getAffiliationId(),
                        newDecision.getAnneeDecision(), globaz.naos.translation.CodeSystem.GENRE_ASS_PERSONNEL,
                        globaz.naos.translation.CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
            }
            int anneeDebut = 0;
            if (!retour.getStatus().equalsIgnoreCase(CPCommunicationFiscaleRetourViewBean.CS_ERREUR)) {
                newDecision.setDebutDecision("01.01." + retour.getAnnee1());
                newDecision.setFinDecision("31.12." + retour.getAnnee1());
                String varDate = "";
                int anneeFin = 0;
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
                // Genre affiliation
                if (traitementConjoint && !JadeStringUtil.isBlankOrZero(retour.getGenreConjoint())) {
                    newDecision.setGenreAffilie(retour.getGenreConjoint());
                } else {
                    newDecision.setGenreAffilie(retour.getGenreAffilie());
                }
                // Recherche période fiscale et date de fortune
                newDecision._initPeriodeFiscale(newDecision, retour.getAnnee1(), anneeDebut);
            }
            if (!retour.getStatus().equalsIgnoreCase(CPCommunicationFiscaleRetourViewBean.CS_ERREUR)) {
                String dateAgeAvs = this.getTiers().getDateAvs();
                int anneeAvs = JACalendar.getYear(dateAgeAvs);
                int anneeDec = JACalendar.getYear(newDecision.getDebutDecision());
                // Si indépendant
                if ((CPDecision.CS_AGRICULTEUR.equalsIgnoreCase(newDecision.getGenreAffilie()))
                        || (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(newDecision.getGenreAffilie()))
                        || (CPDecision.CS_TSE.equalsIgnoreCase(newDecision.getGenreAffilie()))) {
                    // Détermination si rentier
                    if ((anneeAvs < anneeDec)
                            || ((anneeAvs == anneeDec) && BSessionUtil.compareDateFirstGreater(getSession(),
                                    newDecision.getFinDecision(), dateAgeAvs))) {
                        newDecision.setGenreAffilie(CPDecision.CS_RENTIER);
                    }

                } else // Si non actif
                if (newDecision.isNonActif()) {
                    // Cas âge AVS
                    if ((anneeAvs == anneeDec)
                            && BSessionUtil.compareDateFirstGreater(getSession(), newDecision.getFinDecision(),
                                    dateAgeAvs)) {
                        newDecision.setFinDecision(dateAgeAvs);
                        // Iniatisation des champs selon le mode d'encodage
                    }
                }
                newDecision.setBloque(new Boolean(false));
                // Recherche affiliation conjoint
                if (!JadeStringUtil.isIntegerEmpty(retour.getIdConjoint())) {
                    newDecision.setDivision2(Boolean.TRUE);
                    AFAffiliation affiCjt = new AFAffiliation();
                    affiCjt.setSession(getSession());
                    if (!traitementConjoint) {
                        affiCjt.setAffiliationId(retour.getIdAffiliationConjoint());
                    } else {
                        affiCjt.setAffiliationId(retour.getIdAffiliation());
                    }
                    affiCjt.retrieve();
                    if (affiCjt != null) {
                        if (!traitementConjoint) {
                            newDecision.setIdConjoint(retour.getIdConjoint());
                        } else {
                            newDecision.setIdConjoint(affiCjt.getIdTiers());
                        }
                    }
                } else if (newDecision.isNonActif()) { // PO 2509 -> recherche
                    // si il a eu un
                    // conjoint (utile pour
                    // les veufs ou
                    // divorcés)
                    TICompositionTiersManager cjt = new TICompositionTiersManager();
                    cjt.setSession(getSession());
                    // Recherche du conjoint
                    // acr: selon directive AVS: Le mariage doit être considéré
                    // depuis le début de l'année du mariage (division par deux
                    // pour toute l'année).
                    // Le divorce doit être considéré depuis le début de l'année
                    // du divorce (individuel pour toute l'année).
                    // Ce qui signifie que l'on va chercher le conjoint au 31.12
                    // de l'année de la décision
                    cjt.setForIdTiersEnfantParent(this.getTiers().getIdTiers());
                    cjt.setForTypeLien(TICompositionTiers.CS_CONJOINT);
                    cjt.setForDateEntreDebutEtFin("31.12." + newDecision.getAnneeDecision());
                    cjt.find();
                    if (cjt.size() > 0) {
                        newDecision.setDivision2(Boolean.TRUE);
                    }
                }
                // Si on a pas de conjoint renseigné, mais que l'état civil
                // fait partie de la propriété "Etat civil conjoint"
                if (CPProperties.ETAT_CIVIL_SIMUL_CONJOINT.getValue().contains(this.getTiers().getEtatCivil())) {
                    newDecision.setDivision2(Boolean.TRUE);
                }
                // Initialisation des données pour le calcul
                _initDonneeBase(newDecision, retour);
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
                if (!retour.getStatus().equalsIgnoreCase(CPCommunicationFiscaleRetourViewBean.CS_ERREUR)) {
                    newDecision.add(getTransaction());
                }
                // Si étudiant => pas d'impression
                if (CPDecision.CS_ETUDIANT.equalsIgnoreCase(newDecision.getGenreAffilie())) {
                    newDecision.setImpression(Boolean.FALSE);
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0142") + retour.getNumAffilieRecu() + " "
                    + retour.getIdRetour());
        }
        return newDecision;
    }

    /**
     * Cette méthode initialise les données nécessaire pour le calcul de la décision selon les données du fisc.
     * 
     * @param newDecision
     * @param retour
     */
    public void _initDonneeBase(CPDecisionViewBean newDecision, ICommunicationRetour retour) throws Exception {
        newDecision.setAutreFortune("");
        newDecision.setComplementaire(new Boolean(false));
        if (newDecision.isNonActif()) {
            newDecision.setCapital("");
            newDecision.setDebutExercice1("");
            newDecision.setDebutExercice2("");
            newDecision.setFinExercice1("");
            newDecision.setFinExercice2("");
            if (!IConstantes.CS_LOCALITE_CANTON_VALAIS.equalsIgnoreCase(getJournal().getCanton())) {
                newDecision.setFortuneTotale(retour.getFortune());
            }
            if ("Periode".equalsIgnoreCase(typeRevenu)) {
                int dureeDecision = JACalendar.getMonth(newDecision.getFinDecision())
                        - JACalendar.getMonth(newDecision.getDebutDecision()) + 1;
                newDecision.setNbMoisExercice1(Integer.toString(dureeDecision));
                newDecision.setNbMoisRevenuAutre1(Integer.toString(dureeDecision));
            } else {
                newDecision.setNbMoisExercice1("12");
                newDecision.setNbMoisRevenuAutre1("12");
            }
        } else {
            newDecision.setDateFortune("");
            if (JadeStringUtil.isBlankOrZero(newDecision.getCapital())) {
                newDecision.setCapital(retour.getCapital());
            }

            findDateExercice(newDecision, retour);
        }
        initRevenu(newDecision, retour);
        if ((CodeSystem.TYPE_AFFILI_INDEP.equalsIgnoreCase(newDecision.getAffiliation().getTypeAffiliation()) || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY
                .equalsIgnoreCase(newDecision.getAffiliation().getTypeAffiliation()))) {
            // Test s'il faut ajouter les cotisations
            // Par défaut c'est true car on part du principe que les fiscs n'ont pas adaptés
            // et on garde le fonctionnement actuel comme quoi qu'il communique toujours le revenu
            // Si canton différent de SEDEX => se baser sur le code canton du journal
            // SI canton = SEDEx => recherche selon le champ expéditeur de la communication
            // 1.15.0 : D0108 ne plus faire pour les TSE
            boolean revenuAvecCotisation = false;
            if (!JadeStringUtil.isBlankOrZero(newDecision.getRevenu1())
                    || !JadeStringUtil.isBlankOrZero(newDecision.getRevenuAutre1())) {
                CPRevenuCotisationCantonManager rcM = new CPRevenuCotisationCantonManager();
                rcM.setSession(getSession());
                if (CPJournalRetour.CS_CANTON_SEDEX.equalsIgnoreCase(getJournal().getCanton())) {
                    rcM.setForCodeCanton(codeCanton);
                } else {
                    rcM.setForCanton(getJournal().getCanton());
                }
                rcM.setFromAnneeDebutDesc(retour.getAnnee1());
                rcM.setFromDateDebutActiviteDesc(retour.getDateRetour());
                rcM.find();
                if (rcM.getSize() > 0) {
                    CPRevenuCotisationCanton rCot = (CPRevenuCotisationCanton) rcM.getFirstEntity();
                    if (Boolean.TRUE.equals(rCot.getAvecCotisation())
                            && BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), retour.getDateRetour(),
                                    rCot.getDateActivite())) {
                        revenuAvecCotisation = true;
                    }
                }
                // Calcul et ajout des cotisations aux revenus
                if (!revenuAvecCotisation
                        && BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), retour.getDateRetour(),
                                "01.01.2012")) {
                    calculRevenuAvecCotisation(retour, newDecision); // Modif PO 6336
                }
            }
        }
        newDecision.setOpposition(Boolean.FALSE);
        newDecision.setRecours(Boolean.FALSE);
        if (retour.getGenreTaxation().equalsIgnoreCase(CPCommentaireCommunication.CS_GENRE_TO)
                || retour.getGenreTaxation().equalsIgnoreCase(CPCommentaireCommunication.CS_GENRE_TOR)) {
            newDecision.setSourceInformation(CPDonneesBase.CS_TAX_OFFICE);
        } else {
            newDecision.setSourceInformation(CPDonneesBase.CS_FISC);
        }
    }

    protected void defineDateExercice1(CPDecisionViewBean newDecision, String annee, CPSedexDonneesCommerciales doco,
            AFAffiliation affiliation) {

        String formatDateDoco = "yyyy-MM-dd";

        Date dateDebutDefault = new Date("01.01." + annee);
        Date dateFinDefault = new Date("31.12." + annee);

        Date dateDebutExerciceFromFisc = null;
        Date dateFinExerciceFromFisc = null;

        if (doco != null && !doco.isNew()) {
            if (!JadeStringUtil.isBlankOrZero(doco.getCommencementOfSelfEmployment())) {
                dateDebutExerciceFromFisc = new Date(doco.getCommencementOfSelfEmployment(), formatDateDoco);
            }
            if (!JadeStringUtil.isBlankOrZero(doco.getEndOfSelfEmployment())) {
                dateFinExerciceFromFisc = new Date(doco.getEndOfSelfEmployment(), formatDateDoco);
            }
        }

        Date dateDebutExerciceFromAffiliation = null;
        Date dateFinExerciceFromAffiliation = null;

        if (affiliation != null && !affiliation.isNew() && !JadeStringUtil.isEmpty(affiliation.getDateDebut())) {
            dateDebutExerciceFromAffiliation = new Date(affiliation.getDateDebut());

        }
        if (affiliation != null && !affiliation.isNew() && !JadeStringUtil.isEmpty(affiliation.getDateFin())) {
            dateFinExerciceFromAffiliation = new Date(affiliation.getDateFin());
        }

        // Définition de la date que l'on doit prendre
        // ------------
        // Date de début
        if (dateDebutExerciceFromFisc != null) {
            newDecision.setDebutExercice1(dateDebutExerciceFromFisc.getSwissValue());
        } else if (dateDebutExerciceFromAffiliation != null && dateDebutExerciceFromAffiliation.after(dateDebutDefault)
                && dateDebutExerciceFromAffiliation.before(dateFinDefault)) {
            newDecision.setDebutExercice1(dateDebutExerciceFromAffiliation.getSwissValue());
        } else {
            newDecision.setDebutExercice1(dateDebutDefault.getSwissValue());
        }

        // Date de fin
        if (dateFinExerciceFromFisc != null) {
            newDecision.setFinExercice1(dateFinExerciceFromFisc.getSwissValue());
        } else if (dateFinExerciceFromAffiliation != null && dateFinExerciceFromAffiliation.before(dateFinDefault)
                && dateFinExerciceFromAffiliation.after(dateDebutDefault)) {
            newDecision.setFinExercice1(dateFinExerciceFromAffiliation.getSwissValue());
        } else {
            newDecision.setFinExercice1(dateFinDefault.getSwissValue());
        }
    }

    private void findDateExercice(CPDecisionViewBean newDecision, ICommunicationRetour retour) throws Exception {

        // Recherche si infos dans les données du fisc
        CPSedexDonneesCommerciales doco = new CPSedexDonneesCommerciales();
        doco.setIdRetour(retour.getIdRetour());
        doco.setSession(getSession());

        try {
            doco.retrieve();
        } catch (Exception e) {
            JadeLogger.warn(e, "Unabled to retrieve données commerciales of idRetour : " + idRetour);
        }

        defineDateExercice1(newDecision, retour.getAnnee1(), doco, newDecision.getAffiliation());

        // -------------
        // Exercice 2
        if (!JAUtil.isDateEmpty(retour.getDebutExercice2())) {
            newDecision.setDebutExercice2(retour.getDebutExercice2());
        }
        if (!JAUtil.isDateEmpty(retour.getFinExercice2())) {
            newDecision.setFinExercice2(retour.getFinExercice2());
        }
    }

    @Override
    protected void _validate() throws java.lang.Exception {

        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0101"));
        }

        if (JadeStringUtil.isIntegerEmpty(getIdPassage()) && (validationDecision == true)) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0102"));
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

    protected float calculInteret(CPDecisionViewBean newDecision) throws Exception {
        float capital = 0;
        float interet = 0;
        String tauxInteret = "";
        // Détermination des intérêts du capital investi
        // interet = capital arrondi au 1000fr. supérieur * taux
        if (!JadeStringUtil.isBlank(newDecision.getCapital())) {
            capital = Float.parseFloat(JANumberFormatter.deQuote(newDecision.getCapital()));
            // Arrondir le capital au 1000 fr. supérieur
            capital = JANumberFormatter
                    .round(Float.parseFloat(Float.toString(capital)), 1000, 0, JANumberFormatter.SUP);
            // Recherche du taux dans la table des paramètres correspondant
            // au début de décision (praenumerando) ou à la date de fin
            // d'exercice (postnumerando)
            if (newDecision.getTaxation().equalsIgnoreCase("A")) {
                tauxInteret = FWFindParameter.findParameter(getTransaction(), "10500020", "TAUXINTERE",
                        newDecision.getDebutDecision(), "", 2);
            } else {
                tauxInteret = FWFindParameter.findParameter(getTransaction(), "10500020", "TAUXINTERE",
                        newDecision.getFinExercice1(), "", 2);
            }
            // Calcul des intêrets
            if (!JadeStringUtil.isBlank(tauxInteret)) {
                interet = (capital * Float.parseFloat(tauxInteret)) / 100;
            }
            // Ramener au prorata de la période de l'exercice pour le
            // mode de taxation postnumerando
            interet = Float.parseFloat(CPToolBox.prorataInteret(newDecision.getDebutExercice1(),
                    newDecision.getFinExercice1(), Float.toString(interet)));
            interet = JANumberFormatter.round(interet, 1, 0, JANumberFormatter.INF);
            if (interet < 0) {
                interet = 0;
            }
        }
        return interet;
    }

    protected void calculRevenuAvecCotisation(ICommunicationRetour retour, CPDecisionViewBean newDecision)
            throws Exception {
        // Recherche taux dans la table des indépendant qui permet de
        // recalculer la revenu (modification AVS 2012)
        if (!JadeStringUtil.isBlankOrZero(newDecision.getRevenu1())
                || !JadeStringUtil.isBlankOrZero(newDecision.getRevenuAutre1())) {
            String revenu = "0";
            // Si les 2 revenus (Exemple revenu et revenu agricole sont renseignés
            // il ne faut pas calculer les cotisations pour chaque revenus mais prendre le total
            // et ajouter les cotisation sur le revenu non agricole => PO 6336 (I120216_000026)
            FWCurrency varTemp = new FWCurrency(revenu);
            if (!JadeStringUtil.isBlankOrZero(newDecision.getRevenu1())) {
                varTemp.add(JANumberFormatter.deQuote(newDecision.getRevenu1()));
            }
            if (!JadeStringUtil.isBlankOrZero(newDecision.getRevenuAutre1())) {
                varTemp.add(JANumberFormatter.deQuote(newDecision.getRevenuAutre1()));
            }
            revenu = varTemp.toString();
            if (varTemp.isNegative()) { // PO 6469
                return; // PO 6469
            }
            // Nouvelle directive au 01.01.2013 (tenir compte de la franchise pour les rentiers
            String montantPourTaux = revenu;
            float mFranchise = 0;
            if (CPDecision.CS_RENTIER.equalsIgnoreCase(newDecision.getGenreAffilie())) {
                // Détermination du montant de franchise
                mFranchise = getFranchise(this);
                varTemp = new FWCurrency(JANumberFormatter.deQuote(revenu));
                varTemp.sub(mFranchise);
                if (varTemp.isNegative()) { // PO 8272
                    return; // PO 8272
                }
                montantPourTaux = varTemp.toString();
            }

            // S150911_009
            float mInteretcapital = 0;
            if (!JadeStringUtil.isBlankOrZero(newDecision.getCapital())) {
                // Détermination du montant d'intérêt
                mInteretcapital = CPDonneesCalcul.calculInteretCapital(this, newDecision);
                varTemp = new FWCurrency(JANumberFormatter.deQuote(revenu));
                varTemp.sub(mFranchise);
                varTemp.sub(mInteretcapital);
                if (varTemp.isNegative()) { // PO 8272
                    return; // PO 8272
                }
                montantPourTaux = varTemp.toString();
            }

            // Pas de calcul de cotisation si l'affiliié a une activité accessoire
            if (determinineSiActiviteAccessoire(newDecision, revenu)) {
                return;
            }
            //
            CPTableIndependant tInd = null;
            float tauxCalcul = 0;
            CPTableIndependantManager tIndManager = new CPTableIndependantManager();
            tIndManager.setSession(getSession());
            tIndManager.setFromAnneeInd(retour.getAnnee1());
            if (Float.parseFloat(JANumberFormatter.deQuote(montantPourTaux)) < 0) {
                tIndManager.setFromRevenuInd("0");
            } else {
                tIndManager.setFromRevenuInd(montantPourTaux);
            }
            tIndManager.orderByAnneeDescendant();
            tIndManager.orderByRevenuDescendant();
            tIndManager.find();
            if (tIndManager.size() == 0) {
                this._addError(getTransaction(),
                        "cotisation non trouvée: " + retour.getAnnee1() + "  " + retour.getRevenu1() + " "
                                + getDescriptionTiers());
            } else {
                tInd = ((CPTableIndependant) tIndManager.getEntity(0));
            }
            if (tInd == null) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0110") + " " + getDescriptionTiers());
            }
            if (!getTransaction().hasErrors()) {
                if (JadeStringUtil.isEmpty(tInd.getTaux())) {
                    tauxCalcul = 100;
                } else {
                    tauxCalcul = Float.parseFloat(new FWCurrency(100 - Float.parseFloat(JANumberFormatter.deQuote(tInd
                            .getTaux())), 4).toString());
                }

                float calculCotisation = 0f;
                if (!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(newDecision.getRachatLPP()))) {
                    float rachatLpp = Float.parseFloat(JANumberFormatter.deQuote(newDecision.getRachatLPP()));
                    float revenuPourCalcul = Float.parseFloat(JANumberFormatter.deQuote(revenu)) - rachatLpp;

                    calculCotisation = (((revenuPourCalcul * 100)
                            - (mFranchise * Float.parseFloat(JANumberFormatter.deQuote(tInd.getTaux()))) - (mInteretcapital * Float
                            .parseFloat(JANumberFormatter.deQuote(tInd.getTaux())))) / tauxCalcul) - revenuPourCalcul;
                } else {

                    calculCotisation = (((Float.parseFloat(JANumberFormatter.deQuote(revenu)) * 100)
                            - (mFranchise * Float.parseFloat(JANumberFormatter.deQuote(tInd.getTaux()))) - (mInteretcapital * Float
                            .parseFloat(JANumberFormatter.deQuote(tInd.getTaux())))) / tauxCalcul)
                            - Float.parseFloat(JANumberFormatter.deQuote(revenu));
                }
                newDecision.setCotisation1(JANumberFormatter.round(Float.toString(calculCotisation), 1, 2,
                        JANumberFormatter.INF));

            }
        }
    }

    /**
     * Determine si l'affilié à une activité accessoire
     * 
     * @param newDecision
     * @param revenu
     * @return
     * @throws Exception
     */
    private boolean determinineSiActiviteAccessoire(CPDecisionViewBean newDecision, String revenu) throws Exception {
        // PO 8272 - Si activité accessoire ne pas ajouter les cotisations
        if (AFParticulariteAffiliation.existeParticularite(getTransaction(), this.newDecision.getIdAffiliation(),
                AFParticulariteAffiliation.CS_ACTIVITE_ACCESSOIRE, this.newDecision.getDebutDecision())) {
            // Recherche montant maximum pour activité accessoire
            float revenuActviteAccessoire = Float.parseFloat(FWFindParameter.findParameter(getTransaction(),
                    "10500130", "REVACTACC", this.newDecision.getDebutDecision(), "", 0));

            float interet = calculInteret(newDecision);
            float montantActiviteAccessoire = Float.parseFloat(JANumberFormatter.deQuote(revenu.toString())) - interet;
            // Arrondir au 100 CHF pour les tests si le genre est différents de TSE
            if (!CPDecision.CS_TSE.equalsIgnoreCase(newDecision.getGenreAffilie())
                    && (JANumberFormatter.round(montantActiviteAccessoire, 100, 0, JANumberFormatter.INF) <= revenuActviteAccessoire)) {
                return true;
            }
            // ne pas arrondir au 100 CHF pour les tests pour les TSE
            if (CPDecision.CS_TSE.equalsIgnoreCase(newDecision.getGenreAffilie())
                    && (montantActiviteAccessoire <= revenuActviteAccessoire)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Chargement des propriétés
     * 
     * @throws Exception
     */
    protected void chargementProprietes() throws Exception {
        try {
            // type de revenu depuis les properties
            String typeRevenu = ((CPApplication) getSession().getApplication()).getTypeRevenu();
            setTypeRevenu(typeRevenu);
            lettreSignature = ((CPApplication) getSession().getApplication()).isLettreSignature();
            anneeChgt = ((CPApplication) getSession().getApplication()).getAnneeChangement();
            validationDecision = ((CPApplication) getSession().getApplication()).isValidationDecision();
            setModeCalculArrondiCotPers(CPToolBox.getModeArrondiFad(getTransaction()));
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0137"));
        }
    }

    private boolean checkOptionAllowed(String idRetour) {
        String idValidation = "";

        try {
            CPValidationCalculCommunicationManager manager = new CPValidationCalculCommunicationManager();
            manager.setSession(getSession());
            manager.setForIdCommunicationRetour(idRetour);
            manager.find();
            CPValidationCalculCommunication communication = (CPValidationCalculCommunication) manager.getFirstEntity();
            if (communication != null) {
                idValidation = communication.getIdValidationCommunication();
                if (idValidation != null) {
                    CPValidationJournalRetourViewBean validation = new CPValidationJournalRetourViewBean();
                    validation.setSession(getSession());
                    validation.setIdValidation(idValidation);
                    validation.getIdDecision();
                    validation.retrieve();
                    if (validation.getCodeValidation().equalsIgnoreCase("1")) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            // la communication n'est pas encore dans la table calcul
            // communication
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public String determinationTypeDecision() {
        String typeDecision = CPDecision.CS_DEFINITIVE;
        if (retour.getGenreTaxation() != null) {
            if (retour.getGenreTaxation().equalsIgnoreCase(CPCommentaireCommunication.CS_GENRE_TD)
                    || retour.getGenreTaxation().equalsIgnoreCase(CPCommentaireCommunication.CS_GENRE_TO)) {
                typeDecision = CPDecision.CS_DEFINITIVE;
            } else if (retour.getGenreTaxation().equalsIgnoreCase(CPCommentaireCommunication.CS_GENRE_TDD)
                    || retour.getGenreTaxation().equalsIgnoreCase(CPCommentaireCommunication.CS_GENRE_TDR)
                    || retour.getGenreTaxation().equalsIgnoreCase(CPCommentaireCommunication.CS_GENRE_TOR)
                    || retour.getGenreTaxation().equalsIgnoreCase(CPCommentaireCommunication.CS_GENRE_TRD)) {
                typeDecision = CPDecision.CS_RECTIFICATION;
            } else if (retour.getGenreTaxation().equalsIgnoreCase(CPCommentaireCommunication.CS_GENRE_TP)) {
                typeDecision = CPDecision.CS_PROVISOIRE;
            } else if (retour.getGenreTaxation().equalsIgnoreCase(CPCommentaireCommunication.CS_GENRE_TPR)
                    || retour.getGenreTaxation().equalsIgnoreCase(CPCommentaireCommunication.CS_GENRE_TPD)
                    || retour.getGenreTaxation().equalsIgnoreCase(CPCommentaireCommunication.CS_GENRE_TRP)) {
                typeDecision = CPDecision.CS_CORRECTION;
            }
        }
        return typeDecision;
    }

    private String formatInfoMessageErreur(ICommunicationRetour retour) throws Exception {
        String msg;
        if (!JadeStringUtil.isBlankOrZero(numAffilieTraite)) {
            msg = numAffilieTraite;
        } else if ((newDecision != null) && (newDecision.getAffiliation() != null)) {
            msg = newDecision.getAffiliation().getAffilieNumero();
        } else if (!JadeStringUtil.isBlankOrZero(retour.getDescription(1))) {
            msg = retour.getDescription(1);
        } else {
            if (!JadeStringUtil.isBlankOrZero(retour.getIdAffiliation())) {
                msg = retour.getAffiliation().getAffilieNumero();
            } else {
                msg = retour.getAffiliationConjoint().getAffilieNumero();
            }
        }
        msg = msg + " - " + retour.getIdRetour() + " - ";
        return msg;
    }

    /**
     * Permet la génération en partant d'une liste d'id de journal de retour
     * 
     * @return
     */
    protected boolean genererByIdJournalRetour() {

        // Init du compteur de progression
        initProgressCounter(getListIdJournalRetour().length);

        // Pour chaque id journal retour donné
        for (int i = 0; (i < getListIdJournalRetour().length) && !isAborted(); i++) {

            String idJournalRetour = getListIdJournalRetour()[i];

            try {
                if (!JadeStringUtil.isEmpty(idJournalRetour)) {
                    setIdJournal(idJournalRetour);
                    genererDecision(null);
                    setIdJournal(null);
                }

            } catch (Exception e) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0139") + retour.getIdRetour() + " - "
                        + e.toString());
                getListIdNonTraite().add(idJournalRetour);
            }

            if (isAborted()) {
                return false;
            }

            // On incremente le compteur
            incProgressCounter();
        }

        return true;
    }

    /**
     * Permet la génération en partant d'une liste d'id de retour
     * 
     * @return
     */
    protected boolean genererByIdRetour() {
        try {
            // Init du compteur de progression
            initProgressCounter(getListIdRetour().length);

            // Pour chaque idRetour donné
            for (int i = 0; i < getListIdRetour().length; i++) {
                String idRetour = getListIdRetour()[i];
                try {
                    if (!JadeStringUtil.isEmpty(idRetour)) {
                        if (checkOptionAllowed(idRetour)) {
                            genererDecisionByIdRetour(idRetour);
                        } else {
                            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0176") + " " + idRetour);
                        }
                    }
                } catch (Exception e) {
                    this._addError(getTransaction(), getSession().getLabel("CP_MSG_0139") + retour.getIdRetour()
                            + " - " + e.toString());
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
                    getMemoryLog().logMessage(getTransaction().getErrors().toString() + " - " + idRetour,
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

    /**
     * Execution de la génération des décisions
     * 
     * @param idRetour
     * @return
     */
    protected boolean genererDecision(String idRetour) {

        try {
            ICommunicationrRetourManager manager = null;

            setJournal(recuperationJournal(idRetour, getIdJournal()));

            // Définition du manager
            manager = getJournal().determinationManager();
            if (!JadeStringUtil.isEmpty(getIdRetour())) {
                manager.setForIdRetour(getIdRetour());
            } else {
                manager.setInStatus(CPCommunicationFiscaleRetourViewBean.CS_A_CONTROLER + ", "
                        + CPCommunicationFiscaleRetourViewBean.CS_AVERTISSEMENT + ", "
                        + CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE + ", "
                        + CPCommunicationFiscaleRetourViewBean.CS_SANS_ANOMALIE + ", "
                        + CPCommunicationFiscaleRetourViewBean.CS_VALIDE);
                manager.setFromNumAffilie(getFromNumAffilie());
                manager.setTillNumAffilie(getTillNumAffilie());
            }
            manager.setSession(getSession());
            manager.setForIdJournalRetour(getIdJournal());
            manager.setWhitPavsAffilie(true);
            manager.setWhitAffiliation(true);
            manager.orderByNumIFD();
            manager.orderByNumContribuable();
            manager.orderByIdCommunicationRetour();
            manager.setForIdPlausibilite(getForIdPlausibilite());
            if (validationDecision) {
                getJournal().setStatus(CPJournalRetour.CS_VALIDE_PARTIEL);
            } else {
                getJournal().setStatus(CPJournalRetour.CS_GENERE_PARTIEL);
            }
            if (!JadeStringUtil.isEmpty(getForStatus())) {
                manager.setForStatus(getForStatus());
            }
            // On ne traite pas les communications fiscales en enquête.
            manager.setExceptEnEnquete(true);

            this._executeBoucleRetour(manager);

            if (!getTransaction().hasErrors()) {
                getJournal().update(getTransaction());
            }

        } catch (Exception e) {
            this._addError(getTransaction(),
                    getSession().getLabel("CP_MSG_0139") + retour.getIdRetour() + " - " + e.toString());
        }

        return true;
    }

    /**
     * Execution de la génération des décisions
     * 
     * @param idRetour
     * @return
     */
    protected boolean genererDecisionByIdRetour(String idRetour) {

        try {
            this._executeBoucleRetour(idRetour);
            if (!getTransaction().hasErrors()) {
                getJournal().update(getTransaction());
            }

        } catch (Exception e) {
            this._addError(getTransaction(),
                    getSession().getLabel("CP_MSG_0139") + retour.getIdRetour() + " - " + e.toString());
        }

        return true;
    }

    public String getDescriptionTiers() {
        return descriptionTiers;
    }

    @Override
    protected String getEMailObject() {

        if (getMemoryLog().hasErrors() || !processOk) {
            return getSession().getLabel("PROCRECEPTGENDEC_EMAIL_OBJECT_FAILED");
        }

        return getSession().getLabel("SUJET_EMAIL_RECEPTION_CALCUL");
    }

    public String getForIdPlausibilite() {
        return forIdPlausibilite;
    }

    public String getForStatus() {
        return forStatus;
    }

    public float getFranchise(BProcess process) {
        // Pour le mode preanumerando, le montant de la franchise était valable
        // pour toute l'année soit 12* montant mensuel.
        // Depuis le mode postnumerando, le nombre de mois est déterminer depuis
        // le
        // début de la période de décision (ou le début de l'âge AVS si c'est
        // l'année ou
        // l'affilié atteint l'âge AVS) jusqu'à la fin de la decision.
        float mFranchise = 0;
        try {
            // Recherche du montant de franchise mensuel
            mFranchise = Float.parseFloat(FWFindParameter.findParameter(getTransaction(), "10500030", "FRANCHISE",
                    newDecision.getDebutDecision(), "", 0));
            String dateAvs = this.getTiers().getDateAvs();
            int anneeAvs = JACalendar.getYear(dateAvs);
            // Recherche de l'âge AVS
            int moisDebut = 0;
            int moisFin = 0;
            boolean exerciceSur2Annee = false;
            int anneeDebutExercice = JACalendar.getYear(newDecision.getDebutExercice1());
            int anneeFinExercice = JACalendar.getYear(newDecision.getFinExercice1());
            if ((anneeDebutExercice != anneeFinExercice) && newDecision.getTaxation().equalsIgnoreCase("N")
                    && newDecision.getDebutActivite().equals(new Boolean(true))) {
                exerciceSur2Annee = true;
            }
            if (exerciceSur2Annee) {
                if (BSessionUtil.compareDateBetweenOrEqual(process.getSession(), newDecision.getDebutExercice1(),
                        newDecision.getFinExercice1(), dateAvs)) {
                    moisDebut = JACalendar.getMonth(dateAvs) + 1;
                    moisFin = JACalendar.getMonth(newDecision.getFinExercice1());
                    if (JACalendar.getYear(dateAvs) < JACalendar.getYear(newDecision.getFinExercice1())) {
                        moisFin = moisFin + 12;
                    }
                } else {
                    moisDebut = JACalendar.getMonth(newDecision.getDebutExercice1());
                    moisFin = JACalendar.getMonth(newDecision.getFinExercice1()) + 12;
                }
            } else {
                // Voir CPProcessReceptionGenererDecisionTest, si un changement se fait dans cette partie de code, il
                // faudrait adapter le
                // test unitaire
                moisDebut = JACalendar.getMonth(newDecision.getDebutDecision());
                moisFin = JACalendar.getMonth(newDecision.getFinDecision());
                int varNum = Integer.parseInt(newDecision.getNombreMoisTotalDecision());
                if (varNum != 0) {
                    // Recaler la date de début et de fin par rapport à la
                    // période totale
                    int vNum = (moisDebut + varNum) - 1;
                    if (vNum <= 12) { // décalage du mois de fin
                        moisFin = vNum;
                    } else { // Décalage du mois de début
                        vNum = (moisFin - varNum) + 1;
                        if (vNum >= 1) {
                            moisDebut = vNum;
                        } else { // Période ne tenant pas dans la décision
                            moisDebut = 1;
                            moisFin = varNum;
                        }
                    }
                    // Nouveau code pour corriger la problématique des rentiers (K160704_001)
                    if (newDecision.getAnneeDecision().equalsIgnoreCase(Integer.toString(anneeAvs))
                            && moisDebut < (JACalendar.getMonth(dateAvs) + 1)) {
                        moisDebut = JACalendar.getMonth(dateAvs) + 1;
                    }
                }
                if (newDecision.getAnneeDecision().equalsIgnoreCase(Integer.toString(anneeAvs))
                        && BSessionUtil.compareDateFirstLower(getSession(), newDecision.getDebutDecision(), dateAvs)) {
                    moisDebut = JACalendar.getMonth(dateAvs) + 1;
                }
            }
            // Calcul de la franchise
            return mFranchise * ((moisFin - moisDebut) + 1);

        } catch (Exception e) {
            this._addError(process.getTransaction(), " Echec lors du calcul de la franchise pour "
                    + getDescriptionTiers() + " année " + newDecision.getAnneeDecision());
            this._addError(process.getTransaction(), e.toString());
            return 0;
        }
    }

    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdJournal() {
        return idJournal;
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

    // **************************************
    // * Getter
    // **************************************

    public String[] getListIdJournalRetour() {
        return listIdJournalRetour;
    }

    public ArrayList<String> getListIdNonTraite() {
        return listIdNonTraite;
    }

    public String[] getListIdRetour() {
        return listIdRetour;
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

    public String getTillNumAffilie() {
        return tillNumAffilie;
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

    private void initRevenu(CPDecisionViewBean newDecision, ICommunicationRetour retour) throws Exception {
        newDecision.setRevenu1(retour.getRevenu1());
        if (newDecision.isNonActif()) {
            newDecision.setMontantTotalRenteAVS(retour.getMontantTotalRenteAVS());
        }
        newDecision.setRevenu2("");
        newDecision.setRevenuAutre1(retour.getRevenu2());
        newDecision.setRevenuAutre2("");
        // VS -> comme dans la communication, il y a aussi le revenu du conjoint
        // => pour les conjoints indépendants, il faut prendre les revenus
        // distincts de chaque personne
        // cad 1 fois celle du contribuable et 1 fois celle du conjoint
        // (traitementConjoint=true)
        if ("620001".equalsIgnoreCase(getJournal().getCanton())) {
            CPSedexDonneesBase donnee = new CPSedexDonneesBase();
            donnee.setIdRetour(retour.getIdRetour());
            donnee.setSession(getSession());
            donnee.retrieve();
            if (!donnee.isNew()
                    && !(newDecision.getAffiliation().getTypeAffiliation().equalsIgnoreCase(CodeSystem.TYPE_AFFILI_TSE) || newDecision
                            .getAffiliation().getTypeAffiliation()
                            .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE))) {
                if (!newDecision.isNonActif()) {
                    BigDecimal mRevenu = new BigDecimal(0);
                    BigDecimal mRevenuAgricole = new BigDecimal(0);
                    if (traitementConjoint) {
                        if (!JadeStringUtil.isEmpty(donnee.getIncomeFromSelfEmploymentCjt())) {
                            mRevenu = new BigDecimal(JANumberFormatter.deQuote(donnee.getIncomeFromSelfEmploymentCjt()));
                        }
                        if (!JadeStringUtil.isEmpty(donnee.getMainIncomeInAgricultureCjt())) {
                            mRevenuAgricole = new BigDecimal(JANumberFormatter.deQuote(donnee
                                    .getMainIncomeInAgricultureCjt()));
                        }
                        newDecision.setCapital(donnee.getCapitalCjt());
                    } else {
                        if (!JadeStringUtil.isEmpty(donnee.getIncomeFromSelfEmployment())) {
                            mRevenu = new BigDecimal(JANumberFormatter.deQuote(donnee.getIncomeFromSelfEmployment()));
                        }
                        if (!JadeStringUtil.isEmpty(donnee.getMainIncomeInAgriculture())) {
                            mRevenuAgricole = new BigDecimal(JANumberFormatter.deQuote(donnee
                                    .getMainIncomeInAgriculture()));
                        }
                        newDecision.setCapital(donnee.getCapital());
                    }
                    mRevenu = mRevenu.subtract(mRevenuAgricole);
                    // Soustraire le revenu agricole au revenu car celui-ci est compris dedans
                    newDecision.setRevenu1(mRevenu.toString());
                    newDecision.setRevenuAutre1(mRevenuAgricole.toString());
                } else {
                    String renteContribuable = "0";

                    if (!JadeStringUtil.isEmpty(donnee.getPensionIncome())) {
                        renteContribuable = donnee.getPensionIncome();
                    }
                    // Si un des conjoints est non actif, cumuler les revenus de rente même si l'autre est indépendant
                    String renteConjoint = "0";
                    if (!JadeStringUtil.isEmpty(donnee.getPensionIncomeCjt())) {
                        renteConjoint = donnee.getPensionIncomeCjt();
                    }
                    double renteCouple = Double.valueOf(JANumberFormatter.deQuote(renteContribuable)).doubleValue()
                            + Double.valueOf(JANumberFormatter.deQuote(renteConjoint)).doubleValue();
                    newDecision.setRevenu1(String.valueOf(renteCouple));
                    newDecision.setRevenu2("");
                    newDecision.setRevenuAutre1(""); // pour K150708_003
                }
            }
            // incident I130822_024 -> dans inforom 550: Ne plus cumuler les revenus IND et TSE
            // On pourrait avoir un couple IND/TSE donc le seul moyen est d'intervenir au moment de la génération et non
            // au moment de
            // la réception de la communication fiscales
            // Si décision TSE => Soustraire le montant d'indépendant si renseigné
            // Si décision <> TSE => Soustraire le montant TSE
            BigDecimal mRevenuInd = new BigDecimal(0);
            BigDecimal mRevenuTSE = new BigDecimal(0);
            String revenu1 = newDecision.getRevenu1();
            if (JadeStringUtil.isEmpty(revenu1)) {
                revenu1 = "0";
            }
            if (newDecision.getAffiliation().getTypeAffiliation().equalsIgnoreCase(CodeSystem.TYPE_AFFILI_TSE)
                    || newDecision.getAffiliation().getTypeAffiliation()
                            .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE)) {
                if (traitementConjoint) {
                    mRevenuTSE = new BigDecimal(JANumberFormatter.deQuote(donnee.getEmploymentIncomeCjt()));
                } else {
                    mRevenuTSE = new BigDecimal(JANumberFormatter.deQuote(donnee.getEmploymentIncome()));
                }
                newDecision.setRevenu1(mRevenuTSE.toString());
            } else if (!(newDecision.getAffiliation().getTypeAffiliation().equalsIgnoreCase(CodeSystem.TYPE_AFFILI_TSE) || newDecision
                    .getAffiliation().getTypeAffiliation().equalsIgnoreCase(CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE))
                    && !JadeStringUtil.isEmpty(donnee.getEmploymentIncome())) {
                if (!traitementConjoint && !JadeStringUtil.isEmpty(donnee.getEmploymentIncome())) {
                    mRevenuTSE = new BigDecimal(JANumberFormatter.deQuote(donnee.getEmploymentIncome()));
                } else if (traitementConjoint && !JadeStringUtil.isEmpty(donnee.getEmploymentIncomeCjt())) {
                    mRevenuTSE = new BigDecimal(JANumberFormatter.deQuote(donnee.getEmploymentIncomeCjt()));
                }
                mRevenuInd = new BigDecimal(JANumberFormatter.deQuote(revenu1));
                mRevenuInd = mRevenuInd.subtract(mRevenuTSE);
                newDecision.setRevenu1(mRevenuInd.toString());
            }

            calculLPP(newDecision, donnee);

        } else if (IConstantes.CS_LOCALITE_CANTON_VALAIS.equalsIgnoreCase(getJournal().getCanton())) {
            CPCommunicationFiscaleRetourVSViewBean retourVS = new CPCommunicationFiscaleRetourVSViewBean();
            retourVS.setSession(getSession());
            retourVS.setIdRetour(retour.getIdRetour());
            retourVS.retrieve();
            if (!retourVS.isNew()) {
                if (retour.isNonActif(traitementConjoint)) {
                    newDecision.setFortuneTotale(new FWCurrency(Float.parseFloat(JANumberFormatter.deQuote(retourVS
                            .getVsFortunePriveeCtb()))
                            + Float.parseFloat(JANumberFormatter.deQuote(retourVS.getVsFortunePriveeConjoint()))
                            + Float.parseFloat(JANumberFormatter.deQuote(retourVS
                                    .getVsCapitalPropreEngageEntrepriseCtb()))
                            + Float.parseFloat(JANumberFormatter.deQuote(retourVS
                                    .getVsCapitalPropreEngageEntrepriseConjoint()))).toString());
                    newDecision.setRevenu1(new FWCurrency(Float.parseFloat(JANumberFormatter.deQuote(retourVS
                            .getVsRevenuRenteCtb()))
                            + Float.parseFloat(JANumberFormatter.deQuote(retourVS.getVsRevenuRenteConjoint())))
                            .toString());
                } else {
                    if (traitementConjoint) {
                        newDecision.setRevenu1(retourVS.getVsRevenuNonAgricoleConjoint());
                        newDecision.setRevenuAutre1(retourVS.getVsRevenuAgricoleConjoint());
                        newDecision.setCapital(retourVS.getVsCapitalPropreEngageEntrepriseConjoint());
                    } else {
                        newDecision.setRevenu1(retourVS.getVsRevenuNonAgricoleCtb());
                        newDecision.setRevenuAutre1(retourVS.getVsRevenuAgricoleCtb());
                        newDecision.setCapital(retourVS.getVsCapitalPropreEngageEntrepriseCtb());
                    }
                }
            }
        } else if (IConstantes.CS_LOCALITE_CANTON_NEUCHATEL.equalsIgnoreCase(getJournal().getCanton())) {
            if (retour.isNonActif(traitementConjoint)) {
                float sommeRevenu = 0;
                CPCommunicationFiscaleRetourNEViewBean retourNE = new CPCommunicationFiscaleRetourNEViewBean();
                retourNE.setSession(getSession());
                retourNE.setIdRetour(retour.getIdRetour());
                retourNE.retrieve();
                if (!retourNE.isNew()) {
                    sommeRevenu = Float.parseFloat(retourNE.getNePension());
                    sommeRevenu += Float.parseFloat(retourNE.getNePensionAlimentaire());
                    sommeRevenu += Float.parseFloat(retourNE.getNeRenteViagere());
                    sommeRevenu += Float.parseFloat(retourNE.getNeIndemniteJour());
                    sommeRevenu += Float.parseFloat(retourNE.getNeRenteTotale());
                    retour.setRevenu1(Float.toString(sommeRevenu));
                }
            }
        }
    }

    /**
     * @param decision
     * @return
     */
    private boolean isDecisionIdentiqueProvisoire(CPDecision decision) {
        CPDonneesCalcul donCalcul = new CPDonneesCalcul();
        donCalcul.setSession(getSession());
        CPDecision decisionDeBase;
        try {
            decisionDeBase = CPDecision._returnDecisionBase(getSession(), decision.getIdDecision());
            if (decisionDeBase == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        String montant = CPCotisation.getTotalCotisation(getSession(), decision.getIdDecision(),
                CodeSystem.PERIODICITE_ANNUELLE);
        String montantDeBase = CPCotisation.getTotalCotisation(getSession(), decisionDeBase.getIdDecision(),
                CodeSystem.PERIODICITE_ANNUELLE);
        if (montant.equals(montantDeBase)) {
            return true;
        } else {
            return false;
        }
    }

    public void calculLPP(CPDecisionViewBean decisionViewBean, CPSedexDonneesBase donnee) {
        int rachatLPPDonneBase = 0;
        int revenu = 0;
        int revenuAgricole = 0;

        if (donnee.getPurchasingLPP() != "") {
            rachatLPPDonneBase = Integer.parseInt(JANumberFormatter.deQuote(donnee.getPurchasingLPP()));
        }
        if (donnee.getEmploymentIncome() != "") {
            revenu = Integer.parseInt(JANumberFormatter.deQuote(donnee.getIncomeFromSelfEmployment()));
        }

        double revenus = ((double) revenu + revenuAgricole);

        // Ne pas tenir compte du rachat LPP si les revenus sont inférieur à 0
        if (revenus >= 0.0) {
            double rachatLPPCalcule = rachatLPPDonneBase / 2.0;

            if (rachatLPPCalcule > revenus / 2.0) {
                rachatLPPCalcule = revenus / 2.0;
            }

            rachatLPPCalcule = Math.abs(rachatLPPCalcule);

            decisionViewBean.setRachatLPP(String.valueOf(JANumberFormatter.format(rachatLPPCalcule, 1.0, 0,
                    JANumberFormatter.SUP)));
        }
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Récupération du journal
     * 
     * @param idRetour
     * @return
     * @throws Exception
     */
    protected CPJournalRetour recuperationJournal(String idRetour, String idJournalRetour) throws Exception {

        if (!JadeStringUtil.isEmpty(idRetour)) {
            CPCommunicationFiscaleRetourViewBean communication = new CPCommunicationFiscaleRetourViewBean();
            communication.setSession(getSession());
            communication.setIdRetour(idRetour);
            communication.retrieve();
            idJournalRetour = communication.getIdJournalRetour();
        }

        // Si aucun journal, on retourne null
        if (JadeStringUtil.isEmpty(idJournalRetour)) {
            return null;
        }

        CPJournalRetour jrn = new CPJournalRetour();
        jrn.setSession(getSession());
        jrn.setIdJournalRetour(idJournalRetour);
        jrn.retrieve(getTransaction());
        if ((jrn == null) || jrn.isNew()) { // Si aucun journal, on retourne null
            // avec un message
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0138") + " " + idJournalRetour);
            return null;
        }

        return jrn;
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

    public void setIdJournal(String string) {
        idJournal = string;
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

    public void setListIdJournalRetour(String[] listIdJournalRetour) {
        this.listIdJournalRetour = listIdJournalRetour;
    }

    public void setListIdNonTraite(ArrayList<String> listIdNonTraite) {
        this.listIdNonTraite = listIdNonTraite;
    }

    public void setListIdRetour(String[] listIdRetour) {
        this.listIdRetour = listIdRetour;
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

    public void setTillNumAffilie(String string) {
        tillNumAffilie = string;
    }

    public void setTypeRevenu(String typeRevenu) {
        this.typeRevenu = typeRevenu;
    }

    /**
     * Alimente la table CPValidationCalculCommunication Date de création : (18.03.2003 10:59:30)
     * 
     * @return String
     */
    public void validationCalculCommunicationRetour(CPDecision newDecision) throws Exception {
        BigDecimal mNewCoti = null;
        BigDecimal mOldCoti = null;
        BigDecimal m25PourCent = null;
        // On ajoute dans la table de validation
        CPValidationCalculCommunication validation = new CPValidationCalculCommunication();
        validation.setSession(getSession());
        validation.setIdCommunicationRetour(retour.getIdRetour());
        validation.setIdDecision(newDecision.getIdDecision());
        validation.setDateCalcul(JACalendar.todayJJsMMsAAAA());
        // Recherche nouveau montant de cotisation (selon communication)
        CPCotisation newCoti = CPCotisation._returnCotisation(getSession(), newDecision.getIdDecision(),
                CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
        if (newCoti != null) {
            mNewCoti = new BigDecimal(JANumberFormatter.deQuote(newCoti.getMontantAnnuel()));
            m25PourCent = mNewCoti;
            m25PourCent = mNewCoti.multiply(new BigDecimal(25));
            m25PourCent = m25PourCent.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_EVEN);
            m25PourCent = mNewCoti.subtract(m25PourCent);
            if (mNewCoti.compareTo(new BigDecimal(0)) == 0) {
                validation.setGroupeTaxation(CPValidationCalculCommunication.CS_SANS_CHANGEMENT);
            } else if (mNewCoti.compareTo(new BigDecimal(0)) == 1) {
                validation.setGroupeTaxation(CPValidationCalculCommunication.CS_DEBIT_SUP_25);
            }
            if (retour.getDecisionDeBase() != null) {
                validation.setIdDecisionProvisoire(retour.getDecisionDeBase().getIdDecision());
                CPCotisation oldCoti = CPCotisation._returnCotisation(getSession(), retour.getDecisionDeBase()
                        .getIdDecision(), CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
                if (oldCoti != null) {
                    mOldCoti = new BigDecimal(JANumberFormatter.deQuote(oldCoti.getMontantAnnuel()));
                    // Test si crédit ( si nouvelle cotisation >0
                    // (mOldCoti==null) ou su nouveau montant > ancien montant
                    if (mOldCoti != null) {
                        // Test si crédit
                        if (mNewCoti.compareTo(mOldCoti) == -1) {
                            validation.setGroupeTaxation(CPValidationCalculCommunication.CS_CREDITEUR);
                        } else if (mNewCoti.compareTo(mOldCoti) == 0) {
                            validation.setGroupeTaxation(CPValidationCalculCommunication.CS_SANS_CHANGEMENT); // Pas
                            // de
                            // changement
                        } else if (mOldCoti.compareTo(m25PourCent) == -1) {
                            // Débit > 25%
                            validation.setGroupeTaxation(CPValidationCalculCommunication.CS_DEBIT_SUP_25);
                        } else {
                            // Débit < 25%
                            validation.setGroupeTaxation(CPValidationCalculCommunication.CS_DEBIT_INF_25);
                        }
                    }
                }
            }
        }
        validation.setGroupeExtraction(retour.getIdParametrePlausi());
        validation.add(getTransaction());
    }
}
