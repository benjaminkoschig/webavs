package globaz.vulpecula.vb.servicemilitaire;

import java.util.List;
import ch.globaz.common.vb.JadeAbstractAjaxFindForDomain;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.businessimpl.services.prestations.PrestationStatus;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;
import ch.globaz.vulpecula.domain.repositories.Repository;

public class PTServicemilitaireAjaxViewBean extends JadeAbstractAjaxFindForDomain<ServiceMilitaire> {
    private static final long serialVersionUID = -8318917225760831422L;

    private PrestationStatus prestationStatus = new PrestationStatus();

    private String idTravailleur;

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public List<ServiceMilitaire> getServicesMilitaire() {
        return getList();
    }

    @Override
    public ServiceMilitaire getEntity() {
        return new ServiceMilitaire();
    }

    @Override
    public Repository<ServiceMilitaire> getRepository() {
        return VulpeculaRepositoryLocator.getServiceMilitaireRepository();
    }

    @Override
    public List<ServiceMilitaire> findByRepository() {
        return VulpeculaRepositoryLocator.getServiceMilitaireRepository().findByIdTravailleurOrderByIdpassage(
                idTravailleur);
    }

    public boolean isModifiable(ServiceMilitaire serviceMilitaire) {
        return prestationStatus.isModifiable(serviceMilitaire);
    }

    /**
     * Nécessaire pour WS
     */
    public boolean isModifiable(Object object) {
        return prestationStatus.isModifiable((ServiceMilitaire) object);
    }

}
