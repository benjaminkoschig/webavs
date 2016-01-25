package globaz.helios.db.comptes.helper;

import globaz.framework.util.FWCurrency;

/**
 * Classe : type_conteneur
 * 
 * Description :
 * 
 * Date de création: 10 mai 04
 * 
 * @author scr
 * 
 */
public class CGSoldeDataContainer {

    private FWCurrency avoir = new FWCurrency(0);
    private FWCurrency avoirMonnaie = new FWCurrency(0);
    private FWCurrency avoirProvisoire = new FWCurrency(0);
    private FWCurrency avoirProvisoireMonnaie = new FWCurrency(0);
    private FWCurrency budget = new FWCurrency(0);
    private FWCurrency doit = new FWCurrency(0);

    private FWCurrency doitMonnaie = new FWCurrency(0);
    private FWCurrency doitProvisoire = new FWCurrency(0);
    private FWCurrency doitProvisoireMonnaie = new FWCurrency(0);
    private FWCurrency solde = new FWCurrency(0);
    private FWCurrency soldeMonnaie = new FWCurrency(0);
    private FWCurrency soldeProvisoire = new FWCurrency(0);

    private FWCurrency soldeProvisoireMonnaie = new FWCurrency(0);

    /**
     * Constructor for CGSoldeDataContainer.
     */
    public CGSoldeDataContainer() {
        super();
    }

    public void addAvoir(String s) {
        avoir.add(s);
    }

    public void addAvoirMonnaie(String s) {
        avoirMonnaie.add(s);
    }

    public void addAvoirProvisoire(String s) {
        avoirProvisoire.add(s);
    }

    public void addAvoirProvisoireMonnaie(String s) {
        avoirProvisoireMonnaie.add(s);
    }

    public void addBudget(String s) {
        budget.add(s);
    }

    public void addDoit(String s) {
        doit.add(s);
    }

    public void addDoitMonnaie(String s) {
        doitMonnaie.add(s);
    }

    public void addDoitProvisoire(String s) {
        doitProvisoire.add(s);
    }

    public void addDoitProvisoireMonnaie(String s) {
        doitProvisoireMonnaie.add(s);
    }

    public void addSolde(String s) {
        solde.add(s);
    }

    public void addSoldeMonnaie(String s) {
        soldeMonnaie.add(s);
    }

    public void addSoldeProvisoire(String s) {
        soldeProvisoire.add(s);
    }

    public void addSoldeProvisoireMonnaie(String s) {
        soldeProvisoireMonnaie.add(s);
    }

    /**
     * Returns the avoir.
     * 
     * @return FWCurrency
     */
    public FWCurrency getAvoir() {
        return avoir;
    }

    /**
     * Returns the avoirMonnaie.
     * 
     * @return FWCurrency
     */
    public FWCurrency getAvoirMonnaie() {
        return avoirMonnaie;
    }

    /**
     * Returns the avoirProvisoire.
     * 
     * @return FWCurrency
     */
    public FWCurrency getAvoirProvisoire() {
        return avoirProvisoire;
    }

    /**
     * Returns the avoirProvisoireMonnaie.
     * 
     * @return FWCurrency
     */
    public FWCurrency getAvoirProvisoireMonnaie() {
        return avoirProvisoireMonnaie;
    }

    /**
     * Returns the budget.
     * 
     * @return FWCurrency
     */
    public FWCurrency getBudget() {
        return budget;
    }

    /**
     * Returns the doit.
     * 
     * @return FWCurrency
     */
    public FWCurrency getDoit() {
        return doit;
    }

    /**
     * Returns the doitMonnaie.
     * 
     * @return FWCurrency
     */
    public FWCurrency getDoitMonnaie() {
        return doitMonnaie;
    }

    /**
     * Returns the doitProvisoire.
     * 
     * @return FWCurrency
     */
    public FWCurrency getDoitProvisoire() {
        return doitProvisoire;
    }

    /**
     * Returns the doitProvisoireMonnaie.
     * 
     * @return FWCurrency
     */
    public FWCurrency getDoitProvisoireMonnaie() {
        return doitProvisoireMonnaie;
    }

    /**
     * Returns the solde.
     * 
     * @return FWCurrency
     */
    public FWCurrency getSolde() {
        return solde;
    }

    /**
     * Returns the soldeMonnaie.
     * 
     * @return FWCurrency
     */
    public FWCurrency getSoldeMonnaie() {
        return soldeMonnaie;
    }

    /**
     * Returns the soldeProvisoire.
     * 
     * @return FWCurrency
     */
    public FWCurrency getSoldeProvisoire() {
        return soldeProvisoire;
    }

    /**
     * Returns the soldeProvisoireMonnaie.
     * 
     * @return FWCurrency
     */
    public FWCurrency getSoldeProvisoireMonnaie() {
        return soldeProvisoireMonnaie;
    }

    /**
     * Sets the avoir.
     * 
     * @param avoir
     *            The avoir to set
     */
    public void setAvoir(FWCurrency avoir) {
        this.avoir = avoir;
    }

    /**
     * Sets the avoirMonnaie.
     * 
     * @param avoirMonnaie
     *            The avoirMonnaie to set
     */
    public void setAvoirMonnaie(FWCurrency avoirMonnaie) {
        this.avoirMonnaie = avoirMonnaie;
    }

    /**
     * Sets the avoirProvisoire.
     * 
     * @param avoirProvisoire
     *            The avoirProvisoire to set
     */
    public void setAvoirProvisoire(FWCurrency avoirProvisoire) {
        this.avoirProvisoire = avoirProvisoire;
    }

    /**
     * Sets the avoirProvisoireMonnaie.
     * 
     * @param avoirProvisoireMonnaie
     *            The avoirProvisoireMonnaie to set
     */
    public void setAvoirProvisoireMonnaie(FWCurrency avoirProvisoireMonnaie) {
        this.avoirProvisoireMonnaie = avoirProvisoireMonnaie;
    }

    /**
     * Sets the budget.
     * 
     * @param budget
     *            The budget to set
     */
    public void setBudget(FWCurrency budget) {
        this.budget = budget;
    }

    /**
     * Sets the doit.
     * 
     * @param doit
     *            The doit to set
     */
    public void setDoit(FWCurrency doit) {
        this.doit = doit;
    }

    /**
     * Sets the doitMonnaie.
     * 
     * @param doitMonnaie
     *            The doitMonnaie to set
     */
    public void setDoitMonnaie(FWCurrency doitMonnaie) {
        this.doitMonnaie = doitMonnaie;
    }

    /**
     * Sets the doitProvisoire.
     * 
     * @param doitProvisoire
     *            The doitProvisoire to set
     */
    public void setDoitProvisoire(FWCurrency doitProvisoire) {
        this.doitProvisoire = doitProvisoire;
    }

    /**
     * Sets the doitProvisoireMonnaie.
     * 
     * @param doitProvisoireMonnaie
     *            The doitProvisoireMonnaie to set
     */
    public void setDoitProvisoireMonnaie(FWCurrency doitProvisoireMonnaie) {
        this.doitProvisoireMonnaie = doitProvisoireMonnaie;
    }

    /**
     * Sets the solde.
     * 
     * @param solde
     *            The solde to set
     */
    public void setSolde(FWCurrency solde) {
        this.solde = solde;
    }

    /**
     * Sets the soldeMonnaie.
     * 
     * @param soldeMonnaie
     *            The soldeMonnaie to set
     */
    public void setSoldeMonnaie(FWCurrency soldeMonnaie) {
        this.soldeMonnaie = soldeMonnaie;
    }

    /**
     * Sets the soldeProvisoire.
     * 
     * @param soldeProvisoire
     *            The soldeProvisoire to set
     */
    public void setSoldeProvisoire(FWCurrency soldeProvisoire) {
        this.soldeProvisoire = soldeProvisoire;
    }

    /**
     * Sets the soldeProvisoireMonnaie.
     * 
     * @param soldeProvisoireMonnaie
     *            The soldeProvisoireMonnaie to set
     */
    public void setSoldeProvisoireMonnaie(FWCurrency soldeProvisoireMonnaie) {
        this.soldeProvisoireMonnaie = soldeProvisoireMonnaie;
    }

}
