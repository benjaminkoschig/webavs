package globaz.lynx.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.journal.LXJournal;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.operation.LXOperationManager;
import globaz.lynx.db.ventilation.LXVentilation;
import globaz.lynx.db.ventilation.LXVentilationManager;
import globaz.lynx.utils.LXJournalUtil;
import java.util.ArrayList;

public class LXExtourneProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateExtourne;
    private String idJournal;
    private String idOperation;

    private LXOperation operation;

    /**
     * @see BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
        // Do nothing
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        LXOperation opeExtourne = null;
        LXJournal journalExtourne = null;

        try {
            if (isAborted()) {
                return false;
            }

            // Recuperation du clone de l'opération a extourner
            opeExtourne = (LXOperation) getOperation().clone();

            if (isAborted()) {
                return false;
            }

            // Création du journal
            journalExtourne = createJournal();

            if (isAborted()) {
                return false;
            }

            // Création du montant inverse
            FWCurrency currency = new FWCurrency();
            currency.add(opeExtourne.getMontant());
            currency.negate();

            if (isAborted()) {
                return false;
            }

            // Création de l'extourne
            createOperation(opeExtourne, journalExtourne.getIdJournal(), currency.toString());

            if (isAborted()) {
                return false;
            }

            // Création des ventilations
            createVentilations(getIdOperation(), opeExtourne.getId());
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            return false;
        }

        return true;
    }

    /**
     * @see BProcess#_validate() throws Exception
     */
    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isIntegerEmpty(getIdOperation())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_OPERATION"));
            return;
        }

        if (JadeStringUtil.isBlank(getDateExtourne())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_EXTOURNE_DATE"));
            return;
        }

        // Check si on a pas déjà réalisé une extourne
        if (checkExtourne()) {
            this._addError(getTransaction(), getSession().getLabel("VAL_EXTOURNE_EXISTANTE"));
            return;
        }

    }

    /**
     * Check si l'opération que lôn veut extourner est déjà extournée !
     * 
     * @return true si une extourne existe
     * @throws Exception
     */
    private boolean checkExtourne() throws Exception {
        LXOperationManager manager = new LXOperationManager();
        manager.setSession(getSession());

        // Recherche du type extourne
        ArrayList<String> listType = new ArrayList<String>();
        listType.add(LXOperation.CS_TYPE_EXTOURNE);
        manager.setForIdTypeOperationIn(listType);

        // Une extourne pour l'opération source
        manager.setForIdOperationSrc(getIdOperation());
        manager.find();

        if (manager.size() > 0) {
            return true;
        }

        return false;
    }

    /**
     * Créé et retourne un journal
     * 
     * @return
     * @throws Exception
     */
    private LXJournal createJournal() throws Exception {
        return LXJournalUtil.fetchJournalJournalier(getSession(), getTransaction(), getJournal().getIdSociete());
    }

    /**
     * Permet la création en base de l'extourne
     * 
     * @param opeExtourne
     * @param montant
     * @throws Exception
     */
    public void createOperation(LXOperation opeExtourne, String idJournal, String montant) throws Exception {

        String idOperationSrc = opeExtourne.getIdOperation();

        opeExtourne.setSession(getSession());
        opeExtourne.setIdOperation(null);
        opeExtourne.setDateEcheance(null);
        opeExtourne.setMontant(montant);
        opeExtourne.setDateOperation(getDateExtourne());
        opeExtourne.setCsTypeOperation(LXOperation.CS_TYPE_EXTOURNE);
        opeExtourne.setCsEtatOperation(LXOperation.CS_ETAT_OUVERT);
        opeExtourne.setIdJournal(idJournal);
        opeExtourne.setIdOperationSrc(idOperationSrc);

        opeExtourne.add(getTransaction());

        if (opeExtourne.hasErrors()) {
            throw new Exception(opeExtourne.getErrors().toString());
        }

        if (opeExtourne.isNew()) {
            throw new Exception(getSession().getLabel("EXTOURNE_NON_CREE"));
        }

    }

    /**
     * Crée les ventilations
     * 
     * @param forIdOperation
     * @param idOperationExtourne
     * @param montant
     * @throws Exception
     */
    private void createVentilations(String forIdOperation, String idOperationExtourne) throws Exception {
        LXVentilationManager ventilationManager = new LXVentilationManager();
        ventilationManager.setSession(getSession());
        ventilationManager.setForIdOperation(forIdOperation);
        ventilationManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        if (!ventilationManager.isEmpty()) {

            for (int i = 0; i < ventilationManager.size(); i++) {
                LXVentilation ventilation = (LXVentilation) ventilationManager.get(i);

                LXVentilation ventilationExtourner = (LXVentilation) ventilation.clone();
                ventilationExtourner.setIdVentilation(null);
                ventilationExtourner.setIdOperation(idOperationExtourne);

                // On inverse le montant
                FWCurrency currency = new FWCurrency();
                currency.add(ventilationExtourner.getMontant());
                currency.negate();

                ventilationExtourner.setMontant(currency.toString());

                ventilationExtourner.add(getTransaction());

                if (ventilationExtourner.hasErrors()) {
                    throw new Exception(ventilationExtourner.getErrors().toString());
                }

                if (ventilationExtourner.isNew()) {
                    throw new Exception(getSession().getLabel("EXTOURNE_VENTILATION_NON_CREE"));
                }
            }
        } else {
            throw new Exception(getSession().getLabel("EXTOURNE_VENTILATION_NON_TROUVEE"));
        }
    }

    public String getDateExtourne() {
        return dateExtourne;
    }

    /**
     * Renvois le sujet de l'email.
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("EXTOURNER_ERREUR");
        } else {
            return getSession().getLabel("EXTOURNER_OK");
        }
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdOperation() {
        return idOperation;
    }

    /**
     * Charge et return le journal fournisseur.
     * 
     * @return
     */
    private LXJournal getJournal() throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            throw new Exception(getSession().getLabel("VAL_IDENTIFIANT_JOURNAL"));
        }

        LXJournal journal = new LXJournal();
        journal.setSession(getSession());
        journal.setIdJournal(getIdJournal());

        journal.retrieve(getTransaction());

        if (journal.hasErrors()) {
            throw new Exception(journal.getErrors().toString());
        }

        if (journal.isNew()) {
            throw new Exception(getSession().getLabel("VAL_IDENTIFIANT_JOURNAL"));
        }

        return journal;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    /**
     * Retrouve l'opération si pas encore chargée.
     * 
     * @return
     */
    public LXOperation getOperation() throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdOperation())) {
            throw new Exception(getSession().getLabel("VAL_IDENTIFIANT_OPERATION"));
        }

        if (operation == null) {
            operation = new LXOperation();
            operation.setSession(getSession());
            operation.setIdOperation(getIdOperation());
            operation.retrieve(getTransaction());

            if (operation.hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }

            if (operation.isNew()) {
                throw new Exception(getSession().getLabel("VAL_IDENTIFIANT_OPERATION"));
            }
        }

        return operation;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setDateExtourne(String dateExtourne) {
        this.dateExtourne = dateExtourne;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdOperation(String idOperation) {
        this.idOperation = idOperation;
    }
}
