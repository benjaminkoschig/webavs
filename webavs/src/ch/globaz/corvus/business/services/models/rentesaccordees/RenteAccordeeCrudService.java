package ch.globaz.corvus.business.services.models.rentesaccordees;

import ch.globaz.common.business.services.CrudService;
import ch.globaz.corvus.domaine.RenteAccordee;

public interface RenteAccordeeCrudService extends CrudService<RenteAccordee> {

    /**
     * <p>
     * Il est actuellement possible de mettre à jour, par ce service, les données suivantes :
     * <ul>
     * <li>l'état de la rente
     * </ul>
     * </p>
     */
    @Override
    public RenteAccordee update(RenteAccordee objetDeDomaine);
}
