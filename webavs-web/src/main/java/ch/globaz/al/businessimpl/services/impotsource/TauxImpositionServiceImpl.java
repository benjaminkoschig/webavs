package ch.globaz.al.businessimpl.services.impotsource;

import ch.globaz.al.business.services.impotsource.TauxImpositionService;
import ch.globaz.al.impotsource.persistence.TauxImpositionRepository;
import ch.globaz.specifications.Specification;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.al.impotsource.domain.TauxImposition;
import ch.globaz.al.impotsource.domain.TauxImpositions;
import ch.globaz.al.impotsource.utils.TIPeriodesChevauchantes;

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
