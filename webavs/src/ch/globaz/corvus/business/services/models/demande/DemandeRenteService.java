package ch.globaz.corvus.business.services.models.demande;

import java.util.Set;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.corvus.domaine.constantes.EtatDemandeRente;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * Regroupement de m�thodes utilitaires pour les actions li�es � la demande de rente ({@link DemandeRente})
 */
public interface DemandeRenteService {

    /**
     * Renvoi une copie de la demande pass�e en param�tre, mais dans l'�tat {@link EtatDemandeRente#ENREGISTRE} et sans
     * les rentes et les informations compl�mentaire
     * 
     * @param demande
     *            la demande � copie
     * @return la copie
     */
    public DemandeRente copier(DemandeRente demande);

    /**
     * <p>
     * Retourne les demandes de rente du requ�rant et de sa famille proche (enfant, conjoint actuel)
     * </p>
     * 
     * @param requerant
     *            le requ�rant
     * @return les demandes de la famille du requ�rant
     */
    public Set<DemandeRente> demandesDuRequerantEtDeSaFamille(PersonneAVS requerant);

    /**
     * <p>
     * Met � jour la p�riode de la demande de rente en fonction des rentes accord�es d�coulant de cette demande.
     * </p>
     * 
     * @param demande
     *            une demande de rente
     */
    public void mettreAJourLaPeriodeDeLaDemandeEnFonctionDesRentes(DemandeRente demande);

    /**
     * <p>
     * Retourne les rentes accord�es n�cessitant une diminution lors de la validation de cette demande.
     * </p>
     * <p>
     * Ces rentes accord�es seront recherch�es dans la famille du requ�rant de la demande, en ne retournant que les
     * rentes de la m�me famille que celle de la demande (soit API, soit AI / AVS)
     * </p>
     * 
     * @param demande
     *            la demande de rente
     * @return les rentes accord�es
     */
    public Set<RenteAccordee> rentesAccordeesDevantEtreDiminueesLorsDeLaValidationDeLaDemande(DemandeRente demande);
}
