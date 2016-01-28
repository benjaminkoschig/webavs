package globaz.draco.process;

import globaz.draco.api.TestIDSImportDonnees;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.inscriptions.CIJournal;

public class DSProcessAnnulerValidation extends BProcess implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static String CS_DECL_MIXTE = "19170000";
    private String idDeclaration;

    /**
     * Commentaire relatif au constructeur DSProcessValidation.
     */
    public DSProcessAnnulerValidation() {
        super();
    }

    /**
     * Commentaire relatif au constructeur DSProcessValidation.
     * 
     * @param parent
     *            globaz.framework.process.FWProcess
     */
    public DSProcessAnnulerValidation(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur DSProcessValidation.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public DSProcessAnnulerValidation(globaz.globall.db.BSession session) {
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
            // Récupérer la déclaration
            DSDeclarationViewBean decl = new DSDeclarationViewBean();
            decl.setSession(getSession());
            decl.setIdDeclaration(getIdDeclaration());
            decl.retrieve(getTransaction());
            // Contrôler si la déclaration peut être lue
            if (decl.hasErrors() || decl.isNew()) {
                getMemoryLog().logMessage(getSession().getLabel("DECL_NON_EXISTANTE"), FWMessage.ERREUR, "");
                abort();
                return false;
            }
            // Contrôler l'état de la déclaration
            if (!decl.getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_AFACTURER)) {
                getMemoryLog().logMessage(getSession().getLabel("DECL_NON_FACTURE_POUR_REOUVERTURE"), FWMessage.ERREUR,
                        "");
                abort();
                return false;
            }
            // S'il existe un journal associé, il doit être ouvert
            if (!JadeStringUtil.isIntegerEmpty(decl.getIdJournal())) {
                CIJournal journalCi = new CIJournal();
                journalCi.setSession(getSession());
                journalCi.setIdJournal(decl.getIdJournal());
                journalCi.retrieve(getTransaction());
                if (!journalCi.isNew() && !getTransaction().hasErrors()) {
                    if (!CIJournal.CS_OUVERT.equals(journalCi.getIdEtat())) {
                        getMemoryLog().logMessage(getSession().getLabel("DECL_ERREUR_JOURNAL_CI_PAS_OUVERT"),
                                FWMessage.ERREUR, "");
                        abort();
                        return false;
                    }
                }
            }
            // Mise à jour de l'état de la déclaration

            // Spécifique CCVD
            if (decl.getAffiliation() != null) {
                if (DSProcessAnnulerValidation.CS_DECL_MIXTE.equals(decl.getAffiliation().getDeclarationSalaire())) {
                    String adapterName = "agrivit";
                    TestIDSImportDonnees test = new TestIDSImportDonnees();
                    test.checkAnnuleValidation(getSession(), adapterName, idDeclaration);
                }
            }
            decl.setEtat(DSDeclarationViewBean.CS_OUVERT);
            // Suppression du spy de la validation
            decl.setValidationDateSpy("");
            decl.setValidationSpy("");
            decl.setReferenceFacture("");
            decl.update(getTransaction());
            if (decl.hasErrors() || getSession().hasErrors()) {
                getMemoryLog()
                        .logMessage(getSession().getLabel("DECL_ERREUR_UPDATE_DECLARATION"), FWMessage.ERREUR, "");
                abort();
                return false;
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            e.printStackTrace();
            return false;
        }
        return !isOnError();
    }

    /**
     * Validation
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendMailOnError(true);
        setSendCompletionMail(true);
        // Modif 27.06.2006 pour reprendre l'adresse saisie à l'écran
        // setEMailAddress(getSession().getUserEMail());
    }

    /**
     * Envoi d'un Email pour les informations conernant la fin du process
     * 
     * @see BProcess#getEMailObject()
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        if (getSession().hasErrors() || isOnError() || isAborted()) {
            return getSession().getLabel("MSG_VALIDATION_ANNUL_ERREUR");
        } else {
            return getSession().getLabel("MSG_VALIDATION_ANNUL_SUCCES");
        }
    }

    /**
     * Returns the idDeclaration.
     * 
     * @return String
     */
    public String getIdDeclaration() {
        return idDeclaration;
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * Sets the idDeclaration.
     * 
     * @param idDeclaration
     *            The idDeclaration to set
     */
    public void setIdDeclaration(String idDeclaration) {
        this.idDeclaration = idDeclaration;
    }

}
