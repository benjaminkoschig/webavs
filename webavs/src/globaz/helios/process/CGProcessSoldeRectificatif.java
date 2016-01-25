package globaz.helios.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.application.CGApplication;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExtendedMvtCompteListViewBean;
import globaz.helios.db.comptes.CGExtendedMvtCompteViewBean;
import globaz.helios.db.comptes.CGSolde;
import globaz.helios.db.comptes.CGSoldeManager;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;

public class CGProcessSoldeRectificatif extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CGExerciceComptable exercice = null;
    private String idComptabilite;

    private String idCompte;
    private String idExerciceComptable;

    private String idExterneCompte = new String();

    private String idPeriodeComptable;

    private String newSolde = new String();

    /**
     * Constructor for CGProcessImportEcritures.
     */
    public CGProcessSoldeRectificatif() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * Constructor for CGProcessImportEcritures.
     * 
     * @param parent
     */
    public CGProcessSoldeRectificatif(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessImportEcritures.
     * 
     * @param session
     */
    public CGProcessSoldeRectificatif(BSession session) throws Exception {
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
        if (!tesUserValues()) {
            return false;
        }

        CGExtendedMvtCompteListViewBean manager = new CGExtendedMvtCompteListViewBean();
        manager.setSession(getSession());

        manager.setForIdPeriodeComptable(getIdPeriodeComptable());
        manager.setForIdCompte(getIdCompte());
        manager.wantForEstActive(true);
        manager.setForIdMandat(getExerciceComptable(getSession(), getTransaction()).getIdMandat());
        manager.setForIdExerciceComptable(getIdExerciceComptable());
        manager.setReqComptabilite(getIdComptabilite());
        manager.setReqVue(CodeSystem.CS_GENERAL);

        manager.changeManagerSize(BManager.SIZE_NOLIMIT);

        manager.find(getTransaction());

        FWCurrency totalSolde = new FWCurrency();
        FWCurrency totalAvoir = new FWCurrency();
        FWCurrency totalDoit = new FWCurrency();
        for (int i = 0; i < manager.size(); i++) {
            CGExtendedMvtCompteViewBean mvt = (CGExtendedMvtCompteViewBean) manager.get(i);

            totalDoit.add(mvt.getDoit());
            totalAvoir.add(mvt.getAvoir());
            totalSolde.add(mvt.getMontantBase());
        }

        if (totalSolde.compareTo(getNewSoldeCurrency()) != 0) {
            _addError(getTransaction(), getSession().getLabel("RECTIFICATIF_SOLDE_MVT_ERROR") + " (Total mvt ("
                    + totalSolde.toString() + ") <> Montant (" + getNewSolde() + "), idCompte " + getIdCompte() + ")");
            return false;
        }

        if (totalAvoir.isPositive()) {
            totalAvoir.negate();
        }

        CGSoldeManager soldeManager = new CGSoldeManager();
        soldeManager.setSession(getSession());

        soldeManager.setForEstPeriode(new Boolean(!JadeStringUtil.isIntegerEmpty(getIdPeriodeComptable())));
        soldeManager.setForIdPeriodeComptable(getIdPeriodeComptable());
        soldeManager.setForIdCompte(getIdCompte());
        soldeManager.setForIdCentreCharge("0");
        soldeManager.setForIdExerComptable(getIdExerciceComptable());

        soldeManager.find(getTransaction(), 2);

        if (soldeManager.getSize() == 1) {
            CGSolde solde = (CGSolde) soldeManager.getFirstEntity();

            if (isProvisoire()) {
                if (new FWCurrency(solde.getSoldeProvisoire()).compareTo(getNewSoldeCurrency()) != 0) {
                    solde.setSoldeProvisoire(getNewSoldeNotFormatted());
                    solde.setDoitProvisoire(totalDoit.toString());
                    solde.setAvoirProvisoire(totalAvoir.toString());
                    solde.update(getTransaction());
                } else {
                    getMemoryLog().logMessage("RECTIFICATIF_SOLDE_INFOS", null, FWMessage.INFORMATION,
                            getClass().getName());
                    return false;
                }
            } else {
                if (new FWCurrency(solde.getSolde()).compareTo(getNewSoldeCurrency()) != 0) {
                    solde.setSolde(getNewSoldeNotFormatted());
                    solde.setDoit(totalDoit.toString());
                    solde.setAvoir(totalAvoir.toString());
                    solde.update(getTransaction());
                } else {
                    getMemoryLog().logMessage("RECTIFICATIF_SOLDE_INFOS", null, FWMessage.INFORMATION,
                            getClass().getName());
                    return false;
                }
            }
        } else {
            _addError(getTransaction(), getSession().getLabel("GLOBAL_SOLDE_MANQUANT"));
            return false;
        }

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        tesUserValues();
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("RECTIFICATIF_SOLDE_ERROR");
        } else {
            return getSession().getLabel("RECTIFICATIF_SOLDE_OK");
        }
    }

    /**
     * Return l'exercice comptable sélectionné.
     * 
     * @param session
     * @param transaction
     * @return Returns the exercice.
     * @throws Exception
     */
    public CGExerciceComptable getExerciceComptable(BSession session, BTransaction transaction) throws Exception {
        if (exercice == null) {
            exercice = new CGExerciceComptable();
            exercice.setSession(session);

            exercice.setIdExerciceComptable(getIdExerciceComptable());

            exercice.retrieve(transaction);

            if (exercice.isNew()) {
                exercice = null;
            }
        }

        return exercice;
    }

    public String getIdComptabilite() {
        return idComptabilite;
    }

    public String getIdCompte() {
        return idCompte;
    }

    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    public String getIdExterneCompte() {
        return idExterneCompte;
    }

    public String getIdPeriodeComptable() {
        return idPeriodeComptable;
    }

    public String getNewSolde() {
        return newSolde;
    }

    private FWCurrency getNewSoldeCurrency() {
        return new FWCurrency(getNewSoldeNotFormatted());
    }

    private String getNewSoldeNotFormatted() {
        return JANumberFormatter.deQuote(getNewSolde());
    }

    private boolean isProvisoire() {
        return getIdComptabilite().equals(CodeSystem.CS_PROVISOIRE);
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setIdComptabilite(String idComptabilite) {
        this.idComptabilite = idComptabilite;
    }

    public void setIdCompte(String idCompte) {
        this.idCompte = idCompte;
    }

    public void setIdExerciceComptable(String idExerciceComptable) {
        this.idExerciceComptable = idExerciceComptable;
    }

    public void setIdExterneCompte(String idExterneCompte) {
        this.idExterneCompte = idExterneCompte;
    }

    public void setIdPeriodeComptable(String idPeriodeComptable) {
        this.idPeriodeComptable = idPeriodeComptable;
    }

    public void setNewSolde(String newSolde) {
        this.newSolde = newSolde;
    }

    /**
     * Test user values.
     * 
     * @return
     */
    private boolean tesUserValues() {
        if (JadeStringUtil.isIntegerEmpty(getIdCompte())) {
            _addError(getTransaction(), getSession().getLabel("ECRITURE_COMPTE_ERROR_2"));
            return false;
        }

        if (JadeStringUtil.isBlank(getNewSolde())) {
            _addError(getTransaction(), getSession().getLabel("RECTIFICATIF_SOLDE_NON_SAISIT"));
            return false;
        }

        return true;
    }

}
