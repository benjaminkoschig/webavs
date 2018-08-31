package ch.globaz.vulpecula.business.services.syndicat;

import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.syndicat.AffiliationSyndicat;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public interface AffiliationSyndicatService {
    /**
     * Recherche des affiliations avec le cumul salaire calcul� group� par caisse m�tier.
     * Si l'idSyndicat est null, toutes les affiliations aux syndicats seront retourn�es. Dans le cas contraire, seules
     * les affiliations appartenant � ce syndicat sont retourn�es.
     * Si l'id caisseMetier est null, toutes les affiliations pour cette caisse m�tier seront retourn�es. Dans le cas
     * contraire, le map de retour ne contiendra qu'une seule entr�e.
     *
     * @param idSyndicat String repr�sentant un id de syndicat
     * @param idCaisseMetier String repr�sentant un id de caisse m�tier
     * @param annee Ann�e sur laquelle rechercher les affiliations
     * @return Map contenant des affiliations par syndicat.
     */
    Map<Administration, List<AffiliationSyndicat>> findByAnneeWithCumulSalairesGroupByCaisseMetier(String idSyndicat,
            String idCaisseMetier, Annee annee, List<String> listeErreur, String idTravaillleur);

    /**
     * Recherche des affiliations avec le cumul salaire calcul� group� par caisse m�tier.
     * Si l'idSyndicat est null, toutes les affiliations aux syndicats seront retourn�es. Dans le cas contraire, seules
     * les affiliations appartenant � ce syndicat sont retourn�es.
     * Si l'id caisseMetier est null, toutes les affiliations pour cette caisse m�tier seront retourn�es. Dans le cas
     * contraire, seule cette caisse m�tier sera pr�sente.
     * Le premier param�tre de la map est un Syndicat, et le second une caisse m�tier.
     *
     * @param idSyndicat String repr�sentant l'id d'un syndicat
     * @param idCaisseMetier String repr�sentant l'id d'une caisse m�tier
     * @param annee Ann�e pour s�lectionner les affiliations
     * @param listeErreur
     * @param idTravailleur
     * @param isListeTravailleurPaiementSyndicat
     */
    Map<Administration, Map<Administration, List<AffiliationSyndicat>>> findByAnneeWithCumulSalaireGroupBySyndicatAndCaisseMetier(
            String idSyndicat, String idCaisseMetier, Annee annee, List<String> listeErreur, String idTravailleur);

    /**
     * Recherche des travailleurs qui n'ont pas de syndicats actifs pour l'ann�e pass� en param�tre.
     * Le premier param�tre de la map est un Syndicat, et le second une caisse m�tier.
     *
     * @param annee Ann�e pour s�lectionner les affiliations
     */
    List<Travailleur> findTravailleursSansSyndicats(Annee annee);

}
