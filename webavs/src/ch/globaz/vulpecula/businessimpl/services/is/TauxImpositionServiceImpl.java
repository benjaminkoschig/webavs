package ch.globaz.vulpecula.businessimpl.services.is;

import ch.globaz.specifications.Specification;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.is.TauxImpositionService;
import ch.globaz.vulpecula.domain.models.is.TauxImposition;
import ch.globaz.vulpecula.domain.models.is.TauxImpositions;
import ch.globaz.vulpecula.domain.repositories.is.TauxImpositionRepository;
import ch.globaz.vulpecula.domain.specifications.is.TIPeriodesChevauchantes;

public class TauxImpositionServiceImpl implements TauxImpositionService {

    private TauxImpositionRepository repository;

    public TauxImpositionServiceImpl(TauxImpositionRepository repository) {
        this.repository = repository;
    }

    @Override
    public TauxImposition create(TauxImposition tauxImposition) throws UnsatisfiedSpecificationException {
        validate(tauxImposition);
        return repository.create(tauxImposition);
    }

    @Override
    public TauxImposition update(TauxImposition tauxImposition) throws UnsatisfiedSpecificationException {
        validate(tauxImposition);
        return repository.update(tauxImposition);
    }

    private void validate(TauxImposition tauxImposition) throws UnsatisfiedSpecificationException {
        tauxImposition.validate();

        TauxImpositions tauxImpositions = repository.findAll(tauxImposition.getTypeImposition());
        Specification<TauxImposition> spec = new TIPeriodesChevauchantes(tauxImpositions);
        spec.isSatisfiedBy(tauxImposition);
    }
}
