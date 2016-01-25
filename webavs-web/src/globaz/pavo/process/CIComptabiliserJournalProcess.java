package globaz.pavo.process;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.util.FWMessage;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAUtil;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.util.CIUtil;

public class CIComptabiliserJournalProcess extends globaz.framework.process.FWProcess {

    private static final long serialVersionUID = 5852936117958824908L;
    private String fromAvs;
    // Données de comptabilisation
    private String idJournal;
    private String toAvs;

    /**
     * Commentaire relatif au constructeur CIComptabiliserJournalProcess.
     */
    public CIComptabiliserJournalProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CIComptabiliserJournalProcess.
     * 
     * @param parent
     *            globaz.framework.process.FWProcess
     */
    public CIComptabiliserJournalProcess(globaz.framework.process.FWProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur CIComptabiliserJournalProcess.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CIComptabiliserJournalProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        // Si toAvs < fromAvs on arrête
        if ((!JAUtil.isStringEmpty(getFromAvs())) && (!JAUtil.isStringEmpty(getToAvs()))) {
            if (new Double(getToAvs()).doubleValue() < new Double(getFromAvs()).doubleValue()) {
                getMemoryLog()
                        .logMessage(
                                "Comptabilisation échouée. La borne supérieure des numéros AVS doit être inférieure à la borne inférieure.",
                                FWMessage.FATAL, getClass().getName());
            }
        }
        // On récupère le journal
        try {
            CIJournal journal = new CIJournal();
            journal.setSession(getSession());
            journal.setIdJournal(getIdJournal());
            journal.retrieve(getTransaction());
            if (!getTransaction().hasErrors()) {
                // On comptabilise
                StringBuffer errors = journal.comptabiliser(getFromAvs(), getToAvs(), getTransaction(), this);
                if (getTransaction().hasErrors() || errors.length() > 0) {
                    getMemoryLog().logStringBuffer(getTransaction().getErrors(), getClass().getName());
                    getMemoryLog().logMessage("Comptabilisation échouée.", FWMessage.FATAL, getClass().getName());

                }
                if (errors.length() > 0) {
                    getMemoryLog().logMessage(errors.toString(), FWMessage.INFORMATION, getClass().getName());

                }
                if (isAborted()) {
                    setProgressDescription(errors.toString());
                }

            } else {
                getMemoryLog().logMessage("Comptabilisation échouée. Erreur de récupération du journal.",
                        FWMessage.FATAL, getClass().getName());
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, getClass().getName());
        }
        // Ne pas envoyer de mail de quittancement si le journal s'est
        // correctement comptabilisé.
        if (!isOnError()) {
            setSendCompletionMail(false);
        }
        return !isOnError();
    }

    @Override
    protected void _validate() {
        // On charge le journal et si déjà comptabilié, on
        // envoie un message d'erreur.
        CIJournal journal = new CIJournal();
        journal.setSession(getSession());
        journal.setIdJournal(getIdJournal());
        try {
            journal.retrieve(getTransaction());
        } catch (Exception e) {
        }
        if ((!journal.getProprietaireNomComplet().equals(getSession().getUserFullName()))
                && !CIUtil.isSpecialist(getSession())) {
            _addError(getSession().getLabel("MSG_PAS_LES_DROITS_DE_COMPT_UN_JOURNAL"));
        }
        if (journal != null && !journal.isNew()) {
            if (CIJournal.CS_COMPTABILISE.equals(journal.getIdEtat())) {
                _addError(getSession().getLabel("MSG_JOURNAL_DEJA_COMPTABILISE"));
            }
        }
        if (!JAUtil.isIntegerEmpty(journal.getTotalControle())) {
            if (!journal.getTotalControle().equals(journal.getTotalInscrit())) {
                _addError(getSession().getLabel("MSG_JOURNAL_COMPT_TOTAUX") + getIdJournal());
            }
        }
        if (JAUtil.isStringEmpty(getEMailAddress())) {
            _addError(getSession().getLabel("MSG_COMPTA_JOURNAL_EMAIL"));
        } else {
            if (getEMailAddress().indexOf('@') == -1) {
                _addError(getSession().getLabel("MSG_COMPTA_JOURNAL_EMAIL)_INV"));
            }
        }
        if (JAUtil.isStringEmpty(getIdJournal())) {
            _addError(getSession().getLabel("MSG_COMPTA_JOURNAL_ID"));
        }
        // Si toAvs < fromAvs on arrête
        if ((!JAUtil.isStringEmpty(getFromAvs())) && (!JAUtil.isStringEmpty(getToAvs()))) {
            if (new Double(getToAvs()).doubleValue() < new Double(getFromAvs()).doubleValue()) {
                _addError(getSession().getLabel("MSG_COMPTA_JOURNAL_AVS"));
            }
        }
        // Ajout plausibilité 1-5-8 => si journal provenant de draco, compta.
        // manuelle impossible
        if (provenanceDraco()) {
            _addError(getSession().getLabel("MSG_PROVENANCE_JOURNAL_DRACO"));
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    public String getDescriptionJournal() {
        CIJournal jour = new CIJournal();
        jour.setSession(getSession());
        jour.setIdJournal(getIdJournal());
        try {
            jour.retrieve();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jour.getDescription();
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return "La comptabilisation a échoué. ID du journal: " + getIdJournal();
        } else {
            return "La comptabilisation a réussi. ID du journal: " + getIdJournal();
        }
    }

    public String getFromAvs() {
        return fromAvs;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdTypeInscription() {
        CIJournal jour = new CIJournal();
        jour.setSession(getSession());
        jour.setIdJournal(getIdJournal());
        try {
            jour.retrieve();
        } catch (Exception e) {
            // TODO Bloc catch auto-généré
            e.printStackTrace();
        }
        return jour.getIdTypeInscription();
    }

    public String getToAvs() {
        return toAvs;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Méthode qui indique si le journal a été généré par Draco
     * 
     * @return boolean
     */
    public boolean provenanceDraco() {
        try {
            DSDeclarationListViewBean mgr = new DSDeclarationListViewBean();
            mgr.setForEtatNotLike(DSDeclarationViewBean.CS_COMPTABILISE);
            mgr.setSession(getSession());
            mgr.setForIdJournal(getIdJournal());
            return mgr.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setFromAvs(String newFromAvs) {
        fromAvs = CIUtil.unFormatAVS(newFromAvs);
    }

    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }

    public void setToAvs(String newToAvs) {
        toAvs = CIUtil.unFormatAVS(newToAvs);
    }

}
