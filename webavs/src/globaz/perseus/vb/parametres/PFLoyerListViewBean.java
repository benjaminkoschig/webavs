package globaz.perseus.vb.parametres;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.parametres.Loyer;
import ch.globaz.perseus.business.models.parametres.LoyerSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFLoyerListViewBean extends BJadePersistentObjectListViewBean {
    private LoyerSearchModel loyerSearchModel = null;

    public PFLoyerListViewBean() {
        super();
        loyerSearchModel = new LoyerSearchModel();
    }

    @Override
    public void find() throws Exception {
        loyerSearchModel = PerseusServiceLocator.getLoyerService().search(loyerSearchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < loyerSearchModel.getSize() ? new PFLoyerViewBean((Loyer) loyerSearchModel.getSearchResults()[idx])
                : new PFLoyerViewBean();
    }

    /**
     * @return the forDateValableConverter
     */
    public String getForDateValableConverter() {
        return loyerSearchModel.getForDateValable();
    }

    public LoyerSearchModel getLoyerSearchModel() {
        return loyerSearchModel;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return loyerSearchModel;
    }

    public LoyerSearchModel getSearchModel() {
        return loyerSearchModel;
    }

    /**
     * Recoit une date au format mm.yyyy et la converti en 01.mm.yyyy
     * 
     * @param forDateValableConverter
     *            the forDateValableConverter to set
     */
    public void setForDateValableConverter(String forDateValableConverter) {
        if (!JadeStringUtil.isEmpty(forDateValableConverter)) {
            forDateValableConverter = "01." + forDateValableConverter;
            loyerSearchModel.setForDateValable(forDateValableConverter);
        }
    }

    public void setLoyerSearchModel(LoyerSearchModel loyerSearchModel) {
        this.loyerSearchModel = loyerSearchModel;
    }

}
