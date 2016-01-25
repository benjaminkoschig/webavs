package ch.globaz.corvus.businessimpl.services.models;

import ch.globaz.common.business.services.CrudService;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;

/**
 * Canevas pour les services de CRUD. Permet d'éviter de ré-implémenter les doubles méthodes read et delete à chaque
 * fois
 * 
 * @param <T>
 *            une entité métier
 */
public abstract class CanevasCrudService<T extends EntiteDeDomaine> implements CrudService<T> {

    @Override
    public final boolean delete(T objetDeDomaine) {
        Checkers.checkNotNull(objetDeDomaine, "objetDeDomaine");
        Checkers.checkHasID(objetDeDomaine, "objetDeDomaine");
        return this.delete(objetDeDomaine.getId());
    };

    @Override
    public final T read(T objetDeDomaine) {
        Checkers.checkNotNull(objetDeDomaine, "objetDeDomaine");
        Checkers.checkHasID(objetDeDomaine, "objetDeDomaine");
        return this.read(objetDeDomaine.getId());
    }
}
