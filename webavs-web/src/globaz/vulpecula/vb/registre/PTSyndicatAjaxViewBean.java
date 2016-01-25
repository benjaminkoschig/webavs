package globaz.vulpecula.vb.registre;

import globaz.framework.bean.JadeAbstractAjaxCrudFindViewBean;
import globaz.globall.db.BSessionUtil;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.CSTiers;

public class PTSyndicatAjaxViewBean extends
        JadeAbstractAjaxCrudFindViewBean<AdministrationComplexModel, AdministrationSearchComplexModel> {
    private static final long serialVersionUID = -3310193274161190876L;

    public PTSyndicatAjaxViewBean() {
        initList();
        currentEntity = new AdministrationComplexModel();
    }

    private AdministrationComplexModel currentEntity = null;
    private transient AdministrationSearchComplexModel searchModel = null;

    private String adresse = "";

    @Override
    public AdministrationComplexModel getCurrentEntity() {
        return currentEntity;
    }

    @Override
    public AdministrationSearchComplexModel getSearchModel() {
        searchModel.setForGenreAdministration(CSTiers.CS_TIERS_GENRE_ADMINISTRATION_SYNDICATS);
        return searchModel;
    }

    @Override
    public JadeCrudService<AdministrationComplexModel, AdministrationSearchComplexModel> getService()
            throws JadeApplicationServiceNotAvailableException {
        return VulpeculaServiceLocator.getAdministrationService();
    }

    @Override
    public void setCurrentEntity(final AdministrationComplexModel entite) {
        currentEntity = entite;
    }

    @Override
    public void setSearchModel(final AdministrationSearchComplexModel jadeAbstractSearchModel) {
        searchModel = jadeAbstractSearchModel;
    }

    @Override
    public void initList() {
        searchModel = new AdministrationSearchComplexModel();
    }

    @Override
    public void retrieve() throws Exception {
        super.retrieve();

        Adresse adresseDomaine = VulpeculaRepositoryLocator.getAdresseRepository()
                .findAdressePrioriteCourrierByIdTiers(getCurrentEntity().getId());

        if (adresseDomaine != null) {
            adresse = adresseDomaine.toString();
        } else {
            adresse = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_AUCUNE_ADRESSE");
        }
    }

    public String getAdresse() {
        return adresse;
    }
}
