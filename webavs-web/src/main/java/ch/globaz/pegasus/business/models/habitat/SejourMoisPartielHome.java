package ch.globaz.pegasus.business.models.habitat;

import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.home.TypeChambre;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public class SejourMoisPartielHome extends AbstractDonneeFinanciereModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private SimpleSejourMoisPartielHome simpleSejourMoisPartielHome = null;
    private TypeChambre typeChambre = null;

    public SejourMoisPartielHome() {
        super();
        typeChambre = new TypeChambre();
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

    public TypeChambre getTypeChambre() throws JadeApplicationServiceNotAvailableException, TypeChambreException, JadePersistenceException, HomeException {
        if (!JadeStringUtil.isEmpty(simpleSejourMoisPartielHome.getIdTypeChambre()) && typeChambre.getSimpleTypeChambre().getIdTypeChambre() == null ) {
            typeChambre = PegasusServiceLocator.getHomeService().readTypeChambre(simpleSejourMoisPartielHome.getIdTypeChambre());
        }
        return typeChambre;
    }

    public void setTypeChambre(TypeChambre typeChambre) {
        this.typeChambre = typeChambre;
    }
}
