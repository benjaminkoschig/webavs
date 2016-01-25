package ch.globaz.corvus.business.services.models.demande;

import java.util.Set;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.corvus.domaine.constantes.EtatDemandeRente;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * Regroupement de méthodes utilitaires pour les actions liées à la demande de rente ({@link DemandeRente})
 */
public interface DemandeRenteService {

    /**
     * Renvoi une copie de la demande passée en paramètre, mais dans l'état {@link EtatDemandeRente#ENREGISTRE} et sans
     * les rentes et les informations complémentaire
     * 
     * @param demande
     *            la demande à copie
     * @return la copie
     */
    public DemandeRente copier(DemandeRente demande);

    /**
     * <p>
     * Retourne les demandes de rente du requérant et de sa famille proche (enfant, conjoint actuel)
     * </p>
     * 
     * @param requerant
     *            le requérant
     * @return les demandes de la famille du requérant
     */
    public Set<DemandeRente> demandesDuRequerantEtDeSaFamille(PersonneAVS requerant);

    /**
     * <p>
     * Met à jour la période de la demande de rente en fonction des rentes accordées découlant de cette demande.
     * </p>
     * 
     * @param demande
     *            une demande de rente
     */
    public void mettreAJourLaPeriodeDeLaDemandeEnFonctionDesRentes(DemandeRente demande);

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
}
