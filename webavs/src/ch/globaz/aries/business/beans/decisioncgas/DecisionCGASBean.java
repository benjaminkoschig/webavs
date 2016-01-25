package ch.globaz.aries.business.beans.decisioncgas;

import globaz.jade.persistence.model.JadeAbstractModel;
import ch.globaz.aries.business.models.SimpleDecisionCGAS;
import ch.globaz.aries.business.models.SimpleDetailDecisionCGAS;

public class DecisionCGASBean extends JadeAbstractModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDetailDecisionCGAS alpage;
    private SimpleDetailDecisionCGAS cultureArboricole;
    private SimpleDetailDecisionCGAS cultureMaraichere;
    private SimpleDetailDecisionCGAS culturePlaine;
    private SimpleDecisionCGAS decisionCGAS;
    private SimpleDetailDecisionCGAS ugbMontagne;
    private SimpleDetailDecisionCGAS ugbPlaine;
    private SimpleDetailDecisionCGAS ugbSpecial;
    private SimpleDetailDecisionCGAS vigneEstCanton;
    private SimpleDetailDecisionCGAS vigneLaCote;
    private SimpleDetailDecisionCGAS vigneNordCanton;

    public DecisionCGASBean() {
        decisionCGAS = new SimpleDecisionCGAS();
        culturePlaine = new SimpleDetailDecisionCGAS();
        cultureArboricole = new SimpleDetailDecisionCGAS();
        cultureMaraichere = new SimpleDetailDecisionCGAS();
        vigneNordCanton = new SimpleDetailDecisionCGAS();
        vigneEstCanton = new SimpleDetailDecisionCGAS();
        vigneLaCote = new SimpleDetailDecisionCGAS();
        ugbMontagne = new SimpleDetailDecisionCGAS();
        ugbPlaine = new SimpleDetailDecisionCGAS();
        ugbSpecial = new SimpleDetailDecisionCGAS();
        alpage = new SimpleDetailDecisionCGAS();

    }

    public SimpleDetailDecisionCGAS getAlpage() {
        return alpage;
    }

    public SimpleDetailDecisionCGAS getCultureArboricole() {
        return cultureArboricole;
    }

    public SimpleDetailDecisionCGAS getCultureMaraichere() {
        return cultureMaraichere;
    }

    public SimpleDetailDecisionCGAS getCulturePlaine() {
        return culturePlaine;
    }

    public SimpleDecisionCGAS getDecisionCGAS() {
        return decisionCGAS;
    }

    @Override
    public String getId() {
        return decisionCGAS.getId();
    }

    @Override
    public String getSpy() {
        return decisionCGAS.getSpy();
    }

    public SimpleDetailDecisionCGAS getUgbMontagne() {
        return ugbMontagne;
    }

    public SimpleDetailDecisionCGAS getUgbPlaine() {
        return ugbPlaine;
    }

    public SimpleDetailDecisionCGAS getUgbSpecial() {
        return ugbSpecial;
    }

    public SimpleDetailDecisionCGAS getVigneEstCanton() {
        return vigneEstCanton;
    }

    public SimpleDetailDecisionCGAS getVigneLaCote() {
        return vigneLaCote;
    }

    public SimpleDetailDecisionCGAS getVigneNordCanton() {
        return vigneNordCanton;
    }

    public void setAlpage(SimpleDetailDecisionCGAS alpage) {
        this.alpage = alpage;
    }

    public void setCultureArboricole(SimpleDetailDecisionCGAS cultureArboricole) {
        this.cultureArboricole = cultureArboricole;
    }

    public void setCultureMaraichere(SimpleDetailDecisionCGAS cultureMaraichere) {
        this.cultureMaraichere = cultureMaraichere;
    }

    public void setCulturePlaine(SimpleDetailDecisionCGAS culturePlaine) {
        this.culturePlaine = culturePlaine;
    }

    public void setDecisionCGAS(SimpleDecisionCGAS decisionCGAS) {
        this.decisionCGAS = decisionCGAS;
    }

    @Override
    public void setId(String id) {
        decisionCGAS.setId(id);

    }

    @Override
    public void setSpy(String spy) {
        decisionCGAS.setSpy(spy);
    }

    public void setUgbMontagne(SimpleDetailDecisionCGAS ugbMontagne) {
        this.ugbMontagne = ugbMontagne;
    }

    public void setUgbPlaine(SimpleDetailDecisionCGAS ugbPlaine) {
        this.ugbPlaine = ugbPlaine;
    }

    public void setUgbSpecial(SimpleDetailDecisionCGAS ugbSpecial) {
        this.ugbSpecial = ugbSpecial;
    }

    public void setVigneEstCanton(SimpleDetailDecisionCGAS vigneEstCanton) {
        this.vigneEstCanton = vigneEstCanton;
    }

    public void setVigneLaCote(SimpleDetailDecisionCGAS vigneLaCote) {
        this.vigneLaCote = vigneLaCote;
    }

    public void setVigneNordCanton(SimpleDetailDecisionCGAS vigneNordCanton) {
        this.vigneNordCanton = vigneNordCanton;
    }

}