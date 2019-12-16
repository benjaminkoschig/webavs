package ch.globaz.al.business.services;

import ch.globaz.al.impotsource.persistence.TauxImpositionRepository;
import ch.globaz.al.impotsource.persistence.TauxImpositionRepositoryJade;

public final class ALRepositoryLocator {

    /**
     * La classe ne peut pas être instanciée.
     */
    private ALRepositoryLocator() {
        throw new UnsupportedOperationException();
    }


    public static TauxImpositionRepository getTauxImpositionRepository() {
        return TauxImpositionRepositoryHolder.INSTANCE;
    }

    private static class TauxImpositionRepositoryHolder {
        private static final TauxImpositionRepository INSTANCE = new TauxImpositionRepositoryJade();
    }
}
