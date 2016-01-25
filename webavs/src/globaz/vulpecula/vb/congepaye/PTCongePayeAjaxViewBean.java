package globaz.vulpecula.vb.congepaye;

import java.util.List;
import ch.globaz.common.vb.JadeAbstractAjaxFindForDomain;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.businessimpl.services.prestations.PrestationStatus;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.repositories.Repository;

/**
 * ViewBean Ajax utilisée afin d'afficher les congés payés d'un travailleur.
 * 
 * @since WebBMS 0.01.04
 */
public class PTCongePayeAjaxViewBean extends JadeAbstractAjaxFindForDomain<CongePaye> {

    private static final long serialVersionUID = -2421832289586898272L;

    private String idTravailleur;

    private PrestationStatus prestationStatus = new PrestationStatus();

    /**
     * @return the congesPayes
     */
    public List<CongePaye> getCongesPayes() {
        return getList();
    }

    /**
     * @param idTravailleur the idTravailleur to set
     */
    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    /**
     * @return the idTravailleur
     */
    public String getIdTravailleur() {
        return idTravailleur;
    }

    @Override
    public CongePaye getEntity() {
        return new CongePaye();
    }

    @Override
    public Repository<CongePaye> getRepository() {
        return VulpeculaRepositoryLocator.getCongePayeRepository();
    }

    @Override
    public List<CongePaye> findByRepository() {
        return VulpeculaRepositoryLocator.getCongePayeRepository().findByIdTravailleurOrderByIdpassage(idTravailleur);
    }

    public boolean isSupprimable(CongePaye congePaye) {
        return prestationStatus.isModifiable(congePaye);
    }

    /**
     * Nécessaire pour WS
     */
    public boolean isSupprimable(Object object) {
        return isSupprimable((CongePaye) object);
    }
}
