package ch.globaz.vulpecula.business.services.registre;

import java.util.Map;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.utils.Pair;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.syndicat.ParametreSyndicat;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public interface ParametreSyndicatService {
    ParametreSyndicat create(ParametreSyndicat parametreSyndicat) throws UnsatisfiedSpecificationException;

    ParametreSyndicat update(ParametreSyndicat parametreSyndicat) throws UnsatisfiedSpecificationException;

    void delete(ParametreSyndicat parametreSyndicat) throws UnsatisfiedSpecificationException;

    /**
     * Retourne une map contenant le param�trage pour un syndicat et une caisse m�tier pour une ann�e.
     * Le premier param�tre de la pair est le syndicat, le second correspond � la caisse m�tier.
     * Si plusieurs param�tres existent pour la m�me ann�e, on prend alors celle qui a la plus date de d�but.
     * ATTENTION : Si le param�tre venait � changer en cours d'ann�e, celle-ci retournera une exception de param�trage
     * invalide.
     * 
     * @param annee Ann�e sur laquelle aller rechercher les syndicats.
     * @return Map contenant le param�trage pour un syst�me et sa caisse m�tier.
     */
    Map<Pair<Administration, Administration>, ParametreSyndicat> findParametresSyndicats(Annee annee);
}
