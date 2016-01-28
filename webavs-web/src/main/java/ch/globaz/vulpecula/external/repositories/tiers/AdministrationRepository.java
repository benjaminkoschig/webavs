/**
 * 
 */
package ch.globaz.vulpecula.external.repositories.tiers;

import java.util.List;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

/**
 * Repository permettant l'acc�s aux diff�rentes administrations (tiers) li�es
 * au BMS tel que : syndicat, section, association professionnelle, assureur
 * maladie, convention, caisse m�tier, caisse prof et caisse sociale
 * 
 * @author sel
 * 
 */
public interface AdministrationRepository {

    List<Administration> findAllAssociationsProfessionnelles();

    Administration findById(String id);

    List<Administration> findAllCaissesMaladies();

    List<Administration> findAllSyndicats();

    List<Administration> findAllCaissesMetiers();

    /**
     * @return toutes les conventions dans tiers administration
     */
    List<Administration> findAllConventions();

    List<Administration> findAllCaissesAF();
}
