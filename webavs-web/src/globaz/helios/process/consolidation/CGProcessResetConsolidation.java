package globaz.helios.process.consolidation;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.helios.api.consolidation.ICGConsolidationSolde;
import globaz.helios.api.consolidation.ICGConsolidationSoldeManager;
import globaz.helios.application.CGApplication;
import globaz.helios.db.comptes.CGExerciceComptableViewBean;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.db.comptes.CGSolde;
import globaz.helios.db.comptes.CGSoldeManager;
import globaz.helios.db.consolidation.CGSoldeBouclementSuccursale;
import globaz.helios.db.consolidation.CGSoldeBouclementSuccursaleManager;
import globaz.helios.process.consolidation.utils.CGProcessConsolidationUtil;
import globaz.jade.client.util.JadeStringUtil;

public class CGProcessResetConsolidation extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_MANDAT_NON_OFAS = "MANDAT_NON_OFAS";
    private CGExerciceComptableViewBean exerciceComptable;
    private String idExerciceComptable;

    /**
     * Constructor for CGProcessImportConsolidation.
     */
    public CGProcessResetConsolidation() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * Constructor for CGProcessImportConsolidation.
     * 
     * @param parent
     */
    public CGProcessResetConsolidation(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessImportConsolidation.
     * 
     * @param session
     */
    public CGProcessResetConsolidation(BSession session) throws Exception {
        super(session);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        if (!isMandatOfasAndConsolidation()) {
            _addError(getTransaction(), getSession().getLabel(LABEL_MANDAT_NON_OFAS));
            return false;
        }

        CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
        manager.setSession(getSession());
        manager.setForIdExerciceComptable(getIdExerciceComptable());
        manager.setOrderBy(CGPeriodeComptableManager.TRI_DATE_FIN_ASC_AND_TYPE_ASC);
        manager.find();

        for (int i = 0; i < manager.size(); i++) {
            CGPeriodeComptable periode = (CGPeriodeComptable) manager.get(i);

            deleteSolde(new CGSoldeManager(), periode.getIdPeriodeComptable());
            deleteSolde(new CGSoldeBouclementSuccursaleManager(), periode.getIdPeriodeComptable());
        }

        deleteSolde(new CGSoldeManager(), CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE);
        deleteSolde(new CGSoldeBouclementSuccursaleManager(), CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE);

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        if (!isMandatOfasAndConsolidation()) {
            _addError(getTransaction(), getSession().getLabel(LABEL_MANDAT_NON_OFAS));
        }
    }

    /**
     * Mise à jour du solde, débit et crédit.<br/>
     * Méthod utilisé pour mettre à jour CGSolde et CGSoldeBouclementSuccursale.
     * 
     * @param manager
     * @param idSuccursale
     * @param idPeriodeComptable
     * @param idCompte
     * @param soldeActif
     * @param soldePassif
     * @throws Exception
     */
    private void deleteSolde(ICGConsolidationSoldeManager manager, String idPeriodeComptable) throws Exception {
        manager.setISession(getSession());

        manager.setForIdPeriodeComptable(idPeriodeComptable);
        manager.setForIdExerComptable(getIdExerciceComptable());
        manager.setForIdMandat(getExerciceComptable().getIdMandat());

        manager.setForEstPeriode(new Boolean(!JadeStringUtil.isIntegerEmpty(idPeriodeComptable)));

        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }

        for (int i = 0; i < manager.size(); i++) {
            ICGConsolidationSolde entity;
            if (manager instanceof CGSoldeBouclementSuccursaleManager) {
                entity = (CGSoldeBouclementSuccursale) manager.get(i);
            } else {
                entity = (CGSolde) manager.get(i);
            }

            entity.delete(getTransaction());
        }
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("CONSOLIDATION_RESET_ERROR");
        } else {
            return getSession().getLabel("CONSOLIDATION_RESET_OK");
        }
    }

    /**
     * Retourne l'éxercice comptable en cours du siège.
     * 
     * @return
     * @throws Exception
     */
    private CGExerciceComptableViewBean getExerciceComptable() throws Exception {
        if (exerciceComptable == null) {
            exerciceComptable = CGProcessConsolidationUtil.getExerciceComptable(getSession(), getTransaction(),
                    getIdExerciceComptable());
        }

        return exerciceComptable;
    }

    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    private boolean isMandatOfasAndConsolidation() throws Exception {
        return getExerciceComptable().getMandat().isTypePlanComptableOfas()
                && getExerciceComptable().getMandat().isMandatConsolidation();
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setIdExerciceComptable(String idExerciceComptable) {
        this.idExerciceComptable = idExerciceComptable;
    }

}
