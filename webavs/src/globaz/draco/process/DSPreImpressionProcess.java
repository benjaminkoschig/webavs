package globaz.draco.process;

import globaz.draco.application.DSApplication;
import globaz.draco.print.list.DSListDeclarationSalaire;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;

public class DSPreImpressionProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String affilieDebut;
    private String affilieFin;
    private boolean affilieTous;
    private String annee;
    private String dateRetourEff;
    private String dateSurDocument;
    private boolean imprimerDeclaration;
    private boolean imprimerLettre;
    private DSListDeclarationSalaire reportDeclaration = null;

    // private FWDocumentListener documentListener = new FWDocumentListener();
    /**
     * Commentaire relatif au constructeur DSPreImpressionProcess.
     */
    public DSPreImpressionProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur DSPreImpressionProcess.
     * 
     * @param parent
     *            globaz.framework.process.FWProcess
     */
    public DSPreImpressionProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur DSPreImpressionProcess.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public DSPreImpressionProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution
     * 
     * @see BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Exécution du process
     * 
     * @see BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        // Sous contrôle d'exceptions
        try {
            // Contrôle des paramètres
            // Contrôle de saisie des affiliés
            if (isAffilieTous() == false) {
                if (JadeStringUtil.isBlank(getAffilieDebut())) {
                    getMemoryLog().logMessage("2004", null, FWMessage.FATAL, getClass().getName());
                }
                if (JadeStringUtil.isBlank(getAffilieFin())) {
                    getMemoryLog().logMessage("2005", null, FWMessage.FATAL, getClass().getName());
                }
                // Contrôle que la valeur de fin soit égale ou supérieure à la
                // valeur de début
                // --> pas encore implémenté
            }
            // Contrôle de la date sur document
            if (JAUtil.isDateEmpty(getDateSurDocument())) {
                getMemoryLog().logMessage("2000", null, FWMessage.FATAL, getClass().getName());
            } else {
                try {
                    globaz.globall.db.BSessionUtil.checkDateGregorian(
                            ((globaz.globall.db.BApplication) globaz.globall.db.GlobazServer.getCurrentSystem()
                                    .getApplication(DSApplication.DEFAULT_APPLICATION_DRACO)).getAnonymousSession(),
                            getDateSurDocument());
                    setDateSurDocument(new JADate(getDateSurDocument()).toStr("."));
                } catch (Exception e) {
                    getMemoryLog().logMessage("2001", null, FWMessage.FATAL, getClass().getName());
                }
            }
            // Contrôle de la date de retour effective
            if (JAUtil.isDateEmpty(getDateRetourEff())) {
                getMemoryLog().logMessage("2002", null, FWMessage.FATAL, getClass().getName());
            } else {
                try {
                    globaz.globall.db.BSessionUtil.checkDateGregorian(
                            ((globaz.globall.db.BApplication) globaz.globall.db.GlobazServer.getCurrentSystem()
                                    .getApplication(DSApplication.DEFAULT_APPLICATION_DRACO)).getAnonymousSession(),
                            getDateRetourEff());
                    setDateRetourEff(new JADate(getDateRetourEff()).toStr("."));
                } catch (Exception e) {
                    getMemoryLog().logMessage("2003", null, FWMessage.FATAL, getClass().getName());
                }
            }
            // Contrôle de l'année de la déclaration à traiter
            if (JadeStringUtil.isIntegerEmpty(getAnnee())) {
                getMemoryLog().logMessage("2006", null, FWMessage.FATAL, getClass().getName());
            }
            // Sortir en cas d'erreurs
            if (getMemoryLog().isOnFatalLevel()) {
                return false;
            }
            // Création de la déclaration de salaires
            if (isImprimerDeclaration()) {
                reportDeclaration = new DSListDeclarationSalaire();
                // pas encore implémenté reportDeclaration.set
            }
            // Création de la lettre
            if (isImprimerLettre()) {
                // pas encore implémenté
            }
            // Chargement du manager de l'affiliation
            AFAffiliationManager _affiliation = new AFAffiliationManager();
            _affiliation.setSession(getSession());
            // _affiliation.setForAnnee(getAnnee());
            if (!isAffilieTous()) {
                // _affiliation.setFromAffilieNumero(getAffilieDebut());
                // _affiliation.setUntilAffilieNumero(getAffilieFin());
            }
            _affiliation.forIsTraitement(false);
            _affiliation.changeManagerSize(0);
            _affiliation.find(getTransaction());
            // Etat en cours
            setState(getSession().getLabel("6202"));
            setProgressScaleValue(_affiliation.size());
            // Parcourir les sections pour voir lesquelles sont des candidates
            // valables
            for (int i = 0; i < _affiliation.size(); i++) {
                // Vérifier la condition de sortie
                if (isAborted()) {
                    return false;
                }
                // Progression
                incProgressCounter();
                AFAffiliation elemAffiliation;
                elemAffiliation = (AFAffiliation) _affiliation.getEntity(i);
                if (elemAffiliation.getDeclarationSalaire().equals("807001")) {
                    System.out.println(elemAffiliation.getAffilieNumero());
                    System.out.println(elemAffiliation.getDateDebut());
                    System.out.println(elemAffiliation.getDateFin());
                    System.out.println("------------------");
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, getClass().getName());
            e.printStackTrace();
            return false;
        }
        return !isOnError();
    }

    /**
     * Gets the affilieDebut
     * 
     * @return Returns a String
     */
    public String getAffilieDebut() {
        return affilieDebut;
    }

    /**
     * Gets the affilieFin
     * 
     * @return Returns a String
     */
    public String getAffilieFin() {
        return affilieFin;
    }

    /**
     * Gets the annee
     * 
     * @return Returns a String
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * Gets the dateRetourEff
     * 
     * @return Returns a String
     */
    public String getDateRetourEff() {
        return dateRetourEff;
    }

    /**
     * Gets the dateSurDocument
     * 
     * @return Returns a String
     */
    public String getDateSurDocument() {
        return dateSurDocument;
    }

    /**
     * Envoi d'un Email pour les informations conernant la fin du process
     * 
     * @see BProcess#getEMailObject()
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("PREIMPRESSION_ECHOUE");
        } else {
            return getSession().getLabel("PREIMPRESSION_REUSSI");
        }
    }

    /**
     * Gets the affilieTous
     * 
     * @return Returns a boolean
     */
    public boolean isAffilieTous() {
        return affilieTous;
    }

    /**
     * Gets the imprimerDeclaration
     * 
     * @return Returns a boolean
     */
    public boolean isImprimerDeclaration() {
        return imprimerDeclaration;
    }

    /**
     * Gets the imprimerLettre
     * 
     * @return Returns a boolean
     */
    public boolean isImprimerLettre() {
        return imprimerLettre;
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Sets the affilieDebut
     * 
     * @param affilieDebut
     *            The affilieDebut to set
     */
    public void setAffilieDebut(String affilieDebut) {
        this.affilieDebut = affilieDebut;
    }

    /**
     * Sets the affilieFin
     * 
     * @param affilieFin
     *            The affilieFin to set
     */
    public void setAffilieFin(String affilieFin) {
        this.affilieFin = affilieFin;
    }

    /**
     * Sets the affilieTous
     * 
     * @param affilieTous
     *            The affilieTous to set
     */
    public void setAffilieTous(boolean affilieTous) {
        this.affilieTous = affilieTous;
    }

    /**
     * Sets the annee
     * 
     * @param annee
     *            The annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * Sets the dateRetour
     * 
     * @param dateRetour
     *            The dateRetour to set
     */
    public void setDateRetourEff(String dateRetourEff) {
        this.dateRetourEff = dateRetourEff;
    }

    /**
     * Sets the dateSurDocument
     * 
     * @param dateSurDocument
     *            The dateSurDocument to set
     */
    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    /**
     * Sets the imprimerDeclaration
     * 
     * @param imprimerDeclaration
     *            The imprimerDeclaration to set
     */
    public void setImprimerDeclaration(boolean imprimerDeclaration) {
        this.imprimerDeclaration = imprimerDeclaration;
    }

    /**
     * Sets the imprimerLettre
     * 
     * @param imprimerLettre
     *            The imprimerLettre to set
     */
    public void setImprimerLettre(boolean imprimerLettre) {
        this.imprimerLettre = imprimerLettre;
    }
}
