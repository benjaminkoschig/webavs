package globaz.pegasus.vb.home;

import globaz.framework.bean.FWAJAXFindInterface;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.home.TypeChambre;
import ch.globaz.pegasus.business.models.home.TypeChambreSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCTypeChambreAjaxViewBean extends BJadePersistentObjectViewBean implements FWAJAXViewBeanInterface,
        FWAJAXFindInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdHome = "0";// pour que la recherche ne retourn rien si
    private String forIdTier = "0";
    // le parmétre n'é pas fournit ;
    private boolean getListe = false;
    private transient PCTypeChambreAjaxListViewBean listTypeChambre = null;
    private TypeChambre typeChambre = null;

    public PCTypeChambreAjaxViewBean() {
        super();
        typeChambre = new TypeChambre();
    }

    public PCTypeChambreAjaxViewBean(TypeChambre typeChambre) {
        super();
        this.typeChambre = typeChambre;
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void find() throws Exception {
        updateListe();
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return typeChambre;
    }

    /**
     * @return the forIdHome
     */
    public String getForIdHome() {
        return forIdHome;
    }

    /**
     * @return the forIdTier
     */
    public String getForIdTier() {
        return forIdTier;
    }

    @Override
    public String getId() {
        return typeChambre.getId();
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listTypeChambre;
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return listTypeChambre.getTypeChambreSearch();
    }

    @Override
    public BSpy getSpy() {
        return (typeChambre != null) && !typeChambre.isNew() ? new BSpy(typeChambre.getSpy()) : new BSpy(
                (BSession) getISession());
    }

    /**
     * @return the typeChambre
     */
    public TypeChambre getTypeChambre() {
        return typeChambre;
    }

    @Override
    public boolean hasList() {
        return true;
    }

    @Override
    public void initList() {
        listTypeChambre = new PCTypeChambreAjaxListViewBean();

    }

    /**
     * @return the getListe
     */
    public boolean isGetListe() {
        return getListe;
    }

    @Override
    public Iterator iterator() {
        return listTypeChambre.iterator();
    }

    @Override
    public void retrieve() throws Exception {
        // this.updateListe();
        if (!JadeStringUtil.isEmpty(typeChambre.getId())) {
            typeChambre = PegasusServiceLocator.getHomeService().readTypeChambre(typeChambre.getId());
        }
    }

    @Override
    public void setDefinedSearchSize(int definedSearchSize) {
        // TODO Auto-generated method stub
    }

    /**
     * @param forIdHome the forIdHome to set
     */
    public void setForIdHome(String forIdHome) {
        this.forIdHome = forIdHome;
    }

    /**
     * @param forIdTier the forIdTier to set
     */
    public void setForIdTier(String forIdTier) {
        this.forIdTier = forIdTier;
    }

    @Override
    public void setGetListe(boolean getListe) {
        this.getListe = getListe;
    }

    @Override
    public void setId(String newId) {
        typeChambre.setId(newId);
    }

    /**
     * @param listTypeChambre the listTypeChambre to set
     */
    public void setListTypeChambre(PCTypeChambreAjaxListViewBean listTypeChambre) {
        this.listTypeChambre = listTypeChambre;
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listTypeChambre = (PCTypeChambreAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#setOffset(int)
     */
    @Override
    public void setOffset(int offset) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.framework.bean.FWAJAXFindInterface#setSearchModel(globaz.jade.persistence.model.JadeAbstractSearchModel)
     */
    public void setSearchModel(JadeAbstractSearchModel search) {
        listTypeChambre.setTypeChambreSearch((TypeChambreSearch) search);
    }

    /**
     * @param typeChambre the typeChambre to set
     */
    public void setTypeChambre(TypeChambre typeChambre) {
        this.typeChambre = typeChambre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub
    }

    public void updateListe() throws Exception {
        listTypeChambre = new PCTypeChambreAjaxListViewBean();
        if (!JadeStringUtil.isEmpty(forIdHome)) {
            TypeChambreSearch search = new TypeChambreSearch();
            search.setForIdHome(forIdHome);
            search.setForIdTiersParticularite(forIdTier);
            search.setForHomeTypeAdresse(PRTiersHelper.CS_ADRESSE_DOMICILE);
            search.setWhereKey("forTierParticularite");
            listTypeChambre.setTypeChambreSearch(search);
            listTypeChambre.find();
        }
    }
}
