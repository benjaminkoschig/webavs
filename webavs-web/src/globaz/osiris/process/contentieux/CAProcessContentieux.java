package globaz.osiris.process.contentieux;

import globaz.framework.printing.FWDocumentListener;
import globaz.framework.printing.itext.FWIScriptDocument;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BApplication;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIEtape;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.OsirisDef;
import globaz.osiris.application.CAApplication;
import globaz.osiris.application.CAParametres;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperationContentieux;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.contentieux.CACalculTaxe;
import globaz.osiris.db.contentieux.CAContentieuxManager;
import globaz.osiris.db.contentieux.CAEtapeManager;
import globaz.osiris.db.contentieux.CAEvenementContentieux;
import globaz.osiris.db.contentieux.CAParametreEtape;
import globaz.osiris.db.contentieux.CAParametreEtapeCalculTaxe;
import globaz.osiris.db.contentieux.CAParametreEtapeCalculTaxeManager;
import globaz.osiris.db.contentieux.CATaxe;
import globaz.osiris.external.IntAdresseCourrier;
import globaz.osiris.external.IntDocumentContentieux;
import globaz.osiris.external.IntTiers;
import globaz.osiris.print.itext.list.CAIJournalContentieux_DS;
import globaz.osiris.print.itext.list.CAIJournalContentieux_Doc;
import globaz.osiris.print.list.CAListParOP;
import globaz.osiris.process.journal.CAComptabiliserJournal;
import globaz.osiris.process.journal.CAUtilsJournal;
import globaz.osiris.translation.CACodeSystem;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * Date de création : (28.05.2002 15:17:01)
 * 
 * @author: Administrator
 */
public class CAProcessContentieux extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static int AK_IDEXTERNE = 1;

    private String dateDelaiPaiement = "";
    private String dateReference = "";
    private String dateSurDocument = "";
    private FWDocumentListener documentListener = new FWDocumentListener();
    private String forIdCategorie = "";
    private String forIdGenreCompte = "";
    private String idSection = "";
    private Boolean imprimerDocument = new Boolean(false);
    private Boolean imprimerListeDeclenchement = new Boolean(true);
    private Boolean imprimerListePoursuiteOffice = new Boolean(false);
    private CAJournal journal = null;
    private String libelleJournal = "";
    private CAListParOP listPours = null;
    private Boolean modePrevisionnel = new Boolean(true);
    private java.util.Vector paramEtapes = new Vector();
    private CAIJournalContentieux_DS report = null;
    private List roles = null;
    private CASection section = null;
    private String selectionTriListeCA = "";
    private String selectionTriListeSection = "";
    private String sequence = "";
    private int shouldNbCas = 0;
    private List typeEtapes = null;
    private List typeSections = null;

    private CAUtilsJournal utils;

    /**
     * Commentaire relatif au constructeur CAProcessContentieux.
     */
    public CAProcessContentieux() {
        super();
    }

    /**
     * Nettoyage après erreur ou exécution <br>
     * Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
        // Remise à null du journal
        journal = null;

        // Terminer le listener de document en cas d'erreur
        if (isOnError() && getImprimerDocument().booleanValue()) {
            getDocumentListener().abort();
        }
    }

    /**
     * Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        // Vérifie que la date sur document ne dépasse pas le délai
        if (!checkDelaiDateValeur(getSession().getCurrentThreadTransaction())) {
            // Si le délai est dépassé, l'erreur est signalée par e-mail.
            this._addError(getTransaction(), getSession().getLabel("ERR_DELAI_DATE_CONT"));
            return false;
        }

        BStatement statement = null;
        CAContentieuxManager manProcC = new CAContentieuxManager();
        manProcC.setSession(getSession());

        CASection elemSection = null;

        try {
            // Vérification des paramètres
            if (JAUtil.isDateEmpty(getDateReference())) {
                getMemoryLog().logMessage("6207", null, FWMessage.FATAL, this.getClass().getName());
            } else {
                try {
                    BSessionUtil.checkDateGregorian(
                            ((BApplication) GlobazServer.getCurrentSystem().getApplication(
                                    OsirisDef.DEFAULT_APPLICATION_OSIRIS)).getAnonymousSession(), getDateReference());
                    setDateReference(new JADate(getDateReference()).toStr("."));
                } catch (Exception e) {
                    getMemoryLog().logMessage("6211", null, FWMessage.FATAL, this.getClass().getName());
                }
            }
            if (JAUtil.isDateEmpty(getDateSurDocument())) {
                getMemoryLog().logMessage("6208", null, FWMessage.FATAL, this.getClass().getName());
            } else {
                try {
                    BSessionUtil.checkDateGregorian(
                            ((BApplication) GlobazServer.getCurrentSystem().getApplication(
                                    OsirisDef.DEFAULT_APPLICATION_OSIRIS)).getAnonymousSession(), getDateSurDocument());
                    setDateSurDocument(new JADate(getDateSurDocument()).toStr("."));
                } catch (Exception e) {
                    getMemoryLog().logMessage("6212", null, FWMessage.FATAL, this.getClass().getName());
                }
            }
            if (!JAUtil.isDateEmpty(getDateDelaiPaiement())) {
                try {
                    BSessionUtil.checkDateGregorian(
                            ((BApplication) GlobazServer.getCurrentSystem().getApplication(
                                    OsirisDef.DEFAULT_APPLICATION_OSIRIS)).getAnonymousSession(),
                            getDateDelaiPaiement());
                    setDateDelaiPaiement(new JADate(getDateDelaiPaiement()).toStr("."));
                } catch (Exception e) {
                    getMemoryLog().logMessage("6214", null, FWMessage.FATAL, this.getClass().getName());
                }
            }
            if (JadeStringUtil.isBlank(getLibelleJournal()) && !getModePrevisionnel().booleanValue()) {
                getMemoryLog().logMessage("6209", null, FWMessage.FATAL, this.getClass().getName());
            }
            if ((getTypeEtapes() == null) || (getTypeEtapes().size() == 0)) {
                getMemoryLog().logMessage("6210", null, FWMessage.FATAL, this.getClass().getName());
            }

            // Contrôle de la période comptable
            if (!getModePrevisionnel().booleanValue()) {
                utils = new CAUtilsJournal();
                if (utils.isInterfaceCgActive(getJournal().getSession())
                        && !utils.isPeriodeComptableOuverte(getJournal().getSession(), getTransaction(), getJournal()
                                .getDateValeurCG())) {
                    getJournal().getMemoryLog().logMessage(getTransaction().getErrors().toString(), FWMessage.FATAL,
                            this.getClass().getName());
                    return false;
                }
            }

            // Sortir en cas d'erreur
            if (getMemoryLog().isOnFatalLevel()) {
                return false;
            }

            // Création de la source de données du journal contentieux
            if (getImprimerListeDeclenchement().booleanValue()) {
                report = new CAIJournalContentieux_DS();
                report.setDate(getDateSurDocument());
                report.setDateReference(getDateReference());
                report.setModePrevisionnel(getModePrevisionnel().booleanValue());
            }

            // Création de la liste des poursuites
            if (getImprimerListePoursuiteOffice().booleanValue()) {
                listPours = new CAListParOP(getSession());
                listPours.setDate(getDateSurDocument());
                listPours.setDateReference(getDateReference());
                listPours.setSelectionTriListeCA(getSelectionTriListeCA());
                listPours.setSelectionTriListeSection(getSelectionTriListeSection());
                listPours.setModePrevisionnel(getModePrevisionnel().booleanValue());
            }

            // Chargement du manager de section
            setState(getSession().getLabel("6201"));
            if (!JadeStringUtil.isBlank(getSequence())) {
                manProcC.setForIdSequenceContentieux(getSequence());
            }

            // Si un rappel ou une sommation est selectionné, on ne sette pas la
            // date de reference
            String rappel = "";
            String sommation = "";
            if (!JadeStringUtil.isBlank(getIdEtape(APIEtape.RAPPEL))) {
                rappel = getIdEtape(APIEtape.RAPPEL);
            }
            if (!JadeStringUtil.isBlank(getIdEtape(APIEtape.SOMMATION))) {
                sommation = getIdEtape(APIEtape.SOMMATION);
            }
            if (!getTypeEtapes().contains(rappel) && !getTypeEtapes().contains(sommation)) {
                manProcC.setForDateReference(getDateReference());
            }
            manProcC.setForTriListeCA(getSelectionTriListeCA());
            manProcC.setForTriListeSection(getSelectionTriListeSection());
            manProcC.setForRoles(getRoles());
            manProcC.setForTypeSections(getTypeSections());
            manProcC.changeManagerSize(0);
            manProcC.setForIdGenreCompte(getForIdGenreCompte());
            manProcC.setForIdCategorie(getForIdCategorie());
            manProcC.setForDateEcheance(getDateReference());
            /*--------------------------------------------------------------------
             Initiliser la progression du process
             ---------------------------------------------------------------------
             */
            shouldNbCas = manProcC.getCount(getTransaction()); // provisoire
            // Entrer les informations pour l' état du process
            setState(getSession().getLabel("6202"));
            if (shouldNbCas > 0) {
                setProgressScaleValue(shouldNbCas);
            } else {
                setProgressScaleValue(1);
            }
            // ---------------------------------------------------------------------
            statement = manProcC.cursorOpen(getTransaction());

            // Parcourir les sections pour voir lesquelles sont des candidates
            // valables
            while ((elemSection = (CASection) manProcC.cursorReadNext(statement)) != null) {
                // Vérifier la condition de sortie
                if (isAborted()) {
                    return false;
                }
                // Test la propriété de osiris.properties :
                // contentieuxAvsUniquement
                if (CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAvsUniquement()) {
                    // Prend uniquement les sections qui ont de l'AVS (rubriques
                    // : 2110.4010.xxxx ; 2110.4000.xxxx)
                    CAOperationManager operations = new CAOperationManager();
                    operations.setSession(getSession());
                    operations.setForIdSection(elemSection.getIdSection());
                    operations.setForEtat(APIOperation.ETAT_COMPTABILISE);
                    ArrayList forRubriqueIn = new ArrayList();
                    forRubriqueIn.add(APIRubrique.ID_EXTERNE_BEGIN_WITH_2110_4000);
                    forRubriqueIn.add(APIRubrique.ID_EXTERNE_BEGIN_WITH_2110_4010);
                    operations.setForRubriqueIn(forRubriqueIn);
                    operations.find();
                    if (operations.isEmpty()) {
                        continue;
                    }
                }

                // Progression
                incProgressCounter();
                FWCurrency _totalTaxes = new FWCurrency();
                FWCurrency _totalFrais = new FWCurrency();
                for (int j = 0; j < typeEtapes.size(); j++) {
                    CAParametreEtape elemParamEtape = getEtape(elemSection.getIdSequenceContentieux(),
                            (String) typeEtapes.get(j));

                    // Contrôle du solde du compte annexe
                    if ((elemParamEtape != null) && (elemParamEtape.getEtapeParametreEtapePrecedente() != null)) {
                        CAEvenementContentieux elemEvenContPrec = elemParamEtape
                                .getEvenementContentieuxPrecedent(elemSection);
                        if (elemEvenContPrec == null) {
                            continue;
                        } else {
                            if ((elemSection != null) && (elemParamEtape != null)
                                    && (Float.parseFloat(elemSection.getCompteAnnexe().getSolde()) == 0.0)) {
                                if (getImprimerListeDeclenchement().booleanValue()) {
                                    report.insertRow(elemSection, elemParamEtape, "", "0.0", elemSection.getSolde(),
                                            getSession().getLabel("6215"));
                                }
                                continue;
                            }
                        }
                    }

                    // Contrôler si la section est bloquée pour le traitement du
                    // contentieux
                    // ET si la date de référence se situe avant la fin de la
                    // date de blocage
                    if (elemSection.getContentieuxEstSuspendu().booleanValue()
                            && BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), elemSection.getDateSuspendu(),
                                    getDateReference())) {
                        // Si l'étape est un rappel ou une sommation ...
                        if (elemParamEtape.getEtape().getTypeEtape().equals(APIEtape.SOMMATION)
                                || elemParamEtape.getEtape().getTypeEtape().equals(APIEtape.RAPPEL)) {
                            // ... on vérifie les motifs de la section
                            // Si le motif est irrecouvrable ou rentier ou
                            // decompte LTN on ne bloque pas
                            boolean hasSecMotifIrrecouvrableOrRentier = elemSection.getIdMotifContentieuxSuspendu()
                                    .equals(CACodeSystem.CS_RENTIER)
                                    || elemSection.getIdMotifContentieuxSuspendu()
                                            .equals(CACodeSystem.CS_IRRECOUVRABLE)
                                    || elemSection.getIdMotifContentieuxSuspendu().equals(CACodeSystem.CS_DECOMPTE_LTN);
                            if (hasSecMotifIrrecouvrableOrRentier) {
                                // Rien faire, traite
                            } else {
                                continue; // Bloquer, Ne traite pas
                            }
                        } else {
                            continue; // Bloquer, Ne traite pas
                        } // Contrôler si le compte annexe est bloqué pour le
                          // traitement du contentieux
                    } else if (elemSection.getCompteAnnexe().getContEstBloque().booleanValue()) {
                        // Contrôler si la date de référence se situe dans la
                        // période de blocage
                        boolean isInPeriodeBloquee = BSessionUtil.compareDateBetweenOrEqual(getSession(), elemSection
                                .getCompteAnnexe().getContDateDebBloque(), elemSection.getCompteAnnexe()
                                .getContDateFinBloque(), getDateReference());
                        // Si le compte annexe se trouve dans la période
                        // bloquée, on ne le traite pas
                        if (isInPeriodeBloquee) {
                            // Si l'étape est un rappel ou une sommation ...
                            if (elemParamEtape.getEtape().getTypeEtape().equals(APIEtape.SOMMATION)
                                    || elemParamEtape.getEtape().getTypeEtape().equals(APIEtape.RAPPEL)) {
                                // ... on vérifie les motifs de blocage du CA et
                                // de la section
                                // Si le motif est irrecouvrable ou rentier on
                                // ne bloque pas
                                boolean hasCAMotifIrrecouvrableOrRentier = elemSection.getCompteAnnexe()
                                        .getIdContMotifBloque().equals(CACodeSystem.CS_RENTIER)
                                        || elemSection.getCompteAnnexe().getIdContMotifBloque()
                                                .equals(CACodeSystem.CS_IRRECOUVRABLE);
                                if (hasCAMotifIrrecouvrableOrRentier) {
                                    // Rien faire, traite
                                } else {
                                    continue; // Bloquer, Ne traite pas
                                }
                            } else {
                                continue; // Bloquer, Ne traite pas
                            }
                        }
                    }

                    // Etape pas encore déclenchée ou vide
                    if (elemParamEtape != null) {
                        CATaxe _taxe;
                        CAEvenementContentieux elemEvenCont = new CAEvenementContentieux();
                        elemEvenCont.setSession(getSession());
                        elemEvenCont.setAlternateKey(CAEvenementContentieux.AK_IDSECPARAM);
                        elemEvenCont.setIdSection(elemSection.getIdSection());
                        elemEvenCont.setIdParametreEtape(elemParamEtape.getIdParametreEtape());
                        // sch -->
                        // elemEvenCont.setIdParametreEtape(elemParamEtape.getIdEtape());
                        elemEvenCont.retrieve(getTransaction());

                        // Sortir si est ignorée est vrai
                        if (elemEvenCont.getEstIgnoree().booleanValue()) {
                            continue;
                        }

                        // Sortir si déclenché
                        if (elemEvenCont.getEstDeclenche().booleanValue()) {
                            continue;
                        }

                        // Test si l'étape précédente a été déclenchée ou est
                        // ignorée
                        if (elemParamEtape.getEtapeParametreEtapePrecedente() != null) {
                            CAEvenementContentieux elemEvenContPrec = elemParamEtape
                                    .getEvenementContentieuxPrecedent(elemSection);
                            if (elemEvenContPrec == null) {
                                continue;
                            } else {
                                // Test étape précédente déclenchée
                                if (!elemEvenContPrec.getEstDeclenche().booleanValue()
                                        && !elemEvenContPrec.getEstIgnoree().booleanValue()) {
                                    continue;
                                }
                            }
                        }

                        // Calcul de la date de déclenchement
                        String _dateDecl = "";
                        _dateDecl = elemParamEtape.getDateDeclenchement(elemSection);

                        // Fin si erreur
                        if (elemParamEtape.hasErrors() || elemParamEtape.isNew()) {
                            throw new Exception(getSession().getLabel("7392") + " elemParamEtape pour CA : "
                                    + getSection().getCompteAnnexe().getIdExterneRole());
                        }
                        if (JadeStringUtil.isBlank(_dateDecl)
                                || !BSessionUtil.compareDateFirstLowerOrEqual(
                                        ((BApplication) GlobazServer.getCurrentSystem().getApplication(
                                                OsirisDef.DEFAULT_APPLICATION_OSIRIS)).getAnonymousSession(),
                                        _dateDecl, getDateReference())) {
                            continue;
                        }

                        // Contrôle du solde de la section avec le solde limite
                        // de déclenchement
                        if ((elemParamEtape != null)
                                && (Float.parseFloat(elemSection.getSolde()) <= Float.parseFloat(elemParamEtape
                                        .getSoldelimitedeclenchement()))) {
                            if (getImprimerListeDeclenchement().booleanValue()) {
                                report.insertRow(elemSection, elemParamEtape, _dateDecl, "0.0", elemSection.getSolde(),
                                        getSession().getLabel("6200"));
                            }
                            continue;
                        }

                        // Préparer un nouveau document pour l'impression
                        IntDocumentContentieux doc = elemParamEtape.getInstanceDocumentContentieux();
                        doc.setTiers(elemSection.getCompteAnnexe().getTiers());
                        doc.setTiersOfficePoursuites(elemSection.getCompteAnnexe().getTiers());
                        doc.setSection(elemSection);
                        doc.setModePrevisonnel(getModePrevisionnel());
                        doc.setParametreEtape(elemParamEtape);
                        doc.setDate(getDateSurDocument());
                        doc.setDateDelaiPaiement(getDateDelaiPaiement());
                        doc.setISession(getSession());
                        String domaine = elemSection.getCompteAnnexe()._getDefaultDomainFromRole();
                        if (elemParamEtape.getEvenementContentieuxPrecedent(elemSection) != null) {
                            doc.setDatePremierRappel(elemParamEtape.getEvenementContentieuxPrecedent(elemSection)
                                    .getDateExecution());
                        }
                        if (elemParamEtape.getEtape().getTypeEtape().equalsIgnoreCase(APIEtape.POURSUITE)
                                || elemParamEtape.getEtape().getTypeEtape().equalsIgnoreCase(APIEtape.CONTINUER)
                                || elemParamEtape.getEtape().getTypeEtape().equalsIgnoreCase(APIEtape.VENTE)) {
                            doc.setAdresseCourrier(elemSection.getCompteAnnexe().getTiers()
                                    .getAdresseCourrier(IntAdresseCourrier.POURSUITE, domaine));
                        } else {
                            doc.setAdresseCourrier(elemSection.getCompteAnnexe().getTiers()
                                    .getAdresseCourrier(IntAdresseCourrier.CORRESPONDANCE, domaine));
                        }

                        // Calcul des taxes
                        CAParametreEtapeCalculTaxeManager _paEtCalTaMan = elemParamEtape.getParamEtapCalculTaxes();
                        if (_paEtCalTaMan != null) {
                            for (int k = 0; k < _paEtCalTaMan.size(); k++) {
                                CAParametreEtapeCalculTaxe _paEtCalTaElem = (CAParametreEtapeCalculTaxe) _paEtCalTaMan
                                        .getEntity(k);
                                CACalculTaxe _calculTaxe = _paEtCalTaElem.getCalculTaxe();
                                if (_calculTaxe.hasErrors() || _calculTaxe.isNew()) {
                                    throw new Exception(getSession().getLabel("5205") + " pour CA : "
                                            + getSection().getCompteAnnexe().getIdExterneRole());
                                }

                                _taxe = _calculTaxe.calculTaxe(elemSection);
                                // Stocker la taxe pour l'impression
                                if (_taxe != null) {
                                    _totalTaxes.add(_taxe.getMontantTaxe());
                                    doc.addTaxe(_taxe);
                                }
                            }
                        }

                        // Edition du document
                        if (getImprimerDocument().booleanValue()) {
                            doc.beforePrint(getTransaction());
                            if (doc.isPrintable()) {
                                doc.afterPrint(getTransaction());
                                elemParamEtape.getListeDocuments().add(doc);
                            }
                        }

                        // Liste de déclenchement
                        if (getImprimerListeDeclenchement().booleanValue()) {
                            // Informer si le montant de base
                            // (solde-compensation/paiement) est inférieur ou
                            // égal à zéro, cas douteux
                            FWCurrency fMontant = elemSection.getSoldeToCurrency();
                            fMontant.sub(elemSection.getPmtCmp());
                            if (fMontant.isNegative() || fMontant.isZero()) {
                                report.insertRow(elemSection, elemParamEtape, _dateDecl, _totalTaxes.toString(),
                                        elemSection.getSolde(), getSession().getLabel("CONTENTIEUX_CAS_DOUTEUX"));
                            } else {
                                report.insertRow(elemSection, elemParamEtape, _dateDecl, _totalTaxes.toString(),
                                        elemSection.getSolde(), "");
                            }
                        }

                        // Préparation de la liste pour les offices de
                        // poursuites
                        if (getImprimerListePoursuiteOffice().booleanValue()) {
                            if (doc.hasTaxes()) {
                                Enumeration fraisListe = doc.listTaxes();
                                while (fraisListe.hasMoreElements()) {
                                    CATaxe frais = (CATaxe) fraisListe.nextElement();
                                    if (frais.getCalculTaxe().isFraisPoursuite()) {
                                        _totalFrais.add(frais.getMontantTaxeToCurrency());
                                    }
                                }
                            }
                            listPours.insertRow(elemSection, elemParamEtape, _dateDecl, elemSection.getSolde(),
                                    _totalFrais.toString());
                        }

                        // Imputation de la taxe en comptabilité
                        if (!getModePrevisionnel().booleanValue() && elemParamEtape.getImputerTaxe().booleanValue()
                                && doc.hasTaxes()) {
                            Enumeration taxes = doc.listTaxes();
                            while (taxes.hasMoreElements()) {
                                CATaxe taxe = (CATaxe) taxes.nextElement();
                                if (!taxe.getMontantTaxeToCurrency().isZero()) {
                                    CAEcriture ecriture = new CAEcriture();
                                    ecriture.setSession(getSession());
                                    ecriture.setIdJournal(getJournal().getIdJournal());
                                    ecriture.setIdCompteAnnexe(elemSection.getIdCompteAnnexe());
                                    ecriture.setIdSection(elemSection.getIdSection());
                                    ecriture.setDate(getDateSurDocument());
                                    ecriture.setMontant(taxe.getMontantTaxe());
                                    ecriture.setCodeDebitCredit(APIEcriture.DEBIT);
                                    ecriture.setIdCompte(taxe.getCalculTaxe().getIdRubrique());
                                    ecriture.add(getTransaction());
                                    // Fin si erreur
                                    if (ecriture.hasErrors() || ecriture.isNew()) {
                                        throw new Exception(getSession().getLabel("5185") + " pour CA : "
                                                + getSection().getCompteAnnexe().getIdExterneRole());
                                    }
                                }
                            }
                        }

                        // Création ou mise à jour de l'événement contentieux
                        if (!getModePrevisionnel().booleanValue()) {
                            if (elemEvenCont != null) {
                                elemEvenCont.setSession(getSession());
                                elemEvenCont.setMotifJournalisation(CAEvenementContentieux.MOTEDITION);
                                elemEvenCont.setDateExecution(getDateSurDocument());
                                elemEvenCont.setDateDeclenchement(_dateDecl);
                                elemEvenCont.setEstDeclenche(new Boolean(true));
                                elemEvenCont.setIdTiers(elemSection.getCompteAnnexe().getIdTiers());
                                FWCurrency montant = new FWCurrency();
                                montant.add(elemSection.getSolde());
                                montant.add(_totalTaxes);
                                String dom = elemSection.getCompteAnnexe()._getDefaultDomainFromRole();
                                elemEvenCont.setMontant(montant.toString());
                                elemEvenCont.setTaxes(_totalTaxes.toString());
                                if (elemParamEtape.getEtape().getTypeEtape().equalsIgnoreCase(APIEtape.POURSUITE)
                                        || elemParamEtape.getEtape().getTypeEtape()
                                                .equalsIgnoreCase(APIEtape.CONTINUER)
                                        || elemParamEtape.getEtape().getTypeEtape().equalsIgnoreCase(APIEtape.VENTE)) {
                                    elemEvenCont.setIdAdresse(elemSection.getCompteAnnexe().getTiers()
                                            .getIdAdresseCourrier(IntAdresseCourrier.POURSUITE, dom));
                                } else {
                                    elemEvenCont.setIdAdresse(elemSection.getCompteAnnexe().getTiers()
                                            .getIdAdresseCourrier(IntAdresseCourrier.CORRESPONDANCE, dom));
                                }

                                // Office ptes
                                IntTiers tOfp = elemSection.getCompteAnnexe().getTiers().getOfficePoursuites();
                                if (tOfp != null) {
                                    elemEvenCont.setIdTiersOfficePoursuites(tOfp.getIdTiers());
                                }

                                // Création d'une opération événement
                                // contentieux
                                CAOperationContentieux opEvenContentieux = new CAOperationContentieux();
                                opEvenContentieux.setSession(getSession());
                                opEvenContentieux.setEvenementContentieux(elemEvenCont);
                                opEvenContentieux.setIdJournal(getJournal().getIdJournal());
                                opEvenContentieux.add(getTransaction());
                                // Fin si Erreur
                                if (opEvenContentieux.hasErrors() || opEvenContentieux.isNew()) {
                                    throw new Exception(getSession().getLabel("5188") + " pour CA : "
                                            + getSection().getCompteAnnexe().getIdExterneRole());
                                }

                                // Mise à jour événement
                                elemEvenCont.setIdOperation(opEvenContentieux.getIdOperation());
                                if (elemEvenCont.isNew()) {
                                    elemEvenCont.add(getTransaction());
                                } else {
                                    elemEvenCont.update(getTransaction());
                                }

                                // Fin si erreur
                                if (getTransaction().getSession().hasErrors()) {
                                    throw new Exception(getSession().getLabel("5187") + " pour CA : "
                                            + getSection().getCompteAnnexe().getIdExterneRole());
                                }
                            }
                        }
                    }
                } // END FOR
            } // END WHILE

            // Imprimer le rapport
            if (getImprimerListeDeclenchement().booleanValue()) {
                setState(getSession().getLabel("6203"));
                CAIJournalContentieux_Doc impressionProcess = new CAIJournalContentieux_Doc(getSession());
                impressionProcess.setDate(getDateSurDocument());
                impressionProcess.setDateReference(getDateReference());
                impressionProcess.setModePrevisionnel(getModePrevisionnel().booleanValue());
                impressionProcess.bindData(report);
                impressionProcess.setSendCompletionMail(false);
                impressionProcess.executeProcess();
                String _list = impressionProcess.getExporter().getExportNewFilePath();

                // Numéro de référence Inforom
                JadePublishDocumentInfo documentInfo = createDocumentInfo();
                documentInfo.setDocumentTypeNumber(CAIJournalContentieux_Doc.NUMERO_REFERENCE_INFOROM);

                this.registerAttachedDocument(documentInfo, _list);
            }

            // Imprimer la liste des poursuites
            if (getImprimerListePoursuiteOffice().booleanValue()) {
                setState(getSession().getLabel("6216"));

                listPours.setFinalizeSheet();
                this.registerAttachedDocument(listPours.getOutputFile());
            }

            // Comptabiliser les taxes
            if (!getModePrevisionnel().booleanValue() && (journal != null)) {
                setState(getSession().getLabel("6204"));
                new CAComptabiliserJournal().comptabiliser(this, getJournal());
                getJournal().update(getTransaction());
                if (getJournal().hasErrors() || getJournal().isNew()) {
                    throw new Exception(getSession().getLabel("5600"));
                }

                // Imprimer le journal des écritures
                journal.imprimerJournal(this);
            }

            // Préparer la fusion
            if (getImprimerDocument().booleanValue()) {
                this.registerDocuments();
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            if (elemSection != null) {
                getMemoryLog().logMessage("IdSection:" + elemSection.getIdSection(), FWMessage.FATAL,
                        this.getClass().getName());
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (statement != null) {
                    manProcC.cursorClose(statement);
                    statement = null;
                }
            } catch (Exception e) {
            }
        }
        return !isOnError();
    }

    /**
     * Verifie que la "date sur documents" soit avant le "delai date de valeur" paramétré dans FWPARP.
     * 
     * @author: sel Créé le : 16 janv. 07
     * @param transaction
     * @return false en cas d'erreurs ou date sur Document > dateLimite
     */
    public boolean checkDelaiDateValeur(BTransaction transaction) {
        boolean check = true;
        try {
            JACalendar cal = new JACalendarGregorian();
            // Détermine la date limite : date du jour + Xjours (selon paramètre
            // dans FWPARP).
            JADate dateLimite = cal.addDays(JACalendar.today(), CAParametres.getDelaiDateValeur(transaction));
            JADate dateDocument = new JADate(getDateSurDocument());

            // SI dateDocument > dateLimite ALORS retourne false
            if (cal.compare(dateDocument, dateLimite) == JACalendar.COMPARE_FIRSTUPPER) {
                check = false;
            }
        } catch (JAException e) {
            check = false;
        } catch (Exception e) {
            check = false;
        }
        return check;
    }

    /**
     * Date de création : (27.06.2002 09:07:52)
     * 
     * @return String
     */
    public String getDateDelaiPaiement() {
        return dateDelaiPaiement;
    }

    /**
     * Date de création : (28.05.2002 16:52:06)
     * 
     * @return String
     */
    public String getDateReference() {
        return dateReference;
    }

    /**
     * Date de création : (28.05.2002 16:51:31)
     * 
     * @return String
     */
    public String getDateSurDocument() {
        return dateSurDocument;
    }

    /**
     * Date de création : (03.07.2002 09:00:14)
     * 
     * @return globaz.framework.printing.FWDocumentListener
     */
    public FWDocumentListener getDocumentListener() {
        return documentListener;
    }

    /**
     * Date de création : (14.02.2002 14:22:21)
     * 
     * @return String
     */
    @Override
    protected String getEMailObject() {
        if (getMemoryLog().hasErrors()) {
            return getSession().getLabel("6206") + " " + getIdSection();
        } else {
            return getSession().getLabel("6205") + " " + getIdSection();
        }
    }

    /**
     * Date de création : (06.06.2002 12:24:35)
     * 
     * @return globaz.osiris.db.contentieux.CAParametreEtape
     * @param idSeqContentieux
     *            String
     * @param sequence
     *            String
     */
    private CAParametreEtape getEtape(String idSeqContentieux, String etape) throws Exception {
        int size = paramEtapes.size();
        CAParametreEtape tempEtape = null;
        for (int i = 0; i < size; i++) {
            tempEtape = (CAParametreEtape) paramEtapes.elementAt(i);
            if (tempEtape.getIdSequenceContentieux().equals(idSeqContentieux) && tempEtape.getIdEtape().equals(etape)) {
                return tempEtape;
            }
        }

        CAParametreEtape newParam = new CAParametreEtape();
        newParam.setSession(getSession());
        newParam.setAlternateKey(CAParametreEtape.AK_IDETAPE_SEQ_CONT);
        newParam.setIdEtape(etape);
        newParam.setIdSequenceContentieux(idSeqContentieux);
        newParam.retrieve();
        if (!newParam.isNew() && !newParam.hasErrors()) {
            paramEtapes.add(newParam);
        } else {
            return null;
        }

        return newParam;
    }

    /**
     * @return
     */
    public String getForIdCategorie() {
        return forIdCategorie;
    }

    /**
     * @return
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * @author: sel Créé le : 16 nov. 06
     * @param typeEtape
     * @return idEtape ou null en cas de problème
     */
    private String getIdEtape(String typeEtape) {
        CAEtapeManager etapeManager = new CAEtapeManager();
        try {
            etapeManager.setSession(getSession());
            etapeManager.setForTypeEtape(typeEtape);
            etapeManager.find();
            if (etapeManager.getSize() != 0) {
                return etapeManager.getFirstEntity().getId();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Date de création : (28.05.2002 16:30:14)
     * 
     * @return String
     */
    public String getIdSection() {
        return idSection;
    }

    /**
     * Date de création : (28.05.2002 16:41:25)
     * 
     * @return Boolean
     */
    public Boolean getImprimerDocument() {
        return imprimerDocument;
    }

    /**
     * Date de création : (28.05.2002 16:41:25)
     * 
     * @return Boolean
     */
    public Boolean getImprimerListeDeclenchement() {
        return imprimerListeDeclenchement;
    }

    /**
     * Date de création : (28.05.2002 16:41:25)
     * 
     * @return Boolean
     */
    public Boolean getImprimerListePoursuiteOffice() {
        return imprimerListePoursuiteOffice;
    }

    /**
     * Date de création : (01.07.2002 18:26:33)
     * 
     * @return globaz.osiris.db.comptes.CAJournal
     */
    private CAJournal getJournal() throws Exception {

        if (journal == null) {
            journal = new CAJournal();
            journal.setSession(getSession());
            journal.setDateValeurCG(getDateSurDocument());
            journal.setLibelle(getLibelleJournal());
            journal.add(getTransaction());

            // Si erreur
            if (journal.isNew() || journal.hasErrors()) {
                getMemoryLog().logStringBuffer(journal.getSession().getErrors(), journal.getClass().getName());
                getMemoryLog().logMessage("5225", null, FWMessage.FATAL, this.getClass().getName());
                journal = null;
            }
        }

        return journal;
    }

    /**
     * Date de création : (03.06.2002 12:10:09)
     * 
     * @return String
     */
    public String getLibelleJournal() {
        return libelleJournal;
    }

    /**
     * Date de création : (29.05.2002 15:54:42)
     * 
     * @return boolean
     */
    public Boolean getModePrevisionnel() {
        return modePrevisionnel;
    }

    /**
     * Date de création : (29.05.2002 13:09:20)
     * 
     * @return List
     */
    public List getRoles() {
        return roles;
    }

    /**
     * Date de création : (13.03.2002 17:37:02)
     * 
     * @return globaz.osiris.db.comptes.CASection
     */
    public CASection getSection() {
        // Si pas déjà chargé
        if (section == null) {
            try {
                section = new CASection();
                section.setSession(getSession());
                section.setIdSection(getIdSection());
                section.retrieve(getTransaction());
                if (section.hasErrors() || section.isNew()) {
                    getMemoryLog().logMessage("7231", getIdSection(), FWMessage.FATAL, this.getClass().getName());
                    section = null;
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                section = null;
            }
        }

        // Forcer la transaction
        if (getTransaction() != null) {
            section.setSession(getSession());
        }

        return section;
    }

    /**
     * Date de création : (08.07.2002 13:42:12)
     * 
     * @return String
     */
    public String getSelectionTriListeCA() {
        return selectionTriListeCA;
    }

    /**
     * Date de création : (08.07.2002 13:42:34)
     * 
     * @return String
     */
    public String getSelectionTriListeSection() {
        return selectionTriListeSection;
    }

    /**
     * Date de création : (28.05.2002 16:50:06)
     * 
     * @return String
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Date de création : (06.06.2002 14:23:08)
     * 
     * @return java.util.Vector
     */
    public List getTypeEtapes() {
        return typeEtapes;
    }

    /**
     * Date de création : (29.05.2002 13:09:20)
     * 
     * @return List
     */
    public List getTypeSections() {
        return typeSections;
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
     * @author Sébastien Chappatte Cette méthode prépare les documents pdf pour les rappels puis les fusionnent en un
     *         seul fichier pdf nommé Doc_rappelBVR
     */
    private void registerDocuments() throws Exception {

        // Préparation des documents de fusion
        setState(getSession().getLabel("6213"));
        setProgressScaleValue(paramEtapes.size());

        // Parcourir les étapes
        Enumeration e = paramEtapes.elements();
        while (e.hasMoreElements()) {
            CAParametreEtape pe = (CAParametreEtape) e.nextElement();
            FWIScriptDocument classDoc = null;
            // S'il y a des documents
            if (!pe.getListeDocuments().isEmpty()) {

                // Récupérer la première instance
                IntDocumentContentieux doc = (IntDocumentContentieux) pe.getListeDocuments().get(0);

                // Récupérer la classe du document
                classDoc = doc.getFWIDocumentClass();
                classDoc.setParentWithCopy(this);
                classDoc.setDeleteOnExit(true);
                classDoc.bindObject(pe.getListeDocuments());
                classDoc.getExporter().setExportFileName("Doc_rappelBVR");
                classDoc.executeProcess();
                /*
                 * // Créer le fichier XML for (int i = 0; i < pe.getListeDocuments().size(); i++){
                 * classDoc.bindObject(pe.getListeDocuments().elementAt(i)); classDoc.setDeleteOnExit(true);
                 * classDoc.setTemplateFile("temp"); } // créer le fichier PDF commune // Donne la liste des documents
                 * PDF imprimer wrapperIt.addAll(classDoc.getBatchList()); // Indique le lieu ou se trouve les fichiers
                 * wrapperIt.setPath(classDoc.getPathRoot()); // Indique le nom du fichier
                 * wrapperIt.setFileName("Doc_rappelBVR"); wrapperIt.createOutputFile(); concatFile =
                 * wrapperIt.getNewFilePath();
                 */// enregistre le fichier PDF dans les documents attachés
                   // registerAttachedDocument(concatFile);
            }
            // Incrémenter la progression
            incProgressCounter();
        }
    }

    /**
     * Date de création : (27.06.2002 09:07:52)
     * 
     * @param newDateDelaiPaiement
     *            String
     */
    public void setDateDelaiPaiement(String newDateDelaiPaiement) {
        dateDelaiPaiement = newDateDelaiPaiement;
    }

    /**
     * Date de création : (28.05.2002 16:52:06)
     * 
     * @param newDateReference
     *            String
     */
    public void setDateReference(String newDateReference) {
        dateReference = newDateReference;
    }

    /**
     * Date de création : (28.05.2002 16:51:31)
     * 
     * @param newDateSurDocument
     *            String
     */
    public void setDateSurDocument(String newDateSurDocument) {
        dateSurDocument = newDateSurDocument;
    }

    /**
     * @param string
     */
    public void setForIdCategorie(String string) {
        forIdCategorie = string;
    }

    /**
     * @param string
     */
    public void setForIdGenreCompte(String s) {
        forIdGenreCompte = s;
    }

    /**
     * Date de création : (28.05.2002 16:30:14)
     * 
     * @param newIdSection
     *            String
     */
    public void setIdSection(String newIdSection) {
        idSection = newIdSection;
    }

    /**
     * Date de création : (29.05.2002 16:09:13)
     */
    public void setImprimerDocument(Boolean newImprimerDocument) {
        imprimerDocument = newImprimerDocument;
    }

    /**
     * Date de création : (29.05.2002 16:09:13)
     */
    public void setImprimerListeDeclenchement(Boolean newImprimerListeDeclenchement) {
        imprimerListeDeclenchement = newImprimerListeDeclenchement;
    }

    /**
     * Date de création : (29.05.2002 16:09:13)
     */
    public void setImprimerListePoursuiteOffice(Boolean newImprimerListePoursuiteOffice) {
        imprimerListePoursuiteOffice = newImprimerListePoursuiteOffice;
    }

    /**
     * Date de création : (03.06.2002 12:10:09)
     * 
     * @param newLibelleJournal
     *            String
     */
    public void setLibelleJournal(String newLibelleJournal) {
        libelleJournal = newLibelleJournal;
    }

    /**
     * Date de création : (29.05.2002 16:09:13)
     */
    public void setModePrevisionnel(Boolean newModePrevisionnel) {
        modePrevisionnel = newModePrevisionnel;
    }

    /**
     * Date de création : (29.05.2002 13:09:20)
     * 
     * @param newRoles
     *            List
     */
    public void setRoles(List newRoles) {
        roles = newRoles;
    }

    /**
     * Date de création : (08.07.2002 13:42:12)
     * 
     * @param newSelectionTriListeCA
     *            String
     */
    public void setSelectionTriListeCA(String newSelectionTriListeCA) {
        selectionTriListeCA = newSelectionTriListeCA;
    }

    /**
     * Date de création : (08.07.2002 13:42:34)
     * 
     * @param newSelectionTriListeSection
     *            String
     */
    public void setSelectionTriListeSection(String newSelectionTriListeSection) {
        selectionTriListeSection = newSelectionTriListeSection;
    }

    /**
     * Date de création : (28.05.2002 16:50:06)
     * 
     * @param newSequence
     *            String
     */
    public void setSequence(String newSequence) {
        sequence = newSequence;
    }

    /**
     * Date de création : (06.06.2002 14:23:08)
     * 
     * @param newTypeEtapes
     *            List
     */
    public void setTypeEtapes(List newTypeEtapes) {
        typeEtapes = newTypeEtapes;
    }

    /**
     * Date de création : (29.05.2002 13:09:20)
     * 
     * @param newTypeSections
     *            List
     */
    public void setTypeSections(List newTypeSections) {
        typeSections = newTypeSections;
    }

}
