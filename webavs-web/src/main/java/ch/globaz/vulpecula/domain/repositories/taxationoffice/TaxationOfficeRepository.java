package ch.globaz.vulpecula.domain.repositories.taxationoffice;

import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface TaxationOfficeRepository extends Repository<TaxationOffice> {
    List<TaxationOffice> findByIdIn(Collection<String> ids);

    TaxationOffice findByIdDecompte(String idDecompte);

    List<TaxationOffice> getTaxationForFacturation(String idPassage);

    /**
     * Recherche de taxations d'office selon un passage de facturation, un numéro d'affilié ou un état de taxation.
     * Tous ces paramètres peuvent être null, retournant ainsi tous les cas.
     * 
     * @param idPassage Id d'un passage de facturation
     * @param numeroAffilie numéro d'affilié
     * @param etat Etat d'une taxation
     * @return Liste de taxations
     */
    List<TaxationOffice> findBy(String idPassage, String numeroAffilie, EtatTaxation etat);

    int findNbTOActives(Employeur employeur, Date dateDebut);

    /**
     * Recherche des taxations d'office non annulées pour un id employeur.
     * 
     * @param id employeur
     * @return Liste de taxations
     */
    List<TaxationOffice> findByIdEmployeur(String idEmployeur);

    /**
     * Recherche des taxations d'office avec l'état passé en paramètre.
     * 
     * @param etat Etat de taxation
     * @return Liste de taxations d'office
     */
    List<TaxationOffice> findByEtat(EtatTaxation etat);

    List<TaxationOffice> findByEtatIn(EtatTaxation... etat);

    List<TaxationOffice> findAnnuleForDate(Date date);

}
