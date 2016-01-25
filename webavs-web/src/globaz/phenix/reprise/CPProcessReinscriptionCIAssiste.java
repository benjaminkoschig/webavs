package globaz.phenix.reprise;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliationManager;

/**
 * Process de g�naration d'une liste excel pour les concordances entre Cot.Pers. et CI Date de cr�ation : (17.06.2007
 * 08:34:14)
 * 
 * @author: jpa
 */
public class CPProcessReinscriptionCIAssiste extends BProcess {
    public static void main(String[] args) {
        CPProcessReinscriptionCIAssiste process = null;
        String user = "";
        String pwd = "";
        String email = "hna@globaz.ch";
        try {
            user = args[0];
            pwd = args[1];
            email = args[2];
            System.out.println("User : " + user);
            System.out.println("Password : " + pwd);
            System.out.println("Email : " + email);
            System.out.println("depuis l'affili� : " + args[3]);
            System.out.println("jusqu'� l'affili� : " + args[4]);
            System.out.println("depuis l'ann�e : " + args[5]);
            System.out.println("jusqu'� l'ann�e : " + args[6]);
            BSession session = (BSession) GlobazSystem.getApplication("PHENIX").newSession(user, pwd);
            System.out.println("Reprise started...");
            session.connect(user, pwd);
            process = new CPProcessReinscriptionCIAssiste();
            process.setSession(session);
            process.setEMailAddress(email);
            if (!"0".equalsIgnoreCase(args[3])) {
                process.setFromAffilieDebut(args[3]);
            }
            if (!"0".equalsIgnoreCase(args[4])) {
                process.setTillAffilie(args[4]);
            }
            if (!"0".equalsIgnoreCase(args[5])) {
                process.setFromAnneeDecision(args[5]);
            }
            if (!"0".equalsIgnoreCase(args[6])) {
                process.setTillAnneeDecision(args[6]);
            }
            if (!"0".equalsIgnoreCase(args[7])) {
                process.setMiseAjour(true);
            }
            process.executeProcess();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            System.out.println("La reprise CI est termin�e.");
        }
        System.exit(0);
    }

    // Num�ro d'affili� de d�part
    private String fromAffilieDebut = "";
    // Ann�e de d�but de la recherche
    private String fromAnneeDecision = "";

    private boolean miseAjour = false;

    // Num�ro d'affili� de fin
    private String tillAffilie = "";

    // Ann�e de fin de la recherche
    private String tillAnneeDecision = "";

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     */
    public CPProcessReinscriptionCIAssiste() {
        super();
    }

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessReinscriptionCIAssiste(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public CPProcessReinscriptionCIAssiste(BSession session) {
        super(session);
    }

    /**
     * Nettoyage apr�s erreur ou ex�cution Date de cr�ation : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        try {
            CPDecisionAffiliationManager manager = new CPDecisionAffiliationManager();
            manager.setSession(getSession());
            manager.setFromAnneeDecision(getFromAnneeDecision());
            manager.setTillAnneeDecision(getTillAnneeDecision());
            manager.setFromNoAffilie(getFromAffilieDebut());
            manager.setTillNoAffilie(getTillAffilie());
            manager.setIsActive(Boolean.TRUE);
            manager.setInEtat(CPDecision.CS_FACTURATION + ", " + CPDecision.CS_REPRISE + ", " + CPDecision.CS_SORTIE);
            // !!! Mettre en premier l'ordre par idTiers � cause des affili�s
            // qui changent de n�
            manager.orderByIdTiers();
            manager.orderByNoAffilie();
            manager.orderByAnnee();
            manager.orderByIdDecision();
            manager.changeManagerSize(0);
            // manager.find();
            /*
             * CPDecision entity = null; for(int i=0;i<manager.size();i++) { entity = (CPDecision)manager.getEntity(i);
             * }
             */

            // On regarde que le nombre n'est pas trop grand
            /*
             * if(manager.getCount()>1){ _addError(getTransaction(), "Erreur affiner"); return false; }
             */

            // Cr�ation du document
            CPListeReinscriptionCIAssiste excelDoc = new CPListeReinscriptionCIAssiste(getSession());
            excelDoc.setSession(getSession());
            excelDoc.setProcessAppelant(this);
            excelDoc.setFromAnneeDecision(getFromAnneeDecision());
            excelDoc.setTillAnneeDecision(getTillAnneeDecision());
            excelDoc.setFromNumAffilie(getFromAffilieDebut());
            excelDoc.setTillNumAffilie(getTillAffilie());
            excelDoc.setMiseAjourCI(isMiseAjour());
            excelDoc.populateSheet(manager, getTransaction());
            excelDoc.toString();
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setDocumentTypeNumber("");
            this.registerAttachedDocument(docInfo, excelDoc.getOutputFile());
            return true;
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return false;
        }
    }

    /**
     * Valide le contenu de l'entit� (notamment les champs obligatoires) On va compter le nombre d'inscriptions
     */
    @Override
    protected void _validate() throws Exception {
        // Contr�le du mail
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            getSession().addError(getSession().getLabel("CP_MSG_0145"));
        }
        // Si aucun crit�re de s�lection => erreur
        // if(JadeStringUtil.isEmpty(getFromAnneeDecision())
        // &&JadeStringUtil.isEmpty(getToAnneeDecision())
        // &&JadeStringUtil.isEmpty(getFromAffilieDebut())
        // &&JadeStringUtil.isEmpty(getFromAffilieFin())){
        // getSession().addError(getSession().getLabel("SELECTION_INCOMPLETE"));
        // }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        if (getSession().hasErrors()) {
            abort();
        }
    }

    /**
     * Return le sujet de l'email Date de cr�ation : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("SUJET_EMAIL_LISTE_COMPTA_CP_PASOK");
        } else {
            return getSession().getLabel("SUJET_EMAIL_LISTE_COMPTA_CP_OK");
        }
    }

    /**
     * Returns le num�ro d'affili� de d�part
     * 
     * @return String
     */
    public String getFromAffilieDebut() {
        return fromAffilieDebut;
    }

    /**
     * Returns the fromAnneeDecision.
     * 
     * @return String
     */
    public String getFromAnneeDecision() {
        return fromAnneeDecision;
    }

    /**
     * Returns le num�ro d'affili� de fin.
     * 
     * @return String
     */
    public String getTillAffilie() {
        return tillAffilie;
    }

    /**
     * Returns the toAnneeDecision.
     * 
     * @return String
     */
    public String getTillAnneeDecision() {
        return tillAnneeDecision;
    }

    public boolean isMiseAjour() {
        return miseAjour;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Sets the genreDecision.
     * 
     * @param num�ro
     *            d'affili� de d�part
     */
    public void setFromAffilieDebut(String fromNumAffilie) {
        fromAffilieDebut = fromNumAffilie;
    }

    /**
     * Sets the fromAnneeDecision.
     * 
     * @param fromAnneeDecision
     *            The fromAnneeDecision to set
     */
    public void setFromAnneeDecision(String fromAnneeDecision) {
        this.fromAnneeDecision = fromAnneeDecision;
    }

    public void setMiseAjour(boolean miseAjour) {
        this.miseAjour = miseAjour;
    }

    /**
     * Sets the genreDecision.
     * 
     * @param NumAffilie
     *            le num�ro d'affili� de fin
     */
    public void setTillAffilie(String toNumAffilie) {
        tillAffilie = toNumAffilie;
    }

    /**
     * Sets the toAnneeDecision.
     * 
     * @param toAnneeDecision
     *            The toAnneeDecision to set
     */
    public void setTillAnneeDecision(String toAnneeDecision) {
        tillAnneeDecision = toAnneeDecision;
    }
}
