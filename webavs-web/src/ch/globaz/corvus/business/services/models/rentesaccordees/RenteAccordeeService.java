package ch.globaz.corvus.business.services.models.rentesaccordees;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.Set;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.prestation.domaine.constantes.EtatPrestationAccordee;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * Regroupement de m�thodes utilitaires pour les rentes accord�es
 */
public interface RenteAccordeeService extends JadeApplicationService {

    /**
     * Permet de d�tacher cette rente de la demande sur laquelle elle est actuellement li�e, et la rattacher � la
     * demande pass�e en param�tre
     * 
     * @param rente
     *            la rente qu'on aimerait rattacher � une autre demande
     * @param demande
     *            la demande sur laquelle on aimerait rattacher la rente
     * @throw {@link IllegalArgumentException} si la rente ou la demande pass�es en param�tre sont null ou non
     *        initialis�es (sans ID)
     */
    public void rattacherLaRenteSurLaDemande(RenteAccordee rente, DemandeRente demande);

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

    /**
     * Retourne les rentes accord�es en cours (�tat {@link EtatPrestationAccordee#VALIDE valid�} ou
     * {@link EtatPrestationAccordee#PARTIEL partiel}, sans date de fin de droit avec un montant diff�rent de z�ro) de
     * la famille du requ�rant, lui compris
     * 
     * @param requerant
     *            le requ�rant
     * @return les rentes de la famille du requ�rant (lui compris) ou une liste vide si aucune rente trouv�e
     */
    public Set<RenteAccordee> rentesAccordeesEnCoursDeLaFamille(PersonneAVS requerant);
}
