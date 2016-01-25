package globaz.osiris.process.contentieux;

import globaz.framework.printing.FWDocumentListener;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIDocument;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIEtape;
import globaz.osiris.api.OsirisDef;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperationContentieux;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.contentieux.CACalculTaxe;
import globaz.osiris.db.contentieux.CAContentieuxManager;
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
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

public class CAProcessContentieuxAIJ extends BProcess {

    private static final long serialVersionUID = -3582954262908902836L;

    public final static int AK_IDEXTERNE = 1;

    private java.lang.String dateDelaiPaiement = new String();
    private java.lang.String dateReference = new String();
    private java.lang.String dateSurDocument = new String();
    private globaz.framework.printing.FWDocumentListener documentListener = new FWDocumentListener();
    private java.lang.String idSection = new String();
    private Boolean imprimerDocument = new Boolean(false);
    private Boolean imprimerListeDeclenchement = new Boolean(false);
    private Boolean imprimerListePoursuiteOffice = new Boolean(false);
    private globaz.osiris.db.comptes.CAJournal journal = null;
    private java.lang.String libelleJournal = new String();
    private globaz.osiris.print.list.CAListParOP listPours = null;
    private Boolean modePrevisionnel = new Boolean(true);
    private java.util.Vector paramEtapes = new Vector();
    private globaz.osiris.print.itext.list.CAIJournalContentieux_DS report = null;
    private List roles = null;
    private globaz.osiris.db.comptes.CASection section = null;
    private java.lang.String selectionTriListeCA = new String();
    private java.lang.String selectionTriListeSection = new String();
    private java.lang.String sequence = new String();
    private int shouldNbCas = 0;
    private List typeEtapes = null;

    private List typeSections = null;

    /**
     * Commentaire relatif au constructeur CAProcessContentieux.
     */
    public CAProcessContentieuxAIJ() {
        super();
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {

        // Remise à null du Journal
        journal = null;

        // Terminer le listener de document en cas d'erreur
        if (isOnError() && getImprimerDocument().booleanValue()) {
            getDocumentListener().abort();
        }
    }

    @Override
    protected boolean _executeProcess() {
        BStatement statement = null;
        CAContentieuxManager manProcC = new CAContentieuxManager();
        manProcC.setSession(getSession());

        // Sous contrôle d'exceptions
        try {

            // Vérification des paramètres
            if (JAUtil.isDateEmpty(getDateReference())) {
                getMemoryLog().logMessage("6207", null, FWMessage.FATAL, this.getClass().getName());
            } else {
                try {
                    globaz.globall.db.BSessionUtil.checkDateGregorian(
                            ((globaz.globall.db.BApplication) globaz.globall.db.GlobazServer.getCurrentSystem()
                                    .getApplication(OsirisDef.DEFAULT_APPLICATION_OSIRIS)).getAnonymousSession(),
                            getDateReference());
                    setDateReference(new JADate(getDateReference()).toStr("."));
                } catch (Exception e) {
                    getMemoryLog().logMessage("6211", null, FWMessage.FATAL, this.getClass().getName());
                }
            }
            if (JAUtil.isDateEmpty(getDateSurDocument())) {
                getMemoryLog().logMessage("6208", null, FWMessage.FATAL, this.getClass().getName());
            } else {
                try {
                    globaz.globall.db.BSessionUtil.checkDateGregorian(
                            ((globaz.globall.db.BApplication) globaz.globall.db.GlobazServer.getCurrentSystem()
                                    .getApplication(OsirisDef.DEFAULT_APPLICATION_OSIRIS)).getAnonymousSession(),
                            getDateSurDocument());
                    setDateSurDocument(new JADate(getDateSurDocument()).toStr("."));
                } catch (Exception e) {
                    getMemoryLog().logMessage("6212", null, FWMessage.FATAL, this.getClass().getName());
                }
            }
            if (!JAUtil.isDateEmpty(getDateDelaiPaiement())) {
                try {
                    globaz.globall.db.BSessionUtil.checkDateGregorian(
                            ((globaz.globall.db.BApplication) globaz.globall.db.GlobazServer.getCurrentSystem()
                                    .getApplication(OsirisDef.DEFAULT_APPLICATION_OSIRIS)).getAnonymousSession(),
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
                listPours = new CAListParOP(this);
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
            manProcC.setForDateReference(getDateReference());
            manProcC.setForTriListeCA(getSelectionTriListeCA());
            manProcC.setForTriListeSection(getSelectionTriListeSection());
            manProcC.setForRoles(getRoles());
            manProcC.setForTypeSections(getTypeSections());
            manProcC.changeManagerSize(0);
            CASection elemSection = null;
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

                // Progression
                incProgressCounter();
                FWCurrency _totalTaxes = new FWCurrency();
                FWCurrency _totalFrais = new FWCurrency();
                for (int j = 0; j < typeEtapes.size(); j++) {
                    CAParametreEtape elemParamEtape = getEtape(elemSection.getIdSequenceContentieux(),

                    (String) typeEtapes.get(j));

                    // Contrôle du solde du compte annexe
                    if (elemParamEtape.getEtapeParametreEtapePrecedente() != null) {
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
                    // Contrôler si le compte annexe est bloqué pour le
                    // traitement du contentieux
                    if (elemSection.getCompteAnnexe().getContEstBloque().booleanValue()) {
                        // Contrôler si la date de référence se situe dans la
                        // prériode de blocage
                        boolean bloque = BSessionUtil.compareDateBetweenOrEqual(getSession(), elemSection
                                .getCompteAnnexe().getContDateDebBloque(), elemSection.getCompteAnnexe()
                                .getContDateFinBloque(), getDateReference());
                        // Si le compte annexe se trouve dans la période
                        // bloquée, on ne le traite pas
                        if (bloque) {
                            continue;
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
                        String _dateDecl = new String();
                        _dateDecl = elemParamEtape.getDateDeclenchement(elemSection);

                        // Fin si erreur
                        if (elemParamEtape.isNew() || elemParamEtape.hasErrors()) {
                            getMemoryLog().logStringBuffer(getTransaction().getErrors(),
                                    elemParamEtape.getClass().getName());
                            return false;
                        }
                        if (JadeStringUtil.isBlank(_dateDecl)
                                || !globaz.globall.db.BSessionUtil.compareDateFirstLowerOrEqual(
                                        ((globaz.globall.db.BApplication) globaz.globall.db.GlobazServer
                                                .getCurrentSystem()
                                                .getApplication(OsirisDef.DEFAULT_APPLICATION_OSIRIS))
                                                .getAnonymousSession(), _dateDecl, getDateReference())) {
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
                                if (_calculTaxe.isNew() || _calculTaxe.hasErrors()) {
                                    getMemoryLog().logStringBuffer(getTransaction().getErrors(),
                                            _calculTaxe.getClass().getName());
                                    getMemoryLog().logMessage("5205", null, FWMessage.FATAL, this.getClass().getName());
                                    return false;
                                }

                                _taxe = _calculTaxe.calculTaxe(elemSection);
                                _totalTaxes.add(_taxe.getMontantTaxe());
                                // Stocker la taxe pour l'impression
                                if (_taxe != null) {
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
                            report.insertRow(elemSection, elemParamEtape, _dateDecl, _totalTaxes.toString(),
                                    elemSection.getSolde(), "");
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
                                    if (ecriture.isNew() || ecriture.hasErrors()) {
                                        getMemoryLog().logStringBuffer(getTransaction().getErrors(),
                                                ecriture.getClass().getName());
                                        getMemoryLog().logMessage("5185", null, FWMessage.FATAL,
                                                this.getClass().getName());
                                        return false;
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
                                elemEvenCont.setMontant(montant.toString());
                                elemEvenCont.setTaxes(_totalTaxes.toString());
                                String dom = elemSection.getCompteAnnexe()._getDefaultDomainFromRole();
                                if (elemParamEtape.getEtape().getTypeEtape().equalsIgnoreCase(APIEtape.POURSUITE)
                                        || elemParamEtape.getEtape().getTypeEtape()
                                                .equalsIgnoreCase(APIEtape.CONTINUER)
                                        || elemParamEtape.getEtape().getTypeEtape().equalsIgnoreCase(APIEtape.VENTE)) {
                                    elemEvenCont.setIdAdresse(elemSection.getCompteAnnexe().getTiers()
                                            .getIdAdresseCourrier(IntAdresseCourrier.POURSUITE, domaine));
                                } else {
                                    elemEvenCont.setIdAdresse(elemSection.getCompteAnnexe().getTiers()
                                            .getIdAdresseCourrier(IntAdresseCourrier.CORRESPONDANCE, domaine));
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
                                if (opEvenContentieux.isNew() || opEvenContentieux.hasErrors()) {
                                    getMemoryLog().logStringBuffer(getTransaction().getErrors(),
                                            opEvenContentieux.getClass().getName());
                                    getMemoryLog().logMessage("5188", null, FWMessage.FATAL, this.getClass().getName());
                                    return false;
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
                                    getMemoryLog().logStringBuffer(getTransaction().getErrors(),
                                            elemEvenCont.getClass().getName());
                                    getMemoryLog().logMessage("5187", null, FWMessage.FATAL, this.getClass().getName());
                                    return false;
                                }
                            }
                        }
                    }
                }
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
                this.registerAttachedDocument(_list);
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
                if (getJournal().isNew() || getJournal().hasErrors()) {
                    getMemoryLog().logStringBuffer(getTransaction().getErrors(), journal.getClass().getName());
                    getMemoryLog().logMessage("5600", null, FWMessage.FATAL, this.getClass().getName());
                    return false;
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

    public java.lang.String getDateDelaiPaiement() {
        return dateDelaiPaiement;
    }

    public java.lang.String getDateReference() {
        return dateReference;
    }

    public java.lang.String getDateSurDocument() {
        return dateSurDocument;
    }

    public FWDocumentListener getDocumentListener() {
        return documentListener;
    }

    @Override
    protected String getEMailObject() {
        if (getMemoryLog().hasErrors()) {
            return getSession().getLabel("6206") + " " + getIdSection();
        } else {
            return getSession().getLabel("6205") + " " + getIdSection();
        }
    }

    /**
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

    public java.lang.String getIdSection() {
        return idSection;
    }

    public Boolean getImprimerDocument() {
        return imprimerDocument;
    }

    public Boolean getImprimerListeDeclenchement() {
        return imprimerListeDeclenchement;
    }

    public Boolean getImprimerListePoursuiteOffice() {
        return imprimerListePoursuiteOffice;
    }

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

    public java.lang.String getLibelleJournal() {
        return libelleJournal;
    }

    public Boolean getModePrevisionnel() {
        return modePrevisionnel;
    }

    public List getRoles() {
        return roles;
    }

    public CASection getSection() {
        // Si pas déjà chargé
        if (section == null) {
            try {
                section = new CASection();
                if (getTransaction() != null) {
                    section.setSession(getSession());
                }
                section.setIdSection(getIdSection());
                section.retrieve(getTransaction());
                if (section.isNew() || section.hasErrors()) {
                    getMemoryLog().logMessage("5157", getIdSection(), FWMessage.FATAL, this.getClass().getName());
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

    public java.lang.String getSelectionTriListeCA() {
        return selectionTriListeCA;
    }

    public java.lang.String getSelectionTriListeSection() {
        return selectionTriListeSection;
    }

    public java.lang.String getSequence() {
        return sequence;
    }

    public List getTypeEtapes() {
        return typeEtapes;
    }

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

            // S'il y a des documents
            if (!pe.getListeDocuments().isEmpty()) {

                // Récupérer la première instance
                IntDocumentContentieux doc = (IntDocumentContentieux) pe.getListeDocuments().get(0);

                // Récupérer la classe du document
                BIDocument classDoc = doc.getDocumentClass();

                // Créer le fichier XML
                classDoc.bindObject(pe.getListeDocuments());

                // enregistrer le document pour l'impression
                getDocumentListener().registerDocument(classDoc);

            }

            // Incrémenter la progression
            incProgressCounter();
        }

        // Fin de registration
        getDocumentListener().endRegistering();
    }

    public void setDateDelaiPaiement(java.lang.String newDateDelaiPaiement) {
        dateDelaiPaiement = newDateDelaiPaiement;
    }

    public void setDateReference(java.lang.String newDateReference) {
        dateReference = newDateReference;
    }

    public void setDateSurDocument(java.lang.String newDateSurDocument) {
        dateSurDocument = newDateSurDocument;
    }

    public void setIdSection(java.lang.String newIdSection) {
        idSection = newIdSection;
    }

    public void setImprimerDocument(Boolean newImprimerDocument) {
        imprimerDocument = newImprimerDocument;
    }

    public void setImprimerListeDeclenchement(Boolean newImprimerListeDeclenchement) {
        imprimerListeDeclenchement = newImprimerListeDeclenchement;
    }

    public void setImprimerListePoursuiteOffice(Boolean newImprimerListePoursuiteOffice) {
        imprimerListePoursuiteOffice = newImprimerListePoursuiteOffice;
    }

    public void setLibelleJournal(java.lang.String newLibelleJournal) {
        libelleJournal = newLibelleJournal;
    }

    public void setModePrevisionnel(Boolean newModePrevisionnel) {
        modePrevisionnel = newModePrevisionnel;
    }

    public void setRoles(java.util.Vector newRoles) {
        roles = newRoles;
    }

    public void setSelectionTriListeCA(java.lang.String newSelectionTriListeCA) {
        selectionTriListeCA = newSelectionTriListeCA;
    }

    public void setSelectionTriListeSection(java.lang.String newSelectionTriListeSection) {
        selectionTriListeSection = newSelectionTriListeSection;
    }

    public void setSequence(java.lang.String newSequence) {
        sequence = newSequence;
    }

    public void setTypeEtapes(java.util.Vector newTypeEtapes) {
        typeEtapes = newTypeEtapes;
    }

    public void setTypeSections(java.util.Vector newTypeSections) {
        typeSections = newTypeSections;
    }

}
