/**
 *
 */
package ch.globaz.vulpecula.external.repositories.tiers;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.AvoirAdressePaiement;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;

/**
 * Définition des méthodes pour le repository relatif à la classe {@link Adresse}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 23 déc. 2013
 * 
 */
public interface AdresseRepository {
    /**
     * Retourne une adresse selon l'id du tiers passé en paramètres
     * 
     * @param idTiers
     *            id du tiers
     * @return {@link Adresse} principale du tiers ou null
     */
    Adresse findAdressePrioriteCourrierByIdTiers(String idTiers);

    Adresse findAdresseDomicileByIdTiers(String idTiers);

    Adresse findAdresseCourrierByIdTiers(String idTiers, CodeLangue langue);

    /**
     * Recherche d'une adresse paiement en fonction de l'idTiers de la date et d'une liste
     * d'application passé en paramètre.
     * L'adresse de paiement prise en prioritaire sera en fonction de l'ordre des éléments dans la liste des
     * applications.
     * 
     * @param idTiers String représentant un idTiers
     * @param date Date à laquelle rechercher les dates
     * @param applications Liste d'id application
     * @return
     */
    AvoirAdressePaiement findByIdTiers(String idTiers, Date date, List<String> applications);

    /**
     * Recherche d'une adresse de paiement dans le cas d'un versement de prestations
     */
    AvoirAdressePaiement findForPrestations(String idTiers, Date date);

    Adresse findAdresseDomicileByIdTiersForCT(String idTiers);

    Adresse findAdresseCourrierByIdTiersForCT(String idTiers);

    String findAdressePaiementByIdTiers(final String idTiers, Date date);

    /**
     * @param idTiers
     * @param date de référence
     * @return l'adresse de domicile pour une date ou null
     */
    Adresse findAdresseDomicileByIdTiersAndDate(String idTiers, Date date);

    /**
     * @param idTiers
     * @param date de référence
     * @return l'adresse de courrier pour une date ou null
     */
    Adresse findAdresseCourrierByIdTiersAndDate(String idTiers, Date date);

    String findIbanByIdTiers(String idTiers, Date date);

    String findNomBanqueByIdTiers(String idTiers, Date date);

    String findLocaliteBanqueByIdTiers(String idTiers, Date date);

    String findIdLocaliteBanqueByIdTiers(String idTiers, Date date);
}
