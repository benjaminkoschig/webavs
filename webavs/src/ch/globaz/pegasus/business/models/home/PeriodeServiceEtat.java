package ch.globaz.pegasus.business.models.home;

import globaz.jade.persistence.model.JadeComplexModel;
import java.io.Serializable;

public class PeriodeServiceEtat extends JadeComplexModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleHome simpleHome = null;
    private SimplePeriodeServiceEtat simplePeriodeServiceEtat = null;

    /**
     * @param simpleHome
     * @param simplePeriodeServiceEtat
     */
    public PeriodeServiceEtat() {
        super();
        simpleHome = new SimpleHome();
        simplePeriodeServiceEtat = new SimplePeriodeServiceEtat();
    }

    @Override
    public String getId() {
        return simplePeriodeServiceEtat.getId();
    }

    /**
     * @return the home
     */
    public SimpleHome getSimpleHome() {
        return simpleHome;
    }

    /**
     * @return the simplePeriodeServiceEtat
     */
    public SimplePeriodeServiceEtat getSimplePeriodeServiceEtat() {
        return simplePeriodeServiceEtat;
    }

    @Override
    public String getSpy() {
        return simplePeriodeServiceEtat.getSpy();
    }

    @Override
    public void setId(String id) {
        simplePeriodeServiceEtat.setId(id);
    }

    /**
     * @param simpleHome
     *            the home to set
     */
    public void setSimpleHome(SimpleHome simpleHome) {
        this.simpleHome = simpleHome;
        simplePeriodeServiceEtat.setIdHome(simpleHome.getId());
    }

    /**
     * @param simplePeriodeServiceEtat
     *            the simplePeriodeServiceEtat to set
     */
    public void setSimplePeriodeServiceEtat(SimplePeriodeServiceEtat simplePeriodeServiceEtat) {
        this.simplePeriodeServiceEtat = simplePeriodeServiceEtat;
    }

    @Override
    public void setSpy(String spy) {
        simplePeriodeServiceEtat.setSpy(spy);
    }

}
