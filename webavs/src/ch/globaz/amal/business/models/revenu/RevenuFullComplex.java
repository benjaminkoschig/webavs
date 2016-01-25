package ch.globaz.amal.business.models.revenu;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.amal.business.calcul.CalculsRevenuFormules;

public class RevenuFullComplex extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CalculsRevenuFormules calculsRevenuFormules = null;
    private SimpleRevenu simpleRevenu = null;
    private SimpleRevenuContribuable simpleRevenuContribuable = null;
    private SimpleRevenuSourcier simpleRevenuSourcier = null;

    public RevenuFullComplex() {
        super();
        simpleRevenu = new SimpleRevenu();
        simpleRevenuContribuable = new SimpleRevenuContribuable();
        simpleRevenuSourcier = new SimpleRevenuSourcier();
        calculsRevenuFormules = new CalculsRevenuFormules();
    }

    public CalculsRevenuFormules getCalculsRevenuFormules() {
        return calculsRevenuFormules;
    }

    @Override
    public String getId() {
        return simpleRevenu.getId();
    }

    public SimpleRevenu getSimpleRevenu() {
        return simpleRevenu;
    }

    public SimpleRevenuContribuable getSimpleRevenuContribuable() {
        return simpleRevenuContribuable;
    }

    public SimpleRevenuSourcier getSimpleRevenuSourcier() {
        return simpleRevenuSourcier;
    }

    @Override
    public String getSpy() {
        return simpleRevenu.getSpy();
    }

    public void setCalculsRevenuFormules(CalculsRevenuFormules calculsRevenuFormules) {
        this.calculsRevenuFormules = calculsRevenuFormules;
    }

    @Override
    public void setId(String id) {
        simpleRevenu.setId(id);
    }

    public void setSimpleRevenu(SimpleRevenu simpleRevenu) {
        this.simpleRevenu = simpleRevenu;
    }

    public void setSimpleRevenuContribuable(SimpleRevenuContribuable simpleRevenuContribuable) {
        this.simpleRevenuContribuable = simpleRevenuContribuable;
    }

    public void setSimpleRevenuSourcier(SimpleRevenuSourcier simpleRevenuSourcier) {
        this.simpleRevenuSourcier = simpleRevenuSourcier;
    }

    @Override
    public void setSpy(String spy) {
        simpleRevenu.setSpy(spy);
    }

}
