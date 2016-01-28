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
     * Recherche de taxations d'office selon un passage de facturation, un num�ro d'affili� ou un �tat de taxation.
     * Tous ces param�tres peuvent �tre null, retournant ainsi tous les cas.
     * 
     * @param idPassage Id d'un passage de facturation
     * @param numeroAffilie num�ro d'affili�
     * @param etat Etat d'une taxation
     * @return Liste de taxations
     */
    List<TaxationOffice> findBy(String idPassage, String numeroAffilie, EtatTaxation etat);

    int findNbTOActives(Employeur employeur, Date dateDebut);

}
