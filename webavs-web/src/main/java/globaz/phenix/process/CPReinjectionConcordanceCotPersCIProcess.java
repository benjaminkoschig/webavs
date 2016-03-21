package globaz.phenix.process;

import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuelUtil;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliation;
import globaz.phenix.db.principale.CPDecisionAffiliationManager;
import globaz.phenix.listes.excel.CPExcelmlUtils;
import globaz.phenix.listes.excel.ICPListeColumns;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.CPUtil;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.CommonExcelmlContainer;
import globaz.webavs.common.op.CommonExcelDataContainer;
import globaz.webavs.common.op.CommonExcelDataContainer.CommonLine;
import globaz.webavs.common.op.CommonExcelDocumentParser;
import globaz.webavs.common.op.CommonExcelFilterNotSupportedException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author JPA
 * @since 01 juillet 2010
 */
public class CPReinjectionConcordanceCotPersCIProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String NUM_INFOROM_NAME_SPACE = "headerNumInforom";
    private String fileName = null;
    private boolean isAtLessOneError = false;
    private boolean reinjectionEffectue = false;
    BSession sessionPavo = null;

    private boolean status = true;

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        setSendMailOnError(true);

        CommonExcelmlContainer errorContainer = new CommonExcelmlContainer();
        sessionPavo = (BSession) GlobazSystem.getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession(
                getSession());
        CommonExcelDataContainer container = null;
        String path = Jade.getInstance().getHomeDir() + "work/" + getFileName();

        // copy du fichier dans le répertoire work pour le traiter
        JadeFsFacade.copyFile("jdbc://" + Jade.getInstance().getDefaultJdbcSchema() + "/" + getFileName(), path);

        // parse du document, retourne un container avec toutes les entrées et
        // valueres trouvées dans le document
        try {
            container = CommonExcelDocumentParser.parseWorkBook(CPExcelmlUtils.loadPath(path));
        } catch (CommonExcelFilterNotSupportedException e) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_FICHIER_REINJECTION_FILTRE"));
            String messageInformation = "Unsupported filtered Excel file injection throw exception in Parser due to file :"
                    + getFileName() + "\n";
            messageInformation += CPUtil.stack2string(e);
            CPUtil.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());
            status = false;
        } catch (Exception e) {

            this._addError(getTransaction(), getSession().getLabel("ERREUR_FICHIER_REINJECTION"));

            String messageInformation = "Nom du fichier : " + getFileName() + "\n";
            messageInformation += CPUtil.stack2string(e);

            CPUtil.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            status = false;
        }

        // Si le fichier a pu etre parsé, on continu
        if (status) {
            try {
                String numInforom = container
                        .getHeaderValue(CPReinjectionConcordanceCotPersCIProcess.NUM_INFOROM_NAME_SPACE);

                if (JadeStringUtil.isEmpty(numInforom)) {
                    getTransaction().addErrors(getSession().getLabel("NUM_INFOROM_NOT_FOUND"));
                    isAtLessOneError = true;
                    return false;
                } else if (!(numInforom.equals(CPListeExcelConcordanceCotPersCIProcess.NUMERO_INFOROM) || numInforom
                        .equals(CPListeExcelConcordanceCotPersCIProcess.NUMERO_INFOROM))) {
                    getTransaction().addErrors(getSession().getLabel("NUM_INFOROM_NOT_IMPLEMENTED"));
                    isAtLessOneError = true;
                    return false;
                }

                Iterator<CommonLine> lines = container.returnLinesIterator();
                setProgressScaleValue(container.getSize());

                // On récupère les infos de l'entête
                headerReinjectionConcordance(errorContainer, container, numInforom);
                CIJournal journal = getJournalCI();
                // On itère sur chaque ligne
                while (lines.hasNext()) {
                    incProgressCounter();

                    CommonLine line = lines.next();
                    HashMap<String, String> lineMap = line.returnLineHashMap();

                    // On récupère la catégorie de la ligne
                    String numAffilie = returnValeurHashMap(ICPListeColumns.NUM_AFFILIE, lineMap, getTransaction());
                    String categorie = returnValeurHashMapWithNumAffilie(ICPListeColumns.CATEGORIE, lineMap,
                            getTransaction(), numAffilie);

                    if (JadeStringUtil.isBlankOrZero(numAffilie) && !categorie.equals("2") && !categorie.equals("5a")) {
                        CPUtil.addMailInformationsError(getMemoryLog(), ICPListeColumns.NUM_AFFILIE + " "
                                + getSession().getLabel("DOIT_ETRE_RENSEIGNE"), this.getClass().getName());
                        continue;
                    }

                    if (!JadeStringUtil.isBlankOrZero(categorie) && (null != categorie)) {
                        if (categorie.equals("1")) {
                            reinjectionCategorie1et3(errorContainer, getTransaction(), line, lineMap, journal);
                        } else if (categorie.equals("3")) {
                            reinjectionCategorie1et3(errorContainer, getTransaction(), line, lineMap, journal);
                        } else if (categorie.equals("4")) {
                            reinjectionCategorie4(errorContainer, getTransaction(), line, lineMap);
                        } else if (categorie.equals("5b")) {
                            reinjectionCategorie5b(errorContainer, getTransaction(), line, lineMap, journal);
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                    if (isAborted()) {
                        return false;
                    }
                }

                if (isAtLessOneError) {
                    // On imprime la liste des erreurs suivant le document que l'on traite
                    if (numInforom.equals(CPListeExcelConcordanceCotPersCIProcess.NUMERO_INFOROM)) {
                        impressionListeConcordance(errorContainer);
                    }
                }
            } catch (Exception e) {

                this._addError(getTransaction(), getSession().getLabel("ERREUR_REINJECTION"));

                String messageInformation = "Nom du fichier : " + getFileName() + "\n";
                messageInformation += CPUtil.stack2string(e);

                CPUtil.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

                status = false;
            }
        }

        return status;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || !status) {
            return getSession().getLabel("REINJECTION_SUJET_ERROR");
        } else {
            if (isAtLessOneError) {
                return getSession().getLabel("REINJECTION_SUJET_ERROR");
            } else {
                return getSession().getLabel("REINJECTION_SUJET_OK");
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    private CIJournal getJournalCI() throws Exception {
        try {
            CIJournal journalCi = new CIJournal();
            journalCi.setSession(sessionPavo);
            journalCi.setIdTypeInscription(CIJournal.CS_DECISION_COT_PERS);
            journalCi.setLibelle(getSession().getLabel("LISTE_CONCORDANCE_NOM_JOURNAL_CI") + " "
                    + JACalendar.todayJJsMMsAAAA());
            journalCi.add(getTransaction());
            getTransaction().commit();
            return journalCi;
        } catch (Exception e) {
            throw new Exception("creationJournalCi: " + e.getMessage());
        }
    }

    private void headerReinjectionConcordance(CommonExcelmlContainer errorContainer,
            CommonExcelDataContainer container, String numInforom) {
        if (!JadeStringUtil.isEmpty(numInforom)) {
            errorContainer.put(ICPListeColumns.HEADER_NUM_INFOROM, numInforom);
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICPListeColumns.HEADER_NOM_CAISSE))) {
            errorContainer.put(ICPListeColumns.HEADER_NOM_CAISSE,
                    container.getHeaderValue(ICPListeColumns.HEADER_NOM_CAISSE));
        } else {
            errorContainer.put(ICPListeColumns.HEADER_NOM_CAISSE, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICPListeColumns.HEADER_NOM_LISTE))) {
            errorContainer.put(ICPListeColumns.HEADER_NOM_LISTE,
                    container.getHeaderValue(ICPListeColumns.HEADER_NOM_LISTE));
        } else {
            errorContainer.put(ICPListeColumns.HEADER_NOM_LISTE, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICPListeColumns.HEADER_ANNEE))) {
            errorContainer.put(ICPListeColumns.HEADER_ANNEE, container.getHeaderValue(ICPListeColumns.HEADER_ANNEE));
        } else {
            errorContainer.put(ICPListeColumns.HEADER_ANNEE, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICPListeColumns.HEADER_DATE_VISA))) {
            errorContainer.put(ICPListeColumns.HEADER_DATE_VISA,
                    container.getHeaderValue(ICPListeColumns.HEADER_DATE_VISA));
        } else {
            errorContainer.put(ICPListeColumns.HEADER_DATE_VISA, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICPListeColumns.HEADER_CATEGORIE_1))) {
            errorContainer.put(ICPListeColumns.HEADER_CATEGORIE_1,
                    container.getHeaderValue(ICPListeColumns.HEADER_CATEGORIE_1));
        } else {
            errorContainer.put(ICPListeColumns.HEADER_CATEGORIE_1, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICPListeColumns.HEADER_CATEGORIE_2))) {
            errorContainer.put(ICPListeColumns.HEADER_CATEGORIE_2,
                    container.getHeaderValue(ICPListeColumns.HEADER_CATEGORIE_2));
        } else {
            errorContainer.put(ICPListeColumns.HEADER_CATEGORIE_2, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICPListeColumns.HEADER_CATEGORIE_3))) {
            errorContainer.put(ICPListeColumns.HEADER_CATEGORIE_3,
                    container.getHeaderValue(ICPListeColumns.HEADER_CATEGORIE_3));
        } else {
            errorContainer.put(ICPListeColumns.HEADER_CATEGORIE_3, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICPListeColumns.HEADER_CATEGORIE_4))) {
            errorContainer.put(ICPListeColumns.HEADER_CATEGORIE_4,
                    container.getHeaderValue(ICPListeColumns.HEADER_CATEGORIE_4));
        } else {
            errorContainer.put(ICPListeColumns.HEADER_CATEGORIE_4, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICPListeColumns.HEADER_CATEGORIE_5a))) {
            errorContainer.put(ICPListeColumns.HEADER_CATEGORIE_5a,
                    container.getHeaderValue(ICPListeColumns.HEADER_CATEGORIE_5a));
        } else {
            errorContainer.put(ICPListeColumns.HEADER_CATEGORIE_5a, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICPListeColumns.HEADER_CATEGORIE_5b))) {
            errorContainer.put(ICPListeColumns.HEADER_CATEGORIE_5b,
                    container.getHeaderValue(ICPListeColumns.HEADER_CATEGORIE_5b));
        } else {
            errorContainer.put(ICPListeColumns.HEADER_CATEGORIE_5b, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICPListeColumns.HEADER_CATEGORIE_6))) {
            errorContainer.put(ICPListeColumns.HEADER_CATEGORIE_6,
                    container.getHeaderValue(ICPListeColumns.HEADER_CATEGORIE_6));
        } else {
            errorContainer.put(ICPListeColumns.HEADER_CATEGORIE_6, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICPListeColumns.HEADER_REINJECTABLE_2))) {
            errorContainer.put(ICPListeColumns.HEADER_REINJECTABLE_2,
                    container.getHeaderValue(ICPListeColumns.HEADER_REINJECTABLE_2));
        } else {
            errorContainer.put(ICPListeColumns.HEADER_REINJECTABLE_2, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICPListeColumns.HEADER_REINJECTABLE_5a))) {
            errorContainer.put(ICPListeColumns.HEADER_REINJECTABLE_5a,
                    container.getHeaderValue(ICPListeColumns.HEADER_REINJECTABLE_5a));
        } else {
            errorContainer.put(ICPListeColumns.HEADER_REINJECTABLE_5a, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICPListeColumns.HEADER_REINJECTABLE_6))) {
            errorContainer.put(ICPListeColumns.HEADER_REINJECTABLE_6,
                    container.getHeaderValue(ICPListeColumns.HEADER_REINJECTABLE_6));
        } else {
            errorContainer.put(ICPListeColumns.HEADER_REINJECTABLE_6, "");
        }

        errorContainer.put(ICPListeColumns.HEADER_REINJECTABLE_1, "");
        errorContainer.put(ICPListeColumns.HEADER_REINJECTABLE_3, "");
        errorContainer.put(ICPListeColumns.HEADER_REINJECTABLE_4, "");
        errorContainer.put(ICPListeColumns.HEADER_REINJECTABLE_5b, "");

    }

    private void impressionListeConcordance(CommonExcelmlContainer errorContainer) throws Exception, IOException {
        // On imprime le document avec les lignes en erreur
        // On génère le doc
        String nomDoc = getSession().getLabel("LISTE_CONCORDANCE_COTPERD_CI");
        String docPath = CPExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + CPListeExcelConcordanceCotPersCIProcess.MODEL_REINJECTION_NAME, nomDoc, errorContainer);
        publicationDocument(nomDoc, docPath);
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private void publicationDocument(String nomDoc, String docPath) throws IOException {
        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(CPApplication.DEFAULT_APPLICATION_PHENIX);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        this.registerAttachedDocument(docInfo, docPath);
    }

    private void reinjectionCategorie1et3(CommonExcelmlContainer errorContainer, BTransaction transaction,
            CommonLine line, HashMap<String, String> lineMap, CIJournal journalCI) throws Exception {
        // On reprend les infos nécessaire pour la correction
        String numAffilie = returnValeurHashMapWithNumAffilie(ICPListeColumns.NUM_AFFILIE, lineMap, transaction, "");
        String numAvs = returnValeurHashMap(ICPListeColumns.NUM_AVS, lineMap, transaction);
        String idTiers = returnValeurHashMapWithNumAffilie(ICPListeColumns.ID_TIERS, lineMap, transaction, numAffilie);
        String annee = returnValeurHashMapWithNumAffilie(ICPListeColumns.ANNEE, lineMap, transaction, numAffilie);
        String montantSortie = returnValeurHashMapWithNumAffilie(ICPListeColumns.DIFFERENCE_2, lineMap, transaction,
                numAffilie);
        CICompteIndividuelUtil ci = new CICompteIndividuelUtil();
        ci.setSession(sessionPavo);
        ci.setIdJournal(journalCI.getIdJournal());

        CPDecisionAffiliationManager decisionManager = new CPDecisionAffiliationManager();
        decisionManager.setSession(transaction.getSession());
        decisionManager.setForAnneeDecision(annee);
        decisionManager.setForNoAffilie(numAffilie);
        decisionManager.setForExceptTypeDecision(CPDecision.CS_IMPUTATION);
        decisionManager.find();

        CPDecisionAffiliation myDecision = null;
        if (decisionManager.size() > 0) {
            try {
                transaction.clearErrorBuffer();
                myDecision = (CPDecisionAffiliation) decisionManager.getFirstEntity();

                // Récupération du tiers
                TITiersViewBean tiers = new TITiersViewBean();
                tiers.setSession(getSession());
                tiers.setIdTiers(idTiers);
                tiers.retrieve();
                if (JadeStringUtil.isBlankOrZero(numAvs)) {
                    TIHistoriqueAvs hist = new TIHistoriqueAvs();
                    hist.setSession(getSession());
                    try {
                        numAvs = hist.findPrevKnownNumAvs(myDecision.getIdTiers(), "31.12." + annee);
                        if (JadeStringUtil.isEmpty(numAvs)) {
                            numAvs = hist.findNextKnownNumAvs(myDecision.getIdTiers(), "01.01." + annee);
                        }
                    } catch (Exception e) {
                        numAvs = "";
                    }

                    // Si aucun n° trouvé dans historique ou NNSS => prendre
                    // l'actuel n° avs
                    if (JadeStringUtil.isEmpty(numAvs)) {
                        numAvs = tiers.getNumAvsActuel();
                    }
                }
                // Récupération période CI

                String moisDebut = Integer.toString(JACalendar.getMonth(myDecision.getDebutDecision()));
                String moisFin = Integer.toString(JACalendar.getMonth(myDecision.getFinDecision()));
                // Détermination du genre
                String genre = CPUtil.getGenreCI(myDecision, tiers);

                if (!JadeStringUtil.isEmpty(numAvs)) {
                    if (!JadeStringUtil.isBlankOrZero(montantSortie)) {
                        if (CPDecision.CS_IMPUTATION.equals(myDecision.getTypeAffiliation())) {
                            ci.verifieCI(getTransaction(), myDecision.getIdAffiliation(), numAvs, moisDebut, moisFin,
                                    annee, montantSortie, genre, CICompteIndividuelUtil.MODE_DIRECT,
                                    CIEcriture.CS_CODE_MIS_EN_COMTE, CPToolBox._returnCodeSpecial(myDecision), "");
                        } else {
                            ci.verifieCI(getTransaction(), myDecision.getIdAffiliation(), numAvs, moisDebut, moisFin,
                                    annee, montantSortie, genre, CICompteIndividuelUtil.MODE_DIRECT, "",
                                    CPToolBox._returnCodeSpecial(myDecision), "");
                        }
                        reinjectionEffectue = true;
                    }
                }
                // Commit si aucune erreur est présente, sinon on le notifie
                if (transaction.hasErrors()) {
                    CPUtil.fillPhenixContainerWithCPLine(errorContainer, line, ICPListeColumns.listeNoms, transaction
                            .getErrors().toString(), ICPListeColumns.ERREUR);
                    transaction.clearErrorBuffer();
                    transaction.rollback();
                    isAtLessOneError = true;
                } else {
                    transaction.commit();
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(numAffilie + " - " + annee, FWMessage.ERREUR, e.toString());
            }
        } else {
            CPUtil.addMailInformationsError(getMemoryLog(), getSession().getLabel("ERREUR_REINJECTION_PAS_COT_PERS")
                    + " " + numAffilie, this.getClass().getName());
        }
        if (!reinjectionEffectue) {
            CPUtil.addMailInformationsError(getMemoryLog(), getSession().getLabel("ERREUR_REINJECTION_PAS_FAITE") + " "
                    + numAffilie, this.getClass().getName());
        }
    }

    private void reinjectionCategorie4(CommonExcelmlContainer errorContainer, BTransaction transaction,
            CommonLine line, HashMap<String, String> lineMap) throws Exception {
        // On reprend les infos nécessaire pour la correction
        String numAffilie = returnValeurHashMapWithNumAffilie(ICPListeColumns.NUM_AFFILIE, lineMap, transaction, "");
        String idTiers = returnValeurHashMapWithNumAffilie(ICPListeColumns.ID_TIERS, lineMap, transaction, numAffilie);
        String annee = returnValeurHashMapWithNumAffilie(ICPListeColumns.ANNEE, lineMap, transaction, numAffilie);
        String montantEcriture = returnValeurHashMapWithNumAffilie(ICPListeColumns.DIFFERENCE_2, lineMap, transaction,
                numAffilie);

        CPDecisionAffiliationManager decisionManager = new CPDecisionAffiliationManager();
        decisionManager.setSession(transaction.getSession());
        decisionManager.setForAnneeDecision(annee);
        decisionManager.setForNoAffilie(numAffilie);
        decisionManager.setForExceptTypeDecision(CPDecision.CS_IMPUTATION);
        decisionManager.find();

        CPDecisionAffiliation myDecision = null;
        if (decisionManager.size() > 0) {
            try {
                transaction.clearErrorBuffer();
                myDecision = (CPDecisionAffiliation) decisionManager.getFirstEntity();

                CIEcritureManager ecritureMana = new CIEcritureManager();
                ecritureMana.setSession(getSession());
                ecritureMana.setForAnnee(annee);
                ecritureMana.setForIdTiers(idTiers);
                ecritureMana.setForMontant(montantEcriture);
                ecritureMana.find();

                if (ecritureMana.size() > 0) {
                    CIEcriture ecriture = (CIEcriture) ecritureMana.getFirstEntity();
                    ecriture.setIdAffilie(myDecision.getIdAffiliation());
                    ecriture.setForAffiliePersonnel(true);
                    ecriture.update(transaction);
                }
                // Commit si aucune erreur est présente, sinon on le notifie
                if (transaction.hasErrors()) {
                    CPUtil.fillPhenixContainerWithCPLine(errorContainer, line, ICPListeColumns.listeNoms, transaction
                            .getErrors().toString(), ICPListeColumns.ERREUR);
                    transaction.clearErrorBuffer();
                    transaction.rollback();
                    isAtLessOneError = true;
                } else {
                    transaction.commit();
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(numAffilie + " - " + annee, FWMessage.ERREUR, e.toString());
            }
        } else {
            CPUtil.addMailInformationsError(getMemoryLog(), getSession().getLabel("ERREUR_REINJECTION_PAS_COT_PERS")
                    + " " + numAffilie, this.getClass().getName());
        }
    }

    private void reinjectionCategorie5b(CommonExcelmlContainer errorContainer, BTransaction transaction,
            CommonLine line, HashMap<String, String> lineMap, CIJournal journalCI) throws Exception {
        // On reprend les infos nécessaire pour la correction
        String numAffilie = returnValeurHashMapWithNumAffilie(ICPListeColumns.NUM_AFFILIE, lineMap, transaction, "");
        String idTiers = returnValeurHashMapWithNumAffilie(ICPListeColumns.ID_TIERS, lineMap, transaction, numAffilie);
        String annee = returnValeurHashMapWithNumAffilie(ICPListeColumns.ANNEE, lineMap, transaction, numAffilie);
        String numAvs = returnValeurHashMapWithNumAffilie(ICPListeColumns.NUM_AVS, lineMap, transaction, numAffilie);
        String montantCP = returnValeurHashMapWithNumAffilie(ICPListeColumns.MONTANT_COT_PERS, lineMap, transaction,
                numAffilie);
        String montantCI = returnValeurHashMapWithNumAffilie(ICPListeColumns.MONTANT_CI_NOAFFILIE_NOAVS, lineMap,
                transaction, numAffilie);
        CICompteIndividuelUtil ci = new CICompteIndividuelUtil();
        ci.setSession(sessionPavo);
        ci.setIdJournal(journalCI.getIdJournal());

        CPDecisionAffiliationManager decisionManager = new CPDecisionAffiliationManager();
        decisionManager.setSession(transaction.getSession());
        decisionManager.setForAnneeDecision(annee);
        decisionManager.setForNoAffilie(numAffilie);
        decisionManager.setForExceptTypeDecision(CPDecision.CS_IMPUTATION);
        decisionManager.find();

        CPDecisionAffiliation myDecision = null;
        if (decisionManager.size() > 0) {
            try {
                // String numAvs = "";
                transaction.clearErrorBuffer();
                myDecision = (CPDecisionAffiliation) decisionManager.getFirstEntity();

                // Récupération du tiers
                TITiersViewBean tiers = new TITiersViewBean();
                tiers.setSession(getSession());
                tiers.setIdTiers(idTiers);
                tiers.retrieve();
                if (JadeStringUtil.isBlankOrZero(numAvs)) {
                    TIHistoriqueAvs hist = new TIHistoriqueAvs();
                    hist.setSession(getSession());
                    try {
                        numAvs = hist.findPrevKnownNumAvs(myDecision.getIdTiers(), "31.12." + annee);
                        if (JadeStringUtil.isEmpty(numAvs)) {
                            numAvs = hist.findNextKnownNumAvs(myDecision.getIdTiers(), "01.01." + annee);
                        }
                    } catch (Exception e) {
                        numAvs = "";
                    }

                    // Si aucun n° trouvé dans historique ou NNSS => prendre
                    // l'actuel n° avs
                    if (JadeStringUtil.isEmpty(numAvs)) {
                        numAvs = tiers.getNumAvsActuel();
                    }
                }
                // Récupération période CI

                String moisDebut = Integer.toString(JACalendar.getMonth(myDecision.getDebutDecision()));
                String moisFin = Integer.toString(JACalendar.getMonth(myDecision.getFinDecision()));
                // Détermination du genre
                String genre = CPUtil.getGenreCI(myDecision, tiers);

                if (!JadeStringUtil.isEmpty(numAvs)) {
                    // Extourne du total
                    java.math.BigDecimal montantEnCI = new BigDecimal(montantCI);
                    if (montantEnCI.floatValue() != 0) {
                        montantEnCI = montantEnCI.negate();
                        ci.verifieCI(transaction, myDecision.getIdAffiliation(), numAvs, moisDebut, moisFin,
                                myDecision.getAnneeDecision(), montantEnCI.toString(), genre,
                                CICompteIndividuelUtil.MODE_DIRECT, "", CPToolBox._returnCodeSpecial(myDecision), "");
                        reinjectionEffectue = true;
                    }
                    if (!JadeStringUtil.isEmpty(montantCP) && !montantCP.equalsIgnoreCase("0.00")
                            && !montantCP.equalsIgnoreCase("-0.00") && !montantCP.equalsIgnoreCase("0")
                            && !montantCP.equalsIgnoreCase("-0")) {
                        // Inscription du nouveau montant
                        if (CPDecision.CS_IMPUTATION.equals(myDecision.getTypeAffiliation())) {
                            ci.verifieCI(transaction, myDecision.getIdAffiliation(), numAvs, moisDebut, moisFin,
                                    myDecision.getAnneeDecision(), montantCP, genre,
                                    CICompteIndividuelUtil.MODE_DIRECT, CIEcriture.CS_CODE_MIS_EN_COMTE,
                                    CPToolBox._returnCodeSpecial(myDecision), "");
                        } else {
                            ci.verifieCI(transaction, myDecision.getIdAffiliation(), numAvs, moisDebut, moisFin,
                                    myDecision.getAnneeDecision(), montantCP, genre,
                                    CICompteIndividuelUtil.MODE_DIRECT, "", CPToolBox._returnCodeSpecial(myDecision),
                                    "");
                        }
                        reinjectionEffectue = true;
                    }
                }
                // Commit si aucune erreur est présente, sinon on le notifie
                if (transaction.hasErrors()) {
                    CPUtil.fillPhenixContainerWithCPLine(errorContainer, line, ICPListeColumns.listeNoms, transaction
                            .getErrors().toString(), ICPListeColumns.ERREUR);
                    transaction.clearErrorBuffer();
                    transaction.rollback();
                    isAtLessOneError = true;
                } else {
                    transaction.commit();
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(numAffilie + " - " + annee, FWMessage.ERREUR, e.toString());
            }
        } else {
            CPUtil.addMailInformationsError(getMemoryLog(), getSession().getLabel("ERREUR_REINJECTION_PAS_COT_PERS")
                    + " " + numAffilie, this.getClass().getName());
        }
        if (!reinjectionEffectue) {
            CPUtil.addMailInformationsError(getMemoryLog(), getSession().getLabel("ERREUR_REINJECTION_PAS_FAITE") + " "
                    + numAffilie, this.getClass().getName());
        }
    }

    private String returnValeurHashMap(String valeur, HashMap<String, String> lineMap, BTransaction transaction)
            throws Exception {
        String valeurRetour = "";

        if (lineMap.containsKey(valeur)) {
            valeurRetour = lineMap.get(valeur);
        }
        return valeurRetour;
    }

    private String returnValeurHashMapWithNumAffilie(String valeur, HashMap<String, String> lineMap,
            BTransaction transaction, String numAffilie) throws Exception {
        String valeurRetour = "";

        if (lineMap.containsKey(valeur)) {
            valeurRetour = lineMap.get(valeur);
        } else {
            CPUtil.addMailInformationsError(getMemoryLog(),
                    valeur + " " + getSession().getLabel("DOIT_ETRE_RENSEIGNE_POUR") + " " + numAffilie, this
                            .getClass().getName());
        }

        return valeurRetour;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
