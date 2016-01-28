package globaz.phenix.process.documentsItext;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.common.JadePublishUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.musca.db.facturation.FAPassage;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAgenceCommunale;
import globaz.phenix.db.principale.CPDecisionAgenceCommunaleManager;
import globaz.phenix.documentsItext.CPDecision_Ind_Doc;
import globaz.phenix.documentsItext.CPIDecision_Doc;
import globaz.phenix.documentsItext.CPImputation_DOC;
import globaz.phenix.documentsItext.CPLettre_Remise_Doc;
import globaz.phenix.documentsItext.CPLettre_SalarieDispense_Doc;
import globaz.phenix.util.Constante;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.util.TIAdresseResolver;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Dévalida une décision - Enlève l'état validation et remet l'idPassage à blanc Ne peut se faire que si la décision
 * n'est pas en état "facturé" ou "reprise" Date de création : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public class CPProcessImprimerDecisionAgence extends BProcess {

    private static final long serialVersionUID = 2607354542007809402L;

    private final static int NBRE_DECISIONS_PAR_LOT = 500;

    private Boolean affichageEcran = Boolean.FALSE;
    private java.lang.String dateImpression;
    private CPDecisionAgenceCommunale decision = null;
    private CPDecision_Ind_Doc decisionIndDocument;
    private CPIDecision_Doc decisionNacDocument;
    private Boolean duplicata = Boolean.FALSE;
    private Boolean envoiGed = Boolean.FALSE;
    private java.lang.String forAgence = "";
    private java.lang.String forGenreAffilie = "";
    private java.lang.String forOrder = "";
    private java.lang.String forTypeDecision = "";
    private java.lang.String fromAffilieDebut = "";
    private java.lang.String fromAffilieFin = "";
    private java.lang.String idAffiliation = "";
    private java.lang.String idDecision = "";
    private java.lang.String idPassage = "";
    private java.lang.String idTiers = "";
    private Boolean impressionMontantIdentique = Boolean.FALSE;
    private boolean isShortProcess = false;
    private CPImputation_DOC miseEnCompteDocument;
    private CPLettre_Remise_Doc remiseDocument;

    private CPLettre_SalarieDispense_Doc salarieDispenseDocument;

    public CPProcessImprimerDecisionAgence() {
        super();
    }

    public CPProcessImprimerDecisionAgence(BProcess parent) {
        super(parent);
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
    protected boolean _executeProcess() {
        // Variable pour le comptage
        int nbImprimer = 0;
        // Impression des décisions pour le passage sélectionné
        // ---------------------------------------------------------------------
        // compteur du progress
        // long progressCounter = 0;
        // ---------------------------------------------------------------------
        // récupération du wrapper du document
        Vector<CPDecisionAgenceCommunale> decisionNacContainer = new Vector<CPDecisionAgenceCommunale>();
        Vector<CPDecisionAgenceCommunale> decisionNacAdresseContainer = new Vector<CPDecisionAgenceCommunale>();
        Vector<CPDecisionAgenceCommunale> decisionSalarieDispenseContainer = new Vector<CPDecisionAgenceCommunale>();
        Vector<CPDecisionAgenceCommunale> decisionSalarieDispenseAdresseContainer = new Vector<CPDecisionAgenceCommunale>();
        Vector<CPDecisionAgenceCommunale> miseEnCompteContainer = new Vector<CPDecisionAgenceCommunale>();
        Vector<CPDecisionAgenceCommunale> miseEnCompteAdresseContainer = new Vector<CPDecisionAgenceCommunale>();
        Vector<CPDecisionAgenceCommunale> decisionIndContainer = new Vector<CPDecisionAgenceCommunale>();
        Vector<CPDecisionAgenceCommunale> decisionIndAdresseContainer = new Vector<CPDecisionAgenceCommunale>();
        Vector<CPDecisionAgenceCommunale> decisionRemiseContainer = new Vector<CPDecisionAgenceCommunale>();
        Vector<CPDecisionAgenceCommunale> decisionRemiseAdresseContainer = new Vector<CPDecisionAgenceCommunale>();
        boolean useAgence = false;
        try {
            useAgence = ((CPApplication) getSession().getApplication()).isUseAgence();
        } catch (Exception e) {
            useAgence = false;
        }
        //
        try {
            CPDecisionAgenceCommunaleManager manager = new CPDecisionAgenceCommunaleManager();
            manager.setSession(getSession());
            manager.setForIdPassage(getIdPassage());
            manager.setFromAffilieDebut(getFromAffilieDebut());
            manager.setUntilAffilie(getFromAffilieFin());
            manager.setForAgenceCommunale(getForAgence());
            manager.setForGenreAffilie(getForGenreAffilie());
            manager.setForTypeDecision(getForTypeDecision());
            if (useAgence) {
                manager.setForTypeLien("507007"); // Prendre seulement le type
                // de lien agence communale
                // (et non agence privée)
            }
            if (Boolean.TRUE.equals(getImpressionMontantIdentique())) {
                manager.setForImpressionMontantIdentique(Boolean.TRUE);
            }
            if (Boolean.FALSE.equals(getDuplicata())) {
                manager.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
            } else {
                CPDecision dec = new CPDecision();
                dec.setSession(getSession());
                dec.setIdDecision(getIdDecision());
                dec.wantCallMethodAfter(false);
                dec.retrieve();
                if (JadeStringUtil.isEmpty(dec.getDateFacturation())) {
                    manager.setForDateEntreDebutEtFin(dec.getDateInformation());
                } else {
                    manager.setForDateEntreDebutEtFin(dec.getDateFacturation());
                }
            }
            manager.setForIdDecision(getIdDecision());
            // Ne prendre que les décisions à imprimer
            // mais il faut quand même pouvoir la visualiser lors de l'encodage.
            // Encodage= tjs imprimable, batch= prendre que les décisions à
            // imprimer
            if (JadeStringUtil.isIntegerEmpty(getIdDecision())) {
                manager.setForImpression(Boolean.TRUE);
            }
            manager.changeManagerSize(0);
            if (getForOrder().equals("1")) {
                manager.orderByUser();
                manager.orderByNoAffilie();
                manager.orderByAnnee();
                manager.orderByDebutDecision();
            } else if (getForOrder().equals("2")) {
                manager.orderByAgence();
                manager.orderByNoAffilie();
                manager.orderByAnnee();
                manager.orderByDebutDecision();
            } else {
                manager.orderByNoAffilie();
                manager.orderByAnnee();
                manager.orderByDebutDecision();
            }
            // Initialisation iText
            miseEnCompteDocument = new CPImputation_DOC(this);
            miseEnCompteDocument.setParentWithCopy(this);
            decisionNacDocument = new CPIDecision_Doc(this);
            decisionNacDocument.setParentWithCopy(this);
            decisionNacDocument.setDecisionRetroactiveAvecMontantFacture(getSession().getApplication().getProperty(
                    "decisionRetroactiveAvecMontantFacture"));
            decisionIndDocument = new CPDecision_Ind_Doc(this);
            decisionIndDocument.setParentWithCopy(this);
            decisionIndDocument.setDecisionRetroactiveAvecMontantFacture(getSession().getApplication().getProperty(
                    "decisionRetroactiveAvecMontantFacture"));
            salarieDispenseDocument = new CPLettre_SalarieDispense_Doc(this);
            salarieDispenseDocument.setParentWithCopy(this);
            remiseDocument = new CPLettre_Remise_Doc(this);
            remiseDocument.setParentWithCopy(this);
            // compteur pour type de documents
            int k = 0, l = 0, m = 0, n = 0, o = 0;
            // traitement des lignes trouvées
            manager.find();
            CPDecisionAgenceCommunale myDecision = null;
            CPDecisionAgenceCommunale myDecisionAdr = null;
            List<Object[]> adresses = TIAdresseResolver.resolve(manager, "getIdDecision", "519005",
                    IConstantes.CS_AVOIR_ADRESSE_COURRIER, "getNumeroAffilie");
            setProgressScaleValue(adresses.size());
            for (Iterator<Object[]> it = adresses.iterator(); it.hasNext() && !isAborted();) {
                try {
                    boolean impression = true;
                    Object[] obj = it.next();
                    Object[] myDecisionRes = (Object[]) obj[1];
                    myDecision = (CPDecisionAgenceCommunale) myDecisionRes[0];
                    myDecisionAdr = (CPDecisionAgenceCommunale) myDecisionRes[1];
                    // ---------------------------------------------------------------------
                    // setProgressCounter(progressCounter++);
                    incProgressCounter();
                    // ---------------------------------------------------------------------
                    setDecision(myDecision);

                    // Si année préencodée => ne pas imprimer sauf si c'est pour
                    // afficher à l'écran
                    if (Boolean.FALSE.equals(getAffichageEcran())
                            && (Integer.parseInt(myDecision.getAnneeDecision()) > JACalendar.getYear(JACalendar
                                    .todayJJsMMsAAAA()))) {
                        impression = false;
                    }
                    if (impression) {
                        nbImprimer += 1;
                        // type mise en compte
                        if (CPDecision.CS_IMPUTATION.equalsIgnoreCase(getDecision().getTypeDecision())) {
                            miseEnCompteContainer.add(getDecision());
                            miseEnCompteAdresseContainer.add(myDecisionAdr);
                            // incrémenter le compteur sur la mise en compte
                            k++;
                        } else if (CPDecision.CS_REMISE.equalsIgnoreCase(getDecision().getTypeDecision())) {
                            decisionRemiseContainer.add(getDecision());
                            decisionRemiseAdresseContainer.add(myDecisionAdr);
                            // incrémenter le compteur sur la mise en compte
                            o++;
                        }
                        // Genre non actif
                        else if (getDecision().isNonActif()) {
                            if (CPDecision.CS_SALARIE_DISPENSE.equals(getDecision().getSpecification())) {
                                decisionSalarieDispenseContainer.add(getDecision());
                                decisionSalarieDispenseAdresseContainer.add(myDecisionAdr);
                                n++;
                            } else {
                                decisionNacContainer.add(getDecision());
                                decisionNacAdresseContainer.add(myDecisionAdr);
                                l++;
                            }
                        } // Genre indépendant, rentier...
                        else {
                            if (CPDecision.CS_SALARIE_DISPENSE.equals(getDecision().getSpecification())) {
                                decisionSalarieDispenseContainer.add(getDecision());
                                decisionSalarieDispenseAdresseContainer.add(myDecisionAdr);
                                n++;
                            } else {
                                decisionIndContainer.add(getDecision());
                                decisionIndAdresseContainer.add(myDecisionAdr);
                                // incrémenter le compteur sur la décision
                                m++;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("ERROR sur la décision n°=" + getDecision().getIdDecision());
                    JadeLogger.error(this, e);
                }
            } // end while
              // lancer les impressions
              // MISE EN COMPTE
            setProgressScaleValue(adresses.size());
            if (k > 0) {
                try {
                    miseEnCompteDocument.setProcessAppelant(this);
                    miseEnCompteDocument.setContainer(miseEnCompteContainer);
                    miseEnCompteDocument.setContainerAdresse(miseEnCompteAdresseContainer);
                    miseEnCompteDocument.setDateImpression(getDateImpression());
                    miseEnCompteDocument.setTypeDocument(1);
                    miseEnCompteDocument.setSession(getSession());
                    miseEnCompteDocument.setDuplicata(getDuplicata());
                    miseEnCompteDocument.setEnvoiGed(getEnvoiGed());
                    miseEnCompteDocument.setAffichageEcran(getAffichageEcran());
                    miseEnCompteDocument.setRemboursementFraisAdmin(((CPApplication) getSession().getApplication())
                            .isRemboursementFraisAdmin());
                    // EFL begin
                    miseEnCompteDocument.keepDocumentsInChild();
                    // EFL end
                    miseEnCompteDocument.executeProcess();

                    // EFL begin
                    JadePublishDocumentInfo mergedDocumentInfo = createDocumentInfo();
                    mergedDocumentInfo.setDocumentSubject(getSession().getLabel("SUJET_EMAIL_OK_MISEENCOMPTE") + " "
                            + getIdPassage());
                    mergedDocumentInfo.setDocumentTitle("Job " + getIdPassage() + " - "
                            + getSession().getLabel("TITRE_MISEENCOMPTE") + " (" + k + ")");
                    mergedDocumentInfo.setPublishDocument(true);
                    mergedDocumentInfo.setArchiveDocument(false);

                    // On set le numInforom pour pouvoir faire le traitement de publication
                    mergedDocumentInfo.setDocumentTypeNumber(miseEnCompteDocument.getNumInforomForDecision());
                    mergedDocumentInfo.setDuplex(true);

                    List<JadePublishDocument> docs = JadePublishUtil.mergeDocumentList(
                            miseEnCompteDocument.getAttachedDocuments(), "Decision.pdf", mergedDocumentInfo,
                            getEnvoiGed().equals(Boolean.TRUE), CPProcessImprimerDecisionAgence.NBRE_DECISIONS_PAR_LOT);
                    registerDocuments(docs);
                    // EFL end
                } catch (Exception ex) {
                    JadeLogger.error(miseEnCompteDocument, ex);
                    ex.printStackTrace();
                }
            }
            // DECISION NON ACTIF
            if (l > 0) {
                try {
                    // Edition d'une décision
                    // miseEnCompteDocument.executeProcess();
                    decisionNacDocument.setProcessAppelant(this);
                    decisionNacDocument.setContainer(decisionNacContainer);
                    decisionNacDocument.setContainerAdresse(decisionNacAdresseContainer);
                    decisionNacDocument.setTypeDocument(2);
                    decisionNacDocument.setDateImpression(getDateImpression());
                    decisionNacDocument.setSession(getSession());
                    decisionNacDocument.setDuplicata(getDuplicata());
                    decisionNacDocument.setEnvoiGed(getEnvoiGed());
                    decisionNacDocument.setAffichageEcran(getAffichageEcran());
                    decisionNacDocument.setWantLettreCouple(((CPApplication) getSession().getApplication())
                            .isLettreCouple());
                    decisionNacDocument.setAcompteDetailCalcul(((CPApplication) getSession().getApplication())
                            .isAcompteDetailCalcul());
                    // EFL begin
                    decisionNacDocument.keepDocumentsInChild();
                    // EFL end
                    decisionNacDocument.executeProcess();
                    /*
                     * EFL decisionNacDocument.getExporter().setExportFileName("Job " + getIdPassage() + " - " +
                     * getSession().getLabel("TITRE_DECISION_NAC") + " (" + l + ") - ");
                     * setDocList(decisionNacDocument.getExporter());
                     */
                    // EFL begin
                    JadePublishDocumentInfo mergedDocumentInfo = createDocumentInfo();
                    mergedDocumentInfo.setDocumentSubject(getSession().getLabel("SUJET_EMAIL_OK_DECISIONNAC") + " "
                            + getIdPassage());
                    mergedDocumentInfo.setDocumentTitle("Job " + getIdPassage() + " - "
                            + getSession().getLabel("TITRE_DECISION_NAC") + " (" + l + ")");
                    mergedDocumentInfo.setPublishDocument(true);
                    mergedDocumentInfo.setArchiveDocument(false);

                    // On set le numInforom pour pouvoir faire le traitement de publication
                    mergedDocumentInfo.setDocumentTypeNumber(decisionNacDocument.getNumInforomForDecision());
                    mergedDocumentInfo.setDuplex(true);

                    List<JadePublishDocument> docs = JadePublishUtil.mergeDocumentList(
                            decisionNacDocument.getAttachedDocuments(), "Decision.pdf", mergedDocumentInfo,
                            getEnvoiGed().equals(Boolean.TRUE), CPProcessImprimerDecisionAgence.NBRE_DECISIONS_PAR_LOT);
                    if (docs.size() == 1) {
                        JadePublishDocumentInfo docInfo = (docs.get(0)).getPublishJobDefinition().getDocumentInfo();
                        mergedDocumentInfo.setRejectDocument((docInfo.getChildren().get(0)).getRejectDocument());
                        docInfo.setRejectDocument(mergedDocumentInfo.getRejectDocument());
                    }
                    registerDocuments(docs);
                    // EFL end
                } catch (Exception ex2) {
                    JadeLogger.error(decisionNacDocument, ex2);
                    ex2.printStackTrace();
                }
            }
            // DECISION SALARIE DISPENSE
            if (n > 0) {
                try {
                    // Lettre pour salarié dispense
                    salarieDispenseDocument.setProcessAppelant(this);
                    salarieDispenseDocument.setContainer(decisionSalarieDispenseContainer);
                    salarieDispenseDocument.setContainerAdresse(decisionSalarieDispenseAdresseContainer);
                    salarieDispenseDocument.setDateImpression(getDateImpression());
                    salarieDispenseDocument.setTypeDocument(3);
                    salarieDispenseDocument.setSession(getSession());
                    salarieDispenseDocument.setDuplicata(getDuplicata());
                    salarieDispenseDocument.setEnvoiGed(getEnvoiGed());
                    salarieDispenseDocument.setAffichageEcran(getAffichageEcran());
                    // EFL begin
                    salarieDispenseDocument.keepDocumentsInChild();
                    // EFL end
                    salarieDispenseDocument.executeProcess();
                    /*
                     * EFL salarieDispenseDocument.getExporter().setExportFileName( "Job " + getIdPassage() + " - " +
                     * getSession().getLabel("TITRE_DISPENSE") + " (" + n + ") - ");
                     * setDocList(salarieDispenseDocument.getExporter());
                     */
                    // EFL begin
                    JadePublishDocumentInfo mergedDocumentInfo = createDocumentInfo();
                    mergedDocumentInfo.setDocumentSubject(getSession().getLabel("SUJET_EMAIL_OK_LETTREDISPENSE") + " "
                            + getIdPassage());
                    mergedDocumentInfo.setDocumentTitle("Job " + getIdPassage() + " - "
                            + getSession().getLabel("TITRE_DISPENSE") + " (" + n + ")");
                    mergedDocumentInfo.setPublishDocument(true);
                    mergedDocumentInfo.setArchiveDocument(false);

                    // On set le numInforom pour pouvoir faire le traitement de publication
                    mergedDocumentInfo.setDocumentTypeNumber(salarieDispenseDocument.getNumInforomForDecision());
                    mergedDocumentInfo.setDuplex(true);

                    List<JadePublishDocument> docs = JadePublishUtil.mergeDocumentList(
                            salarieDispenseDocument.getAttachedDocuments(), "Decision.pdf", mergedDocumentInfo,
                            getEnvoiGed().equals(Boolean.TRUE), CPProcessImprimerDecisionAgence.NBRE_DECISIONS_PAR_LOT);
                    registerDocuments(docs);
                    // EFL end
                } catch (Exception ex2) {
                    JadeLogger.error(salarieDispenseDocument, ex2);
                    ex2.printStackTrace();
                }
            }
            // DECISION INDEPENDANT
            if (m > 0) {
                try {
                    // Edition d'une décision
                    decisionIndDocument.setProcessAppelant(this);
                    decisionIndDocument.setContainer(decisionIndContainer);
                    decisionIndDocument.setContainerAdresse(decisionIndAdresseContainer);
                    decisionIndDocument.setDateImpression(getDateImpression());
                    decisionIndDocument.setTypeDocument(4);
                    decisionIndDocument.setSession(getSession());
                    decisionIndDocument.setDuplicata(getDuplicata());
                    decisionIndDocument.setEnvoiGed(getEnvoiGed());
                    decisionIndDocument.setWantLettreCouple(((CPApplication) getSession().getApplication())
                            .isLettreCouple());
                    decisionIndDocument.setAcompteDetailCalcul(((CPApplication) getSession().getApplication())
                            .isAcompteDetailCalcul());
                    decisionIndDocument.setAffichageEcran(getAffichageEcran());
                    // EFL begin
                    decisionIndDocument.keepDocumentsInChild();
                    // EFL end
                    decisionIndDocument.executeProcess();
                    /*
                     * EFL decisionIndDocument.getExporter().setExportFileName( "Job " + getIdPassage() + " - " +
                     * getSession().getLabel("TITRE_DECISION_IND") + " (" + m + ") - ");
                     * setDocList(decisionIndDocument.getExporter());
                     */
                    // EFL begin
                    JadePublishDocumentInfo mergedDocumentInfo = createDocumentInfo();
                    mergedDocumentInfo.setDocumentSubject(getSession().getLabel("SUJET_EMAIL_OK_DECISIONIND") + " "
                            + getIdPassage());
                    mergedDocumentInfo.setDocumentTitle("Job " + getIdPassage() + " - "
                            + getSession().getLabel("TITRE_DECISION_IND") + " (" + m + ")");
                    mergedDocumentInfo.setPublishDocument(true);
                    mergedDocumentInfo.setArchiveDocument(false);

                    // On set le numInforom pour pouvoir faire le traitement de publication
                    mergedDocumentInfo.setDocumentTypeNumber(decisionIndDocument.getNumInforomForDecision());
                    mergedDocumentInfo.setDuplex(true);

                    List<JadePublishDocument> docs = JadePublishUtil.mergeDocumentList(
                            decisionIndDocument.getAttachedDocuments(), "Decision.pdf", mergedDocumentInfo,
                            getEnvoiGed().equals(Boolean.TRUE), CPProcessImprimerDecisionAgence.NBRE_DECISIONS_PAR_LOT);
                    if (docs.size() == 1) {
                        JadePublishDocumentInfo docInfo = (docs.get(0)).getPublishJobDefinition().getDocumentInfo();
                        mergedDocumentInfo.setRejectDocument((docInfo.getChildren().get(0)).getRejectDocument());
                        docInfo.setRejectDocument(mergedDocumentInfo.getRejectDocument());
                    }
                    registerDocuments(docs);
                    // EFL end
                } catch (Exception ex2) {
                    JadeLogger.error(decisionIndDocument, ex2);
                    ex2.printStackTrace();
                }
            }
            // Remise
            if (o > 0) {
                try {
                    // Edition d'une décision
                    remiseDocument.setProcessAppelant(this);
                    remiseDocument.setContainer(decisionRemiseContainer);
                    remiseDocument.setContainerAdresse(decisionRemiseAdresseContainer);
                    remiseDocument.setDateImpression(getDateImpression());
                    remiseDocument.setTypeDocument(4);
                    remiseDocument.setSession(getSession());
                    remiseDocument.setDuplicata(getDuplicata());
                    remiseDocument.setEnvoiGed(getEnvoiGed());
                    remiseDocument.setAffichageEcran(getAffichageEcran());
                    // EFL begin
                    remiseDocument.keepDocumentsInChild();
                    // EFL end
                    remiseDocument.executeProcess();
                    /*
                     * EFL remiseDocument.getExporter().setExportFileName( "Job " + getIdPassage() + " - " +
                     * getSession().getLabel("TITRE_REMISE") + " (" + m + ") - ");
                     * setDocList(remiseDocument.getExporter());
                     */
                    // EFL begin
                    JadePublishDocumentInfo mergedDocumentInfo = createDocumentInfo();
                    mergedDocumentInfo.setDocumentSubject(getSession().getLabel("SUJET_EMAIL_OK_DECISION") + " "
                            + getIdPassage());
                    mergedDocumentInfo.setDocumentTitle("Job " + getIdPassage() + " - "
                            + getSession().getLabel("TITRE_REMISE") + " (" + o + ")");
                    mergedDocumentInfo.setPublishDocument(true);
                    mergedDocumentInfo.setArchiveDocument(false);

                    // On set le numInforom pour pouvoir faire le traitement de publication
                    mergedDocumentInfo.setDocumentTypeNumber(remiseDocument.getNumInforomForDecision());
                    mergedDocumentInfo.setDuplex(true);

                    List<JadePublishDocument> docs = JadePublishUtil.mergeDocumentList(remiseDocument
                            .getAttachedDocuments(), "Decision.pdf", mergedDocumentInfo,
                            getEnvoiGed().equals(Boolean.TRUE), CPProcessImprimerDecisionAgence.NBRE_DECISIONS_PAR_LOT);
                    registerDocuments(docs);
                    // EFL end
                } catch (Exception ex2) {
                    JadeLogger.error(remiseDocument, ex2);
                    ex2.printStackTrace();
                }
            }
            // Permet l'affichage des données du processus
            setState(Constante.FWPROCESS_MGS_220);
            if (nbImprimer > 0) {
                getMemoryLog().logMessage(
                        getSession().getLabel("CP_MSG_0149") + " " + nbImprimer + " "
                                + getSession().getLabel("CP_MSG_0149A"), globaz.framework.util.FWMessage.INFORMATION,
                        this.getClass().getName());
            } else {
                getMemoryLog().logMessage(getSession().getLabel("CP_MSG_0148"),
                        globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());
            }
            return true;
        } catch (Exception e) {
            JadeLogger.error(this, e);
            getMemoryLog().logMessage(e.getMessage(), globaz.framework.util.FWMessage.FATAL, this.getClass().getName());
            return false;
        }
    }

    @Override
    protected void _validate() throws java.lang.Exception {
        if (!getAffichageEcran().booleanValue()) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0101"));
            }
        }
        // Si le idDecision est vide nous sommes dans une impression par Lot
        if (JadeStringUtil.isIntegerEmpty(getIdDecision())) {
            if (JadeStringUtil.isIntegerEmpty(getIdPassage())) {
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(getSession().getLabel("CP_MSG_0102"));
            } else {
                FAPassage passage = new FAPassage();
                passage.setSession(getSession());
                passage.setIdPassage(getIdPassage());
                passage.retrieve();
                if (passage.isNew()) {
                    this._addError(getTransaction(), getSession().getLabel("CP_MSG_0039"));
                }
            }
            if (JadeStringUtil.isIntegerEmpty(getDateImpression())) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0147"));
            }
            setSendCompletionMail(true);
        }
        setControleTransaction(true);
    }

    public Boolean getAffichageEcran() {
        return affichageEcran;
    }

    public java.lang.String getDateImpression() {
        return dateImpression;
    }

    public CPDecisionAgenceCommunale getDecision() {
        return decision;
    }

    public Boolean getDuplicata() {
        return duplicata;
    }

    @Override
    protected String getEMailObject() {
        // Déterminer l'objet du message en fonction du code erreur
        String obj = "";
        if (getMemoryLog().hasErrors()) {
            obj = getSession().getLabel("SUJET_EMAIL_PASOK_DECISION") + " " + getIdPassage();
        } else {
            obj = getSession().getLabel("SUJET_EMAIL_OK_DECISION") + " " + getIdPassage();
        }
        // Restituer l'objet
        return obj;
    }

    public Boolean getEnvoiGed() {
        return envoiGed;
    }

    public java.lang.String getForAgence() {
        return forAgence;
    }

    public java.lang.String getForGenreAffilie() {
        return forGenreAffilie;
    }

    public java.lang.String getForOrder() {
        return forOrder;
    }

    public java.lang.String getForTypeDecision() {
        return forTypeDecision;
    }

    public java.lang.String getFromAffilieDebut() {
        return fromAffilieDebut;
    }

    public java.lang.String getFromAffilieFin() {
        return fromAffilieFin;
    }

    public java.lang.String getIdAffiliation() {
        return idAffiliation;
    }

    public java.lang.String getIdDecision() {
        return idDecision;
    }

    public java.lang.String getIdPassage() {
        return idPassage;
    }

    public java.lang.String getIdTiers() {
        return idTiers;
    }

    public Boolean getImpressionMontantIdentique() {
        return impressionMontantIdentique;
    }

    /**
     * Returns the isShortProcess.
     * 
     * @return boolean
     */
    public boolean isShortProcess() {
        return isShortProcess;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // ALD : TO DO parametrer le jobQueue en fonction
        // de la source ( batch ou page web)
        if (isShortProcess()) {
            return GlobazJobQueue.READ_SHORT;
        } else {
            return GlobazJobQueue.UPDATE_LONG;
        }
    }

    public void setAffichageEcran(Boolean affichageEcran) {
        this.affichageEcran = affichageEcran;
    }

    public void setDateImpression(java.lang.String newDateImpression) {
        dateImpression = newDateImpression;
    }

    public void setDecision(CPDecisionAgenceCommunale newDecision) {
        decision = newDecision;
    }

    public void setDuplicata(Boolean duplicata) {
        this.duplicata = duplicata;
    }

    /**
     * @param boolean1
     */
    public void setEnvoiGed(Boolean boolean1) {
        envoiGed = boolean1;
    }

    /**
     * @param string
     */
    public void setForAgence(java.lang.String string) {
        forAgence = string;
    }

    public void setForGenreAffilie(java.lang.String forGenreAffilie) {
        this.forGenreAffilie = forGenreAffilie;
    }

    /**
     * @param string
     */
    public void setForOrder(java.lang.String string) {
        forOrder = string;
    }

    public void setForTypeDecision(java.lang.String forTypeDecision) {
        this.forTypeDecision = forTypeDecision;
    }

    public void setFromAffilieDebut(java.lang.String newFromAffilieDebut) {
        fromAffilieDebut = newFromAffilieDebut;
    }

    public void setFromAffilieFin(java.lang.String newFromAffilieFin) {
        fromAffilieFin = newFromAffilieFin;
    }

    /**
     * Sets the idAffiliation.
     * 
     * @param idAffiliation
     *            The idAffiliation to set
     */
    public void setIdAffiliation(java.lang.String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    /**
     * Sets the idDecision.
     * 
     * @param idDecision
     *            The idDecision to set
     */
    public void setIdDecision(java.lang.String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdPassage(java.lang.String newIdPassage) {
        idPassage = newIdPassage;
    }

    /**
     * Sets the idTiers.
     * 
     * @param idTiers
     *            The idTiers to set
     */
    public void setIdTiers(java.lang.String idTiers) {
        this.idTiers = idTiers;
    }

    public void setImpressionMontantIdentique(Boolean impressionMontantIdentique) {
        this.impressionMontantIdentique = impressionMontantIdentique;
    }

    /**
     * Sets the isShortProcess.
     * 
     * @param isShortProcess
     *            The isShortProcess to set
     */
    public void setIsShortProcess(boolean isShortProcess) {
        this.isShortProcess = isShortProcess;
    }

}
