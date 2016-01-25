package ch.globaz.vulpecula.repositoriesjade;

import globaz.jade.context.JadeThread;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

/**
 * @author Arnaud Geiser (AGE) | Créé le 4 avr. 2014
 * 
 */
public class StorePaginator {
    private static final String PAGINATOR = "jadePaginator";

    @SuppressWarnings("unchecked")
    public static <T extends JadeAbstractSearchModel> PaginationContainer getPaginator() {
        Object object = JadeThread.getTemporaryObject(PAGINATOR);
        if (object != null) {
            return (PaginationContainer) object;
        }
        return null;
    }

    public static <T> void storePaginator(PaginationContainer paginator) {
        JadeThread.storeTemporaryObject(PAGINATOR, paginator);
    }
}
