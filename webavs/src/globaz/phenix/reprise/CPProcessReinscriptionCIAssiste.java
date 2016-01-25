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
 * Process de génaration d'une liste excel pour les concordances entre Cot.Pers. et CI Date de création : (17.06.2007
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
            System.out.println("depuis l'affilié : " + args[3]);
            System.out.println("jusqu'à l'affilié : " + args[4]);
            System.out.println("depuis l'année : " + args[5]);
            System.out.println("jusqu'à l'année : " + args[6]);
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
            System.out.println("La reprise CI est terminée.");
        }
        System.exit(0);
    }

    // Numéro d'affilié de départ
    private String fromAffilieDebut = "";
    // Année de début de la recherche
    private String fromAnneeDecision = "";

    private boolean miseAjour = false;

    // Numéro d'affilié de fin
    private String tillAffilie = "";

    // Année de fin de la recherche
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
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
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
            // !!! Mettre en premier l'ordre par idTiers à cause des affiliés
            // qui changent de n°
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

            // Création du document
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
     * Valide le contenu de l'entité (notamment les champs obligatoires) On va compter le nombre d'inscriptions
     */
    @Override
    protected void _validate() throws Exception {
        // Contrôle du mail
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            getSession().addError(getSession().getLabel("CP_MSG_0145"));
        }
        // Si aucun critêre de sélection => erreur
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
     * Return le sujet de l'email Date de création : (14.02.2002 14:22:21)
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
     * Returns le numéro d'affilié de départ
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
     * Returns le numéro d'affilié de fin.
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
     * @param numéro
     *            d'affilié de départ
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
     *            le numéro d'affilié de fin
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
