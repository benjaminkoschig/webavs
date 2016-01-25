package ch.globaz.pegasus.business.models.renteijapi;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

public class AutreApi extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleAutreApi simpleAutreApi = null;

    public AutreApi() {
        super();
        simpleAutreApi = new SimpleAutreApi();
    }

    @Override
    public String getId() {
        return simpleAutreApi.getId();
    }

    /**
     * @return the simpleAutreApi
     */
    public SimpleAutreApi getSimpleAutreApi() {
        return simpleAutreApi;
    }

    @Override
    public String getSpy() {
        return simpleAutreApi.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleAutreApi.setId(id);
    }

    /**
     * @param simpleAutreApi
     *            the simpleAutreApi to set
     */
    public void setSimpleAutreApi(SimpleAutreApi simpleAutreApi) {
        this.simpleAutreApi = simpleAutreApi;
    }

    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader donneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(donneeFinanciereHeader);
        simpleAutreApi.setIdDonneeFinanciereHeader(donneeFinanciereHeader.getId());
    }

    /**
     * @param simpleAutreApi
     *            the simpleAutreRente to set
     */

    @Override
    public void setSpy(String spy) {
        simpleAutreApi.setSpy(spy);
    }

}
