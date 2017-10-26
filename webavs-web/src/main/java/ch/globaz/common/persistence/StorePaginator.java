package ch.globaz.common.persistence;

import globaz.jade.context.JadeThread;

public class StorePaginator {
    private static final String PAGINATOR = "jadePaginator";

    @SuppressWarnings("unchecked")
    public static PaginationContainer getPaginator() {
        Object object = JadeThread.getTemporaryObject(PAGINATOR);
        if (object != null) {
            return (PaginationContainer) object;
        }
        return null;
    }

    public static void storePaginator(PaginationContainer paginator) {
        JadeThread.storeTemporaryObject(PAGINATOR, paginator);
    }
}
