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
     * Retourne une map contenant le paramétrage pour un syndicat et une caisse métier pour une année.
     * Le premier paramètre de la pair est le syndicat, le second correspond à la caisse métier.
     * Si plusieurs paramètres existent pour la même année, on prend alors celle qui a la plus date de début.
     * ATTENTION : Si le paramètre venait à changer en cours d'année, celle-ci retournera une exception de paramétrage
     * invalide.
     * 
     * @param annee Année sur laquelle aller rechercher les syndicats.
     * @return Map contenant le paramétrage pour un système et sa caisse métier.
     */
    Map<Pair<Administration, Administration>, ParametreSyndicat> findParametresSyndicats(Annee annee);
}
