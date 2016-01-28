package globaz.phenix.reprise;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.db.principale.CPDecisionAffiliationManager;

/**
 * Process de génaration d'une liste excel pour les concordances entre Cot.Pers. et CI Date de création : (17.06.2007
 * 08:34:14)
 * 
 * @author: jpa
 */
public class CPProcessListeComplementaireErronee extends BProcess {
    public static void main(String[] args) {
        CPProcessListeComplementaireErronee process = null;
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
            System.out.println("Année : " + args[3]);
            System.out.println("Mise à jour : " + args[4]);
            BSession session = (BSession) GlobazSystem.getApplication("PHENIX").newSession(user, pwd);
            System.out.println("Reprise started...");
            session.connect(user, pwd);
            process = new CPProcessListeComplementaireErronee();
            process.setSession(session);
            process.setEMailAddress(email);
            if (!"0".equalsIgnoreCase(args[3])) {
                process.setForAnneeDecision(args[3]);
            }
            if (!"0".equalsIgnoreCase(args[4])) {
                process.setMiseAjourDecision(Boolean.TRUE);
            }
            process.executeProcess();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            System.out.println("La reprise pour anciens clients est terminé.");
        }
        System.exit(0);
    }

    // Année de début de la recherche
    private String forAnneeDecision = "";

    private Boolean miseAjourDecision = Boolean.FALSE;

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     */
    public CPProcessListeComplementaireErronee() {
        super();
    }

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessListeComplementaireErronee(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public CPProcessListeComplementaireErronee(BSession session) {
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
            manager.setForAnneeDecision(getForAnneeDecision());
            manager.setIsActive(Boolean.TRUE);
            manager.setForIsComplementaire(Boolean.TRUE);
            manager.setForIdTiers("1137277");
            manager.orderByIdTiers();
            manager.orderByNoAffilie();
            manager.orderByAnnee();
            manager.orderByIdDecision();
            manager.changeManagerSize(0);
            // manager.find();

            // Création du document
            CPListeComplementaireErronnee excelDoc = new CPListeComplementaireErronnee(getSession(),
                    getForAnneeDecision());
            excelDoc.setSession(getSession());
            excelDoc.setMiseAjourDecision(getMiseAjourDecision());
            excelDoc.setProcessAppelant(this);
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
     * Returns the fromAnneeDecision.
     * 
     * @return String
     */
    public String getForAnneeDecision() {
        return forAnneeDecision;
    }

    public Boolean getMiseAjourDecision() {
        return miseAjourDecision;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Sets the fromAnneeDecision.
     * 
     * @param fromAnneeDecision
     *            The fromAnneeDecision to set
     */
    public void setForAnneeDecision(String fromAnneeDecision) {
        forAnneeDecision = fromAnneeDecision;
    }

    public void setMiseAjourDecision(Boolean miseAjourDecision) {
        this.miseAjourDecision = miseAjourDecision;
    }
}
