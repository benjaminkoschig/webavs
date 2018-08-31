package ch.globaz.vulpecula.business.services.syndicat;

import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.syndicat.AffiliationSyndicat;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public interface AffiliationSyndicatService {
    /**
     * Recherche des affiliations avec le cumul salaire calculé groupé par caisse métier.
     * Si l'idSyndicat est null, toutes les affiliations aux syndicats seront retournées. Dans le cas contraire, seules
     * les affiliations appartenant à ce syndicat sont retournées.
     * Si l'id caisseMetier est null, toutes les affiliations pour cette caisse métier seront retournées. Dans le cas
     * contraire, le map de retour ne contiendra qu'une seule entrée.
     *
     * @param idSyndicat String représentant un id de syndicat
     * @param idCaisseMetier String représentant un id de caisse métier
     * @param annee Année sur laquelle rechercher les affiliations
     * @return Map contenant des affiliations par syndicat.
     */
    Map<Administration, List<AffiliationSyndicat>> findByAnneeWithCumulSalairesGroupByCaisseMetier(String idSyndicat,
            String idCaisseMetier, Annee annee, List<String> listeErreur, String idTravaillleur);

    /**
     * Recherche des affiliations avec le cumul salaire calculé groupé par caisse métier.
     * Si l'idSyndicat est null, toutes les affiliations aux syndicats seront retournées. Dans le cas contraire, seules
     * les affiliations appartenant à ce syndicat sont retournées.
     * Si l'id caisseMetier est null, toutes les affiliations pour cette caisse métier seront retournées. Dans le cas
     * contraire, seule cette caisse métier sera présente.
     * Le premier paramètre de la map est un Syndicat, et le second une caisse métier.
     *
     * @param idSyndicat String représentant l'id d'un syndicat
     * @param idCaisseMetier String représentant l'id d'une caisse métier
     * @param annee Année pour sélectionner les affiliations
     * @param listeErreur
     * @param idTravailleur
     * @param isListeTravailleurPaiementSyndicat
     */
    Map<Administration, Map<Administration, List<AffiliationSyndicat>>> findByAnneeWithCumulSalaireGroupBySyndicatAndCaisseMetier(
            String idSyndicat, String idCaisseMetier, Annee annee, List<String> listeErreur, String idTravailleur);

    /**
     * Recherche des travailleurs qui n'ont pas de syndicats actifs pour l'année passé en paramètre.
     * Le premier paramètre de la map est un Syndicat, et le second une caisse métier.
     *
     * @param annee Année pour sélectionner les affiliations
     */
    List<Travailleur> findTravailleursSansSyndicats(Annee annee);

}
