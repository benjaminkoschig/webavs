package ch.globaz.pegasus.business.models.habitat;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

public class SejourMoisPartielHome extends AbstractDonneeFinanciereModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private SimpleSejourMoisPartielHome simpleSejourMoisPartielHome = null;

    public SejourMoisPartielHome() {
        super();
        simpleSejourMoisPartielHome = new SimpleSejourMoisPartielHome();
    }

    @Override
    public String getId() {
        return simpleSejourMoisPartielHome.getId();
    }

    /**
     * @return the simpleSejourMoisPartielHome
     */
    public SimpleSejourMoisPartielHome getSimpleSejourMoisPartielHome() {
        return simpleSejourMoisPartielHome;
    }

    @Override
    public String getSpy() {
        return simpleSejourMoisPartielHome.getSpy();
    }


    @Override
    public void setId(String id) {
        simpleSejourMoisPartielHome.setId(id);
    }

    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader donneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(donneeFinanciereHeader);
        simpleSejourMoisPartielHome.setIdDonneeFinanciereHeader(donneeFinanciereHeader.getId());
    }

    /**
     * @param simpleSejourMoisPartielHome
     *            the simpleSejourMoisPartielHome to set
     */
    public void setSimpleSejourMoisPartielHome(SimpleSejourMoisPartielHome simpleSejourMoisPartielHome) {
        this.simpleSejourMoisPartielHome = simpleSejourMoisPartielHome;
    }

    @Override
    public void setSpy(String spy) {
        simpleSejourMoisPartielHome.setSpy(spy);
    }


}
