package ch.globaz.pegasus.business;

import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce.AnnonceRepositoryJade;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce.AnnonceRpcRepository;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce.RetourAnnonceRepository;

public final class PegasusRepositoryLocator {
    /**
     * La classe ne peut pas être instanciée.
     */
    private PegasusRepositoryLocator() {
        throw new UnsupportedOperationException();
    }

    public static AnnonceRpcRepository getAnnonceRepository() {
        return AnnonceRpcRepositoryHolder.INSTANCE;
    }

    /*
     * Repository holders - to avoid loading all repositories at once -
     * following the "initialization on demand holder idiom" see
     * http://en.wikipedia.org/wiki/Singleton_pattern#The_solution_of_Bill_Pugh
     */
    private static class AnnonceRpcRepositoryHolder {
        private static final AnnonceRpcRepository INSTANCE = new AnnonceRepositoryJade();
    }

    public static RetourAnnonceRepository getRetourAnnonceRepository() {
        return RetourAnnonceRepositoryHolder.INSTANCE;
    }

    /*
     * Repository holders - to avoid loading all repositories at once -
     * following the "initialization on demand holder idiom" see
     * http://en.wikipedia.org/wiki/Singleton_pattern#The_solution_of_Bill_Pugh
     */
    private static class RetourAnnonceRepositoryHolder {
        private static final RetourAnnonceRepository INSTANCE = new RetourAnnonceRepository();
    }

}
