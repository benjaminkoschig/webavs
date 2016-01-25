package globaz.phenix.process.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.parameters.FWParametersCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.listes.excel.CPListeCommunicationEnvoi;
import globaz.phenix.translation.CodeSystem;
import globaz.pyxis.constantes.IConstantes;

/**
 * Dévalida une décision - Enlève l'état validation et remet l'idPassage à blanc Ne peut se faire que si la décision
 * n'est pas en état "facturé" ou "reprise" Date de création : (25.02.2002 13:41:13)
 * 
 * @author: acr
 */
public class CPProcessListeCommunicationsFiscales extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeDecision = "";
    private String dateEdition = "";
    private String dateEnvoi = "";
    private Boolean dateEnvoiVide = new Boolean(false);
    private String descriptionMail = "";
    private String forCanton = "";
    private String forGenreAffilie = "";
    private String forIdPassage = "";
    private String forOrder = "";
    private int nbCanton = 0;

    private Boolean withAnneeEnCours = new Boolean(false);

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessListeCommunicationsFiscales() {
        super();
    }

    public CPProcessListeCommunicationsFiscales(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
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
        try {
            CPCommunicationFiscaleAffichageManager manager = null;
            manager = new CPCommunicationFiscaleAffichageManager();
            manager.setSession(getSession());
            manager.setDateEnvoiVide(getDateEnvoiVide());
            manager.setForDateEnvoi(getDateEnvoi());
            manager.setForCanton(getForCanton());
            manager.setForAnneeDecision(getAnneeDecision());
            manager.setExceptRetour(Boolean.TRUE);
            manager.setExceptComptabilise(Boolean.TRUE);
            // SI Indépendant de sélectionné => IND, TSE, AGR, REN donc
            // différtent de NON ACTIF...
            if (!JadeStringUtil.isEmpty(getForGenreAffilie())) {
                if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(getForGenreAffilie())) {
                    manager.setNotInGenreAffilie(CPDecision.CS_NON_ACTIF + ", " + CPDecision.CS_ETUDIANT);
                } else {
                    manager.setInGenreAffilie(CPDecision.CS_NON_ACTIF + ", " + CPDecision.CS_ETUDIANT);
                }
            }
            if (JadeStringUtil.isEmpty(getAnneeDecision())) {
                manager.setWithAnneeEnCours(getWithAnneeEnCours());
            } else {
                manager.setWithAnneeEnCours(Boolean.FALSE);
            }
            manager.orderByCanton();

            // Si pas de sélection sur le canton => il faut sortir tout les
            // cantons
            if (JadeStringUtil.isEmpty(manager.getForCanton())) {
                FWParametersSystemCodeManager cantonManager = new FWParametersSystemCodeManager();
                cantonManager.setSession(getSession());
                cantonManager.setForIdGroupe("PYCANTON");
                cantonManager.find();
                for (int i = 0; i < cantonManager.size(); i++) {
                    FWParametersCode canton = (FWParametersCode) cantonManager.getEntity(i);
                    manager.setForCanton(canton.getIdCode());
                    if (manager.getCount() > 0) {
                        nbCanton++;
                        // On test si le canton est NW
                        // Si c'est le cas on ordre par NumAVS, sinon par num de
                        // contribuable
                        if (canton.getIdCode().equals(IConstantes.CS_LOCALITE_CANTON_NIDWALD)) {
                            manager.orderByNumAVS();

                        } else if ((canton.getIdCode().equals(IConstantes.CS_LOCALITE_CANTON_VAUD))
                                || (canton.getIdCode().equals(IConstantes.CS_LOCALITE_CANTON_VALAIS))
                                || (canton.getIdCode().equals(IConstantes.CS_LOCALITE_CANTON_JURA))) {
                            manager.orderByNom();
                            manager.orderByPrenom();
                        } else if (canton.getIdCode().equals(IConstantes.CS_LOCALITE_CANTON_NEUCHATEL)) {
                            manager.orderByTypeAffilie();
                            manager.orderByNom();
                            manager.orderByPrenom();
                        } else {
                            manager.orderByNumContribuable();
                        }
                        manager.orderByAnnee();
                        CPListeCommunicationEnvoi excelDoc = new CPListeCommunicationEnvoi(getSession(),
                                manager.getForAnneeDecision(), manager.getForCanton(), getForGenreAffilie(),
                                getDateEdition());
                        excelDoc.setSession(getSession());
                        excelDoc.setProcessAppelant(this);
                        excelDoc.populateSheet(manager, getTransaction());
                        descriptionMail = CodeSystem.getCode(getSession(), manager.getForCanton()) + " - "
                                + manager.getForAnneeDecision() + " - "
                                + CodeSystem.getCode(getSession(), getForGenreAffilie());
                        JadePublishDocumentInfo docInfo = createDocumentInfo();
                        docInfo.setDocumentType("0213CCP");
                        docInfo.setDocumentTypeNumber("");
                        this.registerAttachedDocument(docInfo, excelDoc.getOutputFile());
                    }
                }
            } else {
                // On test si le canton est NW
                // Si c'est le cas on ordre par NumAVS, sinon par num de
                // contribuable
                if (manager.getForCanton().equals(IConstantes.CS_LOCALITE_CANTON_NIDWALD)) {
                    manager.orderByNumAVS();

                } else if ((manager.getForCanton().equals(IConstantes.CS_LOCALITE_CANTON_VAUD))
                        || (manager.getForCanton().equals(IConstantes.CS_LOCALITE_CANTON_VALAIS))
                        || (manager.getForCanton().equals(IConstantes.CS_LOCALITE_CANTON_JURA))) {
                    manager.orderByNom();
                    manager.orderByPrenom();
                } else if (manager.getForCanton().equals(IConstantes.CS_LOCALITE_CANTON_NEUCHATEL)) {
                    manager.orderByTypeAffilie();
                    manager.orderByNom();
                    manager.orderByPrenom();
                } else {
                    manager.orderByNumAVS();
                }
                manager.orderByAnnee();
                // Création du document
                descriptionMail = CodeSystem.getCode(getSession(), manager.getForCanton()) + "-"
                        + manager.getForAnneeDecision() + "-" + CodeSystem.getCode(getSession(), getForGenreAffilie());
                if (manager.getCount() > 0) {
                    nbCanton++;
                    CPListeCommunicationEnvoi excelDoc = new CPListeCommunicationEnvoi(getSession(),
                            manager.getForAnneeDecision(), manager.getForCanton(), getForGenreAffilie(),
                            getDateEdition());
                    excelDoc.setSession(getSession());
                    excelDoc.setProcessAppelant(this);
                    excelDoc.populateSheet(manager, getTransaction());
                    JadePublishDocumentInfo docInfo = createDocumentInfo();
                    docInfo.setDocumentType("0213CCP");
                    docInfo.setDocumentTypeNumber("");
                    this.registerAttachedDocument(docInfo, excelDoc.getOutputFile());
                }
            }
            return true;
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return false;
        }
    }

    @Override
    protected void _validate() throws java.lang.Exception {
        // Contrôle du mail
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(getSession().getLabel("CP_MSG_0145"));
        }
        // Contrôle de l'année de décision
        setAnneeDecision(getAnneeDecision().trim());
        if (JadeStringUtil.isEmpty(getAnneeDecision())) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(getSession().getLabel("DECISION_INVALIDE"));
        } else {
            try {
                Integer.parseInt(getAnneeDecision());
            } catch (NumberFormatException ex) {
                JadeLogger.error(this, ex);
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(getSession().getLabel("DECISION_INVALIDE"));
            }
        }
        // Contrôle de la date d'envoi
        boolean erreurDateEnvoi = false;
        if (!JadeStringUtil.isEmpty(getDateEnvoi())) {
            try {
                JADate dDateEnvoi = new JADate(getDateEnvoi());
                BSessionUtil.checkDateGregorian(getSession(), dDateEnvoi);
            } catch (Exception ex) {
                erreurDateEnvoi = true;
            }
        }
        if (erreurDateEnvoi) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(getSession().getLabel("DATE_ENVOI_INVALIDE"));
        }
        // Contrôle de la d'édition
        if (!JadeStringUtil.isEmpty(getDateEdition())) {
            try {
                JADate dDateEdition = new JADate(getDateEdition());
                BSessionUtil.checkDateGregorian(getSession(), dDateEdition);
            } catch (Exception ex) {
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(getSession().getLabel("DATE_EDITION_INVALIDE"));
            }
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        // Les erreurs sont ajoutées à la session,
        // abort permet l'arrêt du process
        if (!JadeStringUtil.isEmpty(getMessage())) {
            abort();
        }
    }

    /**
     * Returns the anneeDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getAnneeDecision() {
        return anneeDecision;
    }

    public String getDateEdition() {
        return dateEdition;
    }

    /**
     * Returns the dateEnvoi.
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateEnvoi() {
        return dateEnvoi;
    }

    /**
     * Returns the dateEnvoiVide.
     * 
     * @return boolean
     */
    public Boolean getDateEnvoiVide() {
        return dateEnvoiVide;
    }

    @Override
    protected String getEMailObject() {
        if (!isAborted() && (nbCanton > 0)) {
            try {
                return getSession().getLabel("LIST_COMMUNICATIONS_FISCALES") + " - " + descriptionMail;
            } catch (Exception e) {
                return getSession().getLabel("LIST_COMMUNICATIONS_FISCALES") + " - " + getAnneeDecision();
            }
        } else {
            try {
                return "Aucune liste de Communications fiscales - " + descriptionMail;
            } catch (Exception e) {
                return "Aucune liste de Communications fiscales - " + " - " + getAnneeDecision();
            }
        }
    }

    /**
     * Returns the forCanton.
     * 
     * @return String
     */
    public String getForCanton() {
        return forCanton;
    }

    /**
     * Returns the forGenreAffilie.
     * 
     * @return java.lang.String
     */
    public String getForGenreAffilie() {
        return forGenreAffilie;
    }

    /**
     * Returns the forIdPassage.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdPassage() {
        return forIdPassage;
    }

    /**
     * @return
     */
    public String getForOrder() {
        return forOrder;
    }

    /**
     * @return
     */
    public int getNbCanton() {
        return nbCanton;
    }

    public Boolean getWithAnneeEnCours() {
        return withAnneeEnCours;
    }

    /**
     * @author:BTC
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Sets the anneeDecision.
     * 
     * @param anneeDecision
     *            The anneeDecision to set
     */
    public void setAnneeDecision(String newAnneeDecision) {
        anneeDecision = newAnneeDecision;
    }

    public void setDateEdition(String dateEdition) {
        this.dateEdition = dateEdition;
    }

    /**
     * Sets the dateEnvoi.
     * 
     * @param dateEnvoi
     *            The dateEnvoi to set
     */
    public void setDateEnvoi(String newDateEnvoi) {
        dateEnvoi = newDateEnvoi;
    }

    /**
     * Sets the dateEnvoiVide.
     * 
     * @param dateEnvoiVide
     *            The dateEnvoiVide to set
     */
    public void setDateEnvoiVide(Boolean newDateEnvoiVide) {
        dateEnvoiVide = newDateEnvoiVide;
    }

    /**
     * Sets the forCanton.
     * 
     * @param forCanton
     *            The forCanton to set
     */
    public void setForCanton(String forCanton) {
        this.forCanton = forCanton;
    }

    /**
     * Sets the forGenreAffilie.
     * 
     * @param forGenreAffilie
     *            The forGenreAffilie to set
     */
    public void setForGenreAffilie(String newForGenreAffilie) {
        forGenreAffilie = newForGenreAffilie;
    }

    /**
     * Sets the forIdPassage.
     * 
     * @param forIdPassage
     *            The forIdPassage to set
     */
    public void setForIdPassage(String newForIdPassage) {
        forIdPassage = newForIdPassage;
    }

    /**
     * @param string
     */
    public void setForOrder(String string) {
        forOrder = string;
    }

    /**
     * @param i
     */
    public void setNbCanton(int i) {
        nbCanton = i;
    }

    public void setWithAnneeEnCours(Boolean withAnneeEnCours) {
        this.withAnneeEnCours = withAnneeEnCours;
    }
}
