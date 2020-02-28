package ch.globaz.al.business.services;

import ch.globaz.al.impotsource.persistence.TauxImpositionRepository;
import ch.globaz.al.impotsource.persistence.TauxImpositionRepositoryJade;
import ch.globaz.vulpecula.external.repositories.tiers.AdministrationRepository;
import ch.globaz.vulpecula.external.repositories.tiers.AdresseRepository;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.AdministrationRepositoryJade;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.AdresseRepositoryJade;

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

        private TauxImpositionRepositoryHolder() {
            throw new UnsupportedOperationException();
        }

        private static final TauxImpositionRepository INSTANCE = new TauxImpositionRepositoryJade();
    }

    /**
     * Retourne une implémentation de la classe {@link AdministrationRepository}
     *
     * @return Une instance de {@link AdministrationRepositoryJade}
     */
    public static AdministrationRepository getAdministrationRepository() {
        return AdministrationRepositoryHolder.INSTANCE;
    }

    private static class AdministrationRepositoryHolder {
        private AdministrationRepositoryHolder() {
            throw new UnsupportedOperationException();
        }

        private static final AdministrationRepository INSTANCE = new AdministrationRepositoryJade();
    }

    /**
     * Retourne une implémentation de la classe {@link AdresseRepository}
     *
     * @return Une instance de {@link AdresseRepositoryJade}
     */
    public static AdresseRepository getAdresseRepository() {
        return AdresseRepositoryHolder.INSTANCE;
    }

    private static class AdresseRepositoryHolder {
        private AdresseRepositoryHolder() {
            throw new UnsupportedOperationException();
        }

        private static final AdresseRepository INSTANCE = new AdresseRepositoryJade();
    }

}
