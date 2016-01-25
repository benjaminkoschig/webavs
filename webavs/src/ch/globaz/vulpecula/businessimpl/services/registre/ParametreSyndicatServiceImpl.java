package ch.globaz.vulpecula.businessimpl.services.registre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.utils.Pair;
import ch.globaz.vulpecula.business.services.registre.ParametreSyndicatService;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.syndicat.ParametreSyndicat;
import ch.globaz.vulpecula.domain.repositories.syndicat.ParametreSyndicatRepository;
import ch.globaz.vulpecula.domain.specifications.registre.PSChevauchementPeriodes;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class ParametreSyndicatServiceImpl implements ParametreSyndicatService {
    private ParametreSyndicatRepository parametreSyndicatRepository;

    public ParametreSyndicatServiceImpl(ParametreSyndicatRepository parametreSyndicatRepository) {
        this.parametreSyndicatRepository = parametreSyndicatRepository;
    }

    @Override
    public ParametreSyndicat create(ParametreSyndicat parametreSyndicat) throws UnsatisfiedSpecificationException {
        validate(parametreSyndicat);
        return parametreSyndicatRepository.create(parametreSyndicat);
    }

    @Override
    public ParametreSyndicat update(ParametreSyndicat parametreSyndicat) throws UnsatisfiedSpecificationException {
        validate(parametreSyndicat);
        return parametreSyndicatRepository.update(parametreSyndicat);
    }

    @Override
    public void delete(ParametreSyndicat parametreSyndicat) throws UnsatisfiedSpecificationException {
        parametreSyndicatRepository.delete(parametreSyndicat);
    }

    public void validate(ParametreSyndicat parametreSyndicat) throws UnsatisfiedSpecificationException {
        parametreSyndicat.validate();
        PSChevauchementPeriodes psChevauchementPeriodes = new PSChevauchementPeriodes(
                parametreSyndicatRepository.findByIdSyndicat(parametreSyndicat.getIdSyndicat(),
                        parametreSyndicat.getIdCaisseMetier()));
        psChevauchementPeriodes.isSatisfiedBy(parametreSyndicat);
    }

    @Override
    public Map<Pair<Administration, Administration>, ParametreSyndicat> findParametresSyndicats(Annee annee) {
        Map<Pair<Administration, Administration>, ParametreSyndicat> configuration = new HashMap<Pair<Administration, Administration>, ParametreSyndicat>();
        List<ParametreSyndicat> parametres = parametreSyndicatRepository.findForYear(annee);
        for (ParametreSyndicat parametreSyndicat : parametres) {
            Administration caisseMetier = parametreSyndicat.getCaisseMetier();
            Administration syndicat = parametreSyndicat.getSyndicat();
            Pair<Administration, Administration> pair = new Pair<Administration, Administration>(syndicat, caisseMetier);
            if (configuration.containsKey(pair)) {
                throw new GlobazTechnicalException(ExceptionMessage.PARAMETRE_SYNDICAT_INVALIDE_POUR_ANNEE);
            } else {
                configuration.put(pair, parametreSyndicat);
            }
        }
        return configuration;
    }
}
