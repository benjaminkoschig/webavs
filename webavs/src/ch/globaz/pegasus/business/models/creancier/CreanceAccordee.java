package ch.globaz.pegasus.business.models.creancier;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;

public class CreanceAccordee extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleCreanceAccordee simpleCreanceAccordee = null;
    private SimpleCreancier simpleCreancier = null;
    private SimplePCAccordee simplePCAccordee = null;
    private SimpleVersionDroit simpleVersionDroit = null;

    public CreanceAccordee() {
        simpleCreanceAccordee = new SimpleCreanceAccordee();
        simpleCreancier = new SimpleCreancier();
        simplePCAccordee = new SimplePCAccordee();
        simpleVersionDroit = new SimpleVersionDroit();
    }

    @Override
    public String getId() {
        return simpleCreanceAccordee.getId();
    }

    /**
     * @return the simpleCreanceAccordee
     */
    public SimpleCreanceAccordee getSimpleCreanceAccordee() {
        return simpleCreanceAccordee;
    }

    /**
     * @return the simpleCreancier
     */
    public SimpleCreancier getSimpleCreancier() {
        return simpleCreancier;
    }

    /**
     * @return the simplePCAccordee
     */
    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    /**
     * @return the simpleVersionDroit
     */
    public SimpleVersionDroit getSimpleVersionDroit() {
        return simpleVersionDroit;
    }

    @Override
    public String getSpy() {
        return simpleCreanceAccordee.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleCreanceAccordee.setId(id);
    }

    /**
     * @param simpleCreanceAccordee
     *            the simpleCreanceAccordee to set
     */
    public void setSimpleCreanceAccordee(SimpleCreanceAccordee simpleCreanceAccordee) {
        this.simpleCreanceAccordee = simpleCreanceAccordee;
    }

    /**
     * @param simpleCreancier
     *            the simpleCreancier to set
     */
    public void setSimpleCreancier(SimpleCreancier simpleCreancier) {
        this.simpleCreancier = simpleCreancier;
    }

    /**
     * @param simplePCAccordee
     *            the simplePCAccordee to set
     */
    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    /**
     * @param simpleVersionDroit
     *            the simpleVersionDroit to set
     */
    public void setSimpleVersionDroit(SimpleVersionDroit simpleVersionDroit) {
        this.simpleVersionDroit = simpleVersionDroit;
    }

    @Override
    public void setSpy(String spy) {
        simpleCreanceAccordee.setSpy(spy);
    }
}
