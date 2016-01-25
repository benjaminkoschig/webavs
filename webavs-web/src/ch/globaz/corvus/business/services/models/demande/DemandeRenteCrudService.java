package ch.globaz.corvus.business.services.models.demande;

import ch.globaz.common.business.services.CrudService;
import ch.globaz.corvus.domaine.DemandeRente;

/**
 * Contrat pour un service de persistance des demandes de rentes
 */
public interface DemandeRenteCrudService extends CrudService<DemandeRente> {
    /**
     * <p>
     * les seuls éléments pouvant être modifiés actuellement par ce service sur la demande de rente sont :
     * <ul>
     * <li>l'état de la demande
     * </ul>
     * </p>
     */
    @Override
    public DemandeRente update(DemandeRente objetDeDomaine);
}
