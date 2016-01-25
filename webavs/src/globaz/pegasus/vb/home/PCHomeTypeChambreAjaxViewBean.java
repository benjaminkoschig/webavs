package globaz.pegasus.vb.home;

import globaz.framework.bean.JadeAbstractAjaxFindViewBean;
import globaz.globall.db.BSession;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.models.home.TypeChambre;
import ch.globaz.pegasus.business.models.home.TypeChambreSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;

public class PCHomeTypeChambreAjaxViewBean extends JadeAbstractAjaxFindViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Home home = null;
    private String idHome = null;
    public transient PCTypeChambreListViewBean list = null;
    private TypeChambre typeChambre = null;

    public PCHomeTypeChambreAjaxViewBean() {
        super();
        typeChambre = new TypeChambre();
        initList();
    }

    public String getIsLvpc() throws PropertiesException {
        return PCproperties.getBoolean(EPCProperties.LVPC).toString();
    }

    public String getLibelle(String libelle) {
        return ((BSession) getISession()).getCodeLibelle(libelle);
    }

    @Override
    public void add() throws Exception {
        typeChambre = PegasusServiceLocator.getHomeService().createTypeChambre(typeChambre);
    }

    @Override
    public void delete() throws Exception {
        typeChambre = PegasusServiceLocator.getHomeService().deleteTypeChambre(typeChambre);
    }

    @Override
    public void find() throws Exception {
        list.find();
        readeHome();
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return typeChambre;
    }

    public Home getHome() {
        return home;
    }

    public String getIdHome() {
        return idHome;
    }

    public String getNomHome() {
        return home.getAdresse().getTiers().getDesignation1() + " " + home.getAdresse().getTiers().getDesignation2();
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return list.getTypeChambreSearch();
    }

    public TypeChambre getTypeChambre() {
        return typeChambre;
    }

    @Override
    public void initList() {
        list = new PCTypeChambreListViewBean();
    }

    private void readeHome() throws HomeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        setHome(PegasusServiceLocator.getHomeService().read(((TypeChambreSearch) getSearchModel()).getForIdHome()));
    }

    @Override
    public void retrieve() throws Exception {
        typeChambre = PegasusServiceLocator.getHomeService().readTypeChambre(typeChambre.getId());
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public void setIdHome(String idHome) {
        this.idHome = idHome;
    }

    public void setTypeChambre(TypeChambre typeChambre) {
        this.typeChambre = typeChambre;
    }

    @Override
    public void update() throws Exception {
        typeChambre = PegasusServiceLocator.getHomeService().updateTypeChambre(typeChambre);
    }

}
