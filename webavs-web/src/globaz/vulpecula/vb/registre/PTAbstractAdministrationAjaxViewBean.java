package globaz.vulpecula.vb.registre;

import globaz.framework.bean.JadeAbstractAjaxCrudFindViewBean;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;

/**
 * Classe parente à tous les viewBean ajax pointant sur une administration.
 * 
 * @author sel 4 févr. 2014
 * @since Web@BMS 0.01.01
 */
public abstract class PTAbstractAdministrationAjaxViewBean extends
        JadeAbstractAjaxCrudFindViewBean<AdministrationComplexModel, AdministrationSearchComplexModel> {

    protected AdministrationComplexModel currentEntity = null;
    protected transient AdministrationSearchComplexModel searchModel = null;

    /**
     * Retourne la destination par défaut qui sera l'atteinte lors de la
     * validation du formaulaire.
     * 
     * @param idEntity
     *            id de l'entity à afficher
     * @return String représentant la destination
     */
    public String getDestination(final String idEntity) {
        return "";
    }

    /**
     * Adresse de courrier de l'administration
     * 
     * Utiliser dans les écrans des registres
     */
    private String adresse = "";

    /**
     * Permet de filtrer les administrations avec le genre soughaité tel que :
     * section, syndicat, assureur maladie, convention, ...
     */
    private String genreAdministration;

    /**
     * @param genreAdministration
     *            genre de l'administration que l'on souhaite afficher (section,
     *            syndicat, assureur maladie, ...).
     */
    public PTAbstractAdministrationAjaxViewBean(final String genreAdministration) {
        initList();
        currentEntity = new AdministrationComplexModel();
        this.genreAdministration = genreAdministration;
    }

    @Override
    public AdministrationComplexModel getCurrentEntity() {
        return currentEntity;
    }

    @Override
    public AdministrationSearchComplexModel getSearchModel() {
        searchModel.setForGenreAdministration(genreAdministration);
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
            adresse = "Aucune adresse trouvée !"; // TODO Label
        }
    }

    @Override
    public void update() throws Exception {
    }

    public String getAdresse() {
        return adresse;
    }
}
