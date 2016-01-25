package ch.globaz.corvus.business.services.models.rentesaccordees;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.Set;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.prestation.domaine.constantes.EtatPrestationAccordee;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * Regroupement de méthodes utilitaires pour les rentes accordées
 */
public interface RenteAccordeeService extends JadeApplicationService {

    /**
     * Permet de détacher cette rente de la demande sur laquelle elle est actuellement liée, et la rattacher à la
     * demande passée en paramètre
     * 
     * @param rente
     *            la rente qu'on aimerait rattacher à une autre demande
     * @param demande
     *            la demande sur laquelle on aimerait rattacher la rente
     * @throw {@link IllegalArgumentException} si la rente ou la demande passées en paramètre sont null ou non
     *        initialisées (sans ID)
     */
    public void rattacherLaRenteSurLaDemande(RenteAccordee rente, DemandeRente demande);

    /**
     * <p>
     * Retourne les rentes accordées nécessitant une diminution lors de la validation de cette demande.
     * </p>
     * <p>
     * Ces rentes accordées seront recherchées dans la famille du requérant de la demande, en ne retournant que les
     * rentes de la même famille que celle de la demande (soit API, soit AI / AVS)
     * </p>
     * 
     * @param demande
     *            la demande de rente
     * @return les rentes accordées
     */
    public Set<RenteAccordee> rentesAccordeesDevantEtreDiminueesLorsDeLaValidationDeLaDemande(DemandeRente demande);

    /**
     * Retourne les rentes accordées en cours (état {@link EtatPrestationAccordee#VALIDE validé} ou
     * {@link EtatPrestationAccordee#PARTIEL partiel}, sans date de fin de droit avec un montant différent de zéro) de
     * la famille du requérant, lui compris
     * 
     * @param requerant
     *            le requérant
     * @return les rentes de la famille du requérant (lui compris) ou une liste vide si aucune rente trouvée
     */
    public Set<RenteAccordee> rentesAccordeesEnCoursDeLaFamille(PersonneAVS requerant);
}
