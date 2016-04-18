/*
 * Créé le 21 févr. 06
 *
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.ij.process;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.api.basseindemnisation.IIJFormulaireIndemnisation;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisationManager;
import globaz.ij.db.basesindemnisation.IJFormulaireIndemnisation;
import globaz.ij.db.basesindemnisation.IJFormulaireIndemnisationManager;
import globaz.ij.db.prestations.IJIJCalculeeManager;
import globaz.ij.db.prononces.IJMesureJointAgentExecution;
import globaz.ij.db.prononces.IJMesureJointAgentExecutionManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJPrononceManager;
import globaz.ij.itext.IJFormulaires;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.tools.PRDateFormater;

public class IJGenererFormulairesProcess extends BProcess {

    private static final long serialVersionUID = 1L;
    private String annee = null;
    private String anneeDebut = null;
    private String anneeFin = null;
    private String dateDebut = null;
    private String dateFin = null;
    private String dateRetour = "";
    private String dateSurDocument = null;
    private String displaySendToGed = "0";
    private String eMailObject = "";
    private String forIdPrononce = "";
    private Boolean genererFormulaires = null;
    private String impressionFomulairesForEtat = "";
    private Boolean imprimerFormulaires = null;
    private Boolean isSendToGed = Boolean.FALSE;
    private JadePublishDocumentInfo mergedDocInfo = null;

    private String mois = null;
    private String moisDebut = null;

    private String moisFin = null;

    /**
     * Crée une nouvelle instance de la classe IJGenererFormulairesProcess.
     */
    public IJGenererFormulairesProcess() {
        super();
        if (getSession() == null) {
            eMailObject = "Erreur dans la génération des formulaires IJAI";
        } else {
            eMailObject = getSession().getLabel("GENERER_FORMULAIRES_ATTESTATION_ERREUR");
        }
    }

    /**
     * Crée une nouvelle instance de la classe IJGenererFormulairesProcess.
     *
     * @param parent
     *            DOCUMENT ME!
     */
    public IJGenererFormulairesProcess(BProcess parent) {
        super(parent);
        eMailObject = parent.getSession().getLabel("GENERER_FORMULAIRES_ATTESTATION_ERREUR");
    }

    /**
     * Crée une nouvelle instance de la classe IJGenererFormulairesProcess.
     *
     * @param session
     *            DOCUMENT ME!
     */
    public IJGenererFormulairesProcess(BSession session) {
        super(session);
        eMailObject = getSession().getLabel("GENERER_FORMULAIRES_ATTESTATION_ERREUR");
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        boolean succesGenererFormulaires = true;
        boolean succesImprimerFormulaires = true;
        List idsBasesIndemnisationImprimees = new ArrayList();
        setSendMailOnError(true);

        List periodes = createPeriodes();
        Iterator iter = periodes.iterator();

        while (iter.hasNext()) {

            String[] periode = (String[]) iter.next();

            if (genererFormulaires.booleanValue()) {
                if (!genererFormulaires(periode[0], periode[1])) {
                    succesGenererFormulaires = false;
                }
            }
            if (imprimerFormulaires.booleanValue()) {
                if (!imprimerFormulaires(periode[0], periode[1], idsBasesIndemnisationImprimees)) {
                    succesImprimerFormulaires = false;
                }
            }
        }

        JadePublishDocumentInfo docInfo = createDocumentInfo();

        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
        docInfo.setDocumentTitle(getSession().getLabel("DOC_FORMULAIRE_INDEMNISATION_TITLE"));
        docInfo.setDocumentDate(getDateSurDocument());
        // BZ 7870 Ajout du num inforom avant le mergePDF
        // on ajoute au doc info le numéro de référence inforom
        docInfo.setDocumentTypeNumber(IPRConstantesExternes.FORMULAIRE_BASE_INDEMNI_IJ);
        docInfo.setDocumentType(IPRConstantesExternes.FORMULAIRE_BASE_INDEMNI_IJ);
        docInfo.setDocumentSubject(getSession().getLabel("ATTESTATION_DE_PRESENCE"));
        try {
            // Pour les decomptes definitifs et les client qui possedent une GED
            if (getIsSendToGed().booleanValue()) {
                // on genere le doc pour impression (mail) et on set les
                // proprietes DocInfo
                // on ne supprime pas les documents individuels car on doit les
                // envoies à la GED
                // on trie les documents sur le critère "orderPrintBy"
                this.mergePDF(docInfo, false, 25, false, IJFormulaires.ORDER_PRINTING_BY);
            } else {
                // on genere le doc pour impression (mail) et on set les
                // proprietes DocInfo
                // on supprime pas les documents individuels car on ne les
                // envoies pasà la GED
                // on trie les documents sur le critère "orderPrintBy"
                this.mergePDF(docInfo, true, 25, false, IJFormulaires.ORDER_PRINTING_BY);
            }
        } catch (Exception e) {
            e.printStackTrace();
            getMemoryLog().logMessage("APAttestations.afterExecuteReport():" + e.toString(), FWMessage.ERREUR,
                    "APAttestations");
        }

        return createEMailObject(succesGenererFormulaires, succesImprimerFormulaires);
    }

    /**
     * Création du titre du mail en fonction du resultat
     *
     * @param succesGenererFormulaires
     * @param succesImprimerFormulaires
     * @return
     */
    private boolean createEMailObject(boolean succesGenererFormulaires, boolean succesImprimerFormulaires) {
        String langue = getSession().getLibelleLangue();

        if (genererFormulaires.booleanValue() && imprimerFormulaires.booleanValue()) {
            if (succesGenererFormulaires && succesImprimerFormulaires) {
                if (isForPeriode()) {
                    if (hasAttachedDocuments()) {
                        updatePropertiesOfAttachedDoc(MessageFormat.format(
                                getSession().getLabel("GENERER_FORMULAIRES_ATTESTATION_OK_IMPRESSION_OK_PERIODE"), // OK
                                new Object[] { JACalendar.getMonthName(Integer.parseInt(moisDebut), langue), anneeDebut,
                                        JACalendar.getMonthName(Integer.parseInt(moisFin)), anneeFin }));
                    } else {
                        eMailObject = MessageFormat.format(
                                getSession().getLabel("GENERER_FORMULAIRES_ATTESTATION_OK_AUCUN_FORMULAIRES_PERIODE"), // OK
                                new Object[] { JACalendar.getMonthName(Integer.parseInt(moisDebut), langue), anneeDebut,
                                        JACalendar.getMonthName(Integer.parseInt(moisFin)), anneeFin });
                    }
                } else {
                    if (hasAttachedDocuments()) {
                        updatePropertiesOfAttachedDoc(MessageFormat.format(
                                getSession().getLabel("GENERER_FORMULAIRES_ATTESTATION_OK_IMPRESSION_OK"), // OK
                                new Object[] { mois, annee }));
                    } else {
                        eMailObject = MessageFormat.format(
                                getSession().getLabel("GENERER_FORMULAIRES_ATTESTATION_OK_AUCUN_FORMULAIRES"), // OK
                                new Object[] { mois, annee });
                    }
                }
            }

        } else if (genererFormulaires.booleanValue()) {
            if (succesGenererFormulaires) {
                eMailObject = getSession().getLabel("GENERER_FORMULAIRES_ATTESTATION_SUCCES"); // OK - traduit
            }

        } else if (imprimerFormulaires.booleanValue()) {
            if (succesImprimerFormulaires) {
                if (isForPeriode()) {
                    if (hasAttachedDocuments()) {
                        updatePropertiesOfAttachedDoc(MessageFormat
                                .format(getSession().getLabel("GENERER_FORMULAIRES_ATTESTATION_IMPRESSION_OK_PERIODE"), // OK
                                        new Object[] { JACalendar.getMonthName(Integer.parseInt(moisDebut), langue),
                                                anneeDebut, JACalendar.getMonthName(Integer.parseInt(moisFin)),
                                                anneeFin }));
                    } else {
                        eMailObject = MessageFormat.format(
                                getSession().getLabel("GENERER_FORMULAIRES_ATTESTATION_AUCUN_FORMULAIRES_PERIODE"), // OK
                                new Object[] { JACalendar.getMonthName(Integer.parseInt(moisDebut), langue), anneeDebut,
                                        JACalendar.getMonthName(Integer.parseInt(moisFin)), anneeFin });
                    }
                } else {
                    if (hasAttachedDocuments()) {
                        updatePropertiesOfAttachedDoc(MessageFormat.format(
                                getSession().getLabel("GENERER_FORMULAIRES_ATTESTATION_IMPRESSION_OK"), new Object[] { // OK
                                        mois, annee }));
                    } else {
                        eMailObject = MessageFormat.format(
                                getSession().getLabel("GENERER_FORMULAIRES_ATTESTATION_AUCUN_FORMULAIRES"), // OK
                                new Object[] { mois, annee });
                    }
                }
            }
        } else {
            setSendCompletionMail(false);
        }

        if (!succesGenererFormulaires || !succesImprimerFormulaires) {
            eMailObject = getSession().getLabel("GENERER_FORMULAIRES_ATTESTATION_ERREUR"); // OK - traduit
        }

        return succesGenererFormulaires && succesImprimerFormulaires;
    }

    /**
     * decoupe la periode donnée en mois
     *
     * @param dateDebut
     * @param dateFin
     * @return
     */
    private List createPeriodes() {
        List periodes = new ArrayList();

        int anneeDebut = Integer.parseInt(this.anneeDebut);
        int anneeFin = Integer.parseInt(this.anneeFin);
        int moisDebut = Integer.parseInt(this.moisDebut);
        int moisFin = Integer.parseInt(this.moisFin);
        boolean premiereAnnee = true;
        int anneeCourante = anneeDebut;

        while (anneeCourante <= anneeFin) {
            if (anneeCourante < anneeFin) {
                if (premiereAnnee) {
                    // on ajoute les mois entre moi debut et le dernier moi de
                    // l'annee
                    for (int moisCourant = moisDebut; moisCourant <= 12; moisCourant++) {

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.MONTH, moisCourant - 1);
                        cal.set(Calendar.YEAR, anneeCourante);
                        cal.set(Calendar.DAY_OF_MONTH, 1);
                        String dateDebut = getDateFormatted(cal, getSession());

                        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                        String dateFin = getDateFormatted(cal, getSession());

                        String[] periode = { dateDebut, dateFin };
                        periodes.add(periode);
                    }
                    premiereAnnee = false;
                } else {
                    // on ajoute tous les mois de l'annee
                    for (int moisCourant = 1; moisCourant <= 12; moisCourant++) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.MONTH, moisCourant - 1);
                        cal.set(Calendar.YEAR, anneeCourante);
                        cal.set(Calendar.DAY_OF_MONTH, 1);
                        String dateDebut = getDateFormatted(cal, getSession());

                        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                        String dateFin = getDateFormatted(cal, getSession());

                        String[] periode = { dateDebut, dateFin };
                        periodes.add(periode);
                    }
                }
            } else if (anneeCourante == anneeFin) {
                if (premiereAnnee) {
                    // on ajoute les mois entre moi debut et mois fin de l'annee
                    for (int moisCourant = moisDebut; moisCourant <= moisFin; moisCourant++) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.MONTH, moisCourant - 1);
                        cal.set(Calendar.YEAR, anneeCourante);
                        cal.set(Calendar.DAY_OF_MONTH, 1);
                        String dateDebut = getDateFormatted(cal, getSession());

                        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                        String dateFin = getDateFormatted(cal, getSession());

                        String[] periode = { dateDebut, dateFin };
                        periodes.add(periode);
                    }
                    premiereAnnee = false;
                } else {
                    // on ajoute les mois entre le mois de janvier et le mois de
                    // fin
                    for (int moisCourant = 1; moisCourant <= moisFin; moisCourant++) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.MONTH, moisCourant - 1);
                        cal.set(Calendar.YEAR, anneeCourante);
                        cal.set(Calendar.DAY_OF_MONTH, 1);
                        String dateDebut = getDateFormatted(cal, getSession());

                        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                        String dateFin = getDateFormatted(cal, getSession());

                        String[] periode = { dateDebut, dateFin };
                        periodes.add(periode);
                    }
                }
            }
            anneeCourante++;
        }

        return periodes;
    }

    /**
     *
     * @return
     */
    private boolean genererFormulaires(String dateDebut, String dateFin) {
        boolean succes = true;

        // point ouvert 0658
        // ajout etat decide
        String[] csEtat = { IIJPrononce.CS_COMMUNIQUE, IIJPrononce.CS_DECIDE, IIJPrononce.CS_VALIDE };
        IJPrononceManager prononceManager = new IJPrononceManager();

        prononceManager.setSession(getSession());
        prononceManager.setForCsEtats(csEtat);
        prononceManager.setForIsActiveDuringPeriode(dateDebut, dateFin);
        prononceManager.setForIdPrononce(forIdPrononce);

        BTransaction bTransaction = null;
        BStatement bStatement = null;

        try {
            bTransaction = getTransaction();
            bTransaction.openTransaction();
            bStatement = prononceManager.cursorOpen(bTransaction);
            IJPrononce prononce = null;

            // pour tous les prononces pour le mois est l'années concernés
            while ((prononce = (IJPrononce) prononceManager.cursorReadNext(bStatement)) != null) {

                // retrouver tous les agents d'execution
                IJMesureJointAgentExecutionManager agentExecManager = new IJMesureJointAgentExecutionManager();
                agentExecManager.setSession(getSession());
                agentExecManager.setForIdPrononce(prononce.getIdPrononce());
                agentExecManager.setForIsActiveDuringPeriode(dateDebut, dateFin);

                BStatement bStatementAgents = null;
                bStatementAgents = agentExecManager.cursorOpen(bTransaction);
                IJMesureJointAgentExecution agentExec = null;

                // pour tous les agents d'execution
                pourTousLesAgents: while ((agentExec = (IJMesureJointAgentExecution) agentExecManager
                        .cursorReadNext(bStatementAgents)) != null) {

                    // chercher base d'indemnisation pour le prononce et le mois
                    // selectionner
                    IJBaseIndemnisationManager baseIndManager = new IJBaseIndemnisationManager();
                    baseIndManager.setSession(getSession());
                    baseIndManager.setForIdPrononce(prononce.getIdPrononce());
                    baseIndManager.setForIsActiveDuringPeriode(dateDebut, dateFin);

                    BStatement bStatementbaseInd = null;
                    bStatementbaseInd = baseIndManager.cursorOpen(bTransaction);
                    IJBaseIndemnisation baseInd = null;

                    // si pas de base d'indemnisation on en cree une
                    if ((baseInd = (IJBaseIndemnisation) baseIndManager.cursorReadNext(bStatementbaseInd)) == null) {

                        // on verifie aussi qu'il y ait une ij calculee pour
                        // cette periode
                        // pas de creation automatique de base d'indemnisation
                        // pour les prononces de type AIT et AA
                        if (hasIjCalculeeForPeriode(prononce.getIdPrononce(), dateDebut, dateFin)
                                && (!IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(prononce.getCsTypeIJ()))
                                && (!IIJPrononce.CS_ALLOC_ASSIST.equals(prononce.getCsTypeIJ()))) {
                            baseInd = new IJBaseIndemnisation();
                            baseInd.setSession(getSession());
                            baseInd.setIdPrononce(prononce.getIdPrononce());
                            baseInd.setDateDebutPeriode(dateDebut);
                            baseInd.setDateFinPeriode(dateFin);
                            baseInd.setCsEtat(IIJBaseIndemnisation.CS_OUVERT);
                            baseInd.setCsTypeBase(IIJBaseIndemnisation.CS_NORMAL);
                            baseInd.setCsTypeIJ(prononce.getCsTypeIJ());

                            if (!JadeStringUtil.isBlankOrZero(prononce.getCsCantonImpositionSource())) {
                                baseInd.setCsCantonImpotSource(prononce.getCsCantonImpositionSource());
                                baseInd.setTauxImpotSource(prononce.getTauxImpositionSource());
                            }

                            // Modification suite à la mise en service de la demnade Inforom 492
                            baseInd.setNombreJoursExterne("1");

                            try {
                                baseInd.wantCreerFormulaires(false);

                                JADate date = new JADate(dateFin);
                                int nbrJours = date.getDay();
                                StringBuffer sb = new StringBuffer();

                                for (int i = 0; i < nbrJours; i++) {
                                    sb.append(IIJBaseIndemnisation.IJ_CALENDAR_NON_ATTESTE);
                                }
                                baseInd.setAttestationJours(sb.toString());

                                baseInd.add(bTransaction);
                            } catch (Exception e) {
                                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                                        "ERREUR lors de la sauvegarde de la BI.");
                                succes = false;
                            }
                        } else {
                            // si pas d'ij calcule pour cette periode, on ne
                            // cree pas la base d'indemnisation, ni le
                            // formulaire
                            continue pourTousLesAgents;
                        }
                    }

                    // chercher le formulaire pour cette BI et cet Agent
                    // d'indemnisation
                    IJFormulaireIndemnisationManager formIndManager = new IJFormulaireIndemnisationManager();
                    formIndManager.setSession(getSession());
                    formIndManager.setForIdBaseIndemnisation(baseInd.getIdBaseIndemisation());
                    formIndManager.setForIdInstitutionResponsable(agentExec.getIdAgentExecution());

                    BStatement bstatementFormInd = null;
                    bstatementFormInd = formIndManager.cursorOpen(bTransaction);
                    IJFormulaireIndemnisation formInd = null;

                    // si pas de formulaire d'indemnisation pour cet agent on en
                    // cree un
                    if ((formInd = (IJFormulaireIndemnisation) formIndManager
                            .cursorReadNext(bstatementFormInd)) == null) {

                        formInd = new IJFormulaireIndemnisation();
                        formInd.setSession(getSession());
                        formInd.setIdIndemnisation(baseInd.getIdBaseIndemisation());
                        formInd.setIdInstitutionResponsable(agentExec.getIdAgentExecution());
                        formInd.setEtat(IIJFormulaireIndemnisation.CS_ATTENTE);

                        try {
                            formInd.add(bTransaction);
                        } catch (Exception e) {
                            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                                    getSession().getLabel("ERREUR_SAUVEVEGARDE_ATTESTATION_DE_PRESENCE"));
                            succes = false;
                        }
                    }
                    agentExec = null;
                }
                prononce = null;
                bTransaction.commit();
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                    getSession().getLabel("ERREUR_GENERATION_ATTESTATION_DE_PRESENCE"));
            succes = false;
        } finally {
            if (bTransaction != null) {
                try {
                    if (succes) {
                        bTransaction.commit();
                    } else {
                        bTransaction.rollback();
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    try {
                        bTransaction.closeTransaction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return succes;
    }

    /**
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @return
     */
    public String getAnneeDebut() {
        return anneeDebut;
    }

    /**
     * @return
     */
    public String getAnneeFin() {
        return anneeFin;
    }

    /**
     * @return
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     *
     * @param cal
     * @param bSession
     * @return
     */
    private String getDateFormatted(Calendar cal, BSession bSession) {
        DateFormat df = PRDateFormater.getDateFormatInstance(bSession, "dd.MM.yyyy");
        return df.format(cal.getTime());

    }

    /**
     * @return
     */
    public String getDateRetour() {
        return dateRetour;
    }

    /**
     * @return
     */
    public String getDateSurDocument() {
        return dateSurDocument;
    }

    public String getDisplaySendToGed() {
        return displaySendToGed;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return eMailObject;
    }

    /**
     * @return
     */
    public String getForIdPrononce() {
        return forIdPrononce;
    }

    /**
     * @return
     */
    public Boolean getGenererFormulaires() {
        return genererFormulaires;
    }

    /**
     * @return
     */
    public String getImpressionFomulairesForEtat() {
        return impressionFomulairesForEtat;
    }

    /**
     * @return
     */
    public Boolean getImprimerFormulaires() {
        return imprimerFormulaires;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    /**
     * @return
     */
    public String getMois() {
        return mois;
    }

    /**
     * @return
     */
    public String getMoisDebut() {
        return moisDebut;
    }

    /**
     * @return
     */
    public String getMoisFin() {
        return moisFin;
    }

    /**
     * @param dateDebut
     * @param dateFin
     * @return
     */
    private boolean hasIjCalculeeForPeriode(String idPrononce, String dateDebut, String dateFin) throws Exception {

        IJIJCalculeeManager ijCalculeeManager = new IJIJCalculeeManager();
        ijCalculeeManager.setSession(getSession());
        ijCalculeeManager.setForIdPrononce(idPrononce);
        ijCalculeeManager.setForPeriode(dateDebut, dateFin);

        if (ijCalculeeManager.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @return
     */
    private boolean imprimerFormulaires(String dateDebut, String dateFin, List idsBasesIndemnisationImprimees) {
        boolean succes = true;

        // point ouvert 00658
        // ajout etat decide
        String[] csEtat = { IIJPrononce.CS_COMMUNIQUE, IIJPrononce.CS_DECIDE, IIJPrononce.CS_VALIDE };
        IJPrononceManager prononceManager = new IJPrononceManager();

        prononceManager.setSession(getSession());
        prononceManager.setForCsEtats(csEtat);
        prononceManager.setForIsActiveDuringPeriode(dateDebut, dateFin);
        prononceManager.setForIdPrononce(forIdPrononce);

        BTransaction bTransaction = null;
        BStatement bStatement = null;

        try {
            bTransaction = getTransaction();
            bTransaction.openTransaction();
            bStatement = prononceManager.cursorOpen(bTransaction);
            IJPrononce prononce = null;

            // pour tous les prononces pour le mois est l'années concernés
            while ((prononce = (IJPrononce) prononceManager.cursorReadNext(bStatement)) != null) {

                // retrouver tous les agents d'execution
                IJMesureJointAgentExecutionManager agentExecManager = new IJMesureJointAgentExecutionManager();
                agentExecManager.setSession(getSession());
                agentExecManager.setForIdPrononce(prononce.getIdPrononce());
                agentExecManager.setForIsActiveDuringPeriode(dateDebut, dateFin);

                BStatement bStatementAgents = null;
                bStatementAgents = agentExecManager.cursorOpen(bTransaction);
                IJMesureJointAgentExecution agentExec = null;

                // pour tous les agents d'execution
                while ((agentExec = (IJMesureJointAgentExecution) agentExecManager
                        .cursorReadNext(bStatementAgents)) != null) {

                    // chercher base d'indemnisation pour le prononce et le mois
                    // selectionner
                    IJBaseIndemnisationManager baseIndManager = new IJBaseIndemnisationManager();
                    baseIndManager.setSession(getSession());
                    baseIndManager.setForIdPrononce(prononce.getIdPrononce());
                    baseIndManager.setForIsActiveDuringPeriode(dateDebut, dateFin);

                    BStatement bStatementbaseInd = null;
                    bStatementbaseInd = baseIndManager.cursorOpen(bTransaction);
                    IJBaseIndemnisation baseInd = null;

                    // si une base d'indemnisation est trouvee on cherche les
                    // formulaires
                    if ((baseInd = (IJBaseIndemnisation) baseIndManager.cursorReadNext(bStatementbaseInd)) != null) {

                        // chercher le formulaire pour cette BI et cet Agent
                        // d'indemnisation
                        IJFormulaireIndemnisationManager formIndManager = new IJFormulaireIndemnisationManager();
                        formIndManager.setSession(getSession());
                        formIndManager.setForIdBaseIndemnisation(baseInd.getIdBaseIndemisation());
                        formIndManager.setForIdInstitutionResponsable(agentExec.getIdAgentExecution());

                        BStatement bstatementFormInd = null;
                        bstatementFormInd = formIndManager.cursorOpen(bTransaction);
                        IJFormulaireIndemnisation formInd = null;

                        // envoyer le formulaire si (dans etat CS_ATTENTE et
                        // impressionFormulairesForEtat=Non-envoyé)
                        // ou (impressionFormulairesForEtat=Tous)
                        if (((formInd = (IJFormulaireIndemnisation) formIndManager
                                .cursorReadNext(bstatementFormInd)) != null)
                                && ((formInd.getEtat().equalsIgnoreCase(IIJFormulaireIndemnisation.CS_ATTENTE)
                                        && getImpressionFomulairesForEtat()
                                                .equalsIgnoreCase(getSession().getLabel("JSP_NON_ENVOYE")))
                                        || (getImpressionFomulairesForEtat()
                                                .equalsIgnoreCase(getSession().getLabel("JSP_TOUS"))))) {

                            // Evite d'imprimer un même document à double !!!!
                            //
                            // Ex. Impression des tous les formulaire
                            // d'indemnisations pour les périodes
                            // de octobre et novembre.
                            // Dans ce cas, cette méthode sera appelée 2x, une
                            // fois pour octobre et une fois pour novembre.
                            // Si une base d'indemnisation 'étendue' va de
                            // octobre à novembre, elle serait imprimée 2 fois
                            // dans le
                            // document final sans ce test.

                            if (!idsBasesIndemnisationImprimees.contains(
                                    baseInd.getIdBaseIndemisation() + formInd.getIdFormulaireIndemnisation())) {

                                idsBasesIndemnisationImprimees
                                        .add(baseInd.getIdBaseIndemisation() + formInd.getIdFormulaireIndemnisation());
                                try {
                                    IJFormulaires document = new IJFormulaires(getSession());
                                    System.out.println("docRef = " + formInd.getIdFormulaireIndemnisation() + " - ");
                                    document.setEMailAddress(getEMailAddress());
                                    document.setIdFormulaire(formInd.getIdFormulaireIndemnisation());
                                    document.setIdPrononce(prononce.getIdPrononce());
                                    document.setCsTypeIJ(baseInd.getCsTypeIJ());
                                    document.setDate(getDateSurDocument());
                                    document.setDateRetour(getDateRetour());
                                    document.setTransaction(null);
                                    document.setTailleLot(1);
                                    // document.setImpressionParLot(true);
                                    document.setParent(this);
                                    document.setIsSendToGed(getIsSendToGed());
                                    document.executeProcess();

                                    if (!document.isOnError()) {

                                        // si en attente, changer l'etat du
                                        // formulaire à envoyé
                                        if (formInd.getEtat().equalsIgnoreCase(IIJFormulaireIndemnisation.CS_ATTENTE)) {
                                            try {
                                                formInd.retrieve(bTransaction);
                                                formInd.setEtat(IIJFormulaireIndemnisation.CS_ENVOYE);
                                                formInd.setDateEnvoi(getDateSurDocument());
                                                formInd.update(bTransaction);
                                            } catch (Exception e) {
                                                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                                                        getSession()
                                                                .getLabel("ERREUR_MISE_A_JOUR_ATTESTATION_DE_PRESENCE")
                                                                + formInd.getIdFormulaireIndemnisation());
                                                succes = false;
                                            }
                                        }
                                    } else {
                                        succes = false;
                                        // bz-4496
                                        Vector v = document.getMemoryLog().getMessagesToVector();
                                        for (Iterator iterator = v.iterator(); iterator.hasNext();) {
                                            BIMessage msg = (BIMessage) iterator.next();
                                            getMemoryLog().logMessage(msg);
                                        }

                                    }
                                } catch (FWIException e) {
                                    setMessage(e.getMessage());
                                    setMsgType(FWViewBeanInterface.ERROR);
                                    succes = false;
                                }
                            }
                        }
                    }
                    agentExec = null;
                }
                prononce = null;
                bTransaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                    getSession().getLabel("ERREUR_IMPRESSION_ATTESTATION_DE_PRESENCE"));
            succes = false;
        } finally {
            if (bTransaction != null) {
                try {
                    if (succes) {
                        bTransaction.commit();
                    } else {
                        bTransaction.rollback();
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    try {
                        bTransaction.closeTransaction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return succes;
    }

    /**
     *
     * @return true si pour plusieurs mois
     */
    private boolean isForPeriode() {

        if (anneeDebut.equals(anneeFin) && moisDebut.equals(moisFin)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * @param string
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * @param string
     */
    public void setAnneeDebut(String string) {
        anneeDebut = string;
    }

    /**
     * @param string
     */
    public void setAnneeFin(String string) {
        anneeFin = string;
    }

    /**
     * @param string
     */
    public void setDateDebut(String string) {
        dateDebut = string;
    }

    /**
     * @param string
     */
    public void setDateFin(String string) {
        dateFin = string;
    }

    /**
     * @param string
     */
    public void setDateRetour(String string) {
        dateRetour = string;
    }

    /**
     * @param string
     */
    public void setDateSurDocument(String string) {
        dateSurDocument = string;
    }

    public void setDisplaySendToGed(String displaySendToGed) {
        this.displaySendToGed = displaySendToGed;
    }

    /**
     * @param string
     */
    public void setForIdPrononce(String string) {
        forIdPrononce = string;
    }

    /**
     * @param boolean1
     */
    public void setGenererFormulaires(Boolean boolean1) {
        genererFormulaires = boolean1;
    }

    /**
     * @param string
     */
    public void setImpressionFomulairesForEtat(String string) {
        impressionFomulairesForEtat = string;
    }

    /**
     * @param boolean1
     */
    public void setImprimerFormulaires(Boolean boolean1) {
        imprimerFormulaires = boolean1;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    /**
     * @param string
     */
    public void setMois(String string) {
        mois = string;
    }

    /**
     * @param string
     */
    public void setMoisDebut(String string) {
        moisDebut = string;
    }

    /**
     * @param string
     */
    public void setMoisFin(String string) {
        moisFin = string;
    }

    /**
     * Update du tite des mails
     *
     * @param subject
     * @param from
     */
    private void updatePropertiesOfAttachedDoc(String subject) {

        Iterator iter = getAttachedDocuments().iterator();
        int max = getAttachedDocuments().size();
        int nb = 1;

        while (iter.hasNext()) {
            JadePublishDocument doc = (JadePublishDocument) iter.next();
            JadePublishDocumentInfo docInfo = doc.getPublishJobDefinition().getDocumentInfo();

            docInfo.setDocumentSubject(subject + " (" + nb + "/" + max + ")");
            nb++;
        }
    }

}
